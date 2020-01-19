package com.ebig.hdi.modules.reagent.controller;

import com.ebig.hdi.common.enums.SysConfigEnum;
import com.ebig.hdi.common.utils.ExcelUtils;
import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.common.validator.group.AddGroup;
import com.ebig.hdi.common.validator.group.UpdateGroup;
import com.ebig.hdi.modules.core.service.ImportCommon;
import com.ebig.hdi.modules.reagent.service.GoodsSupplierReagentImportService;
import com.ebig.hdi.modules.reagent.service.GoodsSupplierReagentService;
import com.ebig.hdi.modules.reagent.vo.GoodsSupplierReagentVO;
import com.ebig.hdi.modules.sys.controller.AbstractController;
import com.ebig.hdi.modules.sys.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 供应商试剂信息
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:28:15
 */
@RestController
@RequestMapping("/reagent/goodssupplierreagent")
public class GoodsSupplierReagentController extends AbstractController {
    @Autowired
    private GoodsSupplierReagentService goodsSupplierReagentService;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private GoodsSupplierReagentImportService goodsSupplierReagentImportService;
    /**
     * 列表
     */
    @PostMapping("/list")
    //@RequiresPermissions("reagent:goodssupplierreagent:list")
    public Hdi list(@RequestBody Map<String, Object> params){
        PageUtils page = goodsSupplierReagentService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    //@RequiresPermissions("reagent:goodssupplierreagent:info")
    public Hdi info(@PathVariable("id") Long id){
    	GoodsSupplierReagentVO goodsSupplierReagent = goodsSupplierReagentService.selectReagentById(id);

        return Hdi.ok().put("goodsSupplierReagent", goodsSupplierReagent);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    //@RequiresPermissions("reagent:goodssupplierreagent:save")
    public Hdi save(@RequestBody GoodsSupplierReagentVO goodsSupplierReagentVO){
    	ValidatorUtils.validateEntity(goodsSupplierReagentVO, AddGroup.class);
    	goodsSupplierReagentVO.setCreateId(getUserId());
        goodsSupplierReagentService.save(goodsSupplierReagentVO);

        return Hdi.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    //@RequiresPermissions("reagent:goodssupplierreagent:update")
    public Hdi update(@RequestBody GoodsSupplierReagentVO goodsSupplierReagentVO){
        ValidatorUtils.validateEntity(goodsSupplierReagentVO, UpdateGroup.class);
        goodsSupplierReagentVO.setEditId(getUserId());
        goodsSupplierReagentService.update(goodsSupplierReagentVO);
        
        return Hdi.ok();
    }
    
    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("reagent:goodssupplierreagent:delete")
    public Hdi delete(@RequestBody Long[] ids){
        goodsSupplierReagentService.deleteBatchIds(Arrays.asList(ids));

        return Hdi.ok();
    }

    /**
     * 启用停用
     */
    @PostMapping("/toggle")
    //@RequiresPermissions("reagent:goodssupplierreagent:toggle")
    public Hdi toggle(@RequestBody Map<String, Object> params){
    	goodsSupplierReagentService.toggle(params);

        return Hdi.ok();
    }
    
    /**
     * 批量导入
     * @param file
     * @return
     */
    @RequestMapping("/input")
  //@RequiresPermissions("reagent:goodssupplierreagent:input")
    public Hdi input(@RequestParam("file") MultipartFile file) {
    	try {
			String[][] rows = ExcelUtils.readExcelByInput(file.getInputStream(), file.getOriginalFilename(), 2);
			goodsSupplierReagentService.input(rows,getUserId(),getDeptId());
		} catch (Exception e) {
			return Hdi.error("导入失败！"+e.getMessage());
		}
    	
    	return Hdi.ok();
    }

    /**
     * 导出
     */
    @PostMapping(value = "/export")
    public void export(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> params) throws IOException {
        //获取查询字段名
        String[] columnNames = ExcelUtils.getColumnNames(sysConfigService.getValue(SysConfigEnum.GOODSSUPPLIERREAGENT_TEMPLATE.getKey()));
        //获取查询表头
        String[] columns = ExcelUtils.getColumns(sysConfigService.getValue(SysConfigEnum.GOODSSUPPLIERREAGENT_TEMPLATE.getKey()));
        Map<String, Object> map = new HashMap<>(16);
        map.put("columns",columns);
        map.put("queryParams",params);
        List<Map<String,Object>> list=goodsSupplierReagentService.getList(map);
        ExcelUtils.exportExcel(request,response,list,columnNames,columns,"供应商试剂数据","供应商试剂列表单");
    }

    /**
     * 导入
     */
    @PostMapping(value = "/import")
    public Hdi importFile(@RequestParam("file") MultipartFile file ) {
        return  ImportCommon.importFile(file,getUser(),(Object[] obj) ->{
            return goodsSupplierReagentService.importData(obj[0],obj[1]);
        });


    }

}
