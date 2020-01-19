package com.ebig.hdi.modules.consumables.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.modules.consumables.entity.UnicodeConsumablesCateEntity;

/**
 * 
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-13 09:53:58
 */
public interface UnicodeConsumablesCateDao extends BaseMapper<UnicodeConsumablesCateEntity> {
	
	List<UnicodeConsumablesCateEntity> selectConsumables(@Param("pcateId") Long pcateId);
	
	List<UnicodeConsumablesCateEntity> selectCateId(@Param("cateId") Long cateId);
	
	List<UnicodeConsumablesCateEntity> selectByCateLevel(@Param("cateLevel") Integer cateLevel);
	
	List<UnicodeConsumablesCateEntity> selectCateLevel(@Param("cateLevel") Integer cateLevel,@Param("cateId") Long cateId );

	UnicodeConsumablesCateEntity selectBypcateId(@Param("pcateId") Long pcateId);

    List<UnicodeConsumablesCateEntity> selectAll();

}
