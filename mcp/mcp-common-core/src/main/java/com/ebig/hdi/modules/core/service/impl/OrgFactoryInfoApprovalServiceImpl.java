package com.ebig.hdi.modules.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.ebig.hdi.common.constant.ActivitiConstant;
import com.ebig.hdi.common.entity.OrgFactoryInfoApprovalEntity;
import com.ebig.hdi.common.entity.OrgFactoryInfoEntity;
import com.ebig.hdi.common.enums.*;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.Query;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.activiti.entity.ActApprovalEntity;
import com.ebig.hdi.modules.activiti.service.ActApprovalService;

import com.ebig.hdi.modules.core.common.AddressUtil;
import com.ebig.hdi.modules.core.dao.OrgFactoryInfoApprovalDao;
import com.ebig.hdi.modules.core.param.OrgFactoryParam;
import com.ebig.hdi.modules.core.service.OrgFactoryInfoApprovalService;
import com.ebig.hdi.modules.core.service.OrgFactoryInfoService;
import com.ebig.hdi.modules.sys.entity.SysDictEntity;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import com.ebig.hdi.modules.sys.service.SysDictService;
import com.ebig.hdi.modules.sys.service.SysSequenceService;
import com.ebig.hdi.modules.sys.service.SysUserService;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service("orgFactoryInfoApprovalService_")
public class OrgFactoryInfoApprovalServiceImpl extends ServiceImpl<OrgFactoryInfoApprovalDao, OrgFactoryInfoApprovalEntity> implements OrgFactoryInfoApprovalService {

    @Autowired
    private SysSequenceService sysSequenceService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ActApprovalService actApprovalService;

    @Autowired
    private OrgFactoryInfoService orgFactoryInfoService;

    @Autowired
    private SysDictService sysDictService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String factoryName = (String) params.get("factoryName");
        String countryCode = (String) params.get("countryCode");
        String provinceCode = (String) params.get("provinceCode");
        String cityCode = (String) params.get("cityCode");
        String areaCode = (String) params.get("areaCode");
        Integer status = (Integer) params.get("status");
        Integer checkStatus = (Integer) params.get("checkStatus");

        Page<OrgFactoryInfoApprovalEntity> page = this.selectPage(
                new Query<OrgFactoryInfoApprovalEntity>(params).getPage(),
                new EntityWrapper<OrgFactoryInfoApprovalEntity>()
                        .like(StringUtils.isNotBlank(factoryName), "factory_name", factoryName)
                        .eq(StringUtils.isNotBlank(countryCode), "country_code", countryCode)
                        .eq(StringUtils.isNotBlank(provinceCode), "province_code", provinceCode)
                        .eq(StringUtils.isNotBlank(cityCode), "city_code", cityCode)
                        .eq(StringUtils.isNotBlank(areaCode), "area_code", areaCode)
                        .eq(status != null, "status", status)
                        .eq(checkStatus != null, "check_status", checkStatus)
                        .eq("del_flag", DelFlagEnum.NORMAL.getKey())
                        .orderBy("edit_time", false)
                        .orderBy("create_time", false)
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        for (Long id : ids) {
            OrgFactoryInfoApprovalEntity orgFactoryInfo = new OrgFactoryInfoApprovalEntity();
            orgFactoryInfo.setId(id);
            orgFactoryInfo.setDelFlag(DelFlagEnum.DELETE.getKey());
            this.updateById(orgFactoryInfo);
        }
    }

    @Override
    public List<OrgFactoryInfoApprovalEntity> queryByFactoryName(Map<String, Object> params) {
        String factoryName = (String) params.get("factoryName");
        return this.selectList(
                new EntityWrapper<OrgFactoryInfoApprovalEntity>()
                        .like(StringUtils.isNotBlank(factoryName), "factory_name", factoryName)
                        .eq("status", StatusEnum.USABLE.getKey())
                        .eq("check_status",ApprovalTypeEnum.PASS)
                        .eq("del_flag", DelFlagEnum.NORMAL.getKey())
        );
    }

