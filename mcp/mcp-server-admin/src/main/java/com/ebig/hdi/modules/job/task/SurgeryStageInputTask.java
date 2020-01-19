package com.ebig.hdi.modules.job.task;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ebig.hdi.common.entity.OrgFactoryInfoEntity;
import com.ebig.hdi.modules.consumables.dao.GoodsSupplierConsumablesSpecsDao;
import com.ebig.hdi.modules.consumables.entity.GoodsSupplierConsumablesSpecsEntity;
import com.ebig.hdi.modules.job.dao.TempSpdStageinfoDao;
import com.ebig.hdi.modules.job.dao.TempSpdStageinfodtlDao;
import com.ebig.hdi.modules.job.entity.ScheduleJobEntity;
import com.ebig.hdi.modules.job.entity.TempSpdStageinfoEntity;
import com.ebig.hdi.modules.job.entity.TempSpdStageinfodtlEntity;
import com.ebig.hdi.modules.org.dao.OrgFactoryInfoDao;
import com.ebig.hdi.modules.org.dao.OrgHospitalInfoDao;
import com.ebig.hdi.modules.surgery.dao.SurgeryInfoDao;
import com.ebig.hdi.modules.surgery.dao.SurgeryStageDetailInfoDao;
import com.ebig.hdi.modules.surgery.dao.SurgeryStageInfoDao;
import com.ebig.hdi.modules.surgery.entity.SurgeryInfoEntity;
import com.ebig.hdi.modules.surgery.entity.SurgeryStageDetailInfoEntity;
import com.ebig.hdi.modules.surgery.entity.SurgeryStageInfoEntity;
import com.ebig.hdi.modules.unicode.dao.UnicodeSupplyShipDao;
import com.ebig.hdi.modules.unicode.entity.UnicodeSupplyShipEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 抽取跟台目录临时表数据
 * @author clang
 *
 */

@Component("SurgeryStageInputTask")
@Slf4j
public class SurgeryStageInputTask implements ITask{
	
	@Autowired
	private TempSpdStageinfoDao tempSpdStageinfoDao;
	@Autowired
	private TempSpdStageinfodtlDao tempSpdStageinfodtlDao;
	@Autowired
	private UnicodeSupplyShipDao unicodeSupplyShipDao;
	@Autowired
	private OrgHospitalInfoDao orgHospitalInfoDao;
	@Autowired
	private SurgeryInfoDao surgeryInfoDao;
	@Autowired
	private SurgeryStageInfoDao surgeryStageInfoDao;
	@Autowired
	private SurgeryStageDetailInfoDao surgeryStageDetailInfoDao;
	@Autowired
	private GoodsSupplierConsumablesSpecsDao goodsSupplierConsumablesSpecsDao;
	@Autowired
	private OrgFactoryInfoDao orgFactoryInfoDao;
	
	private static boolean flag = false;

	@Override
	public void run(ScheduleJobEntity scheduleJob) {
		synchronized (SurgeryStageInputTask.class){
			if(!flag){
				try{
					flag = true;
					log.info("surgeryStageInputTask定时任务正在执行，参数为：{}", scheduleJob.getParams());
					//开始插入数据
					inputSurgeryStage();
					flag = false;
				}catch(Exception e){
					log.info("surgeryStageInputTask定时任务执行失败，参数为：{}", scheduleJob.getParams());
					flag = false;
				}
			}else{
				log.info("上次的surgeryStageInputTask定时任务还没有运行完！");
			}
		}
	}
	
