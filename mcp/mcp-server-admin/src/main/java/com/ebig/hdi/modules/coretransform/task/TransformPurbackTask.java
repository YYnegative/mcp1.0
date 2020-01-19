package com.ebig.hdi.modules.coretransform.task;

import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.ebig.hdi.common.enums.DataSourceEnum;
import com.ebig.hdi.common.enums.DelFlagEnum;
import com.ebig.hdi.common.enums.RefundsTypeEnum;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.core.dao.CoreStorehouseDao;
import com.ebig.hdi.modules.core.entity.CoreStorehouseEntity;
import com.ebig.hdi.modules.coretransform.dao.*;
import com.ebig.hdi.modules.coretransform.entity.SpdPurbackEntity;
import com.ebig.hdi.modules.coretransform.entity.SpdPurbackdtlEntity;
import com.ebig.hdi.modules.job.entity.ScheduleJobEntity;
import com.ebig.hdi.modules.job.task.ITask;
import com.ebig.hdi.modules.org.entity.OrgHospitalInfoEntity;
import com.ebig.hdi.modules.org.entity.OrgSupplierInfoEntity;
import com.ebig.hdi.modules.org.service.OrgHospitalInfoService;
import com.ebig.hdi.modules.org.service.OrgSupplierInfoService;
import com.ebig.hdi.modules.refunds.entity.RefundsDetailEntity;
import com.ebig.hdi.modules.refunds.entity.RefundsMasterEntity;
import com.ebig.hdi.modules.sys.entity.SysDictEntity;
import com.ebig.hdi.modules.sys.service.SysDictService;
import com.ebig.hdi.modules.unicode.dao.UnicodeSupplyShipDao;
import com.ebig.hdi.modules.unicode.entity.UnicodeSupplyShipEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 退货单定时转换任务（dsc上传的临表spd_purback,spd_purbackdtl 转换到mcp主表 hdi_refunds_master，hdi_refunds_detail）
 *
 * @author：alan
 * @data：2019.10.21 ---------------------供货单标识、商品单位待完善-----------------------10.22
 */
@Component("transformPurbackTask")
@Slf4j
public class TransformPurbackTask implements ITask {

    @Autowired
    private SpdPurbackDao spdPurbackDao;
    @Autowired
    private SpdPurbackdtlDao spdPurbackdtlDao;
    @Autowired
    private RefundsMasterNewDao refundsMasterNewDao;
    @Autowired
    private RefundsDetailNewDao refundsDetailNewDao;
    @Autowired
    private SpdPurplandtlDao spdPurplandtlDao;
    @Autowired
    private UnicodeSupplyShipDao unicodeSupplyShipDao;
    @Autowired
    private CoreStorehouseDao coreStorehouseDao;
    @Autowired
    private OrgSupplierInfoService orgSupplierInfoService;
    @Autowired
    private OrgHospitalInfoService orgHospitalInfoService;

    @Autowired
    private SysDictService sysDictService;

