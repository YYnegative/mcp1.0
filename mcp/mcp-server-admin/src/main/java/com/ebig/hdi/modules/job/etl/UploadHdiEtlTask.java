package com.ebig.hdi.modules.job.etl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import com.ebig.hdi.common.utils.TableEntityUtils;
import com.ebig.hdi.modules.job.entity.HdiEtlEntity;

import lombok.extern.slf4j.Slf4j;

/**
 * ETL上传
 * 
 * @author zack
 *
 */
@Slf4j
@Component("uploadHdiEtlTask")
public class UploadHdiEtlTask extends AbstractHdiEtlTask {

	@Override
	protected void execute(List<HdiEtlEntity> etlConfigs, EtlDataSource etlDataSource) throws SQLException {
		/*
		 * 上传的话,原数据源是新HDI数据源，目标是旧HDI数据源
		 */
		DataSource targrtDataSource = etlDataSource.getTargetDataSource();
		DataSource originDataSource = etlDataSource.getOriginDataSource();
		for (HdiEtlEntity etlEntity : etlConfigs) {
			log.info("上传准备-----------");
			hdiEtlDao.lockConfigById(etlEntity.getId());// 防止后台数据库修改时间
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(targrtDataSource);
			List<String> primaryKeys = getPrimaryKeys(originDataSource,
					HdiEtlConstants.TEMP_FLAG + etlEntity.getTableName());
			SqlParameterSource sqlParameterSource = getSqlParameterSource(etlEntity);
			String sql = etlEntity.getConfigSql();// 上传需要查询标记为0的
			log.info("SQL：" + sql + "参数：" + sqlParameterSource.getValue(HdiEtlConstants.START_DATE) + "---"
					+ sqlParameterSource.getValue(HdiEtlConstants.END_DATE));
			jdbcTemplate.query(sql, sqlParameterSource, new RowCallbackHandler() {
				private int count;

				@Override
				public void processRow(ResultSet rs) throws SQLException {
					Map<String, Object> map = TableEntityUtils.getMapByResultSet(rs);
					count++;
					log.info(etlEntity.getName() + ",查询到第" + count + "条数据：" + map);
					insertOrUpdate(originDataSource, map, HdiEtlConstants.TEMP_FLAG + etlEntity.getTableName(),
							primaryKeys);
				}
			});
			// 记录走的时间
			Date date = (Date) sqlParameterSource.getValue(HdiEtlConstants.END_DATE);
			hdiEtlDao.updateTime(date.getTime(), etlEntity.getId());
		}
	}

}
