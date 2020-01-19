package com.ebig.hdi.modules.core.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ebig.hdi.modules.core.service.ImportCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ebig.hdi.common.entity.MasterDetailsCommonEntity;
import com.ebig.hdi.common.enums.SysConfigEnum;
import com.ebig.hdi.common.utils.ExcelUtils;
import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.modules.core.entity.CorePurchaseDetailEntity;
import com.ebig.hdi.modules.core.entity.CorePurchaseMasterEntity;
import com.ebig.hdi.modules.core.entity.CoreSupplyDetailEntity;
import com.ebig.hdi.modules.core.entity.CoreSupplyMasterEntity;
import com.ebig.hdi.modules.core.param.CorePurchaseParam;
import com.ebig.hdi.modules.core.service.CorePurchaseMasterService;
import com.ebig.hdi.modules.sys.controller.AbstractController;
import com.ebig.hdi.modules.sys.service.SysConfigService;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 *
 * @author frink
 * @email 
 * @date 2019-05-27 11:12:34
 */
@RestController
@RequestMapping("/core/corepurchasemaster")
public class CorePurchaseMasterController extends AbstractController {
    @Autowired
    private CorePurchaseMasterService corePurchaseMasterService;
    
    @Autowired
	private SysConfigService sysConfigService;

    /**
     * 页面主单列表查询
     */
    @RequestMapping("/listMaster")
    //@RequiresPermissions("core:corepurchasemaster:list")
    public Hdi listMaster(@RequestBody Map<String, Object> params){
    	params.put("deptId", getDeptId());
        PageUtils page = corePurchaseMasterService.queryPage(params);

        return Hdi.ok().put("page", page);
    }
    
