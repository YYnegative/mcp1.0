package com.ebig.hdi.modules.core.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.modules.core.entity.CoreSupplyDetailEntity;
import com.ebig.hdi.modules.core.service.CoreSupplyDetailService;

/**
 * 
 *
 * @author frink
 * @email 
 * @date 2019-05-25 12:01:14
 */
@RestController
@RequestMapping("/core/coresupplydetail")
public class CoreSupplyDetailController {
    @Autowired
    private CoreSupplyDetailService coreSupplyDetailService;

    /**
     * 列表
     */
    @RequestMapping("/listDetail")
    //@RequiresPermissions("core:coresupplydetail:list")
    public Hdi list(@RequestBody Map<String, Object> params){
        PageUtils page = coreSupplyDetailService.queryPageDetail(params);

        return Hdi.ok().put("page", page);
    }
    
    
    /**
     * 列表
     */
    @RequestMapping("/listDetailBatchCode")
    //@RequiresPermissions("core:coresupplydetail:list")
    public Hdi listlistDetail(@RequestBody CoreSupplyDetailEntity coreSupplyDetail){
        List<CoreSupplyDetailEntity> list = coreSupplyDetailService.queryDetailBatchCode(coreSupplyDetail);

        return Hdi.ok().put("list", list);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{supplyDetailId}")
    @RequiresPermissions("core:coresupplydetail:info")
    public Hdi info(@PathVariable("supplyDetailId") Long supplyDetailId){
        CoreSupplyDetailEntity coreSupplyDetail = coreSupplyDetailService.selectById(supplyDetailId);

        return Hdi.ok().put("coreSupplyDetail", coreSupplyDetail);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("core:coresupplydetail:save")
    public Hdi save(@RequestBody CoreSupplyDetailEntity coreSupplyDetail){
        coreSupplyDetailService.insert(coreSupplyDetail);

        return Hdi.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("core:coresupplydetail:update")
    public Hdi update(@RequestBody CoreSupplyDetailEntity coreSupplyDetail){
        ValidatorUtils.validateEntity(coreSupplyDetail);
        coreSupplyDetailService.updateAllColumnById(coreSupplyDetail);//全部更新
        
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("core:coresupplydetail:delete")
    public Hdi delete(@RequestBody Long[] supplyDetailIds){
        coreSupplyDetailService.deleteBatchIds(Arrays.asList(supplyDetailIds));

        return Hdi.ok();
    }

}
