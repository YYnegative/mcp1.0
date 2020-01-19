package com.ebig.hdi.modules.coretransform.task;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.core.dao.CoreStorehouseDao;
import com.ebig.hdi.modules.core.entity.CorePurchaseDetailEntity;
import com.ebig.hdi.modules.core.entity.CorePurchaseMasterEntity;
import com.ebig.hdi.modules.core.entity.CoreStorehouseEntity;
import com.ebig.hdi.modules.core.service.CorePurchaseDetailService;
import com.ebig.hdi.modules.core.service.CorePurchaseMasterService;
import com.ebig.hdi.modules.coretransform.dao.TempHpurorderDao;
import com.ebig.hdi.modules.coretransform.dao.TempHpurorderdtlDao;
import com.ebig.hdi.modules.coretransform.entity.TempHpurorderEntity;
import com.ebig.hdi.modules.coretransform.entity.TempHpurorderdtlEntity;
import com.ebig.hdi.modules.job.entity.ScheduleJobEntity;
import com.ebig.hdi.modules.job.task.ITask;

import lombok.extern.slf4j.Slf4j;

@Component("transformHpurorderTask")
@Slf4j
public class TransformHpurorderTask implements ITask {

	public static final int ZERO = 0;
	public static final int ONE = 1;
	public static final int TWO = 2;
	public static final int THREE = 3;

	private static boolean isRunning = false;

	@Autowired
	private TempHpurorderdtlDao tempHpurorderdtlDao;

	@Autowired
	private CorePurchaseMasterService corePurchaseMasterService;

	@Autowired
	private TempHpurorderDao tempHpurorderDao;

	@Autowired
	private CoreStorehouseDao coreStorehouseDao;

	@Autowired
	private CorePurchaseDetailService corePurchaseDetailService;

