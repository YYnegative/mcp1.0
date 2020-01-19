package com.ebig.hdi.modules.core.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.modules.core.entity.CoreStorehouseEntity;
import com.ebig.hdi.modules.core.service.CoreStorehouseService;

/**
 * 
 *
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-06-17 10:45:47
 */
@RestController
@RequestMapping("/core/corestorehouse")
public class CoreStorehouseController {
    @Autowired
    private CoreStorehouseService coreStorehouseService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("core:corestorehouse:list")
    public Hdi list(@RequestParam Map<String, Object> params){
        PageUtils page = coreStorehouseService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{storehouseid}")
    @RequiresPermissions("core:corestorehouse:info")
    public Hdi info(@PathVariable("storehouseid") Long storehouseid){
        CoreStorehouseEntity coreStorehouse = coreStorehouseService.selectById(storehouseid);

        return Hdi.ok().put("coreStorehouse", coreStorehouse);
    }
    
    /**
     * 信息
     */
    @GetMapping("/queryAllByHospitalId/{id}")
    //@RequiresPermissions("core:corestorehouse:queryAllByHospitalId")
    public Hdi queryAllByHospitalId(@PathVariable("id") Long id){
        List<CoreStorehouseEntity> storeHouseList = coreStorehouseService.queryAllByHospitalId(id);

        return Hdi.ok().put("storeHouseList", storeHouseList);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("core:corestorehouse:save")
    public Hdi save(@RequestBody CoreStorehouseEntity coreStorehouse){
        coreStorehouseService.insert(coreStorehouse);

        return Hdi.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("core:corestorehouse:update")
    public Hdi update(@RequestBody CoreStorehouseEntity coreStorehouse){
        ValidatorUtils.validateEntity(coreStorehouse);
        coreStorehouseService.updateAllColumnById(coreStorehouse);//全部更新
        
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("core:corestorehouse:delete")
    public Hdi delete(@RequestBody Long[] storehouseids){
        coreStorehouseService.deleteBatchIds(Arrays.asList(storehouseids));

        return Hdi.ok();
    }

}
