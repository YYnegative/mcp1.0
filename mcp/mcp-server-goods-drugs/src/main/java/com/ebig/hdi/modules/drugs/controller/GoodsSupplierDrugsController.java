package com.ebig.hdi.modules.drugs.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ebig.hdi.common.enums.SysConfigEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.modules.drugs.vo.GoodsPlatformDrugsParams;
import com.ebig.hdi.modules.sys.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ebig.hdi.common.utils.ExcelUtils;
import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.common.validator.group.AddGroup;
import com.ebig.hdi.common.validator.group.UpdateGroup;
import com.ebig.hdi.modules.drugs.service.GoodsSupplierDrugsService;
import com.ebig.hdi.modules.drugs.vo.GoodsSupplierDrugsEntityVo;
import com.ebig.hdi.modules.sys.controller.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 供应商药品信息
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-08 14:17:51
 */
@RestController
@RequestMapping("drugs/goodssupplierdrugs")
public class GoodsSupplierDrugsController extends AbstractController{
    @Autowired
    private GoodsSupplierDrugsService goodsSupplierDrugsService;

    @Autowired
    private SysConfigService sysConfigService;

    /**
     * 列表
     */
    @PostMapping("/list")
    //@RequiresPermissions("drugs:goodssupplierdrugs:list")
    public Hdi list(@RequestBody Map<String, Object> params){
        PageUtils page = goodsSupplierDrugsService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    //@RequiresPermissions("drugs:goodssupplierdrugs:info")
    public Hdi info(@PathVariable("id") Long id){
        GoodsSupplierDrugsEntityVo goodsSupplierDrugs = goodsSupplierDrugsService.selectSupplierDrugsById(id);

        return Hdi.ok().put("goodsSupplierDrugs", goodsSupplierDrugs);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    //@RequiresPermissions("drugs:goodssupplierdrugs:save")
    public Hdi save(@RequestBody GoodsSupplierDrugsEntityVo goodsSupplierDrugsEntityVo){
    	ValidatorUtils.validateEntity(goodsSupplierDrugsEntityVo, AddGroup.class);
    	goodsSupplierDrugsEntityVo.setCreateId(getUserId());
        Map<String,String> errorMap = goodsSupplierDrugsService.save(goodsSupplierDrugsEntityVo,getUser());;
        if (!errorMap.isEmpty()){
            return  Hdi.error(errorMap.get("errorMessage"));
        }
        return Hdi.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    //@RequiresPermissions("drugs:goodssupplierdrugs:update")
    public Hdi update(@RequestBody GoodsSupplierDrugsEntityVo goodsSupplierDrugsEntityVo){
        ValidatorUtils.validateEntity(goodsSupplierDrugsEntityVo, UpdateGroup.class);
        goodsSupplierDrugsEntityVo.setEditId(getUserId());
        Map<String,String> errorMap  = goodsSupplierDrugsService.update(goodsSupplierDrugsEntityVo,getUser());
        if (!errorMap.isEmpty()){
            return Hdi.error(errorMap.get("errorMessage"));
        }
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("drugs:goodssupplierdrugs:delete")
    public Hdi delete(@RequestBody Long[] ids){
        goodsSupplierDrugsService.delete(ids);

        return Hdi.ok();
    }

    /**
     * 启用停用
     */
    @PostMapping("/toggle")
    //@RequiresPermissions("drugs:goodssupplierdrugs:toggle")
    public Hdi toggle(@RequestBody Map<String, Object> params){
    	goodsSupplierDrugsService.toggle(params);

        return Hdi.ok();
    }
    
    /**
     * 批量导入
     * @param file
     * @return
     */
    @RequestMapping("/input")
    //@RequiresPermissions("reagent:goodssupplierdrugs:input")
    public Hdi input(@RequestParam("file") MultipartFile file) {
    	try {
			String[][] rows = ExcelUtils.readExcelByInput(file.getInputStream(), file.getOriginalFilename(), 1);
			goodsSupplierDrugsService.input(rows,getUserId(),getDeptId());
		} catch (Exception e) {
			return Hdi.error("导入失败！"+e.getMessage());
		}
    	
    	return Hdi.ok();
    }
    /**
     * 导出
     */
    @PostMapping(value = "/export")
    public void export(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String,String> queryParams) throws IOException {
        //获取查询字段名
        String[] columnNames = ExcelUtils.getColumnNames(sysConfigService.getValue(SysConfigEnum.GOODS_SUPPLIER_DRUGS_TEMPLATE.getKey()));
        //获取查询表头
        String[] columns = ExcelUtils.getColumns(sysConfigService.getValue(SysConfigEnum.GOODS_SUPPLIER_DRUGS_TEMPLATE.getKey()));
        Map<String, Object> map = new HashMap<>(16);
        map.put("columns",columns);
        map.put("queryParams",queryParams);
        List<Map<String,Object>> list=goodsSupplierDrugsService.getList(map);
        ExcelUtils.exportExcel(request,response,list,columnNames,columns,"供应商药品数据","供应商药品列表单");
    }
}