    @Override
    public void toggle(Map<String, Object> params) {
        List<Long> ids = JSON.parseArray(params.get("ids").toString(), Long.class);
        Integer status = Integer.valueOf(params.get("status").toString());
        for (Long id : ids) {
            OrgFactoryInfoApprovalEntity orgFactoryInfoApprovalEntity = this.baseMapper.selectById(id);
            orgFactoryInfoApprovalEntity.setStatus(status);
            List<OrgFactoryInfoEntity> orgFactoryInfoEntities = this.baseMapper.selectFactoryInfoById(id);
            if (CollectionUtils.isNotEmpty(orgFactoryInfoEntities)){
                //如果原表有数据,修改原表状态
                this.baseMapper.updateFactoryInfoById(id,status);
            }
            this.baseMapper.updateById(orgFactoryInfoApprovalEntity);
        }
    }

    @Override
    public void update(OrgFactoryInfoApprovalEntity orgFactoryInfoApproval, SysUserEntity entity) {
        List<OrgFactoryInfoApprovalEntity> factoryList = baseMapper.selectByFactoryName(orgFactoryInfoApproval.getFactoryName());
        if (!StringUtil.isEmpty(factoryList) && !orgFactoryInfoApproval.getId().equals(factoryList.get(0).getId())) {
            throw new HdiException("厂商名称已存在");
        }
        List<OrgFactoryInfoApprovalEntity> factoryList2 = baseMapper.selectByCreditCode(orgFactoryInfoApproval.getCreditCode());
        if (!StringUtil.isEmpty(factoryList2) && !orgFactoryInfoApproval.getId().equals(factoryList2.get(0).getId())) {
            throw new HdiException("厂商信用代码已存在");
        }

        orgFactoryInfoApproval.setEditTime(new Date());
        orgFactoryInfoApproval.setEditId(entity.getUserId());
        orgFactoryInfoApproval.setCheckStatus(ApprovalTypeEnum.WAIT.getKey());
        //发起审批
        ActApprovalEntity approvalEntity = actApprovalService.getActApprovalEntity(entity, ChangeTypeEnum.UPDATE.getKey(),
                ActTypeEnum.FACTORY.getKey(), orgFactoryInfoApproval.getId().toString(),
                orgFactoryInfoApproval.getFactoryCode(), orgFactoryInfoApproval.getFactoryName());
        Map<String, Object> map = new HashMap<>(16);
        map.put("approvalEntity", approvalEntity);
        actApprovalService.insert(approvalEntity);
        ProcessInstance instance = actApprovalService.startProcess(entity.getUserId().toString(),
                ActivitiConstant.ACTIVITI_ORGFACTORY[0], ActivitiConstant.ACTIVITI_ORGFACTORY[1], map);
        if (instance != null) {
            approvalEntity.setProcessId(instance.getProcessInstanceId());
            approvalEntity.setApprovalCode(instance.getProcessInstanceId());
            orgFactoryInfoApproval.setProcessId(instance.getProcessInstanceId());
        }
        this.baseMapper.updateById(orgFactoryInfoApproval);
        actApprovalService.updateById(approvalEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> save(OrgFactoryInfoApprovalEntity orgFactoryInfoApproval, SysUserEntity entity) {
        SysUserEntity sysUserEntity = sysUserService.selectById(orgFactoryInfoApproval.getCreateId());
        Map<String, Object> map = new HashMap<>(16);
        if (StringUtil.isEmpty(sysUserEntity)) {
            map.put("errmessage", "用户不存在！");
            return map;
        }

        if (!TypeEnum.USER_PLATFORM.getKey().equals(sysUserEntity.getUserType())) {
            map.put("errmessage", "非平台用户无法操作该功能！");
            return map;
        }

        List<OrgFactoryInfoApprovalEntity> factoryName = baseMapper.selectByFactoryName(orgFactoryInfoApproval.getFactoryName());
        if (!StringUtil.isEmpty(factoryName)) {
            map.put("errmessage", "厂商名称已存在！");
            return map;
        }
        List<OrgFactoryInfoApprovalEntity> creditCode = baseMapper.selectByCreditCode(orgFactoryInfoApproval.getCreditCode());
        if (!StringUtil.isEmpty(creditCode)) {
            map.put("errmessage", "厂商信用代码已存在！");
            return map;
        }
        //获取厂商编码序列号
        String factoryCode = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.FACTORY_CODE.getKey());
        orgFactoryInfoApproval.setFactoryCode(factoryCode);
        orgFactoryInfoApproval.setCreateTime(new Date());
        orgFactoryInfoApproval.setEditTime(new Date());
        orgFactoryInfoApproval.setDelFlag(DelFlagEnum.NORMAL.getKey());
        orgFactoryInfoApproval.setCheckStatus(ApprovalTypeEnum.WAIT.getKey());
        this.insert(orgFactoryInfoApproval);
        //发起审批
        ActApprovalEntity approvalEntity = actApprovalService.getActApprovalEntity(entity, ChangeTypeEnum.ADD.getKey(),
                ActTypeEnum.FACTORY.getKey(), orgFactoryInfoApproval.getId().toString(),
                factoryCode, orgFactoryInfoApproval.getFactoryName());
        Map<String, Object> approvalMap = new HashMap<>(16);
        approvalMap.put("approvalEntity", approvalEntity);
        actApprovalService.insert(approvalEntity);
        ProcessInstance instance = actApprovalService.startProcess(entity.getUserId().toString(),
                ActivitiConstant.ACTIVITI_ORGFACTORY[0], ActivitiConstant.ACTIVITI_ORGFACTORY[1], approvalMap);
        if (instance != null) {
            approvalEntity.setProcessId(instance.getProcessInstanceId());
            approvalEntity.setApprovalCode(instance.getProcessInstanceId());
            orgFactoryInfoApproval.setProcessId(instance.getProcessInstanceId());
        }
        this.baseMapper.updateById(orgFactoryInfoApproval);
        actApprovalService.updateById(approvalEntity);
        return map;
    }

    @Override
    public List<Map<String, Object>> getList(String[] columns, OrgFactoryParam queryParam) {
        List<Map<String, Object>> list = this.baseMapper.getList(columns, queryParam);
        StringBuffer address = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            address.append(AddressUtil.getProvinceMap().get(list.get(i).get("province_code"))).append("/")
                    .append(AddressUtil.getCityMap().get(list.get(i).get("city_code"))).append("/")
                    .append(AddressUtil.getAreaMap().get(list.get(i).get("area_code")));
            list.get(i).put("address", address.toString());
            address.setLength(0);
        }

        return list;
    }

