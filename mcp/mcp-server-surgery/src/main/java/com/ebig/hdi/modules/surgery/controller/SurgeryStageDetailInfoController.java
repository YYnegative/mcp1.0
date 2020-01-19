package com.ebig.hdi.modules.surgery.controller;

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
import com.ebig.hdi.modules.surgery.entity.SurgeryStageDetailInfoEntity;
import com.ebig.hdi.modules.surgery.entity.vo.SurgeryStageDetailInfoVO;
import com.ebig.hdi.modules.surgery.service.SurgeryStageDetailInfoService;
import com.ebig.hdi.modules.sys.controller.AbstractController;

/**
 * 手术跟台目录明细表
 *
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-06-11 10:15:32
 */
@RestController
@RequestMapping("/surgery/surgerystagedetailinfo")
public class SurgeryStageDetailInfoController extends AbstractController {
    @Autowired
    private SurgeryStageDetailInfoService surgeryStageDetailInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("surgery:surgerystagedetailinfo:list")
    public Hdi list(@RequestBody Map<String, Object> params){
        PageUtils page = surgeryStageDetailInfoService.queryPage(params);

        return Hdi.ok().put("page", page);
    }
    
    /**
     * 新增跟台目录查询接口
     * @param 	
     * @return
     */
    @PostMapping("/selectToSave")
    //@RequiresPermissions("surgery:surgerystagedetailinfo:selectToSave")
    public Hdi selectToSave(@RequestBody Map<String,Object> params){
    	List<SurgeryStageDetailInfoVO> resultList = surgeryStageDetailInfoService.selectToSave(params);
    	
    	return Hdi.ok().put("SurgeryStageDetailInfoList", resultList);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("surgery:surgerystagedetailinfo:info")
    public Hdi info(@PathVariable("id") Long id){
        SurgeryStageDetailInfoEntity surgeryStageDetailInfo = surgeryStageDetailInfoService.selectById(id);

        return Hdi.ok().put("surgeryStageDetailInfo", surgeryStageDetailInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("surgery:surgerystagedetailinfo:save")
    public Hdi save(@RequestBody SurgeryStageDetailInfoEntity surgeryStageDetailInfo){
        surgeryStageDetailInfoService.insert(surgeryStageDetailInfo);

        return Hdi.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("surgery:surgerystagedetailinfo:update")
    public Hdi update(@RequestBody SurgeryStageDetailInfoEntity surgeryStageDetailInfo){
        ValidatorUtils.validateEntity(surgeryStageDetailInfo);
        surgeryStageDetailInfoService.updateAllColumnById(surgeryStageDetailInfo);//全部更新
        
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("surgery:surgerystagedetailinfo:delete")
    public Hdi delete(@RequestBody Long[] ids){
        surgeryStageDetailInfoService.deleteBatchIds(Arrays.asList(ids));

        return Hdi.ok();
    }

}
