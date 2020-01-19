package com.ebig.hdi.modules.core.controller;

import java.util.Arrays;
import java.util.Map;

import com.ebig.hdi.common.enums.LabelTypeEnum;
import com.ebig.hdi.common.enums.SysConfigEnum;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.core.entity.CoreLabelSizeEntity;
import com.ebig.hdi.modules.core.service.CoreLabelSizeService;
import com.ebig.hdi.modules.sys.controller.AbstractController;
import com.ebig.hdi.modules.sys.service.SysConfigService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.validator.ValidatorUtils;

/**
 * 
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2020-01-08 10:08:48
 */
@RestController
@RequestMapping("/corelabelsize")
public class CoreLabelSizeController extends AbstractController {
    @Autowired
    private CoreLabelSizeService coreLabelSizeService;
    @Autowired
    private SysConfigService sysConfigService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public Hdi list(@RequestParam Map<String, Object> params){
        PageUtils page = coreLabelSizeService.queryPage(params);

        return Hdi.ok().put("page", page);
    }



    /**
     * 信息
     */
    @PostMapping("/info")
    public Hdi info(@RequestBody Map<String, Object> params){
        CoreLabelSizeEntity coreLabelSize = coreLabelSizeService.selectByUserIdAndTypeId(params);
        return Hdi.ok().put("coreLabelSize", coreLabelSize);
    }

    /**
     * 保存
     */
    @PostMapping("/saveorupdate")
    public Hdi save(@RequestBody CoreLabelSizeEntity coreLabelSize){
        coreLabelSizeService.insertOrUpdateByUserIdAndTypeId(coreLabelSize,getUser());
        return Hdi.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("license:corelabelsize:update")
    public Hdi update(@RequestBody CoreLabelSizeEntity coreLabelSize){
        ValidatorUtils.validateEntity(coreLabelSize);
        coreLabelSizeService.updateAllColumnById(coreLabelSize);//全部更新
        
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("license:corelabelsize:delete")
    public Hdi delete(@RequestBody Long[] ids){
        coreLabelSizeService.deleteBatchIds(Arrays.asList(ids));

        return Hdi.ok();
    }

}
