package com.ebig.hdi.modules.reagent.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.consumables.entity.GoodsPlatformConsumablesEntity;
import com.ebig.hdi.modules.reagent.entity.GoodsPlatformReagentApprovalEntity;
import com.ebig.hdi.modules.reagent.param.GoodsPlatformReagentParam;
import com.ebig.hdi.modules.reagent.vo.GoodsPlatformReagentVO;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 平台试剂信息
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:29:52
 */
public interface GoodsPlatformReagentApprovalDao extends BaseMapper<GoodsPlatformReagentApprovalEntity> {
	
	List<GoodsPlatformReagentVO> selectPlatformReagentList(Pagination page, Map<String, Object> params);
	
	GoodsPlatformReagentVO selectPlatformReagentById(@Param("id") Long id);

	List<Map<String, Object>> selectMatch(Map<String, Object> hospitalReagent);
	int insertList(@Param("list") ArrayList<GoodsPlatformReagentVO> goodsPlatformReagentVOs);

	List<Map<String, Object>> selectPlatformReagent(Map map);

	GoodsPlatformReagentApprovalEntity selectListByPlatformReagentCode(@Param("reagentCode")String reagentCode);

    GoodsPlatformReagentVO selectByReagentNameAndApprovals(@Param("reagentName") String reagentName,@Param("approvals") String approvals);

	Integer selectCountByApprovals(String approvals);


    Integer checkApprovalsOnly(@Param("approvals")String approvals,@Param("id")Long id);
}
