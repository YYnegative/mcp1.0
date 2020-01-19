package com.ebig.hdi.modules.drugs.controller;

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
import com.ebig.hdi.common.validator.group.AddGroup;
import com.ebig.hdi.common.validator.group.UpdateGroup;
import com.ebig.hdi.modules.drugs.entity.GoodsSupplierDrugsSpecsEntity;
import com.ebig.hdi.modules.drugs.service.GoodsSupplierDrugsSpecsService;
import com.ebig.hdi.modules.sys.controller.AbstractController;

/**
 * 供应商药品规格
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-08 14:17:50
 */
@RestController
@RequestMapping("drugs/goodssupplierdrugsspecs")
public class GoodsSupplierDrugsSpecsController extends AbstractController {
    @Autowired
    private GoodsSupplierDrugsSpecsService goodsSupplierDrugsSpecsService;

    /**
     * 列表
     */
    @PostMapping("/list")
    //@RequiresPermissions("drugs:goodssupplierdrugsspecs:list")
    public Hdi list(@RequestBody Map<String, Object> params){
        PageUtils page = goodsSupplierDrugsSpecsService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("drugs:goodssupplierdrugsspecs:info")
    public Hdi info(@PathVariable("id") Long id){
        GoodsSupplierDrugsSpecsEntity goodsSupplierDrugsSpecs = goodsSupplierDrugsSpecsService.selectById(id);

        return Hdi.ok().put("goodsSupplierDrugsSpecs", goodsSupplierDrugsSpecs);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("drugs:goodssupplierdrugsspecs:save")
    public Hdi save(@RequestBody GoodsSupplierDrugsSpecsEntity goodsSupplierDrugsSpecs){
    	ValidatorUtils.validateEntity(goodsSupplierDrugsSpecs, AddGroup.class);
    	goodsSupplierDrugsSpecs.setCreateId(getUserId());
        goodsSupplierDrugsSpecsService.save(goodsSupplierDrugsSpecs);

        return Hdi.ok();
    }

    /**
     * 编辑
     */
    @PostMapping("/update")
    //@RequiresPermissions("drugs:goodssupplierdrugsspecs:update")
    public Hdi update(@RequestBody List<GoodsSupplierDrugsSpecsEntity> goodsSupplierDrugsSpecsList){
		for (GoodsSupplierDrugsSpecsEntity goodsSupplierDrugsSpecs : goodsSupplierDrugsSpecsList) {
			ValidatorUtils.validateEntity(goodsSupplierDrugsSpecs, UpdateGroup.class);
			if (goodsSupplierDrugsSpecs.getId() != null) {
				goodsSupplierDrugsSpecs.setEditId(getUserId());
			} else {
				goodsSupplierDrugsSpecs.setCreateId(getUserId());
			}
		}
		goodsSupplierDrugsSpecsService.insertOrUpdate(goodsSupplierDrugsSpecsList);
		
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("drugs:goodssupplierdrugsspecs:delete")
    public Hdi delete(@RequestBody Long[] ids){
        goodsSupplierDrugsSpecsService.deleteBatchIds(Arrays.asList(ids));

        return Hdi.ok();
    }

}
