package com.ebig.hdi.modules.drugs.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.drugs.entity.GoodsSupplierDrugsEntity;
import com.ebig.hdi.modules.drugs.vo.GoodsSupplierDrugsEntityVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 供应商药品信息
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-08 14:17:51
 */
public interface GoodsSupplierDrugsDao extends BaseMapper<GoodsSupplierDrugsEntity> {

	GoodsSupplierDrugsEntity selectBySupplierIdAndDrugsName(@Param("supplierId") Long supplierId, @Param("drugsName") String drugsName);

	List<GoodsSupplierDrugsEntityVo> selectSupplierDrugsList(Pagination page, Map<String, Object> params);

	GoodsSupplierDrugsEntityVo selectSupplierDrugsById(@Param("id") Long id);
	
	GoodsSupplierDrugsEntity selectByGoodsNameAndFactoryNameAndSupplierId(@Param("goodsName") String goodsName,@Param("factoryName") String factoryName,@Param("supplierId") Long supplierId);
	
	//HDI转换用
	List<Map<String, Object>> selectBySourcesIds(@Param("sourcesSpecsIds")List<String> sourcesSpecsIds);

	Map<String, Object> selectSupplierInfoBySupplierId(@Param("supplierId") Long supplierId);

	List<Map<String, Object>> selectNotMatch(@Param("limit")Integer limit);
	
	List<Long> selectSupplierIdByName(@Param("supplierName")String supplierName);

	void updateSupplierGoodsSendNotUpload(@Param("supplierId")Long supplierId, @Param("goodsId")Long goodsId);

	Integer selectByApprovals(@Param("approvals") String approvals);

	List<Map<String, Object>> getList(Map map);

    GoodsSupplierDrugsEntityVo selectByDrugsNameAndApprovals(@Param("drugsName") String drugsName,@Param("approvals")  String approvals);

	Integer selectTheSameNameAndDifferentApprovlas(@Param("drugsName")String drugsName,@Param("approvals")  String approvals,@Param("specs") String specs);

	Integer selectTheSameApprovalsAndDifferentName(@Param("drugsName")String drugsName, @Param("approvals") String approvals);

	Map<String,Object> selectDrugMap(@Param("tspecsId") Long tspecsId,@Param("tgoodsId")  Long tgoodsId);
}
