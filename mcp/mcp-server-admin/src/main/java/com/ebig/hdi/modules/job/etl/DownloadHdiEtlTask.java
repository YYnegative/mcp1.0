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
 * ETL下载
 * 
 * @author zack
 *
 */
@Slf4j
@Component("downloadHdiEtlTask")
public class DownloadHdiEtlTask extends AbstractHdiEtlTask {

	@Override
	protected void execute(List<HdiEtlEntity> etlConfigs, EtlDataSource etlDataSource) throws SQLException {
		DataSource targrtDataSource = etlDataSource.getTargetDataSource();
		DataSource originDataSource = etlDataSource.getOriginDataSource();
		for (HdiEtlEntity etlEntity : etlConfigs) {
			log.info("下载准备-----------");
			hdiEtlDao.lockConfigById(etlEntity.getId());// 防止后台数据库修改时间
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(originDataSource);
			List<String> primaryKeys = getPrimaryKeys(targrtDataSource, etlEntity.getTableName());
			String sql = etlEntity.getConfigSql();// + " AND udflag=" + HdiEtlConstants.UD_FLAG_DOWNLOAD;// 下发需要查询标记为1的
			SqlParameterSource sqlParameterSource = getSqlParameterSource(etlEntity);
			log.info("SQL：" + sql + ",参数：" + sqlParameterSource.getValue(HdiEtlConstants.START_DATE) + "---"
					+ sqlParameterSource.getValue(HdiEtlConstants.END_DATE));
			jdbcTemplate.query(sql, sqlParameterSource, new RowCallbackHandler() {
				private int count;

				@Override
				public void processRow(ResultSet rs) throws SQLException {
					Map<String, Object> map = TableEntityUtils.getMapByResultSet(rs);
					count++;
					log.info(etlEntity.getName() + ",查询到第" + count + "条数据：" + map);
					map.remove("udflag");// 移除掉标记
					insertOrUpdate(targrtDataSource, map, etlEntity.getTableName(), primaryKeys);
				}
			});
			Date date = (Date) sqlParameterSource.getValue(HdiEtlConstants.END_DATE);
			hdiEtlDao.updateTime(date.getTime(), etlEntity.getId());
		}
	}

}
