package com.ebig.hdi.modules.surgery.controller;

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
import com.ebig.hdi.modules.surgery.entity.SalesInfoEntity;
import com.ebig.hdi.modules.surgery.entity.vo.SalesInfoVO;
import com.ebig.hdi.modules.surgery.service.SalesInfoService;
import com.ebig.hdi.modules.sys.controller.AbstractController;

/**
 * 销售表
 *
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-06-11 10:15:32
 */
@RestController
@RequestMapping("/surgery/salesinfo")
public class SalesInfoController extends AbstractController {
    @Autowired
    private SalesInfoService salesInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("surgery:salesinfo:list")
    public Hdi list(@RequestBody Map<String, Object> params){
        PageUtils page = salesInfoService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("surgery:salesinfo:info")
    public Hdi info(@PathVariable("id") Long id){
        SalesInfoEntity salesInfo = salesInfoService.selectById(id);

        return Hdi.ok().put("salesInfo", salesInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("surgery:salesinfo:save")
    public Hdi save(@RequestBody SalesInfoVO salesInfoVO){
    	salesInfoVO.setCreateId(getUserId());
    	salesInfoVO.setDeptId(getDeptId());
        salesInfoService.save(salesInfoVO);

        return Hdi.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("surgery:salesinfo:update")
    public Hdi update(@RequestBody SalesInfoVO salesInfoVO){
        //ValidatorUtils.validateEntity(salesInfo);
        salesInfoService.update(salesInfoVO);
        
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("surgery:salesinfo:delete")
    public Hdi delete(@RequestBody Long[] ids){
        salesInfoService.deleteBatchIds(Arrays.asList(ids));

        return Hdi.ok();
    }

}
