package com.ebig.hdi.modules.org.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.constant.ActivitiConstant;
import com.ebig.hdi.common.entity.OrgHospitalInfoApprovalEntity;
import com.ebig.hdi.common.enums.*;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.Constant;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.Query;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.activiti.entity.ActApprovalEntity;
import com.ebig.hdi.modules.activiti.service.ActApprovalService;
import com.ebig.hdi.modules.common.AddressUtil;
import com.ebig.hdi.modules.org.dao.OrgHospitalInfoApprovalDao;
import com.ebig.hdi.modules.org.dao.OrgHospitalInfoDao;
import com.ebig.hdi.modules.org.entity.OrgHospitalInfoEntity;
import com.ebig.hdi.modules.org.param.OrgHospitalParam;
import com.ebig.hdi.modules.org.service.OrgHospitalInfoApprovalService;
import com.ebig.hdi.modules.org.service.OrgHospitalInfoService;
import com.ebig.hdi.modules.sys.entity.SysDeptEntity;
import com.ebig.hdi.modules.sys.entity.SysDictEntity;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import com.ebig.hdi.modules.sys.service.SysDeptService;
import com.ebig.hdi.modules.sys.service.SysDictService;
import com.ebig.hdi.modules.sys.service.SysSequenceService;
import com.ebig.hdi.modules.sys.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author
 */
@Service("orgHospitalInfoApprovalService")
@Slf4j
public class OrgHospitalInfoApprovalServiceImpl extends ServiceImpl<OrgHospitalInfoApprovalDao, OrgHospitalInfoApprovalEntity> implements OrgHospitalInfoApprovalService {


    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysSequenceService sysSequenceService;

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private ActApprovalService actApprovalService;

    @Autowired
    private OrgHospitalInfoDao orgHospitalInfoDao;

    @Autowired
    private OrgHospitalInfoApprovalService orgHospitalInfoApprovalService;

    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private OrgHospitalInfoService orgHospitalInfoService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String hospitalName = (String) params.get("hospitalName");
        String hospitalGrade = params.get("hospitalGrade") != null ? params.get("hospitalGrade").toString() : null;
        String provinceCode = (String) params.get("provinceCode");
        String cityCode = (String) params.get("cityCode");
        String areaCode = (String) params.get("areaCode");
        Integer status = (Integer) params.get("status");
        Integer checkStatus = (Integer) params.get("checkStatus");