	@Override
	public void run(ScheduleJobEntity scheduleJob) {
		synchronized (TransformHpurorderTask.class) {
			if (isRunning) {
				log.info("transformHpurorderTask定时任务已在执行，为避免重复处理数据，将退出本次任务！");
				return;
			}
			isRunning = true;
		}

		try {
			log.info("transformHpurorderTask定时任务正在执行，参数为：{}", scheduleJob.getParams());
			//查询临时表所有采购细单
			List<TempHpurorderdtlEntity> tempHpurorderdtlList = tempHpurorderdtlDao.selectTempHpurorderdtl();
			for (TempHpurorderdtlEntity tempHpurorderdtlEntity : tempHpurorderdtlList) {
				//转换成MCP表采购细单
				CorePurchaseDetailEntity corePurchaseDetailEntity = new CorePurchaseDetailEntity();

				if (tempHpurorderdtlEntity.getHgoodsid() != null) {
					corePurchaseDetailEntity.setYhgoodstypeid(tempHpurorderdtlEntity.getHgoodsid());
				}
				if (tempHpurorderdtlEntity.getHgoodsno() != null) {
					corePurchaseDetailEntity.setYhgoodsno(tempHpurorderdtlEntity.getHgoodsno());
				}
				if (tempHpurorderdtlEntity.getHgoodsname() != null) {
					corePurchaseDetailEntity.setYhgoodsname(tempHpurorderdtlEntity.getHgoodsname());
				}
				if (tempHpurorderdtlEntity.getHgoodstype() != null) {
					corePurchaseDetailEntity.setYhgoodstypename(tempHpurorderdtlEntity.getHgoodstype());
				}
				if (tempHpurorderdtlEntity.getHgoodsid() != null) {
					Map<String, Object> hospitalConsumablesMap = tempHpurorderDao
							.selectHospitalConsumablesBySourcesSpecsId(tempHpurorderdtlEntity.getHgoodsid());
					Map<String, Object> hospitalDrugsMap = tempHpurorderDao
							.selectHospitalDrugsBySourcesSpecsId(tempHpurorderdtlEntity.getHgoodsid());
					Map<String, Object> hospitalReagentMap = tempHpurorderDao
							.selectHospitalReagentBySourcesSpecsId(tempHpurorderdtlEntity.getHgoodsid());

					if (!StringUtil.isEmpty(hospitalConsumablesMap)) {
						corePurchaseDetailEntity.setGoodsclass(THREE);
						corePurchaseDetailEntity
								.setHgoodsid(Long.valueOf(String.valueOf(hospitalConsumablesMap.get("id"))));
						corePurchaseDetailEntity
								.setHgoodsname(hospitalConsumablesMap.get("consumablesName").toString());
						corePurchaseDetailEntity.setHgoodsno(hospitalConsumablesMap.get("consumablesCode").toString());
						corePurchaseDetailEntity
								.setHgoodstypeid(Long.valueOf(String.valueOf(hospitalConsumablesMap.get("specsId"))));
						corePurchaseDetailEntity.setHgoodstype(hospitalConsumablesMap.get("specs").toString());
					} else if (!StringUtil.isEmpty(hospitalDrugsMap)) {
						corePurchaseDetailEntity.setGoodsclass(ONE);
						corePurchaseDetailEntity.setHgoodsid(Long.valueOf(String.valueOf(hospitalDrugsMap.get("id"))));
						corePurchaseDetailEntity.setHgoodsname(hospitalDrugsMap.get("drugsName").toString());
						corePurchaseDetailEntity.setHgoodsno(hospitalDrugsMap.get("drugsCode").toString());
						corePurchaseDetailEntity
								.setHgoodstypeid(Long.valueOf(String.valueOf(hospitalDrugsMap.get("specsId"))));
						corePurchaseDetailEntity.setHgoodstype(hospitalDrugsMap.get("specs").toString());
					} else if (!StringUtil.isEmpty(hospitalReagentMap)) {
						corePurchaseDetailEntity.setGoodsclass(TWO);
						corePurchaseDetailEntity
								.setHgoodsid(Long.valueOf(String.valueOf(hospitalReagentMap.get("id"))));
						corePurchaseDetailEntity.setHgoodsname(hospitalReagentMap.get("reagentName").toString());
						corePurchaseDetailEntity.setHgoodsno(hospitalReagentMap.get("reagentCode").toString());
						corePurchaseDetailEntity
								.setHgoodstypeid(Long.valueOf(String.valueOf(hospitalReagentMap.get("specsId"))));
						corePurchaseDetailEntity.setHgoodstype(hospitalReagentMap.get("specs").toString());
					}
					if (tempHpurorderdtlEntity.getHgoodsunit() != null) {
						corePurchaseDetailEntity.setHgoodsunit(tempHpurorderdtlEntity.getHgoodsunit());
					}
					if (tempHpurorderdtlEntity.getHgoodsqty() != null) {
						corePurchaseDetailEntity.setHqty(tempHpurorderdtlEntity.getHgoodsqty().doubleValue());
					}
					if (tempHpurorderdtlEntity.getHunitprice() != null) {
						corePurchaseDetailEntity.setHunitprice(tempHpurorderdtlEntity.getHunitprice().doubleValue());
					}
					if (tempHpurorderdtlEntity.getHpurorderdtlid() != null) {
						corePurchaseDetailEntity.setOrgdatadtlid(tempHpurorderdtlEntity.getHpurorderdtlid());
					}
					if (tempHpurorderdtlEntity.getSourceid() != null) {
						corePurchaseDetailEntity.setSourceid(tempHpurorderdtlEntity.getSourceid());
					}
					if (tempHpurorderdtlEntity.getSourcedtlid() != null) {
						corePurchaseDetailEntity.setSourcedtlid(tempHpurorderdtlEntity.getSourcedtlid());
					}
					// 根据原数据标识查询是否存在当前采购主单 不存在则新增采购主单
					CorePurchaseMasterEntity corePurchaseMasterEntity = corePurchaseMasterService
							.selectByOrgdataid(tempHpurorderdtlEntity.getHpurorderid());
					if (StringUtil.isEmpty(corePurchaseMasterEntity)) {
						// 不存在，查询临时表采购主单
						TempHpurorderEntity tempHpurorderEntity = tempHpurorderDao
								.selectTempHpurorder(tempHpurorderdtlEntity.getHpurorderid());
						if (!StringUtil.isEmpty(tempHpurorderEntity)) {
							// 临时表采购主单存在
							CorePurchaseMasterEntity purchaseMasterEntity = new CorePurchaseMasterEntity();

							Map<String, Object> map = tempHpurorderDao.getDeptIdAndHorgIdAndSupplierId(
									tempHpurorderEntity.getUorganid(), tempHpurorderEntity.getHorganid());
							if (map == null) {
								continue;
							}
							CoreStorehouseEntity coreStorehouseEntity = coreStorehouseDao
									.selectByOrgdataid(tempHpurorderEntity.getStorehouseid());
							if (coreStorehouseEntity != null) {
								purchaseMasterEntity.setStorehouseid(coreStorehouseEntity.getStorehouseid());
								purchaseMasterEntity.setSupplyAddr(coreStorehouseEntity.getShaddress());
							}
							purchaseMasterEntity.setPurplanno(tempHpurorderEntity.getPurplanno());
							purchaseMasterEntity.setDeptId(Long.valueOf(String.valueOf(map.get("deptId"))));
							purchaseMasterEntity.setHorgId(Long.valueOf(String.valueOf(map.get("horgId"))));
							purchaseMasterEntity.setSupplierId(Long.valueOf(String.valueOf(map.get("supplierId"))));
							if (tempHpurorderEntity.getAnticipate() != null) {
								purchaseMasterEntity
										.setExpecttime(new Timestamp(tempHpurorderEntity.getAnticipate().getTime()));
							}
							purchaseMasterEntity.setPurchasestatus(ONE);
							purchaseMasterEntity.setPurplantime(null);
							purchaseMasterEntity.setDatasource(TWO);
							purchaseMasterEntity.setCremanid(Long.valueOf(tempHpurorderEntity.getCremanid()));
//							purchaseMasterEntity.setCremanname(tempHpurorderEntity.getCremanname());
							purchaseMasterEntity.setCredate(new Timestamp(tempHpurorderEntity.getCredate().getTime()));
							purchaseMasterEntity.setOrgdataid(tempHpurorderEntity.getHpurorderid());
							purchaseMasterEntity.setDelFlag(ZERO);

							// 保存采购主单和细单 同时删除临时表数据
							savePurchaseMasterAndDetail(tempHpurorderdtlEntity, corePurchaseDetailEntity,
									tempHpurorderEntity, purchaseMasterEntity);
						}
					} else {
						// 采购主单存在
						// 保存采购细单 同时删除临时表数据
						savePurchaseDetail(tempHpurorderdtlEntity, corePurchaseDetailEntity, corePurchaseMasterEntity);
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			isRunning = false;
		}
	}

	@Transactional(rollbackFor = Exception.class)
	private void savePurchaseMasterAndDetail(TempHpurorderdtlEntity tempHpurorderdtlEntity,
			CorePurchaseDetailEntity corePurchaseDetailEntity, TempHpurorderEntity tempHpurorderEntity,
			CorePurchaseMasterEntity purchaseMasterEntity) {
		corePurchaseMasterService.saveCorePurchaseMaster(purchaseMasterEntity);
		tempHpurorderDao.deleteTempHpurorder(tempHpurorderEntity.getHpurorderid());

		savePurchaseDetail(tempHpurorderdtlEntity, corePurchaseDetailEntity, purchaseMasterEntity);
	}

	@Transactional(rollbackFor = Exception.class)
	private void savePurchaseDetail(TempHpurorderdtlEntity tempHpurorderdtlEntity,
			CorePurchaseDetailEntity corePurchaseDetailEntity, CorePurchaseMasterEntity corePurchaseMasterEntity) {
		corePurchaseDetailEntity.setOrgdataid(corePurchaseMasterEntity.getSourceid());
		corePurchaseDetailEntity.setPurchaseMasterId(corePurchaseMasterEntity.getPurchaseMasterId());
		List<CorePurchaseDetailEntity> list = corePurchaseDetailService
				.selectList(new EntityWrapper<CorePurchaseDetailEntity>().eq("orgdatadtlid",
						corePurchaseDetailEntity.getOrgdatadtlid()));
		if (StringUtil.isEmpty(list)) {
			corePurchaseDetailService.saveCorePurchaseDetail(corePurchaseDetailEntity);
		} else {
			corePurchaseDetailEntity.setPurchaseDetailId(list.get(0).getPurchaseDetailId());
			corePurchaseDetailService.updateById(corePurchaseDetailEntity);
		}
		tempHpurorderdtlDao.deleteTempHpurorderdtl(tempHpurorderdtlEntity.getHpurorderdtlid());
	}

}
