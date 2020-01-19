package com.ebig.hdi.common.matching.rules;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;

import com.ebig.hdi.common.utils.StringSemArithUtil;
import com.ebig.hdi.common.utils.TableEntityUtils;

import cn.hutool.core.bean.BeanUtil;
import lombok.Getter;

/**
 * 匹配规则执行者
 * 
 * @author zack
 *
 */
@Component("matchingRulesExecutor")
public class MatchingRulesExecutor {

	@Autowired
	private MatchingRulesParser matchingRulesParser;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final String SIMILARITY="similarity";

	/**
	 * 根据记录对象，规则匹配
	 * 
	 * @param t
	 *            用于比较的原记录对象
	 * @param rules
	 *            规则信息
	 * @return 如果最终匹配的类型为相等，则返回目标表的一条记录(包含所有字段)
	 *         如果是相似，则返回目标表的多条记录（包含所有字段），并增加列：相似度(similarity)
	 *         相似度获取看com.ebig.hdi.common.utils.StringSemArithUtil类，且按照相似度倒序排列
	 */
	public <T> List<Map<String, Object>> matching(T t, Rules rules) {
		String tableName = rules.getDestTablename();
		String sql = rules.getSql();
		String sqlParams = rules.getSqlParams();
		if (StringUtils.isBlank(tableName) && StringUtils.isBlank(sql)) {
			throw new MatchingRulesException("请配置表名或者SQL语句");
		}
		List<String> sList = new LinkedList<>();
		if (StringUtils.isNotBlank(tableName)) {
			sList.add(tableName);
		} else {
			sList.add(sql);
			sList.add(sqlParams);
		}
		if (rules.getRuleList().isEmpty()) {// 规则为空，匹配返回空的list
			return Collections.emptyList();
		}
		Map<String, Object> map = wrapObject(t);// 将对象变为Map
		if (rules.getType() == MatchingConstants.AUTO_RULE_TYPE) {
			if (rules.isEnSort()) {
				return autoOrderMatching(sList, map, rules);
			}
			return autoNoOrderMatching(sList, map, rules);
		}
		return handleMatching(sList, map, rules);
	}

