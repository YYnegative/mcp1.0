package com.ebig.hdi.modules.license.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.license.entity.LicenseWarningEntity;

/**
 * VIEW
 * 
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-05-28 09:22:55
 */
public interface LicenseWarningDao extends BaseMapper<LicenseWarningEntity> {

	List<LicenseWarningEntity> selectLicenseWarningList(Pagination page, Map<String, Object> params);
	
}
