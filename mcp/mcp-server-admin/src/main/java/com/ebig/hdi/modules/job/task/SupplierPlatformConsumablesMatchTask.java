package com.ebig.hdi.modules.job.task;

import com.ebig.hdi.common.constant.ActivitiConstant;
import com.ebig.hdi.common.entity.UnicodeGoodsShipApprovalEntity;
import com.ebig.hdi.common.entity.UnicodeGoodsShipHistEntity;
import com.ebig.hdi.common.enums.*;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.consumables.dao.GoodsPlatformConsumablesDao;
import com.ebig.hdi.modules.consumables.dao.GoodsSupplierConsumablesDao;
import com.ebig.hdi.modules.consumables.entity.GoodsSupplierConsumablesEntity;
import com.ebig.hdi.modules.consumables.service.GoodsShipApprovalConsumablesService;
import com.ebig.hdi.modules.core.dao.UnicodeGoodsShipHistDao;
import com.ebig.hdi.modules.core.service.UnicodeGoodsShipApprovalService;
import com.ebig.hdi.modules.job.entity.ScheduleJobEntity;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component("supplierPlatformConsumablesMatchTask")
@Slf4j
public class SupplierPlatformConsumablesMatchTask implements ITask {

    private static boolean isRunning = false;

    @Autowired
    private GoodsSupplierConsumablesDao goodsSupplierConsumablesDao;

    @Autowired
    private GoodsPlatformConsumablesDao goodsPlatformConsumablesDao;

    @Autowired
    private GoodsShipApprovalConsumablesService goodsShipApprovalConsumablesService;

    @Autowired
    private UnicodeGoodsShipApprovalService unicodeGoodsShipApprovalService;

    @Autowired
    private UnicodeGoodsShipHistDao unicodeGoodsShipHistDao;

    private static final String APPROVALS_ID = "approvalsId";

