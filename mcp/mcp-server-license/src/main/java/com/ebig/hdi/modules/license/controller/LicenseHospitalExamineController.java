package com.ebig.hdi.modules.license.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.modules.license.entity.LicenseHospitalExamineEntity;
import com.ebig.hdi.modules.license.service.LicenseHospitalExamineService;
import com.ebig.hdi.modules.sys.controller.AbstractController;

/**
 * 证照医院审批
 *
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-05-21 17:26:30
 */
@RestController
@RequestMapping("license/licensehospitalexamine")
public class LicenseHospitalExamineController {
    @Autowired
    private LicenseHospitalExamineService licenseHospitalExamineService;

    /**
     * 列表
     */
    @PostMapping("/list")
    //@RequiresPermissions("license:licensehospitalexamine:list")
    public Hdi list(@RequestBody Map<String, Object> params){
        PageUtils page = licenseHospitalExamineService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("license:licensehospitalexamine:info")
    public Hdi info(@PathVariable("id") Long id){
        LicenseHospitalExamineEntity licenseHospitalExamine = licenseHospitalExamineService.selectById(id);

        return Hdi.ok().put("licenseHospitalExamine", licenseHospitalExamine);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("license:licensehospitalexamine:save")
    public Hdi save(@RequestBody LicenseHospitalExamineEntity licenseHospitalExamine){
    	ValidatorUtils.validateEntity(licenseHospitalExamine);
        licenseHospitalExamineService.save(licenseHospitalExamine);

        return Hdi.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("license:licensehospitalexamine:update")
    public Hdi update(@RequestBody LicenseHospitalExamineEntity licenseHospitalExamine){
        ValidatorUtils.validateEntity(licenseHospitalExamine);
        licenseHospitalExamineService.updateAllColumnById(licenseHospitalExamine);//全部更新
        
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("license:licensehospitalexamine:delete")
    public Hdi delete(@RequestBody Long[] ids){
        licenseHospitalExamineService.deleteBatchIds(Arrays.asList(ids));

        return Hdi.ok();
    }

}
