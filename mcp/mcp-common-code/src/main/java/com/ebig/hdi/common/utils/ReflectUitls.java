package com.ebig.hdi.common.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import cn.hutool.core.bean.BeanDesc;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.Convert;

/**
 * 反射工具类
 * 
 * @author zack
 *
 */
public class ReflectUitls {

	/**
	 * 获取实体类被注解的字段
	 * 
	 * @param entity
	 * @param cls
	 * @return
	 */
	public static <T> String getAnnotationField(T entity, Class<? extends Annotation> cls) {
		Field[] fields = entity.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(cls)) {
				return field.getName();
			}
		}
		return null;
	}

	/**
	 * 获取实体类字段值
	 * 
	 * @param entity
	 * @param cls
	 * @return
	 */
	public static <T> Object getFieldValue(T entity, String fieldName) {
		if (StringUtils.isBlank(fieldName)) {
			throw new RuntimeException("传入字段为空");
		}
		Object obj = null;
		try {
			Field field = entity.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			obj = field.get(entity);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		return obj;
	}

	/**
	 * 复制entity的属性到新的对象
	 * 
	 * @param entity
	 * @param cls
	 * @return 新复制的对象
	 */
	public static <T> T transform(Object entity, Class<T> cls) {
		T newInstance = null;
		try {
			newInstance = cls.newInstance();
			BeanUtil.copyProperties(entity, newInstance);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		return newInstance;
	}

	/**
	 * 复制entity的属性到新的对象,根据映射转换属性，类型不一样时，常用类型之间可以转换成功,支持哪些类型之间转换？（看源码，太多了说不过来）
	 * 
	 * @param entity
	 * @param cls
	 * @param fieldMapping
	 * @param onlyMapping
	 *            是否仅仅使用映射来复制属性,若不是，则除了映射，默认将双方相同名字的字段复制
	 * @return
	 */
	public static <T> T transform(Object entity, Class<T> cls, Map<String, String> fieldMapping, boolean onlyMapping) {
		T newInstance = null;
		try {
			newInstance = cls.newInstance();
			BeanDesc beanDesc = BeanUtil.getBeanDesc(entity.getClass());
			BeanDesc beanDescCls = BeanUtil.getBeanDesc(cls);
			for (Map.Entry<String, String> entry : fieldMapping.entrySet()) {
				Field keyField = beanDesc.getField(entry.getKey());
				Field valueField = beanDescCls.getField(entry.getValue());
				keyField.setAccessible(true);// 设置可获取
				String name = valueField.getName();
				if (!keyField.getType().equals(valueField.getType())) {// 若两边类型不一致
					Class<?> fieldClass = beanDescCls.getProp(name).getFieldClass();
					Object convert = Convert.convert(fieldClass, keyField.get(entity));
					beanDescCls.getSetter(name).invoke(newInstance, convert);
				} else {
					BeanUtil.setFieldValue(newInstance, name, keyField.get(entity));
				}
			}

			if (!onlyMapping) {
				CopyOptions copyOptions = CopyOptions.create()
						.setIgnoreProperties(fieldMapping.values().toArray(new String[] {})).setIgnoreNullValue(true);
				BeanUtil.copyProperties(entity, newInstance, copyOptions);
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new RuntimeException(e);
		}
		return newInstance;
	}
}
