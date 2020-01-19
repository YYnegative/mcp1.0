package com.ebig.hdi.modules.core.controller;


import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ebig.hdi.common.enums.TypeEnum;
import com.ebig.hdi.common.utils.DateUtils;
import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.modules.core.entity.CorePurchaseMonthReportEntity;
import com.ebig.hdi.modules.core.entity.CorePurchaseReportEntity;
import com.ebig.hdi.modules.core.service.CorePurchaseReportService;
import com.ebig.hdi.modules.sys.controller.AbstractController;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import com.ebig.hdi.modules.sys.service.SysUserService;

/**
 * 采购单报表统计
 * Created by wen on 2019/8/13.
 */
@RestController
@RequestMapping("/core/corepurchasereport")
public class CorePurchaseReportController extends AbstractController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private CorePurchaseReportService corePurchaseReportService;

    //统计当前时间止12个月每月数据
    private static Integer  TIMEINTERVAL = 12;

    /**
     * 采购金额前五商品统计（统计当前月）
     */
    @GetMapping("/gettop5")
    public Hdi getTop5() {
        SysUserEntity user = getUser();
        Map<String, Object> map = sysUserService.selectSupplierIdAndNameByDeptId(user.getDeptId());
        List<CorePurchaseReportEntity> list = new ArrayList<>();
        Map<String, Object> paramMap = new HashMap<>();
        if (map != null && map.get("id") != null) {
            paramMap.put("supplieId", Long.valueOf(map.get("id").toString()));
        }else if(user.getUserType().equals(TypeEnum.USER_PLATFORM.getKey())) {
            paramMap.put("supplieId",null);
        }
        paramMap.put("startDate", DateUtils.getStartOfMonth());
        paramMap.put("endDate", DateUtils.getLastOfMonth());
        list = corePurchaseReportService.getListByMap(paramMap);
        if (!CollectionUtils.isEmpty(list)) {
            //取出前五
            if (list.size() > 5) {
                list = list.subList(0, 5);
            }
            //转换成万元
            for (CorePurchaseReportEntity entity : list) {
                entity.setTotalPrice(entity.getTotalPrice() / 10000.0);
            }
        }
        return Hdi.ok().put("list", list);
    }

    /**
     * 采购金额月统计（统计当前年每月）
     */
    @GetMapping("/getmonthdata")
    public Hdi getMonthData() {
        SysUserEntity user = getUser();
        Map<String, Object> map = sysUserService.selectSupplierIdAndNameByDeptId(user.getDeptId());
        List<CorePurchaseMonthReportEntity> list = new ArrayList<>();
        if (map != null && map.get("id") != null) {
            list = corePurchaseReportService.getMonthDataBySupplieId(Long.valueOf(map.get("id").toString()));
        }else if(user.getUserType().equals(TypeEnum.USER_PLATFORM.getKey())){
            list = corePurchaseReportService.getMonthDataBySupplieId(null);
        }
        //转换成万元
        if (!CollectionUtils.isEmpty(list)) {
            for (CorePurchaseMonthReportEntity entity : list) {
                entity.setTotalPrice(entity.getTotalPrice() / 10000.0);
            }
        }
        Map<String,CorePurchaseMonthReportEntity> doubleMap = new HashMap<>(TIMEINTERVAL);
        for (int i=0;i<TIMEINTERVAL;i++ ){
            CorePurchaseMonthReportEntity entity = new CorePurchaseMonthReportEntity();
            entity.setRecordDate(DateUtils.format(DateUtils.addDateMonths(new Date(),-(i)),"yyyy-MM"));
            entity.setTotalPrice(0D);
            doubleMap.put(DateUtils.format(DateUtils.addDateMonths(new Date(),-(i)),"yyyy-MM"),entity);
        }
        for (CorePurchaseMonthReportEntity entity:list) {
            doubleMap.put(entity.getRecordDate(),entity);
        }
        List<CorePurchaseMonthReportEntity> returnList = new ArrayList<>();
        doubleMap.forEach((key, value)->{
            returnList.add(value);
        });
        returnList.sort((a, b) -> a.getRecordDate().compareTo(b.getRecordDate()));
        return Hdi.ok().put("list",returnList);
    }
}
