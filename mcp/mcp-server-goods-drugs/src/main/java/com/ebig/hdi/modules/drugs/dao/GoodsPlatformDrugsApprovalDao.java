package com.ebig.hdi.modules.drugs.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.drugs.entity.GoodsPlatformDrugsApprovalEntity;
import com.ebig.hdi.modules.drugs.vo.GoodsPlatformDrugsEntityVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 平台药品信息
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-08 14:21:25
 */
public interface GoodsPlatformDrugsApprovalDao extends BaseMapper<GoodsPlatformDrugsApprovalEntity> {

	GoodsPlatformDrugsApprovalEntity selectByDrugsName(@Param("drugsName") String drugsName);

	GoodsPlatformDrugsEntityVo selectPlatformDrugsById(@Param("id") Long id);

	List<GoodsPlatformDrugsEntityVo> selectPlatformDrugsList(Pagination page, Map<String, Object> params);

	List<Map<String, Object>> selectMatch(Map<String, Object> hospitalDrugs);

	String selectByFactoryName(@Param("factoryName")String factoryName);

    List<Map<String, Object>> getList(Map map);

    Integer selectIfExist(@Param("id") Long id);

	String selectByFactoryNameExist(@Param("factoryName") String factoryName);

	GoodsPlatformDrugsEntityVo selectByDrugsNameAndApprovals(@Param("drugsName") String drugsName,@Param("approvals") String approvals);

	GoodsPlatformDrugsApprovalEntity selectByDrugsNameAndId(@Param("drugsName") String drugsName, @Param("id")Long id);

	Integer selectByApprovals(@Param("approvals")String approvals);

	Integer selectByGoodsNameOrApporvaols(@Param("drugsName")String drugsName, @Param("approvals")String approvals);

	Integer selectByApprovalsAndId(@Param("id")Long id, @Param("approvals")String approvals);
}
