package com.ebig.hdi.modules.coretransform.task;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ebig.hdi.common.enums.SequenceEnum;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.consumables.service.GoodsSupplierConsumablesService;
import com.ebig.hdi.modules.core.dao.CoreLotDao;
import com.ebig.hdi.modules.core.entity.CoreLabelMasterEntity;
import com.ebig.hdi.modules.core.entity.CoreLotEntity;
import com.ebig.hdi.modules.core.entity.CoreStorehouseEntity;
import com.ebig.hdi.modules.core.entity.CoreSupplyDetailEntity;
import com.ebig.hdi.modules.core.entity.CoreSupplyMasterEntity;
import com.ebig.hdi.modules.core.service.CoreLabelMasterService;
import com.ebig.hdi.modules.core.service.CoreStorehouseService;
import com.ebig.hdi.modules.core.service.CoreSupplyDetailService;
import com.ebig.hdi.modules.core.service.CoreSupplyMasterService;
import com.ebig.hdi.modules.coretransform.dao.TempDistributionDao;
import com.ebig.hdi.modules.coretransform.dao.TempDistributiondtlDao;
import com.ebig.hdi.modules.coretransform.dao.TempspdStorehouseDao;
import com.ebig.hdi.modules.coretransform.entity.TempDistributionEntity;
import com.ebig.hdi.modules.coretransform.entity.TempDistributiondtlEntity;
import com.ebig.hdi.modules.coretransform.entity.TempSpdStorehouseEntity;
import com.ebig.hdi.modules.drugs.service.GoodsSupplierDrugsService;
import com.ebig.hdi.modules.job.entity.ScheduleJobEntity;
import com.ebig.hdi.modules.job.task.ITask;
import com.ebig.hdi.modules.reagent.service.GoodsSupplierReagentService;
import com.ebig.hdi.modules.sys.service.SysSequenceService;

import lombok.extern.slf4j.Slf4j;

@Component("transformDistributionTask")
@Slf4j
public class TransformDistributionTask implements ITask {

    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;

    private static AtomicBoolean isRunning = new AtomicBoolean(false);

    //private static Long supplyMasterId = null;

    @Autowired
    private SysSequenceService sysSequenceService;

    @Autowired
    private TempDistributiondtlDao tempDistributiondtlDao;

    @Autowired
    private TempDistributionDao tempDistributionDao;

    @Autowired
    private GoodsSupplierConsumablesService goodsSupplierConsumablesService;

    @Autowired
    private GoodsSupplierDrugsService goodsSupplierDrugsService;

    @Autowired
    private GoodsSupplierReagentService goodsSupplierReagentService;

    @Autowired
    private CoreLotDao coreLotDao;

    @Autowired
    private CoreLabelMasterService coreLabelMasterService;

    @Autowired
    private CoreStorehouseService coreStorehouseService;

    @Autowired
    private TempspdStorehouseDao tempSpdStorehouseDao;

    @Autowired
    private CoreSupplyMasterService coreSupplyMasterService;

    @Autowired
    private CoreSupplyDetailService coreSupplyDetailService;

