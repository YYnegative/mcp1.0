package com.ebig.hdi.modules.job.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@TableName("pub_supplygoods")
@Data
public class TempPubSupplyGoodsEntity implements Serializable {
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
     * 厂家名称
     */
    private String factoryname;
    /**
     * 包装大小
     */
    private BigDecimal packsize;
    /**
     * 单位
     */
    private String goodsunit;
    /**
     * 注册证号
     */
    private String registerdocno;
    /**
     * 储存属性：常温、阴凉、冷藏、冰冻，默认常温
     */
    private BigDecimal goodsprop;
    /**
     * 储存条件 ：1常温、2阴凉、3冷藏、4冰冻
     */
    private BigDecimal storagecondiction;
    /**
     * 混装类别
     */
    private BigDecimal mixtype;
    /**
     * 倍数分子
     */
    private BigDecimal rationmrtr;
    /**
     * 倍数分母
     */
    private BigDecimal ratiodnmtr;
    /**
     * 是否收费
     */
    private BigDecimal pay;
    /**
     * 商品所属类别(1：其他，2，耗材，3，药品，4：试剂；)
     */
    private Integer goodscategorytype;
    /**
     * 供应商id
     */
    private String supplyid;
    /**
     * 基础单位/采购单位的转换比例
     */
    private String unitcover;
    /**
     * 传送时间
     */
    private Timestamp transdate;
    /**
     * 品规采购进货价
     */
    private BigDecimal bidprice;
     /**
     * 采购类别=耗材类别
     */
    private BigDecimal purchasetype;
    /**
     * 商品平台编码
     */
    private String platformno;
    /**
     * 接收状态：0为未接收，1为已接收，默认为0
     */
    private Integer receiptflag;

    /**
     * 阳光平台编码
     */
    private  String sunshinePno;

    /**
     * mcp供应商商品下发id
     */
    @TableField(exist=false)
    private Long sendId;


}
