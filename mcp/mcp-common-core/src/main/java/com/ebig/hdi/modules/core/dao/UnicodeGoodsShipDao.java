package com.ebig.hdi.modules.core.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.common.entity.UnicodeGoodsShipEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-21 15:18:03
 */
public interface UnicodeGoodsShipDao extends BaseMapper<UnicodeGoodsShipEntity> {

	List<UnicodeGoodsShipEntity> selectListByColumn(@Param("torgType") Integer torgType, @Param("tgoodsType") Integer tgoodsType, @Param("tgoodsId") Long tgoodsId, @Param("tspecsId") Long tspecsId);

	List<UnicodeGoodsShipEntity> listHospitalMatchGoods(@Param("torgId") Long torgId, @Param("pgoodsId") Long pgoodsId, @Param("pspecsId") Long pspecsId, @Param("papprovalId") Long papprovalId);

	List<UnicodeGoodsShipEntity> listSupplierMatchGoods(@Param("torgId") Long torgId, @Param("pgoodsId") Long pgoodsId, @Param("pspecsId") Long pspecsId, @Param("papprovalId") Long papprovalId);
}
