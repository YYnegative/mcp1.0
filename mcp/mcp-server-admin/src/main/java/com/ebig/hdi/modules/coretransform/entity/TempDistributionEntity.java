package com.ebig.hdi.modules.coretransform.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author frink
 * @email 
 * @date 2019-06-18 09:55:25
 */
@TableName("temp_hdi_distribution")
public class TempDistributionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 配送数据标识
	 */
	@TableId
	private String distributionid;
	/**
	 * U机构标识
	 */
	private String uorganid;
	/**
	 * 医院机构标识
	 */
	private String horganid;
	/**
	 * U机构编号
	 */
	private String uorganno;
	/**
	 * U机构名称
	 */
	private String uorganname;
	/**
	 * 医院机构编号
	 */
	private String horganno;
	/**
	 * 医院机构名称
	 */
	private String horganname;
	/**
	 * 销售单标识
	 */
	private String salid;
	/**
	 * 销售单编号
	 */
	private String salno;
	/**
	 * 销售日期
	 */
	private Date saldate;
	/**
	 * 配送地址标识
	 */
	private String addressid;
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
	 * 设置：配送数据标识
	 */
	public void setDistributionid(String distributionid) {
		this.distributionid = distributionid;
	}
	/**
	 * 获取：配送数据标识
	 */
	public String getDistributionid() {
		return distributionid;
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
	 * 设置：销售单标识
	 */
	public void setSalid(String salid) {
		this.salid = salid;
	}
	/**
	 * 获取：销售单标识
	 */
	public String getSalid() {
		return salid;
	}
	/**
	 * 设置：销售单编号
	 */
	public void setSalno(String salno) {
		this.salno = salno;
	}
	/**
	 * 获取：销售单编号
	 */
	public String getSalno() {
		return salno;
	}
	/**
	 * 设置：销售日期
	 */
	public void setSaldate(Date saldate) {
		this.saldate = saldate;
	}
	/**
	 * 获取：销售日期
	 */
	public Date getSaldate() {
		return saldate;
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
