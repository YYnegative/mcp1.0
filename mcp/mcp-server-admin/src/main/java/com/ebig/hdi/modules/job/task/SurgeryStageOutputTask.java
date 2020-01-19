package com.ebig.hdi.modules.job.task;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ebig.hdi.common.enums.SupplierIsUploadEnum;
import com.ebig.hdi.modules.consumables.dao.GoodsSupplierConsumablesSpecsDao;
import com.ebig.hdi.modules.consumables.entity.GoodsSupplierConsumablesSpecsEntity;
import com.ebig.hdi.modules.job.dao.TempSpdStageinfoDao;
import com.ebig.hdi.modules.job.dao.TempSpdStageinfodtlDao;
import com.ebig.hdi.modules.job.entity.ScheduleJobEntity;
import com.ebig.hdi.modules.job.entity.TempSpdStageinfoEntity;
import com.ebig.hdi.modules.job.entity.TempSpdStageinfodtlEntity;
import com.ebig.hdi.modules.org.dao.OrgSupplierInfoDao;
import com.ebig.hdi.modules.org.entity.OrgSupplierInfoEntity;
import com.ebig.hdi.modules.surgery.dao.SurgeryInfoDao;
import com.ebig.hdi.modules.surgery.dao.SurgeryStageDetailInfoDao;
import com.ebig.hdi.modules.surgery.dao.SurgeryStageInfoDao;
import com.ebig.hdi.modules.surgery.entity.SurgeryInfoEntity;
import com.ebig.hdi.modules.surgery.entity.SurgeryStageDetailInfoEntity;
import com.ebig.hdi.modules.surgery.entity.SurgeryStageInfoEntity;
import com.ebig.hdi.modules.unicode.dao.UnicodeSupplyShipDao;
import com.ebig.hdi.modules.unicode.entity.UnicodeSupplyShipEntity;

import lombok.extern.slf4j.Slf4j;

/**
 * mcp跟台目录数据转化为临时表数据
 * @author clang
 *
 */

@Component("SurgeryStageOutputTask")
@Slf4j
public class SurgeryStageOutputTask implements ITask{
	@Autowired
	private SurgeryStageInfoDao surgeryStageInfoDao;
	@Autowired
	private SurgeryStageDetailInfoDao surgeryStageDetailInfoDao;
	@Autowired
	private TempSpdStageinfoDao tempSpdStageinfoDao;
	@Autowired
	private TempSpdStageinfodtlDao tempSpdStageinfodtlDao;
	@Autowired
	private UnicodeSupplyShipDao unicodeSupplyShipDao;
	@Autowired
	private SurgeryInfoDao surgeryInfoDao;
	@Autowired
	private OrgSupplierInfoDao orgSupplierInfoDao;
	@Autowired
	private GoodsSupplierConsumablesSpecsDao goodsSupplierConsumablesSpecsDao;
	
	private static boolean flag = false;

	@Override
	public void run(ScheduleJobEntity scheduleJob) {
		synchronized (SurgeryStageOutputTask.class){
			if(!flag){
				try{
					flag = true;
					log.info("surgeryStageOutputTask定时任务正在执行，参数为：{}", scheduleJob.getParams());
					//开始插入数据
					outputSurgeryStage();
					flag = false;
				}catch(Exception e){
					log.info("surgeryStageOutputTask定时任务执行失败，参数为：{}", scheduleJob.getParams());
					flag = false;
				}
			}else{
				log.info("上次的surgeryStageOutputTask定时任务还没有运行完！");
			}
		}
	}
	
