package com.ebig.hdi.modules.job.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.modules.job.entity.TempPubGoodsEntity;
import org.springframework.stereotype.Component;

/**
 * pub_goods
 * 
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-06-18 11:04:28
 */
@Component
public interface TempPubGoodsDao extends BaseMapper<TempPubGoodsEntity> {
	List<TempPubGoodsEntity> queryAllGoodsByType(@Param("companytype") Integer companytype);
	
	List<TempPubGoodsEntity> selectAllSupplierConsumables();
	
	List<TempPubGoodsEntity> selectAllSupplierDrugs();
	
	List<TempPubGoodsEntity> selectAllSupplierReagent();
}
