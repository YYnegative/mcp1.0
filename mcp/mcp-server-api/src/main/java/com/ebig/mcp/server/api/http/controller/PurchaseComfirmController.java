package com.ebig.mcp.server.api.http.controller;


import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.ebig.hdi.common.entity.MasterDetailsCommonEntity;
import com.ebig.hdi.common.enums.HttpStatusEnums;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.mcp.server.api.http.service.IPurchaseMenuComfirmService;
import com.ebig.mcp.server.api.http.vo.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 类功能说明：
 * 类修改者：
 * 修改日期：
 * 修改说明：
 * 公司名称：广东以大供应链管理有限公司
 * @author ： yy
 * 创建时间： 2019/10/18 14:59
 * 版本： V1.0
 */
@RestController
@RequestMapping("/purchasemunecomfirm")
public class PurchaseComfirmController {

    @Autowired
    private IPurchaseMenuComfirmService purchaseMuneComfirmService;

    @PostMapping("/purchasecomfirm")
    public Hdi purchaseComfirm(@RequestBody List<PurchaseComfirmVo> purchaseComfirmVoList){
        try{
            for (int i = 0; i < purchaseComfirmVoList.size(); i++) {
                String message = StringUtil.checkFieldIsNull(purchaseComfirmVoList.get(i));
                if (StringUtils.isNotEmpty(message)){
                    return Hdi.error(HttpStatusEnums.ERRCODE_INVOKE_ERR.getKey(),"第" + ( i + 1)+"个数据对象的" + message + "属性为空");
                }
            }
            List<MasterDetailsCommonEntity> rowData = purchaseMuneComfirmService.dealwithPurchaseComfirm(purchaseComfirmVoList);
            return Hdi.ok().put("rowData",rowData);
        }catch (HdiException e) {
            e.printStackTrace();
            return Hdi.error(e.getMessage());
        }
    }
}