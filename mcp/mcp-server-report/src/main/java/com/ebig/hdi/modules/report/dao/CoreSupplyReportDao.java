package com.ebig.hdi.modules.report.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.modules.report.vo.CoreSupplyDetailVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wenchao
 * @email
 * @date 2020-01-06 16:01:14
 */

public interface CoreSupplyReportDao extends BaseMapper<CoreSupplyDetailVo> {
    List<CoreSupplyDetailVo> getListBySupplyMasterId(@Param("supplyMasterId")Long supplyMasterId);
}
