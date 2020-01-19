package com.ebig.hdi.modules.license.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import com.ebig.hdi.modules.license.entity.LicenseAgentInfoEntity;
import com.ebig.hdi.modules.license.entity.vo.LicenseAgentInfoVO;
import com.ebig.hdi.modules.license.service.LicenseAgentInfoService;

/**
 * 代理商证照信息
 *
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-05-21 17:26:29
 */
@RestController
@RequestMapping("/license/licenseagentinfo")
public class LicenseAgentInfoController {
    @Autowired
    private LicenseAgentInfoService licenseAgentInfoService;

    /**
     * 列表
     */
    @PostMapping("/list")
    //@RequiresPermissions("license:licenseagentinfo:list")
    public Hdi list(@RequestBody Map<String, Object> params){
        PageUtils page = licenseAgentInfoService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    //@RequiresPermissions("license:licenseagentinfo:info")
    public Hdi info(@PathVariable("id") Long id){
        LicenseAgentInfoEntity licenseAgentInfo = licenseAgentInfoService.selectByAgentId(id);

        return Hdi.ok().put("licenseAgentInfo", licenseAgentInfo);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    //@RequiresPermissions("license:licenseagentinfo:save")
    public Hdi save(@RequestBody LicenseAgentInfoVO licenseAgentInfoVO){
        licenseAgentInfoService.save(licenseAgentInfoVO);

        return Hdi.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    //@RequiresPermissions("license:licenseagentinfo:update")
    public Hdi update(@RequestBody LicenseAgentInfoVO licenseAgentInfoVO){
        licenseAgentInfoService.update(licenseAgentInfoVO);
        
        return Hdi.ok();
    }
    
    /**
     * 换证
     */
    @PostMapping("/replace")
    //@RequiresPermissions("license:licenseagentinfo:update")
    public Hdi replace(@RequestBody LicenseAgentInfoVO licenseAgentInfoVO){
        licenseAgentInfoService.replace(licenseAgentInfoVO);
        
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    //@RequiresPermissions("license:licenseagentinfo:delete")
    public Hdi delete(@RequestBody Long[] ids){
        licenseAgentInfoService.deleteBatchIds(Arrays.asList(ids));

        return Hdi.ok();
    }

}
