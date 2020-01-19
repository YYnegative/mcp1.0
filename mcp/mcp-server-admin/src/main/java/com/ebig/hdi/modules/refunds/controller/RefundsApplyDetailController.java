package com.ebig.hdi.modules.refunds.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.modules.refunds.entity.RefundsApplyDetailEntity;
import com.ebig.hdi.modules.refunds.entity.vo.RefundsApplyDetailVO;
import com.ebig.hdi.modules.refunds.service.RefundsApplyDetailService;

/**
 * 退货申请单明细信息
 *
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-05-28 16:24:21
 */
@RestController
@RequestMapping("/refunds/refundsapplydetail")
public class RefundsApplyDetailController {
    @Autowired
    private RefundsApplyDetailService refundsApplyDetailService;

    /**
     * 列表
     */
    @PostMapping("/list")
    //@RequiresPermissions("refunds:refundsapplydetail:list")
    public Hdi list(@RequestBody Map<String, Object> params){
        PageUtils page = refundsApplyDetailService.queryPage(params);

        return Hdi.ok().put("page", page);
    }
    
    /**
     * 信息
     */
    @RequestMapping("/selectList")
  //@RequiresPermissions("refunds:refundsapplydetail:info")
    public Hdi selectList(@RequestParam String refundsApplyNo){
        List<RefundsApplyDetailVO> rdVO = refundsApplyDetailService.selectByApplyNo(refundsApplyNo);

        return Hdi.ok().put("refundsDetail", rdVO);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("refunds:refundsapplydetail:info")
    public Hdi info(@PathVariable("id") Long id){
        RefundsApplyDetailEntity refundsApplyDetail = refundsApplyDetailService.selectById(id);

        return Hdi.ok().put("refundsApplyDetail", refundsApplyDetail);
    }
    
    
    
    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("refunds:refundsapplydetail:save")
    public Hdi save(@RequestBody RefundsApplyDetailEntity refundsApplyDetail){
        refundsApplyDetailService.insert(refundsApplyDetail);

        return Hdi.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("refunds:refundsapplydetail:update")
    public Hdi update(@RequestBody RefundsApplyDetailEntity refundsApplyDetail){
        ValidatorUtils.validateEntity(refundsApplyDetail);
        refundsApplyDetailService.updateAllColumnById(refundsApplyDetail);//全部更新
        
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("refunds:refundsapplydetail:delete")
    public Hdi delete(@RequestBody Long[] ids){
        refundsApplyDetailService.deleteBatchIds(Arrays.asList(ids));

        return Hdi.ok();
    }

}
