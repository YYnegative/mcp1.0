package com.ebig.hdi.modules.consumables.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.modules.consumables.entity.GoodsSupplierConsumablesSpecsEntity;
import com.ebig.hdi.modules.consumables.service.GoodsSupplierConsumablesSpecsService;
import com.ebig.hdi.modules.sys.controller.AbstractController;

/**
 * 供应商耗材规格
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:36:02
 */
@RestController
@RequestMapping("/consumables/goodssupplierconsumablesspecs")
public class GoodsSupplierConsumablesSpecsController extends AbstractController {
    @Autowired
    private GoodsSupplierConsumablesSpecsService goodsSupplierConsumablesSpecsService;

    /**
     * 列表
     */
    @PostMapping("/list")
    //@RequiresPermissions("consumables:goodssupplierconsumablesspecs:list")
    public Hdi list(@RequestBody Map<String, Object> params){
        PageUtils page = goodsSupplierConsumablesSpecsService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 编辑
     */
    @PostMapping("/update")
    //@RequiresPermissions("consumables:goodssupplierconsumablesspecs:update")
    public Hdi update(@RequestBody List<GoodsSupplierConsumablesSpecsEntity> goodsSupplierConsumablesSpecsList){
    	for (GoodsSupplierConsumablesSpecsEntity goodsSupplierConsumablesSpecs : goodsSupplierConsumablesSpecsList) {
			ValidatorUtils.validateEntity(goodsSupplierConsumablesSpecs);
			if (goodsSupplierConsumablesSpecs.getId() != null) {
				goodsSupplierConsumablesSpecs.setEditId(getUserId());
			} else {
				goodsSupplierConsumablesSpecs.setCreateId(getUserId());
			}
		}
    	goodsSupplierConsumablesSpecsService.insertOrUpdate(goodsSupplierConsumablesSpecsList);

		return Hdi.ok();
    }

}
