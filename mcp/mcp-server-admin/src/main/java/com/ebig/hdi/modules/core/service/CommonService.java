package com.ebig.hdi.modules.core.service;

import java.util.Map;

import com.ebig.hdi.common.entity.MasterDetailsCommonEntity;

/**
 * 公共service类
 * 
 * @author zack
 */
public interface CommonService {

	/**
	 * 主细表统一保存,细表必须有DMl标记
	 * 
	 * @param masterDetailsCommonEntity
	 */
	<A, M> void masterDetailsUnifySave(MasterDetailsCommonEntity<A, M> masterDetailsCommonEntity);

	/**
	 * 级联删除
	 * 
	 * @param entity
	 * @param cascadeEntity
	 */
	<B> void cascadeDelete(B entity, Class<?> cascadeEntity);

	/**
	 * 插入Entity,返回插入的ID
	 * 
	 * @param entity
	 * @return
	 */
	<B> Object insertEntity(B entity);

	/**
	 * 根据ID,更新Entity,返回成功条数
	 * 
	 * @param entity
	 * @return
	 */
	<B> Integer updateEntity(B entity);

	/**
	 * 根据ID删除Entity,返回成功条数
	 * 
	 * @param entity
	 * @return
	 */
	<B> Integer deleteEntityById(B entity);

	/**
	 * 根据条件删除Entity,返回成功条数
	 * 
	 * @param entity
	 * @return
	 */
	<B> Integer deleteEntity(B entity, Map<String, Object> map);

}
