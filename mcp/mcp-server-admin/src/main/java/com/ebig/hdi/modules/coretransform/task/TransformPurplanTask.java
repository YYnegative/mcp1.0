package com.ebig.hdi.modules.coretransform.task;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ebig.hdi.common.enums.DataSourceEnum;
import com.ebig.hdi.common.enums.DelFlagEnum;
import com.ebig.hdi.common.enums.PurchaseStatusEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.core.dao.CorePurchaseDetailDao;
import com.ebig.hdi.modules.core.dao.CorePurchaseMasterDao;
import com.ebig.hdi.modules.core.dao.CoreStorehouseDao;
import com.ebig.hdi.modules.core.entity.CorePurchaseDetailEntity;
import com.ebig.hdi.modules.core.entity.CorePurchaseMasterEntity;
import com.ebig.hdi.modules.core.entity.CoreStorehouseEntity;
import com.ebig.hdi.modules.coretransform.dao.SpdPurplandocDao;
import com.ebig.hdi.modules.coretransform.dao.SpdPurplandtlDao;
import com.ebig.hdi.modules.coretransform.entity.SpdPurplandocEntity;
import com.ebig.hdi.modules.coretransform.entity.SpdPurplandtlEntity;
import com.ebig.hdi.modules.job.entity.ScheduleJobEntity;
import com.ebig.hdi.modules.job.task.ITask;
import com.ebig.hdi.modules.org.entity.OrgHospitalInfoEntity;
import com.ebig.hdi.modules.org.entity.OrgSupplierInfoEntity;
import com.ebig.hdi.modules.org.service.OrgHospitalInfoService;
import com.ebig.hdi.modules.org.service.OrgSupplierInfoService;
import com.ebig.hdi.modules.unicode.dao.UnicodeSupplyShipDao;
import com.ebig.hdi.modules.unicode.entity.UnicodeSupplyShipEntity;

import lombok.extern.slf4j.Slf4j;

@Component("transformPurplanTask")
@Slf4j
public class TransformPurplanTask implements ITask {

    private static boolean isRunning = false;

    @Autowired
    private SpdPurplandocDao spdPurplandocDao;

    @Autowired
    private SpdPurplandtlDao spdPurplandtlDao;

    @Autowired
    private CorePurchaseMasterDao corePurchaseMasterDao;

    @Autowired
    private CorePurchaseDetailDao corePurchaseDetailDao;

    @Autowired
    private CoreStorehouseDao coreStorehouseDao;

    @Autowired
    private UnicodeSupplyShipDao unicodeSupplyShipDao;

    @Autowired
    private OrgSupplierInfoService orgSupplierInfoService;

    @Autowired
    private OrgHospitalInfoService orgHospitalInfoService;