    @Override
    public void run(ScheduleJobEntity scheduleJob) {

        synchronized (SupplierPlatformConsumablesMatchTask.class) {
            if (isRunning) {
                log.info("supplierPlatformConsumablesMatchTask定时任务已在执行，为避免重复处理数据，将退出本次任务！");
                return;
            }
            isRunning = true;
        }

        try {
            log.info("supplierPlatformConsumablesMatchTask定时任务正在执行，参数为：{}", scheduleJob.getParams());
            // 获取1000条未匹对商品记录
            List<Map<String, Object>> supplierConsumablesList = goodsSupplierConsumablesDao.selectNotMatch(1000);
            for (Map<String, Object> supplierConsumables : supplierConsumablesList) {
                match(supplierConsumables);
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        } finally {
            isRunning = false;
        }
    }

    private void match(Map<String, Object> supplierConsumables) {
        // 生成匹对记录
        UnicodeGoodsShipApprovalEntity ship = new UnicodeGoodsShipApprovalEntity();
        // 目标机构类型 0医院 1供应商
        ship.setTorgType(MatchOrgTypeEnum.SUPPLIER.getKey());
        // 商品类型 1药品 2试剂 3耗材
        ship.setTgoodsType(MatchGoodsTypeEnum.CONSUMABLE.getKey());
        ship.setTorgId(Long.valueOf(supplierConsumables.get("supplierId").toString()));
        ship.setTgoodsId(Long.valueOf(supplierConsumables.get("consumablesId").toString()));
        ship.setTspecsId(Long.valueOf(supplierConsumables.get("specsId").toString()));
        if (!StringUtil.isEmpty(supplierConsumables.get(APPROVALS_ID))) {
            ship.setTapprovalId(Long.valueOf(supplierConsumables.get(APPROVALS_ID).toString()));
        }

        ship.setCredate(new Date());
        ship.setDelFlag(DelFlagEnum.NORMAL.getKey());
        ship.setShipFlag(IsMatchEnum.NO.getKey());
        UnicodeGoodsShipHistEntity hist = new UnicodeGoodsShipHistEntity();
        // 匹对
        List<Map<String, Object>> platformConsumablesList = goodsPlatformConsumablesDao.selectMatch(supplierConsumables);
        Long pgoodsId = null;
        Long pspecsId = null;
        if (!StringUtil.isEmpty(platformConsumablesList)) {
            pgoodsId = Long.valueOf(platformConsumablesList.get(0).get("consumablesId").toString());
            pspecsId = Long.valueOf(platformConsumablesList.get(0).get("specsId").toString());
            Long papprovalId = Long.valueOf(platformConsumablesList.get(0).get(APPROVALS_ID).toString());
            List<UnicodeGoodsShipApprovalEntity> supplierMatchGoodsList = goodsShipApprovalConsumablesService.listSupplierMatchGoods(ship.getTorgId(), pgoodsId, pspecsId, papprovalId);
            if (StringUtil.isEmpty(supplierMatchGoodsList)) {
                // 匹对成功
                if (!StringUtil.isEmpty(platformConsumablesList.get(0).get("consumables_name"))){
                    hist.setPgoodsName(platformConsumablesList.get(0).get("consumables_name").toString());
                }
                ship.setPgoodsId(pgoodsId);
                if (!StringUtil.isEmpty(platformConsumablesList.get(0).get("specs"))){
                    hist.setPspecs(platformConsumablesList.get(0).get("specs").toString());
                }
                ship.setPspecsId(pspecsId);
                if (!StringUtil.isEmpty(platformConsumablesList.get(0).get("approvals"))){
                    hist.setPapprovals(platformConsumablesList.get(0).get("approvals").toString());
                }
                ship.setPapprovalId(papprovalId);
                if (!StringUtil.isEmpty(platformConsumablesList.get(0).get("goods_nature"))){
                    hist.setPgoodsNature(Integer.valueOf(platformConsumablesList.get(0).get("goods_nature").toString()));
                }
                if (!StringUtil.isEmpty(platformConsumablesList.get(0).get("factory_id"))){
                    hist.setPfactoryId(Long.valueOf(platformConsumablesList.get(0).get("factory_id").toString()));
                }
                if (!StringUtil.isEmpty(platformConsumablesList.get(0).get("factory_name"))){
                    hist.setPfactoryName(platformConsumablesList.get(0).get("factory_name").toString());
                }
                ship.setShipFlag(IsMatchEnum.YES.getKey());
            } else {
                log.info(MessageFormat.format("匹对失败！平台耗材id:{0},规格id:{1},批准文号id:{2}已存在匹对关系！", pgoodsId, pspecsId, papprovalId));
            }
        }

        // 查询匹对记录是否已存在
        List<UnicodeGoodsShipApprovalEntity> list = goodsShipApprovalConsumablesService.selectListByColumn(ship.getTorgType(), ship.getTgoodsType(), ship.getTgoodsId(), ship.getTspecsId());
        SysUserEntity entity = new SysUserEntity();
        entity.setUsername("admin");
        entity.setUserId(1L);
        if (StringUtil.isEmpty(list)) {
            goodsShipApprovalConsumablesService.insert(ship);
            if (ship.getShipFlag().equals(IsMatchEnum.YES.getKey())) {
                //发起审批
                unicodeGoodsShipApprovalService.initiateApproval(entity, ship, ChangeTypeEnum.ADD.getKey(),
                        ActTypeEnum.SUPPLIER_PLATFORM_CONSUMABLES_MATCH.getKey(),
                        ActivitiConstant.ACTIVITI_SUPPLIER_PLATFORM_CONSUMABLES_MATCH);
                // 匹对记录不存在，插入
                ship.setCheckStatus(ApprovalTypeEnum.WAIT.getKey());
                goodsShipApprovalConsumablesService.updateById(ship);
            }
        } else {
            if (list.get(0).getShipFlag().equals(IsMatchEnum.YES.getKey())) {
                //若匹对记录存在并为已匹对，则不做处理
                return;
            }
            // 存在且为不匹对，更新匹对记录
            ship.setShipId(list.get(0).getShipId());
            ship.setEditdate(new Date());
            if (ship.getShipFlag().equals(IsMatchEnum.YES.getKey())) {
                //发起审批
                unicodeGoodsShipApprovalService.initiateApproval(entity, ship, ChangeTypeEnum.UPDATE.getKey(),
                        ActTypeEnum.SUPPLIER_PLATFORM_CONSUMABLES_MATCH.getKey(),
                        ActivitiConstant.ACTIVITI_SUPPLIER_PLATFORM_CONSUMABLES_MATCH);
                ship.setCheckStatus(ApprovalTypeEnum.WAIT.getKey());
            }
            goodsShipApprovalConsumablesService.updateAllColumnById(ship);
        }
        // 更新商品状态为已匹对
        GoodsSupplierConsumablesEntity supplierConsumablesEntity = goodsSupplierConsumablesDao.selectById(ship.getTgoodsId());
        supplierConsumablesEntity.setIsMatch(IsMatchEnum.YES.getKey());
        goodsSupplierConsumablesDao.updateById(supplierConsumablesEntity);
        // 将匹对记录插入匹对历史表
        hist.setShipId(ship.getShipId());
        if (!StringUtil.isEmpty(supplierConsumables.get("supplierId"))){
            hist.setTorgId(Long.valueOf(supplierConsumables.get("supplierId").toString()));
        }
        hist.setTorgType(ship.getTorgType());
        hist.setTgoodsType(ship.getTgoodsType());
        if (!StringUtil.isEmpty(supplierConsumables.get("goods_nature"))){
            hist.setTgoodsNature(Integer.valueOf(supplierConsumables.get("goods_nature").toString()));
        }else {
            hist.setTgoodsNature(null);
        }
        if (!StringUtil.isEmpty(supplierConsumables.get("consumablesName"))){
            hist.setTgoodsName(supplierConsumables.get("consumablesName").toString());
        }
        hist.setTgoodsId(ship.getTgoodsId());
        if (!StringUtil.isEmpty(supplierConsumables.get("specs"))){
            hist.setTspecs(supplierConsumables.get("specs").toString());
        }
        hist.setTspecsId(ship.getTspecsId());
        if (!StringUtil.isEmpty(supplierConsumables.get("approvals"))){
            hist.setTapprovals(supplierConsumables.get("approvals").toString());
        }
        hist.setTapprovalId(ship.getTapprovalId());
        hist.setShipFlag(ship.getShipFlag());
        hist.setPgoodsId(pgoodsId);
        hist.setPspecsId(pspecsId);
        hist.setPapprovalId(ship.getPapprovalId());
        hist.setCredate(new Date());
        hist.setDelFlag(DelFlagEnum.NORMAL.getKey());
        // 操作类型 1匹对 2商品信息变更
        hist.setOperType(OperationTypeEnum.MATCH.getKey());
        hist.setProcessId(ship.getProcessId());
        hist.setCremanname("admin");
        unicodeGoodsShipHistDao.insert(hist);
    }

}
