package com.ebig.hdi.modules.consumables.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.consumables.entity.GoodsPlatformConsumablesEntity;
import com.ebig.hdi.modules.consumables.vo.GoodsPlatformConsumablesEntityVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 平台耗材信息
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:37:46
 */
public interface GoodsPlatformConsumablesDao extends BaseMapper<GoodsPlatformConsumablesEntity> {

	List<GoodsPlatformConsumablesEntityVo> selectPlatformConsumablesList(Pagination page, Map<String, Object> params);

	GoodsPlatformConsumablesEntity selectByConsumablesName(@Param("consumablesName") String consumablesName);

	GoodsPlatformConsumablesEntity selectPlatformConsumablesById(@Param("id") Long id);

	List<Map<String, Object>> selectMatch(Map<String, Object> hospitalConsumables);
}
