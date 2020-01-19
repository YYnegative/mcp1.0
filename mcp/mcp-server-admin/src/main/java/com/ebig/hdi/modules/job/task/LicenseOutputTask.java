package com.ebig.hdi.modules.job.task;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ebig.hdi.modules.consumables.entity.GoodsSupplierConsumablesSpecsEntity;
import com.ebig.hdi.modules.consumables.service.GoodsSupplierConsumablesSpecsService;
import com.ebig.hdi.modules.drugs.entity.GoodsSupplierDrugsSpecsEntity;
import com.ebig.hdi.modules.drugs.service.GoodsSupplierDrugsSpecsService;
import com.ebig.hdi.modules.job.dao.ScheduleJobDao;
import com.ebig.hdi.modules.job.dao.TempSpdLicenseFactoryDao;
import com.ebig.hdi.modules.job.dao.TempSpdLicenseGoodsDao;
import com.ebig.hdi.modules.job.dao.TempSpdLicenseSupplierDao;
import com.ebig.hdi.modules.job.entity.ScheduleJobEntity;
import com.ebig.hdi.modules.job.entity.TempSpdLicenseFactoryEntity;
import com.ebig.hdi.modules.job.entity.TempSpdLicenseGoodsEntity;
import com.ebig.hdi.modules.job.entity.TempSpdLicenseSupplierEntity;
import com.ebig.hdi.modules.license.entity.LicenseFactoryInfoEntity;
import com.ebig.hdi.modules.license.entity.LicenseGoodsInfoEntity;
import com.ebig.hdi.modules.license.entity.LicenseSupplierInfoEntity;
import com.ebig.hdi.modules.license.service.LicenseFactoryInfoService;
import com.ebig.hdi.modules.license.service.LicenseGoodsInfoService;
import com.ebig.hdi.modules.license.service.LicenseSupplierInfoService;
import com.ebig.hdi.modules.reagent.entity.GoodsSupplierReagentSpecsEntity;
import com.ebig.hdi.modules.reagent.service.GoodsSupplierReagentSpecsService;
import com.ebig.hdi.modules.sys.entity.SysConfigEntity;
import com.ebig.hdi.modules.sys.service.SysConfigService;
import com.ebig.hdi.modules.unicode.entity.UnicodeSupplyShipEntity;
import com.ebig.hdi.modules.unicode.service.UnicodeSupplyShipService;

import lombok.extern.slf4j.Slf4j;

/**
 * 把对应医院的证照插入临时表里面
 * @author clang
 *
 */

@Component("licenseOutputTask")
@Slf4j
public class LicenseOutputTask implements ITask{
	
	@Autowired
	private UnicodeSupplyShipService unicodeSupplyShipService;
	@Autowired
	private SysConfigService sysConfigService;
	@Autowired
	private LicenseSupplierInfoService licenseSupplierInfoService;
	@Autowired
	private LicenseFactoryInfoService licenseFactoryInfoService;
	@Autowired
	private LicenseGoodsInfoService licenseGoodsInfoService;
	@Autowired
	private TempSpdLicenseSupplierDao tempSpdLicenseSupplierDao;
	@Autowired
	private TempSpdLicenseFactoryDao tempSpdLicenseFactoryDao;
	@Autowired
	private TempSpdLicenseGoodsDao tempSpdLicenseGoodsDao;
	@Autowired
	private ScheduleJobDao scheduleJobDao;
	@Autowired
	private GoodsSupplierConsumablesSpecsService goodsSupplierConsumablesSpecsService;
	@Autowired
	private GoodsSupplierReagentSpecsService goodsSupplierReagentSpecsService;
	@Autowired
	private GoodsSupplierDrugsSpecsService goodsSupplierDrugsSpecsService;
	
	private static boolean flag = false;

	
	@Override
	public void run(ScheduleJobEntity scheduleJob) {
		synchronized (LicenseOutputTask.class){
			if(!flag){
				flag = true;
				log.info("licenseOutputTask定时任务正在执行，参数为：{}", scheduleJob.getParams());
				//开始插入数据
				licenseOutput();
				flag = false;
			}else{
				log.info("上次的licenseOutputTask定时任务还没有运行完！");
			}
		}
	}
	
