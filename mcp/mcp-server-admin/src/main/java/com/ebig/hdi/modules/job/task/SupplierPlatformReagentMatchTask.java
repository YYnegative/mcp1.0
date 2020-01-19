package com.ebig.hdi.modules.job.task;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ebig.hdi.common.constant.ActivitiConstant;
import com.ebig.hdi.common.entity.UnicodeGoodsShipApprovalEntity;
import com.ebig.hdi.common.entity.UnicodeGoodsShipHistEntity;
import com.ebig.hdi.common.enums.*;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.core.dao.UnicodeGoodsShipHistDao;
import com.ebig.hdi.modules.core.service.UnicodeGoodsShipApprovalService;
import com.ebig.hdi.modules.job.entity.ScheduleJobEntity;
import com.ebig.hdi.modules.reagent.dao.GoodsPlatformReagentDao;
import com.ebig.hdi.modules.reagent.dao.GoodsSupplierReagentDao;
import com.ebig.hdi.modules.reagent.entity.GoodsSupplierReagentEntity;
import com.ebig.hdi.modules.reagent.service.GoodsShipApprovalReagentService;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component("supplierPlatformReagentMatchTask")
@Slf4j
public class SupplierPlatformReagentMatchTask implements ITask {

    private static boolean isRunning = false;

    @Autowired
    private GoodsSupplierReagentDao goodsSupplierReagentDao;

    @Autowired
    private GoodsPlatformReagentDao goodsPlatformReagentDao;

    @Autowired
    private GoodsShipApprovalReagentService goodsShipApprovalReagentService;

    @Autowired
    private UnicodeGoodsShipApprovalService unicodeGoodsShipApprovalService;

