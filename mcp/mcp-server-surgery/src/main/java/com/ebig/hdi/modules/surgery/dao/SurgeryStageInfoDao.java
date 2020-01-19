package com.ebig.hdi.modules.surgery.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.surgery.entity.SurgeryStageInfoEntity;
import com.ebig.hdi.modules.surgery.entity.vo.SurgeryInfoVO;
import com.ebig.hdi.modules.surgery.entity.vo.SurgeryStageInfoVO;

/**
 * 手术跟台目录表
 * 
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-06-11 10:15:31
 */
public interface SurgeryStageInfoDao extends BaseMapper<SurgeryStageInfoEntity> {
	List<SurgeryStageInfoVO> listForPage(Pagination page,SurgeryStageInfoVO stageInfoVo);
	
	SurgeryStageInfoVO selectSurgeryStageInfoById(@Param("id") Long id);
}
