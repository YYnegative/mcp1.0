package com.ebig.hdi.modules.license.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.job.entity.ScheduleJobEntity;
import com.ebig.hdi.modules.license.entity.LicenseSupplierInfoEntity;
import com.ebig.hdi.modules.license.vo.LicenseSupplierInfoEntityVo;

/**
 * 供应商证照信息
 *
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-05-21 17:26:30
 */
public interface LicenseSupplierInfoService extends IService<LicenseSupplierInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    Map<String,Object> goodsDetails(Map<String, Object> params);

	void save(LicenseSupplierInfoEntity licenseSupplierInfo);

	void update(LicenseSupplierInfoEntity licenseSupplierInfo);

	void delete(Long[] ids);

	void examine(Map<String, Object> params);

	void replace(LicenseSupplierInfoEntity licenseSupplierInfo);

	List<LicenseSupplierInfoEntityVo> selectRelativesById(Long id);

	LicenseSupplierInfoEntityVo selectLicenseSupplierInfoById(Long id);

	PageUtils examineInfo(Map<String, Object> params);
	
	List<LicenseSupplierInfoEntity> selectBySupplierIdAndTime(Long supplierId, ScheduleJobEntity scheduleJob);

}

