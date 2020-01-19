package com.ebig.hdi.modules.job.task;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ebig.hdi.common.constant.ActivitiConstant;
import com.ebig.hdi.common.entity.UnicodeGoodsShipApprovalEntity;
import com.ebig.hdi.common.entity.UnicodeGoodsShipHistEntity;
import com.ebig.hdi.common.enums.*;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.core.dao.UnicodeGoodsShipHistDao;
import com.ebig.hdi.modules.core.service.UnicodeGoodsShipApprovalService;
import com.ebig.hdi.modules.drugs.dao.GoodsPlatformDrugsDao;
import com.ebig.hdi.modules.drugs.dao.GoodsSupplierDrugsDao;
import com.ebig.hdi.modules.drugs.entity.GoodsSupplierDrugsEntity;
import com.ebig.hdi.modules.drugs.service.GoodsShipApprovalDrugsService;
import com.ebig.hdi.modules.job.entity.ScheduleJobEntity;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component("supplierPlatformDrugsMatchTask")
@Slf4j
public class SupplierPlatformDrugsMatchTask implements ITask {

    @Autowired
    private GoodsSupplierDrugsDao goodsSupplierDrugsDao;

    @Autowired
    private GoodsPlatformDrugsDao goodsPlatformDrugsDao;

    @Autowired
    private GoodsShipApprovalDrugsService goodsShipApprovalDrugsService;


    @Autowired
    private UnicodeGoodsShipHistDao unicodeGoodsShipHistDao;

    @Autowired
    private UnicodeGoodsShipApprovalService unicodeGoodsShipApprovalService;


    private static boolean isRunning = false;

