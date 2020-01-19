package com.ebig.hdi.modules.license.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.license.entity.LicenseAgentInfoEntity;
import com.ebig.hdi.modules.license.entity.vo.LicenseAgentInfoVO;

/**
 * 代理商证照信息
 * 
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-05-21 17:26:29
 */
public interface LicenseAgentInfoDao extends BaseMapper<LicenseAgentInfoEntity> {
	
	List<LicenseAgentInfoVO> listForPage(Pagination page,LicenseAgentInfoVO laiVO);
	
	LicenseAgentInfoVO selectByAgentId(@Param("id") Long id);
	
	LicenseAgentInfoVO selectByNewLicenseId(@Param("id") Long id);

	void saveAgent(Map<String, Object> agent);
	
	List<Map<String, Object>> selectByAgentName(@Param("agentName") String agentName);
	
	List<Map<String, Object>> selectByCreditCode(@Param("creditCode") String creditCode);
}
