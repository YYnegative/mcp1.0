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

import com.ebig.hdi.modules.consumables.entity.GoodsHospitalConsumablesApprovalsEntity;
import com.ebig.hdi.modules.consumables.service.GoodsHospitalConsumablesApprovalsService;
import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.validator.ValidatorUtils;

/**
 * 医院耗材批准文号
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:39:01
 */
@RestController
@RequestMapping("consumables/goodshospitalconsumablesapprovals")
public class GoodsHospitalConsumablesApprovalsController {
    @Autowired
    private GoodsHospitalConsumablesApprovalsService goodsHospitalConsumablesApprovalsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("consumables:goodshospitalconsumablesapprovals:list")
    public Hdi list(@RequestParam Map<String, Object> params){
        PageUtils page = goodsHospitalConsumablesApprovalsService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("consumables:goodshospitalconsumablesapprovals:info")
    public Hdi info(@PathVariable("id") Long id){
        GoodsHospitalConsumablesApprovalsEntity goodsHospitalConsumablesApprovals = goodsHospitalConsumablesApprovalsService.selectById(id);

        return Hdi.ok().put("goodsHospitalConsumablesApprovals", goodsHospitalConsumablesApprovals);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("consumables:goodshospitalconsumablesapprovals:save")
    public Hdi save(@RequestBody GoodsHospitalConsumablesApprovalsEntity goodsHospitalConsumablesApprovals){
        goodsHospitalConsumablesApprovalsService.insert(goodsHospitalConsumablesApprovals);

        return Hdi.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("consumables:goodshospitalconsumablesapprovals:update")
    public Hdi update(@RequestBody GoodsHospitalConsumablesApprovalsEntity goodsHospitalConsumablesApprovals){
        ValidatorUtils.validateEntity(goodsHospitalConsumablesApprovals);
        goodsHospitalConsumablesApprovalsService.updateAllColumnById(goodsHospitalConsumablesApprovals);//全部更新
        
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("consumables:goodshospitalconsumablesapprovals:delete")
    public Hdi delete(@RequestBody Long[] ids){
        goodsHospitalConsumablesApprovalsService.deleteBatchIds(Arrays.asList(ids));

        return Hdi.ok();
    }

}
