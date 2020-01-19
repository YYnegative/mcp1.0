package com.ebig.hdi.modules.reagent.controller;

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
import com.ebig.hdi.modules.reagent.entity.GoodsSupplierReagentSpecsEntity;
import com.ebig.hdi.modules.reagent.service.GoodsSupplierReagentSpecsService;
import com.ebig.hdi.modules.sys.controller.AbstractController;

/**
 * 供应商试剂规格
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:28:15
 */
@RestController
@RequestMapping("/reagent/goodssupplierreagentspecs")
public class GoodsSupplierReagentSpecsController extends AbstractController {
    @Autowired
    private GoodsSupplierReagentSpecsService goodsSupplierReagentSpecsService;

    /**
     * 列表
     */
    @PostMapping("/list")
    //@RequiresPermissions("reagent:goodssupplierreagentspecs:list")
    public Hdi list(@RequestBody Map<String, Object> params){
        PageUtils page = goodsSupplierReagentSpecsService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 保存
     */
    @PostMapping("/update")
    //@RequiresPermissions("reagent:goodssupplierreagentspecs:update")
    public Hdi update(@RequestBody List<GoodsSupplierReagentSpecsEntity> goodsSupplierReagentSpecsList){
    	for (GoodsSupplierReagentSpecsEntity goodsSupplierReagentSpecs : goodsSupplierReagentSpecsList) {
			ValidatorUtils.validateEntity(goodsSupplierReagentSpecs);
			if (goodsSupplierReagentSpecs.getId() != null) {
				goodsSupplierReagentSpecs.setEditId(getUserId());
			} else {
				goodsSupplierReagentSpecs.setCreateId(getUserId());
			}
		}
    	goodsSupplierReagentSpecsService.insertOrUpdate(goodsSupplierReagentSpecsList);

        return Hdi.ok();
    }

}
