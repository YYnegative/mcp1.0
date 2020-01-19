package com.ebig.hdi.modules.core.controller;

import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.core.service.UnicodeGoodsShipHistService;
import com.ebig.hdi.modules.sys.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @program: mcp
 * @description: 供应商与医院历史详情
 * @email 791203472@qq.com
 * @author: ZhengHaiWen
 * @create: 2020-01-03 16:02
 **/
@RestController
@RequestMapping("/unicode/unicodegoodsshiphists")
public class UnicodeGoodsShipHistsController extends AbstractController {

    @Autowired
    private UnicodeGoodsShipHistService unicodeGoodsShipHistService;


    /**
     * 历史列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("unicode:unicodegoodsshiphist:list")
    public Hdi List(@RequestBody Map<String, Object> params){
        PageUtils page = unicodeGoodsShipHistService.queryPage(params);
        return Hdi.ok().put("page", page);
    }
}
