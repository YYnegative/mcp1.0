package com.ebig.hdi.modules.job.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 统一机构
 * 
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-06-18 11:04:28
 */
@TableName("pub_ucompany")
public class TempPubUcompanyEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 机构ID
	 */
	@TableId
	private String ucompanyid;
	/**
	 * 机构编号
	 */
	private String ucompanyno;
	/**
	 * 机构名称
	 */
	private String ucompanyname;
	/**
	 * 机构拼音
	 */
	private String ucompanypy;
	/**
	 * 机构简称
	 */
	private String ushortname;
	/**
	 * 机构类型
	 */
	private BigDecimal ucompanytype;
	/**
	 * 机构概况
	 */
	private String ucompanysum;
	/**
	 * 机构操作码
	 */
	private String ucompanycode;
	/**
	 * 税号
	 */
	private String taxnamber;
	/**
	 * 法人
	 */
	private String legalpersion;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 邮编
	 */
	private String postcode;
	/**
	 * 联系人
	 */
	private String linkman;
	/**
	 * 联系电话
	 */
	private String linktel;
	/**
	 * 联系手机
	 */
	private String linkmobile;
	/**
	 * 传真
	 */
	private String fax;
	/**
	 * 经营范围
	 */
	private String managerange;
	/**
	 * 网址
	 */
	private String webaddr;
	/**
	 * GMP标志
	 */
	private BigDecimal gmpflag;
	/**
	 * GSP标志
	 */
	private BigDecimal gspflag;
	/**
	 * ISO认证
	 */
	private BigDecimal isoflag;
	/**
	 * 企业性质
	 */
	private BigDecimal corptype;
	/**
	 * 盈利性质
	 */
	private BigDecimal customnature;
	/**
	 * 经济性质
	 */
	private BigDecimal economytype;
	/**
	 * 质量认证
	 */
	private BigDecimal qualitytype;
	/**
	 * 关联性质
	 */
	private BigDecimal relationtype;
	/**
	 * 企业类型
	 */
	private String comtype;
	/**
	 * 行政区域ID
	 */
	private BigDecimal companyzoneid;
	/**
	 * 行政区域
	 */
	private String companyzone;
	/**
	 * 工作流实例
	 */
	private String pinstanceid;
	/**
	 * 工作流状态
	 */
	private BigDecimal workflowstatus;
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
	 * 审核时间
	 */
	private Date auditdate;
	/**
	 * 审核人ID
	 */
	private String auditmanid;
	/**
	 * 审核人
	 */
	private String auditmanname;
	/**
	 * 修改日期
	 */
	private Date editdate;
	/**
	 * 修改人ID
	 */
	private String editmanid;
	/**
	 * 修改人
	 */
	private String editmanname;
	/**
	 * 备注
	 */
	private String memo;
	/**
	 * 机构信息
	 */
	private String ucompanyinfo;
	/**
	 * UDF1
	 */
	private String udf1;
	/**
	 * UDF2
	 */
	private String udf2;
	/**
	 * UDF3
	 */
	private String udf3;
	/**
	 * UDF4
	 */
	private String udf4;
	/**
	 * UDF5
	 */
	private String udf5;
	/**
	 * 状态
	 */
	private BigDecimal usestatus;
	/**
	 * 资料齐全标志
	 */
	private BigDecimal qccontrolflag;
	/**
	 * 父单位ID
	 */
	private String pucompanyid;
	/**
	 * 上传下载标记，0上传 1下载
	 */
	private Integer udflag;

	/**
	 * 设置：机构ID
	 */
	public void setUcompanyid(String ucompanyid) {
		this.ucompanyid = ucompanyid;
	}
	/**
	 * 获取：机构ID
	 */
	public String getUcompanyid() {
		return ucompanyid;
	}
	/**
	 * 设置：机构编号
	 */
	public void setUcompanyno(String ucompanyno) {
		this.ucompanyno = ucompanyno;
	}
	/**
	 * 获取：机构编号
	 */
	public String getUcompanyno() {
		return ucompanyno;
	}
	/**
	 * 设置：机构名称
	 */
	public void setUcompanyname(String ucompanyname) {
		this.ucompanyname = ucompanyname;
	}
	/**
	 * 获取：机构名称
	 */
	public String getUcompanyname() {
		return ucompanyname;
	}
	/**
	 * 设置：机构拼音
	 */
	public void setUcompanypy(String ucompanypy) {
		this.ucompanypy = ucompanypy;
	}
	/**
	 * 获取：机构拼音
	 */
	public String getUcompanypy() {
		return ucompanypy;
	}
	/**
	 * 设置：机构简称
	 */
	public void setUshortname(String ushortname) {
		this.ushortname = ushortname;
	}
	/**
	 * 获取：机构简称
	 */
	public String getUshortname() {
		return ushortname;
	}
	/**
	 * 设置：机构类型
	 */
	public void setUcompanytype(BigDecimal ucompanytype) {
		this.ucompanytype = ucompanytype;
	}
	/**
	 * 获取：机构类型
	 */
	public BigDecimal getUcompanytype() {
		return ucompanytype;
	}
	/**
	 * 设置：机构概况
	 */
	public void setUcompanysum(String ucompanysum) {
		this.ucompanysum = ucompanysum;
	}
	/**
	 * 获取：机构概况
	 */
	public String getUcompanysum() {
		return ucompanysum;
	}
	/**
	 * 设置：机构操作码
	 */
	public void setUcompanycode(String ucompanycode) {
		this.ucompanycode = ucompanycode;
	}
	/**
	 * 获取：机构操作码
	 */
	public String getUcompanycode() {
		return ucompanycode;
	}
	/**
	 * 设置：税号
	 */
	public void setTaxnamber(String taxnamber) {
		this.taxnamber = taxnamber;
	}
	/**
	 * 获取：税号
	 */
	public String getTaxnamber() {
		return taxnamber;
	}
	/**
	 * 设置：法人
	 */
	public void setLegalpersion(String legalpersion) {
		this.legalpersion = legalpersion;
	}
	/**
	 * 获取：法人
	 */
	public String getLegalpersion() {
		return legalpersion;
	}
	/**
	 * 设置：地址
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * 获取：地址
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * 设置：邮编
	 */
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	/**
	 * 获取：邮编
	 */
	public String getPostcode() {
		return postcode;
	}
	/**
	 * 设置：联系人
	 */
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	/**
	 * 获取：联系人
	 */
	public String getLinkman() {
		return linkman;
	}
	/**
	 * 设置：联系电话
	 */
	public void setLinktel(String linktel) {
		this.linktel = linktel;
	}
	/**
	 * 获取：联系电话
	 */
	public String getLinktel() {
		return linktel;
	}
	/**
	 * 设置：联系手机
	 */
	public void setLinkmobile(String linkmobile) {
		this.linkmobile = linkmobile;
	}
	/**
	 * 获取：联系手机
	 */
	public String getLinkmobile() {
		return linkmobile;
	}
	/**
	 * 设置：传真
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}
	/**
	 * 获取：传真
	 */
	public String getFax() {
		return fax;
	}
	/**
	 * 设置：经营范围
	 */
	public void setManagerange(String managerange) {
		this.managerange = managerange;
	}
	/**
	 * 获取：经营范围
	 */
	public String getManagerange() {
		return managerange;
	}
	/**
	 * 设置：网址
	 */
	public void setWebaddr(String webaddr) {
		this.webaddr = webaddr;
	}
	/**
	 * 获取：网址
	 */
	public String getWebaddr() {
		return webaddr;
	}
	/**
	 * 设置：GMP标志
	 */
	public void setGmpflag(BigDecimal gmpflag) {
		this.gmpflag = gmpflag;
	}
	/**
	 * 获取：GMP标志
	 */
	public BigDecimal getGmpflag() {
		return gmpflag;
	}
	/**
	 * 设置：GSP标志
	 */
	public void setGspflag(BigDecimal gspflag) {
		this.gspflag = gspflag;
	}
	/**
	 * 获取：GSP标志
	 */
	public BigDecimal getGspflag() {
		return gspflag;
	}
	/**
	 * 设置：ISO认证
	 */
	public void setIsoflag(BigDecimal isoflag) {
		this.isoflag = isoflag;
	}
	/**
	 * 获取：ISO认证
	 */
	public BigDecimal getIsoflag() {
		return isoflag;
	}
	/**
	 * 设置：企业性质
	 */
	public void setCorptype(BigDecimal corptype) {
		this.corptype = corptype;
	}
	/**
	 * 获取：企业性质
	 */
	public BigDecimal getCorptype() {
		return corptype;
	}
	/**
	 * 设置：盈利性质
	 */
	public void setCustomnature(BigDecimal customnature) {
		this.customnature = customnature;
	}
	/**
	 * 获取：盈利性质
	 */
	public BigDecimal getCustomnature() {
		return customnature;
	}
	/**
	 * 设置：经济性质
	 */
	public void setEconomytype(BigDecimal economytype) {
		this.economytype = economytype;
	}
	/**
	 * 获取：经济性质
	 */
	public BigDecimal getEconomytype() {
		return economytype;
	}
	/**
	 * 设置：质量认证
	 */
	public void setQualitytype(BigDecimal qualitytype) {
		this.qualitytype = qualitytype;
	}
	/**
	 * 获取：质量认证
	 */
	public BigDecimal getQualitytype() {
		return qualitytype;
	}
	/**
	 * 设置：关联性质
	 */
	public void setRelationtype(BigDecimal relationtype) {
		this.relationtype = relationtype;
	}
	/**
	 * 获取：关联性质
	 */
	public BigDecimal getRelationtype() {
		return relationtype;
	}
	/**
	 * 设置：企业类型
	 */
	public void setComtype(String comtype) {
		this.comtype = comtype;
	}
	/**
	 * 获取：企业类型
	 */
	public String getComtype() {
		return comtype;
	}
	/**
	 * 设置：行政区域ID
	 */
	public void setCompanyzoneid(BigDecimal companyzoneid) {
		this.companyzoneid = companyzoneid;
	}
	/**
	 * 获取：行政区域ID
	 */
	public BigDecimal getCompanyzoneid() {
		return companyzoneid;
	}
	/**
	 * 设置：行政区域
	 */
	public void setCompanyzone(String companyzone) {
		this.companyzone = companyzone;
	}
	/**
	 * 获取：行政区域
	 */
	public String getCompanyzone() {
		return companyzone;
	}
	/**
	 * 设置：工作流实例
	 */
	public void setPinstanceid(String pinstanceid) {
		this.pinstanceid = pinstanceid;
	}
	/**
	 * 获取：工作流实例
	 */
	public String getPinstanceid() {
		return pinstanceid;
	}
	/**
	 * 设置：工作流状态
	 */
	public void setWorkflowstatus(BigDecimal workflowstatus) {
		this.workflowstatus = workflowstatus;
	}
	/**
	 * 获取：工作流状态
	 */
	public BigDecimal getWorkflowstatus() {
		return workflowstatus;
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
	 * 设置：审核时间
	 */
	public void setAuditdate(Date auditdate) {
		this.auditdate = auditdate;
	}
	/**
	 * 获取：审核时间
	 */
	public Date getAuditdate() {
		return auditdate;
	}
	/**
	 * 设置：审核人ID
	 */
	public void setAuditmanid(String auditmanid) {
		this.auditmanid = auditmanid;
	}
	/**
	 * 获取：审核人ID
	 */
	public String getAuditmanid() {
		return auditmanid;
	}
	/**
	 * 设置：审核人
	 */
	public void setAuditmanname(String auditmanname) {
		this.auditmanname = auditmanname;
	}
	/**
	 * 获取：审核人
	 */
	public String getAuditmanname() {
		return auditmanname;
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
	 * 设置：机构信息
	 */
	public void setUcompanyinfo(String ucompanyinfo) {
		this.ucompanyinfo = ucompanyinfo;
	}
	/**
	 * 获取：机构信息
	 */
	public String getUcompanyinfo() {
		return ucompanyinfo;
	}
	/**
	 * 设置：UDF1
	 */
	public void setUdf1(String udf1) {
		this.udf1 = udf1;
	}
	/**
	 * 获取：UDF1
	 */
	public String getUdf1() {
		return udf1;
	}
	/**
	 * 设置：UDF2
	 */
	public void setUdf2(String udf2) {
		this.udf2 = udf2;
	}
	/**
	 * 获取：UDF2
	 */
	public String getUdf2() {
		return udf2;
	}
	/**
	 * 设置：UDF3
	 */
	public void setUdf3(String udf3) {
		this.udf3 = udf3;
	}
	/**
	 * 获取：UDF3
	 */
	public String getUdf3() {
		return udf3;
	}
	/**
	 * 设置：UDF4
	 */
	public void setUdf4(String udf4) {
		this.udf4 = udf4;
	}
	/**
	 * 获取：UDF4
	 */
	public String getUdf4() {
		return udf4;
	}
	/**
	 * 设置：UDF5
	 */
	public void setUdf5(String udf5) {
		this.udf5 = udf5;
	}
	/**
	 * 获取：UDF5
	 */
	public String getUdf5() {
		return udf5;
	}
	/**
	 * 设置：状态
	 */
	public void setUsestatus(BigDecimal usestatus) {
		this.usestatus = usestatus;
	}
	/**
	 * 获取：状态
	 */
	public BigDecimal getUsestatus() {
		return usestatus;
	}
	/**
	 * 设置：资料齐全标志
	 */
	public void setQccontrolflag(BigDecimal qccontrolflag) {
		this.qccontrolflag = qccontrolflag;
	}
	/**
	 * 获取：资料齐全标志
	 */
	public BigDecimal getQccontrolflag() {
		return qccontrolflag;
	}
	/**
	 * 设置：父单位ID
	 */
	public void setPucompanyid(String pucompanyid) {
		this.pucompanyid = pucompanyid;
	}
	/**
	 * 获取：父单位ID
	 */
	public String getPucompanyid() {
		return pucompanyid;
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
