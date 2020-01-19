package com.ebig.hdi.modules.core.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ebig.hdi.modules.core.entity.CoreLotEntity;
import com.ebig.hdi.modules.core.entity.CoreStorehouseEntity;
import com.ebig.hdi.modules.org.entity.OrgHospitalInfoEntity;

/**
 * 公共Dao层，医院、库房、批号下拉框查询
 * @author zack
 */
public interface PublicComboDataDao{

	/**
	 * 根据当前账号、查询和供应商绑定的医院数据,支持医院名称模糊查询
	 * @param value
	 * @return
	 */
	List<OrgHospitalInfoEntity> queryHospitalData(@Param("value")String value, @Param("deptId")Long deptId);
	
	/**
	 * 查询该商品规格下的生产批号，支持批号模糊查询 
	 * @param goodsid
	 * @param goodstypeid
	 * @param value
	 * @return
	 */
	List<CoreLotEntity> queryLotData(Map<String, Object> params);
	
	/**
	 * 查询该医院下的库房，支持库房模糊查询 
	 * @param goodsid
	 * @param goodstypeid
	 * @param value
	 * @return
	 */
	List<CoreStorehouseEntity> queryStorehouseData(Map<String, Object> params);
	
	/**
	 * 查询该医院/供应商下的商品，支持商品模糊查询 
	 * @param goodsid
	 * @param goodstypeid
	 * @param value
	 * @return
	 */
	List<Map<String,Object>> queryHospitalGoodsData(Map<String, Object> params);
	
	/**
	 * 查询该该医院/供应商、商品下的规格，支持规格模糊查询 
	 * @param goodsid
	 * @param goodstypeid
	 * @param value
	 * @return
	 */
	List<Map<String,Object>> querySupplierGoodsData(Map<String, Object> params);

	/**
	 * 查询供应商商品信息
	 * @param deptId  供应商机构id
	 * @param goodstypeno 供应商商品规格编码
	 * @return
	 */
	List<Map<String,Object>> querySupplierGoodsInfo(@Param("deptId") Long deptId,@Param("goodstypeno") String goodstypeno);
}
