package com.ebig.hdi.modules.job.etl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.ebig.hdi.common.datasources.config.DynamicDataSource;
import com.ebig.hdi.common.utils.SpringContextUtils;
import com.ebig.hdi.modules.job.dao.HdiEtlDao;
import com.ebig.hdi.modules.job.entity.HdiEtlEntity;
import com.ebig.hdi.modules.job.entity.ScheduleJobEntity;
import com.ebig.hdi.modules.job.task.ITask;

import cn.hutool.core.util.ReflectUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractHdiEtlTask implements ITask {

	@Autowired
	protected HdiEtlDao hdiEtlDao;

	@Override
	public void run(ScheduleJobEntity scheduleJob) {
		try {
			// 解析参数0是上传、1是下载
			List<HdiEtlEntity> etlConfigs = hdiEtlDao.findEtlConfig(Integer.parseInt(scheduleJob.getParams()));
			// 根据优先级排序
			Collections.sort(etlConfigs, (HdiEtlEntity o1, HdiEtlEntity o2) -> o1.getPriority() - o2.getPriority());
			// 获取Datasourse
			DynamicDataSource dynamicDataSource = (DynamicDataSource) SpringContextUtils.getBean("dynamicDataSource");
			// 交给子类
			log.info("开始ETL抓取,配置：" + etlConfigs);
			EtlDataSource etlDataSource = new EtlDataSource(dynamicDataSource);
			execute(etlConfigs, etlDataSource);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * 业务执行方法
	 * 
	 * @param etlConfigs
	 * @param etlDataSource
	 * @throws SQLException
	 */
	protected abstract void execute(List<HdiEtlEntity> etlConfigs, EtlDataSource etlDataSource) throws SQLException;

	/**
	 * 获取主键
	 * 
	 * @param dataSource
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	protected List<String> getPrimaryKeys(DataSource dataSource, String tableName) throws SQLException {
		List<String> list = new LinkedList<>();
		try (Connection connection = dataSource.getConnection()) {// 用完后关闭
			DatabaseMetaData databaseMetaData = connection.getMetaData();
			ResultSet resultSet = databaseMetaData.getPrimaryKeys(null, null, tableName);
			while (resultSet.next()) {
				list.add(resultSet.getString(4));// 主键名
			}
		}
		return list;
	}

	/**
	 * 判断是否为双向传输
	 * 
	 * @return
	 */
	protected boolean isDoubleEtl(HdiEtlEntity etlEntity, List<HdiEtlEntity> etlConfigs) {
		int udflag = 0;
		for (HdiEtlEntity hdiEtlEntity : etlConfigs) {
			if (hdiEtlEntity.getTableName().equals(etlEntity.getTableName())) {
				udflag += hdiEtlEntity.getUdflag();
			}
		}
		return udflag == 1;
	}

	/**
	 * 获取参数资源
	 * 
	 * @param etlEntity
	 * @return
	 */
	protected SqlParameterSource getSqlParameterSource(HdiEtlEntity etlEntity) {
		// 计算参数,开始时间为当前时间-间隔时间-事务延迟时间

		Long endDate = System.currentTimeMillis();
		Long startDate = etlEntity.getBeginTime();
		if (startDate == null) {
			startDate = 0L;
		} else {// 若给定开始时间，则按照该时间重新传输
			startDate = etlEntity.getBeginTime() - etlEntity.getTsDelay();
		}
		return new MapSqlParameterSource().addValue(HdiEtlConstants.START_DATE, new Date(startDate))
				.addValue(HdiEtlConstants.END_DATE, new Date(endDate))
				.addValue(HdiEtlConstants.UID, etlEntity.getUid());
	}

	/**
	 * 插入还是更新
	 * 
	 * @param dataSource
	 * @param map
	 * @param tableName
	 * @param primaryKey
	 */
	protected void insertOrUpdate(DataSource dataSource, Map<String, Object> map, String tableName,
			List<String> primaryKeys) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		// 考虑多主键的情况
		StringBuilder pKeysBuilder = new StringBuilder(primaryKeys.size());
		List<Object> pValueList = new ArrayList<>();
		for (String key : primaryKeys) {
			pKeysBuilder.append(" AND " + key + "=?");
			pValueList.add(map.get(key));
		}
		Map<String, Object> qMap = null;
		try {
			qMap = jdbcTemplate.queryForMap(
					String.format(HdiEtlConstants.FINDRECORDBYID_SQL, tableName, pKeysBuilder.toString()),
					pValueList.toArray());
		} catch (DataAccessException e) {

		}
		StringBuilder builder = new StringBuilder(map.size());
		List<Object> values = new ArrayList<>();
		if (qMap != null) {// 更新
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				String Field = entry.getKey();
				
				Boolean etlGetTempField = EtlGetTempField(dataSource,tableName,Field);
				if (etlGetTempField ) {
					builder.append(entry.getKey() + "=?,");
					values.add(entry.getValue());
				}
			}
			String updateSql = String.format(HdiEtlConstants.UPDATE_SQL, tableName,
					builder.substring(0, builder.length() - 1), pKeysBuilder.toString());
			values.addAll(pValueList);// 加上主键的条件
			log.info("开始更新,SQL:" + updateSql + "条件：" + values);
			jdbcTemplate.update(updateSql, values.toArray());
			// TODO 可能需要监听器
			log.info("更新成功");
		} else {
			StringBuilder valueBuilder = new StringBuilder(map.size());
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				String Field = entry.getKey();
				
				Boolean etlGetTempField = EtlGetTempField(dataSource,tableName,Field);
				if (etlGetTempField ) {
					builder.append(entry.getKey() + ",");
					valueBuilder.append("?,");
					values.add(entry.getValue());
				}
			}

			String insertSql = String.format(HdiEtlConstants.INSERT_SQL, tableName,
					builder.substring(0, builder.length() - 1), valueBuilder.substring(0, valueBuilder.length() - 1));
			log.info("开始插入,SQL:" + insertSql + "条件：" + values);
			jdbcTemplate.update(insertSql, values.toArray());
			// TODO 可能需要监听器
			log.info("插入成功");
		}
	}

	
	/**
	 * 判断临时表是否存在此字段
	 * @author frink
	 */
	public Boolean EtlGetTempField(DataSource dataSource,String tableName,String Field){
		Boolean Fieldr = false;
		try (Connection connection = dataSource.getConnection()) {// 用完后关闭
			DatabaseMetaData databaseMetaData = connection.getMetaData();
			ResultSet resultSet = databaseMetaData.getColumns(null, null, tableName,null);
			
			while (resultSet.next()) {
				String string = resultSet.getString("COLUMN_NAME");
				if (string.equals(Field)) {
					Fieldr = true;
					break;
				}
			}
		}catch (SQLException e) {
			throw new RuntimeException(e); 
		}
		return Fieldr;
	}
	
	
	
	/**
	 * 数据源包装
	 * 
	 * @author zack
	 *
	 */
	protected class EtlDataSource {
		/**
		 * 目标datasource
		 */
		@Getter
		private DataSource targetDataSource;
		/**
		 * 原datasource
		 */
		@Getter
		private DataSource originDataSource;

		public EtlDataSource(DynamicDataSource dynamicDataSource) {
			@SuppressWarnings("unchecked")
			Map<Object, Object> fieldValue = (Map<Object, Object>) ReflectUtil.getFieldValue(dynamicDataSource,
					"targetDataSources");
			this.targetDataSource = (DataSource) fieldValue.get(HdiEtlConstants.DATASOURCE_NAME);
			this.originDataSource = (DataSource) ReflectUtil.getFieldValue(dynamicDataSource,
					"defaultTargetDataSource");
		}
	}
	
	
}
