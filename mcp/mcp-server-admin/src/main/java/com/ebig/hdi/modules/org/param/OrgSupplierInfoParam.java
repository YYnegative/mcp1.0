package com.ebig.hdi.modules.org.param;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: mcp
 * @description: 封装供应商信息查询参数
 * @email 791203472@qq.com
 * @author: ZhengHaiWen
 * @create: 2019-11-12 16:10
 **/
@Data
public class OrgSupplierInfoParam implements Serializable {
    private static final long serialVersionUID = 3L;

    /*
     *供应商编码
     */
    private  String supplierCode;

    /*
    *供应商名称
    */
    private String  supplierName;

    /*
     * 状态
     */
    private String status;

    /*
     * 医院区域
     */
    private String address;
    /*
     * 是否集团机构
     */
    private String isGroup;
    /*
     * 所在省份编码
     */
    private String provinceCode;

    /*
     * 所在城市编码
     */
    private String cityCode;

    /**
     * 所在区编码
     */
    private String areaCode;

    /**
     * 审核状态
     */
    private  String checkStatus;

}
