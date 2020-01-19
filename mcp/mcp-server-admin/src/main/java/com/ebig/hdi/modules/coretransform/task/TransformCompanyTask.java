package com.ebig.hdi.modules.coretransform.task;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ebig.hdi.common.enums.DataSourceEnum;
import com.ebig.hdi.common.enums.DelFlagEnum;
import com.ebig.hdi.common.enums.IsMatchEnum;
import com.ebig.hdi.common.enums.StatusEnum;
import com.ebig.hdi.common.utils.DateUtils;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.coretransform.entity.TempPubCompanyEntity;
import com.ebig.hdi.modules.job.dao.TempPubCompanyDao;
import com.ebig.hdi.modules.job.dao.TempPubUcompanyDao;
import com.ebig.hdi.modules.job.entity.ScheduleJobEntity;
import com.ebig.hdi.modules.job.entity.TempPubUcompanyEntity;
import com.ebig.hdi.modules.job.task.ITask;
import com.ebig.hdi.modules.unicode.dao.UnicodeSupplyShipDao;
import com.ebig.hdi.modules.unicode.entity.UnicodeSupplyShipEntity;

import lombok.extern.slf4j.Slf4j;

@Component("transformCompanyTask")
@Slf4j
public class TransformCompanyTask implements ITask {

	@Autowired
	private TempPubCompanyDao tempPubCompanyDao;

	@Autowired
	private UnicodeSupplyShipDao unicodeSupplyShipDao;

	@Autowired
	private TempPubUcompanyDao tempPubUcompanyDao;

	private static boolean isRunning = false;

	@Override
	public void run(ScheduleJobEntity scheduleJob) {

		synchronized (TransformCompanyTask.class) {
			if (isRunning) {
				log.info("transformCompanyTask定时任务已在执行，为避免重复处理数据，将退出本次任务！");
				return;
			}
			isRunning = true;
		}

		try {
			log.info("transformCompanyTask定时任务正在执行，参数为：{}", scheduleJob.getParams());
			// 默认获得前一天临时表数据
			Date beginTime = scheduleJob.getBeginTime();
			Date endTime = scheduleJob.getEndTime();
			if (StringUtil.isEmpty(beginTime) && StringUtil.isEmpty(endTime)) {
				endTime = new Date();
				beginTime = DateUtils.addDateDays(endTime, -1);
			}
			List<TempPubCompanyEntity> tempPubCompanyEntityList = tempPubCompanyDao.selectByTime(beginTime, endTime);

			for (TempPubCompanyEntity tempPubCompanyEntity : tempPubCompanyEntityList) {

				try {
					if (StatusEnum.DISABLE.getKey().equals(tempPubCompanyEntity.getUsestatus().intValue())) {
						// 原系统关系解绑
						List<UnicodeSupplyShipEntity> shipList = unicodeSupplyShipDao.selectList(
								new EntityWrapper<UnicodeSupplyShipEntity>().eq("del_flag", DelFlagEnum.NORMAL.getKey())
										.eq("sources_supplier_id", tempPubCompanyEntity.getMcompanyid())
										.eq("sources_hospital_id", tempPubCompanyEntity.getUorganid())
										);
						if (!StringUtil.isEmpty(shipList)) {
							deleteSupplyShip(tempPubCompanyEntity, shipList);
						}

					} else {
						// 原系统关系正常绑定
						TempPubUcompanyEntity sourcesHospital = tempPubUcompanyDao
								.selectById(tempPubCompanyEntity.getUorganid());
						if (StringUtil.isEmpty(sourcesHospital)) {
							log.error("原系统医院id：{}，对应的原系统医院信息不存在，入库失败！", tempPubCompanyEntity.getUorganid());
							continue;
						}

						List<UnicodeSupplyShipEntity> shipList = unicodeSupplyShipDao.selectList(
								new EntityWrapper<UnicodeSupplyShipEntity>().eq("del_flag", DelFlagEnum.NORMAL.getKey())
										.eq("sources_supplier_id", tempPubCompanyEntity.getMcompanyid())
										.eq("sources_hospital_id", tempPubCompanyEntity.getUorganid())
										);

						UnicodeSupplyShipEntity unicodeSupplyShipEntity = new UnicodeSupplyShipEntity();
						unicodeSupplyShipEntity.setSourcesSupplierId(tempPubCompanyEntity.getMcompanyid());
						unicodeSupplyShipEntity.setSourcesHospitalId(tempPubCompanyEntity.getUorganid());
						unicodeSupplyShipEntity.setSourcesSupplierName(tempPubCompanyEntity.getCompanyname());
						unicodeSupplyShipEntity.setSourcesHospitalName(sourcesHospital.getUcompanyname());
						unicodeSupplyShipEntity.setSourcesSupplierCreditCode(tempPubCompanyEntity.getCompanyno());
						unicodeSupplyShipEntity.setSourcesHospitalCreditCode(sourcesHospital.getUcompanyno());
						unicodeSupplyShipEntity.setDatasource(DataSourceEnum.PORT.getKey());
						unicodeSupplyShipEntity.setShipFlag(IsMatchEnum.NO.getKey());
						unicodeSupplyShipEntity.setDelFlag(DelFlagEnum.NORMAL.getKey());
						unicodeSupplyShipEntity.setCredate(new Date());

						insertOrUpdateSupplyship(tempPubCompanyEntity, shipList, unicodeSupplyShipEntity);
					}
					
				} catch (Exception e) {
					log.error("pubCompany表id:{}，记录入库失败，该记录将被跳过，请检查数据正确性！", tempPubCompanyEntity.getMcompanyid());
					continue;
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			isRunning = false;
		}
	}

	@Transactional(rollbackFor = Exception.class)
	private void insertOrUpdateSupplyship(TempPubCompanyEntity tempPubCompanyEntity, List<UnicodeSupplyShipEntity> shipList,
			UnicodeSupplyShipEntity unicodeSupplyShipEntity) {
		if (StringUtil.isEmpty(shipList)) {
			// MCP医院供应商关系表中不存在，插入一条未匹配的关系记录
			unicodeSupplyShipDao.insert(unicodeSupplyShipEntity);
		} else {
			// MCP医院供应商关系表中存在，初始化该关系记录为未匹配状态
			unicodeSupplyShipEntity.setShipId(shipList.get(0).getShipId());
			unicodeSupplyShipDao.updateAllColumnById(unicodeSupplyShipEntity);
		}
		
		//删除临时表数据
		tempPubCompanyDao.deleteById(tempPubCompanyEntity.getMcompanyid());
	}

	@Transactional(rollbackFor = Exception.class)
	private void deleteSupplyShip(TempPubCompanyEntity tempPubCompanyEntity, List<UnicodeSupplyShipEntity> shipList) {
		//删除关系记录
		unicodeSupplyShipDao.deleteById(shipList.get(0).getShipId());
		
		//删除临时表数据
		tempPubCompanyDao.deleteById(tempPubCompanyEntity.getMcompanyid());
	}
}
