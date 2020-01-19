package com.ebig.hdi.modules.org.param;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: mcp
 * @description: 封装厂商管理页面查询参数
 * @email 791203472@qq.com
 * @author: ZhengHaiWen
 * @create: 2019-11-09 10:11
 **/
@Data
public class OrgHospitalParam implements Serializable {

    private static final long serialVersionUID = 2L;

    /**
     * 医院编码
     */
    private  String hospitalCode;
    /**
     * 医院名称
     */
    private  String hospitalName;

    /**
     *  医院等级
     */
    private String hospitalGrade;


    /**
     * 状态
     */
    private String status;

    /**
     * 医院区域
     */
    private String address;

    /**
     * 所在省份编码
     */
    private String provinceCode;

    /**
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