	private void licenseOutput(){
		SysConfigEntity sysConfig = sysConfigService.selectByParamKey("SPD_HOSPITAL_IDS");
		if(sysConfig == null){
			log.info("查询key为SPD_HOSPITAL_IDS的系统配置表失败，请检查是否存在该系统配置数据！");
			return ;
		}
		String spdHospitalIds = sysConfig.getParamValue();
		if(StringUtils.isBlank(spdHospitalIds)){
			log.info("key为SPD_HOSPITAL_IDS的系统配置表没有设置医院id！");
			return ;
		}
		
		//遍历需要下发的原医院id，并且查出对应供应商的证照
		String[] spdHospitalIdArr = spdHospitalIds.split(",");
		for(String spdHospitalId : spdHospitalIdArr){
			List<UnicodeSupplyShipEntity> supplyShipList = unicodeSupplyShipService.selectList(new EntityWrapper<UnicodeSupplyShipEntity>()
							.eq("sources_hospital_id", spdHospitalId));
			
			//通过原医院id查询匹配表里所对应的供应商，然后查询供应商对应的商品、厂商、供应商证照
			for(UnicodeSupplyShipEntity supplyShip : supplyShipList){
				Long supplierId = supplyShip.getSupplierId();
				getAndPushSupplierLicense(supplierId,spdHospitalId);
			}
		}
	}
	
