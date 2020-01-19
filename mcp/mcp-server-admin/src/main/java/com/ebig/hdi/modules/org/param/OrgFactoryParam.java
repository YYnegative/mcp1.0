package com.ebig.hdi.modules.org.param;

import lombok.Data;

import java.io.Serializable;

/**
 * @athor: yzh
 * Date: 2019/11/5
 * Time: 12:58
 * Description: 封装厂商管理页面查询参数
 */
@Data
public class OrgFactoryParam implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 厂商名称
     */
    private String factoryName;
    /**
     * 所在国家编码
     */
    private String countryCode;
    /**
     * 所在省份编码
     */
    private String provinceCode;
    /**
     * 所在城市编码
     */
    private String cityCode;
    /**
     * 所在区县编码
     */
    private String areaCode;
    /**
     * 状态
     */
    private String status;
    /**
     * 审批状态
     */
    private String checkStatus;
}
