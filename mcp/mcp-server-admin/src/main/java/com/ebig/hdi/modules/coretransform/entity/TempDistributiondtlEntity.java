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
 * @date 2019-06-18 10:59:10
 */
@TableName("temp_hdi_distributiondtl")
public class TempDistributiondtlEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 配送明细标识
	 */
	@TableId
	private String distributiondtlid;
	/**
	 * 配送数据标识
	 */
	private String distributionid;
	/**
	 * U机构标识
	 */
	private String uorganid;
	/**
	 * U机构编号
	 */
	private String uorganno;
	/**
	 * U机构名称
	 */
	private String uorganname;
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
	 * 配送商商品标识
	 */
	private String sgoodsid;
	/**
	 * 标签条码
	 */
	private String labelno;
	/**
	 * 标签类型
	 */
	private BigDecimal labeltype;
	/**
	 * 配送商商品编码
	 */
	private String sgoodsno;
	/**
	 * 商品名称
	 */
	private String sgoodsname;
	/**
	 * 商品规格
	 */
	private String sgoodstype;
	/**
	 * 配送商商品单位
	 */
	private String sgoodsunit;
	/**
	 * 批准文号
	 */
	private String approvedocno;
	/**
	 * 厂牌
	 */
	private String factorydoc;
	/**
	 * 厂家名称
	 */
	private String factoryname;
	/**
	 * 产地
	 */
	private String prodarea;
	/**
	 * 配送商包装大小
	 */
	private BigDecimal spacksize;
	/**
	 * 配送商单价
	 */
	private BigDecimal sunitprice;
	/**
	 * 基本单位数量
	 */
	private BigDecimal sgoodsqty;
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
	 * 配送地址标识
	 */
	private String addressid;
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
	 * 发票号
	 */
	private String invno;
	/**
	 * 发票轨号
	 */
	private String invfirstno;
	/**
	 * 清单顺序
	 */
	private BigDecimal invorder;
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
	 * 国家零售价
	 */
	private BigDecimal wholeprice;
	/**
	 * S收货数量
	 */
	private BigDecimal srgqty;
	/**
	 * 验收标志
	 */
	private BigDecimal rgflag;
	/**
	 * 采购计划
	 */
	private String purplandocid;
	/**
	 * 采购计划明细标识
	 */
	private String purplandtlid;
	/**
	 * 商品单位
	 */
	private String unitid;
	/**
	 * 商品单位编码
	 */
	private String unitcode;
	/**
	 * 确认状态
	 */
	private BigDecimal confirmed;
	/**
	 * 已打印
	 */
	private BigDecimal printflag;
	/**
	 * 销售明细标识
	 */
	private String saldtlid;
	/**
	 * 银行账号
	 */
	private String bankaccount;
	/**
	 * 开户银行
	 */
	private String bank;
	/**
	 * 来源
	 */
	private String sourceid;
	/**
	 * 来源明细
	 */
	private String sourcedtlid;
	/**
	 * 跟台标志
	 */
	private BigDecimal stageflag;
	/**
	 * 总排序号
	 */
	private Integer orderno;
	/**
	 * 页码
	 */
	private Integer pageno;
	/**
	 * 行号
	 */
	private Integer rowno;
	/**
	 * 下单时间
	 */
	private Date ordertime;
	/**
	 * 下单数量
	 */
	private BigDecimal orderquantity;
	/**
	 * 平台
	 */
	private String platform;
	/**
	 * 签收单ID
	 */
	private String signforno;
	/**
	 * 商品合计件数
	 */
	private BigDecimal signforqty;
	/**
	 * 上传下载标记，0上传 1下载
	 */
	private Integer udflag;

	/**
	 * 设置：配送明细标识
	 */
	public void setDistributiondtlid(String distributiondtlid) {
		this.distributiondtlid = distributiondtlid;
	}
	/**
	 * 获取：配送明细标识
	 */
	public String getDistributiondtlid() {
		return distributiondtlid;
	}
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
	 * 设置：配送商商品标识
	 */
	public void setSgoodsid(String sgoodsid) {
		this.sgoodsid = sgoodsid;
	}
	/**
	 * 获取：配送商商品标识
	 */
	public String getSgoodsid() {
		return sgoodsid;
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
	/**
	 * 设置：标签类型
	 */
	public void setLabeltype(BigDecimal labeltype) {
		this.labeltype = labeltype;
	}
	/**
	 * 获取：标签类型
	 */
	public BigDecimal getLabeltype() {
		return labeltype;
	}
	/**
	 * 设置：配送商商品编码
	 */
	public void setSgoodsno(String sgoodsno) {
		this.sgoodsno = sgoodsno;
	}
	/**
	 * 获取：配送商商品编码
	 */
	public String getSgoodsno() {
		return sgoodsno;
	}
	/**
	 * 设置：商品名称
	 */
	public void setSgoodsname(String sgoodsname) {
		this.sgoodsname = sgoodsname;
	}
	/**
	 * 获取：商品名称
	 */
	public String getSgoodsname() {
		return sgoodsname;
	}
	/**
	 * 设置：商品规格
	 */
	public void setSgoodstype(String sgoodstype) {
		this.sgoodstype = sgoodstype;
	}
	/**
	 * 获取：商品规格
	 */
	public String getSgoodstype() {
		return sgoodstype;
	}
	/**
	 * 设置：配送商商品单位
	 */
	public void setSgoodsunit(String sgoodsunit) {
		this.sgoodsunit = sgoodsunit;
	}
	/**
	 * 获取：配送商商品单位
	 */
	public String getSgoodsunit() {
		return sgoodsunit;
	}
	/**
	 * 设置：批准文号
	 */
	public void setApprovedocno(String approvedocno) {
		this.approvedocno = approvedocno;
	}
	/**
	 * 获取：批准文号
	 */
	public String getApprovedocno() {
		return approvedocno;
	}
	/**
	 * 设置：厂牌
	 */
	public void setFactorydoc(String factorydoc) {
		this.factorydoc = factorydoc;
	}
	/**
	 * 获取：厂牌
	 */
	public String getFactorydoc() {
		return factorydoc;
	}
	/**
	 * 设置：厂家名称
	 */
	public void setFactoryname(String factoryname) {
		this.factoryname = factoryname;
	}
	/**
	 * 获取：厂家名称
	 */
	public String getFactoryname() {
		return factoryname;
	}
	/**
	 * 设置：产地
	 */
	public void setProdarea(String prodarea) {
		this.prodarea = prodarea;
	}
	/**
	 * 获取：产地
	 */
	public String getProdarea() {
		return prodarea;
	}
	/**
	 * 设置：配送商包装大小
	 */
	public void setSpacksize(BigDecimal spacksize) {
		this.spacksize = spacksize;
	}
	/**
	 * 获取：配送商包装大小
	 */
	public BigDecimal getSpacksize() {
		return spacksize;
	}
	/**
	 * 设置：配送商单价
	 */
	public void setSunitprice(BigDecimal sunitprice) {
		this.sunitprice = sunitprice;
	}
	/**
	 * 获取：配送商单价
	 */
	public BigDecimal getSunitprice() {
		return sunitprice;
	}
	/**
	 * 设置：基本单位数量
	 */
	public void setSgoodsqty(BigDecimal sgoodsqty) {
		this.sgoodsqty = sgoodsqty;
	}
	/**
	 * 获取：基本单位数量
	 */
	public BigDecimal getSgoodsqty() {
		return sgoodsqty;
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
	 * 设置：清单顺序
	 */
	public void setInvorder(BigDecimal invorder) {
		this.invorder = invorder;
	}
	/**
	 * 获取：清单顺序
	 */
	public BigDecimal getInvorder() {
		return invorder;
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
	 * 设置：国家零售价
	 */
	public void setWholeprice(BigDecimal wholeprice) {
		this.wholeprice = wholeprice;
	}
	/**
	 * 获取：国家零售价
	 */
	public BigDecimal getWholeprice() {
		return wholeprice;
	}
	/**
	 * 设置：S收货数量
	 */
	public void setSrgqty(BigDecimal srgqty) {
		this.srgqty = srgqty;
	}
	/**
	 * 获取：S收货数量
	 */
	public BigDecimal getSrgqty() {
		return srgqty;
	}
	/**
	 * 设置：验收标志
	 */
	public void setRgflag(BigDecimal rgflag) {
		this.rgflag = rgflag;
	}
	/**
	 * 获取：验收标志
	 */
	public BigDecimal getRgflag() {
		return rgflag;
	}
	/**
	 * 设置：采购计划
	 */
	public void setPurplandocid(String purplandocid) {
		this.purplandocid = purplandocid;
	}
	/**
	 * 获取：采购计划
	 */
	public String getPurplandocid() {
		return purplandocid;
	}
	/**
	 * 设置：采购计划明细标识
	 */
	public void setPurplandtlid(String purplandtlid) {
		this.purplandtlid = purplandtlid;
	}
	/**
	 * 获取：采购计划明细标识
	 */
	public String getPurplandtlid() {
		return purplandtlid;
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
	 * 设置：确认状态
	 */
	public void setConfirmed(BigDecimal confirmed) {
		this.confirmed = confirmed;
	}
	/**
	 * 获取：确认状态
	 */
	public BigDecimal getConfirmed() {
		return confirmed;
	}
	/**
	 * 设置：已打印
	 */
	public void setPrintflag(BigDecimal printflag) {
		this.printflag = printflag;
	}
	/**
	 * 获取：已打印
	 */
	public BigDecimal getPrintflag() {
		return printflag;
	}
	/**
	 * 设置：销售明细标识
	 */
	public void setSaldtlid(String saldtlid) {
		this.saldtlid = saldtlid;
	}
	/**
	 * 获取：销售明细标识
	 */
	public String getSaldtlid() {
		return saldtlid;
	}
	/**
	 * 设置：银行账号
	 */
	public void setBankaccount(String bankaccount) {
		this.bankaccount = bankaccount;
	}
	/**
	 * 获取：银行账号
	 */
	public String getBankaccount() {
		return bankaccount;
	}
	/**
	 * 设置：开户银行
	 */
	public void setBank(String bank) {
		this.bank = bank;
	}
	/**
	 * 获取：开户银行
	 */
	public String getBank() {
		return bank;
	}
	/**
	 * 设置：来源
	 */
	public void setSourceid(String sourceid) {
		this.sourceid = sourceid;
	}
	/**
	 * 获取：来源
	 */
	public String getSourceid() {
		return sourceid;
	}
	/**
	 * 设置：来源明细
	 */
	public void setSourcedtlid(String sourcedtlid) {
		this.sourcedtlid = sourcedtlid;
	}
	/**
	 * 获取：来源明细
	 */
	public String getSourcedtlid() {
		return sourcedtlid;
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
	 * 设置：总排序号
	 */
	public void setOrderno(Integer orderno) {
		this.orderno = orderno;
	}
	/**
	 * 获取：总排序号
	 */
	public Integer getOrderno() {
		return orderno;
	}
	/**
	 * 设置：页码
	 */
	public void setPageno(Integer pageno) {
		this.pageno = pageno;
	}
	/**
	 * 获取：页码
	 */
	public Integer getPageno() {
		return pageno;
	}
	/**
	 * 设置：行号
	 */
	public void setRowno(Integer rowno) {
		this.rowno = rowno;
	}
	/**
	 * 获取：行号
	 */
	public Integer getRowno() {
		return rowno;
	}
	/**
	 * 设置：下单时间
	 */
	public void setOrdertime(Date ordertime) {
		this.ordertime = ordertime;
	}
	/**
	 * 获取：下单时间
	 */
	public Date getOrdertime() {
		return ordertime;
	}
	/**
	 * 设置：下单数量
	 */
	public void setOrderquantity(BigDecimal orderquantity) {
		this.orderquantity = orderquantity;
	}
	/**
	 * 获取：下单数量
	 */
	public BigDecimal getOrderquantity() {
		return orderquantity;
	}
	/**
	 * 设置：平台
	 */
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	/**
	 * 获取：平台
	 */
	public String getPlatform() {
		return platform;
	}
	/**
	 * 设置：签收单ID
	 */
	public void setSignforno(String signforno) {
		this.signforno = signforno;
	}
	/**
	 * 获取：签收单ID
	 */
	public String getSignforno() {
		return signforno;
	}
	/**
	 * 设置：商品合计件数
	 */
	public void setSignforqty(BigDecimal signforqty) {
		this.signforqty = signforqty;
	}
	/**
	 * 获取：商品合计件数
	 */
	public BigDecimal getSignforqty() {
		return signforqty;
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
