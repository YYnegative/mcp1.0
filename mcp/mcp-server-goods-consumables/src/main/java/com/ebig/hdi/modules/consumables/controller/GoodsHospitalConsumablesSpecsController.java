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
import org.springframework.web.bind.annotation.RestController;

import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.modules.consumables.entity.GoodsHospitalConsumablesSpecsEntity;
import com.ebig.hdi.modules.consumables.service.GoodsHospitalConsumablesSpecsService;
import com.ebig.hdi.modules.sys.controller.AbstractController;

/**
 * 医院耗材规格
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:39:00
 */
@RestController
@RequestMapping("consumables/goodshospitalconsumablesspecs")
public class GoodsHospitalConsumablesSpecsController extends AbstractController{
    @Autowired
    private GoodsHospitalConsumablesSpecsService goodsHospitalConsumablesSpecsService;

    /**
     * 列表
     */
    @PostMapping("/list")
    //@RequiresPermissions("consumables:goodshospitalconsumablesspecs:list")
    public Hdi list(@RequestBody Map<String, Object> params){
        PageUtils page = goodsHospitalConsumablesSpecsService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("consumables:goodshospitalconsumablesspecs:info")
    public Hdi info(@PathVariable("id") Long id){
        GoodsHospitalConsumablesSpecsEntity goodsHospitalConsumablesSpecs = goodsHospitalConsumablesSpecsService.selectById(id);

        return Hdi.ok().put("goodsHospitalConsumablesSpecs", goodsHospitalConsumablesSpecs);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("consumables:goodshospitalconsumablesspecs:save")
    public Hdi save(@RequestBody GoodsHospitalConsumablesSpecsEntity goodsHospitalConsumablesSpecs){
        goodsHospitalConsumablesSpecsService.insert(goodsHospitalConsumablesSpecs);

        return Hdi.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    //@RequiresPermissions("consumables:goodshospitalconsumablesspecs:update")
    public Hdi update(@RequestBody List<GoodsHospitalConsumablesSpecsEntity> goodsHospitalConsumablesSpecsList){
    	for (GoodsHospitalConsumablesSpecsEntity goodsHospitalConsumablesSpecs : goodsHospitalConsumablesSpecsList) {
			ValidatorUtils.validateEntity(goodsHospitalConsumablesSpecs);
			if (goodsHospitalConsumablesSpecs.getId() != null) {
				goodsHospitalConsumablesSpecs.setEditId(getUserId());
			} else {
				goodsHospitalConsumablesSpecs.setCreateId(getUserId());
			}
		}
    	goodsHospitalConsumablesSpecsService.insertOrUpdate(goodsHospitalConsumablesSpecsList);
        
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("consumables:goodshospitalconsumablesspecs:delete")
    public Hdi delete(@RequestBody Long[] ids){
        goodsHospitalConsumablesSpecsService.deleteBatchIds(Arrays.asList(ids));

        return Hdi.ok();
    }

}
