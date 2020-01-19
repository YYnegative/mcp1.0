package com.ebig.hdi.modules.license.controller;

import java.util.Arrays;
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
import com.ebig.hdi.modules.license.entity.vo.LicenseGoodsInfoVO;
import com.ebig.hdi.modules.license.service.LicenseGoodsInfoService;

/**
 * 商品证照信息
 *
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-05-21 17:26:30
 */
@RestController
@RequestMapping("/license/licensegoodsinfo")
public class LicenseGoodsInfoController {
    @Autowired
    private LicenseGoodsInfoService licenseGoodsInfoService;

    /**
     * 列表
     */
    @PostMapping("/list")
    //@RequiresPermissions("license:licensegoodsinfo:list")
    public Hdi list(@RequestBody Map<String, Object> params){
        PageUtils page = licenseGoodsInfoService.queryPage(params);

        return Hdi.ok().put("page", page);
    }
    
    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    //@RequiresPermissions("license:licensegoodsinfo:info")
    public Hdi info(@PathVariable("id") Long id){
    	LicenseGoodsInfoVO lgiVO = licenseGoodsInfoService.selectById(id);

        return Hdi.ok().put("licenseGoodsInfoEntity", lgiVO);
    }

    /**
     * 详情
     */
    @GetMapping("/details/{id}")
    //@RequiresPermissions("license:licensegoodsinfo:info")
    public Hdi details(@PathVariable("id") Long id){
        List<LicenseGoodsInfoVO> list = licenseGoodsInfoService.details(id);

        return Hdi.ok().put("list", list);
    }
    
    /**
     * 查询所有商品
     */
    @GetMapping("/allGoods/{id}")
    //@RequiresPermissions("license:licensegoodsinfo:allGoods")
    public Hdi allGoods(@PathVariable("id") Long id){
        List<Map<String,Object>> list = licenseGoodsInfoService.allGoods(id);

        return Hdi.ok().put("list", list);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    //@RequiresPermissions("license:licensegoodsinfo:save")
    public Hdi save(@RequestBody LicenseGoodsInfoVO licenseGoodsInfoVO){
        licenseGoodsInfoService.save(licenseGoodsInfoVO);
        return Hdi.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    //@RequiresPermissions("license:licensegoodsinfo:update")
    public Hdi update(@RequestBody LicenseGoodsInfoVO licenseGoodsInfoVO){
        licenseGoodsInfoService.update(licenseGoodsInfoVO);
        return Hdi.ok();
    }
    
    /**
     * 换证
     */
    @PostMapping("/replace")
    //@RequiresPermissions("license:licensegoodsinfo:update")
    public Hdi replace(@RequestBody LicenseGoodsInfoVO licenseGoodsInfoVO){
        licenseGoodsInfoService.replace(licenseGoodsInfoVO);
        return Hdi.ok();
    }
    
    /**
     * 提交审批
     */
    @PostMapping("/examine")
    //@RequiresPermissions("license:licensegoodsinfo:examine")
    public Hdi submitExamine(@RequestBody Map<String, Object> params){
        licenseGoodsInfoService.examine(params);
        return Hdi.ok();
    }
    
    /**
     * 审批详情
     */
    @PostMapping("/examineInfo")
    //@RequiresPermissions("license:licensegoodsinfo:examineInfo")
    public Hdi examineInfo(@RequestBody Map<String, Object> params){
    	PageUtils page = licenseGoodsInfoService.examineInfo(params);

        return Hdi.ok().put("page", page);
    }
    
    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("license:licensegoodsinfo:delete")
    public Hdi delete(@RequestBody Long[] ids){
        licenseGoodsInfoService.deleteBatchIds(Arrays.asList(ids));

        return Hdi.ok();
    }

}
