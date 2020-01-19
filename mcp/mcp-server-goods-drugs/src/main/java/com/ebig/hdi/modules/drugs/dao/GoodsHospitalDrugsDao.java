package com.ebig.hdi.modules.drugs.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.drugs.entity.GoodsHospitalDrugsEntity;
import com.ebig.hdi.modules.drugs.vo.GoodsHospitalDrugsEntityVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 医院药品信息
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-08 14:22:45
 */
public interface GoodsHospitalDrugsDao extends BaseMapper<GoodsHospitalDrugsEntity> {

	GoodsHospitalDrugsEntity selectByHospitalIdAndDrugsName(@Param("hospitalId") Long hospitalId, @Param("drugsName") String drugsName);

	List<GoodsHospitalDrugsEntityVo> selectHospitalDrugsList(Pagination page, Map<String, Object> params);

	GoodsHospitalDrugsEntityVo selectHospitalDrugsById(@Param("id") Long id);
	
	GoodsHospitalDrugsEntity selectByGoodsNameAndFactoryNameAndHospitalId(@Param("goodsName") String goodsName,@Param("factoryName") String factoryName, @Param("hospitalId") Long hospitalId);
	
	//HDI转换用
	Map<String, Object> selectBySourcesId(@Param("sourcesSpecsId")String sourcesSpecsId);

	Map<String, Object> selectHospitalInfoByHospitalId(@Param("hospitalId") Long hospitalId);
	
	List<Map<String, Object>>selectNotMatch(@Param("limit")Integer limit);

	Map<String,Object> selectDrugMap(@Param("tspecsId") Long tspecsId,@Param("tgoodsId")  Long tgoodsId);
}
