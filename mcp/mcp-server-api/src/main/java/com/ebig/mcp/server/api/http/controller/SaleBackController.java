package com.ebig.mcp.server.api.http.controller;

import com.alibaba.fastjson.JSONObject;
import com.ebig.hdi.common.entity.MasterDetailsCommonEntity;
import com.ebig.hdi.common.utils.Hdi;
import com.ebig.mcp.server.api.http.service.SaleBackService;
import com.ebig.mcp.server.api.http.vo.SaleBackVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 *供应商ERP退货单接口
 *
 * @author alan
 * @email
 * @date 2019-10-20 12:01:15
 */
@RestController
@RequestMapping("/api/saleback")
public class SaleBackController {

    @Autowired
    SaleBackService saleBackService;

    /**
     * 供应商ERP下载退货单接口
     */
    @RequestMapping("/download")
    public Hdi downloadsaleback(@RequestBody SaleBackVo saleBackVo){
        List<MasterDetailsCommonEntity> downloadsaleback = null;
        try {
            downloadsaleback = saleBackService.downloadsaleback(saleBackVo);
        } catch (Exception e) {
           return Hdi.error(e.getMessage());
        }

        return Hdi.ok().put("rowData",downloadsaleback);
    }
}
