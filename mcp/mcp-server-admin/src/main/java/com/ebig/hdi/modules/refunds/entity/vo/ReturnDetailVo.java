package com.ebig.hdi.modules.refunds.entity.vo;

import com.ebig.hdi.modules.refunds.entity.RefundsDetailEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 类功能说明：
 * 类修改者：
 * 修改日期：
 * 修改说明：
 * company：广东以大供应链管理有限公司
 *
 * @author： yy
 * @Date： 2019/11/5 0005 下午 16:37
 * @version： V1.0
 */
@Data
public class ReturnDetailVo  implements Serializable {

    private String factoryName;

    private String goodsUnit;

    private String goodsName;

    private String goodsType;

    private String goodsId;


}