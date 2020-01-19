package com.ebig.hdi.modules.coretransform.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 医院采购明细
 * 
 * @author frink
 * @email 
 * @date 2019-06-21 10:33:47
 */
@TableName("temp_hdi_hpurorderdtl")
public class TempHpurorderdtlEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 订单明细标识
	 */
	@TableId
	private String hpurorderdtlid;
	/**
	 * 采购订单标识
	 */
	private String hpurorderid;
	/**
	 * U机构标识
	 */
	private String uorganid;
	/**
	 * 医院商品标识
	 */
	private String hgoodsid;
	/**
	 * 配送商商品标识
	 */
	private String sgoodsid;
	/**
	 * 医院商品编码
	 */
	private String hgoodsno;
	/**
	 * 配送商商品编码
	 */
	private String sgoodsno;
	/**
	 * 医院商品名称
	 */
	private String hgoodsname;
	/**
	 * 配送商商品名称
	 */
	private String sgoodsname;
	/**
	 * 医院商品规格
	 */
	private String hgoodstype;
	/**
	 * 配送商商品规格
	 */
	private String sgoodstype;
	/**
	 * 医院商品单位
	 */
	private String hgoodsunit;
	/**
	 * 配送商商品单位
	 */
	private String sgoodsunit;
	/**
	 * 医院包装大小
	 */
	private BigDecimal hpacksize;
	/**
	 * 配送商包装大小
	 */
	private BigDecimal spacksize;
	/**
	 * 来源标识
	 */
	private String sourceid;
	/**
	 * 来源明细标识
	 */
	private String sourcedtlid;
	/**
	 * 医院单价
	 */
	private BigDecimal hunitprice;
	/**
	 * 配送商单价
	 */
	private BigDecimal sunitprice;
	/**
	 * 医院数量
	 */
	private BigDecimal hgoodsqty;
	/**
	 * 配送商数量
	 */
	private BigDecimal sgoodsqty;
	/**
	 * 本期购进金额
	 */
	private BigDecimal purmoney;
	/**
	 * 税率
	 */
	private BigDecimal purrate;
	/**
	 * 交易平台标识
	 */
	private String tpid;
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
	 * 医院采购明细状态
	 */
	private BigDecimal hpodtlstatus;
	/**
	 * 已配送数量
	 */
	private BigDecimal shipedqty;
	/**
	 * 已验收数量
	 */
	private BigDecimal receivedqty;
	/**
	 * 下载标志
	 */
	private BigDecimal exportflag;
	/**
	 * 上传下载标记，0上传 1下载
	 */
	private Integer udflag;

	/**
	 * 设置：订单明细标识
	 */
	public void setHpurorderdtlid(String hpurorderdtlid) {
		this.hpurorderdtlid = hpurorderdtlid;
	}
	/**
	 * 获取：订单明细标识
	 */
	public String getHpurorderdtlid() {
		return hpurorderdtlid;
	}
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
	 * 设置：医院商品标识
	 */
	public void setHgoodsid(String hgoodsid) {
		this.hgoodsid = hgoodsid;
	}
	/**
	 * 获取：医院商品标识
	 */
	public String getHgoodsid() {
		return hgoodsid;
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
	 * 设置：配送商商品名称
	 */
	public void setSgoodsname(String sgoodsname) {
		this.sgoodsname = sgoodsname;
	}
	/**
	 * 获取：配送商商品名称
	 */
	public String getSgoodsname() {
		return sgoodsname;
	}
	/**
	 * 设置：医院商品规格
	 */
	public void setHgoodstype(String hgoodstype) {
		this.hgoodstype = hgoodstype;
	}
	/**
	 * 获取：医院商品规格
	 */
	public String getHgoodstype() {
		return hgoodstype;
	}
	/**
	 * 设置：配送商商品规格
	 */
	public void setSgoodstype(String sgoodstype) {
		this.sgoodstype = sgoodstype;
	}
	/**
	 * 获取：配送商商品规格
	 */
	public String getSgoodstype() {
		return sgoodstype;
	}
	/**
	 * 设置：医院商品单位
	 */
	public void setHgoodsunit(String hgoodsunit) {
		this.hgoodsunit = hgoodsunit;
	}
	/**
	 * 获取：医院商品单位
	 */
	public String getHgoodsunit() {
		return hgoodsunit;
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
	 * 设置：医院包装大小
	 */
	public void setHpacksize(BigDecimal hpacksize) {
		this.hpacksize = hpacksize;
	}
	/**
	 * 获取：医院包装大小
	 */
	public BigDecimal getHpacksize() {
		return hpacksize;
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
	 * 设置：配送商数量
	 */
	public void setSgoodsqty(BigDecimal sgoodsqty) {
		this.sgoodsqty = sgoodsqty;
	}
	/**
	 * 获取：配送商数量
	 */
	public BigDecimal getSgoodsqty() {
		return sgoodsqty;
	}
	/**
	 * 设置：本期购进金额
	 */
	public void setPurmoney(BigDecimal purmoney) {
		this.purmoney = purmoney;
	}
	/**
	 * 获取：本期购进金额
	 */
	public BigDecimal getPurmoney() {
		return purmoney;
	}
	/**
	 * 设置：税率
	 */
	public void setPurrate(BigDecimal purrate) {
		this.purrate = purrate;
	}
	/**
	 * 获取：税率
	 */
	public BigDecimal getPurrate() {
		return purrate;
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
	 * 设置：医院采购明细状态
	 */
	public void setHpodtlstatus(BigDecimal hpodtlstatus) {
		this.hpodtlstatus = hpodtlstatus;
	}
	/**
	 * 获取：医院采购明细状态
	 */
	public BigDecimal getHpodtlstatus() {
		return hpodtlstatus;
	}
	/**
	 * 设置：已配送数量
	 */
	public void setShipedqty(BigDecimal shipedqty) {
		this.shipedqty = shipedqty;
	}
	/**
	 * 获取：已配送数量
	 */
	public BigDecimal getShipedqty() {
		return shipedqty;
	}
	/**
	 * 设置：已验收数量
	 */
	public void setReceivedqty(BigDecimal receivedqty) {
		this.receivedqty = receivedqty;
	}
	/**
	 * 获取：已验收数量
	 */
	public BigDecimal getReceivedqty() {
		return receivedqty;
	}
	/**
	 * 设置：下载标志
	 */
	public void setExportflag(BigDecimal exportflag) {
		this.exportflag = exportflag;
	}
	/**
	 * 获取：下载标志
	 */
	public BigDecimal getExportflag() {
		return exportflag;
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