    @Override
    public Map<String, Object> checkStatus(Map<String, Object> params, SysUserEntity user) {
        Integer checkStatus = (Integer) params.get("checkStatus");
        Map<String, Object> map = new HashMap<>(16);
        if (StringUtil.isEmpty(checkStatus)) {
            map.put("errorMessage", "状态为空");
            return map;
        }
        Integer orgId = (Integer) params.get("orgId");
        if (StringUtil.isEmpty(orgId)) {
            map.put("errorMessage", "传入ID为空");
            return map;
        }
        OrgFactoryInfoApprovalEntity orgFactoryInfoApproval = this.baseMapper.selectById(orgId);
        if (checkStatus.equals(ApprovalTypeEnum.PASS.getKey())) {
            //审核通过（修改厂商信息表）
            orgFactoryInfoApproval.setCheckStatus(ApprovalTypeEnum.PASS.getKey());
            orgFactoryInfoApproval.setEditTime(new Date());
            orgFactoryInfoApproval.setEditId(user.getUserId());
            this.updateById(orgFactoryInfoApproval);
            List<OrgFactoryInfoEntity> orgFactoryInfoEntities = this.baseMapper.selectListByFactoryCode(orgFactoryInfoApproval.getFactoryCode());
            //表中没数据则新增
            if (CollectionUtils.isEmpty(orgFactoryInfoEntities)) {
                orgFactoryInfoService.insertByFactoryInfoApproval(orgFactoryInfoApproval);
            } else {
                //表中有数据则更新
                orgFactoryInfoService.updateByFactoryInfoApproval(orgFactoryInfoApproval);
            }
        }
        if (checkStatus.equals(ApprovalTypeEnum.FAIL.getKey())) {
            //审核不通过(修改待审批厂商信息表审批状态)
            orgFactoryInfoApproval.setCheckStatus(ApprovalTypeEnum.FAIL.getKey());
            orgFactoryInfoApproval.setEditTime(new Date());
            orgFactoryInfoApproval.setEditId(user.getUserId());
            this.baseMapper.updateById(orgFactoryInfoApproval);
        }
        return map;
    }