    @Override
    public void run(ScheduleJobEntity scheduleJob) {
        synchronized (TransformPurplanTask.class) {
            if (isRunning) {
                log.info("transformPurplanTask定时任务已在执行，为避免重复处理数据，将退出本次任务！");
                return;
            }
            isRunning = true;
        }

        try {
            log.info("transformPurplanTask定时任务正在执行，参数为：{}", scheduleJob.getParams());
            // 查询临时表所有采购计划细单
            List<SpdPurplandtlEntity> purplandtlList = spdPurplandtlDao.queryList(1000);
            Set<String> spdPurplandocIdSet = new HashSet<>();
            Set<String> removeIdSet = new HashSet<>();

            for (SpdPurplandtlEntity purplandtl : purplandtlList) {
                try {
                    CorePurchaseDetailEntity purchaseDetail = transferToPurchaseDetail(purplandtl);
                    // 查询mcp表中采购主单是否存在
                    CorePurchaseMasterEntity purchaseMaster = corePurchaseMasterDao.selectByOrgdataid(purplandtl.getPurplandocid());

                    if (StringUtil.isEmpty(purchaseMaster)) {
                        purchaseMaster = new CorePurchaseMasterEntity();
                        // 查询临时表采购主单
                        SpdPurplandocEntity purplandoc = spdPurplandocDao.selectById(purplandtl.getPurplandocid());
                        if (StringUtil.isEmpty(purplandoc)) {
                            log.error("临时表中不存在记录，采购主单id:{}", purplandtl.getPurplandocid());
                            throw new HdiException("临时表采购主单不存在");
                        }
                        //采购计划编号
                        purchaseMaster.setPurplanno(purplandoc.getPurplanno());

                        // 查询mcp医院 供应商
                        UnicodeSupplyShipEntity ship = unicodeSupplyShipDao.selectBySourcesSupplierIdAndSourcesHospitalId(purplandoc.getSupplyid(), purplandoc.getUorganid());
                        if (StringUtil.isEmpty(ship)) {
                            log.error("mcp匹对关系表中不存在记录，原系统供应商id:{}，原系统医院id:{}", purplandoc.getSupplyid(), purplandoc.getUorganid());
                            throw new HdiException("mcp表中对应的医院或供应商不存在");
                        }
                        purchaseMaster.setSourcesSupplierId(ship.getSourcesSupplierId());
                        purchaseMaster.setSourcesSupplierCode(ship.getSourcesSupplierCreditCode());
                        purchaseMaster.setSourcesSupplierName(ship.getSourcesSupplierName());
                        //查询平台供应商信息
                        if (!StringUtil.isEmpty(ship.getSupplierId())) {
                            OrgSupplierInfoEntity entity = orgSupplierInfoService.selectById(ship.getSupplierId());
                            if (null != entity) {
                                purchaseMaster.setSupplierId(entity.getId());
                                purchaseMaster.setSupplierCode(entity.getSupplierCode());
                                purchaseMaster.setSupplierName(entity.getSupplierName());
                            }
                        }
                        purchaseMaster.setSourcesHospitalId(ship.getSourcesHospitalId());
                        purchaseMaster.setSourcesHospitalCode(ship.getSourcesHospitalCreditCode());
                        purchaseMaster.setSourcesHospitalName(ship.getSourcesHospitalName());

                        //查询平台医院信息
                        if (!StringUtil.isEmpty(ship.getHospitalId())) {
                            OrgHospitalInfoEntity entity = orgHospitalInfoService.selectById(ship.getHospitalId());
                            if (null != entity) {
                                purchaseMaster.setHorgId(entity.getId());
                                purchaseMaster.setHospitalCode(entity.getHospitalCode());
                                purchaseMaster.setHospitalName(entity.getHospitalName());
                            }

                        }
                        // 查询mcp库房
                        CoreStorehouseEntity storehouse = coreStorehouseDao.selectByOrgdataid(purplandoc.getStorehouseid());
                        if (StringUtil.isEmpty(storehouse)) {
                            log.error("mcp库房表中不存在记录，原系统id:{}", purplandoc.getStorehouseid());
                            throw new HdiException("mcp表库房不存在");
                        }
                        purchaseMaster.setSourcesStorehouseId(storehouse.getOrgdataid());
                        purchaseMaster.setStorehouseid(storehouse.getStorehouseid());
                        purchaseMaster.setStorehouseNo(storehouse.getStorehouseno());
                        purchaseMaster.setStorehouseName(storehouse.getStorehousename());
                        // XXX 采购时间
                        purchaseMaster.setPurplantime(purplandoc.getCommitdate());
                        purchaseMaster.setExpecttime(purplandoc.getAnticipate());
                        purchaseMaster.setPurchasestatus(PurchaseStatusEnum.UNCONFIRMED.getKey());
                        purchaseMaster.setSupplyAddr(storehouse.getShaddress());
                        purchaseMaster.setOrgdataid(purplandoc.getPurplandocid());
                        purchaseMaster.setSourceid(purplandoc.getOrgdataid());
                        purchaseMaster.setDatasource(DataSourceEnum.PORT.getKey());
                        purchaseMaster.setDeptId(ship.getDeptId());
                        purchaseMaster.setDelFlag(DelFlagEnum.NORMAL.getKey());
                        purchaseMaster.setMemo(purplandoc.getMemo());
                        if (!StringUtil.isEmpty(purplandoc.getCremanid())) {
                            purchaseMaster.setCremanid(Long.parseLong(purplandoc.getCremanid()));
                        }
                        purchaseMaster.setCredate(new Timestamp(purplandoc.getCredate().getTime()));
                    }
                    // 保存采购单到mcp表
                    savePurchase(purchaseMaster, purchaseDetail);
                    spdPurplandocIdSet.add(purchaseMaster.getOrgdataid());

                } catch (Exception e) {
                    log.error(MessageFormat.format("采购计划细单id:{0}，记录入库失败！", purplandtl.getPurplandtlid()), e);
                    removeIdSet.add(purplandtl.getPurplandocid());
                    continue;
                }
            }
            //统一删除临时采购计划主单,排除细单转换失败的主单
           spdPurplandocIdSet.removeAll(removeIdSet);
            spdPurplandocDao.BatchDelPurplandoc(spdPurplandocIdSet);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            isRunning = false;
        }

    }

