package com.ebig.hdi.modules.job.task;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.ebig.hdi.common.constant.ActivitiConstant;
import com.ebig.hdi.common.entity.UnicodeGoodsShipApprovalEntity;
import com.ebig.hdi.common.entity.UnicodeGoodsShipHistEntity;
import com.ebig.hdi.common.enums.*;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.core.dao.UnicodeGoodsShipHistDao;
import com.ebig.hdi.modules.core.service.UnicodeGoodsShipApprovalService;
import com.ebig.hdi.modules.job.entity.ScheduleJobEntity;
import com.ebig.hdi.modules.reagent.dao.GoodsHospitalReagentDao;
import com.ebig.hdi.modules.reagent.dao.GoodsPlatformReagentDao;
import com.ebig.hdi.modules.reagent.entity.GoodsHospitalReagentEntity;
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

@Component("hospitalPlatformReagentMatchTask")
@Slf4j
public class HospitalPlatformReagentMatchTask implements ITask {

    @Autowired
    private GoodsHospitalReagentDao goodsHospitalReagentDao;

    @Autowired
    private GoodsPlatformReagentDao goodsPlatformReagentDao;

    @Autowired
    private GoodsShipApprovalReagentService goodsShipApprovalReagentService;


    @Autowired
    private UnicodeGoodsShipHistDao unicodeGoodsShipHistDao;

    @Autowired
    private UnicodeGoodsShipApprovalService unicodeGoodsShipApprovalService;


    private boolean isRunning = false;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(ScheduleJobEntity scheduleJob) {
        synchronized (HospitalPlatformReagentMatchTask.class) {
            if (isRunning) {
                log.info("hospitalPlatformReagentMatchTask定时任务已在执行，为避免重复处理数据，将退出本次任务！");
                return;
            }
            isRunning = true;
        }

        try {
            log.info("hospitalPlatformReagentMatchTask定时任务正在执行，参数为：{}", scheduleJob.getParams());

            // 获取1000条未匹对商品记录
            List<Map<String, Object>> hospitalReagentList = goodsHospitalReagentDao.selectNotMatch(1000);

            if (CollectionUtils.isEmpty(hospitalReagentList)) {
                log.info("未有医院试剂未匹配记录");
                return;
            }
            for (Map<String, Object> hospitalReagent : hospitalReagentList) {
                match(hospitalReagent);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            isRunning = false;
        }
    }

    private void match(Map<String, Object> hospitalReagent) {
        // 生成匹对记录
        UnicodeGoodsShipApprovalEntity ship = new UnicodeGoodsShipApprovalEntity();
        // 将匹对记录插入匹对历史表
        UnicodeGoodsShipHistEntity hist = new UnicodeGoodsShipHistEntity();
        // 目标机构类型 0医院 1供应商
        ship.setTorgType(MatchOrgTypeEnum.HOSPITAL.getKey());
        // 商品类型 1药品 2试剂 3耗材
        ship.setTgoodsType(MatchGoodsTypeEnum.REAGENT.getKey());
        ship.setTorgId(Long.valueOf(hospitalReagent.get("hospitalId").toString()));
        ship.setTgoodsId(Long.valueOf(hospitalReagent.get("reagentId").toString()));
        ship.setTspecsId(Long.valueOf(hospitalReagent.get("specsId").toString()));
        ship.setCredate(new Date());
        ship.setDelFlag(DelFlagEnum.NORMAL.getKey());
        ship.setShipFlag(IsMatchEnum.NO.getKey());

        // 获取可匹对的平台试剂商品进行匹配
        List<Map<String, Object>> platformReagentList = goodsPlatformReagentDao.selectMatch(hospitalReagent);
        Long pgoodsId = null;
        Long pspecsId = null;
        if (CollectionUtils.isNotEmpty(platformReagentList)) {
            pgoodsId = Long.valueOf(platformReagentList.get(0).get("reagentId").toString());
            pspecsId = Long.valueOf(platformReagentList.get(0).get("specsId").toString());
            List<UnicodeGoodsShipApprovalEntity> hospitalMatchGoodsList = goodsShipApprovalReagentService.listHospitalMatchGoods(ship.getTorgId(), pgoodsId, pspecsId);
            if (CollectionUtils.isEmpty(hospitalMatchGoodsList)) {
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
            } else {
                log.info(MessageFormat.format("匹对失败！平台试剂id:{0},规格id:{1}已存在匹对关系！", pgoodsId, pspecsId));
            }

        }

        // 查询匹对记录是否已存在
        List<UnicodeGoodsShipApprovalEntity> list = goodsShipApprovalReagentService
                .selectList(new EntityWrapper<UnicodeGoodsShipApprovalEntity>().eq("torg_type", ship.getTorgType())
                        .eq("tgoods_type", ship.getTgoodsType()).eq("tgoods_id", ship.getTgoodsId())
                        .eq("tspecs_id", ship.getTspecsId()));

        SysUserEntity entity = new SysUserEntity();
        entity.setUsername("admin");
        entity.setUserId(1L);
        if (CollectionUtils.isEmpty(list)) {
            goodsShipApprovalReagentService.insert(ship);
            //成功匹配的才需要审批
            if (ship.getShipFlag().equals(IsMatchEnum.YES.getKey())) {
                unicodeGoodsShipApprovalService.initiateApproval(entity, ship, ChangeTypeEnum.ADD.getKey(),
                        ActTypeEnum.HOSPITAL_PLATFORM_REAGENT_MATCH.getKey(),
                        ActivitiConstant.ACTIVITI_HOSPITAL_PLATFORM_REAGENT_MATCH);
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
                        ActTypeEnum.HOSPITAL_PLATFORM_REAGENT_MATCH.getKey(),
                        ActivitiConstant.ACTIVITI_HOSPITAL_PLATFORM_REAGENT_MATCH);
                ship.setCheckStatus(ApprovalTypeEnum.WAIT.getKey());
            }
            goodsShipApprovalReagentService.updateAllColumnById(ship);

        }
        // 更新商品状态为已匹对
        GoodsHospitalReagentEntity hospitalReagentEntity = new GoodsHospitalReagentEntity();
        hospitalReagentEntity.setId(ship.getTgoodsId());
        hospitalReagentEntity.setIsMatch(IsMatchEnum.YES.getKey());
        goodsHospitalReagentDao.updateById(hospitalReagentEntity);
        // 将匹对记录插入匹对历史表
        hist.setShipId(ship.getShipId());
        if (!StringUtil.isEmpty(hospitalReagent.get("hospitalId"))){
            hist.setTorgId(Long.valueOf(hospitalReagent.get("hospitalId").toString()));
        }
        hist.setTorgType(ship.getTorgType());
        hist.setTgoodsType(ship.getTgoodsType());
        hist.setTgoodsNature(hospitalReagent.get("goods_nature")!= null ? Integer.parseInt(hospitalReagent.get("goods_nature").toString()): null);
        if (!StringUtil.isEmpty(hospitalReagent.get("reagent_name"))){
            hist.setTgoodsName(hospitalReagent.get("reagent_name").toString());
        }
        hist.setTgoodsId(ship.getTgoodsId());
        if (!StringUtil.isEmpty(hospitalReagent.get("specs"))){
            hist.setTspecs(hospitalReagent.get("specs").toString());
        }
        hist.setTspecsId(ship.getTspecsId());
        if (!StringUtil.isEmpty(hospitalReagent.get("approvals"))){
            hist.setTapprovals(hospitalReagent.get("approvals").toString());
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
