package com.ebig.hdi.modules.coretransform.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.ebig.hdi.common.datasources.annotation.DMLFlag;
import com.ebig.hdi.common.datasources.annotation.Reference;

import lombok.Data;

/**
 * spd_rgdtl
 * 
 * @author frink
 * @email 
 * @date 2019-06-24 09:40:10
 */
@Data
@TableName("spd_rgdtl")
public class TempSpdRgdtlEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 收货明细标识
	 */
	@TableId(type = IdType.INPUT)
	private String rgdtlid;
	/**
	 * 收货单标识
	 */
	@Reference(TempSpdRgEntity.class)
	private String rgid;
	/**
	 * 库房科室标识
	 */
	private String storehouseid;
	/**
	 * U机构标识
	 */
	private String uorganid;
	/**
	 * 配送商商品标识
	 */
	private String sgoodsid;
	/**
	 * 医院商品标识
	 */
	private String hgoodsid;
	/**
	 * 来源标识
	 */
	private String sourceid;
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
	 * 配送商商品名称
	 */
	private String sgoodsname;
	/**
	 * 配送商商品规格
	 */
	private String sgoodstype;
	/**
	 * 配送商商品单位
	 */
	private String sgoodsunit;
	/**
	 * 医院商品编码
	 */
	private String hgoodsno;
	/**
	 * 医院商品名称
	 */
	private String hgoodsname;
	/**
	 * 医院商品规格
	 */
	private String hgoodstype;
	/**
	 * 医院商品单位
	 */
	private String hgoodsunit;
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
	 * 医院包装大小
	 */
	private BigDecimal hpacksize;
	/**
	 * 验收标志
	 */
	private BigDecimal rgflag;
	/**
	 * 配送商单价
	 */
	private BigDecimal sunitprice;
	/**
	 * 配送商数量
	 */
	private BigDecimal sgoodsqty;
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
	 * 库房地址标识
	 */
	private String shaddressid;
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
	 * 冷链录入标志
	 */
	private BigDecimal ccinputflag;
	/**
	 * 备注
	 */
	private String memo;
	/**
	 * 验收时间
	 */
	private Date checkpotime;
	/**
	 * 验收人名称
	 */
	private String checkpomanid;
	/**
	 * 验收人名称
	 */
	private String checkpomanname;
	/**
	 * 采购计划标识
	 */
	private String purplandocid;
	/**
	 * 采购计划明细标识
	 */
	private String purplandtlid;
	/**
	 * 批次标识
	 */
	private String batchid;
	/**
	 * 生产批号标识
	 */
	private String plotid;
	/**
	 * 实收配送数量
	 */
	private BigDecimal srgqty;
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
	 * 商品单位
	 */
	private String hunitid;
	/**
	 * 医院单位数量
	 */
	private BigDecimal hgoodsunitqty;
	/**
	 * 医院实收单位数量
	 */
	private BigDecimal hrgunitqty;
	/**
	 * 原始细单标识
	 */
	private String origindtlid;
	/**
	 * 银行账号
	 */
	private String bankaccount;
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
	 * dml标记 1新增 2更新 3删除
	 */
	@DMLFlag
	@TableField(exist=false)
	private Integer dmlFlag;

	@TableField(exist=false)
	private Long hospitalGoodsId;


	@TableField(exist=false)
	private Integer goodsclass;

	public int getGoodsclass() {
		return goodsclass;
	}
}
