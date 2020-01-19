package com.ebig.hdi.modules.license.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.modules.license.entity.LicenseClassifyInfoEntity;
import com.ebig.hdi.modules.license.service.LicenseClassifyInfoService;
import com.ebig.hdi.modules.sys.controller.AbstractController;

/**
 * 证照分类信息
 *
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-05-21 16:45:19
 */
@RestController
@RequestMapping("/license/licenseclassifyinfo")
public class LicenseClassifyInfoController {
    @Autowired
    private LicenseClassifyInfoService licenseClassifyInfoService;

    /**
     * 列表
     */
    @PostMapping("/list")
    //@RequiresPermissions("license:licenseclassifyinfo:list")
    public Hdi list(@RequestBody Map<String, Object> params){
        List<LicenseClassifyInfoEntity> entityList = licenseClassifyInfoService.list(params);

        return Hdi.ok().put("list", entityList);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    //@RequiresPermissions("license:licenseclassifyinfo:info")
    public Hdi info(@PathVariable("id") Long id){
        LicenseClassifyInfoEntity licenseClassifyInfo = licenseClassifyInfoService.selectById(id);

        return Hdi.ok().put("licenseClassifyInfo", licenseClassifyInfo);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    //@RequiresPermissions("license:licenseclassifyinfo:save")
    public Hdi save(@RequestBody LicenseClassifyInfoEntity licenseClassifyInfo){
        licenseClassifyInfoService.save(licenseClassifyInfo);

        return Hdi.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    //@RequiresPermissions("license:licenseclassifyinfo:update")
    public Hdi update(@RequestBody LicenseClassifyInfoEntity licenseClassifyInfo){
        ValidatorUtils.validateEntity(licenseClassifyInfo);
		if(licenseClassifyInfo.getEditId() == null){
			throw new HdiException("修改人id不能为空");
		}
        licenseClassifyInfo.setEditTime(new Date());
        licenseClassifyInfoService.updateById(licenseClassifyInfo);//全部更新
        
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    //@RequiresPermissions("license:licenseclassifyinfo:delete")
    public Hdi delete(@RequestBody Long[] ids){
        licenseClassifyInfoService.delete(ids);

        return Hdi.ok();
    }

}
