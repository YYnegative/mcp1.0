package com.ebig.hdi.modules.sys.controller;

import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.modules.sys.entity.SysSequenceEntity;
import com.ebig.hdi.modules.sys.service.SysSequenceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 系统序列
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-04-24 13:06:34
 */
@RestController
@RequestMapping("org/syssequence")
public class SysSequenceController {
    @Autowired
    private SysSequenceService sysSequenceService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("org:syssequence:list")
    public Hdi list(@RequestParam Map<String, Object> params){
        PageUtils page = sysSequenceService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("org:syssequence:info")
    public Hdi info(@PathVariable("id") Long id){
        SysSequenceEntity sysSequence = sysSequenceService.selectById(id);

        return Hdi.ok().put("sysSequence", sysSequence);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("org:syssequence:save")
    public Hdi save(@RequestBody SysSequenceEntity sysSequence){
        sysSequenceService.insert(sysSequence);

        return Hdi.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("org:syssequence:update")
    public Hdi update(@RequestBody SysSequenceEntity sysSequence){
        ValidatorUtils.validateEntity(sysSequence);
        sysSequenceService.updateAllColumnById(sysSequence);//全部更新
        
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("org:syssequence:delete")
    public Hdi delete(@RequestBody Long[] ids){
        sysSequenceService.deleteBatchIds(Arrays.asList(ids));

        return Hdi.ok();
    }
    /**
     * 获取序列的最大值
     */
    @RequestMapping("/selectvalue/{seqCode}")
    public String selectSeqValueBySeqCode(@PathVariable("seqCode") String seqCode){
        return sysSequenceService.selectSeqValueBySeqCode(seqCode);
    }


}
