package com.ebig.hdi.modules.coretransform.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * temp_spd_podtl
 * 
 * @author frink
 * @email 
 * @date 2019-06-24 21:24:46
 */
@TableName("spd_podtl")
public class TempSpdPodtlEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 验收明细标识
	 */
	@TableId
	private String podtlid;
	/**
	 * 验收单标识
	 */
	private String poid;
	/**
	 * 批次标识
	 */
	private String batchid;
	/**
	 * U机构标识
	 */
	private String uorganid;
	/**
	 * 来源标识
	 */
	private String sourceid;
	/**
	 * 来源单号
	 */
	private String sourceno;
	/**
	 * 来源明细标识
	 */
	private String sourcedtlid;
	/**
	 * 原始标识
	 */
	private String originid;
	/**
	 * 原始单号
	 */
	private String originno;
	/**
	 * 医院商品编码
	 */
	private String hgoodsno;
	/**
	 * 医院商品名称
	 */
	private String hgoodsname;
	/**
	 * 医院单价
	 */
	private BigDecimal hunitprice;
	/**
	 * 医院数量
	 */
	private BigDecimal hgoodsqty;
	/**
	 * 医院实收数量
	 */
	private BigDecimal hrgqty;
	/**
	 * 生产批号标识
	 */
	private String plotid;
	/**
	 * 生产批号
	 */
	private String plotno;
	/**
	 * 生产日期
	 */
	private Date pproddate;
	/**
	 * 生产效期
	 */
	private Date pvaliddate;
	/**
	 * 生产失效期
	 */
	private Date pinvaliddate;
	/**
	 * 灭菌批号标识
	 */
	private String slotid;
	/**
	 * 灭菌批号
	 */
	private String slotno;
	/**
	 * 灭菌日期
	 */
	private Date sproddate;
	/**
	 * 灭菌效期
	 */
	private Date svaliddate;
	/**
	 * 灭菌失效期
	 */
	private Date sinvaliddate;
	/**
	 * 发票号
	 */
	private String invno;
	/**
	 * 发票轨号
	 */
	private String invfirstno;
	/**
	 * 发票日期
	 */
	private Date invdate;
	/**
	 * 发票号总金额
	 */
	private BigDecimal invmoney;
	/**
	 * 交易平台标识
	 */
	private String tpid;
	/**
	 * 创建时间
	 */
	private Date credate;
	/**
	 * 备注
	 */
	private String memo;
	/**
	 * 验收时间
	 */
	private Date checkpotime;
	/**
	 * 验收人标识
	 */
	private String checkpomanid;
	/**
	 * 验收人名称
	 */
	private String checkpomanname;
	/**
	 * 质量状态
	 */
	private BigDecimal qualitystatus;
	/**
	 * 商品单位
	 */
	private String unitid;
	/**
	 * 商品单位编码
	 */
	private String unitcode;
	/**
	 * 医院商品
	 */
	private String hgoodsid;
	/**
	 * 医院单位数量
	 */
	private BigDecimal hgoodsunitqty;
	/**
	 * 医院实收单位数量
	 */
	private BigDecimal hrgunitqty;
	/**
	 * 发票状态
	 */
	private String invstatus;
	/**
	 * 跟台标志
	 */
	private BigDecimal stageflag;
	/**
	 * 处理标志
	 */
	private BigDecimal expflag;
	/**
	 * 标签条码
	 */
	private String labelno;

	/**
	 * 设置：验收明细标识
	 */
	public void setPodtlid(String podtlid) {
		this.podtlid = podtlid;
	}
	/**
	 * 获取：验收明细标识
	 */
	public String getPodtlid() {
		return podtlid;
	}
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
	 * 设置：批次标识
	 */
	public void setBatchid(String batchid) {
		this.batchid = batchid;
	}
	/**
	 * 获取：批次标识
	 */
	public String getBatchid() {
		return batchid;
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
	 * 设置：来源明细标识
	 */
	public void setSourcedtlid(String sourcedtlid) {
		this.sourcedtlid = sourcedtlid;
	}
	/**
	 * 获取：来源明细标识
	 */
	public String getSourcedtlid() {
		return sourcedtlid;
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
	 * 设置：医院商品编码
	 */
	public void setHgoodsno(String hgoodsno) {
		this.hgoodsno = hgoodsno;
	}
	/**
	 * 获取：医院商品编码
	 */
	public String getHgoodsno() {
		return hgoodsno;
	}
	/**
	 * 设置：医院商品名称
	 */
	public void setHgoodsname(String hgoodsname) {
		this.hgoodsname = hgoodsname;
	}
	/**
	 * 获取：医院商品名称
	 */
	public String getHgoodsname() {
		return hgoodsname;
	}
	/**
	 * 设置：医院单价
	 */
	public void setHunitprice(BigDecimal hunitprice) {
		this.hunitprice = hunitprice;
	}
	/**
	 * 获取：医院单价
	 */
	public BigDecimal getHunitprice() {
		return hunitprice;
	}
	/**
	 * 设置：医院数量
	 */
	public void setHgoodsqty(BigDecimal hgoodsqty) {
		this.hgoodsqty = hgoodsqty;
	}
	/**
	 * 获取：医院数量
	 */
	public BigDecimal getHgoodsqty() {
		return hgoodsqty;
	}
	/**
	 * 设置：医院实收数量
	 */
	public void setHrgqty(BigDecimal hrgqty) {
		this.hrgqty = hrgqty;
	}
	/**
	 * 获取：医院实收数量
	 */
	public BigDecimal getHrgqty() {
		return hrgqty;
	}
	/**
	 * 设置：生产批号标识
	 */
	public void setPlotid(String plotid) {
		this.plotid = plotid;
	}
	/**
	 * 获取：生产批号标识
	 */
	public String getPlotid() {
		return plotid;
	}
	/**
	 * 设置：生产批号
	 */
	public void setPlotno(String plotno) {
		this.plotno = plotno;
	}
	/**
	 * 获取：生产批号
	 */
	public String getPlotno() {
		return plotno;
	}
	/**
	 * 设置：生产日期
	 */
	public void setPproddate(Date pproddate) {
		this.pproddate = pproddate;
	}
	/**
	 * 获取：生产日期
	 */
	public Date getPproddate() {
		return pproddate;
	}
	/**
	 * 设置：生产效期
	 */
	public void setPvaliddate(Date pvaliddate) {
		this.pvaliddate = pvaliddate;
	}
	/**
	 * 获取：生产效期
	 */
	public Date getPvaliddate() {
		return pvaliddate;
	}
	/**
	 * 设置：生产失效期
	 */
	public void setPinvaliddate(Date pinvaliddate) {
		this.pinvaliddate = pinvaliddate;
	}
	/**
	 * 获取：生产失效期
	 */
	public Date getPinvaliddate() {
		return pinvaliddate;
	}
	/**
	 * 设置：灭菌批号标识
	 */
	public void setSlotid(String slotid) {
		this.slotid = slotid;
	}
	/**
	 * 获取：灭菌批号标识
	 */
	public String getSlotid() {
		return slotid;
	}
	/**
	 * 设置：灭菌批号
	 */
	public void setSlotno(String slotno) {
		this.slotno = slotno;
	}
	/**
	 * 获取：灭菌批号
	 */
	public String getSlotno() {
		return slotno;
	}
	/**
	 * 设置：灭菌日期
	 */
	public void setSproddate(Date sproddate) {
		this.sproddate = sproddate;
	}
	/**
	 * 获取：灭菌日期
	 */
	public Date getSproddate() {
		return sproddate;
	}
	/**
	 * 设置：灭菌效期
	 */
	public void setSvaliddate(Date svaliddate) {
		this.svaliddate = svaliddate;
	}
	/**
	 * 获取：灭菌效期
	 */
	public Date getSvaliddate() {
		return svaliddate;
	}
	/**
	 * 设置：灭菌失效期
	 */
	public void setSinvaliddate(Date sinvaliddate) {
		this.sinvaliddate = sinvaliddate;
	}
	/**
	 * 获取：灭菌失效期
	 */
	public Date getSinvaliddate() {
		return sinvaliddate;
	}
	/**
	 * 设置：发票号
	 */
	public void setInvno(String invno) {
		this.invno = invno;
	}
	/**
	 * 获取：发票号
	 */
	public String getInvno() {
		return invno;
	}
	/**
	 * 设置：发票轨号
	 */
	public void setInvfirstno(String invfirstno) {
		this.invfirstno = invfirstno;
	}
	/**
	 * 获取：发票轨号
	 */
	public String getInvfirstno() {
		return invfirstno;
	}
	/**
	 * 设置：发票日期
	 */
	public void setInvdate(Date invdate) {
		this.invdate = invdate;
	}
	/**
	 * 获取：发票日期
	 */
	public Date getInvdate() {
		return invdate;
	}
	/**
	 * 设置：发票号总金额
	 */
	public void setInvmoney(BigDecimal invmoney) {
		this.invmoney = invmoney;
	}
	/**
	 * 获取：发票号总金额
	 */
	public BigDecimal getInvmoney() {
		return invmoney;
	}
	/**
	 * 设置：交易平台标识
	 */
	public void setTpid(String tpid) {
		this.tpid = tpid;
	}
	/**
	 * 获取：交易平台标识
	 */
	public String getTpid() {
		return tpid;
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
	 * 设置：验收时间
	 */
	public void setCheckpotime(Date checkpotime) {
		this.checkpotime = checkpotime;
	}
	/**
	 * 获取：验收时间
	 */
	public Date getCheckpotime() {
		return checkpotime;
	}
	/**
	 * 设置：验收人标识
	 */
	public void setCheckpomanid(String checkpomanid) {
		this.checkpomanid = checkpomanid;
	}
	/**
	 * 获取：验收人标识
	 */
	public String getCheckpomanid() {
		return checkpomanid;
	}
	/**
	 * 设置：验收人名称
	 */
	public void setCheckpomanname(String checkpomanname) {
		this.checkpomanname = checkpomanname;
	}
	/**
	 * 获取：验收人名称
	 */
	public String getCheckpomanname() {
		return checkpomanname;
	}
	/**
	 * 设置：质量状态
	 */
	public void setQualitystatus(BigDecimal qualitystatus) {
		this.qualitystatus = qualitystatus;
	}
	/**
	 * 获取：质量状态
	 */
	public BigDecimal getQualitystatus() {
		return qualitystatus;
	}
	/**
	 * 设置：商品单位
	 */
	public void setUnitid(String unitid) {
		this.unitid = unitid;
	}
	/**
	 * 获取：商品单位
	 */
	public String getUnitid() {
		return unitid;
	}
	/**
	 * 设置：商品单位编码
	 */
	public void setUnitcode(String unitcode) {
		this.unitcode = unitcode;
	}
	/**
	 * 获取：商品单位编码
	 */
	public String getUnitcode() {
		return unitcode;
	}
	/**
	 * 设置：医院商品
	 */
	public void setHgoodsid(String hgoodsid) {
		this.hgoodsid = hgoodsid;
	}
	/**
	 * 获取：医院商品
	 */
	public String getHgoodsid() {
		return hgoodsid;
	}
	/**
	 * 设置：医院单位数量
	 */
	public void setHgoodsunitqty(BigDecimal hgoodsunitqty) {
		this.hgoodsunitqty = hgoodsunitqty;
	}
	/**
	 * 获取：医院单位数量
	 */
	public BigDecimal getHgoodsunitqty() {
		return hgoodsunitqty;
	}
	/**
	 * 设置：医院实收单位数量
	 */
	public void setHrgunitqty(BigDecimal hrgunitqty) {
		this.hrgunitqty = hrgunitqty;
	}
	/**
	 * 获取：医院实收单位数量
	 */
	public BigDecimal getHrgunitqty() {
		return hrgunitqty;
	}
	/**
	 * 设置：发票状态
	 */
	public void setInvstatus(String invstatus) {
		this.invstatus = invstatus;
	}
	/**
	 * 获取：发票状态
	 */
	public String getInvstatus() {
		return invstatus;
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
	 * 设置：处理标志
	 */
	public void setExpflag(BigDecimal expflag) {
		this.expflag = expflag;
	}
	/**
	 * 获取：处理标志
	 */
	public BigDecimal getExpflag() {
		return expflag;
	}
	/**
	 * 设置：标签条码
	 */
	public void setLabelno(String labelno) {
		this.labelno = labelno;
	}
	/**
	 * 获取：标签条码
	 */
	public String getLabelno() {
		return labelno;
	}
}
