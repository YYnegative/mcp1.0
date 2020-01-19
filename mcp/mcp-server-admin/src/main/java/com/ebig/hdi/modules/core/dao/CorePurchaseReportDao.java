package com.ebig.hdi.modules.core.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.modules.core.entity.CorePurchaseMonthReportEntity;
import com.ebig.hdi.modules.core.entity.CorePurchaseReportEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by wen on 2019/8/13.
 */
public interface CorePurchaseReportDao extends BaseMapper<CorePurchaseReportEntity> {
    List<CorePurchaseReportEntity> getListByMap(Map<String, Object> params);

    List<CorePurchaseMonthReportEntity> getMonthDataBySupplieId(@Param("supplieId")Long supplieId);
}
