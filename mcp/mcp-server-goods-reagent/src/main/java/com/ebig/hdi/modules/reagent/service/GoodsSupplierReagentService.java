package com.ebig.hdi.modules.reagent.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.reagent.entity.GoodsSupplierReagentEntity;
import com.ebig.hdi.modules.reagent.vo.GoodsSupplierReagentVO;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;

/**
 * 供应商试剂信息
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:28:15
 */
public interface GoodsSupplierReagentService extends IService<GoodsSupplierReagentEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    void save(GoodsSupplierReagentVO goodsSupplierReagentVO);
    
    void update(GoodsSupplierReagentVO goodsSupplierReagentVO);
    
    GoodsSupplierReagentVO selectReagentById(Long id);

	void toggle(Map<String, Object> params);
	
	GoodsSupplierReagentEntity selectByGoodsNameAndFactoryNameAndSupplierId(String goodsName, String factoryName, Long supplierId);
	
	//定时任务用	
	void updateIsMatch(Long goodsId);
	
    //HDI转换用
	List<Map<String, Object>> selectBySourcesIds(List<String> sourcesSpecsIds);
	
	void input(String[][] rows,Long userId,Long deptId);

	void updateSupplierGoodsSendNotUpload(Long supplierId, Long goodsId);

	List<Map<String, Object>> getList(Map<String, Object> map);

	/**
	 * @Description:批量导入
	 * @Author: ZhengHaiWen
	 * @Date: 2019/12/12
	 */
	 Map<String, String> importData(String[][] rows, SysUserEntity user);

	Map<String, String> importData(Object o, Object o1);
}

