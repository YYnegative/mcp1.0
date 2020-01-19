package com.ebig.hdi.modules.reagent.controller;

import java.util.Arrays;
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
import com.ebig.hdi.modules.reagent.service.GoodsHospitalReagentService;
import com.ebig.hdi.modules.reagent.vo.GoodsHospitalReagentVO;
import com.ebig.hdi.modules.sys.controller.AbstractController;

/**
 * 医院试剂信息
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:30:58
 */
@RestController
@RequestMapping("/reagent/goodshospitalreagent")
public class GoodsHospitalReagentController extends AbstractController {
    @Autowired
    private GoodsHospitalReagentService goodsHospitalReagentService;
    /**
     * 列表
     */
    @PostMapping("/list")
    //@RequiresPermissions("reagent:goodshospitalreagent:list")
    public Hdi list(@RequestBody Map<String, Object> params){
        PageUtils page = goodsHospitalReagentService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    //@RequiresPermissions("reagent:goodshospitalreagent:info")
    public Hdi info(@PathVariable("id") Long id){
    	GoodsHospitalReagentVO goodsHospitalReagent = goodsHospitalReagentService.selectById(id);

        return Hdi.ok().put("goodsHospitalReagent", goodsHospitalReagent);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    //@RequiresPermissions("reagent:goodshospitalreagent:save")
    public Hdi save(@RequestBody GoodsHospitalReagentVO goodsHospitalReagentVO){
    	ValidatorUtils.validateEntity(goodsHospitalReagentVO, AddGroup.class);
    	goodsHospitalReagentVO.setDeptId(getDeptId());
    	goodsHospitalReagentVO.setCreateId(getUserId());
        goodsHospitalReagentService.save(goodsHospitalReagentVO);

        return Hdi.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    //@RequiresPermissions("reagent:goodshospitalreagent:update")
    public Hdi update(@RequestBody GoodsHospitalReagentVO goodsHospitalReagentVO){
        ValidatorUtils.validateEntity(goodsHospitalReagentVO, UpdateGroup.class);
        goodsHospitalReagentVO.setEditId(getUserId());
        goodsHospitalReagentService.update(goodsHospitalReagentVO);
        
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("reagent:goodshospitalreagent:delete")
    public Hdi delete(@RequestBody Long[] ids){
        goodsHospitalReagentService.deleteBatchIds(Arrays.asList(ids));

        return Hdi.ok();
    }
    
    /**
     * 启用停用
     */
    @PostMapping("/toggle")
    //@RequiresPermissions("reagent:goodshospitalreagent:toggle")
    public Hdi toggle(@RequestBody Map<String, Object> params){
    	goodsHospitalReagentService.toggle(params);

        return Hdi.ok();
    }

}
