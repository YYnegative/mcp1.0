package com.ebig.hdi.modules.consumables.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.modules.consumables.entity.GoodsPlatformConsumablesSpecsEntity;
import com.ebig.hdi.modules.consumables.service.GoodsPlatformConsumablesSpecsService;
import com.ebig.hdi.modules.sys.controller.AbstractController;

/**
 * 平台耗材规格
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:37:45
 */
@RestController
@RequestMapping("consumables/goodsplatformconsumablesspecs")
public class GoodsPlatformConsumablesSpecsController extends AbstractController{
    @Autowired
    private GoodsPlatformConsumablesSpecsService goodsPlatformConsumablesSpecsService;

    /**
     * 列表
     */
    @PostMapping("/list")
    //@RequiresPermissions("consumables:goodsplatformconsumablesspecs:list")
    public Hdi list(@RequestBody Map<String, Object> params){
        PageUtils page = goodsPlatformConsumablesSpecsService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("consumables:goodsplatformconsumablesspecs:info")
    public Hdi info(@PathVariable("id") Long id){
        GoodsPlatformConsumablesSpecsEntity goodsPlatformConsumablesSpecs = goodsPlatformConsumablesSpecsService.selectById(id);

        return Hdi.ok().put("goodsPlatformConsumablesSpecs", goodsPlatformConsumablesSpecs);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("consumables:goodsplatformconsumablesspecs:save")
    public Hdi save(@RequestBody GoodsPlatformConsumablesSpecsEntity goodsPlatformConsumablesSpecs){
		ValidatorUtils.validateEntity(goodsPlatformConsumablesSpecs);
		goodsPlatformConsumablesSpecs.setCreateId(getUserId());
		goodsPlatformConsumablesSpecsService.save(goodsPlatformConsumablesSpecs);

        return Hdi.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    //@RequiresPermissions("consumables:goodsplatformconsumablesspecs:update")
    public Hdi update(@RequestBody List<GoodsPlatformConsumablesSpecsEntity> goodsPlatformConsumablesSpecsList){
		for (GoodsPlatformConsumablesSpecsEntity goodsPlatformConsumablesSpecs : goodsPlatformConsumablesSpecsList) {
			ValidatorUtils.validateEntity(goodsPlatformConsumablesSpecs);
			if (goodsPlatformConsumablesSpecs.getId() != null) {
				goodsPlatformConsumablesSpecs.setEditId(getUserId());
			} else {
				goodsPlatformConsumablesSpecs.setCreateId(getUserId());
			}
		}
		goodsPlatformConsumablesSpecsService.insertOrUpdate(goodsPlatformConsumablesSpecsList);

		return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("consumables:goodsplatformconsumablesspecs:delete")
    public Hdi delete(@RequestBody Long[] ids){
        goodsPlatformConsumablesSpecsService.deleteBatchIds(Arrays.asList(ids));

        return Hdi.ok();
    }

}
