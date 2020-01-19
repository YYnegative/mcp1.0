package com.ebig.hdi.modules.coretransform.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 采购退货单
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-10-21 15:10:37
 */
@TableName("spd_purback")
public class SpdPurbackEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 采退单标识
	 */
	@TableId
	private String purbackid;
	/**
	 * 采退单单号
	 */
	private String purbackno;
	/**
	 * 配送商
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
	 * 库房科室
	 */
	private String storehouseid;
	/**
	 * 工作流状态
	 */
	private BigDecimal workflowstatus;
	/**
	 * 采退单状态
	 */
	private BigDecimal purbackstatus;
	/**
	 * 制单人ID
	 */
	private String cremanid;
	/**
	 * 制单人
	 */
	private String cremanname;
	/**
	 * 制单时间
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
	 * 修改时间
	 */
	private Date editdate;
	/**
	 * 审核人ID
	 */
	private String auditmanid;
	/**
	 * 审核人
	 */
	private String auditmanname;
	/**
	 * 审核时间
	 */
	private Date auditdate;
	/**
	 * 作废人ID
	 */
	private String revokerid;
	/**
	 * 作废人
	 */
	private String revoker;
	/**
	 * 作废时间
	 */
	private Date revoketime;
	/**
	 * 备注
	 */
	private String memo;
	/**
	 * 医院机构
	 */
	private String uorganid;
	/**
	 * 工作流实例
	 */
	private String pinstanceid;
	/**
	 * 来源标识
	 */
	private String sourceid;
	/**
	 * 来源单号
	 */
	private String sourceno;
	/**
	 * 打印标志
	 */
	private BigDecimal printflag;
	/**
	 * 打印时间
	 */
	private Date printtime;
	/**
	 * 打印人ID
	 */
	private String printmanid;
	/**
	 * 打印人
	 */
	private String printmanname;
	/**
	 * 退货人编码
	 */
	private String revokemanid;
	/**
	 * 退货人名称
	 */
	private String revokemanname;
	/**
	 * 销退生成标志
	 */
	private BigDecimal gensalback;
	/**
	 * 打印次数
	 */
	private Integer printtimes;
	/**
	 * 原数据
	 */
	private String orgdataid;
	/**
	 * 采退单类型
	 */
	private String purbacktype;
	/**
	 * 上传下载标记，0上传 1下载
	 */
	private Integer udflag;

	/**
	 * 设置：采退单标识
	 */
	public void setPurbackid(String purbackid) {
		this.purbackid = purbackid;
	}
	/**
	 * 获取：采退单标识
	 */
	public String getPurbackid() {
		return purbackid;
	}
	/**
	 * 设置：采退单单号
	 */
	public void setPurbackno(String purbackno) {
		this.purbackno = purbackno;
	}
	/**
	 * 获取：采退单单号
	 */
	public String getPurbackno() {
		return purbackno;
	}
	/**
	 * 设置：配送商
	 */
	public void setSupplyid(String supplyid) {
		this.supplyid = supplyid;
	}
	/**
	 * 获取：配送商
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
	 * 设置：库房科室
	 */
	public void setStorehouseid(String storehouseid) {
		this.storehouseid = storehouseid;
	}
	/**
	 * 获取：库房科室
	 */
	public String getStorehouseid() {
		return storehouseid;
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
	 * 设置：采退单状态
	 */
	public void setPurbackstatus(BigDecimal purbackstatus) {
		this.purbackstatus = purbackstatus;
	}
	/**
	 * 获取：采退单状态
	 */
	public BigDecimal getPurbackstatus() {
		return purbackstatus;
	}
	/**
	 * 设置：制单人ID
	 */
	public void setCremanid(String cremanid) {
		this.cremanid = cremanid;
	}
	/**
	 * 获取：制单人ID
	 */
	public String getCremanid() {
		return cremanid;
	}
	/**
	 * 设置：制单人
	 */
	public void setCremanname(String cremanname) {
		this.cremanname = cremanname;
	}
	/**
	 * 获取：制单人
	 */
	public String getCremanname() {
		return cremanname;
	}
	/**
	 * 设置：制单时间
	 */
	public void setCredate(Date credate) {
		this.credate = credate;
	}
	/**
	 * 获取：制单时间
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
	 * 设置：修改时间
	 */
	public void setEditdate(Date editdate) {
		this.editdate = editdate;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getEditdate() {
		return editdate;
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
	 * 设置：作废人ID
	 */
	public void setRevokerid(String revokerid) {
		this.revokerid = revokerid;
	}
	/**
	 * 获取：作废人ID
	 */
	public String getRevokerid() {
		return revokerid;
	}
	/**
	 * 设置：作废人
	 */
	public void setRevoker(String revoker) {
		this.revoker = revoker;
	}
	/**
	 * 获取：作废人
	 */
	public String getRevoker() {
		return revoker;
	}
	/**
	 * 设置：作废时间
	 */
	public void setRevoketime(Date revoketime) {
		this.revoketime = revoketime;
	}
	/**
	 * 获取：作废时间
	 */
	public Date getRevoketime() {
		return revoketime;
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
	 * 设置：医院机构
	 */
	public void setUorganid(String uorganid) {
		this.uorganid = uorganid;
	}
	/**
	 * 获取：医院机构
	 */
	public String getUorganid() {
		return uorganid;
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
	 * 设置：打印标志
	 */
	public void setPrintflag(BigDecimal printflag) {
		this.printflag = printflag;
	}
	/**
	 * 获取：打印标志
	 */
	public BigDecimal getPrintflag() {
		return printflag;
	}
	/**
	 * 设置：打印时间
	 */
	public void setPrinttime(Date printtime) {
		this.printtime = printtime;
	}
	/**
	 * 获取：打印时间
	 */
	public Date getPrinttime() {
		return printtime;
	}
	/**
	 * 设置：打印人ID
	 */
	public void setPrintmanid(String printmanid) {
		this.printmanid = printmanid;
	}
	/**
	 * 获取：打印人ID
	 */
	public String getPrintmanid() {
		return printmanid;
	}
	/**
	 * 设置：打印人
	 */
	public void setPrintmanname(String printmanname) {
		this.printmanname = printmanname;
	}
	/**
	 * 获取：打印人
	 */
	public String getPrintmanname() {
		return printmanname;
	}
	/**
	 * 设置：退货人编码
	 */
	public void setRevokemanid(String revokemanid) {
		this.revokemanid = revokemanid;
	}
	/**
	 * 获取：退货人编码
	 */
	public String getRevokemanid() {
		return revokemanid;
	}
	/**
	 * 设置：退货人名称
	 */
	public void setRevokemanname(String revokemanname) {
		this.revokemanname = revokemanname;
	}
	/**
	 * 获取：退货人名称
	 */
	public String getRevokemanname() {
		return revokemanname;
	}
	/**
	 * 设置：销退生成标志
	 */
	public void setGensalback(BigDecimal gensalback) {
		this.gensalback = gensalback;
	}
	/**
	 * 获取：销退生成标志
	 */
	public BigDecimal getGensalback() {
		return gensalback;
	}
	/**
	 * 设置：打印次数
	 */
	public void setPrinttimes(Integer printtimes) {
		this.printtimes = printtimes;
	}
	/**
	 * 获取：打印次数
	 */
	public Integer getPrinttimes() {
		return printtimes;
	}
	/**
	 * 设置：原数据
	 */
	public void setOrgdataid(String orgdataid) {
		this.orgdataid = orgdataid;
	}
	/**
	 * 获取：原数据
	 */
	public String getOrgdataid() {
		return orgdataid;
	}
	/**
	 * 设置：采退单类型
	 */
	public void setPurbacktype(String purbacktype) {
		this.purbacktype = purbacktype;
	}
	/**
	 * 获取：采退单类型
	 */
	public String getPurbacktype() {
		return purbacktype;
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
