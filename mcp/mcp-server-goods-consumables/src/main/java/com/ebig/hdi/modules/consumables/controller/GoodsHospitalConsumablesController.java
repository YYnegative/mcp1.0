package com.ebig.hdi.modules.consumables.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.ebig.hdi.modules.consumables.service.GoodsHospitalConsumablesService;
import com.ebig.hdi.modules.consumables.vo.GoodsHospitalConsumablesEntityVo;
import com.ebig.hdi.modules.sys.controller.AbstractController;

/**
 * 医院耗材信息
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:39:01
 */
@RestController
@RequestMapping("consumables/goodshospitalconsumables")
public class GoodsHospitalConsumablesController extends AbstractController{
    @Autowired
    private GoodsHospitalConsumablesService goodsHospitalConsumablesService;

    /**
     * 列表
     */
    @PostMapping("/list")
    //@RequiresPermissions("consumables:goodshospitalconsumables:list")
    public Hdi list(@RequestBody Map<String, Object> params){
        PageUtils page = goodsHospitalConsumablesService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    //@RequiresPermissions("consumables:goodshospitalconsumables:info")
    public Hdi info(@PathVariable("id") Long id){
        GoodsHospitalConsumablesEntityVo goodsHospitalConsumables = goodsHospitalConsumablesService.selectHospitalConsumablesById(id);

        return Hdi.ok().put("goodsHospitalConsumables", goodsHospitalConsumables);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    //@RequiresPermissions("consumables:goodshospitalconsumables:save")
    public Hdi save(@RequestBody GoodsHospitalConsumablesEntityVo goodsHospitalConsumablesVo){
    	ValidatorUtils.validateEntity(goodsHospitalConsumablesVo, AddGroup.class);
    	goodsHospitalConsumablesVo.setCreateId(getUserId());
        goodsHospitalConsumablesService.save(goodsHospitalConsumablesVo);

        return Hdi.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    //@RequiresPermissions("consumables:goodshospitalconsumables:update")
    public Hdi update(@RequestBody GoodsHospitalConsumablesEntityVo goodsHospitalConsumablesVo){
        ValidatorUtils.validateEntity(goodsHospitalConsumablesVo, UpdateGroup.class);
        goodsHospitalConsumablesVo.setEditId(getUserId());
        goodsHospitalConsumablesService.update(goodsHospitalConsumablesVo);

        return Hdi.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    //@RequiresPermissions("consumables:goodshospitalconsumables:delete")
    public Hdi delete(@RequestBody Long[] ids){
        goodsHospitalConsumablesService.delete(ids);

        return Hdi.ok();
    }
    
    /**
     * 启用停用
     */
    @PostMapping("/toggle")
    //@RequiresPermissions("drugs:goodshospitaldrugs:toggle")
    public Hdi toggle(@RequestBody Map<String, Object> params){
    	goodsHospitalConsumablesService.toggle(params);

        return Hdi.ok();
    }

}
