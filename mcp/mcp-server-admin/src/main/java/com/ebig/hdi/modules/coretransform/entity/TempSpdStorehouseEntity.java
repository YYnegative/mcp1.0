package com.ebig.hdi.modules.coretransform.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * spd_storehouse
 * 
 * @author frink
 * @email 
 * @date 2019-06-21 09:48:50
 */
@TableName("spd_storehouse")
public class TempSpdStorehouseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 库房科室标识
	 */
	@TableId
	private String storehouseid;
	/**
	 * U机构标识
	 */
	private String uorganid;
	/**
	 * 库房地址标识
	 */
	private String shaddressid;
	/**
	 * 库房科室编号
	 */
	private String storehouseno;
	/**
	 * 库房科室名称
	 */
	private String storehousename;
	/**
	 * 库房科室地址
	 */
	private String shaddress;
	/**
	 * 库房科室类型
	 */
	private BigDecimal storehousetype;
	/**
	 * 库房科室状态
	 */
	private BigDecimal storehousestatus;
	/**
	 * 上架流程
	 */
	private BigDecimal rackflow;
	/**
	 * 拣选流程
	 */
	private BigDecimal pickflow;
	/**
	 * 备货流程
	 */
	private BigDecimal preloadflow;
	/**
	 * 自动释放
	 */
	private BigDecimal autorelease;
	/**
	 * 创建人ID
	 */
	private String cremanid;
	/**
	 * 创建人名称
	 */
	private String cremanname;
	/**
	 * 创建时间
	 */
	private Date credate;
	/**
	 * 修改人ID
	 */
	private String editmanid;
	/**
	 * 修改人
	 */
	private String editmanname;
	/**
	 * 修改日期
	 */
	private Date editdate;
	/**
	 * 负责人
	 */
	private String mastername;
	/**
	 * 数据来源
	 */
	private BigDecimal datasource;
	/**
	 * 效期预警天数
	 */
	private Integer vwarndays;
	/**
	 * 备注
	 */
	private String memo;
	/**
	 * 库房助记码
	 */
	private String storehouseinfo;
	/**
	 * 原数据标识
	 */
	private String orgdataid;
	/**
	 * 消耗流程
	 */
	private BigDecimal receiveflow;
	/**
	 * 科室类别
	 */
	private BigDecimal storehousecategory;
	/**
	 * IP地址
	 */
	private String ipaddress;
	/**
	 * 自动分配
	 */
	private BigDecimal autoallocate;
	/**
	 * 库房标签
	 */
	private String lable;
	/**
	 * 上传下载标记，0上传 1下载
	 */
	private Integer udflag;

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
	 * 设置：库房科室编号
	 */
	public void setStorehouseno(String storehouseno) {
		this.storehouseno = storehouseno;
	}
	/**
	 * 获取：库房科室编号
	 */
	public String getStorehouseno() {
		return storehouseno;
	}
	/**
	 * 设置：库房科室名称
	 */
	public void setStorehousename(String storehousename) {
		this.storehousename = storehousename;
	}
	/**
	 * 获取：库房科室名称
	 */
	public String getStorehousename() {
		return storehousename;
	}
	/**
	 * 设置：库房科室地址
	 */
	public void setShaddress(String shaddress) {
		this.shaddress = shaddress;
	}
	/**
	 * 获取：库房科室地址
	 */
	public String getShaddress() {
		return shaddress;
	}
	/**
	 * 设置：库房科室类型
	 */
	public void setStorehousetype(BigDecimal storehousetype) {
		this.storehousetype = storehousetype;
	}
	/**
	 * 获取：库房科室类型
	 */
	public BigDecimal getStorehousetype() {
		return storehousetype;
	}
	/**
	 * 设置：库房科室状态
	 */
	public void setStorehousestatus(BigDecimal storehousestatus) {
		this.storehousestatus = storehousestatus;
	}
	/**
	 * 获取：库房科室状态
	 */
	public BigDecimal getStorehousestatus() {
		return storehousestatus;
	}
	/**
	 * 设置：上架流程
	 */
	public void setRackflow(BigDecimal rackflow) {
		this.rackflow = rackflow;
	}
	/**
	 * 获取：上架流程
	 */
	public BigDecimal getRackflow() {
		return rackflow;
	}
	/**
	 * 设置：拣选流程
	 */
	public void setPickflow(BigDecimal pickflow) {
		this.pickflow = pickflow;
	}
	/**
	 * 获取：拣选流程
	 */
	public BigDecimal getPickflow() {
		return pickflow;
	}
	/**
	 * 设置：备货流程
	 */
	public void setPreloadflow(BigDecimal preloadflow) {
		this.preloadflow = preloadflow;
	}
	/**
	 * 获取：备货流程
	 */
	public BigDecimal getPreloadflow() {
		return preloadflow;
	}
	/**
	 * 设置：自动释放
	 */
	public void setAutorelease(BigDecimal autorelease) {
		this.autorelease = autorelease;
	}
	/**
	 * 获取：自动释放
	 */
	public BigDecimal getAutorelease() {
		return autorelease;
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
	 * 设置：修改人ID
	 */
	public void setEditmanid(String editmanid) {
		this.editmanid = editmanid;
	}
	/**
	 * 获取：修改人ID
	 */
	public String getEditmanid() {
		return editmanid;
	}
	/**
	 * 设置：修改人
	 */
	public void setEditmanname(String editmanname) {
		this.editmanname = editmanname;
	}
	/**
	 * 获取：修改人
	 */
	public String getEditmanname() {
		return editmanname;
	}
	/**
	 * 设置：修改日期
	 */
	public void setEditdate(Date editdate) {
		this.editdate = editdate;
	}
	/**
	 * 获取：修改日期
	 */
	public Date getEditdate() {
		return editdate;
	}
	/**
	 * 设置：负责人
	 */
	public void setMastername(String mastername) {
		this.mastername = mastername;
	}
	/**
	 * 获取：负责人
	 */
	public String getMastername() {
		return mastername;
	}
	/**
	 * 设置：数据来源
	 */
	public void setDatasource(BigDecimal datasource) {
		this.datasource = datasource;
	}
	/**
	 * 获取：数据来源
	 */
	public BigDecimal getDatasource() {
		return datasource;
	}
	/**
	 * 设置：效期预警天数
	 */
	public void setVwarndays(Integer vwarndays) {
		this.vwarndays = vwarndays;
	}
	/**
	 * 获取：效期预警天数
	 */
	public Integer getVwarndays() {
		return vwarndays;
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
	 * 设置：库房助记码
	 */
	public void setStorehouseinfo(String storehouseinfo) {
		this.storehouseinfo = storehouseinfo;
	}
	/**
	 * 获取：库房助记码
	 */
	public String getStorehouseinfo() {
		return storehouseinfo;
	}
	/**
	 * 设置：原数据标识
	 */
	public void setOrgdataid(String orgdataid) {
		this.orgdataid = orgdataid;
	}
	/**
	 * 获取：原数据标识
	 */
	public String getOrgdataid() {
		return orgdataid;
	}
	/**
	 * 设置：消耗流程
	 */
	public void setReceiveflow(BigDecimal receiveflow) {
		this.receiveflow = receiveflow;
	}
	/**
	 * 获取：消耗流程
	 */
	public BigDecimal getReceiveflow() {
		return receiveflow;
	}
	/**
	 * 设置：科室类别
	 */
	public void setStorehousecategory(BigDecimal storehousecategory) {
		this.storehousecategory = storehousecategory;
	}
	/**
	 * 获取：科室类别
	 */
	public BigDecimal getStorehousecategory() {
		return storehousecategory;
	}
	/**
	 * 设置：IP地址
	 */
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}
	/**
	 * 获取：IP地址
	 */
	public String getIpaddress() {
		return ipaddress;
	}
	/**
	 * 设置：自动分配
	 */
	public void setAutoallocate(BigDecimal autoallocate) {
		this.autoallocate = autoallocate;
	}
	/**
	 * 获取：自动分配
	 */
	public BigDecimal getAutoallocate() {
		return autoallocate;
	}
	/**
	 * 设置：库房标签
	 */
	public void setLable(String lable) {
		this.lable = lable;
	}
	/**
	 * 获取：库房标签
	 */
	public String getLable() {
		return lable;
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
