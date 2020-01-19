package com.ebig.hdi.modules.org.service.impl;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import com.ebig.hdi.modules.org.param.OrgHospitalParam;

import com.ebig.hdi.modules.sys.entity.SysDictEntity;
import com.ebig.hdi.modules.sys.service.SysDictService;
import com.ebig.hdi.modules.sys.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.enums.DelFlagEnum;
import com.ebig.hdi.common.enums.IsGroupEnum;
import com.ebig.hdi.common.enums.SequenceEnum;
import com.ebig.hdi.common.enums.StatusEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.Constant;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.Query;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.org.dao.OrgHospitalInfoDao;
import com.ebig.hdi.modules.org.entity.OrgHospitalInfoEntity;
import com.ebig.hdi.modules.org.service.OrgHospitalInfoService;
import com.ebig.hdi.modules.sys.entity.SysDeptEntity;
import com.ebig.hdi.modules.sys.service.SysDeptService;
import com.ebig.hdi.modules.sys.service.SysSequenceService;


@Service("orgHospitalInfoService")
public class OrgHospitalInfoServiceImpl extends ServiceImpl<OrgHospitalInfoDao, OrgHospitalInfoEntity> implements OrgHospitalInfoService {

    @Autowired
    private SysSequenceService sysSequenceService;

    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysDictService sysDictService;

