package com.ebig.hdi.modules.sys.controller;

import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.modules.sys.entity.SysUserMessageEntity;
import com.ebig.hdi.modules.sys.service.SysUserMessageService;
import com.ebig.hdi.modules.sys.vo.SysUserMessageEntityVo;

/**
 * 系统用户消息
 *
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-04-26 12:11:51
 */
@RestController
@RequestMapping("sys/sysusermessage")
public class SysUserMessageController extends AbstractController {
    @Autowired
    private SysUserMessageService sysUserMessageService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("sys:sysusermessage:list")
    public Hdi list(@RequestParam Map<String, Object> params){
    	params.put("userId", getUserId());
        PageUtils page = sysUserMessageService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("sys:sysusermessage:info")
    public Hdi info(@PathVariable("id") Long id){
        SysUserMessageEntityVo sysUserMessageVo = sysUserMessageService.queryInfoById(id);

        return Hdi.ok().put("sysUserMessageVo", sysUserMessageVo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:sysusermessage:save")
    public Hdi save(@RequestBody SysUserMessageEntity sysUserMessage){
        sysUserMessageService.insert(sysUserMessage);

        return Hdi.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:sysusermessage:update")
    public Hdi update(@RequestBody SysUserMessageEntity sysUserMessage){
        ValidatorUtils.validateEntity(sysUserMessage);
        sysUserMessageService.updateAllColumnById(sysUserMessage);//全部更新
        
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("sys:sysusermessage:delete")
    public Hdi delete(@RequestBody Long[] ids){
        sysUserMessageService.delete(ids);

        return Hdi.ok();
    }

}