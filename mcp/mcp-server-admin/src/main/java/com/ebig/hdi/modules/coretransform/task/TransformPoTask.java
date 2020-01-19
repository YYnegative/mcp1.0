package com.ebig.hdi.modules.coretransform.task;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.ebig.hdi.modules.sys.service.SysSequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ebig.hdi.common.enums.DataSourceEnum;
import com.ebig.hdi.common.enums.DelFlagEnum;
import com.ebig.hdi.common.enums.MatchGoodsTypeEnum;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.core.dao.CoreAcceptDetailDao;
import com.ebig.hdi.modules.core.dao.CoreAcceptMasterDao;
import com.ebig.hdi.modules.core.entity.CoreAcceptDetailEntity;
import com.ebig.hdi.modules.core.entity.CoreAcceptMasterEntity;
import com.ebig.hdi.modules.core.entity.CoreLotEntity;
import com.ebig.hdi.modules.coretransform.dao.TempSpdPoDao;
import com.ebig.hdi.modules.coretransform.dao.TempSpdPodtlDao;
import com.ebig.hdi.modules.coretransform.entity.TempSpdPoEntity;
import com.ebig.hdi.modules.coretransform.entity.TempSpdPodtlEntity;
import com.ebig.hdi.modules.job.entity.ScheduleJobEntity;
import com.ebig.hdi.modules.job.task.ITask;

import lombok.extern.slf4j.Slf4j;

/**
 * HDI转换任务验收单转换
 *
 */
@Component("transformPoTask")
@Slf4j
public class TransformPoTask implements ITask {
	
	private static boolean isRunning = false;

	public static final int ZERO = 0;
	public static final int ONE = 1;
	public static final int TWO = 2;
	public static final int THREE = 3;
	
	@Autowired
    private TempSpdPodtlDao tempSpdPodtlDao;
	@Autowired
    private TempSpdPoDao tempSpdPoDao;
	@Autowired
    private CoreAcceptMasterDao coreAcceptMasterDao;
	@Autowired
    private CoreAcceptDetailDao coreAcceptDetailDao;
	@Autowired
	private SysSequenceService sysSequenceService;

