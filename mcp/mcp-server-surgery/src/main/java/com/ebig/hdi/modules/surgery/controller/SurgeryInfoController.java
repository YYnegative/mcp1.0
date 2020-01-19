package com.ebig.hdi.modules.surgery.controller;

import java.util.Arrays;
import java.util.List;
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
import com.ebig.hdi.modules.surgery.entity.SurgeryInfoEntity;
import com.ebig.hdi.modules.surgery.entity.vo.SurgeryInfoVO;
import com.ebig.hdi.modules.surgery.entity.vo.SurgeryStageInfoVO;
import com.ebig.hdi.modules.surgery.service.SurgeryInfoService;
import com.ebig.hdi.modules.sys.controller.AbstractController;

/**
 * 手术信息表
 *
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-06-11 10:15:32
 */
@RestController
@RequestMapping("/surgery/surgeryinfo")
public class SurgeryInfoController extends AbstractController {
    @Autowired
    private SurgeryInfoService surgeryInfoService;

    /**
     * 列表
     */
    @PostMapping("/list")
    //@RequiresPermissions("surgery:surgeryinfo:list")
    public Hdi list(@RequestBody Map<String, Object> params){
        PageUtils page = surgeryInfoService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("surgery:surgeryinfo:info")
    public Hdi info(@PathVariable("id") Long id){
        SurgeryInfoVO surgeryInfoVO = surgeryInfoService.selectById(id);

        return Hdi.ok().put("surgeryInfo", surgeryInfoVO);
    }
    
    /**
     * 生成跟台目录主单
     */
    @GetMapping("/createStage/{id}")
    //@RequiresPermissions("surgery:surgeryinfo:createStage")
    public Hdi createStage(@PathVariable("id") Long id){
    	SurgeryStageInfoVO surgeryStageInfoVO = surgeryInfoService.createStage(id);

        return Hdi.ok().put("surgeryStageInfo", surgeryStageInfoVO);
    }
    
    @PostMapping("/querySurgeryNo")
    //@RequiresPermissions("surgery:surgeryinfo:querySurgeryNo")
    public Hdi querySurgeryNo(@RequestBody Map<String, Object> params){
    	List<SurgeryInfoEntity> surgeryList = surgeryInfoService.querySurgeryNo(params);
    	
    	return Hdi.ok().put("surgeryInfoList", surgeryList);
    }
    
    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("surgery:surgeryinfo:save")
    public Hdi save(@RequestBody SurgeryInfoEntity surgeryInfo){
        surgeryInfoService.insert(surgeryInfo);

        return Hdi.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("surgery:surgeryinfo:update")
    public Hdi update(@RequestBody SurgeryInfoEntity surgeryInfo){
        ValidatorUtils.validateEntity(surgeryInfo);
        surgeryInfoService.updateAllColumnById(surgeryInfo);//全部更新
        
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("surgery:surgeryinfo:delete")
    public Hdi delete(@RequestBody Long[] ids){
        surgeryInfoService.deleteBatchIds(Arrays.asList(ids));

        return Hdi.ok();
    }

}
