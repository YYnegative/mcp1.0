package com.ebig.hdi.modules.job.task;

import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.ebig.hdi.common.constant.ActivitiConstant;
import com.ebig.hdi.common.entity.UnicodeGoodsShipApprovalEntity;
import com.ebig.hdi.common.entity.UnicodeGoodsShipHistEntity;
import com.ebig.hdi.common.enums.*;
import com.ebig.hdi.common.utils.ReflectUitls;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.consumables.dao.GoodsHospitalConsumablesDao;
import com.ebig.hdi.modules.consumables.dao.GoodsPlatformConsumablesDao;
import com.ebig.hdi.modules.consumables.dao.GoodsSupplierConsumablesDao;
import com.ebig.hdi.modules.consumables.service.GoodsShipApprovalConsumablesService;
import com.ebig.hdi.modules.core.dao.UnicodeGoodsShipHistDao;
import com.ebig.hdi.modules.core.service.UnicodeGoodsShipApprovalService;
import com.ebig.hdi.modules.drugs.dao.GoodsHospitalDrugsDao;
import com.ebig.hdi.modules.drugs.dao.GoodsPlatformDrugsDao;
import com.ebig.hdi.modules.drugs.dao.GoodsSupplierDrugsDao;
import com.ebig.hdi.modules.drugs.service.GoodsShipApprovalDrugsService;
import com.ebig.hdi.modules.job.entity.ScheduleJobEntity;
import com.ebig.hdi.modules.reagent.dao.GoodsHospitalReagentDao;
import com.ebig.hdi.modules.reagent.dao.GoodsPlatformReagentDao;
import com.ebig.hdi.modules.reagent.dao.GoodsSupplierReagentDao;
import com.ebig.hdi.modules.reagent.service.GoodsShipApprovalReagentService;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @title: HospitalAndSupplierPlatformRematchTask(未匹配的医院商品重新匹配)
 * @author：wenchao
 * @date：2019-12-25 16:09
 * @version：V1.0
 */
@Component("hospitalAndSupplierPlatformRematchTask")
@Slf4j
public class HospitalAndSupplierPlatformRematchTask implements ITask {

    private volatile boolean isRunning = false;

    @Autowired
    private UnicodeGoodsShipApprovalService unicodeGoodsShipApprovalService;

    @Autowired
    private GoodsPlatformConsumablesDao goodsPlatformConsumablesDao;

    @Autowired
    private GoodsPlatformDrugsDao goodsPlatformDrugsDao;

    @Autowired
    private GoodsPlatformReagentDao goodsPlatformReagentDao;

    @Autowired
    private GoodsHospitalConsumablesDao goodsHospitalConsumablesDao;


    @Autowired
    private GoodsHospitalDrugsDao goodsHospitalDrugsDao;


    @Autowired
    private GoodsHospitalReagentDao goodsHospitalReagentDao;

    @Autowired
    private GoodsSupplierConsumablesDao goodsSupplierConsumablesDao;

    @Autowired
    private GoodsSupplierDrugsDao goodsSupplierDrugsDao;

    @Autowired
    private GoodsSupplierReagentDao goodsSupplierReagentDao;
    @Autowired
    private GoodsShipApprovalConsumablesService goodsShipApprovalConsumablesService;

    @Autowired
    private GoodsShipApprovalDrugsService goodsShipApprovalDrugsService;

    @Autowired
    private GoodsShipApprovalReagentService goodsShipApprovalReagentService;

    @Autowired
    private UnicodeGoodsShipHistDao unicodeGoodsShipHistDao;

    private static final String APPROVALS_ID = "approvalsId";

    private static final String SPECS_ID = "specsId";