	/**
	 * 把临时表里的跟台目录数据转化为mcp跟台目录表数据
	 */
	/**
	 * 
	 */
	private void inputSurgeryStage(){
		
		//每次抽取300条跟台目录临时表数据，插入跟台目录主表和细表数据
		List<TempSpdStageinfoEntity> tempStageList = tempSpdStageinfoDao.selectAll();
		for(TempSpdStageinfoEntity tempStage : tempStageList){
			try{
				
				//先插入跟台目录主表数据
				List<UnicodeSupplyShipEntity> shipList = unicodeSupplyShipDao.selectList(new EntityWrapper<UnicodeSupplyShipEntity>()
						.eq("sources_hospital_id", tempStage.getUorganid()));
				if(shipList == null || shipList.size() == 0){
					log.error("temp_spd_stageinfo 表中stageinfoid为 " + tempStage.getStageinfoid() 
					+ " 数据插入失败 !查询不出原医院id为 " + tempStage.getUorganid() + " 的绑定对应关系(hdi_unicode_supply_ship)" );
					continue;
				}
				
				// 通过绑定关系表查询出需要插入的医院id与部门id
				Long hospitalId = shipList.get(0).getHospitalId();
				Long deptId = orgHospitalInfoDao.selectById(hospitalId).getDeptId();
				
				//查询出mcp对应的手术单id
				List<SurgeryInfoEntity> surgeryList = surgeryInfoDao.selectList(new EntityWrapper<SurgeryInfoEntity>()
						.eq("source_id", tempStage.getSurgeryid()));
				if(surgeryList == null || surgeryList.size() == 0){
					log.error("temp_spd_stageinfo 表中stageinfoid为 " + tempStage.getStageinfoid() 
					+ " 数据插入失败 !查询不出mcp系统中对应的手术单id" );
					continue;
				}
				Long surgeryId = surgeryList.get(0).getId();
				
				SurgeryStageInfoEntity stage = new SurgeryStageInfoEntity();
				stage.setHospitalId(hospitalId);
				stage.setDeptId(deptId);
				stage.setSurgeryStageNo(tempStage.getStageinfono());
				stage.setSurgeryId(surgeryId);
				stage.setSurgeryStageType(tempStage.getStageinfotype()!=null?tempStage.getStageinfotype().intValue():null);
				stage.setStatus(tempStage.getStageinfostatus()!=null?tempStage.getStageinfostatus().intValue():null);
				stage.setCreateTime(tempStage.getCredate());
				stage.setMemo(tempStage.getMemo());
				surgeryStageInfoDao.insert(stage);
				//插入跟台目录细表数据
				
				//查询出所有这个跟台目录主单的细单临时表数据
				List<TempSpdStageinfodtlEntity> tempStageDtlList = tempSpdStageinfodtlDao.selectList(new EntityWrapper<TempSpdStageinfodtlEntity>()
						.eq("stageinfoid", tempStage.getStageinfoid()));
				for(TempSpdStageinfodtlEntity tempStageDtl : tempStageDtlList){
					List<GoodsSupplierConsumablesSpecsEntity> specsList = goodsSupplierConsumablesSpecsDao.selectList(new EntityWrapper<GoodsSupplierConsumablesSpecsEntity>()
						.eq("sources_specs_id", tempStageDtl.getGoodsid()));
					if(specsList == null || specsList.size() == 0){
						log.error("temp_spd_stageinfo 表中stageinfoid为 " + tempStage.getStageinfoid() + " 数据插入失败 !找不到对应的供应商耗材规格");
						continue;
					}
					
					List<OrgFactoryInfoEntity> factoryList = orgFactoryInfoDao.selectList(new EntityWrapper<OrgFactoryInfoEntity>()
						.eq("factory_code", tempStageDtl.getFactoryno())
						.eq("del_flag", 0));
					
					//插入跟台目录细单数据
					GoodsSupplierConsumablesSpecsEntity specs = specsList.get(0);
					SurgeryStageDetailInfoEntity stageDtl = new SurgeryStageDetailInfoEntity();
					stageDtl.setSurgeryStageId(stage.getId());
					stageDtl.setConsumablesId(specs.getConsumablesId());
					stageDtl.setConsumablesSpecsId(specs.getId());
					stageDtl.setPlotNo(tempStageDtl.getPlotno());
					stageDtl.setPlotProddate(tempStageDtl.getPproddate());
					stageDtl.setPlotValidto(tempStageDtl.getPvalidto());
					stageDtl.setSlotNo(tempStageDtl.getSlotno());
					stageDtl.setSlotProddate(tempStageDtl.getSproddate());
					stageDtl.setSlotValidto(tempStageDtl.getSvalidto());
					if(factoryList != null && factoryList.size() >0){
						stageDtl.setFactoryId(factoryList.get(0).getId());
						stageDtl.setFactoryNo(factoryList.get(0).getFactoryCode());
					}
					stageDtl.setPrice(tempStageDtl.getBatchprice()!=null?tempStageDtl.getBatchprice().doubleValue():null);
					stageDtl.setConsumablesQuantity(tempStageDtl.getAcceptqty()!=null?tempStageDtl.getAcceptqty().doubleValue():null);
					surgeryStageDetailInfoDao.insert(stageDtl);
					
					//插入成功后删除对应的细单数据
					tempSpdStageinfodtlDao.deleteById(tempStageDtl.getStageinfodtlid());
				}
				
				//删除临时表跟台目录数据
				tempSpdStageinfoDao.deleteById(tempStage.getStageinfoid());
				
			}catch(Exception e){
				log.error("temp_spd_stageinfo 表中stageinfoid为 " + tempStage.getStageinfoid() + " 数据插入失败 !");
				continue;
			}
			
		}
		
		
	}
		
}
