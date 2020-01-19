package com.ebig.hdi.modules.core.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.core.entity.CoreAcceptMasterEntity;

/**
 * 
 * 
 * @author frink
 * @email 
 * @date 2019-05-25 12:01:15
 */
public interface CoreAcceptMasterDao extends BaseMapper<CoreAcceptMasterEntity> {
	
	List<CoreAcceptMasterEntity> selectByDeptId(Pagination page, Map<String, Object> params);
	
	List<CoreAcceptMasterEntity> selectByBedingungen(Pagination page, Map<String, Object> params);
	
	CoreAcceptMasterEntity selectAcceptMaster(@Param("deptId") Long deptId,@Param("storehouseid") Long storehouseid,@Param("horgId") Long horgId);
	
	//转换
	CoreAcceptMasterEntity selectByOrgdataid(@Param("orgdataid") String orgdataid);
}
