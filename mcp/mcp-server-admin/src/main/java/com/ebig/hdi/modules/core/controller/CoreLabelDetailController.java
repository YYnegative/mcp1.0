package com.ebig.hdi.modules.core.controller;

import java.util.Arrays;
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
import com.ebig.hdi.modules.core.entity.CoreLabelDetailEntity;
import com.ebig.hdi.modules.core.service.CoreLabelDetailService;

/**
 * 
 *
 * @author frink
 * @email 
 * @date 2019-05-25 12:01:15
 */
@RestController
@RequestMapping("/core/corelabeldetail")
public class CoreLabelDetailController {
    @Autowired
    private CoreLabelDetailService coreLabelDetailService;

    /**
     * 列表
     */
    @RequestMapping("/listDetail")
    //@RequiresPermissions("core:corelabeldetail:list")
    public Hdi listDetail(@RequestBody Map<String, Object> params){
        PageUtils page = coreLabelDetailService.queryPageDetail(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{labeldtlid}")
    @RequiresPermissions("core:corelabeldetail:info")
    public Hdi info(@PathVariable("labeldtlid") Long labeldtlid){
        CoreLabelDetailEntity coreLabelDetail = coreLabelDetailService.selectById(labeldtlid);

        return Hdi.ok().put("coreLabelDetail", coreLabelDetail);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("core:corelabeldetail:save")
    public Hdi save(@RequestBody CoreLabelDetailEntity coreLabelDetail){
        coreLabelDetailService.insert(coreLabelDetail);

        return Hdi.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("core:corelabeldetail:update")
    public Hdi update(@RequestBody CoreLabelDetailEntity coreLabelDetail){
        ValidatorUtils.validateEntity(coreLabelDetail);
        coreLabelDetailService.updateAllColumnById(coreLabelDetail);//全部更新
        
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("core:corelabeldetail:delete")
    public Hdi delete(@RequestBody Long[] labeldtlids){
        coreLabelDetailService.deleteBatchIds(Arrays.asList(labeldtlids));

        return Hdi.ok();
    }

}