    @Override
    @DataFilter(subDept = true, user = false)
    public PageUtils queryPage(Map<String, Object> params) {
        String hospitalName = (String) params.get("hospitalName");
        String hospitalGrade = params.get("hospitalGrade") != null ? params.get("hospitalGrade").toString() : null;
        String provinceCode = (String) params.get("provinceCode");
        String cityCode = (String) params.get("cityCode");
        String areaCode = (String) params.get("areaCode");
        Integer status = (Integer) params.get("status");

        Page<OrgHospitalInfoEntity> page = this.selectPage(
                new Query<OrgHospitalInfoEntity>(params).getPage(),
                new EntityWrapper<OrgHospitalInfoEntity>()
                        .eq("del_flag", DelFlagEnum.NORMAL.getKey())
                        .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER))
                        .like(StringUtils.isNotBlank(hospitalName), "hospital_name", hospitalName)
                        .eq(StringUtils.isNotBlank(hospitalGrade), "hospital_grade", hospitalGrade)
                        .eq(StringUtils.isNotBlank(provinceCode), "province_code", provinceCode)
                        .eq(StringUtils.isNotBlank(cityCode), "city_code", cityCode)
                        .eq(StringUtils.isNotBlank(areaCode), "area_code", areaCode)
                        .eq(!StringUtil.isEmpty(status), "status", status)
                        .orderBy("edit_time", false)
                        .orderBy("create_time", false)
        );
        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertValidate(OrgHospitalInfoEntity orgHospitalInfo) {
        List<OrgHospitalInfoEntity> hospitalName = baseMapper.selectByHospitalName(orgHospitalInfo.getHospitalName());
        if (!StringUtil.isEmpty(hospitalName)) {
            throw new HdiException(MessageFormat.format("医院名称{0}已存在", orgHospitalInfo.getHospitalName()));
        }
        List<OrgHospitalInfoEntity> creditCode = baseMapper.selectByCreditCode(orgHospitalInfo.getCreditCode());
        if (!StringUtil.isEmpty(creditCode)) {
            throw new HdiException(MessageFormat.format("医院统一社会信用代码{0}已存在", orgHospitalInfo.getCreditCode()));
        }

        //创建系统机构信息
        SysDeptEntity sysDept = new SysDeptEntity();
        sysDept.setParentId(0L);
        sysDept.setName(orgHospitalInfo.getHospitalName());
        sysDept.setDelFlag(DelFlagEnum.NORMAL.getKey());
        sysDeptService.insert(sysDept);

        orgHospitalInfo.setDeptId(sysDept.getDeptId());
        String hospitalCode = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.HOSPITAL_CODE.getKey());
        orgHospitalInfo.setParentId(0L);
        orgHospitalInfo.setHospitalCode(hospitalCode);
        orgHospitalInfo.setIsGroup(IsGroupEnum.NO.getKey());
        orgHospitalInfo.setCreateTime(new Date());
        orgHospitalInfo.setDelFlag(DelFlagEnum.NORMAL.getKey());

        this.baseMapper.insert(orgHospitalInfo);
    }

    @Override
    public void updateValidate(OrgHospitalInfoEntity orgHospitalInfo) {
        List<OrgHospitalInfoEntity> hospitalName = baseMapper.selectByHospitalName(orgHospitalInfo.getHospitalName());
        // 如果修改的目标医院名已存在(此时必然有且仅有一个)且与当前修改记录不是同一条记录
        if (!StringUtil.isEmpty(hospitalName) && hospitalName.get(0).getId().equals(orgHospitalInfo.getId())) {
            throw new HdiException(MessageFormat.format("医院名称{0}已存在", orgHospitalInfo.getHospitalName()));
        }
        // 统一社会信用代码同上
        List<OrgHospitalInfoEntity> creditCode = baseMapper.selectByCreditCode(orgHospitalInfo.getCreditCode());
        if (!StringUtil.isEmpty(creditCode) && creditCode.get(0).getId().equals(orgHospitalInfo.getId())) {
            throw new HdiException(MessageFormat.format("医院统一社会信用代码{0}已存在", orgHospitalInfo.getCreditCode()));
        }

        orgHospitalInfo.setEditTime(new Date());

        this.baseMapper.updateById(orgHospitalInfo);

        orgHospitalInfo = this.baseMapper.selectById(orgHospitalInfo.getId());

        SysDeptEntity sysDept = sysDeptService.selectById(orgHospitalInfo.getDeptId());
        sysDept.setName(orgHospitalInfo.getHospitalName());
        sysDeptService.updateById(sysDept);
    }

    @Override
    @DataFilter(subDept = true, user = false)
    public List<OrgHospitalInfoEntity> queryByHospitalName(Map<String, Object> params) {
        String hospitalName = (String) params.get("hospitalName");
        return this.selectList(
                new EntityWrapper<OrgHospitalInfoEntity>()
                        .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER))
                        .eq("del_flag", DelFlagEnum.NORMAL.getKey())
                        .eq("status", StatusEnum.USABLE.getKey())
                        .like("hospital_name", hospitalName)
                        .orderBy("create_time", false)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        for (Long id : ids) {
            OrgHospitalInfoEntity orgHospitalInfoEntity = new OrgHospitalInfoEntity();
            orgHospitalInfoEntity.setId(id);
            //设置信息为删除状态
            orgHospitalInfoEntity.setDelFlag((DelFlagEnum.DELETE.getKey()));
            this.updateById(orgHospitalInfoEntity);
        }
    }

    @Override
    public Map importData(String[][] rows, Long userId, Long deptId) {
        OrgHospitalInfoEntity orgHospitalInfoEntity = new OrgHospitalInfoEntity();
        StringBuilder sb = new StringBuilder();
        SysDictEntity sysDictEntity = new SysDictEntity();
        List<?> codeList;
        Map<String, Object> dictMap = new HashMap<>(4);
        dictMap.put("page", "1");
        dictMap.put("limit", "1");
        //保存返回信息
        String key = "successCount";
        Map<String, Object> map = new HashMap<>(16);
        PageUtils page;
        if (userId == null || deptId == null || rows.length <= 1) {
            map.put(key, "0");
            map.put("failCount", String.valueOf(rows.length - 1));
            sb.append("系统错误");
            map.put("errorMessage", sb.toString());
            return map;
        }
        Integer successCount = 0;
        for (int i = 1; i < rows.length; i++) {
            String[] row = rows[i];
            String str = row[4];
            String[] strs = str.split("/");
            dictMap.put("value", row[2]);
            page = sysDictService.queryPage(dictMap);
            if (page.getList().size() == 0) {
                throw new HdiException("医疗机构级别不存在！");
            }
            codeList = page.getList();
            sysDictEntity = (SysDictEntity) codeList.get(0);
            orgHospitalInfoEntity.setHospitalGrade(Integer.valueOf(sysDictEntity.getCode()));
            dictMap.put("value", strs[0]);
            page = sysDictService.queryPage(dictMap);
            if (page.getList().size() == 0) {
                throw new HdiException("省份或者直辖市不存在！");
            }
            codeList = page.getList();
            sysDictEntity = (SysDictEntity) codeList.get(0);
            orgHospitalInfoEntity.setProvinceCode(sysDictEntity.getCode());
            dictMap.put("value", strs[1]);
            page = sysDictService.queryPage(dictMap);
            if (page.getList().size() == 0) {
                throw new HdiException("城市不存在！");
            }
            codeList = page.getList();
            sysDictEntity = (SysDictEntity) codeList.get(0);
            orgHospitalInfoEntity.setCityCode(sysDictEntity.getCode());
            dictMap.put("value", strs[2]);
            page = sysDictService.queryPage(dictMap);
            if (page.getList().size() == 0) {
                throw new HdiException("区不存在！");
            }
            codeList = page.getList();
            sysDictEntity = (SysDictEntity) codeList.get(0);
            orgHospitalInfoEntity.setAreaCode(sysDictEntity.getCode());
            orgHospitalInfoEntity.setStatus(StatusEnum.USABLE.getKey());
            orgHospitalInfoEntity.setCreateId(userId);
            orgHospitalInfoEntity.setDeptId(deptId);
            orgHospitalInfoEntity.setHospitalName(row[0]);
            orgHospitalInfoEntity.setCreditCode(row[1]);
            insertValidate(orgHospitalInfoEntity);
            successCount++;
        }
        map.put(key, successCount.toString());
        map.put("failCount", "0");
        return map;
    }

    @Override
    public List<Map<String, Object>> getList(String[] columns, OrgHospitalParam orgHospitalParam) {
        List<Map<String, Object>> list = this.baseMapper.getList(columns, orgHospitalParam);
        return list;
    }

    @Override
    public void toggle(Map<String, Object> params) {
        List<Long> ids = JSON.parseArray(params.get("ids").toString(), Long.class);
        for (Long id : ids) {
            OrgHospitalInfoEntity orgHospitalInfoEntity = new OrgHospitalInfoEntity();
            orgHospitalInfoEntity.setId(id);
            orgHospitalInfoEntity.setStatus(Integer.valueOf(params.get("status").toString()));
            this.baseMapper.updateById(orgHospitalInfoEntity);
        }
    }

    @Override
    public List<OrgHospitalInfoEntity> selectByHospitalName(String hospitalName) {
        return this.baseMapper.selectByHospitalName(hospitalName);
    }
}
