package com.ebig.hdi.modules.drugs.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.modules.drugs.entity.UnicodeDrugsCateEntity;
import org.springframework.stereotype.Repository;

/**
 * 
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-13 11:02:14
 */
@Repository
public interface UnicodeDrugsCateDao extends BaseMapper<UnicodeDrugsCateEntity> {
	
	List<UnicodeDrugsCateEntity> selectDrugs(@Param("pcateId") Long pcateId);
	
	List<UnicodeDrugsCateEntity> selectCateId(@Param("cateId") Long cateId);
	
	List<UnicodeDrugsCateEntity> selectByCateLevel(@Param("cateLevel") Integer cateLevel);

	List<UnicodeDrugsCateEntity> selectCateLevel(@Param("cateLevel") Integer cateLevel,@Param("cateId") Long cateId );

	UnicodeDrugsCateEntity selectBypcateId(@Param("pcateId") Long pcateId);

    List<UnicodeDrugsCateEntity> selectAll();

	String selectIdByCateName(@Param("cateName") String cateName);
}
