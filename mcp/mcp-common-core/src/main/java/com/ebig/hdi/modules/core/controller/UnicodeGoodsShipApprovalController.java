package com.ebig.hdi.modules.core.controller;

import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.modules.core.service.UnicodeGoodsShipApprovalService;
import com.ebig.hdi.modules.sys.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @title: UnicodeGoodsShipApprovalController
 * @author：wenchao
 * @date：2019-12-29 9:40
 * @version：V1.0
 */
@RestController
@RequestMapping("/common")
public class UnicodeGoodsShipApprovalController extends AbstractController {


    @Autowired
    private UnicodeGoodsShipApprovalService unicodeGoodsShipApprovalService;

    /**
     * 取消已经匹对的医院药品
     */
    @PutMapping("/cancelmatch/{shipId}")
    public Hdi cancelMatch(@PathVariable("shipId") Long shipId ){
            unicodeGoodsShipApprovalService.cancelMatch(shipId,getUser());
        return Hdi.ok();
    }
}