	@Override
	public void run(ScheduleJobEntity scheduleJob) {
		synchronized (TransformPoTask.class) {
			if (isRunning) {
				log.info("transformPoTask定时任务已在执行，为避免重复处理数据，将退出本次任务！");
				return;
			}
			isRunning = true;
		}
		
		try {
			log.info("transformPoTask定时任务正在执行，参数为：{}", scheduleJob.getParams());

			// 获取1000条未上传记录
			List<TempSpdPodtlEntity> tempSpdPodtlEntityList = tempSpdPodtlDao.getTempSpdPodtlEntity(1000);

			for (TempSpdPodtlEntity tempSpdPodtl : tempSpdPodtlEntityList) {
				try {
					insertOrUpdate(tempSpdPodtl);
				} catch (Exception e) {
					log.error(e.getMessage());
					continue;
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			isRunning = false;
		}
	}

	private void insertOrUpdate(TempSpdPodtlEntity tempSpdPodtl) {
		//验收主单数据入库
		if(!StringUtil.isEmpty(tempSpdPodtl.getPoid())){
			//根据spd验收主单ID获取临时表验收主单信息
			TempSpdPoEntity tempSpdPoEntity = tempSpdPoDao.selectByPoid(tempSpdPodtl.getPoid());
			//根据spd原验收主单ID获取hdi验收主单
			CoreAcceptMasterEntity acceptMasterEntity = coreAcceptMasterDao.selectByOrgdataid(tempSpdPodtl.getPoid());
			//根据spd验收细单ID获取供货单ID和供货细单ID
			Map<String, Object> supplyMap = tempSpdPodtlDao.getSupplyMasterIdAndSupplyDetailId(tempSpdPodtl.getPodtlid());
			//根据原医院ID和原供应商ID获取平台医院、供应商匹对关系
			Map<String, Object> shipMap = tempSpdPodtlDao.getHospitalSupplierShip(tempSpdPoEntity.getUorganid(), tempSpdPoEntity.getSupplyid());
			if(!StringUtil.isEmpty(tempSpdPoEntity) && !StringUtil.isEmpty(tempSpdPoEntity.getUorganid())  &&  !StringUtil.isEmpty(tempSpdPoEntity.getSupplyid())){				
				//新增验收主单时赋值
				if(StringUtil.isEmpty(acceptMasterEntity)){
					acceptMasterEntity = new CoreAcceptMasterEntity();
					acceptMasterEntity.setOrgdataid(tempSpdPodtl.getPoid());
					acceptMasterEntity.setCredate(new Timestamp(System.currentTimeMillis()));
					acceptMasterEntity.setDelFlag(DelFlagEnum.NORMAL.getKey());
				}
				if(!StringUtil.isEmpty(tempSpdPoEntity.getPono())){
					acceptMasterEntity.setAcceptno(tempSpdPoEntity.getPono());
				}else {
					acceptMasterEntity.setAcceptno(sysSequenceService.selectSeqValueBySeqCode("CORE_ACCEPT_MASTER_ACCEPTNO"));

				}
				if(!StringUtil.isEmpty(shipMap)){
					if(!StringUtil.isEmpty(shipMap.get("supplier_id"))){
						acceptMasterEntity.setSupplierId(Long.valueOf(shipMap.get("supplier_id").toString()));
					}
					if(!StringUtil.isEmpty(shipMap.get("supplier_dept_id"))){
						acceptMasterEntity.setDeptId(Long.valueOf(shipMap.get("supplier_dept_id").toString()));
					}
					if(!StringUtil.isEmpty(shipMap.get("hospital_id"))){
						acceptMasterEntity.setHorgId(Long.valueOf(shipMap.get("hospital_id").toString()));
					}
					if(!StringUtil.isEmpty(shipMap.get("sources_hospital_id"))){
						acceptMasterEntity.setUorganid((String)shipMap.get("sources_hospital_id"));
					}
					if(!StringUtil.isEmpty(shipMap.get("sources_supplier_id"))){
						acceptMasterEntity.setHisSupplyid((String)shipMap.get("sources_supplier_id"));
					}
					//改造后新增存值字段
					acceptMasterEntity.setSourcesSupplierCode((String)shipMap.get("sources_supplier_credit_code"));
					acceptMasterEntity.setSourcesSupplierName((String)shipMap.get("sources_supplier_name"));
					acceptMasterEntity.setSourcesHospitalCode((String)shipMap.get("sources_hospital_credit_code"));
					acceptMasterEntity.setSourcesHospitalName((String)shipMap.get("sources_hospital_name"));
					acceptMasterEntity.setHospitalCode((String)shipMap.get("hospital_code"));
					acceptMasterEntity.setHospitalName((String)shipMap.get("hospital_name"));
					acceptMasterEntity.setSupplierCode((String)shipMap.get("supplier_code"));
					acceptMasterEntity.setSupplierName((String)shipMap.get("supplier_name"));
				}
				
				if(!StringUtil.isEmpty(tempSpdPoEntity.getStorehouseid())){
					acceptMasterEntity.setYstorehouseid(tempSpdPoEntity.getStorehouseid());
					//根据原医院ID和原库房ID获取库房信息
					Map<String, Object> storehouseMap = tempSpdPodtlDao.getStorehouse(tempSpdPoEntity.getUorganid(), tempSpdPoEntity.getStorehouseid());
					if(!StringUtil.isEmpty(storehouseMap) && !StringUtil.isEmpty(storehouseMap.get("store_house_id"))){
						acceptMasterEntity.setStorehouseid(Long.valueOf(storehouseMap.get("store_house_id").toString()));
						acceptMasterEntity.setStorehouseNo((String)storehouseMap.get("store_house_no"));
						acceptMasterEntity.setStorehouseName((String)storehouseMap.get("store_house_name"));
					}
				}
				acceptMasterEntity.setDatasource(DataSourceEnum.PORT.getKey());
				//供货单ID
				if(!StringUtil.isEmpty(supplyMap)){
					acceptMasterEntity.setSourceid(Long.valueOf(supplyMap.get("supply_master_id").toString()));
				}
				
				//acceptMasterEntity.setSettleFlag(settleFlag);
				acceptMasterEntity.setMemo(tempSpdPoEntity.getMemo());
				
			}
			
			//验收细单数据入库
			if(!StringUtil.isEmpty(tempSpdPodtl.getPoid()) && !StringUtil.isEmpty(tempSpdPodtl.getPodtlid())){
				//根据原验收主单和细单ID获取已入库的验收细单信息
				CoreAcceptDetailEntity coreAcceptDetailEntity = coreAcceptDetailDao.selectByOrgdataidAndOrgdatadtlid(tempSpdPodtl.getPoid(), tempSpdPodtl.getPodtlid());
				if(!StringUtil.isEmpty(tempSpdPodtl.getUorganid())  &&  !StringUtil.isEmpty(tempSpdPodtl.getHgoodsid())){
					//根据原医院ID和原医院商品规格ID获取平台医院商品信息
					Map<String, Object> hospitalGoodsSpecs = tempSpdPodtlDao.getHospitalGoodsSpecs(shipMap.get("hospital_id").toString(), tempSpdPodtl.getHgoodsid());
					if(!StringUtil.isEmpty(hospitalGoodsSpecs)){
						//新增验收细单时赋值
						if(StringUtil.isEmpty(coreAcceptDetailEntity)){
							coreAcceptDetailEntity = new CoreAcceptDetailEntity();
							coreAcceptDetailEntity.setOrgdataid(tempSpdPodtl.getPoid());
							coreAcceptDetailEntity.setOrgdatadtlid(tempSpdPodtl.getPodtlid());
							coreAcceptDetailEntity.setCredate(new Timestamp(System.currentTimeMillis()));
						}
						if(!StringUtil.isEmpty(hospitalGoodsSpecs.get("goods_id"))){
							coreAcceptDetailEntity.setGoodsid(Long.valueOf(hospitalGoodsSpecs.get("goods_id").toString()));

						}
						if(!StringUtil.isEmpty(hospitalGoodsSpecs.get("goods_type"))){
							coreAcceptDetailEntity.setGoodsclass(Integer.valueOf(hospitalGoodsSpecs.get("goods_type").toString()));
						}
						if(!StringUtil.isEmpty(hospitalGoodsSpecs.get("goods_specs_id"))){
							coreAcceptDetailEntity.setGoodstypeid(Long.valueOf(hospitalGoodsSpecs.get("goods_specs_id").toString()));

						}
						if(!StringUtil.isEmpty(hospitalGoodsSpecs.get("sources_specs_id"))){
							coreAcceptDetailEntity.setYgoodstypeid(hospitalGoodsSpecs.get("sources_specs_id").toString());
						}
						if(!StringUtil.isEmpty(hospitalGoodsSpecs.get("goods_unit"))){
							coreAcceptDetailEntity.setGoodsunit(hospitalGoodsSpecs.get("goods_unit").toString());
						}
						if(!StringUtil.isEmpty(tempSpdPodtl.getHrgunitqty())){
							coreAcceptDetailEntity.setAcceptQty(tempSpdPodtl.getHrgunitqty().doubleValue());
						}
						if (!StringUtil.isEmpty(tempSpdPodtl)){
							coreAcceptDetailEntity.setGoodsno((String)hospitalGoodsSpecs.get("goods_code"));
							coreAcceptDetailEntity.setGoodsname((String)hospitalGoodsSpecs.get("goods_name"));
							coreAcceptDetailEntity.setGoodstypeno((String)hospitalGoodsSpecs.get("specs_code"));
							coreAcceptDetailEntity.setGoodstype((String)hospitalGoodsSpecs.get("specs"));
							coreAcceptDetailEntity.setYgoodstypeno((String)hospitalGoodsSpecs.get("specs_code"));
							coreAcceptDetailEntity.setYgoodstypename((String)hospitalGoodsSpecs.get("specs"));
						}
						Integer goodstype=Integer.valueOf(hospitalGoodsSpecs.get("goods_type").toString());
						if (goodstype == MatchGoodsTypeEnum.CONSUMABLE.getKey()){
							coreAcceptDetailEntity.setLotno(tempSpdPodtl.getSlotno());
							coreAcceptDetailEntity.setProddate(tempSpdPodtl.getSproddate());
							coreAcceptDetailEntity.setInvadate(tempSpdPodtl.getSinvaliddate());
						}else{
							coreAcceptDetailEntity.setLotno(tempSpdPodtl.getPlotno());
							coreAcceptDetailEntity.setProddate(tempSpdPodtl.getPproddate());
							coreAcceptDetailEntity.setInvadate(tempSpdPodtl.getPinvaliddate());
						}
						coreAcceptDetailEntity.setYgoodsid(tempSpdPodtl.getHgoodsid());
						coreAcceptDetailEntity.setYgoodsno(tempSpdPodtl.getHgoodsno());
						coreAcceptDetailEntity.setYgoodsname(tempSpdPodtl.getHgoodsname());
						
						//供货单和供货细单ID
						if(!StringUtil.isEmpty(supplyMap)){
							coreAcceptDetailEntity.setSourceid(Long.valueOf(supplyMap.get("supply_master_id").toString()));
							coreAcceptDetailEntity.setSourcedtlid(Long.valueOf(supplyMap.get("supply_detail_id").toString()));
						}
						coreAcceptDetailEntity.setMemo(tempSpdPodtl.getMemo());
						
						insertOrUpdate(acceptMasterEntity, coreAcceptDetailEntity);
					}
				}
			}
		}
	}

	/**
	 * 函数功能说明 ：保存或更新验收单信息 <br/>
	 * 修改者名字： <br/>
	 * 修改日期： <br/>
	 * 修改内容：<br/>
	 * 作者：lrx <br/>
	 * 参数：@param acceptMasterEntity <br/>
	 * return：void <br/>
	 */
	@Transactional(rollbackFor = Exception.class)
	private void insertOrUpdate(CoreAcceptMasterEntity acceptMasterEntity, CoreAcceptDetailEntity coreAcceptDetailEntity) {
		//保存或更新验收单信息
		if(StringUtil.isEmpty(acceptMasterEntity.getAcceptMasterId()) && StringUtil.isEmpty(coreAcceptDetailEntity.getAcceptDetailId())){
			coreAcceptMasterDao.insert(acceptMasterEntity);
			coreAcceptDetailEntity.setAcceptMasterId(acceptMasterEntity.getAcceptMasterId());
			coreAcceptDetailDao.insert(coreAcceptDetailEntity);
		}else if(!StringUtil.isEmpty(acceptMasterEntity.getAcceptMasterId()) && StringUtil.isEmpty(coreAcceptDetailEntity.getAcceptDetailId()) && DelFlagEnum.NORMAL.getKey() == acceptMasterEntity.getDelFlag()){
			coreAcceptMasterDao.updateById(acceptMasterEntity);
			coreAcceptDetailEntity.setAcceptMasterId(acceptMasterEntity.getAcceptMasterId());
			coreAcceptDetailDao.insert(coreAcceptDetailEntity);
		}else if(DelFlagEnum.NORMAL.getKey() == acceptMasterEntity.getDelFlag()){
			//只更新未删除的主单信息
			coreAcceptMasterDao.updateById(acceptMasterEntity);
			coreAcceptDetailDao.updateById(coreAcceptDetailEntity);
		}
		//删除验收单临时数据
		tempSpdPoDao.deleteById(coreAcceptDetailEntity.getOrgdataid());
		tempSpdPodtlDao.deleteById(coreAcceptDetailEntity.getOrgdatadtlid());
	}
	
}
