package com.ebig.hdi.modules.coretransform.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * temp_spd_po
 * 
 * @author frink
 * @email 
 * @date 2019-06-24 21:27:53
 */
@TableName("spd_po")
public class TempSpdPoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 验收单标识
	 */
	@TableId
	private String poid;
	/**
	 * 库房科室标识
	 */
	private String storehouseid;
	/**
	 * U机构标识
	 */
	private String uorganid;
	/**
	 * 验收单编号
	 */
	private String pono;
	/**
	 * 验收单状态
	 */
	private BigDecimal postatus;
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
	 * 来源单号
	 */
	private String sourceno;
	/**
	 * 原始标识
	 */
	private String originid;
	/**
	 * 原始单号
	 */
	private String originno;
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
	 * 创建人ID
	 */
	private String cremanid;
	/**
	 * 创建人名称
	 */
	private String cremanname;

	/**
	 * 设置：验收单标识
	 */
	public void setPoid(String poid) {
		this.poid = poid;
	}
	/**
	 * 获取：验收单标识
	 */
	public String getPoid() {
		return poid;
	}
	/**
	 * 设置：库房科室标识
	 */
	public void setStorehouseid(String storehouseid) {
		this.storehouseid = storehouseid;
	}
	/**
	 * 获取：库房科室标识
	 */
	public String getStorehouseid() {
		return storehouseid;
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
	 * 设置：验收单编号
	 */
	public void setPono(String pono) {
		this.pono = pono;
	}
	/**
	 * 获取：验收单编号
	 */
	public String getPono() {
		return pono;
	}
	/**
	 * 设置：验收单状态
	 */
	public void setPostatus(BigDecimal postatus) {
		this.postatus = postatus;
	}
	/**
	 * 获取：验收单状态
	 */
	public BigDecimal getPostatus() {
		return postatus;
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
	 * 设置：来源单号
	 */
	public void setSourceno(String sourceno) {
		this.sourceno = sourceno;
	}
	/**
	 * 获取：来源单号
	 */
	public String getSourceno() {
		return sourceno;
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
	 * 设置：创建人ID
	 */
	public void setCremanid(String cremanid) {
		this.cremanid = cremanid;
	}
	/**
	 * 获取：创建人ID
	 */
	public String getCremanid() {
		return cremanid;
	}
	/**
	 * 设置：创建人名称
	 */
	public void setCremanname(String cremanname) {
		this.cremanname = cremanname;
	}
	/**
	 * 获取：创建人名称
	 */
	public String getCremanname() {
		return cremanname;
	}
}
