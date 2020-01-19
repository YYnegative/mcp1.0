package com.ebig.hdi.modules.license.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.job.entity.ScheduleJobEntity;
import com.ebig.hdi.modules.license.entity.LicenseFactoryInfoEntity;
import com.ebig.hdi.modules.license.vo.LicenseFactoryInfoEntityVo;

/**
 * 厂商证照信息
 *
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-05-21 17:26:29
 */
public interface LicenseFactoryInfoService extends IService<LicenseFactoryInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

	void save(LicenseFactoryInfoEntityVo licenseFactoryInfo);

	void update(LicenseFactoryInfoEntityVo licenseFactoryInfo);

	void delete(Long[] ids);

	LicenseFactoryInfoEntity selectLicenseFactoryInfoById(Long id);

	List<LicenseFactoryInfoEntity> selectRelativesById(Long id);

	void replace(LicenseFactoryInfoEntityVo licenseFactoryInfo);
	
	List<LicenseFactoryInfoEntity> selectBySupplierIdAndTime(Long supplierId, ScheduleJobEntity scheduleJob);
}

