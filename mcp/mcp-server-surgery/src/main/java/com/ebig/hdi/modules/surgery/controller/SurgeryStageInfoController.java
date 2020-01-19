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
import com.ebig.hdi.modules.surgery.entity.SurgeryStageInfoEntity;
import com.ebig.hdi.modules.surgery.entity.vo.SurgeryStageInfoVO;
import com.ebig.hdi.modules.surgery.service.SurgeryStageInfoService;
import com.ebig.hdi.modules.sys.controller.AbstractController;

/**
 * 手术跟台目录表
 *
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-06-11 10:15:31
 */
@RestController
@RequestMapping("/surgery/surgerystageinfo")
public class SurgeryStageInfoController extends AbstractController {
    @Autowired
    private SurgeryStageInfoService surgeryStageInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("surgery:surgerystageinfo:list")
    public Hdi list(@RequestBody Map<String, Object> params){
        PageUtils page = surgeryStageInfoService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("surgery:surgerystageinfo:info")
    public Hdi info(@PathVariable("id") Long id){
        SurgeryStageInfoVO stageInfoVO = surgeryStageInfoService.selectById(id);

        return Hdi.ok().put("surgeryStageInfo", stageInfoVO);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("surgery:surgerystageinfo:save")
    public Hdi save(@RequestBody SurgeryStageInfoVO stageInfoVO){
    	stageInfoVO.setCreateId(getUserId());
    	stageInfoVO.setDeptId(getDeptId());
        surgeryStageInfoService.save(stageInfoVO);

        return Hdi.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("surgery:surgerystageinfo:update")
    public Hdi update(@RequestBody SurgeryStageInfoVO stageInfoVO){
        //ValidatorUtils.validateEntity(surgeryStageInfo);
        surgeryStageInfoService.update(stageInfoVO);
        
        return Hdi.ok();
    }
    
    /**
     * 提交
     */
    @RequestMapping("/submit")
    //@RequiresPermissions("surgery:surgerystageinfo:submit")
    public Hdi submit(@RequestBody Long[] ids){
        surgeryStageInfoService.submit(Arrays.asList(ids));

        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("surgery:surgerystageinfo:delete")
    public Hdi delete(@RequestBody Long[] ids){
        surgeryStageInfoService.deleteBatchIds(Arrays.asList(ids));

        return Hdi.ok();
    }

}
