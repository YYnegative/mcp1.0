package com.ebig.hdi.modules.org.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import com.ebig.hdi.modules.org.entity.OrgAgentInfoEntity;
import com.ebig.hdi.modules.org.service.OrgAgentInfoService;
import com.ebig.hdi.modules.sys.controller.AbstractController;

/**
 * 代理商信息
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-04-23 09:27:41
 */
@RestController
@RequestMapping("/org/orgagentinfo")
public class OrgAgentInfoController extends AbstractController {
    @Autowired
    private OrgAgentInfoService orgAgentInfoService;

    /**
     * 列表
     */
    @PostMapping("/list")
    @RequiresPermissions("org:orgagentinfo:list")
    public Hdi list(@RequestBody Map<String, Object> params){
    	//传当前登录人的机构id进去进行数据过滤
        PageUtils page = orgAgentInfoService.queryPage(params);
        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("org:orgagentinfo:info")
    public Hdi info(@PathVariable("id") Long id){
        OrgAgentInfoEntity orgAgentInfo = orgAgentInfoService.selectById(id);

        return Hdi.ok().put("orgAgentInfo", orgAgentInfo);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("org:orgagentinfo:save")
    public Hdi save(@RequestBody OrgAgentInfoEntity orgAgentInfo){
    	ValidatorUtils.validateEntity(orgAgentInfo);
    	orgAgentInfo.setCreateId(getUserId());
    	orgAgentInfo.setDeptId(getDeptId());
        orgAgentInfoService.save(orgAgentInfo);
        return Hdi.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("org:orgagentinfo:update")
    public Hdi update(@RequestBody OrgAgentInfoEntity orgAgentInfo){
        ValidatorUtils.validateEntity(orgAgentInfo);
        orgAgentInfo.setEditId(getUserId());
        orgAgentInfoService.update(orgAgentInfo);//动态更新
        
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @RequiresPermissions("org:orgagentinfo:delete")
    public Hdi delete(@RequestBody Long[] ids){
        orgAgentInfoService.delete(ids);

        return Hdi.ok();
    }
    
    /**
     * 根据厂商名称查询
     */
    @RequestMapping("/queryByAgentName")
    @RequiresPermissions("org:orgagentinfo:list")
    public Hdi queryByAgentName(@RequestBody Map<String, Object> params){
        List<OrgAgentInfoEntity> orgAgentInfoList = orgAgentInfoService.queryByAgentName(params);

        return Hdi.ok().put("orgAgentInfoList", orgAgentInfoList);
    }

}
