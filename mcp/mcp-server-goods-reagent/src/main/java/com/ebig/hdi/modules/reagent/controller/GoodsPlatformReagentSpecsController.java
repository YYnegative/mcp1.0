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
import com.ebig.hdi.modules.reagent.entity.GoodsPlatformReagentSpecsEntity;
import com.ebig.hdi.modules.reagent.service.GoodsPlatformReagentSpecsService;
import com.ebig.hdi.modules.sys.controller.AbstractController;

/**
 * 平台试剂规格
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:28:15
 */
@RestController
@RequestMapping("/reagent/goodsplatformreagentspecs")
public class GoodsPlatformReagentSpecsController extends AbstractController {
    @Autowired
    private GoodsPlatformReagentSpecsService goodsPlatformReagentSpecsService;

    /**
     * 列表
     */
    @PostMapping("/list")
    //@RequiresPermissions("reagent:goodsplatformreagentspecs:list")
    public Hdi list(@RequestBody Map<String, Object> params){
        PageUtils page = goodsPlatformReagentSpecsService.queryPage(params);

        return Hdi.ok().put("page", page);
    }

    /**
     * 编辑
     */
    @PostMapping("/update")
    //@RequiresPermissions("reagent:goodsplatformreagentspecs:update")
    public Hdi update(@RequestBody List<GoodsPlatformReagentSpecsEntity> goodsPlatformReagentSpecsList){
    	for (GoodsPlatformReagentSpecsEntity goodsPlatformReagentSpecs : goodsPlatformReagentSpecsList) {
			ValidatorUtils.validateEntity(goodsPlatformReagentSpecs);
			if (goodsPlatformReagentSpecs.getId() != null) {
				goodsPlatformReagentSpecs.setEditId(getUserId());
			} else {
				goodsPlatformReagentSpecs.setCreateId(getUserId());
			}
		}
    	goodsPlatformReagentSpecsService.insertOrUpdate(goodsPlatformReagentSpecsList);

        return Hdi.ok();
    }
}
