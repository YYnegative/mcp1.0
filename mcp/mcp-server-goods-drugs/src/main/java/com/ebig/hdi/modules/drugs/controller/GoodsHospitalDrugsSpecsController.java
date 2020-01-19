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
import com.ebig.hdi.modules.drugs.entity.GoodsHospitalDrugsSpecsEntity;
import com.ebig.hdi.modules.drugs.service.GoodsHospitalDrugsSpecsService;
import com.ebig.hdi.modules.sys.controller.AbstractController;

/**
 * 医院药品规格
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-08 14:22:44
 */
@RestController
@RequestMapping("drugs/goodshospitaldrugsspecs")
public class GoodsHospitalDrugsSpecsController extends AbstractController {
    @Autowired
    private GoodsHospitalDrugsSpecsService goodsHospitalDrugsSpecsService;

    /**
     * 列表
     */
    @PostMapping("/list")
    //@RequiresPermissions("drugs:goodshospitaldrugsspecs:list")
    public Hdi list(@RequestBody Map<String, Object> params){
        PageUtils page = goodsHospitalDrugsSpecsService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("drugs:goodshospitaldrugsspecs:info")
    public Hdi info(@PathVariable("id") Long id){
        GoodsHospitalDrugsSpecsEntity goodsHospitalDrugsSpecs = goodsHospitalDrugsSpecsService.selectById(id);

        return Hdi.ok().put("goodsHospitalDrugsSpecs", goodsHospitalDrugsSpecs);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    //@RequiresPermissions("drugs:goodshospitaldrugsspecs:save")
    public Hdi save(@RequestBody GoodsHospitalDrugsSpecsEntity goodsHospitalDrugsSpecs){
    	ValidatorUtils.validateEntity(goodsHospitalDrugsSpecs, AddGroup.class);
    	goodsHospitalDrugsSpecs.setCreateId(getUserId());
        goodsHospitalDrugsSpecsService.save(goodsHospitalDrugsSpecs);

        return Hdi.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    //@RequiresPermissions("drugs:goodshospitaldrugsspecs:update")
    public Hdi update(@RequestBody List<GoodsHospitalDrugsSpecsEntity> goodsHospitalDrugsSpecsList){
    	for (GoodsHospitalDrugsSpecsEntity goodsHospitalDrugsSpecs : goodsHospitalDrugsSpecsList) {
			ValidatorUtils.validateEntity(goodsHospitalDrugsSpecs, UpdateGroup.class);
			if (goodsHospitalDrugsSpecs.getId() != null) {
				goodsHospitalDrugsSpecs.setEditId(getUserId());
			} else {
				goodsHospitalDrugsSpecs.setCreateId(getUserId());
			}
		}
    	goodsHospitalDrugsSpecsService.insertOrUpdate(goodsHospitalDrugsSpecsList);
        
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("drugs:goodshospitaldrugsspecs:delete")
    public Hdi delete(@RequestBody Long[] ids){
        goodsHospitalDrugsSpecsService.deleteBatchIds(Arrays.asList(ids));

        return Hdi.ok();
    }

}