	private void getAndPushSupplierLicense(Long supplierId,String spdHospitalId){
		List<ScheduleJobEntity> scheduleJobList = scheduleJobDao.selectList(new EntityWrapper<ScheduleJobEntity>()
				.eq("bean_name", "licenseOutputTask"));
		if(scheduleJobList == null || scheduleJobList.size() < 1){
			log.info("licenseOutputTask 定时任务执行失败！查询不出对应的定时任务");
			return ;
		}
		if(scheduleJobList.size() != 1){
			log.info("licenseOutputTask 定时任务执行失败！查询出多条定时任务");
			return ;
		}
		ScheduleJobEntity scheduleJob = scheduleJobList.get(0);
		checkAndSetDefaultDate(scheduleJob);
		//插入供应商证照
		 List<LicenseSupplierInfoEntity> supplierLicenseList = licenseSupplierInfoService.selectBySupplierIdAndTime(supplierId,scheduleJob);
		
		for(LicenseSupplierInfoEntity supplierLicense : supplierLicenseList){
			try{
				// 先查询临时表是否存在对应id的记录，如果存在则执行修改，如果不存在则执行新增记录
				TempSpdLicenseSupplierEntity tempSupplierLicense = tempSpdLicenseSupplierDao.selectById(supplierLicense.getId());
				if(tempSupplierLicense == null){
					tempSupplierLicense = new TempSpdLicenseSupplierEntity();
					tempSupplierLicense.setId(supplierLicense.getId().toString());
					tempSupplierLicense.setSupplierId(supplierLicense.getSupplierId()!=null?supplierLicense.getSupplierId().toString():null);
					tempSupplierLicense.setClassifyId(supplierLicense.getClassifyId()!=null?supplierLicense.getClassifyId().toString():null);
					tempSupplierLicense.setName(supplierLicense.getName());
					tempSupplierLicense.setNumber(supplierLicense.getNumber());
					tempSupplierLicense.setBeginTime(supplierLicense.getBeginTime());
					tempSupplierLicense.setEndTime(supplierLicense.getEndTime());
					tempSupplierLicense.setPicUrl(supplierLicense.getPicUrl());
					tempSupplierLicense.setStatus(new BigDecimal(supplierLicense.getStatus()));
					tempSupplierLicense.setDeptId(spdHospitalId);
					tempSupplierLicense.setCreateTime(supplierLicense.getCreateTime());
					tempSupplierLicense.setDelFlag(new BigDecimal(supplierLicense.getDelFlag()));
					tempSupplierLicense.setNewLicenseId(supplierLicense.getNewLicenseId()!=null?supplierLicense.getNewLicenseId().toString():null);
					tempSpdLicenseSupplierDao.insert(tempSupplierLicense);
				}else{
					tempSupplierLicense.setSupplierId(supplierLicense.getSupplierId()!=null?supplierLicense.getSupplierId().toString():null);
					tempSupplierLicense.setClassifyId(supplierLicense.getClassifyId()!=null?supplierLicense.getClassifyId().toString():null);
					tempSupplierLicense.setName(supplierLicense.getName());
					tempSupplierLicense.setNumber(supplierLicense.getNumber());
					tempSupplierLicense.setBeginTime(supplierLicense.getBeginTime());
					tempSupplierLicense.setEndTime(supplierLicense.getEndTime());
					tempSupplierLicense.setPicUrl(supplierLicense.getPicUrl());
					tempSupplierLicense.setStatus(new BigDecimal(supplierLicense.getStatus()));
					tempSupplierLicense.setDeptId(spdHospitalId);
					tempSupplierLicense.setCreateTime(supplierLicense.getCreateTime());
					tempSupplierLicense.setDelFlag(new BigDecimal(supplierLicense.getDelFlag()));
					tempSupplierLicense.setNewLicenseId(supplierLicense.getNewLicenseId()!=null?supplierLicense.getNewLicenseId().toString():null);
					tempSpdLicenseSupplierDao.updateById(tempSupplierLicense);
				}
				
			}catch(Exception e){
				log.error("hdi_license_supplier_info 表中 id 为" + supplierLicense.getId() + " 数据插入失败 !");
				continue;
			}
			
		}
		
		//插入厂商证照
		List<LicenseFactoryInfoEntity> factoryLicenseList = licenseFactoryInfoService.selectBySupplierIdAndTime(supplierId,scheduleJob);
		for(LicenseFactoryInfoEntity factoryLicense : factoryLicenseList){
			try{
				
				// 先查询临时表是否存在对应id的记录，如果存在则执行修改，如果不存在则执行新增记录
				TempSpdLicenseFactoryEntity tempFactoryLicense = tempSpdLicenseFactoryDao.selectById(factoryLicense.getId());
				if(tempFactoryLicense == null){
					tempFactoryLicense = new TempSpdLicenseFactoryEntity();
					tempFactoryLicense.setId(factoryLicense.getId().toString());
					tempFactoryLicense.setSupplierId(factoryLicense.getSupplierId()!=null?factoryLicense.getSupplierId().toString():null);
					tempFactoryLicense.setFactoryId(factoryLicense.getFactoryId()!=null?factoryLicense.getFactoryId().toString():null);
					tempFactoryLicense.setClassifyId(factoryLicense.getClassifyId()!=null?factoryLicense.getClassifyId().toString():null);
					tempFactoryLicense.setName(factoryLicense.getName());
					tempFactoryLicense.setNumber(factoryLicense.getNumber());
					tempFactoryLicense.setBeginTime(factoryLicense.getBeginTime());
					tempFactoryLicense.setEndTime(factoryLicense.getEndTime());
					tempFactoryLicense.setPicUrl(factoryLicense.getPicUrl());
					tempFactoryLicense.setStatus(new BigDecimal(factoryLicense.getStatus()));
					tempFactoryLicense.setDeptId(spdHospitalId);
					tempFactoryLicense.setDelFlag(new BigDecimal(factoryLicense.getDelFlag()));
					tempFactoryLicense.setNewLicenseId(factoryLicense.getNewLicenseId()!=null?factoryLicense.getNewLicenseId().toString():null);
					tempSpdLicenseFactoryDao.insert(tempFactoryLicense);
				}else{
					tempFactoryLicense.setSupplierId(factoryLicense.getSupplierId()!=null?factoryLicense.getSupplierId().toString():null);
					tempFactoryLicense.setFactoryId(factoryLicense.getFactoryId()!=null?factoryLicense.getFactoryId().toString():null);
					tempFactoryLicense.setClassifyId(factoryLicense.getClassifyId()!=null?factoryLicense.getClassifyId().toString():null);
					tempFactoryLicense.setName(factoryLicense.getName());
					tempFactoryLicense.setNumber(factoryLicense.getNumber());
					tempFactoryLicense.setBeginTime(factoryLicense.getBeginTime());
					tempFactoryLicense.setEndTime(factoryLicense.getEndTime());
					tempFactoryLicense.setPicUrl(factoryLicense.getPicUrl());
					tempFactoryLicense.setStatus(new BigDecimal(factoryLicense.getStatus()));
					tempFactoryLicense.setDeptId(spdHospitalId);
					tempFactoryLicense.setDelFlag(new BigDecimal(factoryLicense.getDelFlag()));
					tempFactoryLicense.setNewLicenseId(factoryLicense.getNewLicenseId()!=null?factoryLicense.getNewLicenseId().toString():null);
					tempSpdLicenseFactoryDao.updateById(tempFactoryLicense);
				}
				
				
			}catch(Exception e){
				log.error("hdi_license_factory_info 表中 id 为" + factoryLicense.getId() + " 数据插入失败 !");
				continue;
			}
		}
		
		//插入商品证照
		List<LicenseGoodsInfoEntity> goodsLicenseList = licenseGoodsInfoService.selectBySupplierIdAndTime(supplierId,scheduleJob);
		for(LicenseGoodsInfoEntity goodsLicense : goodsLicenseList){
			try{
				// 查询出所关联的规格id
				List<Long> specsIdList = setSpecsIds(goodsLicense);
				// 先查询临时表是否存在对应id的记录，如果存在则执行修改，如果不存在则执行新增记录
				for(Long specsId : specsIdList){
					
					List<TempSpdLicenseGoodsEntity> tempGoodsLicenseList = tempSpdLicenseGoodsDao.selectList(new EntityWrapper<TempSpdLicenseGoodsEntity>()
							.eq("id", goodsLicense.getId())
							.eq("goods_specs_id", specsId)
							.eq("goods_type", goodsLicense.getGoodsType()));
					if(tempGoodsLicenseList == null || tempGoodsLicenseList.size() == 0){
						TempSpdLicenseGoodsEntity tempGoodsLicense = new TempSpdLicenseGoodsEntity();
						tempGoodsLicense.setId(goodsLicense.getId().toString());
						tempGoodsLicense.setSupplierId(goodsLicense.getSupplierId()!=null?goodsLicense.getSupplierId().toString():null);
						tempGoodsLicense.setGoodsSpecsId(specsId.toString());
						tempGoodsLicense.setGoodsType(new BigDecimal(goodsLicense.getGoodsType()));
						tempGoodsLicense.setClassifyId(goodsLicense.getClassifyId()!=null?goodsLicense.getClassifyId().toString():null);
						tempGoodsLicense.setName(goodsLicense.getName());
						tempGoodsLicense.setNumber(goodsLicense.getNumber());
						tempGoodsLicense.setBeginTime(goodsLicense.getBeginTime());
						tempGoodsLicense.setEndTime(goodsLicense.getEndTime());
						tempGoodsLicense.setPicUrl(goodsLicense.getPicUrl());
						tempGoodsLicense.setStatus(new BigDecimal(goodsLicense.getStatus()));
						tempGoodsLicense.setDeptId(spdHospitalId);
						tempGoodsLicense.setDelFlag(new BigDecimal(goodsLicense.getDelFlag()));
						tempGoodsLicense.setNewLicenseId(goodsLicense.getNewLicenseId()!=null?goodsLicense.getNewLicenseId().toString():null);
						tempSpdLicenseGoodsDao.insert(tempGoodsLicense);
					}else{
						TempSpdLicenseGoodsEntity tempGoodsLicense = tempGoodsLicenseList.get(0);
						tempGoodsLicense.setSupplierId(goodsLicense.getSupplierId()!=null?goodsLicense.getSupplierId().toString():null);
						tempGoodsLicense.setClassifyId(goodsLicense.getClassifyId()!=null?goodsLicense.getClassifyId().toString():null);
						tempGoodsLicense.setName(goodsLicense.getName());
						tempGoodsLicense.setNumber(goodsLicense.getNumber());
						tempGoodsLicense.setBeginTime(goodsLicense.getBeginTime());
						tempGoodsLicense.setEndTime(goodsLicense.getEndTime());
						tempGoodsLicense.setPicUrl(goodsLicense.getPicUrl());
						tempGoodsLicense.setStatus(new BigDecimal(goodsLicense.getStatus()));
						tempGoodsLicense.setDeptId(spdHospitalId);
						tempGoodsLicense.setDelFlag(new BigDecimal(goodsLicense.getDelFlag()));
						tempGoodsLicense.setNewLicenseId(goodsLicense.getNewLicenseId()!=null?goodsLicense.getNewLicenseId().toString():null);
						tempSpdLicenseGoodsDao.updateById(tempGoodsLicense);
					}
				}
				
				
			}catch(Exception e){
				log.error("hdi_license_factory_info 表中 id 为" + goodsLicense.getId() + " 数据插入失败 !");
				continue;
			}
		}
		
	}
	
