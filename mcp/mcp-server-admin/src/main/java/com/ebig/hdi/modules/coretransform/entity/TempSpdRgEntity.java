package com.ebig.hdi.modules.coretransform.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * spd_rg
 * 
 * @author frink
 * @email 
 * @date 2019-06-24 09:41:32
 */
@TableName("spd_rg")
public class TempSpdRgEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 收货单标识
	 */
	@TableId(type = IdType.INPUT)
	private String rgid;
	/**
	 * U机构标识
	 */
	private String uorganid;
	/**
	 * 收货单编号
	 */
	private String rgno;
	/**
	 * 收货状态
	 */
	private BigDecimal rgstatus;
	/**
	 * 收货类型
	 */
	private BigDecimal rgtype;
	/**
	 * U机构编号
	 */
	private String uorganno;
	/**
	 * U机构名称
	 */
	private String uorganname;
	/**
	 * 配送商标识
	 */
	private String supplyid;
	/**
	 * 配送商编号
	 */
	private String supplyno;
	/**
	 * 配送商名称
	 */
	private String supplyname;
	/**
	 * 来源标识
	 */
	private String sourceid;
	/**
	 * 原始标识
	 */
	private String originid;
	/**
	 * 原始单号
	 */
	private String originno;
	/**
	 * 库房地址标识
	 */
	private String shaddressid;
	/**
	 * 创建时间
	 */
	private Date credate;
	/**
	 * 备注
	 */
	private String memo;
	/**
	 * 跟台标志
	 */
	private BigDecimal stageflag;
	/**
	 * 上传下载标记，0上传 1下载
	 */
	private Integer udflag;

	/**
	 * 设置：收货单标识
	 */
	public void setRgid(String rgid) {
		this.rgid = rgid;
	}
	/**
	 * 获取：收货单标识
	 */
	public String getRgid() {
		return rgid;
	}
	/**
	 * 设置：U机构标识
	 */
	public void setUorganid(String uorganid) {
		this.uorganid = uorganid;
	}
	/**
	 * 获取：U机构标识
	 */
	public String getUorganid() {
		return uorganid;
	}
	/**
	 * 设置：收货单编号
	 */
	public void setRgno(String rgno) {
		this.rgno = rgno;
	}
	/**
	 * 获取：收货单编号
	 */
	public String getRgno() {
		return rgno;
	}
	/**
	 * 设置：收货状态
	 */
	public void setRgstatus(BigDecimal rgstatus) {
		this.rgstatus = rgstatus;
	}
	/**
	 * 获取：收货状态
	 */
	public BigDecimal getRgstatus() {
		return rgstatus;
	}
	/**
	 * 设置：收货类型
	 */
	public void setRgtype(BigDecimal rgtype) {
		this.rgtype = rgtype;
	}
	/**
	 * 获取：收货类型
	 */
	public BigDecimal getRgtype() {
		return rgtype;
	}
	/**
	 * 设置：U机构编号
	 */
	public void setUorganno(String uorganno) {
		this.uorganno = uorganno;
	}
	/**
	 * 获取：U机构编号
	 */
	public String getUorganno() {
		return uorganno;
	}
	/**
	 * 设置：U机构名称
	 */
	public void setUorganname(String uorganname) {
		this.uorganname = uorganname;
	}
	/**
	 * 获取：U机构名称
	 */
	public String getUorganname() {
		return uorganname;
	}
	/**
	 * 设置：配送商标识
	 */
	public void setSupplyid(String supplyid) {
		this.supplyid = supplyid;
	}
	/**
	 * 获取：配送商标识
	 */
	public String getSupplyid() {
		return supplyid;
	}
	/**
	 * 设置：配送商编号
	 */
	public void setSupplyno(String supplyno) {
		this.supplyno = supplyno;
	}
	/**
	 * 获取：配送商编号
	 */
	public String getSupplyno() {
		return supplyno;
	}
	/**
	 * 设置：配送商名称
	 */
	public void setSupplyname(String supplyname) {
		this.supplyname = supplyname;
	}
	/**
	 * 获取：配送商名称
	 */
	public String getSupplyname() {
		return supplyname;
	}
	/**
	 * 设置：来源标识
	 */
	public void setSourceid(String sourceid) {
		this.sourceid = sourceid;
	}
	/**
	 * 获取：来源标识
	 */
	public String getSourceid() {
		return sourceid;
	}
	/**
	 * 设置：原始标识
	 */
	public void setOriginid(String originid) {
		this.originid = originid;
	}
	/**
	 * 获取：原始标识
	 */
	public String getOriginid() {
		return originid;
	}
	/**
	 * 设置：原始单号
	 */
	public void setOriginno(String originno) {
		this.originno = originno;
	}
	/**
	 * 获取：原始单号
	 */
	public String getOriginno() {
		return originno;
	}
	/**
	 * 设置：库房地址标识
	 */
	public void setShaddressid(String shaddressid) {
		this.shaddressid = shaddressid;
	}
	/**
	 * 获取：库房地址标识
	 */
	public String getShaddressid() {
		return shaddressid;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCredate(Date credate) {
		this.credate = credate;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCredate() {
		return credate;
	}
	/**
	 * 设置：备注
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}
	/**
	 * 获取：备注
	 */
	public String getMemo() {
		return memo;
	}
	/**
	 * 设置：跟台标志
	 */
	public void setStageflag(BigDecimal stageflag) {
		this.stageflag = stageflag;
	}
	/**
	 * 获取：跟台标志
	 */
	public BigDecimal getStageflag() {
		return stageflag;
	}
	/**
	 * 设置：上传下载标记，0上传 1下载
	 */
	public void setUdflag(Integer udflag) {
		this.udflag = udflag;
	}
	/**
	 * 获取：上传下载标记，0上传 1下载
	 */
	public Integer getUdflag() {
		return udflag;
	}
}
