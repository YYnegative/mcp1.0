package com.ebig.hdi.modules.refunds.controller;

import java.util.Arrays;
import java.util.List;
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
import com.ebig.hdi.modules.refunds.entity.RefundsDetailEntity;
import com.ebig.hdi.modules.refunds.entity.vo.RefundsDetailVO;
import com.ebig.hdi.modules.refunds.service.RefundsDetailService;

/**
 * 退货单明细信息
 *
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-05-28 16:24:21
 */
@RestController
@RequestMapping("/refunds/refundsdetail")
public class RefundsDetailController {
    @Autowired
    private RefundsDetailService refundsDetailService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("refunds:refundsdetail:list")
    public Hdi list(@RequestBody Map<String, Object> params){
        PageUtils page = refundsDetailService.queryPage(params);

        return Hdi.ok().put("page", page);
    }
    
    /**
     * 信息
     */
    @RequestMapping("/selectList")
    //@RequiresPermissions("refunds:refundsdetail:info")
    public Hdi selectList(@RequestParam String refundsApplyNo){
        List<RefundsDetailVO> rdVO = refundsDetailService.selectByApplyNo(refundsApplyNo);

        return Hdi.ok().put("refundsDetail", rdVO);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("refunds:refundsdetail:info")
    public Hdi info(@PathVariable("id") Long id){
        RefundsDetailEntity refundsDetail = refundsDetailService.selectById(id);

        return Hdi.ok().put("refundsDetail", refundsDetail);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("refunds:refundsdetail:save")
    public Hdi save(@RequestBody RefundsDetailEntity refundsDetail){
        refundsDetailService.insert(refundsDetail);

        return Hdi.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("refunds:refundsdetail:update")
    public Hdi update(@RequestBody RefundsDetailEntity refundsDetail){
        ValidatorUtils.validateEntity(refundsDetail);
        refundsDetailService.updateAllColumnById(refundsDetail);//全部更新
        
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("refunds:refundsdetail:delete")
    public Hdi delete(@RequestBody Long[] ids){
        refundsDetailService.deleteBatchIds(Arrays.asList(ids));

        return Hdi.ok();
    }

}