    @Autowired
    private UnicodeGoodsShipHistDao unicodeGoodsShipHistDao;
    @Override
    public void run(ScheduleJobEntity scheduleJob) {
        synchronized (SupplierPlatformReagentMatchTask.class) {
            if (isRunning) {
                log.info("supplierPlatformReagentMatchTask定时任务已在执行，为避免重复处理数据，将退出本次任务！");
                return;
            }
            isRunning = true;
        }

        try {
            log.info("supplierPlatformReagentMatchTask定时任务正在执行，参数为：{}", scheduleJob.getParams());

            // 获取1000条未匹对商品记录
            List<Map<String, Object>> supplierReagentList = goodsSupplierReagentDao.selectNotMatch(1000);

            if (supplierReagentList.size() < 1) {
                log.info("未有供应商试剂未匹配记录");
                return;
            }
            for (Map<String, Object> supplierReagent : supplierReagentList) {

                match(supplierReagent);
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        } finally {
            isRunning = false;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    private void match(Map<String, Object> supplierReagent) {
        // 生成匹对记录
        UnicodeGoodsShipApprovalEntity ship = new UnicodeGoodsShipApprovalEntity();
        // 将匹对记录插入匹对历史表
        UnicodeGoodsShipHistEntity hist = new UnicodeGoodsShipHistEntity();
        // 目标机构类型 0医院 1供应商
        ship.setTorgType(MatchOrgTypeEnum.SUPPLIER.getKey());
        ship.setTorgId(Long.valueOf(supplierReagent.get("supplierId").toString()));
        // 商品类型 1药品 2试剂 3耗材
        ship.setTgoodsType(MatchGoodsTypeEnum.REAGENT.getKey());
        ship.setTgoodsId(Long.valueOf(supplierReagent.get("reagentId").toString()));
        ship.setTspecsId(Long.valueOf(supplierReagent.get("specsId").toString()));
        ship.setCredate(new Date());
        ship.setShipFlag(IsMatchEnum.NO.getKey());
        ship.setDelFlag(DelFlagEnum.NORMAL.getKey());

        // 获取可匹对的平台试剂商品进行匹配
        List<Map<String, Object>> platformReagentList = goodsPlatformReagentDao.selectMatch(supplierReagent);
        Long pgoodsId=null;
        Long pspecsId=null;
        if (!StringUtil.isEmpty(platformReagentList)) {
            pgoodsId = Long.valueOf(platformReagentList.get(0).get("reagentId").toString());
            pspecsId = Long.valueOf(platformReagentList.get(0).get("specsId").toString());
            // 查询平台商品与供应商商品已存在匹对记录
            List<UnicodeGoodsShipApprovalEntity> supplierMatchGoodsList = goodsShipApprovalReagentService.listSupplierMatchGoods(ship.getTorgId(), pgoodsId, pspecsId);
            if (!StringUtil.isEmpty(supplierMatchGoodsList)) {
                log.info(MessageFormat.format("匹对失败！平台试剂id:{0},规格id:{1}已存在匹对关系！", pgoodsId, pspecsId));
            } else {
                // 匹对成功
                if (!StringUtil.isEmpty(platformReagentList.get(0).get("reagent_name"))){
                    hist.setPgoodsName(platformReagentList.get(0).get("reagent_name").toString());
                }
                ship.setPgoodsId(pgoodsId);
                if (!StringUtil.isEmpty(platformReagentList.get(0).get("specs"))){
                    hist.setPspecs(platformReagentList.get(0).get("specs").toString());
                }
                ship.setPspecsId(pspecsId);
                if (!StringUtil.isEmpty(platformReagentList.get(0).get("approvals"))){
                    hist.setPapprovals(platformReagentList.get(0).get("approvals").toString());
                }
                if (!StringUtil.isEmpty(platformReagentList.get(0).get("goods_nature"))){
                    hist.setPgoodsNature(Integer.valueOf(platformReagentList.get(0).get("goods_nature").toString()));
                }
                if (!StringUtil.isEmpty(platformReagentList.get(0).get("factory_id"))){
                    hist.setPfactoryId(Long.valueOf(platformReagentList.get(0).get("factory_id").toString()));
                }
                if (!StringUtil.isEmpty(platformReagentList.get(0).get("factory_name"))){
                    hist.setPfactoryName(platformReagentList.get(0).get("factory_name").toString());
                }
                ship.setShipFlag(IsMatchEnum.YES.getKey());
                //匹配历史
                hist.setPgoodsId(pgoodsId);
                hist.setPspecsId(pspecsId);
            }
        }

        // 查询该供应商商品是否已存在匹对记录
        List<UnicodeGoodsShipApprovalEntity> list = goodsShipApprovalReagentService
                .selectList(new EntityWrapper<UnicodeGoodsShipApprovalEntity>().eq("torg_type", ship.getTorgType())
                        .eq("torg_id", ship.getTorgId()).eq("tgoods_type", ship.getTgoodsType())
                        .eq("tgoods_id", ship.getTgoodsId()).eq("tspecs_id", ship.getTspecsId()));
        SysUserEntity entity = new SysUserEntity();
        entity.setUsername("admin");
        entity.setUserId(1L);
        if (StringUtil.isEmpty(list)) {
            goodsShipApprovalReagentService.insert(ship);
            //成功匹配的才需要审批
            if (ship.getShipFlag().equals(IsMatchEnum.YES.getKey())) {
                // 匹对记录不存在，插入
                unicodeGoodsShipApprovalService.initiateApproval(entity, ship, ChangeTypeEnum.ADD.getKey(),
                        ActTypeEnum.SUPPLIER_PLATFORM_REAGENT_MATCH.getKey(),
                        ActivitiConstant.ACTIVITI_SUPPLIER_PLATFORM_REAGENT_MATCH);
                // 匹对记录不存在，插入
                ship.setCheckStatus(ApprovalTypeEnum.WAIT.getKey());
                goodsShipApprovalReagentService.updateById(ship);
            }
        } else {
            if (list.get(0).getShipFlag().equals(IsMatchEnum.YES.getKey())) {
                //若匹对记录存在并为已匹对，则不做处理
                return;
            }
            // 存在且为不匹对，更新匹对记录
            ship.setShipId(list.get(0).getShipId());
            //成功匹配的才需要审批
            if (ship.getShipFlag().equals(IsMatchEnum.YES.getKey())) {
                unicodeGoodsShipApprovalService.initiateApproval(entity, ship, ChangeTypeEnum.UPDATE.getKey(),
                        ActTypeEnum.SUPPLIER_PLATFORM_REAGENT_MATCH.getKey(),
                        ActivitiConstant.ACTIVITI_SUPPLIER_PLATFORM_REAGENT_MATCH);
                ship.setCheckStatus(ApprovalTypeEnum.WAIT.getKey());
            }
            goodsShipApprovalReagentService.updateAllColumnById(ship);
        }
        // 更新商品状态为已匹对
        GoodsSupplierReagentEntity supplierReagentEntity = goodsSupplierReagentDao.selectById(ship.getTgoodsId());
        supplierReagentEntity.setIsMatch(IsMatchEnum.YES.getKey());
        goodsSupplierReagentDao.updateById(supplierReagentEntity);

        // 将匹对记录插入匹对历史表
        hist.setShipId(ship.getShipId());
        if (!StringUtil.isEmpty(supplierReagent.get("supplierId"))){
            hist.setTorgId(Long.valueOf(supplierReagent.get("supplierId").toString()));
        }
        hist.setTorgType(ship.getTorgType());
        hist.setTgoodsType(ship.getTgoodsType());
        if (!StringUtil.isEmpty(supplierReagent.get("goods_nature"))){
            hist.setTgoodsNature(Integer.valueOf(supplierReagent.get("goods_nature").toString()));
        }else {
            hist.setTgoodsNature(null);
        }
        if (!StringUtil.isEmpty(supplierReagent.get("reagent_name"))){
            hist.setTgoodsName(supplierReagent.get("reagent_name").toString());
        }
        hist.setTgoodsId(ship.getTgoodsId());
        if (!StringUtil.isEmpty(supplierReagent.get("specs"))){
            hist.setTspecs(supplierReagent.get("specs").toString());
        }
        hist.setTspecsId(ship.getTspecsId());
        if (!StringUtil.isEmpty(supplierReagent.get("approvals"))){
            hist.setTapprovals(supplierReagent.get("approvals").toString());
        }
        hist.setTapprovalId(ship.getTapprovalId());
        hist.setShipFlag(ship.getShipFlag());
        hist.setPgoodsId(pgoodsId);
        hist.setPspecsId(pspecsId);
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
