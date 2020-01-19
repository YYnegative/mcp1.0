package com.ebig.hdi.modules.drugs.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.constant.ActivitiConstant;
import com.ebig.hdi.common.enums.*;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.activiti.entity.ActApprovalEntity;
import com.ebig.hdi.modules.activiti.service.ActApprovalService;
import com.ebig.hdi.modules.drugs.common.UnicodeDrugsCate;
import com.ebig.hdi.modules.drugs.dao.GoodsPlatformDrugsApprovalDao;
import com.ebig.hdi.modules.drugs.dao.UnicodeDrugsCateDao;
import com.ebig.hdi.modules.drugs.entity.DrugsTreeNode;
import com.ebig.hdi.modules.drugs.entity.GoodsPlatformDrugsApprovalEntity;
import com.ebig.hdi.modules.drugs.entity.GoodsPlatformDrugsEntity;
import com.ebig.hdi.modules.drugs.entity.GoodsPlatformDrugsSpecsEntity;
import com.ebig.hdi.modules.drugs.service.*;
import com.ebig.hdi.modules.drugs.vo.GoodsPlatformDrugsEntityVo;
import com.ebig.hdi.modules.drugs.vo.GoodsPlatformDrugsParams;
import com.ebig.hdi.modules.sys.entity.SysDictEntity;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import com.ebig.hdi.modules.sys.service.SysDictService;
import com.ebig.hdi.modules.sys.service.SysSequenceService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
@Service("goodsPlatformDrugsApprovalService")
public class GoodsPlatformDrugsApprovalServiceImpl extends ServiceImpl<GoodsPlatformDrugsApprovalDao, GoodsPlatformDrugsApprovalEntity>
        implements GoodsPlatformDrugsApprovalService {

    @Autowired
    private GoodsPlatformDrugsSpecsService goodsPlatformDrugsSpecsService;

    @Autowired
    private OrgFactoryService orgFactoryService;

    @Autowired
    private SysSequenceService sysSequenceService;

    @Autowired
    private UnicodeDrugsCateService unicodeDrugsCateService;

    @Autowired
    private ActApprovalService actApprovalService;
    @Autowired
    private GoodsPlatformDrugsService goodsPlatformDrugsService;
    @Autowired
    private UnicodeDrugsCateDao unicodeDrugsCateDao;

    @Autowired
    private SysDictService sysDictService;

    @Override
    @DataFilter(subDept = true, user = false, tableAlias = "d")
    public PageUtils queryPage(Map<String, Object> params) {
        int currPage = Integer.parseInt(params.get("page").toString());
        int pageSize = Integer.parseInt(params.get("limit").toString());

        if (params != null && params.get("typeId") != null) {

            List<Long> cateIds = new ArrayList<>();
            cateIds.add(Long.parseLong(params.get("typeId").toString()));
            unicodeDrugsCateService.queryList(Long.parseLong(params.get("typeId").toString()), cateIds);
            params.put("typeId", cateIds);
        }
        Page<GoodsPlatformDrugsEntityVo> page = new Page<>(currPage, pageSize);
        List<GoodsPlatformDrugsEntityVo> list = this.baseMapper.selectPlatformDrugsList(page, params);

        for (GoodsPlatformDrugsEntityVo goodsPlatformDrugs : list) {
            List<GoodsPlatformDrugsSpecsEntity> specsList = goodsPlatformDrugsSpecsService.selectListByDrugsId(goodsPlatformDrugs.getId());
            goodsPlatformDrugs.setSpecsEntityList(specsList);
        }
        page.setRecords(list);
        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> save(GoodsPlatformDrugsEntityVo goodsPlatformDrugsEntityVo, SysUserEntity user) throws Exception {
        Map<String, String> errorMap = new HashMap<>(16);
        // 判断药品名称是否存在
        GoodsPlatformDrugsApprovalEntity drugsName = this.baseMapper.selectByDrugsName(goodsPlatformDrugsEntityVo.getDrugsName());
        if (!user.getUserType().equals(TypeEnum.USER_PLATFORM.getKey())) {
            errorMap.put("errorMessage", "非平台用户不能操作");
            return errorMap;
        }
        Integer approvalsExist = this.baseMapper.selectByApprovals(goodsPlatformDrugsEntityVo.getApprovals());
        if (approvalsExist>0) {
            errorMap.put("errorMessage", "批准文号/注册文号" + goodsPlatformDrugsEntityVo.getApprovals() + "已存在");
            return errorMap;
        }
        if (!StringUtil.isEmpty(drugsName)) {
            errorMap.put("errorMessage", "药品名称：" + goodsPlatformDrugsEntityVo.getDrugsName() + "，已存在！");
            return errorMap;
        }
        if (StringUtil.isEmpty(goodsPlatformDrugsEntityVo.getFactoryId())) {
            String factoryId = this.baseMapper.selectByFactoryNameExist(goodsPlatformDrugsEntityVo.getFactoryName());
            if (StringUtil.isEmpty(factoryId)) {
                errorMap.put("errorMessage", "本生产厂商未建档，请先建档等待审核通过");
                return errorMap;
            } else {
                goodsPlatformDrugsEntityVo.setFactoryId(factoryId);
            }

        }

        // 生成药品编码（商品码）
        String drugsCode = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.PLATFORM_DRUGS_CODE.getKey());
        goodsPlatformDrugsEntityVo.setDrugsCode(drugsCode);

        String factoryCode = orgFactoryService.selectById(goodsPlatformDrugsEntityVo.getFactoryId()).getFactoryCode();

        // 生成商品统一编码
        String goodsUnicode = unicodeDrugsCateService.generatorGoodsUnicode(goodsPlatformDrugsEntityVo.getGoodsNature(),
                goodsPlatformDrugsEntityVo.getTypeId(), drugsCode, factoryCode);

        goodsPlatformDrugsEntityVo.setGoodsUnicode(goodsUnicode);
        goodsPlatformDrugsEntityVo.setCreateTime(new Date());
        this.baseMapper.insert(goodsPlatformDrugsEntityVo);
        //发起审批
        startApproval(goodsPlatformDrugsEntityVo, user);
        // 保存商品规格
        goodsPlatformDrugsSpecsService.save(goodsPlatformDrugsEntityVo);
        return errorMap;
    }

    public void startApproval(GoodsPlatformDrugsEntityVo goodsPlatformDrugsEntityVo, SysUserEntity user) {
        ActApprovalEntity approvalEntity = actApprovalService.getActApprovalEntity(user, ChangeTypeEnum.ADD.getKey(),
                ActTypeEnum.PLATFORM_DRUGS.getKey(), goodsPlatformDrugsEntityVo.getId().toString(),
                goodsPlatformDrugsEntityVo.getDrugsCode(), goodsPlatformDrugsEntityVo.getDrugsName());
        Map<String, Object> map = new HashMap<>(16);
        map.put("approvalEntity", approvalEntity);
        actApprovalService.insert(approvalEntity);
        ProcessInstance instance = actApprovalService.startProcess(user.getUserId().toString(),
                ActivitiConstant.ACTIVITI_GOODS_PLATFORM_DRUGS[0], ActivitiConstant.ACTIVITI_GOODS_PLATFORM_DRUGS[1], map);
        if (instance != null) {
            approvalEntity.setProcessId(instance.getProcessInstanceId());
            approvalEntity.setApprovalCode(instance.getProcessInstanceId());
            goodsPlatformDrugsEntityVo.setProcessId(instance.getProcessInstanceId());
        }
        goodsPlatformDrugsEntityVo.setCheckStatus(ApprovalTypeEnum.WAIT.getKey());
        this.baseMapper.updateById(goodsPlatformDrugsEntityVo);
        // 更新审批流

        actApprovalService.updateById(approvalEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(GoodsPlatformDrugsEntityVo goodsPlatformDrugsEntityVo, SysUserEntity user) {

        // 判断药品名称是否存在
        GoodsPlatformDrugsApprovalEntity drugsName = this.baseMapper
                .selectByDrugsNameAndId(goodsPlatformDrugsEntityVo.getDrugsName(), goodsPlatformDrugsEntityVo.getId());
        if (!StringUtil.isEmpty(drugsName) && !drugsName.getId().equals(goodsPlatformDrugsEntityVo.getId())) {
            throw new HdiException("药品名称：" + goodsPlatformDrugsEntityVo.getDrugsName() + "，已存在！");
        }
        Integer approvalsExist = this.baseMapper.selectByApprovalsAndId(goodsPlatformDrugsEntityVo.getId(),goodsPlatformDrugsEntityVo.getApprovals());
        if (approvalsExist>0) {
            throw new HdiException("批准文号/注册文号" + goodsPlatformDrugsEntityVo.getApprovals() + "已存在");

        }
        // 生成商品统一编码
        String factoryCode = orgFactoryService.selectById(goodsPlatformDrugsEntityVo.getFactoryId()).getFactoryCode();
        String goodsUnicode = unicodeDrugsCateService.generatorGoodsUnicode(goodsPlatformDrugsEntityVo.getGoodsNature(), goodsPlatformDrugsEntityVo.getTypeId(), goodsPlatformDrugsEntityVo.getDrugsCode(), factoryCode);
        goodsPlatformDrugsEntityVo.setGoodsUnicode(goodsUnicode);

        goodsPlatformDrugsEntityVo.setEditTime(new Date());

        if (goodsPlatformDrugsEntityVo.getCheckStatus().equals(ApprovalTypeEnum.FAIL.getKey())) {
            ActApprovalEntity approvalEntity = actApprovalService.getActApprovalEntity(user, ChangeTypeEnum.UPDATE.getKey(),
                    ActTypeEnum.PLATFORM_DRUGS.getKey(), goodsPlatformDrugsEntityVo.getId().toString(),
                    goodsPlatformDrugsEntityVo.getDrugsCode(), goodsPlatformDrugsEntityVo.getDrugsName());
            Map<String, Object> map = new HashMap<>(16);
            map.put("approvalEntity", approvalEntity);
            actApprovalService.insert(approvalEntity);
            ProcessInstance instance = actApprovalService.startProcess(user.getUserId().toString(),
                    ActivitiConstant.ACTIVITI_GOODS_PLATFORM_DRUGS[0], ActivitiConstant.ACTIVITI_GOODS_PLATFORM_DRUGS[1], map);
            if (instance != null) {
                approvalEntity.setProcessId(instance.getProcessInstanceId());
                approvalEntity.setApprovalCode(instance.getProcessInstanceId());
                goodsPlatformDrugsEntityVo.setProcessId(instance.getProcessInstanceId());
            }
            goodsPlatformDrugsEntityVo.setCheckStatus(ApprovalTypeEnum.WAIT.getKey());
            this.baseMapper.updateById(goodsPlatformDrugsEntityVo);
            // 更新审批流

            actApprovalService.updateById(approvalEntity);
        }
        if (goodsPlatformDrugsEntityVo.getCheckStatus().equals(ApprovalTypeEnum.WAIT.getKey())) {
            this.baseMapper.updateById(goodsPlatformDrugsEntityVo);

        }
        if (goodsPlatformDrugsEntityVo.getCheckStatus().equals(ApprovalTypeEnum.PASS.getKey())) {
            this.baseMapper.updateById(goodsPlatformDrugsEntityVo);
            goodsPlatformDrugsService.updateByGoodsDrugsApprovalEntity(goodsPlatformDrugsEntityVo);
        }

    }

    @Override
    public GoodsPlatformDrugsEntityVo selectPlatformDrugsById(Long id) {
        return this.baseMapper.selectPlatformDrugsById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        for (Long id : ids) {
            GoodsPlatformDrugsApprovalEntity goodsPlatformDrugs = this.baseMapper.selectById(id);
            if (!goodsPlatformDrugs.getCheckStatus().equals(ApprovalTypeEnum.PASS.getKey())){
                goodsPlatformDrugs.setDelFlag(DelFlagEnum.DELETE.getKey());
                GoodsPlatformDrugsEntity goodsPlatformDrugsEntity = goodsPlatformDrugsService.selectById(id);
                if (!StringUtil.isEmpty(goodsPlatformDrugsEntity)) {
                    goodsPlatformDrugsEntity.setDelFlag(DelFlagEnum.DELETE.getKey());
                    goodsPlatformDrugsService.updateById(goodsPlatformDrugsEntity);
                }
                this.baseMapper.updateById(goodsPlatformDrugs);
            }else {
                throw new HdiException("已审批通过的记录不能删除");
            }
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toggle(Map<String, Object> params) {
        List<Long> ids = JSON.parseArray(params.get("ids").toString(), Long.class);
        for (Long id : ids) {
            GoodsPlatformDrugsApprovalEntity goodsPlatformDrugs = new GoodsPlatformDrugsApprovalEntity();
            goodsPlatformDrugs.setId(id);
            goodsPlatformDrugs.setStatus(Integer.valueOf(params.get("status").toString()));
            Integer drugsExist = this.baseMapper.selectIfExist(id);
            if (drugsExist.equals(1)) {
                GoodsPlatformDrugsEntity goodsPlatformDrugsEntity = new GoodsPlatformDrugsEntity();
                goodsPlatformDrugsEntity.setId(id);
                goodsPlatformDrugsEntity.setStatus(Integer.valueOf(params.get("status").toString()));
                goodsPlatformDrugsService.updateById(goodsPlatformDrugsEntity);
            }
            this.baseMapper.updateById(goodsPlatformDrugs);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> importData(String[][] rows, SysUserEntity user) {

        Set<GoodsPlatformDrugsEntityVo> updateDrugsList = new HashSet<>(rows.length);
        if (!user.getUserType().equals(TypeEnum.USER_PLATFORM.getKey())) {
            throw new HdiException("当前登录用户无此权限");
        }


        Map<String, String> errorMap = new HashMap<>(16);
        StringBuilder sb = new StringBuilder();
        String key = "successCount";
        Integer failCount = 0;
        Integer successCount = 0;
        for (int i = 1; i < rows.length; i++) {
            GoodsPlatformDrugsEntityVo goodsPlatformDrugsEntityVo = new GoodsPlatformDrugsEntityVo();
            String[] row = rows[i];
            for (int i1 = 0; i1 < row[i].length(); i1++) {
                row[i1] = row[i1].trim();
            }
            String drugsName = row[0];
            String goodsNature = row[1];
            String cateName = row[2];
            String specs = row[3];
            String factoryName = row[4];
            String approvals = row[5];
            String commonName = row[6];
            String storageWay = row[7];
            String goodsUnit = row[8];
            String goodsGuid = row[9];
            //校验必填数据
            if (StringUtil.isEmpty(drugsName) || StringUtil.isEmpty(goodsNature) ||
                    StringUtil.isEmpty(cateName) || StringUtil.isEmpty(specs) || StringUtil.isEmpty(factoryName) || StringUtil.isEmpty(approvals) || StringUtil.isEmpty(goodsUnit)) {
                sb.append("\n");
                sb.append("第 ").append(i + 2).append("行数据错误，必填字段不能为空");
                failCount++;
                continue;
            }
            if (!"国产".equals(goodsNature)) {
                if (!"进口".equals(goodsNature)) {
                    sb.append("\n");
                    sb.append("\n导入失败，商品属性填写不正确,第:").append(i + 2).append(" 数据");
                    failCount++;
                    continue;
                }
            }
            //判断厂商名称是否存在
            String factoryId = this.baseMapper.selectByFactoryName(factoryName);
            if (StringUtil.isEmpty(factoryId)) {
                sb.append("\n");
                sb.append("第 ").append(i + 2).append("行数据错误，请检查该数据中生产厂商是否已建档");
                failCount++;
                continue;
            }
            //存储方式转化
            List<SysDictEntity> sysDicts = sysDictService.selectDictByType("store_way");
            Map<String, SysDictEntity> maps = sysDicts.stream().collect(Collectors.toMap(SysDictEntity::getValue, Function.identity()));
            SysDictEntity sysDictEntity = maps.get(storageWay);
            if (null != sysDictEntity) {
                storageWay = sysDictEntity.getCode();
            }
            String typeId = unicodeDrugsCateDao.selectIdByCateName(cateName);
            if (StringUtil.isEmpty(typeId)) {
                sb.append("\n");
                sb.append("第 ").append(i + 2).append("行数据错误，商品分类未存在");
                failCount++;
                continue;
            }

            // 校验 名称 + 批准文号
            GoodsPlatformDrugsEntityVo goodsDrugs = this.baseMapper.selectByDrugsNameAndApprovals(drugsName, approvals);
            if (!StringUtil.isEmpty(goodsDrugs)) {
                Integer goodsNatureExist = "国产".equals(goodsNature) ? 0 : 1;
                goodsDrugs.setGoodsNature(goodsNatureExist);
                goodsDrugs.setTypeName(cateName);
                goodsDrugs.setSpecs(specs);
                goodsDrugs.setFactoryName(factoryName);
                goodsDrugs.setCommonName(commonName);
                goodsDrugs.setStoreWay(storageWay);
                goodsDrugs.setGoodsUnit(checkGoodsUnit(goodsUnit));
                goodsDrugs.setGuid(goodsGuid);
                goodsDrugs.setEditId(user.getUserId());
                goodsDrugs.setEditTime(new Date());
                updateDrugsList.add(goodsDrugs);
                // 校验规格
                GoodsPlatformDrugsSpecsEntity goodsPlatformDrugsSpecsEntity = goodsPlatformDrugsSpecsService.selectByDrugsIdAndSpecs(goodsDrugs.getId(), specs);
                if (StringUtil.isEmpty(goodsPlatformDrugsSpecsEntity)) {
                    try {
                        goodsPlatformDrugsSpecsService.save(goodsDrugs);
                    } catch (Exception e) {
                        sb.append("\n");
                        sb.append("第 ").append(i + 2).append("行数据错误，请检查该数据").append(goodsGuid).append("已存在");
                        failCount++;
                        continue;
                    }
                }
                successCount++;
            } else {
                //校验 名称 或 批准文号
                Integer errorApprovalsFlag = this.baseMapper.selectByGoodsNameOrApporvaols(drugsName, approvals);
                if (errorApprovalsFlag != null) {
                    sb.append("\n");
                    sb.append("第 ").append(i + 2).append("行数据错误，请检查该数据中名称与批准文号是否正确,已存在该药品名或批准文号");
                    failCount++;
                    continue;
                } else {
                    goodsPlatformDrugsEntityVo.setFactoryId(factoryId);
                    Integer goodsNatureExist = "国产".equals(goodsNature) ? 0 : 1;
                    goodsPlatformDrugsEntityVo.setGoodsNature(goodsNatureExist);
                    goodsPlatformDrugsEntityVo.setTypeId(Long.parseLong(typeId));
                    // 生成药品编码（商品码）
                    String drugsCode = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.PLATFORM_DRUGS_CODE.getKey());
                    String factoryCode = orgFactoryService.selectById(goodsPlatformDrugsEntityVo.getFactoryId()).getFactoryCode();
                    String goodsUnicode = unicodeDrugsCateService.generatorGoodsUnicode(goodsPlatformDrugsEntityVo.getGoodsNature(),
                            Long.parseLong(typeId), drugsCode, factoryCode);
                    goodsPlatformDrugsEntityVo.setDrugsCode(drugsCode);
                    goodsPlatformDrugsEntityVo.setGoodsUnicode(goodsUnicode);
                    goodsPlatformDrugsEntityVo.setCreateTime(new Date());
                    goodsPlatformDrugsEntityVo.setCreateId(user.getUserId());
                    goodsPlatformDrugsEntityVo.setDrugsName(drugsName);
                    goodsPlatformDrugsEntityVo.setDeptId(user.getDeptId());
                    goodsPlatformDrugsEntityVo.setSpecs(specs);
                    goodsPlatformDrugsEntityVo.setFactoryName(factoryName);
                    goodsPlatformDrugsEntityVo.setApprovals(approvals);
                    goodsPlatformDrugsEntityVo.setStatus(StatusEnum.USABLE.getKey());
                    goodsPlatformDrugsEntityVo.setDelFlag(DelFlagEnum.NORMAL.getKey());
                    goodsPlatformDrugsEntityVo.setCommonName(commonName);
                    goodsPlatformDrugsEntityVo.setStoreWay(storageWay);
                    goodsPlatformDrugsEntityVo.setGoodsUnit(checkGoodsUnit(goodsUnit));
                    goodsPlatformDrugsEntityVo.setGuid(goodsGuid);
                    this.baseMapper.insert(goodsPlatformDrugsEntityVo);
                    try {
                        goodsPlatformDrugsSpecsService.save(goodsPlatformDrugsEntityVo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    startApproval(goodsPlatformDrugsEntityVo, user);
                    successCount++;
                }
            }
        }
        if (failCount > 0) {
            throw new HdiException("导入失败,错误条数" + failCount.toString() + "\n 错误信息" + sb.toString());
        }

        errorMap.put("errorMessage", sb.toString());
        errorMap.put(key, successCount.toString());
        errorMap.put("failCount", failCount.toString());
        return errorMap;
    }
    @Transactional(rollbackFor = Exception.class)
    public String checkGoodsUnit(String srcGoodsUnit) {
        String goodsUnit = null;
        if (!StringUtil.isEmpty(srcGoodsUnit)) {
            List<SysDictEntity> dicts = sysDictService.selectList(new EntityWrapper<SysDictEntity>().eq("value", srcGoodsUnit));
            if (StringUtil.isEmpty(dicts)) {
                SysDictEntity dict = new SysDictEntity();
                dict.setName("商品单位");
                dict.setType("goods_unit");
                dict.setCode(sysSequenceService.selectSeqValueBySeqCode("GOODS_UNIT_CODE"));
                dict.setValue(srcGoodsUnit);
                sysDictService.insert(dict);
                goodsUnit = dict.getCode();
            } else {
                goodsUnit = dicts.get(0).getCode();
            }
        }
        return goodsUnit;
    }
    @Override
    @DataFilter(subDept = true, user = false, tableAlias = "gpd")
    public List<Map<String, Object>> getList(Map<String, Object> mapList) {
        Map params = (Map)mapList.get("queryParams");
        if (params != null && params.get("typeId") != null) {

            List<Long> cateIds = new ArrayList<>();
            cateIds.add(Long.parseLong(params.get("typeId").toString()));
            unicodeDrugsCateService.queryList(Long.parseLong(params.get("typeId").toString()), cateIds);
            params.put("typeIds", cateIds);
        }
        List<Map<String, Object>> list = this.baseMapper.getList(mapList);
        for (Map<String, Object> map : list) {
            map.put("typeId", UnicodeDrugsCate.getUnicodeDrugsMap((Long) map.get("type_id")));
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> checkStatus(Map<String, Object> params, SysUserEntity user) {
        Map<String, String> errorMessage = new HashMap<>(16);
        Integer checkStatus = (Integer) params.get("checkStatus");
        Integer platformDrugsId = (Integer) params.get("goodsId");
        if (StringUtil.isEmpty(checkStatus)) {
            errorMessage.put("errorMessage", "状态为空");
            return errorMessage;
        }
        if (StringUtil.isEmpty(platformDrugsId)) {
            errorMessage.put("errorMessage", "传入ID为空");
            return errorMessage;
        }
        if (!user.getUserType().equals(TypeEnum.USER_PLATFORM.getKey())) {
            errorMessage.put("errorMessage", "非平台用户不能操作");
            return errorMessage;
        }
        GoodsPlatformDrugsApprovalEntity goodsPlatformDrugsApprovalEntity = this.baseMapper.selectById(platformDrugsId);
        goodsPlatformDrugsApprovalEntity.setEditId(user.getUserId());
        goodsPlatformDrugsApprovalEntity.setEditTime(new Date());
        goodsPlatformDrugsApprovalEntity.setCheckStatus(checkStatus);
        if (checkStatus.equals(ApprovalTypeEnum.PASS.getKey())) {
            goodsPlatformDrugsApprovalEntity.setCheckStatus(ApprovalTypeEnum.PASS.getKey());
            GoodsPlatformDrugsEntity goodsPlatformDrugsEntity = goodsPlatformDrugsService.selectById(goodsPlatformDrugsApprovalEntity.getId());
            if (StringUtil.isEmpty(goodsPlatformDrugsEntity)) {
                goodsPlatformDrugsService.insertByGoodsDrugsApprovalEntity(goodsPlatformDrugsApprovalEntity);

            } else {
                goodsPlatformDrugsService.updateByGoodsDrugsApprovalEntity(goodsPlatformDrugsApprovalEntity);

            }
        }
        if (checkStatus.equals(ApprovalTypeEnum.FAIL.getKey())) {
            goodsPlatformDrugsApprovalEntity.setCheckStatus(ApprovalTypeEnum.FAIL.getKey());
            this.baseMapper.updateById(goodsPlatformDrugsApprovalEntity);
        }
        this.baseMapper.updateById(goodsPlatformDrugsApprovalEntity);
        return errorMessage;
    }


}
