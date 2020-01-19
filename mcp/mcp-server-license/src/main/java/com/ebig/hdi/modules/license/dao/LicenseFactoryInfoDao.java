package com.ebig.hdi.modules.license.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.job.entity.ScheduleJobEntity;
import com.ebig.hdi.modules.license.entity.LicenseFactoryInfoEntity;
import com.ebig.hdi.modules.license.entity.LicenseSupplierInfoEntity;
import com.ebig.hdi.modules.license.vo.LicenseFactoryInfoEntityVo;

/**
 * 厂商证照信息
 * 
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-05-21 17:26:29
 */
public interface LicenseFactoryInfoDao extends BaseMapper<LicenseFactoryInfoEntity> {

	void saveFactory(Map<String, Object> factory);

	LicenseFactoryInfoEntity selectLicenseFactoryInfoById(Long id);

	LicenseFactoryInfoEntity selectLicenseFactoryInfoByNewLicenseId(Long newLicenseId);

	List<LicenseFactoryInfoEntityVo> selectLicenseFactoryInfoList(Pagination page, Map<String, Object> params);
	
	List<LicenseFactoryInfoEntity> selectBySupplierIdAndTime(@Param("supplierId")Long supplierId,@Param("scheduleJob")ScheduleJobEntity scheduleJob);
}
