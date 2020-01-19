package com.ebig.hdi.modules.consumables.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.common.entity.OrgFactoryInfoEntity;
import com.ebig.hdi.modules.consumables.entity.GoodsPlatformConsumablesApprovalEntity;
import com.ebig.hdi.modules.consumables.entity.GoodsPlatformConsumablesApprovalsEntity;
import com.ebig.hdi.modules.consumables.entity.GoodsPlatformConsumablesEntity;
import com.ebig.hdi.modules.consumables.entity.UnicodeConsumablesCateEntity;
import com.ebig.hdi.modules.consumables.param.GoodsPlatformConsumablesApprovalParam;
import com.ebig.hdi.modules.consumables.vo.GoodsPlatformConsumablesEntityVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 平台耗材信息
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:37:46
 */
public interface GoodsPlatformConsumablesApprovalDao extends BaseMapper<GoodsPlatformConsumablesApprovalEntity> {

	List<GoodsPlatformConsumablesEntityVo> selectPlatformConsumablesList(Pagination page, Map<String, Object> params);

	GoodsPlatformConsumablesApprovalEntity selectByConsumablesName(@Param("consumablesName") String consumablesName);

	GoodsPlatformConsumablesApprovalEntity selectPlatformConsumablesById(@Param("id") Long id);

	List<Map<String, Object>> selectMatch(Map<String, Object> hospitalConsumables);

	List<Map<String, Object>> getList(Map map);

	List<GoodsPlatformConsumablesEntity> selectGoodsPlatformConsumablesById(Long id);

	void updateGoodsPlatformConsumablesById(@Param("id") Long id, @Param("status") Integer status);

    List<GoodsPlatformConsumablesEntity> selectListByPlatformConsumablesCode(String consumablesCode);

    List<OrgFactoryInfoEntity> selectFactoryList();

    List<UnicodeConsumablesCateEntity> selectCate();

    List<String> selectAllApprovalsByConsumablesId(Long id);

    GoodsPlatformConsumablesEntityVo selectByConsumablesNameAndApprovals(@Param("consumablesName") String consumablesName, @Param("approvals") String approvals);

	Integer selectByGoodsNameOrApporvaols(@Param("consumablesName") String consumablesName, @Param("approvals") String approvals);

    List<String> selectAllApprovals();

    List<GoodsPlatformConsumablesApprovalsEntity> selectApprovalsEntity();

}
