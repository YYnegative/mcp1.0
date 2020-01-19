package com.ebig.mcp.server.api.http.controller;

import com.ebig.hdi.common.entity.MasterDetailsCommonEntity;
import com.ebig.hdi.common.utils.Hdi;
import com.ebig.mcp.server.api.http.entity.CoreSupplyDetailEntity;
import com.ebig.mcp.server.api.http.entity.CoreSupplyMasterEntity;
import com.ebig.mcp.server.api.http.service.SupplyMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * ERP供货单上传接口
 * @author alan
 * @Date 2019.10.22
 */
@RestController
@RequestMapping("/supplymenu")
public class SupplyMenuController {

    @Autowired
    SupplyMenuService supplyMenuService;
    /**
     * 供货单上传
     * @return
     */
    @RequestMapping("/uploadsupplymenu")
    public Object uploadsupplymenu(@RequestBody List<MasterDetailsCommonEntity<CoreSupplyMasterEntity, CoreSupplyDetailEntity>> list){

        Map<String, Object> map = supplyMenuService.uploadsupplymenu(list);
        if ((Integer)map.get("errorCount")>0){
            return  Hdi.error(map.get("errorCount")+"个供货单上传失败，"+map.get("errorMsg"));
        }else {
            return Hdi.ok("成功上传供货单："+map.get("successCount")+"个");
        }

    }



}
