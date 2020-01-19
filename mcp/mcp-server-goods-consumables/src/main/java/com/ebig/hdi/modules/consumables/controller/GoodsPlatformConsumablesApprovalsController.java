package com.ebig.hdi.modules.consumables.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ebig.hdi.modules.consumables.entity.GoodsPlatformConsumablesApprovalsEntity;
import com.ebig.hdi.modules.consumables.service.GoodsPlatformConsumablesApprovalsService;
import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.validator.ValidatorUtils;

/**
 * 平台耗材批准文号
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:37:46
 */
@RestController
@RequestMapping("consumables/goodsplatformconsumablesapprovals")
public class GoodsPlatformConsumablesApprovalsController {
    @Autowired
    private GoodsPlatformConsumablesApprovalsService goodsPlatformConsumablesApprovalsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("consumables:goodsplatformconsumablesapprovals:list")
    public Hdi list(@RequestParam Map<String, Object> params){
        PageUtils page = goodsPlatformConsumablesApprovalsService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("consumables:goodsplatformconsumablesapprovals:info")
    public Hdi info(@PathVariable("id") Long id){
        GoodsPlatformConsumablesApprovalsEntity goodsPlatformConsumablesApprovals = goodsPlatformConsumablesApprovalsService.selectById(id);

        return Hdi.ok().put("goodsPlatformConsumablesApprovals", goodsPlatformConsumablesApprovals);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("consumables:goodsplatformconsumablesapprovals:save")
    public Hdi save(@RequestBody GoodsPlatformConsumablesApprovalsEntity goodsPlatformConsumablesApprovals){
        goodsPlatformConsumablesApprovalsService.insert(goodsPlatformConsumablesApprovals);

        return Hdi.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("consumables:goodsplatformconsumablesapprovals:update")
    public Hdi update(@RequestBody GoodsPlatformConsumablesApprovalsEntity goodsPlatformConsumablesApprovals){
        ValidatorUtils.validateEntity(goodsPlatformConsumablesApprovals);
        goodsPlatformConsumablesApprovalsService.updateAllColumnById(goodsPlatformConsumablesApprovals);//全部更新
        
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("consumables:goodsplatformconsumablesapprovals:delete")
    public Hdi delete(@RequestBody Long[] ids){
        goodsPlatformConsumablesApprovalsService.deleteBatchIds(Arrays.asList(ids));

        return Hdi.ok();
    }

}
