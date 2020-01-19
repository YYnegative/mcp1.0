package com.ebig.hdi.modules.surgery.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.modules.surgery.entity.SalesDetailInfoEntity;
import com.ebig.hdi.modules.surgery.service.SalesDetailInfoService;

/**
 * 销售明细表
 *
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-06-11 10:15:32
 */
@RestController
@RequestMapping("/surgery/salesdetailinfo")
public class SalesDetailInfoController {
    @Autowired
    private SalesDetailInfoService salesDetailInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("surgery:salesdetailinfo:list")
    public Hdi list(@RequestBody Map<String, Object> params){
        PageUtils page = salesDetailInfoService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("surgery:salesdetailinfo:info")
    public Hdi info(@PathVariable("id") Long id){
        SalesDetailInfoEntity salesDetailInfo = salesDetailInfoService.selectById(id);

        return Hdi.ok().put("salesDetailInfo", salesDetailInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("surgery:salesdetailinfo:save")
    public Hdi save(@RequestBody SalesDetailInfoEntity salesDetailInfo){
        salesDetailInfoService.insert(salesDetailInfo);

        return Hdi.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("surgery:salesdetailinfo:update")
    public Hdi update(@RequestBody SalesDetailInfoEntity salesDetailInfo){
        ValidatorUtils.validateEntity(salesDetailInfo);
        salesDetailInfoService.updateAllColumnById(salesDetailInfo);//全部更新
        
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("surgery:salesdetailinfo:delete")
    public Hdi delete(@RequestBody Long[] ids){
        salesDetailInfoService.deleteBatchIds(Arrays.asList(ids));

        return Hdi.ok();
    }

}
