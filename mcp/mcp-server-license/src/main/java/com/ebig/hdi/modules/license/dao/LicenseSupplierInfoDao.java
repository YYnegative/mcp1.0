package com.ebig.hdi.modules.license.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.job.entity.ScheduleJobEntity;
import com.ebig.hdi.modules.license.entity.LicenseSupplierInfoEntity;
import com.ebig.hdi.modules.license.entity.vo.LicenseAgentInfoVO;
import com.ebig.hdi.modules.license.entity.vo.LicenseGoodsInfoVO;
import com.ebig.hdi.modules.license.vo.LicenseFactoryInfoEntityVo;
import com.ebig.hdi.modules.license.vo.LicenseSupplierInfoEntityVo;

/**
 * 供应商证照信息
 * 
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-05-21 17:26:30
 */
public interface LicenseSupplierInfoDao extends BaseMapper<LicenseSupplierInfoEntity> {

	List<LicenseSupplierInfoEntityVo> selectLicenseSupplierInfoList(Pagination page, Map<String, Object> params);

	LicenseSupplierInfoEntityVo selectLicenseSupplierInfoById(Long id);

	LicenseSupplierInfoEntityVo selectLicenseSupplierInfoByNewLicenseId(Long newLicenseId);
	
	List<LicenseAgentInfoVO> selectAgentView(@Param("supplierId")String supplierId,@Param("agentId")String agentId);
	
	List<LicenseFactoryInfoEntityVo> selectFactoryView(@Param("supplierId")String supplierId,@Param("factoryId")String factoryId);
	
	List<LicenseGoodsInfoVO> selectGoodsView(@Param("supplierId")String supplierId,@Param("goodsId")String goodsId);
	
	List<LicenseSupplierInfoEntityVo> selectSupplierView(@Param("supplierId")String supplierId);
	
	List<LicenseSupplierInfoEntity> selectBySupplierIdAndTime(@Param("supplierId")Long supplierId,@Param("scheduleJob")ScheduleJobEntity scheduleJob);
}
