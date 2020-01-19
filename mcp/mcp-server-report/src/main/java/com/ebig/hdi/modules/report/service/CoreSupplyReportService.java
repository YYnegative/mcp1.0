package com.ebig.hdi.modules.report.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.modules.report.vo.CoreSupplyDetailVo;

import java.util.List;

/**
 * @author wenchao
 * @email
 * @date 2020-01-06 16:01:14
 */
public interface CoreSupplyReportService extends IService<CoreSupplyDetailVo> {


	List<CoreSupplyDetailVo> getListBySupplyMasterId(Long supplyMasterId);
	
}

