package com.ebig.hdi.modules.license.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.job.entity.ScheduleJobEntity;
import com.ebig.hdi.modules.license.entity.LicenseFactoryInfoEntity;
import com.ebig.hdi.modules.license.entity.LicenseGoodsInfoEntity;
import com.ebig.hdi.modules.license.entity.vo.LicenseGoodsInfoVO;

/**
 * 商品证照信息
 * 
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-05-21 17:26:30
 */
public interface LicenseGoodsInfoDao extends BaseMapper<LicenseGoodsInfoEntity> {
	
	List<LicenseGoodsInfoVO> listForPage(Pagination page,LicenseGoodsInfoVO lgiVO);
	
	LicenseGoodsInfoVO selectLicenseById(@Param("id") Long id);
	
	LicenseGoodsInfoVO selectByNewLicenseId(@Param("id") Long id);
	
	List<Map<String,Object>> allGoods(@Param("supplierId") Long supplierId);
	
	List<LicenseGoodsInfoEntity> selectBySupplierIdAndTime(@Param("supplierId")Long supplierId,@Param("scheduleJob")ScheduleJobEntity scheduleJob);
}
