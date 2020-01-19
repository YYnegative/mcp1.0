package com.ebig.hdi.modules.license.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.license.service.LicenseWarningService;

/**
 * VIEW
 *
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-05-28 09:22:55
 */
@RestController
@RequestMapping("license/licensewarning")
public class LicenseWarningController {
    @Autowired
    private LicenseWarningService licenseWarningService;

    /**
     * 列表
     */
    @PostMapping("/list")
    //@RequiresPermissions("license:licensewarning:list")
    public Hdi list(@RequestBody Map<String, Object> params){
        PageUtils page = licenseWarningService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


}
