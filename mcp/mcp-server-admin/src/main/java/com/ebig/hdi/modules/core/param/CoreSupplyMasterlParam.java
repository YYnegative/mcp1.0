package com.ebig.hdi.modules.core.param;

import lombok.Data;

import java.sql.Date;

/**
 * @program: mcp
 * @description: 供货单管理param
 * @email 791203472@qq.com
 * @author: ZhengHaiWen
 * @create: 2020-01-02 10:43
 **/

@Data
public class CoreSupplyMasterlParam {


    private  Integer supplyType;

    private  Integer supplyStatus;

    private  String supplyno;

    private Date StartingTime;

    private Date EndTime;

    private  String hospitalName;

    private  String purplanno;






}
