package com.ebig.hdi.modules.consumables.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.common.entity.OrgFactoryInfoApprovalEntity;
import com.ebig.hdi.common.entity.OrgSupplierInfoEntity;
import com.ebig.hdi.modules.consumables.entity.GoodsSupplierConsumablesApprovalsEntity;
import com.ebig.hdi.modules.consumables.entity.GoodsSupplierConsumablesEntity;
import com.ebig.hdi.modules.consumables.entity.vo.GoodsSupplierConsumablesVO;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 供应商耗材信息
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:36:02
 */
public interface GoodsSupplierConsumablesDao extends BaseMapper<GoodsSupplierConsumablesEntity> {

	List<GoodsSupplierConsumablesVO> listForPage(Pagination page, GoodsSupplierConsumablesVO gscVO);

	GoodsSupplierConsumablesVO selectConsumablesById(@Param("id") Long id);

	GoodsSupplierConsumablesEntity selectByGoodsNameAndFactoryNameAndSupplierId(@Param("goodsName") String goodsName, @Param("factoryName") String factoryName, @Param("supplierId") Long supplierId);

	//HDI转换用
	List<Map<String, Object>> selectBySourcesIds(@Param("sourcesSpecsIds") List<String> sourcesSpecsIds);

	List<GoodsSupplierConsumablesEntity> selectAll();

	Map<String, Object> selectSupplierInfoBySupplierId(@Param("supplierId") Long supplierId);

	List<Map<String, Object>> selectNotMatch(@Param("limit") Integer limit);

	void updateSupplierGoodsSendNotUpload(@Param("supplierId") Long supplierId, @Param("goodsId") Long goodsId);

    List<GoodsSupplierConsumablesApprovalsEntity> selectAllApprovals();

    List<OrgFactoryInfoApprovalEntity> selectFactoryList();

    List<OrgSupplierInfoEntity> selectSupplierList();

    List<GoodsSupplierConsumablesEntity> selectByGoodsNameAndApprovals(@Param("consumablesName") String consumablesName, @Param("approvals") String approvals);

    List<Map<String, Object>> getList(HashMap<String, Object> map);

    GoodsSupplierConsumablesApprovalsEntity selectByApprovalsAndconsumablesId(@Param("approvals") String approvals, @Param("consumablesId") Long id);

    GoodsSupplierConsumablesApprovalsEntity selectByApprovals(String approvals);

    int selectConsumablesNameAndApprovals(@Param("consumablesName") String consumablesName, @Param("approvals") String approvals);

	Map<String,Object> selectConsumablesMap(@Param("tapprovalId") Long tapprovalId,@Param("tspecsId")  Long tspecsId,@Param("tgoodsId")  Long tgoodsId);
}
