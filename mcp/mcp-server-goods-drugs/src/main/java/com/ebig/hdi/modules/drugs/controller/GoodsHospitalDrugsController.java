package com.ebig.hdi.modules.drugs.controller;

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
import com.ebig.hdi.modules.drugs.service.GoodsHospitalDrugsService;
import com.ebig.hdi.modules.drugs.vo.GoodsHospitalDrugsEntityVo;
import com.ebig.hdi.modules.sys.controller.AbstractController;

/**
 * 医院药品信息
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-08 14:22:45
 */
@RestController
@RequestMapping("drugs/goodshospitaldrugs")
public class GoodsHospitalDrugsController extends AbstractController{
    @Autowired
    private GoodsHospitalDrugsService goodsHospitalDrugsService;

    /**
     * 列表
     */
    @PostMapping("/list")
    //@RequiresPermissions("drugs:goodshospitaldrugs:list")
    public Hdi list(@RequestBody Map<String, Object> params){
        PageUtils page = goodsHospitalDrugsService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    //@RequiresPermissions("drugs:goodshospitaldrugs:info")
    public Hdi info(@PathVariable("id") Long id){
        GoodsHospitalDrugsEntityVo goodsHospitalDrugs = goodsHospitalDrugsService.selectHospitalDrugsById(id);

        return Hdi.ok().put("goodsHospitalDrugs", goodsHospitalDrugs);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    //@RequiresPermissions("drugs:goodshospitaldrugs:save")
    public Hdi save(@RequestBody GoodsHospitalDrugsEntityVo goodsHospitalDrugsEntityVo){
    	ValidatorUtils.validateEntity(goodsHospitalDrugsEntityVo, AddGroup.class);
    	goodsHospitalDrugsEntityVo.setCreateId(getUserId());
    	goodsHospitalDrugsService.save(goodsHospitalDrugsEntityVo);;

        return Hdi.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    //@RequiresPermissions("drugs:goodshospitaldrugs:update")
    public Hdi update(@RequestBody GoodsHospitalDrugsEntityVo goodsHospitalDrugsEntityVo){
        ValidatorUtils.validateEntity(goodsHospitalDrugsEntityVo, UpdateGroup.class);
        goodsHospitalDrugsEntityVo.setEditId(getUserId());
        goodsHospitalDrugsService.update(goodsHospitalDrugsEntityVo);
        
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    //@RequiresPermissions("drugs:goodshospitaldrugs:delete")
    public Hdi delete(@RequestBody Long[] ids){
        goodsHospitalDrugsService.delete(ids);

        return Hdi.ok();
    }
    
    /**
     * 启用停用
     */
    @PostMapping("/toggle")
    //@RequiresPermissions("drugs:goodshospitaldrugs:toggle")
    public Hdi toggle(@RequestBody Map<String, Object> params){
        goodsHospitalDrugsService.toggle(params);

        return Hdi.ok();
    }

}
