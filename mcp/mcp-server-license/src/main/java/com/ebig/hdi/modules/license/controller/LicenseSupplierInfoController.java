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
import com.ebig.hdi.modules.license.entity.LicenseSupplierInfoEntity;
import com.ebig.hdi.modules.license.service.LicenseSupplierInfoService;
import com.ebig.hdi.modules.license.vo.LicenseSupplierInfoEntityVo;

/**
 * 供应商证照信息
 *
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-05-21 17:26:30
 */
@RestController
@RequestMapping("/license/licensesupplierinfo")
public class LicenseSupplierInfoController {
    @Autowired
    private LicenseSupplierInfoService licenseSupplierInfoService;

    /**
     * 列表
     */
    @PostMapping("/list")
    //@RequiresPermissions("license:licensesupplierinfo:list")
    public Hdi list(@RequestBody Map<String, Object> params){
        PageUtils page = licenseSupplierInfoService.queryPage(params);

        return Hdi.ok().put("page", page);
    }
    
    /**
     * 供应商商品关联证照详情
     */
    @PostMapping("/goods/details")
    public Hdi goodsDetails(@RequestBody Map<String, Object> params){
        Map<String,Object> map = licenseSupplierInfoService.goodsDetails(params);

        return Hdi.ok().put("map", map);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    //@RequiresPermissions("license:licensesupplierinfo:info")
    public Hdi info(@PathVariable("id") Long id){
    	LicenseSupplierInfoEntityVo licenseSupplierInfo = licenseSupplierInfoService.selectLicenseSupplierInfoById(id);
        List<LicenseSupplierInfoEntityVo> list = licenseSupplierInfoService.selectRelativesById(id);

        return Hdi.ok().put("licenseSupplierInfo", licenseSupplierInfo).put("list", list);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    //@RequiresPermissions("license:licensesupplierinfo:save")
    public Hdi save(@RequestBody LicenseSupplierInfoEntity licenseSupplierInfo){
    	ValidatorUtils.validateEntity(licenseSupplierInfo);
        licenseSupplierInfoService.save(licenseSupplierInfo);

        return Hdi.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    //@RequiresPermissions("license:licensesupplierinfo:update")
    public Hdi update(@RequestBody LicenseSupplierInfoEntity licenseSupplierInfo){
        ValidatorUtils.validateEntity(licenseSupplierInfo);
        licenseSupplierInfoService.update(licenseSupplierInfo);
        
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    //@RequiresPermissions("license:licensesupplierinfo:delete")
    public Hdi delete(@RequestBody Long[] ids){
        licenseSupplierInfoService.delete(ids);

        return Hdi.ok();
    }
    
    /**
     * 提交审批
     */
    @PostMapping("/examine")
    //@RequiresPermissions("license:licensesupplierinfo:examine")
    public Hdi examine(@RequestBody Map<String, Object> params){
    	licenseSupplierInfoService.examine(params);

        return Hdi.ok();
    }
    
    /**
     * 换证
     */
    @PostMapping("/replace")
    //@RequiresPermissions("license:licensesupplierinfo:update")
    public Hdi replace(@RequestBody LicenseSupplierInfoEntity licenseSupplierInfo){
    	licenseSupplierInfoService.replace(licenseSupplierInfo);

        return Hdi.ok();
    }
    
    /**
     * 医院审批详情列表
     */
    @PostMapping("/examineInfo")
    //@RequiresPermissions("license:licensehospitalexamine:examineInfo")
    public Hdi examineInfo(@RequestBody Map<String, Object> params){
        PageUtils page = licenseSupplierInfoService.examineInfo(params);

        return Hdi.ok().put("page", page);
    }

}
