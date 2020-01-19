package com.ebig.hdi.modules.org.dao;

import com.ebig.hdi.modules.org.entity.OrgHospitalInfoEntity;
import com.ebig.hdi.modules.org.entity.OrgSupplierHospitalRefEntity;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 供应商医院绑定关系
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-04-23 09:27:39
 */
public interface OrgSupplierHospitalRefDao extends BaseMapper<OrgSupplierHospitalRefEntity> {

	List<OrgSupplierHospitalRefEntity> selectBySupplierIdAndHospitalId(@Param("supplierId") Long supplierId, @Param("hospitalId") Long hospitalId);

	List<OrgSupplierHospitalRefEntity> selectBySupplierId(@Param("supplierId") Long supplierId);

	List<OrgSupplierHospitalRefEntity> selectByHospitalIds(@Param("hospitalIds") List<String> jjHospitalIds);
	
	List<OrgHospitalInfoEntity> queryAllHospital(@Param("id") Long id);
	
	List<OrgSupplierHospitalRefEntity> selectChanged(Map<String, Object> params);

	OrgSupplierHospitalRefEntity selectBySourcesId(@Param("sourcesId") String sourcesId);

	List<OrgHospitalInfoEntity> queryMatchHospital(@Param("id") Long id);
	
}
