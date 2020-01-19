package com.ebig.hdi.modules.reagent.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.reagent.entity.GoodsHospitalReagentEntity;
import com.ebig.hdi.modules.reagent.vo.GoodsHospitalReagentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 医院试剂信息
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:30:58
 */
public interface GoodsHospitalReagentDao extends BaseMapper<GoodsHospitalReagentEntity> {
	
	List<GoodsHospitalReagentVO> listForPage(Pagination page,GoodsHospitalReagentVO ghr);
	
	GoodsHospitalReagentVO selectReagentById(@Param("id")Long id);
	
	GoodsHospitalReagentEntity selectByGoodsNameAndFactoryNameAndHospitalId(@Param("goodsName") String goodsName,@Param("factoryName") String factoryName,@Param("hospitalId") Long hospitalId);
	
	//HDI转换用
	Map<String, Object> selectBySourcesId(@Param("sourcesSpecsId")String sourcesSpecsId);

	Map<String, Object> selectHospitalInfoByHospitalId(@Param("hospitalId")Long hospitalId);

	List<Map<String, Object>>selectNotMatch(@Param("limit")Integer limit);

	Map<String,Object> selectReagentMap(@Param("tspecsId") Long tspecsId,@Param("tgoodsId") Long tgoodsId);
}
