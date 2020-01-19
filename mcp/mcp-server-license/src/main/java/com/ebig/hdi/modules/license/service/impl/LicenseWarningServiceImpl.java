package com.ebig.hdi.modules.license.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.license.dao.LicenseWarningDao;
import com.ebig.hdi.modules.license.entity.LicenseWarningEntity;
import com.ebig.hdi.modules.license.service.LicenseWarningService;


@Service("licenseWarningService")
public class LicenseWarningServiceImpl implements LicenseWarningService {

	@Autowired
	private LicenseWarningDao licenseWarningDao;
	
    @Override
    @DataFilter(subDept = true, user = false)
    public PageUtils queryPage(Map<String, Object> params) {
		int currPage = Integer.parseInt(params.get("page").toString());
		int pageSize = Integer.parseInt(params.get("limit").toString());
		
		Page<LicenseWarningEntity> page = new Page<LicenseWarningEntity>(currPage, pageSize);
		
		List<LicenseWarningEntity> list = licenseWarningDao.selectLicenseWarningList(page, params);
				
		page.setRecords(list);

        return new PageUtils(page);
    }

}
