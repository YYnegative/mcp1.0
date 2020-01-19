package com.ebig.hdi.modules.consumables.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.consumables.entity.GoodsSupplierConsumablesEntity;
import com.ebig.hdi.modules.consumables.entity.vo.GoodsSupplierConsumablesVO;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;

/**
 * 供应商耗材信息
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:36:02
 */
public interface GoodsSupplierConsumablesService extends IService<GoodsSupplierConsumablesEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    GoodsSupplierConsumablesVO selectById(Long id);

    Map<String, Object> save(GoodsSupplierConsumablesVO goodsSupplierConsumablesVO);

    Map<String, Object> update(GoodsSupplierConsumablesVO goodsSupplierConsumablesVO);
    
	void toggle(Map<String, Object> params);
	
	GoodsSupplierConsumablesEntity selectByGoodsNameAndFactoryNameAndSupplierId(String goodsName,String factoryName,Long supplierId);
    
    void updateIsNoUpload(Long goodsId);
    
	//定时任务用
    void updateIsMatch(Long goodsId);
    
    //HDI转换用
    List<Map<String, Object>> selectBySourcesIds(List<String> sourcesSpecsIds);
    
    //定时任务，查询300条为上传的数据
    List<GoodsSupplierConsumablesEntity> selectAll();

	void updateSupplierGoodsSendNotUpload(Long supplierId, Long goodsId);

    Map<String, String> importData(String[][] rows, SysUserEntity user);

    List<Map<String, Object>> getList(HashMap<String, Object> map);
}