    private static boolean isRunning = false;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(ScheduleJobEntity scheduleJob) {
        synchronized (TransformPoTask.class) {
            if (isRunning) {
                log.info("transformPurbackTask定时任务已在执行，为避免重复处理数据，将退出本次任务！");
                return;
            }
            isRunning = true;
        }

        try {
            log.info("transformPurbackTask定时任务正在执行，参数为：{}", scheduleJob.getParams());
            // 查询临时表所有退货细单
            List<SpdPurbackdtlEntity> spdPurbackdtlList = spdPurbackdtlDao.queryList(1000);
            Set<String> purBackIdSet = new HashSet<>();
            for (SpdPurbackdtlEntity spdPurbackdtl : spdPurbackdtlList) {
                RefundsDetailEntity refundsDetail = transformPurbackdtl(spdPurbackdtl);
                //根据spd退货单细单ID获取供货单ID和供货细单ID
                Map<String, Object> supplyMap = spdPurbackdtlDao.getSupplyMasterIdAndSupplyDetailId(spdPurbackdtl.getPurbackdtlid());
                if (supplyMap == null || supplyMap.size() <= 0) {
                    log.error("数据错误，无法找到对应的供货主单:{}", spdPurbackdtl.getPurbackid());
                    continue;
                }
                refundsDetail.setSupplyMasterId(Long.valueOf(supplyMap.get("supply_master_id").toString()));
                refundsDetail.setSupplyDetailId((supplyMap.get("supply_detail_id").toString()));
                //判断mcp是否有退货主单了
                RefundsMasterEntity refundsMaster = refundsMasterNewDao.selectByOrgdataid(spdPurbackdtl.getPurbackid());
                if (StringUtil.isEmpty(refundsMaster)) {
                    refundsMaster = new RefundsMasterEntity();
                    //获取临时表退货主单
                    SpdPurbackEntity spdPurback = spdPurbackDao.selectById(spdPurbackdtl.getPurbackid());
                    if (StringUtil.isEmpty(spdPurback)) {
                        log.error("临时表中不存在主单记录，退货主单id:{}", spdPurbackdtl.getPurbackid());
                        continue;
                    }

                    // 查询mcp医院 供应商
                    UnicodeSupplyShipEntity ship = unicodeSupplyShipDao.selectBySourcesSupplierIdAndSourcesHospitalId(spdPurback.getSupplyid(), spdPurback.getUorganid());
                    if (StringUtil.isEmpty(ship)) {
                        log.error("mcp匹对关系表中不存在记录，原系统供应商id:{}，原系统医院id:{}", spdPurback.getSupplyid(), spdPurback.getUorganid());
                        continue;
                    }
                    refundsMaster.setSourcesSupplierId(ship.getSourcesSupplierId());
                    refundsMaster.setSourcesSupplierCode(ship.getSourcesSupplierCreditCode());
                    refundsMaster.setSourcesSupplierName(ship.getSourcesSupplierName());
                    //查询平台供应商信息
                    if (!StringUtil.isEmpty(ship.getSupplierId())) {
                        OrgSupplierInfoEntity entity = orgSupplierInfoService.selectById(ship.getSupplierId());
                        if (null != entity) {
                            refundsMaster.setSupplierId(entity.getId());
                            refundsMaster.setSupplierCode(entity.getSupplierCode());
                            refundsMaster.setSupplierName(entity.getSupplierName());
                        }
                    }
                    refundsMaster.setSourcesHospitalId(ship.getSourcesHospitalId());
                    refundsMaster.setSourcesHospitalCode(ship.getSourcesHospitalCreditCode());
                    refundsMaster.setSourcesHospitalName(ship.getSourcesHospitalName());

                    //查询平台医院信息
                    if (!StringUtil.isEmpty(ship.getHospitalId())) {
                        OrgHospitalInfoEntity entity = orgHospitalInfoService.selectById(ship.getHospitalId());
                        if (null != entity) {
                            refundsMaster.setHospitalId(entity.getId());
                            refundsMaster.setHospitalCode(entity.getHospitalCode());
                            refundsMaster.setHospitalName(entity.getHospitalName());
                        }
                    }

                    // 查询mcp库房
                    CoreStorehouseEntity storehouse = coreStorehouseDao.selectByOrgdataid(spdPurback.getStorehouseid());
                    if (StringUtil.isEmpty(storehouse)) {
                        log.error("mcp库房表中不存在记录，原系统id:{}", spdPurback.getStorehouseid());
                        continue;
                    }
                    refundsMaster.setSourcesStoreHouseId(storehouse.getOrgdataid());
                    refundsMaster.setStoreHouseId(storehouse.getStorehouseid());
                    refundsMaster.setStorehouseNo(storehouse.getStorehouseno());
                    refundsMaster.setStoreHouseName(storehouse.getStorehousename());


                    //为退货主单赋值：
                    refundsMaster.setRefundsNo(spdPurback.getPurbackno());
                    refundsMaster.setRefundsTime(spdPurback.getAuditdate());
                    refundsMaster.setCreateId(Long.getLong(spdPurback.getCremanid()));
                    refundsMaster.setCreateTime(spdPurback.getCredate());
                    refundsMaster.setEditId(Long.getLong(spdPurback.getAuditmanid()));
                    refundsMaster.setEditTime(spdPurback.getEditdate());
                    refundsMaster.setDelFlag(DelFlagEnum.NORMAL.getKey());
                    refundsMaster.setDataSource(DataSourceEnum.PORT.getKey());
                    refundsMaster.setStatus(RefundsTypeEnum.UNSUBMIT.getKey());
                    refundsMaster.setDeptId(ship.getDeptId());
                    refundsMaster.setOrgdataId(spdPurback.getPurbackid());
                }
                //保存退货单到mcp表
                saveRefundsInfo(refundsMaster, refundsDetail);
                purBackIdSet.add(spdPurbackdtl.getPlotid());

            }
            //统一删除临时退货单主单
            if (CollectionUtils.isNotEmpty(purBackIdSet)) {
                spdPurbackDao.deleteBatchIds(purBackIdSet);
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            isRunning = false;
        }


    }

    private void saveRefundsInfo(RefundsMasterEntity refundsMaster, RefundsDetailEntity refundsDetail) {
        RefundsMasterEntity entity = refundsMasterNewDao.selectByOrgdataid(refundsMaster.getOrgdataId());
        if (null != entity) {
            if ("0".equals(entity.getStatus().toString())) {
                refundsMasterNewDao.updateById(refundsMaster);
                RefundsDetailEntity detailEntity = refundsDetailNewDao.selectByOrgdatadtlid(refundsDetail.getOrgdatadtlId());
                if (StringUtil.isEmpty(detailEntity)) {
                    refundsDetailNewDao.insert(refundsDetail);
                } else {
                    refundsDetailNewDao.updateById(refundsDetail);
                }
            }
            //删除临时表主单及细单
            spdPurbackdtlDao.deleteById(refundsDetail.getOrgdatadtlId());

        } else {
            //新增退货主单
            refundsMasterNewDao.insert(refundsMaster);
            //新增退货细单
            refundsDetail.setMasterId(refundsMaster.getId());
            refundsDetailNewDao.insert(refundsDetail);
            //删除临时表主单及细单
            spdPurbackdtlDao.deleteById(refundsDetail.getOrgdatadtlId());
        }
    }

    /**
     * 退货细单临时表转换为mcp 退货单
     *
     * @param spdPurbackdtl
     * @return
     */
    private RefundsDetailEntity transformPurbackdtl(SpdPurbackdtlEntity spdPurbackdtl) {
        RefundsDetailEntity refundsDetail = new RefundsDetailEntity();
        refundsDetail.setRefundsRemark(spdPurbackdtl.getBackreason());
        refundsDetail.setRealityRefundsNumber(spdPurbackdtl.getPurbackqty().intValue());
        refundsDetail.setRefundsPrice(spdPurbackdtl.getUnitprice());
        refundsDetail.setOrgdatadtlId(spdPurbackdtl.getPurbackdtlid());
        refundsDetail.setOrgdataId(spdPurbackdtl.getPurbackid());
        //商品单位名称
        SysDictEntity entity = sysDictService.checkGoodsUnit(spdPurbackdtl.getGoodsunit());
        if (entity != null) {
            refundsDetail.setGoodsUnitCode(entity.getCode());
        }
        refundsDetail.setGoodsUnitCode(spdPurbackdtl.getUnitid());

        Map<String, Object> hospitalGoods = spdPurplandtlDao.selectHospitalGoodsBySourcesSpecsId(spdPurbackdtl.getGoodsid());

        if (hospitalGoods != null && hospitalGoods.size() > 0) {
            refundsDetail.setSourcesGoodsId(hospitalGoods.get("goods_id") + "");
            refundsDetail.setSourcesGoodsCode(hospitalGoods.get("goods_code") + "");
            refundsDetail.setSourcesGoodsName(hospitalGoods.get("goods_name") + "");
            refundsDetail.setSourcesSpecsId(hospitalGoods.get("goods_specs_id") + "");
            refundsDetail.setSourcesSpecsName(hospitalGoods.get("specs") + "");
            refundsDetail.setSourcesSpecsCode(hospitalGoods.get("specs_code") + "");
            refundsDetail.setGoodsId(Long.valueOf(hospitalGoods.get("goods_id") + ""));
            refundsDetail.setGoodsCode(hospitalGoods.get("goods_code") + "");
            refundsDetail.setGoodsName(hospitalGoods.get("goods_name") + "");
        }
        return refundsDetail;
    }

}