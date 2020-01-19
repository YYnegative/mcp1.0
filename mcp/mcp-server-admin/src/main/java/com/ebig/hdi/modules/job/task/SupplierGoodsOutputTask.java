package com.ebig.hdi.modules.job.task;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ebig.hdi.common.enums.SupplierIsUploadEnum;
import com.ebig.hdi.modules.consumables.entity.GoodsSupplierConsumablesEntity;
import com.ebig.hdi.modules.consumables.service.GoodsSupplierConsumablesService;
import com.ebig.hdi.modules.drugs.entity.GoodsSupplierDrugsEntity;
import com.ebig.hdi.modules.drugs.service.GoodsSupplierDrugsService;
import com.ebig.hdi.modules.job.dao.TempPubGoodsDao;
import com.ebig.hdi.modules.job.entity.ScheduleJobEntity;
import com.ebig.hdi.modules.job.entity.TempPubGoodsEntity;
import com.ebig.hdi.modules.reagent.entity.GoodsSupplierReagentEntity;
import com.ebig.hdi.modules.reagent.service.GoodsSupplierReagentService;

import lombok.extern.slf4j.Slf4j;

/**
 * 插入供应商商品数据到临时表中(temp_pub_goods)
 * @author clang
 *
 */

@Component("supplierGoodsOutputTask")
@Slf4j
public class SupplierGoodsOutputTask implements ITask{
	
	@Autowired
	private TempPubGoodsDao tempPubGoodsDao;
	@Autowired
	private GoodsSupplierReagentService goodsSupplierReagentService;
	@Autowired
	private GoodsSupplierDrugsService goodsSupplierDrugsService;
	@Autowired
	private GoodsSupplierConsumablesService goodsSupplierConsumablesService;
	
	private static boolean flag = false;

	@Override
	public void run(ScheduleJobEntity scheduleJob) {
		synchronized (SupplierGoodsOutputTask.class){
			if(!flag){
				try{
					flag = true;
					log.info("supplierGoodsOutputTask定时任务正在执行，参数为：{}", scheduleJob.getParams());
					//开始插入数据
					outputSupplierGoods();
					flag = false;
				}catch(Exception e){
					log.info("supplierGoodsOutputTask定时任务执行失败，参数为：{}", scheduleJob.getParams());
					flag = false;
				}
			}else{
				log.info("上次的supplierGoodsOutputTask定时任务还没有运行完！");
			}
		}
	}
	
	private void outputSupplierGoods(){
		consumablesOutput();
		drugsOutput();
		reagentOutput();
	}
	
	private void consumablesOutput(){
		//查询300条需要上传的供应商耗材数据
		List<TempPubGoodsEntity> goodsList = tempPubGoodsDao.selectAllSupplierConsumables();
		for(TempPubGoodsEntity goods : goodsList){
			try{
				//先查询临时表是否存在主键相同的数据
				List<TempPubGoodsEntity> tempGoodsList = tempPubGoodsDao.selectList(new EntityWrapper<TempPubGoodsEntity>()
						.eq("mgoodsid", goods.getMgoodsid())
						.eq("udflag", 1));
				if(tempGoodsList != null && tempGoodsList.size() >0){
					goods.setEditdate(new Date());
					tempPubGoodsDao.update(goods,new EntityWrapper<TempPubGoodsEntity>()
							.eq("mgoodsid", goods.getMgoodsid()));
				}else{
					//商品所属类别(1：其他，2，耗材，3，药品，4：试剂；)
					goods.setGoodscategorytype(2);
					goods.setUdflag(1);
					goods.setInputdate(new Date());
					tempPubGoodsDao.insert(goods);
				}
				//更新供应商耗材是否上传的字段
				GoodsSupplierConsumablesEntity consumables = new GoodsSupplierConsumablesEntity();
				consumables.setId(goods.getGoodsId());
				consumables.setIsUpload(SupplierIsUploadEnum.YES.getKey());
				goodsSupplierConsumablesService.updateById(consumables);
			}catch(Exception e){
				log.error("hdi_goods_supplier_consumables 表中id为" + goods.getGoodsId() + " 数据上传失败 !");
				continue;
			}
		}
			
	}
	private void drugsOutput(){
		//查询300条需要上传的供应商药品数据
		List<TempPubGoodsEntity> goodsList = tempPubGoodsDao.selectAllSupplierDrugs();
		for(TempPubGoodsEntity goods : goodsList){
			try{
				//先查询临时表是否存在主键相同的数据
				List<TempPubGoodsEntity> tempGoodsList = tempPubGoodsDao.selectList(new EntityWrapper<TempPubGoodsEntity>()
						.eq("mgoodsid", goods.getMgoodsid())
						.eq("udflag", 1));
				if(tempGoodsList != null && tempGoodsList.size() >0){
					goods.setEditdate(new Date());
					tempPubGoodsDao.update(goods,new EntityWrapper<TempPubGoodsEntity>()
							.eq("mgoodsid", goods.getMgoodsid()));
				}else{
					//商品所属类别(1：其他，2，耗材，3，药品，4：试剂；)
					goods.setGoodscategorytype(3);
					goods.setUdflag(1);
					goods.setInputdate(new Date());
					tempPubGoodsDao.insert(goods);
				}
				
				//更新供应商药品是否上传的字段
				GoodsSupplierDrugsEntity drugs = new GoodsSupplierDrugsEntity();
				drugs.setId(goods.getGoodsId());
				drugs.setIsUpload(SupplierIsUploadEnum.YES.getKey());
				goodsSupplierDrugsService.updateById(drugs);
			}catch(Exception e){
				log.error("hdi_goods_supplier_drugs 表中id为" + goods.getGoodsId() + " 数据上传失败 !");
				continue;
			}
		}
		
	}
	
	private void reagentOutput(){
		//查询300条需要上传的供应商试剂数据
		List<TempPubGoodsEntity> goodsList = tempPubGoodsDao.selectAllSupplierReagent();
		for(TempPubGoodsEntity goods : goodsList){
			try{
				//先查询临时表是否存在主键相同的数据
				List<TempPubGoodsEntity> tempGoodsList = tempPubGoodsDao.selectList(new EntityWrapper<TempPubGoodsEntity>()
						.eq("mgoodsid", goods.getMgoodsid())
						.eq("udflag", 1));
				if(tempGoodsList != null && tempGoodsList.size() >0){
					goods.setEditdate(new Date());
					tempPubGoodsDao.update(goods,new EntityWrapper<TempPubGoodsEntity>()
							.eq("mgoodsid", goods.getMgoodsid()));
				}else{
					//商品所属类别(1：其他，2，耗材，3，药品，4：试剂；)
					goods.setGoodscategorytype(4);
					goods.setUdflag(1);
					goods.setInputdate(new Date());
					tempPubGoodsDao.insert(goods);
				}
				
				//更新供应商试剂是否上传的字段
				GoodsSupplierReagentEntity reagent = new GoodsSupplierReagentEntity();
				reagent.setId(goods.getGoodsId());
				reagent.setIsUpload(SupplierIsUploadEnum.YES.getKey());
				goodsSupplierReagentService.updateById(reagent);
			}catch(Exception e){
				log.error("hdi_goods_supplier_reagent 表中id为" + goods.getGoodsId() + " 数据上传失败 !");
				continue;
			}
		}
		
	}
}
