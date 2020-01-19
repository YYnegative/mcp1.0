package com.ebig.hdi.modules.job.etl;

/**
 * 常量类
 * 
 * @author zack
 *
 */
public final class HdiEtlConstants {

	/**
	 * 数据源名称
	 */
	public static final String DATASOURCE_NAME = "hdi";

	/**
	 * 上传标记值
	 */
	public static final int UD_FLAG_UPLOAD = 0;
	
	/**
	 * 下载标记值
	 */
	public static final int UD_FLAG_DOWNLOAD = 1;
	
	/**
	 * 开始时间参数
	 */
	public static final String START_DATE = "startDate";
	
	/**
	 * 结束时间参数
	 */
	public static final String END_DATE = "endDate";
	
	/**
	 * U机构参数
	 */
	public static final String UID = "uid";
	
	/**
	 * 临时表标记
	 */
	public static final String TEMP_FLAG = "temp_";
	
	/**
	 * 根据ID查询目标记录
	 */
	public static final String FINDRECORDBYID_SQL = "SELECT * FROM %s WHERE 1=1 %s";
	
	/**
	 * 插入
	 */
	public static final String INSERT_SQL = "INSERT INTO %s (%s) VALUES (%s)";
	
	/**
	 * 更新 
	 */
	public static final String UPDATE_SQL = "UPDATE %s SET %s WHERE 1=1 %s";

}