        Page<OrgHospitalInfoApprovalEntity> page = this.selectPage(
                new Query<OrgHospitalInfoApprovalEntity>(params).getPage(),
                new EntityWrapper<OrgHospitalInfoApprovalEntity>()
                        .eq("del_flag", DelFlagEnum.NORMAL.getKey())
                        .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER))
                        .like(org.apache.commons.lang.StringUtils.isNotBlank(hospitalName), "hospital_name", hospitalName)
                        .eq(org.apache.commons.lang.StringUtils.isNotBlank(hospitalGrade), "hospital_grade", hospitalGrade)
                        .eq(org.apache.commons.lang.StringUtils.isNotBlank(provinceCode), "province_code", provinceCode)
                        .eq(org.apache.commons.lang.StringUtils.isNotBlank(cityCode), "city_code", cityCode)
                        .eq(org.apache.commons.lang.StringUtils.isNotBlank(areaCode), "area_code", areaCode)
                        .eq(!StringUtil.isEmpty(status), "status", status)
                        .eq(!StringUtil.isEmpty(checkStatus), "check_status", checkStatus)
                        .orderBy("edit_time", false)
                        .orderBy("create_time", false)
        );
        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> save(OrgHospitalInfoApprovalEntity hospitalInfoApprovalEntity, SysUserEntity entity) {
        SysUserEntity sysUserEntity = sysUserService.selectById(hospitalInfoApprovalEntity.getCreateId());
        Map<String, Object> map = new HashMap<>(16);
        if (StringUtil.isEmpty(sysUserEntity)) {
            map.put("errmessage", "用户不存在！");
            log.error("用户不存在");
            return map;
        }
        if (!(TypeEnum.HOSPITAL_SUPPLIER.getKey().equals(entity.getUserType()) || TypeEnum.USER_PLATFORM.getKey().equals(entity.getUserType()))) {
            map.put("errmessage", "非医院用户或平台用户无法操作该功能！");
            log.error("当前用户类型{}", sysUserEntity.getUserType());
            return map;
        }
        List<OrgHospitalInfoApprovalEntity> hospitalName = baseMapper.selectByHospitalName(hospitalInfoApprovalEntity.getHospitalName());
        if (!StringUtil.isEmpty(hospitalName)) {
            map.put("errmessage", "医院名称已存在！");
            log.error("当前医院{}已存在", hospitalName);
            return map;
        }
        List<OrgHospitalInfoApprovalEntity> creditCode = baseMapper.selectByCreditCode(hospitalInfoApprovalEntity.getCreditCode());
        if (!StringUtil.isEmpty(creditCode)) {
            map.put("errmessage", "医院信用代码已存在！");
            log.error("医院信用代码{}已存在", creditCode);
            return map;
        }
        //获取医院编码序列号
        String hospitalCode = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.HOSPITAL_CODE.getKey());
        hospitalInfoApprovalEntity.setCreateTime(new Date());
        hospitalInfoApprovalEntity.setDelFlag(DelFlagEnum.NORMAL.getKey());
        hospitalInfoApprovalEntity.setHospitalCode(hospitalCode);
        hospitalInfoApprovalEntity.setDataSource(DataSourceEnum.MANUAL.getKey());
        hospitalInfoApprovalEntity.setCheckStatus(ApprovalTypeEnum.WAIT.getKey());
        hospitalInfoApprovalEntity.setParentId(0L);
        hospitalInfoApprovalEntity.setIsGroup(IsGroupEnum.NO.getKey());
        this.insert(hospitalInfoApprovalEntity);
        //发起审批
        ActApprovalEntity approvalEntity = actApprovalService.getActApprovalEntity(entity, ChangeTypeEnum.ADD.getKey(),
                ActTypeEnum.HOSPITAL.getKey(), hospitalInfoApprovalEntity.getId().toString(), hospitalCode,
                hospitalInfoApprovalEntity.getHospitalName());
        Map<String, Object> approvalMap = new HashMap<>(16);
        approvalMap.put("approvalEntity", approvalEntity);
        actApprovalService.insert(approvalEntity);
        ProcessInstance instance = actApprovalService.startProcess(entity.getUserId().toString(), ActivitiConstant.ACTIVITI_ORGHOSPITAL[0], ActivitiConstant.ACTIVITI_ORGHOSPITAL[1], approvalMap);
        if (instance != null) {
            approvalEntity.setProcessId(instance.getProcessInstanceId());
            approvalEntity.setApprovalCode(instance.getProcessInstanceId());
            hospitalInfoApprovalEntity.setProcessId(instance.getProcessInstanceId());
        }
        this.updateById(hospitalInfoApprovalEntity);
        actApprovalService.updateById(approvalEntity);
        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(OrgHospitalInfoApprovalEntity hospitalInfoApprovalEntity, SysUserEntity entity) {
        List<OrgHospitalInfoApprovalEntity> hospitalName = this.baseMapper.selectByHospitalName(hospitalInfoApprovalEntity.getHospitalName());
        // 如果修改的目标医院名已存在(此时必然有且仅有一个)且与当前修改记录不是同一条记录
        if (!StringUtil.isEmpty(hospitalName) && !hospitalName.get(0).getId().equals(hospitalInfoApprovalEntity.getId())) {
            throw new HdiException(MessageFormat.format("医院名称{0}已存在", hospitalInfoApprovalEntity.getHospitalName()));
        }
        // 统一社会信用代码同上
        List<OrgHospitalInfoApprovalEntity> creditCode = this.baseMapper.selectByCreditCode(hospitalInfoApprovalEntity.getCreditCode());
        if (!StringUtil.isEmpty(creditCode) && !creditCode.get(0).getId().equals(hospitalInfoApprovalEntity.getId())) {
            throw new HdiException(MessageFormat.format("医院统一社会信用代码{0}已存在", hospitalInfoApprovalEntity.getCreditCode()));
        }
        hospitalInfoApprovalEntity.setEditTime(new Date());
        hospitalInfoApprovalEntity.setEditId(entity.getUserId());
        hospitalInfoApprovalEntity.setCheckStatus(ApprovalTypeEnum.WAIT.getKey());
        //发起审批
        ActApprovalEntity approvalEntity = actApprovalService.getActApprovalEntity(entity, ChangeTypeEnum.UPDATE.getKey(),
                ActTypeEnum.HOSPITAL.getKey(), hospitalInfoApprovalEntity.getId().toString(), hospitalInfoApprovalEntity.getHospitalCode(),
                hospitalInfoApprovalEntity.getHospitalName());
        Map<String, Object> approvalMap = new HashMap<>(16);
        approvalMap.put("approvalEntity", approvalEntity);
        actApprovalService.insert(approvalEntity);
        ProcessInstance instance = actApprovalService.startProcess(entity.getUserId().toString(), ActivitiConstant.ACTIVITI_ORGHOSPITAL[0], ActivitiConstant.ACTIVITI_ORGHOSPITAL[1], approvalMap);
        if (instance != null) {
            approvalEntity.setProcessId(instance.getProcessInstanceId());
            approvalEntity.setApprovalCode(instance.getProcessInstanceId());
            hospitalInfoApprovalEntity.setProcessId(instance.getProcessInstanceId());
        }
        actApprovalService.updateById(approvalEntity);
        this.updateById(hospitalInfoApprovalEntity);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        for (Long id : ids) {
            OrgHospitalInfoApprovalEntity orgHospitalInfo = new OrgHospitalInfoApprovalEntity();
            orgHospitalInfo.setId(id);
            orgHospitalInfo.setDelFlag(DelFlagEnum.DELETE.getKey());
            this.updateById(orgHospitalInfo);
        }
    }
    @Override
    public Map importData(String[][] rows, SysUserEntity sysUserEntity) {

        ArrayList<OrgHospitalInfoApprovalEntity> rgHospitalList = new ArrayList(rows.length);

        StringBuilder sb = new StringBuilder();
        SysDictEntity sysDictEntity = new SysDictEntity();
        Map<String, Object> saveMap;
        List<?> codeList;
        List<SysDictEntity> gradeList = sysDictService.selectDictByType("hospital_grade");
        HashMap<String, String> gradeMap = gradeList.stream().collect(Collectors.toMap(SysDictEntity::getValue, SysDictEntity::getCode, (key1, key2) -> key2, HashMap::new));
        Map<String, Object> dictMap = new HashMap<>(4);
        dictMap.put("page", "1");
        dictMap.put("limit", "10");
        //保存返回信息
        String key = "successCount";
        Map<String, Object> map = new HashMap<>(16);
        PageUtils page;
        if (sysUserEntity == null || rows.length <= 1) {
            map.put(key, "0");
            map.put("failCount", String.valueOf(rows.length - 1));
            sb.append("系统错误");
            map.put("errorMessage", sb.toString());
            return map;
        }
        Integer failCount = 0;
        Integer successCount = 0;
        for (int i = 1; i < rows.length; i++) {
            OrgHospitalInfoApprovalEntity orgHospitalInfoEntity = new OrgHospitalInfoApprovalEntity();
            String[] row = rows[i];
            if (StringUtil.isEmpty(row[0]) || StringUtil.isEmpty(row[1]) || StringUtil.isEmpty(row[2])
                    || StringUtil.isEmpty(row[3])) {
                sb.append("\n");
                sb.append("第 " + i  + " 行数据错误，请检查数据是否正确");
                failCount++;
                continue;
            }
            List<OrgHospitalInfoApprovalEntity> hospitalName = baseMapper.selectByHospitalName(row[0]);
            if (!StringUtil.isEmpty(hospitalName)) {
                sb.append("\n");
                sb.append("第 " + i  + "医院名称已存在！医院名称："+row[0]);
                failCount++;
                continue;
            }
            List<OrgHospitalInfoApprovalEntity> creditCode = baseMapper.selectByCreditCode(row[1]);
            if (!StringUtil.isEmpty(creditCode)) {
                sb.append("\n");
                sb.append("第 " + i  + "医院信用代码已存在！ 医院信用代码："+row[1]);
                failCount++;
                continue;
            }
            String str = row[3];
            String[] strs = str.split("/");
            if (strs.length != 3) {
                sb.append("\n");
                sb.append("第 " + i + " 行数据错误，输入区域不正确 区域："+row[3]);
                failCount++;
                continue;
            }
            if (!gradeMap.containsKey(row[2])) {
                sb.append("\n");
                sb.append("第 " + i  + " 行数据错误，输入医疗机构级别不存在 医疗机构级别："+row[2]);
                failCount++;
                continue;
            }
            orgHospitalInfoEntity.setHospitalGrade(Integer.valueOf(gradeMap.get(row[2])));
            if (!AddressUtil.getProvinceValueMap().containsKey(strs[0])) {
                sb.append("\n");
                sb.append("第 " + i + " 行数据错误，输入省份或者直辖市不存在 区域："+row[3]);
                failCount++;
                continue;
            }
            orgHospitalInfoEntity.setProvinceCode(AddressUtil.getProvinceValueMap().get(strs[0]));
            sysDictEntity = sysDictService.selectDictByCodeAndParentCode(strs[1], AddressUtil.getProvinceValueMap().get(strs[0]));

            if (StringUtil.isEmpty(sysDictEntity)) {
                sb.append("第 " + i + " 行数据错误，输入城市不存在 区域："+row[3]);
                sb.append(" \n ");
                failCount++;
                continue;
            }
            orgHospitalInfoEntity.setCityCode(sysDictEntity.getCode());
            sysDictEntity = sysDictService.selectDictByCodeAndParentCode(strs[2], sysDictEntity.getCode());
            if (StringUtil.isEmpty(sysDictEntity)) {
                sb.append("\n");
                sb.append("第 " + i + " 行数据错误，区不存在! 区域: " + row[2]);
                failCount++;
                continue;
            }
            orgHospitalInfoEntity.setAreaCode(sysDictEntity.getCode());
            sysDictEntity = null;
            orgHospitalInfoEntity.setStatus(StatusEnum.USABLE.getKey());
            orgHospitalInfoEntity.setCreateId(sysUserEntity.getUserId());
            orgHospitalInfoEntity.setHospitalName(row[0]);
            orgHospitalInfoEntity.setCreditCode(row[1]);
            rgHospitalList.add(orgHospitalInfoEntity);
            successCount++;
        }
        if(failCount >0){
            successCount = 0;
            map.put("errorMessage",sb.toString());
            map.put(key, successCount.toString());
            map.put("failCount",failCount.toString());
            return map;
        }
        rgHospitalList.stream().forEach(rgHospital -> {
            save(rgHospital,sysUserEntity);
        });
        map.put("errorMessage",sb.toString());
        map.put(key, successCount.toString());
        map.put("failCount",failCount.toString());
        return map;
    }


    @Override
    public List<Map<String, Object>> getList(String[] columns, OrgHospitalParam orgHospitalParam) {
        List<SysDictEntity> hospitalGradeList = sysDictService.selectDictByType("hospital_grade");
        List<Map<String, Object>> list = this.baseMapper.getList(columns, orgHospitalParam);
        HashMap<String, String> hospitalGradeMap = hospitalGradeList.stream().collect(Collectors.toMap(SysDictEntity::getCode, SysDictEntity::getValue, (key1, key2) -> key2, HashMap::new));
        int size = list.size();
        String hospitalGrade, checkStatus;
        StringBuffer address = new StringBuffer();
        for (int i = 0; i < size; i++) {
            checkStatus = ApprovalTypeEnum.getName(Integer.valueOf(list.get(i).get("checkStatus").toString()));
            hospitalGrade = hospitalGradeMap.get(list.get(i).get("hospitalGrade").toString());
            address.append(AddressUtil.getProvinceMap().get(list.get(i).get("provinceCode"))).append("/")
                    .append(AddressUtil.getCityMap().get(list.get(i).get("cityCode"))).append("/")
                    .append(AddressUtil.getAreaMap().get(list.get(i).get("areaCode")));
            list.get(i).put("address", address.toString());
            list.get(i).put("hospitalGrade", hospitalGrade);
            list.get(i).put("checkStatus", checkStatus);
            address.setLength(0);
        }
        return list;
    }

    @Override
    public void toggle(Map<String, Object> params) {
        List<Long> ids = JSON.parseArray(params.get("ids").toString(), Long.class);
        for (Long id : ids) {
            OrgHospitalInfoApprovalEntity orgHospitalInfoApprovalEntity = new OrgHospitalInfoApprovalEntity();
            orgHospitalInfoApprovalEntity.setId(id);
            orgHospitalInfoApprovalEntity.setStatus(Integer.valueOf(params.get("status").toString()));
            //查看原表是否存在数据
            Integer checkStatus = this.baseMapper.selectIfExist(orgHospitalInfoApprovalEntity.getId());
            if (checkStatus.equals(1)) {
                OrgHospitalInfoEntity orgHospitalInfoEntity = new OrgHospitalInfoEntity();
                orgHospitalInfoEntity.setId(id);
                orgHospitalInfoEntity.setStatus(Integer.valueOf(params.get("status").toString()));
                orgHospitalInfoService.updateById(orgHospitalInfoEntity);
            }
            this.baseMapper.updateById(orgHospitalInfoApprovalEntity);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> checkStatus(Map<String, Object> params, SysUserEntity user) {
        Integer checkStatus = Integer.parseInt(params.get("checkStatus").toString());
        Map<String, Object> map = new HashMap<>(16);
        if (StringUtil.isEmpty(checkStatus)) {
            map.put("errmessage", "状态为空");
            return map;
        }
        long orgId = Long.parseLong(params.get("orgId").toString());
        if (StringUtil.isEmpty(orgId)) {
            map.put("errmessage", "传入ID为空");
            return map;
        }
        OrgHospitalInfoApprovalEntity orgHospitalInfoApprovalEntity = orgHospitalInfoApprovalService.selectById(orgId);
        orgHospitalInfoApprovalEntity.setEditId(user.getUserId());
        orgHospitalInfoApprovalEntity.setEditTime(new Date());
        //判断审批状态
        if (checkStatus.equals(ApprovalTypeEnum.PASS.getKey())) {
            orgHospitalInfoApprovalEntity.setCheckStatus(ApprovalTypeEnum.PASS.getKey());
            this.updateById(orgHospitalInfoApprovalEntity);
            //查看原表有没有当前数据 如果有就更新 没有就新增
            OrgHospitalInfoEntity orgHospitalInfo = orgHospitalInfoDao.selectById(orgId);
            if (StringUtil.isEmpty(orgHospitalInfo)) {
                //创建系统机构信息
                SysDeptEntity sysDept = new SysDeptEntity();
                sysDept.setParentId(0L);
                sysDept.setName(orgHospitalInfoApprovalEntity.getHospitalName());
                sysDept.setDelFlag(DelFlagEnum.NORMAL.getKey());
                sysDeptService.insert(sysDept);
                orgHospitalInfoApprovalEntity.setDeptId(sysDept.getDeptId());
                this.baseMapper.updateById(orgHospitalInfoApprovalEntity);
                orgHospitalInfoDao.insertApproval(orgHospitalInfoApprovalEntity);
            } else {
                orgHospitalInfoDao.updateApproval(orgHospitalInfoApprovalEntity);
                //更新系统机构信息
                SysDeptEntity sysDept = sysDeptService.selectById(orgHospitalInfo.getDeptId());
                sysDept.setName(orgHospitalInfo.getHospitalName());
                sysDeptService.updateById(sysDept);
            }
        }
        //审批不通过
        if (checkStatus.equals(ApprovalTypeEnum.FAIL.getKey())) {
            orgHospitalInfoApprovalEntity.setCheckStatus(ApprovalTypeEnum.FAIL.getKey());
            this.updateById(orgHospitalInfoApprovalEntity);
        }
        return map;

    }

}
