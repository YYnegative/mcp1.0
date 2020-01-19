package com.ebig.hdi.modules.core.controller;

import java.util.List;
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
import com.ebig.hdi.modules.core.entity.CoreAcceptMasterEntity;
import com.ebig.hdi.modules.core.service.CoreAcceptMasterService;
import com.ebig.hdi.modules.sys.controller.AbstractController;

/**
 * 
 *
 * @author frink
 * @email 
 * @date 2019-05-25 12:01:15
 */
@RestController
@RequestMapping("/core/coreacceptmaster")
public class CoreAcceptMasterController extends AbstractController{
    @Autowired
    private CoreAcceptMasterService coreAcceptMasterService;

    /**
     * 查询页面验收主单列表
     */
    @RequestMapping("/listMaster")
    //@RequiresPermissions("core:coreacceptmaster:list")
    public Hdi listMaster(@RequestBody Map<String, Object> params){
    	params.put("deptId", getDeptId());
        PageUtils page = coreAcceptMasterService.queryPage(params);

        return Hdi.ok().put("page", page);
    }
    
    /**
     * 页面主单列表条件查询
     */
    @RequestMapping("/listBedingungen")
    //@RequiresPermissions("core:corepurchasemaster:list")
    public Hdi listBedingungen(@RequestBody Map<String, Object> params){
    	params.put("deptId", getDeptId());
        PageUtils page = coreAcceptMasterService.bedingungenQueryPage(params);
        
        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{acceptMasterId}")
    //@RequiresPermissions("core:coreacceptmaster:info")
    public Hdi info(@PathVariable("acceptMasterId") Long acceptMasterId){
        CoreAcceptMasterEntity coreAcceptMaster = coreAcceptMasterService.selectById(acceptMasterId);

        return Hdi.ok().put("coreAcceptMaster", coreAcceptMaster);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("core:coreacceptmaster:save")
    public Hdi save(@RequestBody CoreAcceptMasterEntity coreAcceptMaster){
        coreAcceptMasterService.insert(coreAcceptMaster);

        return Hdi.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("core:coreacceptmaster:update")
    public Hdi update(@RequestBody CoreAcceptMasterEntity coreAcceptMaster){
        ValidatorUtils.validateEntity(coreAcceptMaster);
        coreAcceptMasterService.updateAllColumnById(coreAcceptMaster);//全部更新
        
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("core:coreacceptmaster:delete")
    public Hdi delete(@RequestBody List<CoreAcceptMasterEntity> listEntity){
        coreAcceptMasterService.deleteMaster(listEntity,getUser());

        return Hdi.ok();
    }

}
