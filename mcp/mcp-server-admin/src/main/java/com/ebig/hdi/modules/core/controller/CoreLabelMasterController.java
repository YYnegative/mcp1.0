package com.ebig.hdi.modules.core.controller;

import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.modules.core.entity.CoreLabelMasterEntity;
import com.ebig.hdi.modules.core.service.CoreLabelMasterService;
import com.ebig.hdi.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author frink
 * @email 
 * @date 2019-05-25 12:01:14
 */
@RestController
@RequestMapping("/core/corelabelmaster")
public class CoreLabelMasterController extends AbstractController {
	
    @Autowired
    private CoreLabelMasterService coreLabelMasterService;
    
    /**
     * 函数功能说明 ： 获取打印的标签PDF文件<br/>
     * 修改者名字： <br/>
     * 修改日期： <br/>
     * 修改内容：<br/>
     * 作者：roncoo-lrx <br/>
     * 参数：@param ids
     * 参数：@return
     * 参数：@throws Exception <br/>
     * return：Hdi <br/>
     */
    @PostMapping("/getLabelPdf")
    public void getLabelPdf(@RequestBody Long[] labelids, HttpServletResponse response) throws Exception{
        coreLabelMasterService.getLabelPdf(labelids,response,getUser());
    }

    /**
     * 列表
     */
    @RequestMapping("/listMaster")
    //@RequiresPermissions("core:corelabelmaster:list")
    public Hdi listMaster(@RequestBody Map<String, Object> params){
    	params.put("deptId", getDeptId());
        PageUtils page = coreLabelMasterService.queryPage(params);
        return Hdi.ok().put("page", page);
    }

    /**
     * 页面主单列表条件查询
     */
    @RequestMapping("/listBedingungen")
    //@RequiresPermissions("core:corepurchasemaster:list")
    public Hdi listBedingungen(@RequestBody Map<String, Object> params){
    	params.put("deptId", getDeptId());
        PageUtils page = coreLabelMasterService.bedingungenQueryPage(params);
        
        return Hdi.ok().put("page", page);
    }
    

    /**
     * 信息
     */
    @RequestMapping("/info/{labelid}")
    @RequiresPermissions("core:corelabelmaster:info")
    public Hdi info(@PathVariable("labelid") Long labelid){
        CoreLabelMasterEntity coreLabelMaster = coreLabelMasterService.selectById(labelid);

        return Hdi.ok().put("coreLabelMaster", coreLabelMaster);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("core:corelabelmaster:save")
    public Hdi save(@RequestBody CoreLabelMasterEntity coreLabelMaster){
        coreLabelMasterService.insert(coreLabelMaster);

        return Hdi.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("core:corelabelmaster:update")
    public Hdi update(@RequestBody CoreLabelMasterEntity coreLabelMaster){
        ValidatorUtils.validateEntity(coreLabelMaster);
        coreLabelMasterService.updateAllColumnById(coreLabelMaster);//全部更新
        
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("core:corelabelmaster:delete")
    public Hdi delete(@RequestBody List<CoreLabelMasterEntity> listEntity){
        coreLabelMasterService.deleteMaster(listEntity,getUser());

        return Hdi.ok();
    }

}
