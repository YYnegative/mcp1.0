package com.ebig.hdi.modules.job.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ebig.hdi.modules.consumables.service.GoodsSupplierConsumablesApprovalsService;
import com.ebig.hdi.modules.consumables.service.GoodsSupplierConsumablesService;
import com.ebig.hdi.modules.consumables.service.GoodsSupplierConsumablesSpecsService;
import com.ebig.hdi.modules.drugs.entity.GoodsSupplierDrugsSpecsEntity;
import com.ebig.hdi.modules.drugs.service.GoodsSupplierDrugsService;
import com.ebig.hdi.modules.drugs.service.GoodsSupplierDrugsSpecsService;
import com.ebig.hdi.modules.job.dao.TempPubGoodsDao;
import com.ebig.hdi.modules.job.dao.TempSpdSurgeryDao;
import com.ebig.hdi.modules.job.entity.ScheduleJobEntity;
import com.ebig.hdi.modules.org.service.OrgFactoryInfoService;
import com.ebig.hdi.modules.reagent.service.GoodsSupplierReagentService;
import com.ebig.hdi.modules.reagent.service.GoodsSupplierReagentSpecsService;
import com.ebig.hdi.modules.surgery.dao.SurgeryInfoDao;
import com.ebig.hdi.modules.surgery.entity.SurgeryInfoEntity;
import com.ebig.hdi.modules.sys.service.SysSequenceService;

import lombok.extern.slf4j.Slf4j;

/**
 * 抽取手术单临时表数据
 * @author clang
 *
 */

@Component("SurgeryInputTask")
@Slf4j
public class SurgeryInputTask implements ITask{
	
	@Autowired
	private TempSpdSurgeryDao tempSpdSurgeryDao;
	@Autowired
	private SurgeryInfoDao surgeryInfoDao;
	
	private static boolean flag = false;

	@Override
	public void run(ScheduleJobEntity scheduleJob) {
		synchronized (SurgeryInputTask.class){
			if(!flag){
				flag = true;
				log.info("surgeryInputTask定时任务正在执行，参数为：{}", scheduleJob.getParams());
				//开始插入数据
				inputSurgery();
				flag = false;
			}else{
				log.info("上次的surgeryInputTask定时任务还没有运行完！");
			}
		}
	}
	
	/**
	 * 把临时表里的手术单数据转化为mcp跟台目录数据
	 */
	private void inputSurgery(){
		// 每次从临时表查询300条手术单进行同步
		List<SurgeryInfoEntity> tempSurgeryList = tempSpdSurgeryDao.selectAll();
		for(SurgeryInfoEntity surgery : tempSurgeryList){
			try{
				
				//先查询是否存在source_id相同的手术单，如果存在则修改，不存在则新增
				List<SurgeryInfoEntity> surgeryList = surgeryInfoDao.selectList(new EntityWrapper<SurgeryInfoEntity>()
								.eq("source_id", surgery.getSourceId()));
				if(surgeryList!=null && surgeryList.size() > 0){
					//存在source_id相同的数据，update
					surgery.setId(surgeryList.get(0).getId());
					surgeryInfoDao.updateById(surgery);
				}else{
					//不存在source_id相同的数据，insert
					surgeryInfoDao.insert(surgery);
				}
				
				//最后删除对应临时表里的数据
				tempSpdSurgeryDao.deleteById(surgery.getSourceId());
				
			}catch(Exception e){
				log.info("temp_spd_surgery表中id为 " + surgery.getSourceId() + " 的手术单同步失败");
				continue;
			}
			
		}
	}
		
}