    @Override
    public void run(ScheduleJobEntity scheduleJob) {
        synchronized (TransformPoTask.class) {
            if (isRunning.get()) {
                log.info("transformDistributionTask定时任务已在执行，为避免重复处理数据，将退出本次任务！");
                return;
            }
            isRunning.set(true);
        }

        try {
            log.info("transformDistributionTask定时任务正在执行，参数为：{}", scheduleJob.getParams());
            List<TempDistributiondtlEntity> tempDistributiondtlEntityList = tempDistributiondtlDao
                    .selectTempDistributiondtl();

            if (CollectionUtils.isNotEmpty(tempDistributiondtlEntityList)) {
                List<String> sourcesIds = tempDistributiondtlEntityList.stream().map(TempDistributiondtlEntity::getSgoodsid).collect(Collectors.toList());
                List<Map<String, Object>> consumablesMap = goodsSupplierConsumablesService
                        .selectBySourcesIds(sourcesIds);
                List<Map<String, Object>> drugsMap = goodsSupplierDrugsService.selectBySourcesIds(sourcesIds);
                List<Map<String, Object>> reagentMap = goodsSupplierReagentService
                        .selectBySourcesIds(sourcesIds);
                CoreSupplyDetailEntity coreSupplyDetailEntity = null;
                for (TempDistributiondtlEntity entity : tempDistributiondtlEntityList) {//遍历所有的细单数据
                    coreSupplyDetailEntity = new CoreSupplyDetailEntity();
                    if (entity.getSourceid() != null) {
                        coreSupplyDetailEntity.setSourceid(Long.valueOf(entity.getSourceid()));
                    }
                    if (entity.getSourcedtlid() != null) {
                        coreSupplyDetailEntity.setSourcedtlid(Long.valueOf(entity.getSourcedtlid()));
                    }
                    if (entity.getPurplandocid() != null) {
                        coreSupplyDetailEntity.setPurplanid(entity.getPurplandocid());
                    }
                    if (entity.getPurplandtlid() != null) {
                        coreSupplyDetailEntity.setPurplandtlid(entity.getPurplandtlid());
                    }
                    if (entity.getSgoodsid() != null) {
                        if (CollectionUtils.isNotEmpty(consumablesMap)) {
                            for (Map<String, Object> map : consumablesMap) {
                                if (StringUtils.isNotEmpty((CharSequence) map.get("sourcesSpecsId"))) {
                                    setValues(coreSupplyDetailEntity, Long.valueOf(String.valueOf(map.get("sgoodsid"))), THREE, map.get("sgoodsno").toString(), Long.valueOf(String.valueOf(map.get("sgoodstypeid"))), map.get("sgoodstype").toString(), map.get("sgoodsunit").toString());
                                    break;
                                }
                            }

                        } else if (CollectionUtils.isNotEmpty(drugsMap)) {
                            for (Map<String, Object> map : drugsMap) {
                                if (StringUtils.isNotEmpty((CharSequence) map.get("sourcesSpecsId"))) {
                                    setValues(coreSupplyDetailEntity, Long.valueOf(String.valueOf(map.get("sgoodsid"))), ONE, map.get("sgoodsno").toString(), Long.valueOf(String.valueOf(map.get("sgoodstypeid"))), map.get("sgoodstype").toString(), map.get("sgoodsunit").toString());
                                    break;
                                }
                            }

                        } else if (CollectionUtils.isNotEmpty(reagentMap)) {
                            for (Map<String, Object> map : drugsMap) {
                                if (StringUtils.isNotEmpty((CharSequence) map.get("sourcesSpecsId"))) {
                                    setValues(coreSupplyDetailEntity, Long.valueOf(String.valueOf(map.get("sgoodsid"))), TWO, map.get("sgoodsno").toString(), Long.valueOf(String.valueOf(map.get("sgoodstypeid"))), map.get("sgoodstype").toString(), map.get("sgoodsunit").toString());
                                    break;
                                }
                            }

                        } else {
                            continue;
                        }
                        if (entity.getOrderquantity() != null) {
                            coreSupplyDetailEntity.setSupplyQty(entity.getOrderquantity().doubleValue());
                        }
                        if (entity.getSunitprice() != null) {
                            coreSupplyDetailEntity.setSupplyUnitprice(entity.getSunitprice().doubleValue());
                        }
                        Map<String, Object> map = coreLotDao.getDeptId(entity.getUorganid(), entity.getHorganid());
                        if (map == null) {
                            continue;
                        }
                        if (entity.getPlotno() != null) {
                            List<CoreLotEntity> coreLotEntityList = coreLotDao.selectByLotno(entity.getPlotno());
                            if (!coreLotEntityList.isEmpty()) {
                                coreSupplyDetailEntity.setLotid(coreLotEntityList.get(0).getLotid());
                            } else {
                                CoreLotEntity lotEntity = getCoreLotEntity(coreSupplyDetailEntity, entity, map, ONE);
                                coreLotDao.insert(lotEntity);
                                coreSupplyDetailEntity.setLotid(lotEntity.getLotid());
                            }
                        }
                        if (entity.getSlotno() != null) {
                            List<CoreLotEntity> coreLotEntityList = coreLotDao.selectByLotno(entity.getSlotno());
                            if (!coreLotEntityList.isEmpty()) {
                                coreSupplyDetailEntity.setLotid(coreLotEntityList.get(0).getLotid());
                            } else {
                                CoreLotEntity lotEntity = getCoreLotEntity(coreSupplyDetailEntity, entity, map, TWO);
                                coreLotDao.insert(lotEntity);
                                coreSupplyDetailEntity.setLotid(lotEntity.getLotid());
                            }
                        }
                        if (entity.getDistributionid() != null) {
                            coreSupplyDetailEntity.setOrgdataid(entity.getDistributionid());
                        }
                        if (entity.getDistributiondtlid() != null) {
                            coreSupplyDetailEntity.setOrgdatadtlid(entity.getDistributiondtlid());
                        }
                        coreSupplyDetailEntity.setCremanid(Long.valueOf(String.valueOf(map.get("userId"))));
//                        coreSupplyDetailEntity.setCremanname(String.valueOf(map.get("username")));
                        coreSupplyDetailEntity.setCredate(new Timestamp(entity.getCredate().getTime()));
                        //查询对应的库房标识
                        CoreStorehouseEntity coreStorehouseEntity = new CoreStorehouseEntity();
                        if (entity.getAddressid() != null) {
                            TempSpdStorehouseEntity spdStorehouseEntity = tempSpdStorehouseDao
                                    .selectByShaddress(entity.getAddressid(), entity.getHorganid());
                            if (spdStorehouseEntity != null) {
                                CoreStorehouseEntity storehouseEntity = coreStorehouseService
                                        .selectByOrgdataid(spdStorehouseEntity.getStorehouseid());
                                if (storehouseEntity != null) {
                                    coreStorehouseEntity.setStorehouseid(storehouseEntity.getStorehouseid());
                                } else {
                                    continue;
                                }
                            } else {
                                continue;
                            }
                        }
                        //根据细单的主单标识找到主单
                        TempDistributionEntity tempDistributionEntity = tempDistributionDao
                                .selectByDistributionid(entity.getDistributionid());
                        //根据原数据标识找到mcp供货主单数据
                        CoreSupplyMasterEntity supplyMasterEntity = coreSupplyMasterService
                                .selectByOrgdataid(entity.getDistributionid());
                        //查询原主单存在，则为第一条原细单转换时
                        if (tempDistributionEntity != null) {
                            CoreSupplyMasterEntity coreSupplyMasterEntity = new CoreSupplyMasterEntity();
                            //不存在则新增主单
                            if (supplyMasterEntity == null) {
                                coreSupplyMasterEntity.setSupplyno(sysSequenceService
                                        .selectSeqValueBySeqCode(SequenceEnum.CORE_SUPPLY_MASTER_NO.getKey()));
                                coreSupplyMasterEntity.setSalno(tempDistributionEntity.getSalno());
                                Map<String, Object> horgIdMap = tempDistributionDao.getDeptIdAndHorgIdAndSupplierId(
                                        tempDistributionEntity.getUorganid(), tempDistributionEntity.getHorganid());
                                if (horgIdMap == null) {
                                    continue;
                                }
                                coreSupplyMasterEntity.setDeptId(Long.valueOf(String.valueOf(horgIdMap.get("deptId"))));
                                coreSupplyMasterEntity.setHorgId(Long.valueOf(String.valueOf(horgIdMap.get("horgId"))));
                                coreSupplyMasterEntity
                                        .setSupplierId(Long.valueOf(String.valueOf(horgIdMap.get("supplierId"))));
                                coreSupplyMasterEntity.setStorehouseid(coreStorehouseEntity.getStorehouseid());
                                coreSupplyMasterEntity.setPurplanno(null);//待修改
                                if (tempDistributionEntity.getSaldate() != null) {
                                    coreSupplyMasterEntity
                                            .setSupplyTime(new Timestamp(tempDistributionEntity.getSaldate().getTime()));
                                }
                                coreSupplyMasterEntity.setSupplyType(ONE);
                                coreSupplyMasterEntity.setSupplyStatus(ZERO);
                                Map<String, Object> selectShaddress = tempSpdStorehouseDao
                                        .selectShaddress(entity.getAddressid(), entity.getHorganid());
                                coreSupplyMasterEntity.setSupplyAddr(selectShaddress.get("shaddress").toString());
                                coreSupplyMasterEntity.setDatasource(TWO);
                                coreSupplyMasterEntity.setDelFlag(ZERO);
                                if (entity.getSourceid() != null) {
                                    coreSupplyMasterEntity.setSourceid(Long.valueOf(String.valueOf(entity.getSourceid())));
                                } else {
                                    coreSupplyMasterEntity.setSourceid(null);
                                }
                                coreSupplyMasterEntity.setCremanid(Long.valueOf(String.valueOf(map.get("userId"))));
//                                coreSupplyMasterEntity.setCremanname(map.get("username").toString());
                                coreSupplyMasterEntity.setCredate(new Timestamp(System.currentTimeMillis()));
                                coreSupplyMasterEntity.setOrgdataid(tempDistributionEntity.getDistributionid());
                                saveSupplyMasterAndDetail(entity, coreSupplyDetailEntity, tempDistributionEntity,
                                        coreSupplyMasterEntity);
                            } else {
                                saveSupplyDetail(entity, coreSupplyDetailEntity, supplyMasterEntity);
                            }
                            //查询原表的标签是否存在   不存在则创建
                            if (entity.getLabelno() != null) {
                                CoreLabelMasterEntity labelMasterEntity = coreLabelMasterService
                                        .selectByLabelno(entity.getLabelno());
                                if (labelMasterEntity == null) {
                                    CoreLabelMasterEntity labelMaster = getCoreLabelMaster(entity, map, coreStorehouseEntity, coreSupplyMasterEntity);
                                    coreLabelMasterService.saveLabel(labelMaster);
                                }
                            }
                        } else {//原细单第二条++转换时
                            //查询原表的标签是否存在   不存在则创建
                            if (entity.getLabelno() != null) {
                                CoreLabelMasterEntity labelMasterEntity = coreLabelMasterService
                                        .selectByLabelno(entity.getLabelno());
                                if (labelMasterEntity == null) {
                                    CoreLabelMasterEntity labelMaster = getCoreLabelMaster(entity, map, coreStorehouseEntity, supplyMasterEntity);
                                    coreLabelMasterService.saveLabel(labelMaster);
                                }
                            }
                            saveSupplyDetail(entity, coreSupplyDetailEntity, supplyMasterEntity);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            isRunning.set(false);
        }
    }

    /**
     * 设置coreSupplyDetailEntity对象的值
     * @param coreSupplyDetailEntity
     * @param sgoodsid
     * @param goodsclass
     * @param sgoodsno
     * @param sgoodstypeid
     * @param sgoodstype
     * @param sgoodsunit
     */
    private void setValues(CoreSupplyDetailEntity coreSupplyDetailEntity, Long sgoodsid, int goodsclass, String sgoodsno, Long sgoodstypeid, String sgoodstype, String sgoodsunit) {
        coreSupplyDetailEntity.setGoodsid(sgoodsid);
        coreSupplyDetailEntity.setGoodsclass(goodsclass);
        coreSupplyDetailEntity.setGoodsno(sgoodsno);
        coreSupplyDetailEntity
                .setGoodstypeid(sgoodstypeid);
        coreSupplyDetailEntity.setGoodstype(sgoodstype);
        coreSupplyDetailEntity.setGoodsunit(sgoodsunit);
    }

    /**
     * 构造CoreLotEntity实体对象
     * @param entity
     * @param map
     * @param coreStorehouseEntity
     * @param supplyMasterEntity
     * @return
     */
    private CoreLabelMasterEntity getCoreLabelMaster(TempDistributiondtlEntity entity, Map<String, Object> map, CoreStorehouseEntity coreStorehouseEntity, CoreSupplyMasterEntity supplyMasterEntity) {
        CoreLabelMasterEntity labelMaster = new CoreLabelMasterEntity();
        labelMaster.setDeptId(Long.valueOf(String.valueOf(map.get("deptId"))));
        labelMaster.setLabelno(entity.getLabelno());
        labelMaster.setHorgId(Long.valueOf(String.valueOf(map.get("hospitalId"))));
        labelMaster.setStorehouseid(coreStorehouseEntity.getStorehouseid());
        labelMaster.setLabelstatus(ZERO);
        //存储主单后赋值
        labelMaster.setSourceid(supplyMasterEntity.getSupplyMasterId());
        labelMaster.setCremanid(Long.valueOf(String.valueOf(map.get("userId"))));
        labelMaster.setCremanname(map.get("username").toString());
        labelMaster.setCredate(new Timestamp(System.currentTimeMillis()));
        labelMaster.setSupplierId(Long.valueOf(map.get("supplierId").toString()));
        labelMaster.setDelFlag(ZERO);
        return labelMaster;
    }

    /**
     * 构造CoreLotEntity实体对象
     * @param coreSupplyDetailEntity
     * @param entity
     * @param map
     * @param lottype
     * @return
     */
    private CoreLotEntity getCoreLotEntity(CoreSupplyDetailEntity coreSupplyDetailEntity,
                                                 TempDistributiondtlEntity entity, Map<String, Object> map, int lottype) {
        CoreLotEntity lotEntity = new CoreLotEntity();
        lotEntity.setDeptId(Long.valueOf(String.valueOf(map.get("deptId"))));
        lotEntity.setSupplierId(Long.valueOf(map.get("supplierId").toString()));
        lotEntity.setGoodsid(coreSupplyDetailEntity.getGoodsid());
        lotEntity.setGoodsclass(coreSupplyDetailEntity.getGoodsclass());
        lotEntity.setGoodstypeid(coreSupplyDetailEntity.getGoodstypeid());
        lotEntity.setLottype(lottype);
        lotEntity.setLotstatus(ONE);
        lotEntity.setLotno(entity.getPlotno());
        lotEntity.setProddate(entity.getPproddate());
        lotEntity.setInvadate(entity.getPinvaliddate());
        lotEntity.setDelFlag(ONE);
        return lotEntity;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveSupplyMasterAndDetail(TempDistributiondtlEntity entity, CoreSupplyDetailEntity coreSupplyDetailEntity,
                                           TempDistributionEntity tempDistributionEntity, CoreSupplyMasterEntity coreSupplyMasterEntity) {
        coreSupplyMasterService.saveMaster(coreSupplyMasterEntity);
        //supplyMasterId = coreSupplyMasterEntity.getSupplyMasterId();
        tempDistributionDao.deleteTempDistribution(tempDistributionEntity.getDistributionid());
        saveSupplyDetail(entity, coreSupplyDetailEntity, coreSupplyMasterEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveSupplyDetail(TempDistributiondtlEntity entity, CoreSupplyDetailEntity coreSupplyDetailEntity,
                                  CoreSupplyMasterEntity supplyMasterEntity) {
        coreSupplyDetailEntity.setSupplyMasterId(supplyMasterEntity.getSupplyMasterId());
        List<CoreSupplyDetailEntity> list = coreSupplyDetailService.selectList(new EntityWrapper<CoreSupplyDetailEntity>().eq("orgdatadtlid", coreSupplyDetailEntity.getOrgdatadtlid()));
        if (StringUtil.isEmpty(list)) {
            coreSupplyDetailService.saveSupplyDetail(coreSupplyDetailEntity);
        } else {
            coreSupplyDetailEntity.setSupplyDetailId(list.get(0).getSupplyDetailId());
            coreSupplyDetailService.updateById(coreSupplyDetailEntity);
        }
        tempDistributiondtlDao.deleteTempDistributiondtl(entity.getDistributiondtlid());
    }
}
