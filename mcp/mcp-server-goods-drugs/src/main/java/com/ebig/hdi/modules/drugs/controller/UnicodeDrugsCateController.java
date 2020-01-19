package com.ebig.hdi.modules.drugs.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ebig.hdi.common.utils.ExcelUtils;
import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.modules.drugs.entity.DrugsTreeNode;
import com.ebig.hdi.modules.drugs.entity.UnicodeDrugsCateEntity;
import com.ebig.hdi.modules.drugs.service.UnicodeDrugsCateService;
import com.ebig.hdi.modules.sys.controller.AbstractController;

/**
 * 
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-13 11:02:14
 */
@RestController
@RequestMapping("/unicode/unicodedrugscate")
public class UnicodeDrugsCateController extends AbstractController  {
    @Autowired
    private UnicodeDrugsCateService unicodeDrugsCateService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("unicode:unicodedrugscate:list")
    public Hdi list(@RequestBody Map<String, Object> params){
    	List<DrugsTreeNode> nodeList = unicodeDrugsCateService.queryNode(params);
        return Hdi.ok().put("data", nodeList);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{cateId}")
    //@RequiresPermissions("unicode:unicodedrugscate:info")
    public Hdi info(@PathVariable("cateId") Long cateId){
        UnicodeDrugsCateEntity unicodeDrugsCate = unicodeDrugsCateService.selectById(cateId);

        return Hdi.ok().put("unicodeDrugsCate", unicodeDrugsCate);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("unicode:unicodedrugscate:save")
    public Hdi save(@RequestBody UnicodeDrugsCateEntity UnicodeDrugsCateEntity){
    	ValidatorUtils.validateEntity(UnicodeDrugsCateEntity);
    	UnicodeDrugsCateEntity.setDeptId(getDeptId());
    	unicodeDrugsCateService.save(UnicodeDrugsCateEntity);
        
        return Hdi.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("unicode:unicodedrugscate:update")
    public Hdi update(@RequestBody UnicodeDrugsCateEntity UnicodeDrugsCateEntity){
        ValidatorUtils.validateEntity(UnicodeDrugsCateEntity); 
        unicodeDrugsCateService.update(UnicodeDrugsCateEntity);//全部更新
        
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("unicode:unicodedrugscate:delete")
    public Hdi delete(@RequestBody Map<String, Object> params){
    	unicodeDrugsCateService.deleteNode(Long.parseLong(params.get("cateId").toString()));
        return Hdi.ok();
    }
    
    /**
     * 批量导入
     * @param file
     * @return
     */
    @RequestMapping("/input")
    public Hdi input(@RequestParam("file") MultipartFile file) {
    	try {
			String[][] rows = ExcelUtils.readExcelByInput(file.getInputStream(), file.getOriginalFilename(), 1);
			unicodeDrugsCateService.input(rows);
		} catch (Exception e) {
			return Hdi.error("导入失败！");
		}
    	
    	return Hdi.ok();
    }

}
