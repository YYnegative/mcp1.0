package com.ebig.hdi.modules.core.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 
 * 
 * @author frink
 * @email 
 * @date 2019-05-25 12:01:14
 */
@Data
@TableName("hdi_core_lot")
public class CoreLotEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 批号标识
	 */
	@TableId
	private Long lotid;
	/**
	 * 机构标识
	 */
	private Long deptId;
	/**
	 * 供应商id
	 */
	private Long supplierId;
	/**
	 * 商品标识
	 */
	private Long goodsid;
	/**
	 * 1 药品 2 试剂 3 耗材
	 */
	private Integer goodsclass;
	/**
	 * 商品规格标识
	 */
	private Long goodstypeid;
	/**
	 * 批号类型 1生产批号 2灭菌批号
	 */
	private Integer lottype;
	/**
	 * 批号状态0 停用 1启用
	 */
	private Integer lotstatus;
	/**
	 * 批号
	 */
	private String lotno;
	/**
	 * 生产日期
	 */
	private Date proddate;
	/**
	 * 失效日期
	 */
	private Date invadate;
	/**
	 * 0否 -1是
	 */
	private Integer delFlag;
	
	
	/**
	 * 采购商品名称
	 */
	@TableField(exist=false)
	private String goodsName;
	/**
	 * 生产厂家名称
	 */
	@TableField(exist=false)
	private String factoryName;
	/**
	 * 生产厂家id
	 */
	@TableField(exist=false)
	private Integer factoryId;
	/**
	 * 商品规格
	 */
	@TableField(exist=false)
	private String specs;


	
	/**
	 * 设置：生产日期
	 */
	public void setProddate(Date proddate) {
		this.proddate = proddate;
	}
	/**
	 * 获取：生产日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	public Date getProddate() {
		return proddate;
	}
	/**
	 * 设置：失效日期
	 */
	public void setInvadate(Date invadate) {
		this.invadate = invadate;
	}
	/**
	 * 获取：失效日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	public Date getInvadate() {
		return invadate;
	}
}
