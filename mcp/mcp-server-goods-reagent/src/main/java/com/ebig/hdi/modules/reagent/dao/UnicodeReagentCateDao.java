package com.ebig.hdi.modules.reagent.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.modules.reagent.entity.UnicodeReagentCateEntity;

/**
 * 
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-13 11:01:00
 */
public interface UnicodeReagentCateDao extends BaseMapper<UnicodeReagentCateEntity> {
	
	List<UnicodeReagentCateEntity> selectReagent(@Param("pcateId") Long pcateId);
	
	List<UnicodeReagentCateEntity> selectCateId(@Param("cateId") Long cateId);
	
	List<UnicodeReagentCateEntity> selectByCateLevel(@Param("cateLevel") Integer cateLevel);
	
	List<UnicodeReagentCateEntity> selectCateLevel(@Param("cateLevel") Integer cateLevel,@Param("cateId") Long cateId );

	UnicodeReagentCateEntity selectBypcateId(@Param("pcateId") Long pcateId);
}
