package com.ebig.hdi.modules.job.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;

/**
 * pub_goods
 * 
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-06-18 11:04:28
 */
@TableName("pub_goods")
@Data
public class TempPubGoodsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * M商品主键
	 */
	@TableId(type = IdType.INPUT)
	private String mgoodsid;
	/**
	 * U机构标识
	 */
	private String uorganid;
	/**
	 * 商品编码
	 */
	private String goodsno;
	/**
	 * 通用名称
	 */
	private String goodsname;
	/**
	 * 商品名拼音
	 */
	private String goodspy;
	/**
	 * 商品名称
	 */
	private String commonname;
	/**
	 * 通用名拼音
	 */
	private String commonpy;
	/**
	 * 英文名
	 */
	private String enname;
	/**
	 * 商品规格
	 */
	private String goodstype;
	/**
	 * 商品缩写名
	 */
	private String goodsshortname;
	/**
	 * 状态
	 */
	private BigDecimal usestatus;
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
	 * 条形码
	 */
	private String barcode;
	/**
	 * 发票名称
	 */
	private String goodsinvname;
	/**
	 * 包装大小
	 */
	private BigDecimal packsize;
	/**
	 * 单位
	 */
	private String goodsunit;
	/**
	 * 批准文号
	 */
	private String approvedocno;
	/**
	 * 批文认证日期
	 */
	private Date approvedocdate;
	/**
	 * 批文有效期
	 */
	private Date approvedocenddate;
	/**
	 * 注册证号
	 */
	private String registerdocno;
	/**
	 * 注册证认证日期
	 */
	private Date registerdocdate;
	/**
	 * 注册证有效期
	 */
	private Date registerdocenddate;
	/**
	 * 储存属性
	 */
	private BigDecimal goodsprop;
	/**
	 * 储存条件
	 */
	private BigDecimal storagecondition;
	/**
	 * 有效期
	 */
	private BigDecimal validperiod;
	/**
	 * 有效期单位
	 */
	private String timeunit;
	/**
	 * 基药类别
	 */
	private BigDecimal basictype;
	/**
	 * 采购类别
	 */
	private BigDecimal purchasetype;
	/**
	 * 基本类别
	 */
	private BigDecimal basegoodsattr;
	/**
	 * 报告书标志
	 */
	private BigDecimal reportflag;
	/**
	 * 处方otc属性
	 */
	private BigDecimal recipeflag;
	/**
	 * 医保属性
	 */
	private BigDecimal insur;
	/**
	 * 剂型分类
	 */
	private BigDecimal classtype;
	/**
	 * 功效分类
	 */
	private BigDecimal effictype;
	/**
	 * GMP认证号码
	 */
	private String gmpcertno;
	/**
	 * GMP认证时间
	 */
	private Date gmpcertdate;
	/**
	 * GMP有效期
	 */
	private Date gmpcertdeadline;
	/**
	 * 混装类别
	 */
	private BigDecimal mixtype;
	/**
	 * 特殊药品标志
	 */
	private BigDecimal specdrugflag;
	/**
	 * 录入人Id
	 */
	private String inputmanid;
	/**
	 * 录入人
	 */
	private String inputmanname;
	/**
	 * 录入日期
	 */
	private Date inputdate;
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
	 * 备注
	 */
	private String memo;
	/**
	 * 商品助记码信息
	 */
	private String goodsinfo;
	/**
	 * UDF1
	 */
	private String udf1;
	/**
	 * UDF2
	 */
	private String udf2;
	/**
	 * UDF4
	 */
	private String udf4;
	/**
	 * UDF3
	 */
	private String udf3;
	/**
	 * UDF5
	 */
	private String udf5;
	/**
	 * 平台商品编码
	 */
	private String ugoodsid;
	/**
	 * 倍数分子
	 */
	private BigDecimal rationmrtr;
	/**
	 * 倍数分母
	 */
	private BigDecimal ratiodnmtr;
	/**
	 * 原数据标识
	 */
	private String orgdataid;
	/**
	 * 数据来源
	 */
	private BigDecimal datasource;
	/**
	 * 效期预警天数
	 */
	private Integer vwarndays;
	/**
	 * 默认配送商
	 */
	private String defsupplyid;
	/**
	 * 税率
	 */
	private BigDecimal purtax;
	/**
	 * 存储温度
	 */
	private String storetemp;
	/**
	 * 照片
	 */
	private String photo;
	/**
	 * 中标价
	 */
	private BigDecimal bidprice;
	/**
	 * 统计分类
	 */
	private BigDecimal countgroup;
	/**
	 * 首营商品标志
	 */
	private BigDecimal firstcampgoods;
	/**
	 * 付费项目
	 */
	private BigDecimal pay;
	/**
	 * 单品管理标识
	 */
	private BigDecimal singleflag;
	/**
	 * 生产许可证号
	 */
	private String prolicenseno;
	/**
	 * 生产许可证有效期
	 */
	private Date prolicensenovalid;
	/**
	 * 商品所属类别(1：其他，2，耗材，3，药品，4：试剂；)
	 */
	private Integer goodscategorytype;
	/**
	 * 上传下载标记，0上传 1下载
	 */
	private Integer udflag;
	/**
	 * 医院商品标识(null:未生成医院商品对应关系；0：更新医院商品；1：已生成医院商品)
	 */
	private Integer hospitalflag;
	
	/**
	 * 供应商id
	 */
	@TableField(exist=false)
	private Long supplierId;
	
	/**
	 * 医院id
	 */
	@TableField(exist=false)
	private Long hospitalId;
	
	/**
	 * mcp机构id
	 */
	@TableField(exist=false)
	private Long deptId;
	
	/**
	 * mcp商品id
	 */
	@TableField(exist=false)
	private Long goodsId;
	
	/**
	 * mcp供应商商品下发id
	 */
	@TableField(exist=false)
	private Long sendId;
	
	/**
	 * mcp供应商商品储存方式
	 */
	@TableField(exist=false)
	private String storeWay;

}
