package com.ebig.hdi.modules.core.service.impl;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.entity.TableFieldInfo;
import com.baomidou.mybatisplus.entity.TableInfo;
import com.baomidou.mybatisplus.toolkit.TableInfoHelper;
import com.ebig.hdi.common.datasources.annotation.DMLFlag;
import com.ebig.hdi.common.datasources.annotation.Reference;
import com.ebig.hdi.common.entity.MasterDetailsCommonEntity;
import com.ebig.hdi.common.enums.DMLFlagEnum;
import com.ebig.hdi.common.utils.ReflectUitls;
import com.ebig.hdi.modules.core.dao.CommonDao;
import com.ebig.hdi.modules.core.service.CommonService;

import cn.hutool.core.collection.CollectionUtil;

@Service("commonService")
public class CommonServiceImpl implements CommonService {

	@Autowired
	private CommonDao commonDao;
	private static final String PRIMARY_KEY = "_id";

	@Override
	@Transactional(rollbackFor = Exception.class)
	public <A, M> void masterDetailsUnifySave(MasterDetailsCommonEntity<A, M> masterDetailsCommonEntity) {
		A master = masterDetailsCommonEntity.getMaster();
		// 主表
		Object masterId = masterOperation(master);
		// 细表
		List<M> details = masterDetailsCommonEntity.getDetails();
		for (M m : details) {
			detailOperation(m, masterId);
		}
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public <B> void cascadeDelete(B entity, Class<?> cascadeEntity) {
		Class<? extends Object> entityCls = entity.getClass();
		String refComposite = checkMasterDetailShip(entityCls, cascadeEntity);
		String[] split = refComposite.split(":");
		String refKey;
		if (split.length == 0 || StringUtils.isBlank(refKey = split[0])) {
			throw new RuntimeException(
					"实体：" + cascadeEntity.getName() + "与实体：" + entityCls.getName() + "并无外键关联，不能进行级联删除");
		}
		String key = null;
		if (split.length == 1) {
			key = ReflectUitls.getAnnotationField(entity, TableId.class);
		} else {
			key = split[1];
		}
		Object fieldValue = ReflectUitls.getFieldValue(entity, key);
		Map<String, Object> condition = new LinkedHashMap<>();
		condition.put(refKey, fieldValue);
		commonDao.deleteEntity(wrapDeleteEntity(cascadeEntity, condition));// 删除所有孩子
		condition.put(key, fieldValue);
		commonDao.deleteEntity(wrapDeleteEntity(entity, condition));// 删除自己
	}

	@Override
	public <B> Object insertEntity(B entity) {
		Map<String, Object> wrapInsertEntity = wrapInsertEntity(entity, null);
		commonDao.insertEntity(wrapInsertEntity);
		return wrapInsertEntity.get(PRIMARY_KEY);
	}

	@Override
	public <B> Integer updateEntity(B entity) {
		return commonDao.updateEntity(wrapUpdateEntity(entity));
	}

	@Override
	public <B> Integer deleteEntityById(B entity) {
		return commonDao.deleteEntity(wrapDeleteEntityById(entity));
	}
	
	@Override
	public <B> Integer deleteEntity(B entity, Map<String, Object> map) {
		return commonDao.deleteEntity(wrapDeleteEntity(entity, map));
	}

	/**
	 * 构造删除的参数
	 * 
	 * @param entity
	 * @param condition
	 * @return
	 */
	private <B> Map<String, Object> wrapDeleteEntity(B entity, Map<String, Object> condition) {
		Class<?> entityCls;
		if (entity instanceof Class) {
			entityCls = (Class<?>) entity;
		} else {
			entityCls = entity.getClass();
		}
		Map<String, Object> map = new LinkedHashMap<>();
		TableInfo tableInfo = TableInfoHelper.getTableInfo(entityCls);
		map.put("tableName", tableInfo.getTableName());
		StringBuilder builder = new StringBuilder("1=1");
		for (Map.Entry<String, Object> entry : condition.entrySet()) {
			String key = entry.getKey();
			TableFieldInfo tableFieldInfo = CollectionUtil.findOne(tableInfo.getFieldList(),
					(t) -> t.getProperty().equals(key));
			if (tableFieldInfo == null) {
				if (tableInfo.getKeyProperty().equals(key)) {// 如果是主键
					builder.append(" AND " + tableInfo.getKeyColumn() + "=#{" + key + "}");
				}
			} else {
				builder.append(" AND " + tableFieldInfo.getColumn() + "=#{" + key + "}");
			}
			map.put(key, entry.getValue());
		}
		map.put("condition", builder.toString());
		return map;
	}

	/**
	 * 根据ID删除,Entity可空
	 * 
	 * @param entity
	 * @param condition
	 * @return
	 */
	private <B> Map<String, Object> wrapDeleteEntityById(B entity, String idName, Object idValue) {
		Map<String, Object> condition = new LinkedHashMap<>();
		condition.put(idName, idValue);
		return wrapDeleteEntity(entity, condition);
	}

	/**
	 * 根据ID删除，Entity主键必须有值
	 * 
	 * @param entity
	 * @param condition
	 * @return
	 */
	private <B> Map<String, Object> wrapDeleteEntityById(B entity) {
		String idName = ReflectUitls.getAnnotationField(entity, TableId.class);
		Map<String, Object> condition = new LinkedHashMap<>();
		condition.put(idName, ReflectUitls.getFieldValue(entity, idName));
		return wrapDeleteEntity(entity, condition);
	}

	/**
	 * 检查主外键关系,如果存在主外键关系，则返回外键属性名+涉及属性名
	 * 
	 * @param orgEntity
	 * @param cascadeEntity
	 * @return
	 */
	private String checkMasterDetailShip(Class<?> orgEntity, Class<?> cascadeEntity) {
		Field[] declaredFields = cascadeEntity.getDeclaredFields();
		for (Field field : declaredFields) {
			if (field.isAnnotationPresent(Reference.class)) {
				Reference[] annotation = field.getAnnotationsByType(Reference.class);
				for (Reference reference : annotation) {
					if (reference.value().equals(orgEntity)) {
						return field.getName() + ":" + reference.field();
					}
				}
			}
		}
		return StringUtils.EMPTY;
	}

	/**
	 * 构造新增的参数
	 * 
	 * @param entity
	 * @return
	 */
	private <B> Map<String, Object> wrapInsertEntity(B entity, Object masterId) {
		Class<?> entityClass = entity.getClass();
		Map<String, Object> map = new LinkedHashMap<>();
		TableInfo tableInfo = TableInfoHelper.getTableInfo(entity.getClass());
		map.put("tableName", tableInfo.getTableName());
		Field[] fields = entityClass.getDeclaredFields();
		StringBuilder columnBuilder = new StringBuilder("(");
		StringBuilder valuesBuilder = new StringBuilder("(");
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			try {
				if (field.isAnnotationPresent(DMLFlag.class)) {// DML标记属性跳过
					continue;
				}
				field.setAccessible(true);// 设置属性可访问
				String name = field.getName();
				if (masterId != null) {// 如果是细表,设置主表的id
					String annotationField = ReflectUitls.getAnnotationField(entity, Reference.class);
					if (name.equals(annotationField)) {
						field.set(entity, masterId);// 设置外键字段的值
					}
				}
				Object object = field.get(entity);
				TableFieldInfo tableFieldInfo = CollectionUtil.findOne(tableInfo.getFieldList(),
						(t) -> t.getProperty().equals(name) && object != null);
				if (tableFieldInfo != null) {
					columnBuilder.append(tableFieldInfo.getColumn() + ",");// 插入的列
					valuesBuilder.append("#{key" + i + "},");// 插入的值
					map.put("key" + i, object);
				}else if(field.isAnnotationPresent(TableId.class)){
					columnBuilder.append(field.getName() + ",");// 插入的列
					valuesBuilder.append("#{key" + i + "},");// 插入的值
					map.put("key" + i, object);
				}

			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		map.put("columns", columnBuilder.replace(columnBuilder.length() - 1, columnBuilder.length(), ")").toString());
		map.put("values", valuesBuilder.replace(valuesBuilder.length() - 1, valuesBuilder.length(), ")").toString());
		return map;
	}

	/**
	 * 构造更新的参数
	 * 
	 * @param entity
	 * @return
	 */
	private <B> Map<String, Object> wrapUpdateEntity(B entity) {
		Class<?> entityClass = entity.getClass();
		Map<String, Object> map = new LinkedHashMap<>();
		TableInfo tableInfo = TableInfoHelper.getTableInfo(entity.getClass());
		map.put("tableName", tableInfo.getTableName());
		StringBuilder builder = new StringBuilder();
		Field[] fields = entityClass.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			try {
				if (field.isAnnotationPresent(DMLFlag.class)) {// DML标记属性跳过
					continue;
				}
				field.setAccessible(true);
				Object object = field.get(entity);
				if (field.isAnnotationPresent(TableId.class)) {// 主键跳过
					map.put("id", tableInfo.getKeyColumn());
					map.put("value", object);
					continue;
				}
				TableFieldInfo tableFieldInfo = CollectionUtil.findOne(tableInfo.getFieldList(),
						(t) -> t.getProperty().equals(field.getName()) && object != null);
				if (tableFieldInfo != null) {
					builder.append(tableFieldInfo.getColumn() + "=#{key" + i + "},");
					map.put("key" + i, object);
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		map.put("columnsMap", builder.substring(0, builder.length() - 1));
		return map;
	}

	private <B> Object masterOperation(B entity) {
		Object fieldValue = ReflectUitls.getFieldValue(entity, ReflectUitls.getAnnotationField(entity, TableId.class));
		if (fieldValue != null) {
			// 编辑
			commonDao.updateEntity(wrapUpdateEntity(entity));
			return fieldValue;
		}
		Map<String, Object> wrapInsertEntity = wrapInsertEntity(entity, null);
		commonDao.insertEntity(wrapInsertEntity);
		return wrapInsertEntity.get(PRIMARY_KEY);
	}

	private <B> void detailOperation(B entity, Object masterId) {
		Integer type = DMLFlagEnum.getType(entity);
		if (DMLFlagEnum.INSERT.getType().equals(type)) {
			commonDao.insertEntity(wrapInsertEntity(entity, masterId));
		} else if (DMLFlagEnum.UPDATE.getType().equals(type)) {
			commonDao.updateEntity(wrapUpdateEntity(entity));
		} else if (DMLFlagEnum.DELETE.getType().equals(type)) {
			String primaryKey = ReflectUitls.getAnnotationField(entity, TableId.class);
			if (StringUtils.isBlank(primaryKey)) {
				throw new RuntimeException("找不到主键字段,无法删除");
			}
			commonDao.deleteEntity(
					wrapDeleteEntityById(entity, primaryKey, ReflectUitls.getFieldValue(entity, primaryKey)));
		} else {
			throw new RuntimeException("不能识别的DML类型:type=" + type);
		}
	}
}
