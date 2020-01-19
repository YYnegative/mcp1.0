package com.ebig.hdi.modules.core.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.modules.core.entity.CorePurchaseDetailEntity;
import com.ebig.hdi.modules.core.service.CorePurchaseDetailService;

/**
 * 
 *
 * @author frink
 * @email 
 * @date 2019-05-27 11:13:13
 */
@RestController
@RequestMapping("core/corepurchasedetail")
public class CorePurchaseDetailController {
    @Autowired
    private CorePurchaseDetailService corePurchaseDetailService;

    /**
	 * 采购单细单列表
	 */
	@RequestMapping("/listDetail")
	//@RequiresPermissions("core:corepurchasedetail:list")
	public Hdi listDetail(@RequestBody Map<String, Object> params){
	    PageUtils page = corePurchaseDetailService.queryPageDetail(params);
	
	    return Hdi.ok().put("page", page);
	}


    /**
     * 信息
     */
    @RequestMapping("/info/{purchaseDetailId}")
    @RequiresPermissions("core:corepurchasedetail:info")
    public Hdi info(@PathVariable("purchaseDetailId") Long purchaseDetailId){
        CorePurchaseDetailEntity corePurchaseDetail = corePurchaseDetailService.selectById(purchaseDetailId);

        return Hdi.ok().put("corePurchaseDetail", corePurchaseDetail);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("core:corepurchasedetail:save")
    public Hdi save(@RequestBody CorePurchaseDetailEntity corePurchaseDetail){
        corePurchaseDetailService.insert(corePurchaseDetail);

        return Hdi.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("core:corepurchasedetail:update")
    public Hdi update(@RequestBody CorePurchaseDetailEntity corePurchaseDetail){
        ValidatorUtils.validateEntity(corePurchaseDetail);
        corePurchaseDetailService.updateAllColumnById(corePurchaseDetail);//全部更新
        
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("core:corepurchasedetail:delete")
    public Hdi delete(@RequestBody Long[] purchaseDetailIds){
        corePurchaseDetailService.deleteBatchIds(Arrays.asList(purchaseDetailIds));

        return Hdi.ok();
    }

}