	/**
	 * 把mcp跟台目录数据转化为临时表数据
	 */
	private void outputSurgeryStage(){
		
		//查询状态为2已提交并且未上传的跟台目录数据，并且同步到临时表中temp_spd_stageinfo与temp_spd_stageinfodtl
		List<SurgeryStageInfoEntity> stageList = surgeryStageInfoDao.selectList(new EntityWrapper<SurgeryStageInfoEntity>()
						.eq("status", 2)
						.eq("is_upload", 0));
		for(SurgeryStageInfoEntity stage : stageList){
			
			// 先插入跟台目录主单
			// 先获取原医院id
			String sourceHospitalId = null;
			if(stage.getHospitalId() != null){
				List<UnicodeSupplyShipEntity> shipList = unicodeSupplyShipDao.selectList(new EntityWrapper<UnicodeSupplyShipEntity>()
						.eq("hospital_id", stage.getHospitalId()));
				if(shipList != null && shipList.size() > 0){
					sourceHospitalId = shipList.get(0).getSourcesHospitalId();
				}
			}
			// 先判断临时表是否存在记录
			List<TempSpdStageinfoEntity> tempStageList = tempSpdStageinfoDao.selectList(new EntityWrapper<TempSpdStageinfoEntity>()
						.eq("stageinfoid", stage.getSourceStageId()));
			TempSpdStageinfoEntity tempStage = new TempSpdStageinfoEntity();
			if(tempStageList != null && tempStageList.size() >0){
				tempStage.setStageinfoid(tempStageList.get(0).getStageinfoid());
			}
			tempStage.setStageinfono(stage.getSurgeryStageNo());
			tempStage.setUorganid(sourceHospitalId);
			if(stage.getSurgeryId() != null){
				// 如果有手术单id，则插入原手术单id和原供应商id
				SurgeryInfoEntity surgery = surgeryInfoDao.selectById(stage.getSurgeryId());
				tempStage.setSurgeryid(surgery.getSourceId());
				if(surgery.getSupplierId() != null){
					OrgSupplierInfoEntity supplier = orgSupplierInfoDao.selectById(surgery.getSupplierId());
					tempStage.setSupplyno(supplier.getSupplierCode());
					tempStage.setSupplyname(supplier.getSupplierName());
					List<UnicodeSupplyShipEntity> shipList = unicodeSupplyShipDao.selectList(new EntityWrapper<UnicodeSupplyShipEntity>()
							.eq("supplier_id", supplier.getId()));
					if(shipList != null && shipList.size() >0){
						tempStage.setSupplyid(shipList.get(0).getSourcesSupplierId());
					}
				}
			}
			tempStage.setStageinfostatus(new BigDecimal(stage.getStatus()));
			tempStage.setStageinfotype(new BigDecimal(stage.getSurgeryStageType()));
			tempStage.setMemo(stage.getMemo());
			if(StringUtils.isBlank(tempStage.getStageinfoid())){
				// 执行新增操作
				tempStage.setCredate(new Date());
				tempStage.setStageinfoid(UUID.randomUUID().toString());
				tempSpdStageinfoDao.insert(tempStage);
			}else{
				tempStage.setEditdate(new Date());
				tempSpdStageinfoDao.update(tempStage, new EntityWrapper<TempSpdStageinfoEntity>()
							.eq("stageinfoid", tempStage.getStageinfoid()));
			}
			
			// 插入跟台目录细单
			List<SurgeryStageDetailInfoEntity> stageDtlList = surgeryStageDetailInfoDao.selectList(new EntityWrapper<SurgeryStageDetailInfoEntity>()
							.eq("surgery_stage_id", stage.getId()));
			for(SurgeryStageDetailInfoEntity stageDtl : stageDtlList){
				
				//先判断是否存在
				List<TempSpdStageinfodtlEntity> tempStageDtlList = tempSpdStageinfodtlDao.selectList(new EntityWrapper<TempSpdStageinfodtlEntity>()
							.eq("stageinfodtlid", stageDtl.getSourceStageDetailId()));
				TempSpdStageinfodtlEntity tempStageDtl = new TempSpdStageinfodtlEntity();
				if(tempStageDtlList != null && tempStageDtlList.size() >0){
					tempStageDtl.setStageinfodtlid(stageDtl.getSourceStageDetailId());
				}
				tempStageDtl.setStageinfodtlid(UUID.randomUUID().toString());
				tempStageDtl.setStageinfoid(tempStage.getStageinfoid());
				GoodsSupplierConsumablesSpecsEntity specs = goodsSupplierConsumablesSpecsDao.selectById(stageDtl.getConsumablesSpecsId());
				tempStageDtl.setGoodsid(specs.getSourcesSpecsId());
				//tempStageDtl.setUnitid();
				tempStageDtl.setPlotno(stageDtl.getPlotNo());
				tempStageDtl.setPproddate(stageDtl.getPlotProddate());
				tempStageDtl.setPvalidto(stageDtl.getPlotValidto());
				tempStageDtl.setSlotno(stageDtl.getSlotNo());
				tempStageDtl.setSproddate(stageDtl.getSlotProddate());
				tempStageDtl.setSvalidto(stageDtl.getSlotValidto());
				tempStageDtl.setBatchprice(new BigDecimal(stageDtl.getPrice()));
				tempStageDtl.setAcceptqty(new BigDecimal(stageDtl.getConsumablesQuantity()));
				tempStageDtl.setFactoryno(stageDtl.getFactoryNo());
				tempStageDtl.setUdflag(1);
				if(tempStageDtl.getStageinfodtlid() == null){
					tempSpdStageinfodtlDao.insert(tempStageDtl);
				}else{
					tempSpdStageinfodtlDao.update(tempStageDtl, new EntityWrapper<TempSpdStageinfodtlEntity>()
							.eq("stageinfodtlid", tempStageDtl.getStageinfodtlid()));
				}
				
			}
			
			//改变跟台目录主单为已上传
			stage.setIsUpload(SupplierIsUploadEnum.YES.getKey());
			surgeryStageInfoDao.updateById(stage);
		}
	}
		
}
