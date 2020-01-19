package com.ebig.hdi.common.utils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 表与实体操作工具类
 * @author zack
 *
 */
public class TableEntityUtils {
	
	/**
	 * 根据ResultSet获得map对象
	 * @param entityName
	 * @return
	 */
	public static Map<String,Object> getMapByResultSet(ResultSet resultSet){
		Map<String,Object> map = new LinkedHashMap<>();
		try {
			ResultSetMetaData metaData = resultSet.getMetaData();
			for (int i = 1; i <= metaData.getColumnCount(); i++) {
				map.put(metaData.getColumnName(i), resultSet.getObject(i));
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return map;
	}
}
