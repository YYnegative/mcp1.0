package com.ebig.hdi.modules.license.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.license.entity.LicenseHospitalExamineEntity;

/**
 * 证照医院审批
 * 
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-05-21 17:26:30
 */
public interface LicenseHospitalExamineDao extends BaseMapper<LicenseHospitalExamineEntity> {

	List<Map<String, Object>> selectLicenseHospitalExamineList(Pagination page, Map<String, Object> params);
	
	Map<String, Object> selectHospitalInfoByHospitalId(@Param("hospitalId")Long hospitalId);
}
