package com.ebig.hdi.modules.job.task;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ebig.hdi.common.enums.DelFlagEnum;
import com.ebig.hdi.common.enums.IsMatchEnum;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.job.entity.ScheduleJobEntity;
import com.ebig.hdi.modules.org.dao.OrgHospitalInfoDao;
import com.ebig.hdi.modules.org.dao.OrgSupplierHospitalRefDao;
import com.ebig.hdi.modules.org.dao.OrgSupplierInfoDao;
import com.ebig.hdi.modules.org.entity.OrgHospitalInfoEntity;
import com.ebig.hdi.modules.org.entity.OrgSupplierHospitalRefEntity;
import com.ebig.hdi.modules.org.entity.OrgSupplierInfoEntity;
import com.ebig.hdi.modules.unicode.dao.UnicodeSupplyShipDao;
import com.ebig.hdi.modules.unicode.entity.UnicodeSupplyShipEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Component("supplierPlatformMatchTask")
@Slf4j
public class SupplierPlatformMatchTask implements ITask {

	@Autowired
	private UnicodeSupplyShipDao unicodeSupplyShipDao;
	
	@Autowired
	private OrgHospitalInfoDao orgHospitalInfoDao;
	
	@Autowired
	private OrgSupplierInfoDao orgSupplierInfoDao;
	
	@Autowired
	private OrgSupplierHospitalRefDao orgSupplierHospitalRefDao;

	private  boolean isRunning = false;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void run(ScheduleJobEntity scheduleJob) {

		synchronized (SupplierPlatformMatchTask.class) {
			if (isRunning) {
				log.info("supplierPlatformMatchTask定时任务已在执行，为避免重复处理数据，将退出本次任务！");
				return;
			}
			isRunning = true;
		}

		try {
			log.info("supplierPlatformMatchTask定时任务正在执行，参数为：{}", scheduleJob.getParams());

			// 获取未匹配关系数据
			List<UnicodeSupplyShipEntity> supplyShipList = unicodeSupplyShipDao
					.selectList(new EntityWrapper<UnicodeSupplyShipEntity>().eq("del_flag", DelFlagEnum.NORMAL.getKey())
							.eq("ship_flag", IsMatchEnum.NO.getKey()));

			for (UnicodeSupplyShipEntity supplyShip : supplyShipList) {
				match(supplyShip);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			isRunning = false;
		}

	}
	private void match(UnicodeSupplyShipEntity supplyShip) {
		List<OrgHospitalInfoEntity> hospitalList = orgHospitalInfoDao.selectByCreditCode(supplyShip.getSourcesHospitalCreditCode());
		List<OrgSupplierInfoEntity> supplierList = orgSupplierInfoDao.selectByCreditCode(supplyShip.getSourcesSupplierCreditCode());
		if(!StringUtil.isEmpty(hospitalList) && !StringUtil.isEmpty(supplierList)) {
			List<OrgSupplierHospitalRefEntity> refList = orgSupplierHospitalRefDao.selectBySupplierIdAndHospitalId(supplierList.get(0).getId(), hospitalList.get(0).getId());
			if(!StringUtil.isEmpty(refList)) {
				//匹配成功
				supplyShip.setSupplierHospitalRefId(refList.get(0).getId());
				supplyShip.setSupplierId(refList.get(0).getSupplierId());
				supplyShip.setHospitalId(refList.get(0).getHospitalId());
				supplyShip.setShipFlag(IsMatchEnum.YES.getKey());
				supplyShip.setEditdate(new Date());
				unicodeSupplyShipDao.updateById(supplyShip);
			}
		}
	}

}