    /**
     * 页面主单列表条件查询
     */
    @RequestMapping("/listBedingungen")
    //@RequiresPermissions("core:corepurchasemaster:list")
    public Hdi listBedingungen(@RequestBody Map<String, Object> params){
    	params.put("deptId", getDeptId());
        PageUtils page = corePurchaseMasterService.bedingungenQueryPage(params);
        
        return Hdi.ok().put("page", page);
    }
    
    
    /**
     * 信息
     */
    @RequestMapping("/info/{purchaseMasterId}")
    //@RequiresPermissions("core:corepurchasemaster:info")
    public Hdi info(@PathVariable("purchaseMasterId") Long purchaseMasterId){
        CorePurchaseMasterEntity corePurchaseMaster = corePurchaseMasterService.selectById(purchaseMasterId);

        return Hdi.ok().put("corePurchaseMaster", corePurchaseMaster);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("core:corepurchasemaster:save")
    public Hdi save(@RequestBody MasterDetailsCommonEntity<CorePurchaseMasterEntity, CorePurchaseDetailEntity> masterdetailsCommonEntity){
    	ValidatorUtils.validateEntity(masterdetailsCommonEntity);
    	
    	corePurchaseMasterService.save(masterdetailsCommonEntity,getDeptId(),getUserId(),getUser().getUsername(),getUser());
    	
        return Hdi.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/updatePurchaseStatus")
    //@RequiresPermissions("core:corepurchasemaster:update")
    public Hdi updatePurchaseStatus(@RequestBody CorePurchaseMasterEntity corePurchaseMaster){
        ValidatorUtils.validateEntity(corePurchaseMaster);
        corePurchaseMasterService.updatePurchaseStatus(corePurchaseMaster,getUser());//全部更新
        
        return Hdi.ok();
    }
    
    
    /**
     * 查询数据返回至前台编辑
     */
    @RequestMapping("/editList")
    //@RequiresPermissions("core:corepurchasemaster:update")
    public Hdi editList(@RequestBody CorePurchaseMasterEntity corePurchaseMaster){
        ValidatorUtils.validateEntity(corePurchaseMaster);
        MasterDetailsCommonEntity<CorePurchaseMasterEntity,CorePurchaseDetailEntity> page = corePurchaseMasterService.editList(corePurchaseMaster,getUser());

        return Hdi.ok().put("page", page);
    }    
    
    
    /**
     * 编辑保存
     */
    @RequestMapping("/edit")
    //@RequiresPermissions("core:corepurchasemaster:save")
    public Hdi edit(@RequestBody MasterDetailsCommonEntity<CorePurchaseMasterEntity, CorePurchaseDetailEntity> masterdetailsCommonEntity){
    	ValidatorUtils.validateEntity(masterdetailsCommonEntity);
    	
    	corePurchaseMasterService.edit(masterdetailsCommonEntity,getDeptId(),getUserId(),getUser().getUsername());
    	
        return Hdi.ok();
    }
    
    
    /**
     * 供货单数据
     */
    @RequestMapping("/supplyList")
    //@RequiresPermissions("core:corepurchasemaster:update")
    public Hdi supplyList(@RequestBody CorePurchaseMasterEntity corePurchaseMaster){
        ValidatorUtils.validateEntity(corePurchaseMaster);
        MasterDetailsCommonEntity<CoreSupplyMasterEntity, CorePurchaseDetailEntity> page = corePurchaseMasterService.supplyList(corePurchaseMaster,getUser());
    
        return Hdi.ok().put("page", page);
    }  
    
    
    /**
     * 供货单数据
     */
    @RequestMapping("/supplyListPurplanno")
    //@RequiresPermissions("core:corepurchasemaster:update")
    public Hdi supplyListPurplanno(@RequestBody CorePurchaseMasterEntity corePurchaseMaster){
        ValidatorUtils.validateEntity(corePurchaseMaster);
        MasterDetailsCommonEntity<CoreSupplyMasterEntity, CorePurchaseDetailEntity> page = corePurchaseMasterService.supplyList(corePurchaseMaster,getUser());
    
        return Hdi.ok().put("page", page);
    }   
    
    
    /**
     * 生成供货单
     */
    @RequestMapping("/saveSupply")
    //@RequiresPermissions("core:corepurchasemaster:save")
    public Hdi saveSupply(@RequestBody MasterDetailsCommonEntity<CoreSupplyMasterEntity,CoreSupplyDetailEntity> masterdetailsCommonEntity) throws IOException{
    	ValidatorUtils.validateEntity(masterdetailsCommonEntity);
    	
    	corePurchaseMasterService.saveSupply(masterdetailsCommonEntity,getDeptId(),getUserId(),getUser().getUsername());
    	
    	corePurchaseMasterService.updatePurchasestatus(masterdetailsCommonEntity.getMaster().getPurchaseMasterId());
    	
        return Hdi.ok();
    }
    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("core:corepurchasemaster:delete")
    public Hdi delete(@RequestBody List<CorePurchaseMasterEntity> listEntity){
        corePurchaseMasterService.deleteMaster(listEntity,getUser());

        return Hdi.ok();
    }
    
    /**
     * 根据医院采购计划编号查询采购单生成供货单
     * @param purplanno
     * @return
     */
    @RequestMapping("/infoByPurplanno")
    public Hdi infoByPurplanno(@RequestBody CorePurchaseMasterEntity purchaseMasterEntity) {
    	List<CorePurchaseMasterEntity> list = corePurchaseMasterService.getInfoByPurplanno(purchaseMasterEntity.getPurplanno(), purchaseMasterEntity.getSupplierId());
    	
    	return Hdi.ok().put("list", list);
    }

    /**
     * 导出供货单
     */
    @RequestMapping(value = "/export")
    public void export(HttpServletRequest request, HttpServletResponse response, @RequestBody CorePurchaseParam queryParam) throws IOException{
        //获取模板表头和查询字段名
        String[] columnNames = ExcelUtils.getColumnNames(sysConfigService.getValue(SysConfigEnum.PURCHASE_TEMPLATE.getKey()));
    	String[] columns = ExcelUtils.getColumns(sysConfigService.getValue(SysConfigEnum.PURCHASE_TEMPLATE.getKey()));
        List<Map<String,Object>> list = corePurchaseMasterService.getList(columns,queryParam);
        ExcelUtils.exportExcel(request,response,list,columnNames,columns,"采购单数据","供货单");
    }



 /*   @PostMapping(value = "/import")
    public Hdi importFile(@RequestParam("file") MultipartFile file ) {
        return  ImportCommon.importFile(file,getUser(),(Object[] obj) -> corePurchaseMasterService.importData(obj[0],obj[1]));
    }*/
}
