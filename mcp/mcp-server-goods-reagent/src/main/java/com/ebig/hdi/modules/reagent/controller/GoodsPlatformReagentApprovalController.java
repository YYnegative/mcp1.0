package com.ebig.hdi.modules.reagent.controller;

import com.ebig.hdi.common.enums.SysConfigEnum;
import com.ebig.hdi.common.utils.ExcelUtils;
import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.common.validator.group.AddGroup;
import com.ebig.hdi.common.validator.group.UpdateGroup;
import com.ebig.hdi.modules.reagent.param.GoodsPlatformReagentParam;
import com.ebig.hdi.modules.reagent.service.GoodsPlatformReagentApprovalService;
import com.ebig.hdi.modules.reagent.vo.GoodsPlatformReagentVO;
import com.ebig.hdi.modules.sys.controller.AbstractController;
import com.ebig.hdi.modules.sys.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 平台试剂信息
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:29:52
 */
@RestController
@RequestMapping("/reagent/goodsplatformreagent")
public class GoodsPlatformReagentApprovalController extends AbstractController {
    @Autowired
    private GoodsPlatformReagentApprovalService goodsPlatformReagentApprovalService;

    @Autowired
    private SysConfigService sysConfigService;
    /**
     * 列表
     */
    @PostMapping("/list")
    //@RequiresPermissions("reagent:goodsplatformreagent:list")
    public Hdi list(@RequestBody Map<String, Object> params){
        PageUtils page = goodsPlatformReagentApprovalService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("reagent:goodsplatformreagent:info")
    public Hdi info(@PathVariable("id") Long id){
    	GoodsPlatformReagentVO goodsPlatformReagent = goodsPlatformReagentApprovalService.selectById(id);

        return Hdi.ok().put("goodsPlatformReagent", goodsPlatformReagent);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("reagent:goodsplatformreagent:save")
    public Hdi save(@RequestBody GoodsPlatformReagentVO goodsPlatformReagentVO){
    	ValidatorUtils.validateEntity(goodsPlatformReagentVO, AddGroup.class);
    	goodsPlatformReagentVO.setDeptId(getDeptId());
    	goodsPlatformReagentVO.setCreateId(getUserId());
        goodsPlatformReagentApprovalService.save(goodsPlatformReagentVO,getUser());

        return Hdi.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("reagent:goodsplatformreagent:update")
    public Hdi update(@RequestBody GoodsPlatformReagentVO goodsPlatformReagentVO){
        ValidatorUtils.validateEntity(goodsPlatformReagentVO, UpdateGroup.class);
        goodsPlatformReagentVO.setEditId(getUserId());
        goodsPlatformReagentApprovalService.update(goodsPlatformReagentVO,getUser());
        
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("reagent:goodsplatformreagent:delete")
    public Hdi delete(@RequestBody Long[] ids){
        goodsPlatformReagentApprovalService.delete(ids);

        return Hdi.ok();
    }

    /**
     * 启用停用
     */
    @PostMapping("/toggle")
    //@RequiresPermissions("reagent:goodsplatformreagent:toggle")
    public Hdi toggle(@RequestBody Map<String, Object> params){
        goodsPlatformReagentApprovalService.toggle(params);

        return Hdi.ok();
    }

    /**
     * 导出
     */
    @RequestMapping(value = "/export")
    public void export(HttpServletRequest request, HttpServletResponse response, @RequestBody GoodsPlatformReagentParam goodsPlatformReagentParam) throws IOException {
        //获取查询字段名
        String[] columnNames = ExcelUtils.getColumnNames(sysConfigService.getValue(SysConfigEnum.GOODSPLATFORMREAGENT_TEMPLATE.getKey()));
        //获取查询表头
        String[] columns = ExcelUtils.getColumns(sysConfigService.getValue(SysConfigEnum.GOODSPLATFORMREAGENT_TEMPLATE.getKey()));
        HashMap map = new HashMap<String,Object>();
        map.put("columns",columns);
        map.put("queryParam",goodsPlatformReagentParam);
        List<Map<String,Object>> list= goodsPlatformReagentApprovalService.getList(map);
        ExcelUtils.exportExcel(request,response,list,columnNames,columns,"平台试剂数据","平台列表单");
    }

    /**
     * 平台耗材状态审核接口
     * @param params
     * @return
     */
    @RequestMapping("/checkStatus")
    public Hdi check(@RequestBody Map<String,Object> params) {
        Map<String, Object> map = goodsPlatformReagentApprovalService.checkStatus(params,getUser());
        if (!map.isEmpty()){
            return Hdi.error(map.get("errorMessage").toString());
        }
        return Hdi.ok();
    }
}
