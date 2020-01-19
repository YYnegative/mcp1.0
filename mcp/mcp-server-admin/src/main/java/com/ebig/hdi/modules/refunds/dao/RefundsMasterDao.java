package com.ebig.hdi.modules.refunds.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.refunds.entity.RefundsDetailEntity;
import com.ebig.hdi.modules.refunds.entity.RefundsMasterEntity;
import com.ebig.hdi.modules.refunds.entity.vo.RefundsApplyDetailVO;
import com.ebig.hdi.modules.refunds.entity.vo.RefundsDetailVO;
import com.ebig.hdi.modules.refunds.entity.vo.RefundsMasterVO;
import com.ebig.hdi.modules.refunds.entity.vo.ReturnDetailVo;
import com.ebig.hdi.modules.refunds.param.RefundsMasterParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 退货单信息
 * 
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-05-28 16:24:21
 */
public interface RefundsMasterDao extends BaseMapper<RefundsMasterEntity> {
	
	List<RefundsMasterVO> listForPage(Pagination page,RefundsMasterVO rmVO);
	
	RefundsMasterVO selectRefundsById(@Param("id") Long id);
	
	List<RefundsDetailVO> selectToSave(@Param("params") Map<String,Object> params);

	/**
	 * 获取数据
	 * @param columns 返回的列
	 * @param queryParam 查询参数
     * @return
     */
	List<Map<String,Object>> getList(@Param("columns")String[] columns,@Param("queryParam") RefundsMasterParam queryParam);
	/**
	 * /查询原医院商品信息,平台医院商品信息
	 * @param hospitalId 医院id
	 * @param supplierId 供应商id
	 * @param supplierGoodsName 供应商商品名称
	 * @param supplierGoodsSpecsName 供应商商品规格名称
	 * @return
	 */
	Map<String,Object> getMap(@Param("hospitalId") Long hospitalId, @Param("supplierId")Long supplierId,
							  @Param("supplierGoodsName") String supplierGoodsName,
							  @Param("supplierGoodsSpecsName")String supplierGoodsSpecsName);


	List<ReturnDetailVo> getDetailAndFactoryName(@Param("supplyNo")String supplyNo);

    Map<String, Object> getMaster(@Param("hospitalId")Long hospitalId,
                                  @Param("supplierId")Long supplierId,
                                  @Param("goodsType")Integer goodsType,
                                  @Param("supplierGoodsId") Long supplierGoodsId,
                                  @Param("supplierGoodsSpecsId")Long supplierGoodsSpecsId);

	void deleteDetailByMasterId(@Param("refundsMasterId") Long refundsMasterId);

    Integer selectByRefundsNo(@Param("refundsNo") String refundsNo);

	List<Map<String, Object>> getGoodsSpecsMap(@Param("supplyNo")String supplyNo, @Param("goodsId")Integer goodsId, @Param("goodsClass")Integer goodsClass);

	List<Map<String, Object>> getLotMap(@Param("supplyNo")String supplyNo, @Param("goodsId")Integer goodsId, @Param("goodsClass")Integer goodsClass,
										@Param("goodsSpecsId")Integer specsId);

    List<RefundsDetailEntity> selectDetail(@Param("id") Long id);
}
