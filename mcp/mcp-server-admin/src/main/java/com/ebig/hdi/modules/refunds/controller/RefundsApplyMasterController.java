package com.ebig.hdi.modules.refunds.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.refunds.entity.vo.RefundsApplyDetailVO;
import com.ebig.hdi.modules.refunds.entity.vo.RefundsApplyMasterVO;
import com.ebig.hdi.modules.refunds.entity.vo.RefundsMasterVO;
import com.ebig.hdi.modules.refunds.service.RefundsApplyMasterService;
import com.ebig.hdi.modules.sys.controller.AbstractController;

/**
 * 退货申请单信息
 *
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-05-28 16:24:21
 */
@RestController
@RequestMapping("/refunds/refundsapplymaster")
public class RefundsApplyMasterController extends AbstractController {
    @Autowired
    private RefundsApplyMasterService refundsApplyMasterService;

    /**
     * 列表
     */
    @PostMapping("/list")
    //@RequiresPermissions("refunds:refundsapplymaster:list")
    public Hdi list(@RequestBody Map<String, Object> params){
        PageUtils page = refundsApplyMasterService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    //@RequiresPermissions("refunds:refundsapplymaster:info")
    public Hdi info(@PathVariable("id") Long id){
        RefundsApplyMasterVO refundsApplyMasterVO = refundsApplyMasterService.selectById(id);
        return Hdi.ok().put("refundsApplyMaster", refundsApplyMasterVO);
    }
    
    /**
     * 新增退货申请详情单查询接口
     * @param 	
     * @return
     */
    @PostMapping("/selectToSave")
    //@RequiresPermissions("refunds:refundsapplymaster:selectToSave")
    public Hdi selectToSave(@RequestBody Map<String,Object> params){
    	List<RefundsApplyDetailVO> resultList = refundsApplyMasterService.selectToSave(params);
    	
    	return Hdi.ok().put("refundsApplyDetailList", resultList);
    }
    
    /**
     * 通过供应商id,医院id,库房id查询关联的医院退货申请编号
     */
    @PostMapping("/selectRefundsApplyNo")
    //@RequiresPermissions("refunds:refundsapplymaster:selectRefundsApplyNo")
    public Hdi selectRefundsApplyNo(@RequestBody Map<String,Object> params){
    	List<String> resultList = refundsApplyMasterService.selectRefundsApplyNo(params);
    	
    	return Hdi.ok().put("stringList", resultList);
    }
    
    /**
     * 生成退货单数据
     */
    @GetMapping("/change/{id}")
    //@RequiresPermissions("refunds:refundsapplymaster:change")
    public Hdi change(@PathVariable("id") Long id){
    	RefundsMasterVO refundsMasterVO = refundsApplyMasterService.change(id);
        return Hdi.ok().put("refundsMaster", refundsMasterVO);
    }
    
    /**
     * 确认接口
     */
    @PostMapping("/determine")
    //@RequiresPermissions("refunds:refundsapplymaster:determine")
    public Hdi determine(@RequestBody Long[] ids){
    	refundsApplyMasterService.determine(ids,getUserId());
    	return Hdi.ok();
    }
    
    /**
     * 保存
     */
    @PostMapping("/save")
    //@RequiresPermissions("refunds:refundsapplymaster:save")
    public Hdi save(@RequestBody RefundsApplyMasterVO refundsApplyMasterVO){
    	refundsApplyMasterVO.setDeptId(getDeptId());
    	refundsApplyMasterVO.setCreateId(getUserId());
        refundsApplyMasterService.save(refundsApplyMasterVO);

        return Hdi.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    //@RequiresPermissions("refunds:refundsapplymaster:update")
    public Hdi update(@RequestBody RefundsApplyMasterVO refundsApplyMasterVO){
        //ValidatorUtils.validateEntity(refundsApplyMaster);
    	refundsApplyMasterVO.setEditId(getUserId());
        refundsApplyMasterService.update(refundsApplyMasterVO);
        
        return Hdi.ok();
    }
    
    
    /**
     * 删除
     */
    @PostMapping("/delete")
    //@RequiresPermissions("refunds:refundsapplymaster:delete")
    public Hdi delete(@RequestBody Long[] ids){
        refundsApplyMasterService.deleteBatchIds(Arrays.asList(ids));

        return Hdi.ok();
    }

}
