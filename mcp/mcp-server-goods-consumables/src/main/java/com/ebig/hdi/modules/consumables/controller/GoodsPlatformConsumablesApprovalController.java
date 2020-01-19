package com.ebig.hdi.modules.consumables.controller;

import com.ebig.hdi.common.enums.SysConfigEnum;
import com.ebig.hdi.common.utils.ExcelUtils;
import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.common.validator.group.AddGroup;
import com.ebig.hdi.common.validator.group.UpdateGroup;
import com.ebig.hdi.modules.consumables.entity.GoodsPlatformConsumablesApprovalEntity;
import com.ebig.hdi.modules.consumables.param.GoodsPlatformConsumablesApprovalParam;
import com.ebig.hdi.modules.consumables.service.GoodsPlatformConsumablesApprovalService;
import com.ebig.hdi.modules.consumables.vo.GoodsPlatformConsumablesEntityVo;
import com.ebig.hdi.modules.sys.controller.AbstractController;
import com.ebig.hdi.modules.sys.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 平台耗材信息
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:37:46
 */
@RestController
@RequestMapping("consumables/goodsplatformconsumables")
public class GoodsPlatformConsumablesApprovalController extends AbstractController{
    @Autowired
    private GoodsPlatformConsumablesApprovalService goodsPlatformConsumablesApprovalService;
    @Autowired
    private SysConfigService sysConfigService;
    /**
     * 列表
     */
    @PostMapping("/list")
    //@RequiresPermissions("consumables:goodsplatformconsumables:list")
    public Hdi list(@RequestBody Map<String, Object> params){
        PageUtils page = goodsPlatformConsumablesApprovalService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    //@RequiresPermissions("consumables:goodsplatformconsumables:info")
    public Hdi info(@PathVariable("id") Long id){
        GoodsPlatformConsumablesApprovalEntity goodsPlatformConsumables = goodsPlatformConsumablesApprovalService.selectPlatformConsumablesById(id);
        return Hdi.ok().put("goodsPlatformConsumables", goodsPlatformConsumables);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    //@RequiresPermissions("consumables:goodsplatformconsumables:save")
    public Hdi save(@RequestBody GoodsPlatformConsumablesEntityVo goodsPlatformConsumablesVo){
    	ValidatorUtils.validateEntity(goodsPlatformConsumablesVo, AddGroup.class);
    	goodsPlatformConsumablesVo.setCreateId(getUserId());
    	goodsPlatformConsumablesVo.setEditId(getUserId());
    	goodsPlatformConsumablesVo.setDeptId(getDeptId());
        Map<String, Object> map = goodsPlatformConsumablesApprovalService.save(goodsPlatformConsumablesVo, getUser());
        if (!map.isEmpty()){
            return Hdi.error(map.get("errmessage").toString());
        }
        return Hdi.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    //@RequiresPermissions("consumables:goodsplatformconsumables:update")
    public Hdi update(@RequestBody GoodsPlatformConsumablesEntityVo goodsPlatformConsumablesVo){
        ValidatorUtils.validateEntity(goodsPlatformConsumablesVo, UpdateGroup.class);
        goodsPlatformConsumablesVo.setEditId(getUserId());
        goodsPlatformConsumablesApprovalService.update(goodsPlatformConsumablesVo,getUser());

        return Hdi.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    //@RequiresPermissions("consumables:goodsplatformconsumables:delete")
    public Hdi delete(@RequestBody Long[] ids){
        goodsPlatformConsumablesApprovalService.delete(ids);

        return Hdi.ok();
    }

    /**
     * 启用停用
     */
    @PostMapping("/toggle")
    //@RequiresPermissions("drugs:goodshospitaldrugs:toggle")
    public Hdi toggle(@RequestBody Map<String, Object> params){
        goodsPlatformConsumablesApprovalService.toggle(params);

        return Hdi.ok();
    }

    /**
     * 平台商品耗材导出
     */
    @RequestMapping(value = "/export")
    public void export(HttpServletRequest request, HttpServletResponse response, @RequestBody GoodsPlatformConsumablesApprovalParam queryParam) throws IOException {
        //获取查询字段名
        String[] columnNames = ExcelUtils.getColumnNames(sysConfigService.getValue(SysConfigEnum.GOODSPLATFORMCONSUMABLES_TEMPLATE.getKey()));
        //获取查询表头
        String[] columns = ExcelUtils.getColumns(sysConfigService.getValue(SysConfigEnum.GOODSPLATFORMCONSUMABLES_TEMPLATE.getKey()));
        HashMap<String, Object> map = new HashMap<>();
        map.put("columns",columns);
        map.put("queryParam",queryParam);
        List<Map<String, Object>> list = goodsPlatformConsumablesApprovalService.getList(map);
        ExcelUtils.exportExcel(request, response, list, columnNames, columns, "平台耗材数据", "平台耗材列表单");
    }

    /**
     * 平台耗材状态审核接口
     * @param params
     * @return
     */
    @RequestMapping("/checkStatus")
    public Hdi check(@RequestBody Map<String,Object> params) {
        Map<String, Object> map = goodsPlatformConsumablesApprovalService.checkStatus(params,getUser());
        if (!map.isEmpty()){
            return Hdi.error(map.get("errorMessage").toString());
        }
        return Hdi.ok();
    }
}
