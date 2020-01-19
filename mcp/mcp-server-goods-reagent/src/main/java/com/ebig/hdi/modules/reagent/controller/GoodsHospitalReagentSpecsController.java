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
import com.ebig.hdi.modules.reagent.entity.GoodsHospitalReagentSpecsEntity;
import com.ebig.hdi.modules.reagent.service.GoodsHospitalReagentSpecsService;
import com.ebig.hdi.modules.sys.controller.AbstractController;

/**
 * 医院试剂规格
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:29:52
 */
@RestController
@RequestMapping("/reagent/goodshospitalreagentspecs")
public class GoodsHospitalReagentSpecsController extends AbstractController {
    @Autowired
    private GoodsHospitalReagentSpecsService goodsHospitalReagentSpecsService;

    /**
     * 列表
     */
    @PostMapping("/list")
    //@RequiresPermissions("reagent:goodshospitalreagentspecs:list")
    public Hdi list(@RequestBody Map<String, Object> params){
        PageUtils page = goodsHospitalReagentSpecsService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 编辑
     */
    @PostMapping("/update")
    //@RequiresPermissions("reagent:goodshospitalreagentspecs:update")
    public Hdi update(@RequestBody List<GoodsHospitalReagentSpecsEntity> goodsHospitalReagentSpecsList){
    	for (GoodsHospitalReagentSpecsEntity goodsHospitalReagentSpecs : goodsHospitalReagentSpecsList) {
			ValidatorUtils.validateEntity(goodsHospitalReagentSpecs);
			if (goodsHospitalReagentSpecs.getId() != null) {
				goodsHospitalReagentSpecs.setEditId(getUserId());
			} else {
				goodsHospitalReagentSpecs.setCreateId(getUserId());
			}
		}
    	goodsHospitalReagentSpecsService.insertOrUpdate(goodsHospitalReagentSpecsList);

        return Hdi.ok();
    }

}
