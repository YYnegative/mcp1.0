package com.ebig.hdi.modules.core.service;

import java.util.List;
import java.util.Map;

import com.ebig.hdi.modules.core.entity.CoreLotEntity;
import com.ebig.hdi.modules.core.entity.CoreStorehouseEntity;
import com.ebig.hdi.modules.org.entity.OrgHospitalInfoEntity;

/**
 * 下拉框公用service 
 * @author zack
 *
 */
public interface PublicComboDataService{

	/**
	 * 根据当前账号、查询和供应商绑定的医院数据,支持医院名称模糊查询
	 * @param value
	 * @return
	 */
	List<OrgHospitalInfoEntity> getHospitalInfo(String value, Long deptId);
	
	/**
	 * 查询该商品规格下的生产批号，支持批号模糊查询 
	 * @param goodsid
	 * @param goodstypeid
	 * @param value
	 * @return
	 */
	List<CoreLotEntity> getLotInfo(Map<String, Object> params);

	List<CoreStorehouseEntity> getStorehouseInfo(Map<String, Object> params);

	List<Map<String, Object>> queryHospitalGoodsInfo(Map<String, Object> params);
	
	List<Map<String, Object>> querySupplierGoodsInfo(Map<String, Object> params);

	/**
	 * 查询供应商商品信息
	 * @param deptId  供应商机构id
	 * @param goodstypeno 供应商商品规格编码
     * @return
     */
	List<Map<String, Object>> querySupplierGoodsInfo(Long deptId,String goodstypeno);
}
