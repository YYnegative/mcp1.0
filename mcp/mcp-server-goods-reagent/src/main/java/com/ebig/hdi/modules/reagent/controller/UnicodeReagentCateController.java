package com.ebig.hdi.modules.reagent.controller;

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
import com.ebig.hdi.modules.reagent.entity.ReagentTreeNode;
import com.ebig.hdi.modules.reagent.entity.UnicodeReagentCateEntity;
import com.ebig.hdi.modules.reagent.service.UnicodeReagentCateService;
import com.ebig.hdi.modules.sys.controller.AbstractController;

/**
 * 
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-13 11:01:00
 */
@RestController
@RequestMapping("/unicode/unicodereagentcate")
public class UnicodeReagentCateController extends AbstractController {
    @Autowired
    private UnicodeReagentCateService unicodeReagentCateService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("unicode:unicodereagentcate:list")
    public Hdi list(@RequestBody Map<String, Object> params){
    	List<ReagentTreeNode> nodeList = unicodeReagentCateService.queryNode(params);
    	
        return Hdi.ok().put("data", nodeList);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{cateId}")
    //@RequiresPermissions("unicode:unicodereagentcate:info")
    public Hdi info(@PathVariable("cateId") Long cateId){
        UnicodeReagentCateEntity unicodeReagentCate = unicodeReagentCateService.selectById(cateId);

        return Hdi.ok().put("unicodeReagentCate", unicodeReagentCate);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("unicode:unicodereagentcate:save")
    public Hdi save(@RequestBody UnicodeReagentCateEntity unicodeReagentCateEntity){
    	ValidatorUtils.validateEntity(unicodeReagentCateEntity);
    	unicodeReagentCateEntity.setDeptId(getDeptId());
    	unicodeReagentCateService.save(unicodeReagentCateEntity);
        
        return Hdi.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("unicode:unicodereagentcate:update")
    public Hdi update(@RequestBody UnicodeReagentCateEntity unicodeReagentCateEntity){
        ValidatorUtils.validateEntity(unicodeReagentCateEntity); 
        unicodeReagentCateService.update(unicodeReagentCateEntity);//全部更新
        
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("unicode:unicodereagentcate:delete")
    public Hdi delete(@RequestBody Map<String, Object> params){
    	unicodeReagentCateService.deleteNode(Long.parseLong(params.get("cateId").toString()));
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
			unicodeReagentCateService.input(rows);
		} catch (Exception e) {
			return Hdi.error("导入失败！");
		}
    	
    	return Hdi.ok();
    }

}
