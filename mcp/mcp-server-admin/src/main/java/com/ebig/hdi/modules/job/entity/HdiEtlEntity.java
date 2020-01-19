package com.ebig.hdi.modules.job.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;
import lombok.ToString;

@TableName("hdiEtlEntity")
@Data
@ToString
public class HdiEtlEntity {

	/**
	 * 主键
	 */
	@TableId
	private Long id;
	
	/**
	 * 名称
	 */
	private String name;
	
	/**
	 * 表名称
	 */
	private String tableName;
	
	/**
	 * SQL语句,其中:startDate为开始时间,:endDate是结束时间
	 */
	private String configSql;
	
	/**
	 * 优先级
	 */
	private int priority;
	
	/**
	 * 启动时间
	 */
	private Long beginTime;
	
	/**
	 * 事务延迟时间，以毫秒为单位
	 */
	private Long tsDelay;
	
	/**
	 * 上传下载标记,0上传，1下载
	 */
	private int udflag;
	
	/**
	 * 机构数据
	 */
	private String uid;

}
