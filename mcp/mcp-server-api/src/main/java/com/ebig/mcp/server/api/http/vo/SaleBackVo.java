package com.ebig.mcp.server.api.http.vo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class SaleBackVo {
    private String supplierCode;
    private String status;
    private Timestamp createTime;
    private Timestamp startTime;
    private Timestamp endTime;

}
