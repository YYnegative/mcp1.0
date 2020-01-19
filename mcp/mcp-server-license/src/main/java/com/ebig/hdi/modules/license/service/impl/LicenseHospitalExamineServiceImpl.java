package com.ebig.hdi.modules.license.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.enums.ApprovalTypeEnum;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.license.dao.LicenseHospitalExamineDao;
import com.ebig.hdi.modules.license.entity.LicenseHospitalExamineEntity;
import com.ebig.hdi.modules.license.service.LicenseHospitalExamineService;


@Service("licenseHospitalExamineService")
public class LicenseHospitalExamineServiceImpl extends ServiceImpl<LicenseHospitalExamineDao, LicenseHospitalExamineEntity> implements LicenseHospitalExamineService {

    @Override
    @DataFilter(subDept = true, user = false)
    public PageUtils queryPage(Map<String, Object> params) {
		int currPage = Integer.parseInt(params.get("page").toString());
		int pageSize = Integer.parseInt(params.get("limit").toString());
		
		Page<Map<String, Object>> page = new Page<Map<String, Object>>(currPage, pageSize);
		
        List<Map<String, Object>> list = this.baseMapper.selectLicenseHospitalExamineList(page, params);
        
        page.setRecords(list);

        return new PageUtils(page);
    }

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(LicenseHospitalExamineEntity licenseHospitalExamine) {
		//保存医院历史信息
		Map<String, Object> hospitalInfo = this.baseMapper.selectHospitalInfoByHospitalId(licenseHospitalExamine.getHospitalId());
		licenseHospitalExamine.setHospitalName((String)hospitalInfo.get("hospital_name"));
		
		//设置为待审批状态
		licenseHospitalExamine.setStatus(ApprovalTypeEnum.WAIT.getKey());
		licenseHospitalExamine.setCreateTime(new Date());
		this.baseMapper.insert(licenseHospitalExamine);
		
	}

}
