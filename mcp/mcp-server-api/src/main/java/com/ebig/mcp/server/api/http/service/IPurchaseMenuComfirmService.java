package com.ebig.mcp.server.api.http.service;

import com.ebig.hdi.common.entity.MasterDetailsCommonEntity;
import com.ebig.mcp.server.api.http.vo.PurchaseComfirmVo;

import java.util.List;

/**
 * 类功能说明：
 * 类修改者：
 * 修改日期：
 * 修改说明：
 * company：广东以大供应链管理有限公司
 *
 * @author： yy
 * @date： 2019/10/22 14:36
 * @version： V1.0
 */
public interface IPurchaseMenuComfirmService {

    /**
     *
     * @param purchaseComfirmVoList 接收确认参数
     * @return 返回主细单列表
     */
    List<MasterDetailsCommonEntity> dealwithPurchaseComfirm(List<PurchaseComfirmVo> purchaseComfirmVoList);
}