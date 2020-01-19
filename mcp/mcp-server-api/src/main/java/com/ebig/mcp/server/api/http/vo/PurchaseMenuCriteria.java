package com.ebig.mcp.server.api.http.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 类功能说明：
 * 类修改者：
 * 修改日期：
 * 修改说明：
 * company：广东以大供应链管理有限公司
 *
 * @author： yy
 * @date： 2019/10/26 9:00
 * @version： V1.0
 */
@Data
public class PurchaseMenuCriteria implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotBlank

    private String supplierCode;
    @NotBlank
    private String beginTime;
    @NotBlank
    private String endTime;
}