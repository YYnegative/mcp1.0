package com.ebig.hdi.modules.drugs.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.drugs.entity.GoodsSupplierDrugsEntity;
import com.ebig.hdi.modules.drugs.vo.GoodsSupplierDrugsEntityVo;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;

/**
 * 供应商药品信息
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-08 14:17:51
 */
public interface GoodsSupplierDrugsService extends IService<GoodsSupplierDrugsEntity> {

    PageUtils queryPage(Map<String, Object> params);

	Map<String,String> save(GoodsSupplierDrugsEntityVo goodsSupplierDrugsEntityVo,SysUserEntity user);

	Map<String,String> update(GoodsSupplierDrugsEntityVo goodsSupplierDrugsEntityVo,SysUserEntity user);

	GoodsSupplierDrugsEntityVo selectSupplierDrugsById(Long id);

	void delete(Long[] ids);

	void toggle(Map<String, Object> params);
	
	//定时任务用
	void updateIsMatch(Long goodsId);
	
	GoodsSupplierDrugsEntity selectByGoodsNameAndFactoryNameAndSupplierId(String goodsName,String factoryName, Long supplierId);
	
    //HDI转换用
	List<Map<String, Object>> selectBySourcesIds(List<String> sourcesSpecsIds);
	
	void input(String[][] rows,Long userId,Long deptId);

	void updateSupplierGoodsSendNotUpload(Long supplierId, Long goodsId);

	Map<String, String> importData(String[][] rows, SysUserEntity user);

	List<Map<String, Object>> getList(Map<String, Object> map);
}

