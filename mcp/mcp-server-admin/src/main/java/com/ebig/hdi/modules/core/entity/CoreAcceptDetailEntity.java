package com.ebig.hdi.modules.core.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.ebig.hdi.common.datasources.annotation.DMLFlag;
import com.ebig.hdi.common.datasources.annotation.Reference;

import lombok.Data;

/**
 * 验收细单
 * 
 * @author frink
 * @email 
 * @date 2019-05-31 15:29:12
 */
@Data
@TableName("hdi_core_accept_detail")
public class CoreAcceptDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 验收细单标识
	 */
	@TableId
	private Long acceptDetailId;
	/**
	 * 验收单标识
	 */
	@Reference(CoreAcceptMasterEntity.class)
	private Long acceptMasterId;
	/**
	 * 平台医院商品标识
	 */
	private Long goodsid;
	/**
	 * 原医院商品标识
	 */
	private String ygoodsid;
	/**
	 * 1 药品 2 试剂 3 耗材
	 */
	private Integer goodsclass;
	/**
	 * 平台医院商品规格标识
	 */
	private Long goodstypeid;
	/**
	 * 原商品规格标识
	 */
	private String ygoodstypeid;
	/**
	 * 商品单位
	 */
	private String goodsunit;
	/**
	 * 验收数量
	 */
	private Double acceptQty;
	/**
	 * 批号标识
	 */
	private Long lotid;
	/**
	 * 原始数据标识
	 */
	private String orgdataid;
	/**
	 * 原始明细数据标识
	 */
	private String orgdatadtlid;
	/**
	 * 供货单标识
	 */
	private Long sourceid;
	/**
	 * 供货细单标识
	 */
	private Long sourcedtlid;
	/**
	 * 备注
	 */
	private String memo;
	/**
	 * 创建人标识
	 */
	private Long cremanid;
	/**
	 * 创建人
	 */
//	private String cremanname;
	/**
	 * 创建时间
	 */
	private Timestamp credate;
	/**
	 * 生产日期
	 */
	private Date proddate;
	/**
	 * 失效日期
	 */
	private Date invadate;
	/**
	 * 平台医院商品规格
	 */
	private String goodstype;
	/**
	 * 生产批号
	 */
	private String lotno;

//---------改造后新增字段-------
	/**
	 * 原医院商品编码
	 */
	private String ygoodsno;
	/**
	 * 原医院商品名称
	 */
	private String ygoodsname;
	/**
	 * 原医院商品规格标识
	 */
	private String ygoodstypeno;
	/**
	 * 原医院商品规格标识
	 */
	private String ygoodstypename;
	/**
	 * 平台医院商品名称
	 */
	private String goodsno;
	/**
	 * 平台医院商品名称
	 */
	private String goodsname;
	/**
	 * 平台医院商品规格
	 */
	private String goodstypeno;




	/**
	 * 采购商品名称
	 */
//	@TableField(exist=false)
//	private String goodsName;
	/**
	 * 医院名称
	 */
	@TableField(exist=false)
	private String hospitalName;
	/**
	 * 供货单编号
	 */
	@TableField(exist=false)
	private String supplyno;
	/**
	 * dml标记 1新增 2更新 3删除
	 */
	@DMLFlag
	@TableField(exist=false)
	private Integer dmlFlag;

	/**
	 * 供货单价
	 */
	@TableField(exist=false)
	private Double supplyUnitprice;
	/**
	 * 库房名称
	 */
	@TableField(exist=false)
	private String storehousename;
	/**
	 * 生产厂家名称
	 */
	@TableField(exist=false)
	private String factoryName;
	/**
	 * 生产厂家id
	 */
	@TableField(exist=false)
	private Long factoryId;
	/**
	 * 唯一ID
	 */
	@TableField(exist=false)
	private Long uniqueId;
	
}