    @Override
    public Map importData(String[][] rows, SysUserEntity sysUserEntity) {
        ArrayList<OrgFactoryInfoApprovalEntity> orgFactoryList = new ArrayList(rows.length);
        StringBuilder sb = new StringBuilder();
        SysDictEntity sysDictEntity = new SysDictEntity();
        //保存返回信息
        String key = "successCount";
        Map<String, Object> map = new HashMap<>(16);
        Map<String, Object> resultMap = new HashMap<>(16);
        Map<String, Object> cityMap = new HashMap<>(4);
        cityMap.put("page", "1");
        cityMap.put("limit", "10");
        if (!TypeEnum.USER_PLATFORM.getKey().equals(sysUserEntity.getUserType())) {
            map.put("errmessage", "非平台用户无法操作该功能！");
            return map;
        }
        if (StringUtil.isEmpty(sysUserEntity) || rows.length <= 1) {
            map.put(key, "0");
            map.put("failCount", String.valueOf(rows.length - 1));
            sb.append("系统错误");
            map.put("errorMessage", sb.toString());
            return map;
        }
        Integer failCount = 0;
        Integer successCount = 0;
        for (int i = 1; i < rows.length; i++) {
            OrgFactoryInfoApprovalEntity orgFactoryInfoEntity = new OrgFactoryInfoApprovalEntity();

            String[] row = rows[i];
            if (StringUtil.isEmpty(row[0]) || StringUtil.isEmpty(row[1])) {
                sb.append("\n");
                sb.append("第 " + i + " 行数据错误，请检查数据是否正确");
                failCount++;
                continue;
            }
            List<OrgFactoryInfoApprovalEntity> factoryName = baseMapper.selectByFactoryName(row[0]);
            if (!StringUtil.isEmpty(factoryName)) {
                sb.append("\n");
                sb.append("第 " + i + " 行数据错误，厂商名称已存在！ 厂商名称:" + row[0]);
                failCount++;
                continue;
            }
            List<OrgFactoryInfoApprovalEntity> creditCode = baseMapper.selectByCreditCode(row[1]);
            if (!StringUtil.isEmpty(creditCode)) {
                sb.append("\n");
                sb.append("第 " + i + " 行数据错误，厂商信用代码已存在！厂商信用代码： " + row[1]);
                failCount++;
                continue;
            }
            String str = row[2];
            //分割地区字符串
            String[] strs = str.split("/");
            if (strs.length != 3) {
                sb.append("\n");
                sb.append("第 " + i + " 行数据错误，输入厂商区域格式错误 区域: " + row[2]);
                failCount++;
                continue;
            }
            if (!AddressUtil.getProvinceValueMap().containsKey(strs[0])) {
                sb.append("\n");
                sb.append("第 " + i + " 行数据错误，省份或者直辖市不存在! 区域: " + row[2]);
                failCount++;
                continue;
            }
            orgFactoryInfoEntity.setProvinceCode(AddressUtil.getProvinceValueMap().get(strs[0]));
            sysDictEntity = sysDictService.selectDictByCodeAndParentCode(strs[1],AddressUtil.getProvinceValueMap().get(strs[0]));
            if (StringUtil.isEmpty(sysDictEntity)){
                sb.append("\n");
                sb.append("第 " + i + " 行数据错误，城市不存在!区域: " + row[2]);
                failCount++;
                continue;
            }
            orgFactoryInfoEntity.setCityCode(sysDictEntity.getCode());
            sysDictEntity = sysDictService.selectDictByCodeAndParentCode(strs[2], sysDictEntity.getCode());
            if (StringUtil.isEmpty(sysDictEntity)) {
                sb.append("\n");
                sb.append("第 " + i + " 行数据错误，区不存在! 区域: " + row[2]);
                failCount++;
                continue;
            }
            orgFactoryInfoEntity.setAreaCode(sysDictEntity.getCode());
            orgFactoryInfoEntity.setStatus(StatusEnum.USABLE.getKey());
            orgFactoryInfoEntity.setFactoryName(row[0]);
            orgFactoryInfoEntity.setCreditCode(row[1]);
            orgFactoryInfoEntity.setCreateId(sysUserEntity.getUserId());
            orgFactoryList.add(orgFactoryInfoEntity);
            orgFactoryInfoEntity = null;
            successCount++;
        }
        if (failCount > 0) {
            successCount = 0;
            map.put("errorMessage", sb.toString());
            map.put(key, successCount.toString());
            map.put("failCount", failCount.toString());
            return map;
        }

        orgFactoryList.stream().forEach(OrgFactoryInfo -> {
            save(OrgFactoryInfo, sysUserEntity);
        });
        map.put("errorMessage", sb.toString());
        map.put(key, successCount.toString());
        map.put("failCount", failCount.toString());
        return map;
    }

}
