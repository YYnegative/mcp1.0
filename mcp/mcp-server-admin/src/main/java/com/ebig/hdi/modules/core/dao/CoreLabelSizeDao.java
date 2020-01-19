package com.ebig.hdi.modules.core.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.modules.core.entity.CoreLabelSizeEntity;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2020-01-08 10:08:48
 */
public interface CoreLabelSizeDao extends BaseMapper<CoreLabelSizeEntity> {

    CoreLabelSizeEntity getUserDetail(@Param("userId") Long userId,@Param("typeId")Integer typeId);


}
