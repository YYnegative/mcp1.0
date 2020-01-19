package com.ebig.hdi.modules.core.service.impl;

import com.ebig.hdi.modules.core.dao.CorePurchaseReportDao;
import com.ebig.hdi.modules.core.entity.CorePurchaseMonthReportEntity;
import com.ebig.hdi.modules.core.entity.CorePurchaseReportEntity;
import com.ebig.hdi.modules.core.service.CorePurchaseReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by wen on 2019/8/13.
 */
@Service("corePurchaseReportService")
public class CorePurchaseReportServiceImpl implements CorePurchaseReportService {

    @Autowired
    private CorePurchaseReportDao corepurchaseReportDao;

    @Override
    public List<CorePurchaseReportEntity> getListByMap(Map<String, Object> params) {
        List<CorePurchaseReportEntity> dblist = corepurchaseReportDao.getListByMap(params);
        if (!CollectionUtils.isEmpty(dblist)) {
            Map<String, CorePurchaseReportEntity> map = new HashMap<>();
            for (CorePurchaseReportEntity entity : dblist) {
                if (null != map.get(entity.getMark())) {
                    Double totalPrice = map.get(entity.getMark()).getTotalPrice();
                    entity.setTotalPrice(totalPrice + entity.getTotalPrice());
                }
                map.put(entity.getMark(), entity);
            }
            Collection<CorePurchaseReportEntity> valueCollection = map.values();
            List<CorePurchaseReportEntity> valueList = new ArrayList<>(valueCollection);
            if(!CollectionUtils.isEmpty(valueList)){
                valueList.stream()
                        .map(i -> i.getTotalPrice()).distinct()
                        .collect(Collectors.toList()).sort(Comparator.reverseOrder()); //倒序
            }
            return valueList;
        }
        return new ArrayList<>();
    }
    @Override
    public List<CorePurchaseMonthReportEntity> getMonthDataBySupplieId(Long supplieId) {
        return corepurchaseReportDao.getMonthDataBySupplieId(supplieId);
    }
}