    /**
     * 保存采购主单和采购细单
     *
     * @param purchaseMaster
     * @param purchaseDetail
     */
    @Transactional(rollbackFor = Exception.class)
    private void savePurchase(CorePurchaseMasterEntity purchaseMaster, CorePurchaseDetailEntity purchaseDetail) {
        CorePurchaseMasterEntity entity = corePurchaseMasterDao.selectByOrgdataid(purchaseMaster.getOrgdataid());
        if(null != entity ){
            if(entity.getPurchasestatus().equals(PurchaseStatusEnum.UNCONFIRMED.getKey()) || entity.getPurchasestatus().equals(PurchaseStatusEnum.EXPIRED.getKey())){
                corePurchaseMasterDao.updateById(purchaseMaster);
                CorePurchaseDetailEntity detailEntity = corePurchaseDetailDao.selectByOrgdatadtlid(purchaseDetail.getOrgdatadtlid());
                purchaseDetail.setPurchaseMasterId(purchaseMaster.getPurchaseMasterId());
                purchaseDetail.setOrgdataid(purchaseMaster.getOrgdataid());
                if(detailEntity != null){
                    purchaseDetail.setOrgdataid(purchaseMaster.getOrgdataid());
                    corePurchaseDetailDao.updateById(purchaseDetail);
                }else {
                    corePurchaseDetailDao.insert(purchaseDetail);
                }
            }
            //删除临时表细单，因为外键原因主单统一删除完细单后删除，
            spdPurplandtlDao.deleteById(purchaseDetail.getOrgdatadtlid());
//            spdPurplandocDao.deleteById(entity.getOrgdataid());
        }else {
            //新增采购主单，
            corePurchaseMasterDao.insert(purchaseMaster);
            //新增采购细单，
            purchaseDetail.setPurchaseMasterId(purchaseMaster.getPurchaseMasterId());
            purchaseDetail.setOrgdataid(purchaseMaster.getOrgdataid());
            corePurchaseDetailDao.insert(purchaseDetail);
            //删除临时表细单，因为外键原因主单统一删除完细单后删除，
            spdPurplandtlDao.deleteById(purchaseDetail.getOrgdatadtlid());
//            spdPurplandocDao.deleteById(purchaseMaster.getOrgdataid());
        }


    }

    /**
     * 转换成MCP表采购细单
     *
     * @param purplandtl
     * @return MCP表采购细单
     */
    private CorePurchaseDetailEntity transferToPurchaseDetail(SpdPurplandtlEntity purplandtl) {
        CorePurchaseDetailEntity purchaseDetail = new CorePurchaseDetailEntity();
        purchaseDetail.setYhgoodsno(purplandtl.getHgoodsno());
        purchaseDetail.setYhgoodsname(purplandtl.getHgoodsname());
        purchaseDetail.setYhgoodstypeid(purplandtl.getHgoodsid());
        purchaseDetail.setYhgoodstypename(purplandtl.getHgoodstype());
        purchaseDetail.setHgoodsunit(purplandtl.getHgoodsunit());
        if(purplandtl.getPurplanqty() != null){
            purchaseDetail.setHqty(purplandtl.getPurplanqty().doubleValue());
        }
        if(purplandtl.getPurprice() != null){
            purchaseDetail.setHunitprice(purplandtl.getPurprice().doubleValue());
        }
        purchaseDetail.setOrgdatadtlid(purplandtl.getPurplandtlid());
        purchaseDetail.setSourceid(purplandtl.getOrgdatadtlid());
        purchaseDetail.setSourcedtlid(purplandtl.getOrgdatadtlid());
        Map<String, Object> hospitalGoods = spdPurplandtlDao.selectHospitalGoodsBySourcesSpecsId(purplandtl.getHgoodsid());
        purchaseDetail.setGoodsclass(Integer.valueOf(hospitalGoods.get("goods_type").toString()));
        purchaseDetail.setHgoodsid(Long.valueOf(hospitalGoods.get("goods_id").toString()));
        purchaseDetail.setHgoodsno(hospitalGoods.get("goods_code").toString());
        purchaseDetail.setHgoodsname(hospitalGoods.get("goods_name").toString());
        purchaseDetail.setHgoodstypeid(Long.valueOf(hospitalGoods.get("goods_specs_id").toString()));
        purchaseDetail.setHgoodstype(hospitalGoods.get("specs").toString());

        purchaseDetail.setHgoodstypeno(hospitalGoods.get("specs_code").toString());

        purchaseDetail.setYhgoodsid(purplandtl.getHgoodsid());
        if (!StringUtil.isEmpty(purplandtl.getCremanid())) {
            purchaseDetail.setCreateId(Long.parseLong(purplandtl.getCremanid()));
        }
        purchaseDetail.setCreateTime(purplandtl.getCredate());

        return purchaseDetail;
    }

}
