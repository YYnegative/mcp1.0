package com.ebig.hdi.modules.consumables.controller;

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
import com.ebig.hdi.modules.consumables.entity.ConsumablesTreeNode;
import com.ebig.hdi.modules.consumables.entity.UnicodeConsumablesCateEntity;
import com.ebig.hdi.modules.consumables.service.UnicodeConsumablesCateService;
import com.ebig.hdi.modules.sys.controller.AbstractController;

/**
 * 
 *
 * @author frink
 * @email 492699405@qq.com
 * @date 2019-05-13 09:53:58
 */
@RestController
@RequestMapping("/unicode/unicodeconsumablescate")
public class UnicodeConsumablesCateController extends AbstractController {
    @Autowired
    private UnicodeConsumablesCateService unicodeConsumablesCateService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("unicode:unicodeconsumablescate:list")
    public Hdi list(@RequestBody Map<String, Object> params){
    	List<ConsumablesTreeNode> nodeList = unicodeConsumablesCateService.queryNode(params);
    	
        return Hdi.ok().put("data", nodeList);
    }
    
    /**
     * 信息
     */
    @RequestMapping("/info/{cateId}")
    //@RequiresPermissions("unicode:unicodeconsumablescate:info")
    public Hdi info(@PathVariable("cateId") Long cateId){
        UnicodeConsumablesCateEntity unicodeConsumablesCate = unicodeConsumablesCateService.selectById(cateId);

        return Hdi.ok().put("unicodeConsumablesCate", unicodeConsumablesCate);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("unicode:unicodeconsumablescate:save")
    public Hdi save(@RequestBody UnicodeConsumablesCateEntity unicodeConsumablesCate){
    	ValidatorUtils.validateEntity(unicodeConsumablesCate);
    	unicodeConsumablesCate.setDeptId(getDeptId());
        unicodeConsumablesCateService.save(unicodeConsumablesCate);
        
        return Hdi.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("unicode:unicodeconsumablescate:update")
    public Hdi update(@RequestBody UnicodeConsumablesCateEntity unicodeConsumablesCate){
        ValidatorUtils.validateEntity(unicodeConsumablesCate); 
        unicodeConsumablesCateService.update(unicodeConsumablesCate);//全部更新
        
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("unicode:unicodeconsumablescate:delete")
    public Hdi delete(@RequestBody Map<String, Object> params){
    	unicodeConsumablesCateService.deleteNode(Long.parseLong(params.get("cateId").toString()));
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
			unicodeConsumablesCateService.input(rows);
		} catch (Exception e) {
			return Hdi.error("导入失败！");
		}
    	
    	return Hdi.ok();
    }

}
