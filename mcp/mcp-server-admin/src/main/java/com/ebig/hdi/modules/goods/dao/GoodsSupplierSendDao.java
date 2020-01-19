package com.ebig.hdi.modules.goods.dao;

import java.util.List;
import java.util.Map;

import com.ebig.hdi.modules.job.entity.TempPubSupplyGoodsEntity;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.goods.entity.GoodsSupplierSendEntity;
import com.ebig.hdi.modules.goods.vo.GoodsSupplierSendEntityVo;
import com.ebig.hdi.modules.job.entity.TempPubGoodsEntity;

/**
 * 供应商品下发
 * 
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-07-26 11:49:04
 */
public interface GoodsSupplierSendDao extends BaseMapper<GoodsSupplierSendEntity> {

	List<GoodsSupplierSendEntityVo> selectSendableList(Pagination page, Map<String, Object> params);

	List<GoodsSupplierSendEntityVo> selectSentList(Pagination page, Map<String, Object> params);

	List<TempPubGoodsEntity> selectNotUpload(@Param("limit")Integer limit);

	List<TempPubSupplyGoodsEntity> selectNotUploadNew(@Param("limit")Integer limit);

}