    @Override
    public void run(ScheduleJobEntity scheduleJob) {
        synchronized (HospitalAndSupplierPlatformRematchTask.class) {
            if (isRunning) {
                log.info("HospitalPlatformRematchTask定时任务已在执行，为避免重复处理数据，将退出本次任务！");
                return;
            }
            isRunning = true;
        }
        try {
            log.info("HospitalPlatformRematchTask定时任务正在执行，参数为：{}", scheduleJob.getParams());
            // 获取1000条未匹对医院记录
            List<UnicodeGoodsShipApprovalEntity> list = unicodeGoodsShipApprovalService.selectNotMatch(1000);
            for (UnicodeGoodsShipApprovalEntity entity : list) {
                match(entity);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            isRunning = false;
        }
    }
    @Transactional(rollbackFor = Exception.class)
    public void match(UnicodeGoodsShipApprovalEntity entity) {
        //匹配规则先根据全球唯一码匹配，若全球唯一码没匹配上，则再根据规格和批准文号匹配
        entity.setShipFlag(IsMatchEnum.YES.getKey());
        SysUserEntity userEntity = new SysUserEntity();
        userEntity.setUsername("admin");
        userEntity.setUserId(1L);
        //耗材
        if (entity.getTgoodsType().equals(MatchGoodsTypeEnum.CONSUMABLE.getKey())) {
            consumablesMath(userEntity, entity);
        } else if (entity.getTgoodsType().equals(MatchGoodsTypeEnum.DRUGS.getKey())) {
            drugMath(userEntity, entity);

        } else if (entity.getTgoodsType().equals(MatchGoodsTypeEnum.REAGENT.getKey())) {
            reagentMath(userEntity, entity);
        }
    }
    @Transactional(rollbackFor = Exception.class)
    public void consumablesMath(SysUserEntity userEntity, UnicodeGoodsShipApprovalEntity entity) {
        if (entity.getTorgType().equals(MatchOrgTypeEnum.HOSPITAL.getKey())) {
            //查询全球唯一码,规格，批准文号
            Map<String, Object> map = goodsHospitalConsumablesDao.selectConsumablesMap(entity.getTapprovalId(), entity.getTspecsId(), entity.getTgoodsId());
            if (map != null && map.size() > 1) {
                List<Map<String, Object>> platformConsumablesList = goodsPlatformConsumablesDao.selectMatch(map);
                if (CollectionUtils.isEmpty(platformConsumablesList)) {
                    return;
                }
                entity.setPgoodsId(Long.valueOf(platformConsumablesList.get(0).get("consumablesId").toString()));
                entity.setPspecsId(Long.valueOf(platformConsumablesList.get(0).get(SPECS_ID).toString()));
                entity.setPapprovalId(Long.valueOf(platformConsumablesList.get(0).get(APPROVALS_ID).toString()));
                unicodeGoodsShipApprovalService.initiateApproval(userEntity, entity, ChangeTypeEnum.UPDATE.getKey(),
                        ActTypeEnum.HOSPITAL_PLATFORM_CONSUMABLES_MATCH.getKey(),
                        ActivitiConstant.ACTIVITI_HOSPITAL_PLATFORM_CONSUMABLES_MATCH);
                entity.setCheckStatus(ApprovalTypeEnum.WAIT.getKey());
                goodsShipApprovalConsumablesService.updateById(entity);
                UnicodeGoodsShipHistEntity histEntity = ReflectUitls.transform(entity, UnicodeGoodsShipHistEntity.class);
                if (!StringUtil.isEmpty(map.get("consumables_name"))){
                    histEntity.setTgoodsName(map.get("consumables_name").toString());
                }
                if (!StringUtil.isEmpty(platformConsumablesList.get(0).get("consumables_name"))){
                    histEntity.setPgoodsName(platformConsumablesList.get(0).get("consumables_name").toString());
                }
                insertHist(histEntity, map, platformConsumablesList.get(0));
            }
        } else {
            //查询全球唯一码,规格，批准文号
            Map<String, Object> map = goodsSupplierConsumablesDao.selectConsumablesMap(entity.getTapprovalId(), entity.getTspecsId(), entity.getTgoodsId());
            if (map != null && map.size() > 1) {
                List<Map<String, Object>> platformConsumablesList = goodsPlatformConsumablesDao.selectMatch(map);
                if (CollectionUtils.isEmpty(platformConsumablesList)) {
                    return;
                }
                entity.setPgoodsId(Long.valueOf(platformConsumablesList.get(0).get("consumablesId").toString()));
                entity.setPspecsId(Long.valueOf(platformConsumablesList.get(0).get(SPECS_ID).toString()));
                entity.setPapprovalId(Long.valueOf(platformConsumablesList.get(0).get(APPROVALS_ID).toString()));
                unicodeGoodsShipApprovalService.initiateApproval(userEntity, entity, ChangeTypeEnum.UPDATE.getKey(),
                        ActTypeEnum.SUPPLIER_PLATFORM_CONSUMABLES_MATCH.getKey(),
                        ActivitiConstant.ACTIVITI_SUPPLIER_PLATFORM_CONSUMABLES_MATCH);
                entity.setCheckStatus(ApprovalTypeEnum.WAIT.getKey());
                goodsShipApprovalConsumablesService.updateById(entity);
                UnicodeGoodsShipHistEntity histEntity = ReflectUitls.transform(entity, UnicodeGoodsShipHistEntity.class);
                if (!StringUtil.isEmpty(map.get("consumables_name"))){
                    histEntity.setTgoodsName(map.get("consumables_name").toString());
                }
                if (!StringUtil.isEmpty(platformConsumablesList.get(0).get("consumables_name"))){
                    histEntity.setPgoodsName(platformConsumablesList.get(0).get("consumables_name").toString());
                }
                insertHist(histEntity, map, platformConsumablesList.get(0));
            }
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void drugMath(SysUserEntity userEntity, UnicodeGoodsShipApprovalEntity entity) {
        if (entity.getTorgType().equals(MatchOrgTypeEnum.HOSPITAL.getKey())) {
            //查询全球唯一码,规格，批准文号
            Map<String, Object> map = goodsHospitalDrugsDao.selectDrugMap(entity.getTspecsId(), entity.getTgoodsId());
            if (map != null && map.size() > 1) {

                List<Map<String, Object>> platformDrugsList = goodsPlatformDrugsDao.selectMatch(map);
                if (CollectionUtils.isEmpty(platformDrugsList)) {
                    return;
                }
                entity.setPgoodsId(Long.valueOf(platformDrugsList.get(0).get("drugsId").toString()));
                entity.setPspecsId(Long.valueOf(platformDrugsList.get(0).get(SPECS_ID).toString()));
                unicodeGoodsShipApprovalService.initiateApproval(userEntity, entity, ChangeTypeEnum.UPDATE.getKey(),
                        ActTypeEnum.HOSPITAL_PLATFORM_DRUGS_MATCH.getKey(),
                        ActivitiConstant.ACTIVITI_HOSPITAL_PLATFORM_DRUGS_MATCH);
                entity.setCheckStatus(ApprovalTypeEnum.WAIT.getKey());
                goodsShipApprovalDrugsService.updateById(entity);
                UnicodeGoodsShipHistEntity histEntity = ReflectUitls.transform(entity, UnicodeGoodsShipHistEntity.class);
                if (!StringUtil.isEmpty(map.get("drugs_name"))){
                    histEntity.setTgoodsName(map.get("drugs_name").toString());
                }
                if (!StringUtil.isEmpty(platformDrugsList.get(0).get("drugsName"))){
                    histEntity.setPgoodsName(platformDrugsList.get(0).get("drugsName").toString());
                }
                insertHist(histEntity, map, platformDrugsList.get(0));
            }
        } else {
            //查询全球唯一码,规格，批准文号
            Map<String, Object> map = goodsSupplierDrugsDao.selectDrugMap(entity.getTspecsId(), entity.getTgoodsId());
            if (map != null && map.size() > 1) {

                List<Map<String, Object>> platformDrugsList = goodsPlatformDrugsDao.selectMatch(map);
                if (CollectionUtils.isEmpty(platformDrugsList)) {
                    return;
                }
                entity.setPgoodsId(Long.valueOf(platformDrugsList.get(0).get("drugsId").toString()));
                entity.setPspecsId(Long.valueOf(platformDrugsList.get(0).get(SPECS_ID).toString()));
                unicodeGoodsShipApprovalService.initiateApproval(userEntity, entity, ChangeTypeEnum.UPDATE.getKey(),
                        ActTypeEnum.SUPPLIER_PLATFORM_DRUGS_MATCH.getKey(),
                        ActivitiConstant.ACTIVITI_SUPPLIER_PLATFORM_DRUGS_MATCH);
                entity.setCheckStatus(ApprovalTypeEnum.WAIT.getKey());
                goodsShipApprovalDrugsService.updateById(entity);
                UnicodeGoodsShipHistEntity histEntity = ReflectUitls.transform(entity, UnicodeGoodsShipHistEntity.class);
                if (!StringUtil.isEmpty(map.get("drugs_name"))){
                    histEntity.setTgoodsName(map.get("drugs_name").toString());
                }
                if (!StringUtil.isEmpty(platformDrugsList.get(0).get("drugsName"))){
                    histEntity.setPgoodsName(platformDrugsList.get(0).get("drugsName").toString());
                }
                insertHist(histEntity, map, platformDrugsList.get(0));

            }
        }


    }
    @Transactional(rollbackFor = Exception.class)
    public void reagentMath(SysUserEntity userEntity, UnicodeGoodsShipApprovalEntity entity) {
        if (entity.getTorgType().equals(MatchOrgTypeEnum.HOSPITAL.getKey())) {
            //查询全球唯一码,规格，批准文号
            Map<String, Object> map = goodsHospitalReagentDao.selectReagentMap(entity.getTspecsId(), entity.getTgoodsId());
            if (map != null && map.size() > 1) {
                List<Map<String, Object>> platformReagentList = goodsPlatformReagentDao.selectMatch(map);
                if (CollectionUtils.isEmpty(platformReagentList)) {
                    return;
                }
                entity.setPgoodsId(Long.valueOf(platformReagentList.get(0).get("reagentId").toString()));
                entity.setPspecsId(Long.valueOf(platformReagentList.get(0).get(SPECS_ID).toString()));
                unicodeGoodsShipApprovalService.initiateApproval(userEntity, entity, ChangeTypeEnum.UPDATE.getKey(),
                        ActTypeEnum.HOSPITAL_PLATFORM_REAGENT_MATCH.getKey(),
                        ActivitiConstant.ACTIVITI_HOSPITAL_PLATFORM_REAGENT_MATCH);
                entity.setCheckStatus(ApprovalTypeEnum.WAIT.getKey());
                goodsShipApprovalReagentService.updateById(entity);
                UnicodeGoodsShipHistEntity histEntity = ReflectUitls.transform(entity, UnicodeGoodsShipHistEntity.class);
                if (!StringUtil.isEmpty(map.get("reagent_name"))){
                    histEntity.setTgoodsName(map.get("reagent_name").toString());
                }
                if (!StringUtil.isEmpty(platformReagentList.get(0).get("reagent_name"))){
                    histEntity.setPgoodsName(platformReagentList.get(0).get("reagent_name").toString());
                }
                insertHist(histEntity, map, platformReagentList.get(0));

            }

        } else {
            //查询全球唯一码,规格，批准文号
            Map<String, Object> map = goodsSupplierReagentDao.selectReagentMap(entity.getTspecsId(), entity.getTgoodsId());
            if (map != null && map.size() > 1) {
                List<Map<String, Object>> platformReagentList = goodsPlatformReagentDao.selectMatch(map);
                if (CollectionUtils.isEmpty(platformReagentList)) {
                    return;
                }
                entity.setPgoodsId(Long.valueOf(platformReagentList.get(0).get("reagentId").toString()));
                entity.setPspecsId(Long.valueOf(platformReagentList.get(0).get(SPECS_ID).toString()));
                unicodeGoodsShipApprovalService.initiateApproval(userEntity, entity, ChangeTypeEnum.UPDATE.getKey(),
                        ActTypeEnum.SUPPLIER_PLATFORM_REAGENT_MATCH.getKey(),
                        ActivitiConstant.ACTIVITI_SUPPLIER_PLATFORM_REAGENT_MATCH);
                entity.setCheckStatus(ApprovalTypeEnum.WAIT.getKey());
                goodsShipApprovalReagentService.updateById(entity);
                UnicodeGoodsShipHistEntity histEntity = ReflectUitls.transform(entity, UnicodeGoodsShipHistEntity.class);
                if (!StringUtil.isEmpty(map.get("reagent_name"))){
                    histEntity.setTgoodsName(map.get("reagent_name").toString());
                }
                if (!StringUtil.isEmpty(platformReagentList.get(0).get("reagent_name"))){
                    histEntity.setPgoodsName(platformReagentList.get(0).get("reagent_name").toString());
                }
                insertHist(histEntity, map, platformReagentList.get(0));
            }

        }

    }

    private void insertHist(UnicodeGoodsShipHistEntity histEntity, Map map, Map platformMap) {
        log.info("HospitalPlatformRematchTask定时任务插入历史记录开始------------------------------------------------");
        //医院或供应商商品属性
        try {
            histEntity.setCredate(new Date());
            histEntity.setTgoodsNature(map.get("goods_nature") != null ? Integer.parseInt(map.get("goods_nature").toString()) : null);
            histEntity.setTspecs( map.get("specs").toString());
            histEntity.setTapprovals(map.get("approvals").toString());

            //平台商品属性
            histEntity.setPfactoryId(Long.parseLong(platformMap.get("factory_id").toString()));
            histEntity.setPfactoryName(platformMap.get("factory_name").toString());
            histEntity.setPgoodsNature(platformMap.get("goods_nature")!= null ? Integer.parseInt(platformMap.get("goods_nature").toString()): null);
            histEntity.setPspecs(platformMap.get("specs").toString());
            histEntity.setPapprovals(platformMap.get("approvals").toString());
            // 操作类型 1匹对 2商品信息变更
            histEntity.setOperType(OperationTypeEnum.MATCH.getKey());
            unicodeGoodsShipHistDao.insert(histEntity);
        }catch ( Exception e){
            log.error(e.getMessage());
        }
        log.info("HospitalPlatformRematchTask定时任务插入历史记录结束------------------------------------------------");


    }
}