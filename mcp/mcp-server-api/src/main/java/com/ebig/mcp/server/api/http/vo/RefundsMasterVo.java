package com.ebig.mcp.server.api.http.vo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class RefundsMasterVo {

    public Long SALBACKSEQ;
    public String SALBACKNO;
    public String supplierCode;
    public String supplierName;
    public String supplyid;
    public String supplyNo;
    public String hospitalCode;
    public String hospitalName;
    public String storeHouseNo;
    public String storeHouseName;
    public String storeHouseAddress;
    public Timestamp purplanTime;
    public Timestamp expectTime;
    public Timestamp createTime;
    public String masterRemark;
}
