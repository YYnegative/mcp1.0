package com.ebig.hdi.modules.core.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ebig.hdi.modules.core.entity.CoreLotEntity;
import com.ebig.hdi.modules.core.entity.CoreStorehouseEntity;
import com.ebig.hdi.modules.core.service.PublicComboDataService;
import com.ebig.hdi.modules.org.entity.OrgHospitalInfoEntity;
import com.ebig.hdi.modules.sys.controller.AbstractController;

/**
 * 公共controller
 * 
 * @author zack
 */
@RestController
@RequestMapping("/public/combo")
public class PublicComboDataController extends AbstractController {

	@Autowired
	private PublicComboDataService publicComboDataService;

	/**
	 * 获取医院数据
	 * 
	 * @return
	 */
	@RequestMapping("/hospitalInfo")
	public List<OrgHospitalInfoEntity> hospitalInfo(@RequestBody Map<String, Object> params) {
		return publicComboDataService.getHospitalInfo((String) params.get("value"), getDeptId());
	}
	
	/**
	 * 获取医院数据
	 * 
	 * @return
	 */
	@RequestMapping("/supplyHospitalInfo")
	public List<OrgHospitalInfoEntity> supplyHospitalInfo(@RequestBody Map<String, Object> params) {
		return publicComboDataService.getHospitalInfo((String) params.get("value"), getDeptId());
	}
	
	
	/**
	 * 获取医院数据
	 * 
	 * @return
	 */
	@RequestMapping("/acceptHospitalInfo")
	public List<OrgHospitalInfoEntity> acceptHospitalInfo(@RequestBody Map<String, Object> params) {
		return publicComboDataService.getHospitalInfo((String) params.get("value"), getDeptId());
	}
	
	
	/**
	 * 获取医院数据
	 * 
	 * @return
	 */
	@RequestMapping("/labelHospitalInfo")
	public List<OrgHospitalInfoEntity> labelHospitalInfo(@RequestBody Map<String, Object> params) {
		return publicComboDataService.getHospitalInfo((String) params.get("value"), getDeptId());
	}
	

	/**
	 * 获取批号数据
	 * 
	 * @return
	 */
	@RequestMapping("/lotInfo")
	public List<CoreLotEntity> lotInfo(@RequestBody Map<String, Object> params) {
		params.put("deptId", getDeptId());
		return publicComboDataService.getLotInfo(params);
	}
	
	
	/**
	 * 获取批号数据
	 * 
	 * @return
	 */
	@RequestMapping("/supplyLotInfo")
	public List<CoreLotEntity> supplyLotInfo(@RequestBody Map<String, Object> params) {
		params.put("deptId", getDeptId());
		return publicComboDataService.getLotInfo(params);
	}
	
	
	/**
	 * 获取库房数据
	 * 
	 * @return
	 */
	@RequestMapping("/storehouseInfo")
	public List<CoreStorehouseEntity> storehouseInfo(@RequestBody Map<String, Object> params) {
		params.put("deptId", getDeptId());
		return publicComboDataService.getStorehouseInfo(params);
	}
	
	/**
	 * 获取库房数据
	 * 
	 * @return
	 */
	@RequestMapping("/supplyStorehouseInfo")
	public List<CoreStorehouseEntity> supplyStorehouseInfo(@RequestBody Map<String, Object> params) {
		params.put("deptId", getDeptId());
		return publicComboDataService.getStorehouseInfo(params);
	}
	
	
	/**
	 * 获取库房数据
	 * 
	 * @return
	 */
	@RequestMapping("/labelStorehouseInfo")
	public List<CoreStorehouseEntity> labelStorehouseInfo(@RequestBody Map<String, Object> params) {
		params.put("deptId", getDeptId());
		return publicComboDataService.getStorehouseInfo(params);
	}
	
	
	/**
	 * 获取库房数据
	 * 
	 * @return
	 */
	@RequestMapping("/acceptStorehouseInfo")
	public List<CoreStorehouseEntity> acceptStorehouseInfo(@RequestBody Map<String, Object> params) {
		params.put("deptId", getDeptId());
		return publicComboDataService.getStorehouseInfo(params);
	}
	
	
	/**
	 * 获取医院商品数据(包括未匹配和已匹配)
	 * @param goodsid
	 * @param goodstypeid
	 * @param value
	 * @return
	 */
	@RequestMapping("/hospitalGoodsInfo")
	public List<Map<String,Object>> hospitalGoodsInfo(@RequestBody Map<String, Object> params){
		return publicComboDataService.queryHospitalGoodsInfo(params);
	}
	
	
	/**
	 * 获取医院商品数据(包括未匹配和已匹配)
	 * @param goodsid
	 * @param goodstypeid
	 * @param value
	 * @return
	 */
	@RequestMapping("/hospitalGoodsInfoSpecs")
	public List<Map<String,Object>> hospitalGoodsInfoSpecs(@RequestBody Map<String, Object> params){
		return publicComboDataService.queryHospitalGoodsInfo(params);
	}
	
	/**
	 * 获取供应商商品数据(只包括已匹配)
	 * @param goodsid
	 * @param goodstypeid
	 * @param value
	 * @return
	 */
	@RequestMapping("/supplierGoodsInfo")
	public List<Map<String,Object>> supplierGoodsInfo(@RequestBody Map<String, Object> params){
		params.put("deptId", getDeptId());
		return publicComboDataService.querySupplierGoodsInfo(params);
	}
	
	/**
	 * 获取供应商商品数据(只包括已匹配) 批号
	 * @param goodsid
	 * @param goodstypeid
	 * @param value
	 * @return
	 */
	@RequestMapping("/lotGoodsInfo")
	public List<Map<String,Object>> lotGoodsInfo(@RequestBody Map<String, Object> params){
		params.put("deptId", getDeptId());
		return publicComboDataService.querySupplierGoodsInfo(params);
	}
	
	
	/**
	 * 获取供应商商品数据(只包括已匹配)
	 * @param goodsid
	 * @param goodstypeid
	 * @param value
	 * @return
	 */
	@RequestMapping("/supplierGoodsInfoSpecs")
	public List<Map<String,Object>> supplierGoodsInfoSpecs(@RequestBody Map<String, Object> params){
		params.put("deptId", getDeptId());
		return publicComboDataService.querySupplierGoodsInfo(params);
	}
	
	
	/**
	 * 获取供应商商品数据(只包括已匹配) 批号
	 * @param goodsid
	 * @param goodstypeid
	 * @param value
	 * @return
	 */
	@RequestMapping("/lotGoodsInfoSpecs")
	public List<Map<String,Object>> lotGoodsInfoSpecs(@RequestBody Map<String, Object> params){
		params.put("deptId", getDeptId());
		return publicComboDataService.querySupplierGoodsInfo(params);
	}
	
}
