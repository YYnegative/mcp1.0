package com.ebig.hdi.modules.surgery.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.surgery.entity.SurgeryStageDetailInfoEntity;
import com.ebig.hdi.modules.surgery.entity.vo.SurgeryStageDetailInfoVO;

/**
 * 手术跟台目录明细表
 * 
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-06-11 10:15:32
 */
public interface SurgeryStageDetailInfoDao extends BaseMapper<SurgeryStageDetailInfoEntity> {
	
	List<SurgeryStageDetailInfoVO> selectToSave(@Param("params") Map<String,Object> params);
	
	List<SurgeryStageDetailInfoVO> selectBySurgeryStageId(@Param("id") Long id);
	
	List<SurgeryStageDetailInfoVO> listForPage(Pagination page,@Param("id")Long id);
	
}