	/**
	 * 有序自动匹配，按配置顺序依次向下匹配
	 * 
	 * @param sList
	 * @param ruleList
	 * @return
	 */
	private List<Map<String, Object>> autoOrderMatching(List<String> sList, Map<String, Object> map, Rules rules) {
		List<Rule> ruleList = rules.getRuleList();
		SqlParamter sqlParamter = new SqlParamter(sList, map);
		StringBuilder sqlBuilder = sqlParamter.getSqlBuilder();
		List<Object> params = sqlParamter.getParams();
		for (int i = 0; i < ruleList.size(); i++) {
			Rule rule = ruleList.get(i);
			Object value = map.get(rule.getKey());
			if (value != null) {// 如果value为null,不拼接条件，按顺序从上到下取第一个不等于null的条件
				sqlBuilder.append(" and " + rule.getDestColumn() + " = ?");
				params.add(value);
				List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlBuilder.toString(), params.toArray());
				int size = list.size();
				if (size == 1) {
					return list;
				} else if (size < 1) {
					return Collections.emptyList();
				} else {
					// 配置了多条策略
					MultiPolicy multiPolicy = rules.getMultiPolicy();
					if (multiPolicy != null && i == multiPolicy.getEnIndex()) {
						return multiPolicyParse(list, multiPolicy);
					}
				}
			}
		}
		return Collections.emptyList();
	}

	private List<Map<String, Object>> multiPolicyParse(List<Map<String, Object>> list, MultiPolicy multiPolicy) {
		String type = multiPolicy.getType();
		if (MultiPolicyType.ALL.getType().equals(type)) {
			return list;
		}
		if (MultiPolicyType.FIRST.getType().equals(type)) {
			dealSingleResult(list, multiPolicy.getPolicyColumn());
			return list.subList(0, 1);
		}
		if (MultiPolicyType.LAST.getType().equals(type)) {
			dealSingleResult(list, multiPolicy.getPolicyColumn());
			return list.subList(list.size() - 1, list.size());
		}
		// 没有配置类型或者类型配置错误默认为NONE
		return Collections.emptyList();
	}

	private void dealSingleResult(List<Map<String, Object>> list, String policyColumn) {
		Map<String, Object> map = list.get(0);
		if (!map.containsKey(policyColumn)) {
			throw new MatchingRulesException("策略字段policyColumn:" + policyColumn + "在结果集中找不到");
		}
		Collections.sort(list, new Comparator<Map<String, Object>>() {

			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				return StringUtils.compare(getStrValue(o1), getStrValue(o2));// 从小到大排序
			}

			private String getStrValue(Map<String, Object> map) {
				Object value = map.get(policyColumn);
				if (value instanceof Date) {
					value = ((Date) value).getTime();
				}
				return String.valueOf(value);
			}

		});
	}

	@SuppressWarnings("unchecked")
	private <T> Map<String, Object> wrapObject(T t) {
		Map<String, Object> map = null;
		if (t instanceof Map) {
			map = (Map<String, Object>) t;
		} else {
			map = BeanUtil.beanToMap(t);
		}
		return map;
	}

	/**
	 * 内部类，记录SQL和参数
	 * 
	 * @author zack
	 *
	 */
	private class SqlParamter {
		@Getter
		private StringBuilder sqlBuilder;
		@Getter
		private List<Object> params;

		public SqlParamter(List<String> sList, Map<String, Object> map) {
			sqlBuilder = new StringBuilder();
			params = new LinkedList<>();
			if (sList.size() != 1) {
				sqlBuilder.append(sList.get(0));
				params.addAll(parseParams(sList.get(1), map));
			} else {// 不是SQL就是表名
				sqlBuilder.append(String.format(MatchingConstants.RULE_SQL, sList.get(0)));
			}
			if (sqlBuilder.toString().toLowerCase().indexOf("where") == -1) {
				sqlBuilder.append(" where 1=1");
			}
		}

		/**
		 * 解析参数，规定传入参数格式为{aaa},{sss},a
		 * @param params
		 * @param map
		 * @return
		 */
		private List<Object> parseParams(String params, Map<String, Object> map) {
			List<Object> list = new LinkedList<>();
			for (String param : params.split(",")) {
				if (param.indexOf('{') == 0 && param.indexOf('}') == param.length() - 1) {
					String substring = param.substring(1, param.length() - 1);
					if (!map.containsKey(substring)) {
						throw new MatchingRulesException("参数：" + param + "配置错误,找不到对应参数的值");
					}
					list.add(map.get(substring));
				} else {
					list.add(param);
				}
			}
			return list;
		}
	}

	/**
	 * 无序自动匹配
	 * 
	 * @param sList
	 * @param ruleList
	 * @return
	 */
	private List<Map<String, Object>> autoNoOrderMatching(List<String> sList, Map<String, Object> map, Rules rules) {
		List<Rule> ruleList = rules.getRuleList();
		SqlParamter sqlParamter = new SqlParamter(sList, map);
		StringBuilder sqlBuilder = sqlParamter.getSqlBuilder();
		List<Object> params = sqlParamter.getParams();
		for (Rule rule : ruleList) {
			Object value = map.get(rule.getKey());
			if (value != null) {// 如果value为null,不拼接条件
				sqlBuilder.append(" and " + rule.getDestColumn() + " = ?");
				params.add(value);
			}
		}
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlBuilder.toString(), params.toArray());
		if (list.size() == 1) {
			return list;
		}
		// 配置了多条策略
		if (rules.getMultiPolicy() != null) {
			return multiPolicyParse(list, rules.getMultiPolicy());
		}
		return list;
	}

	/**
	 * 手动匹配
	 * 
	 * @param sList
	 * @param ruleList
	 * @return
	 */
	private List<Map<String, Object>> handleMatching(List<String> sList, Map<String, Object> m, Rules rules) {
		List<Rule> ruleList = rules.getRuleList();
		if (ruleList.size() > 1) {// 如果手动匹配规则项数量大于1，则不符合规则
			throw new MatchingRulesException("手动匹配的规则项大于1，不符合规则");
		}
		SqlParamter sqlParamter = new SqlParamter(sList, m);
		StringBuilder sqlBuilder = sqlParamter.getSqlBuilder();
		List<Object> params = sqlParamter.getParams();
		final List<Map<String, Object>> mList = new LinkedList<>();
		final Rule rule = ruleList.get(0);
		jdbcTemplate.query(sqlBuilder.toString(), params.toArray(), new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				Map<String, Object> map = TableEntityUtils.getMapByResultSet(rs);
				String value = String.valueOf(m.get(rule.getKey()));
				String destValue = String.valueOf(map.get(rule.getDestColumn()));
				double semblance = StringSemArithUtil.getSemblance(value, destValue);
				if (semblance < rule.getSimilarity()) {// 如果小于定义的相似比,跳过
					return;
				}
				map.put(SIMILARITY, semblance);
				mList.add(map);
			}
		});
		Collections.sort(mList, (Map<String, Object> o1, Map<String, Object> o2) -> Double
				.compare((double) o2.get(SIMILARITY), (double) o1.get(SIMILARITY)));

		int showQty = rule.getShowQty();
		if (showQty <= 0) {
			return Collections.emptyList();
		}
		if (mList.size() < showQty) {
			return mList;
		}
		return mList.subList(0, showQty);
	}

	/**
	 * 根据记录对象，规则名匹配
	 * 
	 * @param t
	 * @param ruleName
	 * @return
	 */
	public <T> List<Map<String, Object>> matching(T t, String name) {
		Rules rules = getRules(name);
		return matching(t, rules);
	}

	/**
	 * 根据规则名获取规则
	 * 
	 * @param name
	 * @return
	 */
	public Rules getRules(String name) {
		Map<String, Rules> rules = matchingRulesParser.getMatchingRules().getRules();
		if (rules.isEmpty()) {
			throw new MatchingRulesException("匹配规则项rules未定义");
		}
		return rules.get(name);
	}

}
