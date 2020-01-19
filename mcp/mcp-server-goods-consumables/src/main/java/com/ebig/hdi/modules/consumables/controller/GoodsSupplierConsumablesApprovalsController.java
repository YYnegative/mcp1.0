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

import com.ebig.hdi.modules.consumables.entity.GoodsSupplierConsumablesApprovalsEntity;
import com.ebig.hdi.modules.consumables.service.GoodsSupplierConsumablesApprovalsService;
import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.validator.ValidatorUtils;

/**
 * 供应商耗材批准文号
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:36:02
 */
@RestController
@RequestMapping("/consumables/goodssupplierconsumablesapprovals")
public class GoodsSupplierConsumablesApprovalsController {
    @Autowired
    private GoodsSupplierConsumablesApprovalsService goodsSupplierConsumablesApprovalsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("consumables:goodssupplierconsumablesapprovals:list")
    public Hdi list(@RequestParam Map<String, Object> params){
        PageUtils page = goodsSupplierConsumablesApprovalsService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("consumables:goodssupplierconsumablesapprovals:info")
    public Hdi info(@PathVariable("id") Long id){
        GoodsSupplierConsumablesApprovalsEntity goodsSupplierConsumablesApprovals = goodsSupplierConsumablesApprovalsService.selectById(id);

        return Hdi.ok().put("goodsSupplierConsumablesApprovals", goodsSupplierConsumablesApprovals);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("consumables:goodssupplierconsumablesapprovals:save")
    public Hdi save(@RequestBody GoodsSupplierConsumablesApprovalsEntity goodsSupplierConsumablesApprovals){

       goodsSupplierConsumablesApprovalsService.save(goodsSupplierConsumablesApprovals);
        
        return Hdi.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("consumables:goodssupplierconsumablesapprovals:update")
    public Hdi update(@RequestBody GoodsSupplierConsumablesApprovalsEntity goodsSupplierConsumablesApprovals){
        ValidatorUtils.validateEntity(goodsSupplierConsumablesApprovals);
        goodsSupplierConsumablesApprovalsService.updateAllColumnById(goodsSupplierConsumablesApprovals);//全部更新
        
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("consumables:goodssupplierconsumablesapprovals:delete")
    public Hdi delete(@RequestBody Long[] ids){
        goodsSupplierConsumablesApprovalsService.deleteBatchIds(Arrays.asList(ids));

        return Hdi.ok();
    }

}