    @Override
    public void run(ScheduleJobEntity scheduleJob) {

        synchronized (SupplierPlatformDrugsMatchTask.class) {
            if (isRunning) {
                log.info("supplierPlatformDrugsMatchTask定时任务已在执行，为避免重复处理数据，将退出本次任务！");
                return;
            }
            isRunning = true;
        }

        try {
            log.info("supplierPlatformDrugsMatchTask定时任务正在执行，参数为：{}", scheduleJob.getParams());

            // 获取1000条未匹对商品记录
            List<Map<String, Object>> supplierDrugsList = goodsSupplierDrugsDao.selectNotMatch(1000);
            for (Map<String, Object> supplierDrugs : supplierDrugsList) {
                match(supplierDrugs);
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        } finally {
            isRunning = false;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    private void match(Map<String, Object> supplierDrugs) {
        // 生成匹对记录
        UnicodeGoodsShipApprovalEntity ship = new UnicodeGoodsShipApprovalEntity();
        // 目标机构类型 0医院 1供应商
        ship.setTorgType(MatchOrgTypeEnum.SUPPLIER.getKey());
        // 商品类型 1药品 2试剂 3耗材
        ship.setTgoodsType(MatchGoodsTypeEnum.DRUGS.getKey());
        ship.setTorgId(Long.valueOf(supplierDrugs.get("supplierId").toString()));
        ship.setTgoodsId(Long.valueOf(supplierDrugs.get("drugsId").toString()));
        ship.setTspecsId(Long.valueOf(supplierDrugs.get("specsId").toString()));
        ship.setCredate(new Date());
        ship.setDelFlag(DelFlagEnum.NORMAL.getKey());
        ship.setShipFlag(IsMatchEnum.NO.getKey());
        UnicodeGoodsShipHistEntity hist = new UnicodeGoodsShipHistEntity();
        // 匹对
        List<Map<String, Object>> platformDrugsList = goodsPlatformDrugsDao.selectMatch(supplierDrugs);
        Long pGoodsId = null;
        Long pSpecsId = null;
        if (!StringUtil.isEmpty(platformDrugsList)) {
            pGoodsId = Long.valueOf(platformDrugsList.get(0).get("drugsId").toString());
            pSpecsId = Long.valueOf(platformDrugsList.get(0).get("specsId").toString());
            List<UnicodeGoodsShipApprovalEntity> supplierMatchGoodsList = goodsShipApprovalDrugsService.listSupplierMatchGoods(ship.getTorgId(), pGoodsId, pSpecsId);
            if (StringUtil.isEmpty(supplierMatchGoodsList)) {
                // 匹对成功
                if (!StringUtil.isEmpty(platformDrugsList.get(0).get("drugsName"))){
                    hist.setPgoodsName(platformDrugsList.get(0).get("drugsName").toString());
                }
                ship.setPgoodsId(pGoodsId);
                if (!StringUtil.isEmpty(platformDrugsList.get(0).get("specs"))){
                    hist.setPspecs(platformDrugsList.get(0).get("specs").toString());
                }
                ship.setPspecsId(pSpecsId);
                if (!StringUtil.isEmpty(platformDrugsList.get(0).get("approvals"))){
                    hist.setPapprovals(platformDrugsList.get(0).get("approvals").toString());
                }
                if (!StringUtil.isEmpty(platformDrugsList.get(0).get("goods_nature"))){
                    hist.setPgoodsNature(Integer.valueOf(platformDrugsList.get(0).get("goods_nature").toString()));
                }
                if (!StringUtil.isEmpty(platformDrugsList.get(0).get("factory_id"))){
                    hist.setPfactoryId(Long.valueOf(platformDrugsList.get(0).get("factory_id").toString()));
                }
                if (!StringUtil.isEmpty(platformDrugsList.get(0).get("factory_name"))){
                    hist.setPfactoryName(platformDrugsList.get(0).get("factory_name").toString());
                }
                ship.setShipFlag(IsMatchEnum.YES.getKey());
            } else {
                log.info(MessageFormat.format("匹对失败！平台药品id:{0},规格id:{1}已存在匹对关系！", pGoodsId, pSpecsId));
            }
        }

        // 查询匹对记录是否已存在
        List<UnicodeGoodsShipApprovalEntity> list = goodsShipApprovalDrugsService
                .selectList(new EntityWrapper<UnicodeGoodsShipApprovalEntity>().eq("torg_type", ship.getTorgType())
                        .eq("tgoods_type", ship.getTgoodsType()).eq("tgoods_id", ship.getTgoodsId())
                        .eq("tspecs_id", ship.getTspecsId()));
        SysUserEntity entity = new SysUserEntity();
        entity.setUsername("admin");
        entity.setUserId(1L);
        if (StringUtil.isEmpty(list)) {
            goodsShipApprovalDrugsService.insert(ship);
            //成功匹配的才需要审批
            if (ship.getShipFlag().equals(IsMatchEnum.YES.getKey())) {
                //发起审批
                unicodeGoodsShipApprovalService.initiateApproval(entity, ship, ChangeTypeEnum.ADD.getKey(),
                        ActTypeEnum.SUPPLIER_PLATFORM_DRUGS_MATCH.getKey(),
                        ActivitiConstant.ACTIVITI_SUPPLIER_PLATFORM_DRUGS_MATCH);
                // 匹对记录不存在，插入
                ship.setCheckStatus(ApprovalTypeEnum.WAIT.getKey());
                goodsShipApprovalDrugsService.updateById(ship);
            }
        } else {
            if (list.get(0).getShipFlag().equals(IsMatchEnum.YES.getKey())) {
                //若匹对记录存在并为已匹对，则不做处理
                return;
            }
            // 存在且为不匹对，更新匹对记录
            ship.setShipId(list.get(0).getShipId());
            if (ship.getShipFlag().equals(IsMatchEnum.YES.getKey())) {
                unicodeGoodsShipApprovalService.initiateApproval(entity, ship, ChangeTypeEnum.UPDATE.getKey(),
                        ActTypeEnum.SUPPLIER_PLATFORM_DRUGS_MATCH.getKey(),
                        ActivitiConstant.ACTIVITI_SUPPLIER_PLATFORM_DRUGS_MATCH);
                ship.setCheckStatus(ApprovalTypeEnum.WAIT.getKey());
            }
            goodsShipApprovalDrugsService.updateAllColumnById(ship);
        }
        // 更新商品状态为已匹对
        GoodsSupplierDrugsEntity supplierDrugsEntity = goodsSupplierDrugsDao.selectById(ship.getTgoodsId());
        supplierDrugsEntity.setIsMatch(IsMatchEnum.YES.getKey());
        goodsSupplierDrugsDao.updateById(supplierDrugsEntity);

        // 将匹对记录插入匹对历史表
        hist.setShipId(ship.getShipId());
        if (!StringUtil.isEmpty(supplierDrugs.get("supplierId"))){
            hist.setTorgId(Long.valueOf(supplierDrugs.get("supplierId").toString()));
        }
        hist.setTorgType(ship.getTorgType());
        hist.setTgoodsType(ship.getTgoodsType());
        if (!StringUtil.isEmpty(supplierDrugs.get("goods_nature"))){
            hist.setTgoodsNature(Integer.valueOf(supplierDrugs.get("goods_nature").toString()));
        }else {
            hist.setTgoodsNature(null);
        }
        if (!StringUtil.isEmpty(supplierDrugs.get("drugs_name"))){
            hist.setTgoodsName(supplierDrugs.get("drugs_name").toString());
        }
        hist.setTgoodsId(ship.getTgoodsId());
        if (!StringUtil.isEmpty(supplierDrugs.get("specs"))){
            hist.setTspecs(supplierDrugs.get("specs").toString());
        }
        hist.setTspecsId(ship.getTspecsId());
        if (!StringUtil.isEmpty(supplierDrugs.get("approvals"))){
            hist.setTapprovals(supplierDrugs.get("approvals").toString());
        }
        hist.setTapprovalId(ship.getTapprovalId());
        hist.setShipFlag(ship.getShipFlag());
        hist.setPgoodsId(pGoodsId);
        hist.setPspecsId(pSpecsId);
        hist.setPapprovalId(ship.getPapprovalId());
        hist.setCredate(new Date());
        hist.setDelFlag(DelFlagEnum.NORMAL.getKey());
        hist.setProcessId(ship.getProcessId());
        // 操作类型 1匹对 2商品信息变更
        hist.setOperType(OperationTypeEnum.MATCH.getKey());
        hist.setCremanname("admin");
        unicodeGoodsShipHistDao.insert(hist);

    }
}
