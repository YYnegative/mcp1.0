package com.ebig.hdi.modules.surgery.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.surgery.entity.SurgeryInfoEntity;
import com.ebig.hdi.modules.surgery.entity.vo.SurgeryInfoVO;
import com.ebig.hdi.modules.surgery.entity.vo.SurgeryStageInfoVO;

/**
 * 手术信息表
 * 
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-06-11 10:15:32
 */
public interface SurgeryInfoDao extends BaseMapper<SurgeryInfoEntity> {
	List<SurgeryInfoVO> listForPage(Pagination page,SurgeryInfoVO siVO);
	
	SurgeryStageInfoVO createStageMaster(@Param("id") Long id);
	
	List<SurgeryInfoEntity> querySurgeryNo(@Param("params") Map<String, Object> params);
}
