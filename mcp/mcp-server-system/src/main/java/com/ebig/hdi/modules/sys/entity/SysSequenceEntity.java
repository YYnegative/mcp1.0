package com.ebig.hdi.modules.sys.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 系统序列
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-04-24 13:06:34
 */
@Data
@TableName("sys_sequence")
public class SysSequenceEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Long id;
	/**
	 * 序列编码
	 */
	private String seqCode;
	/**
	 * 序列名称
	 */
	private String seqName;
	/**
	 * 序列前缀
	 */
	private String seqPrefix;
	/**
	 * 格式化长度
	 */
	private Integer formatLength;
	/**
	 * 当前值
	 */
	private Long currentVal;

}
