package com.ebig.hdi.modules.consumables.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.consumables.entity.GoodsHospitalConsumablesEntity;
import com.ebig.hdi.modules.consumables.vo.GoodsHospitalConsumablesEntityVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 医院耗材信息
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:39:01
 */
public interface GoodsHospitalConsumablesDao extends BaseMapper<GoodsHospitalConsumablesEntity> {

	List<GoodsHospitalConsumablesEntityVo> selectHospitalConsumablesList(Pagination page, Map<String, Object> params);

	GoodsHospitalConsumablesEntity selectByHospitalIdAndConsumablesName(@Param("hospitalId") Long hospitalId, @Param("consumablesName") String consumablesName);

	GoodsHospitalConsumablesEntityVo selectHospitalConsumablesById(@Param("id") Long id);
	
	GoodsHospitalConsumablesEntity selectByGoodsNameAndFactoryNameAndHospitalId(@Param("goodsName") String goodsName,@Param("factoryName") String factoryName,@Param("hospitalId") Long hospitalId);
	
	//HDI转换用
	Map<String, Object> selectBySourcesId(@Param("sourcesSpecsId")String sourcesSpecsId);

	Map<String, Object> selectHospitalInfoByHospitalId(@Param("hospitalId")Long hospitalId);

	List<Map<String, Object>> selectNotMatch(@Param("limit")Integer limit);

	Map<String,Object> selectConsumablesMap(@Param("tapprovalId") Long tapprovalId,@Param("tspecsId") Long tspecsId,@Param("tgoodsId") Long tgoodsId);
}
