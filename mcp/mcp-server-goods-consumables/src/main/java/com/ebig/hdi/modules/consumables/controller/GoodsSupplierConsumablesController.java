package com.ebig.hdi.modules.consumables.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ebig.hdi.common.enums.SysConfigEnum;
import com.ebig.hdi.common.utils.ExcelUtils;
import com.ebig.hdi.modules.consumables.param.GoodsSupplierConsumablesParam;
import com.ebig.hdi.modules.sys.service.SysConfigService;
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
import com.ebig.hdi.common.validator.group.AddGroup;
import com.ebig.hdi.common.validator.group.UpdateGroup;
import com.ebig.hdi.modules.consumables.entity.vo.GoodsSupplierConsumablesVO;
import com.ebig.hdi.modules.consumables.service.GoodsSupplierConsumablesService;
import com.ebig.hdi.modules.sys.controller.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 供应商耗材信息
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:36:02
 */
@RestController
@RequestMapping("/consumables/goodssupplierconsumables")
public class GoodsSupplierConsumablesController extends AbstractController {
    @Autowired
    private GoodsSupplierConsumablesService goodsSupplierConsumablesService;
    @Autowired
    private SysConfigService sysConfigService;
    /**
     * 列表
     */
    @PostMapping("/list")
    //@RequiresPermissions("consumables:goodssupplierconsumables:list")
    public Hdi list(@RequestBody Map<String, Object> params){
        PageUtils page = goodsSupplierConsumablesService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    //@RequiresPermissions("consumables:goodssupplierconsumables:info")
    public Hdi info(@PathVariable("id") Long id){
    	GoodsSupplierConsumablesVO goodsSupplierConsumables = goodsSupplierConsumablesService.selectById(id);

        return Hdi.ok().put("goodsSupplierConsumables", goodsSupplierConsumables);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    //@RequiresPermissions("consumables:goodssupplierconsumables:save")
    public Hdi save(@RequestBody GoodsSupplierConsumablesVO goodsSupplierConsumablesVO){
    	ValidatorUtils.validateEntity(goodsSupplierConsumablesVO, AddGroup.class);
    	goodsSupplierConsumablesVO.setCreateId(getUserId());
        Map<String, Object> map = goodsSupplierConsumablesService.save(goodsSupplierConsumablesVO);
        if (!map.isEmpty()){
            return Hdi.error(map.get("errmessage").toString());
        }

        return Hdi.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    //@RequiresPermissions("consumables:goodssupplierconsumables:update")
    public Hdi update(@RequestBody GoodsSupplierConsumablesVO goodsSupplierConsumablesVO){
        ValidatorUtils.validateEntity(goodsSupplierConsumablesVO, UpdateGroup.class);
        goodsSupplierConsumablesVO.setEditId(getUserId());
        Map<String, Object> map = goodsSupplierConsumablesService.update(goodsSupplierConsumablesVO);
        if (!map.isEmpty()){
            return Hdi.error(map.get("errmessage").toString());
        }

        return Hdi.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    //@RequiresPermissions("consumables:goodssupplierconsumables:delete")
    public Hdi delete(@RequestBody Long[] ids){
        goodsSupplierConsumablesService.deleteBatchIds(Arrays.asList(ids));

        return Hdi.ok();
    }
    
    /**
     * 启用停用
     */
    @PostMapping("/toggle")
    //@RequiresPermissions("consumables:goodssupplierconsumables:toggle")
    public Hdi toggle(@RequestBody Map<String, Object> params){
    	goodsSupplierConsumablesService.toggle(params);

        return Hdi.ok();
    }
    /**
     * 供应商耗材导出
     */
    @RequestMapping(value = "/export")
    public void export(HttpServletRequest request, HttpServletResponse response, @RequestBody GoodsSupplierConsumablesParam queryParam) throws IOException {
        //获取查询字段名
        String[] columnNames = ExcelUtils.getColumnNames(sysConfigService.getValue(SysConfigEnum.GOODS_SUPPLIER_CONSUMABLES_TEMPLATE.getKey()));
        //获取查询表头
        String[] columns = ExcelUtils.getColumns(sysConfigService.getValue(SysConfigEnum.GOODS_SUPPLIER_CONSUMABLES_TEMPLATE.getKey()));
        HashMap<String, Object> map = new HashMap<>();
        map.put("columns",columns);
        map.put("queryParam",queryParam);
        List<Map<String, Object>> list = goodsSupplierConsumablesService.getList(map);
        ExcelUtils.exportExcel(request, response, list, columnNames, columns, "供应商耗材数据", "供应商耗材列表单");
    }

}
