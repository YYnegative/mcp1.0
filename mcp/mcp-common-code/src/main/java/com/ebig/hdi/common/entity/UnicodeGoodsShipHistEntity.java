package com.ebig.hdi.common.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-22 09:26:07
 */
@Data
@TableName("hdi_unicode_goods_ship_hist")
public class UnicodeGoodsShipHistEntity implements Serializable {
	private static final long serialVersionUID = 1L;


	/**
	 * 历史记录id
	 */
	@TableId
	private Long shiphistId;
	/**
	 * 关系id
	 */
	private Long shipId;
	/**
	 * 所属机构id
	 */
	private Long deptId;
	/**
	 * 目标机构标识
	 */
	private Long torgId;
	/**
	 * 目标机构类型(0:医院;1:供应商)
	 */
	private Integer torgType;
	/**
	 * 商品类型(1:药品;2:试剂;3:耗材)
	 */
	private Integer tgoodsType;
	/**
	 * 目标商品属性
	 */
	private Integer tgoodsNature;
	/**
	 * 目标商品名称
	 */
	private String tgoodsName;
	/**
	 * 目标商品id
	 */
	private Long tgoodsId;
	/**
	 * 平台商品属性
	 */
	private Integer pgoodsNature;
	/**
	 * 平台商品名称
	 */
	private String pgoodsName;
	/**
	 * 平台商品id
	 */
	private Long pgoodsId;
	/**
	 * 目标规格
	 */
	private String tspecs;
	/**
	 * 目标规格id
	 */
	private Long tspecsId;
	/**
	 * 平台规格
	 */
	private String pspecs;
	/**
	 * 平台规格id
	 */
	private Long pspecsId;
	/**
	 * 目标商品批文
	 */
	private String tapprovals;
	/**
	 * 目标商品批文id
	 */
	private Long tapprovalId;
	/**
	 * 平台商品批文
	 */
	private String papprovals;
	/**
	 * 平台商品批文id
	 */
	private Long papprovalId;
	/**
	 * 是否匹对(0:未匹配,1:已匹配 2.取消匹配)
	 */
	private Integer shipFlag;
	/**
	 * 审核状态(0:未审核,1:已审核 2.审核不通过)
	 */
	private Integer checkStatus;
	/**
	 * 操作类型(0,审批1:匹对,2:商品信息变更 3.取消匹对)
	 */
	private Integer operType;
	/**
	 * 是否删除(0:否,1:是)
	 */
	private Integer delFlag;
	/**
	 * 创建人id
	 */
	private Long cremanid;
	/**
	 * 创建人姓名
	 */
	private String cremanname;
	/**
	 * 创建时间
	 */
	private Date credate;
	/**
	 * 修改人id
	 */
	private Long editmanid;
	/**
	 * 修改人姓名
	 */
	private String editmanname;
	/**
	 * 修改时间
	 */
	private Date editdate;
	/**
	 * 备注
	 */
	private String memo;
	/**
	 *
	 */
	private String processId;
	/**
	 * 平台厂商名称
	 */
	private String pfactoryName;
	/**
	 *  平台厂商id
	 */
	private Long pfactoryId;
}
