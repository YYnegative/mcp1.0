package com.ebig.mcp.server.api.http.vo;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 类功能说明： 返回采购单主单类
 * 类修改者：
 * 修改日期：
 * 修改说明：
 * company：广东以大供应链管理有限公司
 *
 * @author： yy
 * @Date： 2019/10/22 10:02
 * @version： V1.0
 */
@Data
public class PurchaseMenuVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * "采购主单ID"
     */
    private Long masterId;
    /**
     * 采购计划编号
     */
    private String purplanNo;
    /**
     * 供应商编码
     */
    private String supplierCode;
    /**
     * 供应商名称
     */
    private String supplierName;
    /**
     * 医院编码
     */
    private String hospitalCode;
    /**
     * 医院名称
     */
    private String hospitalName;
    /**
     * 医院库房编码
     */
    private String storeHouseNo;
    /**
     * 医院库房名称
     */
    private String storeHouseName;
    /**
     * 医院库房地址
     */
    private String storeHouseAddress;
    /**
     * 采购时间
     */
    private Date purplanTime;
    /**
     * 预计到货时间
     */
    private Date expectTime;
    /**
     * 采购主单创建时间
     */
    private Date createTime;
    /**
     * 采购主单状态
     */
    private Integer purchaseStatus;
    /**
     * 采购主单备注
     */
    private String masterRemark;

}