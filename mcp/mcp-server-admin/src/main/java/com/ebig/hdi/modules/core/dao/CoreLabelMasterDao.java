package com.ebig.hdi.modules.core.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.core.entity.CoreLabelMasterEntity;
import org.springframework.stereotype.Component;

/**
 * 
 * 
 * @author frink
 * @email 
 * @date 2019-05-25 12:01:14
 */
@Component
public interface CoreLabelMasterDao extends BaseMapper<CoreLabelMasterEntity> {
	
	List<CoreLabelMasterEntity> selectByDeptId(Pagination page, Map<String, Object> params);
	
	List<CoreLabelMasterEntity> selectByBedingungen(Pagination page, Map<String, Object> params);
	
	//HDI转换用  根据labelno  查询是否存在此标签
	CoreLabelMasterEntity selectByLabelno(@Param("labelno") String labelno);

	//提交医院   查询所有的相关的标签
	List<CoreLabelMasterEntity> getCoreLabelMasterEntity(@Param("sourceid")Long sourceid);

	List<CoreLabelMasterEntity> selectByLabelids(@Param("labelids") List<Long> labelids);

	CoreLabelMasterEntity selectLabelSubmittedById(@Param("labelid") Long labelid);

	void updateLabelStatus(@Param("labelids") Long[] labelids);
}
