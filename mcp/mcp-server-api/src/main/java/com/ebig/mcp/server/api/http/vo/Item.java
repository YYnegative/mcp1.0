package com.ebig.mcp.server.api.http.vo;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 类功能说明：采购单主单类
 * 类修改者：
 * 修改日期：
 * 修改说明：
 * company：广东以大供应链管理有限公司
 *
 * @author： yy
 * @date： 2019/10/25 9:22
 * @version： V1.0
 */
@Data
public class Item implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 采购单标识
     */
    private Long purchaseMasterId;
    /**
     * 采购计划编号
     */
    private String purplanno;
    /**
     * 原供应商编码
     */
    private String sourcesSupplierCode;
    /**
     * 原供应商名称
     */
    private String sourcesSupplierName;
    /**
     * 平台供应商编码
     */
    private String supplierCode;
    /**
     * 平台供应商id
     */
    private String supplierId;
    /**
     * 原医院名称
     */
    private String sourcesHospitalName;

    /**
     * 原医院名称
     */
    private String sourcesHospitalCode;
    /**
     * 库房编码
     */
    private String storehouseNo;
    /**
     * 库房名称
     */
    private String storehouseName;
    /**
     * 医院库房地址
     */
    private String shaddress;
    /**
     * 采购时间
     */
    private Date purplantime;
    /**
     * 预计到货时间
     */
    private Date expecttime;
    /**
     * 创建时间
     */
    private Timestamp credate;
    /**
     * 采购单状态(0:已作废;1:未确认;2:已确认;3:已供货;4:部分供货)
     */
    private Integer purchasestatus;
    /**
     * 备注
     */
    private String memo;

}