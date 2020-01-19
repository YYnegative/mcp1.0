package com.ebig.hdi.modules.reagent.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.reagent.entity.GoodsSupplierReagentEntity;
import com.ebig.hdi.modules.reagent.vo.GoodsSupplierReagentVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 供应商试剂信息
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:28:15
 */
public interface GoodsSupplierReagentDao extends BaseMapper<GoodsSupplierReagentEntity> {
	
	List<GoodsSupplierReagentVO> listForPage(Pagination page,GoodsSupplierReagentVO ghr);
	
	GoodsSupplierReagentVO selectReagentById(@Param("id")Long id);

	@Select({"Select * from hdi_goods_supplier_reagent where approvals = #{approvals} and supplier_id =#{supplierId} and del_flag = 0"})
	GoodsSupplierReagentVO selectReagentByApprovals(@Param("approvals")String approvals,@Param("supplierId")Long supplierId);


	GoodsSupplierReagentEntity selectByGoodsNameAndFactoryNameAndSupplierId(@Param("goodsName") String goodsName,@Param("factoryName") String factoryName, @Param("supplierId") Long supplierId);
	
	//HDI转换用
	List<Map<String, Object>> selectBySourcesIds(@Param("sourcesSpecsIds") List<String> sourcesSpecsIds);

	Map<String, Object> selectSupplierInfoBySupplierId(@Param("supplierId")Long supplierId);

	List<Map<String, Object>> selectNotMatch(@Param("limit")Integer limit);
	
	List<Long> selectSupplierIdByName(@Param("name")String name);

	List<Map<String, Object>> getList(Map map);


	void updateSupplierGoodsSendNotUpload(@Param("supplierId")Long supplierId, @Param("goodsId")Long goodsId);

    Integer selectApprovals(GoodsSupplierReagentVO goodsSupplierReagentVO);

	Integer selectReagentNameAndApprovals(@Param("reagentName")String reagentName, @Param("approvals")String approvals);

	Integer selectfactory(GoodsSupplierReagentVO goodsSupplierReagentVO);

	Map<String,Object> selectReagentMap(@Param("tspecsId") Long tspecsId,@Param("tgoodsId") Long tgoodsId);
}
