package com.ebig.hdi.modules.org.dao;

import com.ebig.hdi.modules.org.entity.OrgAgentInfoEntity;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 代理商信息
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-04-23 09:27:41
 */
public interface OrgAgentInfoDao extends BaseMapper<OrgAgentInfoEntity> {
	
	List<OrgAgentInfoEntity> selectByAgentName(@Param("agentName") String agentName);
	
	List<OrgAgentInfoEntity> selectByCreditCode(@Param("creditCode") String creditCode);
}
