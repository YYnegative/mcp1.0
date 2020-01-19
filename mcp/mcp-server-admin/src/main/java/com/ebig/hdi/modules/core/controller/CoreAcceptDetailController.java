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
import com.ebig.hdi.modules.core.entity.CoreAcceptDetailEntity;
import com.ebig.hdi.modules.core.service.CoreAcceptDetailService;

/**
 * 
 *
 * @author frink
 * @email 
 * @date 2019-05-25 12:01:15
 */
@RestController
@RequestMapping("/core/coreacceptdetail")
public class CoreAcceptDetailController {
    @Autowired
    private CoreAcceptDetailService coreAcceptDetailService;

    /**
     * 查询页面验收细单列表
     */
	@RequestMapping("/listDetail")
	//@RequiresPermissions("core:corepurchasedetail:list")
	public Hdi listDetail(@RequestBody Map<String, Object> params){
	    PageUtils page = coreAcceptDetailService.queryPageDetail(params);
	
	    return Hdi.ok().put("page", page);
	}


    /**
     * 信息
     */
    @RequestMapping("/info/{acceptDetailId}")
    @RequiresPermissions("core:coreacceptdetail:info")
    public Hdi info(@PathVariable("acceptDetailId") Long acceptDetailId){
        CoreAcceptDetailEntity coreAcceptDetail = coreAcceptDetailService.selectById(acceptDetailId);

        return Hdi.ok().put("coreAcceptDetail", coreAcceptDetail);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("core:coreacceptdetail:save")
    public Hdi save(@RequestBody CoreAcceptDetailEntity coreAcceptDetail){
        coreAcceptDetailService.insert(coreAcceptDetail);

        return Hdi.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("core:coreacceptdetail:update")
    public Hdi update(@RequestBody CoreAcceptDetailEntity coreAcceptDetail){
        ValidatorUtils.validateEntity(coreAcceptDetail);
        coreAcceptDetailService.updateAllColumnById(coreAcceptDetail);//全部更新
        
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("core:coreacceptdetail:delete")
    public Hdi delete(@RequestBody Long[] acceptDetailIds){
        coreAcceptDetailService.deleteBatchIds(Arrays.asList(acceptDetailIds));

        return Hdi.ok();
    }

}
