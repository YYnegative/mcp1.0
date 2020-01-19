package com.ebig.hdi.modules.drugs.controller;

import com.ebig.hdi.common.enums.SysConfigEnum;
import com.ebig.hdi.common.utils.ExcelUtils;
import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.common.validator.group.AddGroup;
import com.ebig.hdi.common.validator.group.UpdateGroup;
import com.ebig.hdi.modules.drugs.service.GoodsPlatformDrugsApprovalService;
import com.ebig.hdi.modules.drugs.vo.GoodsPlatformDrugsEntityVo;
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
 * 平台药品信息
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-08 14:21:25
 */
@RestController
@RequestMapping("drugs/goodsplatformdrugs")
public class GoodsPlatformDrugsApprovalController extends AbstractController {
	
    @Autowired
    private GoodsPlatformDrugsApprovalService goodsPlatformDrugsApprovalService;

    @Autowired
    private SysConfigService sysConfigService;


    /**
     * 列表
     */
    @PostMapping("/list")
    //@RequiresPermissions("drugs:goodsplatformdrugs:list")
    public Hdi list(@RequestBody Map<String, Object> params){
        PageUtils page = goodsPlatformDrugsApprovalService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    //@RequiresPermissions("drugs:goodsplatformdrugs:info")
    public Hdi info(@PathVariable("id") Long id){
        GoodsPlatformDrugsEntityVo goodsPlatformDrugs = goodsPlatformDrugsApprovalService.selectPlatformDrugsById(id);

        return Hdi.ok().put("goodsPlatformDrugs", goodsPlatformDrugs);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    //@RequiresPermissions("drugs:goodsplatformdrugs:save")
    public Hdi save(@RequestBody GoodsPlatformDrugsEntityVo goodsPlatformDrugsEntityVo) throws Exception {
    	ValidatorUtils.validateEntity(goodsPlatformDrugsEntityVo, AddGroup.class);
    	goodsPlatformDrugsEntityVo.setCreateId(getUserId());
    	goodsPlatformDrugsEntityVo.setDeptId(getDeptId());
    	Map<String,String> errorMap = goodsPlatformDrugsApprovalService.save(goodsPlatformDrugsEntityVo,getUser());
    	if (!errorMap.isEmpty()){
    	    return Hdi.error(errorMap.get("errorMessage"));
        }
        return Hdi.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    //@RequiresPermissions("drugs:goodsplatformdrugs:update")
    public Hdi update(@RequestBody GoodsPlatformDrugsEntityVo goodsPlatformDrugsEntityVo){
        ValidatorUtils.validateEntity(goodsPlatformDrugsEntityVo, UpdateGroup.class);
        goodsPlatformDrugsEntityVo.setEditId(getUserId());
        goodsPlatformDrugsApprovalService.update(goodsPlatformDrugsEntityVo,getUser());
        
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    //@RequiresPermissions("drugs:goodsplatformdrugs:delete")
    public Hdi delete(@RequestBody Long[] ids){
        goodsPlatformDrugsApprovalService.delete(ids);

        return Hdi.ok();
    }
    
    /**
     * 启用停用
     */
    @PostMapping("/toggle")
    //@RequiresPermissions("drugs:goodsplatformdrugs:toggle")
    public Hdi toggle(@RequestBody Map<String, Object> params){
        goodsPlatformDrugsApprovalService.toggle(params);

        return Hdi.ok();
    }
    /**
     * 导出
     */
    @PostMapping(value = "/export")
    public void export(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String,Object> queryParams) throws IOException {
        //获取查询字段名
        String[] columnNames = ExcelUtils.getColumnNames(sysConfigService.getValue(SysConfigEnum.GOODS_PLATFORM_DRUGS_TEMPLATE.getKey()));
        //获取查询表头
        String[] columns = ExcelUtils.getColumns(sysConfigService.getValue(SysConfigEnum.GOODS_PLATFORM_DRUGS_TEMPLATE.getKey()));
        Map<String, Object> map = new HashMap<>(16);
        map.put("columns",columns);
        map.put("queryParams",queryParams);
        List<Map<String,Object>> list=goodsPlatformDrugsApprovalService.getList(map);
        ExcelUtils.exportExcel(request,response,list,columnNames,columns,"平台药品数据","平台药品列表单");
    }

    @PostMapping("/checkStatus")
    public Hdi checkStatus(@RequestBody Map<String,Object> params){
        Map<String,String> errorMessage  = goodsPlatformDrugsApprovalService.checkStatus(params,getUser());
        if (!errorMessage.isEmpty()){
            return Hdi.error(errorMessage.get("errorMessage"));
        }
        return Hdi.ok();
    }

}
