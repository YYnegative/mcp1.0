package com.ebig.mcp.server.api.http.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class RefundsDetailVo {

    public Long salbackid;
    public Long SALBACKSEQ;
    public String goodsName;
    public String masterRemark;
    public String goodsSpecsCode;
    public String goodsSpecs;
    public String goodsUnit;
    public BigDecimal goodsAmount;
    public String lotNo;
    public Timestamp beginTime;
    public Timestamp endTime;
    public String endTigoodsPriceme;
    public String detailRemark;
}