	// 如果定时任务没有设置开始时间与结束时间，给默认前一天零点到今天零点
	private void checkAndSetDefaultDate(ScheduleJobEntity scheduleJob){
		if(scheduleJob.getBeginTime() == null && scheduleJob.getEndTime() == null){
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(new Date());
			calendar.set(Calendar.HOUR_OF_DAY, 0);
		    calendar.set(Calendar.MINUTE, 0);
		    calendar.set(Calendar.SECOND, 0);
			calendar.add(calendar.DATE,-1);
			scheduleJob.setBeginTime(calendar.getTime());
			calendar = new GregorianCalendar();
			calendar.setTime(new Date());
			calendar.set(Calendar.HOUR_OF_DAY, 0);
		    calendar.set(Calendar.MINUTE, 0);
		    calendar.set(Calendar.SECOND, 0);
		    scheduleJob.setEndTime(calendar.getTime());
		}
		
		
	}
	
	private List<Long> setSpecsIds(LicenseGoodsInfoEntity goodsLicense){
		List<Long> specsIdList = new ArrayList<>();
		if(goodsLicense.getGoodsType() == 1){
			//药品
			List<GoodsSupplierDrugsSpecsEntity> drugsSpecsList = goodsSupplierDrugsSpecsService.selectList(new EntityWrapper<GoodsSupplierDrugsSpecsEntity>()
					.eq("drugs_id", goodsLicense.getGoodsId()));
			for(GoodsSupplierDrugsSpecsEntity drugsSpecs : drugsSpecsList){
				specsIdList.add(drugsSpecs.getId());
			}
		}else if(goodsLicense.getGoodsType() == 2){
			//试剂
			List<GoodsSupplierReagentSpecsEntity> reagentSpecsList = goodsSupplierReagentSpecsService.selectList(new EntityWrapper<GoodsSupplierReagentSpecsEntity>()
					.eq("reagen_id", goodsLicense.getGoodsId()));
			for(GoodsSupplierReagentSpecsEntity reagentSpecs : reagentSpecsList){
				specsIdList.add(reagentSpecs.getId());
			}
			
		}else if(goodsLicense.getGoodsType() == 3){
			//耗材
			List<GoodsSupplierConsumablesSpecsEntity> consumablesSpecsList = goodsSupplierConsumablesSpecsService.selectList(new EntityWrapper<GoodsSupplierConsumablesSpecsEntity>()
					.eq("consumables_id", goodsLicense.getGoodsId()));
			for(GoodsSupplierConsumablesSpecsEntity consumablesSpecs : consumablesSpecsList){
				specsIdList.add(consumablesSpecs.getId());
			}
		}
		return specsIdList;
	}

	
}
