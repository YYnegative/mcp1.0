package com.ebig.hdi.modules.license.controller;

import java.util.List;
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
import com.ebig.hdi.modules.license.entity.LicenseFactoryInfoEntity;
import com.ebig.hdi.modules.license.service.LicenseFactoryInfoService;
import com.ebig.hdi.modules.license.vo.LicenseFactoryInfoEntityVo;

/**
 * 厂商证照信息
 *
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-05-21 17:26:29
 */
@RestController
@RequestMapping("license/licensefactoryinfo")
public class LicenseFactoryInfoController {
    @Autowired
    private LicenseFactoryInfoService licenseFactoryInfoService;

    /**
     * 列表
     */
    @PostMapping("/list")
    //@RequiresPermissions("license:licensefactoryinfo:list")
    public Hdi list(@RequestBody Map<String, Object> params){
        PageUtils page = licenseFactoryInfoService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    //@RequiresPermissions("license:licensefactoryinfo:info")
    public Hdi info(@PathVariable("id") Long id){
        LicenseFactoryInfoEntity licenseFactoryInfo = licenseFactoryInfoService.selectLicenseFactoryInfoById(id);
        List<LicenseFactoryInfoEntity> list = licenseFactoryInfoService.selectRelativesById(id);

        return Hdi.ok().put("licenseFactoryInfo", licenseFactoryInfo).put("list", list);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    //@RequiresPermissions("license:licensefactoryinfo:save")
    public Hdi save(@RequestBody LicenseFactoryInfoEntityVo licenseFactoryInfo){
    	ValidatorUtils.validateEntity(licenseFactoryInfo);
        licenseFactoryInfoService.save(licenseFactoryInfo);

        return Hdi.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    //@RequiresPermissions("license:licensefactoryinfo:update")
    public Hdi update(@RequestBody LicenseFactoryInfoEntityVo licenseFactoryInfo){
        ValidatorUtils.validateEntity(licenseFactoryInfo);
        licenseFactoryInfoService.update(licenseFactoryInfo);
        
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("license:licensefactoryinfo:delete")
    public Hdi delete(@RequestBody Long[] ids){
        licenseFactoryInfoService.delete(ids);

        return Hdi.ok();
    }

    /**
     * 换证
     */
    @PostMapping("/replace")
    //@RequiresPermissions("license:licensefactoryinfo:update")
    public Hdi replace(@RequestBody LicenseFactoryInfoEntityVo licenseFactoryInfo){
    	ValidatorUtils.validateEntity(licenseFactoryInfo);
        licenseFactoryInfoService.replace(licenseFactoryInfo);

        return Hdi.ok();
    }
}
