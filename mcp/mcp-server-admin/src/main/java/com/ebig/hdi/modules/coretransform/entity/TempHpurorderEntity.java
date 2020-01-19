package com.ebig.hdi.modules.coretransform.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 医院采购订单
 * 
 * @author frink
 * @email 
 * @date 2019-06-21 10:34:03
 */
@TableName("temp_hdi_hpurorder")
public class TempHpurorderEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 采购订单标识
	 */
	@TableId
	private String hpurorderid;
	/**
	 * 库房科室标识
	 */
	private String storehouseid;
	/**
	 * U机构标识
	 */
	private String uorganid;
	/**
	 * 采购计划编号
	 */
	private String purplanno;
	/**
	 * 医院机构标识
	 */
	private String horganid;
	/**
	 * 医院机构编号
	 */
	private String horganno;
	/**
	 * 医院机构名称
	 */
	private String horganname;
	/**
	 * 采购订单状态
	 */
	private BigDecimal purorderstatus;
	/**
	 * 来源标识
	 */
	private String sourceid;
	/**
	 * 总金额
	 */
	private BigDecimal totalmoney;
	/**
	 * 计划方式
	 */
	private BigDecimal purplanmode;
	/**
	 * 配送地址标识
	 */
	private String addressid;
	/**
	 * 创建时间
	 */
	private Date credate;
	/**
	 * 创建人ID
	 */
	private String cremanid;
	/**
	 * 创建人名称
	 */
	private String cremanname;
	/**
	 * 备注
	 */
	private String memo;
	/**
	 * 导出标志
	 */
	private BigDecimal exportflag;
	/**
	 * 预计到货时间
	 */
	private Date anticipate;
	/**
	 * 配送商确认时间
	 */
	private Date supplyconfirmdate;
	/**
	 * 跟台标志
	 */
	private BigDecimal stageflag;
	/**
	 * 上传下载标记，0上传 1下载
	 */
	private Integer udflag;

	/**
	 * 设置：采购订单标识
	 */
	public void setHpurorderid(String hpurorderid) {
		this.hpurorderid = hpurorderid;
	}
	/**
	 * 获取：采购订单标识
	 */
	public String getHpurorderid() {
		return hpurorderid;
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
	 * 设置：采购计划编号
	 */
	public void setPurplanno(String purplanno) {
		this.purplanno = purplanno;
	}
	/**
	 * 获取：采购计划编号
	 */
	public String getPurplanno() {
		return purplanno;
	}
	/**
	 * 设置：医院机构标识
	 */
	public void setHorganid(String horganid) {
		this.horganid = horganid;
	}
	/**
	 * 获取：医院机构标识
	 */
	public String getHorganid() {
		return horganid;
	}
	/**
	 * 设置：医院机构编号
	 */
	public void setHorganno(String horganno) {
		this.horganno = horganno;
	}
	/**
	 * 获取：医院机构编号
	 */
	public String getHorganno() {
		return horganno;
	}
	/**
	 * 设置：医院机构名称
	 */
	public void setHorganname(String horganname) {
		this.horganname = horganname;
	}
	/**
	 * 获取：医院机构名称
	 */
	public String getHorganname() {
		return horganname;
	}
	/**
	 * 设置：采购订单状态
	 */
	public void setPurorderstatus(BigDecimal purorderstatus) {
		this.purorderstatus = purorderstatus;
	}
	/**
	 * 获取：采购订单状态
	 */
	public BigDecimal getPurorderstatus() {
		return purorderstatus;
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
	 * 设置：总金额
	 */
	public void setTotalmoney(BigDecimal totalmoney) {
		this.totalmoney = totalmoney;
	}
	/**
	 * 获取：总金额
	 */
	public BigDecimal getTotalmoney() {
		return totalmoney;
	}
	/**
	 * 设置：计划方式
	 */
	public void setPurplanmode(BigDecimal purplanmode) {
		this.purplanmode = purplanmode;
	}
	/**
	 * 获取：计划方式
	 */
	public BigDecimal getPurplanmode() {
		return purplanmode;
	}
	/**
	 * 设置：配送地址标识
	 */
	public void setAddressid(String addressid) {
		this.addressid = addressid;
	}
	/**
	 * 获取：配送地址标识
	 */
	public String getAddressid() {
		return addressid;
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
	 * 设置：导出标志
	 */
	public void setExportflag(BigDecimal exportflag) {
		this.exportflag = exportflag;
	}
	/**
	 * 获取：导出标志
	 */
	public BigDecimal getExportflag() {
		return exportflag;
	}
	/**
	 * 设置：预计到货时间
	 */
	public void setAnticipate(Date anticipate) {
		this.anticipate = anticipate;
	}
	/**
	 * 获取：预计到货时间
	 */
	public Date getAnticipate() {
		return anticipate;
	}
	/**
	 * 设置：配送商确认时间
	 */
	public void setSupplyconfirmdate(Date supplyconfirmdate) {
		this.supplyconfirmdate = supplyconfirmdate;
	}
	/**
	 * 获取：配送商确认时间
	 */
	public Date getSupplyconfirmdate() {
		return supplyconfirmdate;
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
