package com.ebig.hdi.modules.goods.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.goods.entity.GoodsSupplierSendEntity;
import com.ebig.hdi.modules.job.entity.TempPubGoodsEntity;
import com.ebig.hdi.modules.job.entity.TempPubSupplyGoodsEntity;

import java.util.List;
import java.util.Map;

/**
 * 供应商品下发
 *
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-07-26 11:49:04
 */
public interface GoodsSupplierSendService extends IService<GoodsSupplierSendEntity> {

    PageUtils selectSendableList(Map<String, Object> params);

	PageUtils selectSentList(Map<String, Object> params);

	void save(GoodsSupplierSendEntity goodsSupplierSend);

	void batchSave(List<GoodsSupplierSendEntity> goodsSupplierSendList);

	/**
	 * 获取i条未上传记录
	 * @param i
	 * @return
     */
	List<TempPubGoodsEntity> selectNotUpload(int i);

	/**
	 * 改造后获取i条未上传记录
	 * @param i
	 * @return
	 */
	List<TempPubSupplyGoodsEntity> selectNotUploadNew(int i);
}

