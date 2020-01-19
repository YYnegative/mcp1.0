package com.ebig.hdi.modules.report.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.modules.report.dao.CoreSupplyReportDao;
import com.ebig.hdi.modules.report.service.CoreSupplyReportService;
import com.ebig.hdi.modules.report.vo.CoreSupplyDetailVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wenchao
 * @email
 * @date 2020-01-06 16:01:14
 */
@Service("coreSupplyReportService")
public class CoreSupplyReportServiceImpl extends ServiceImpl<CoreSupplyReportDao, CoreSupplyDetailVo> implements CoreSupplyReportService {

    @Override
    public List<CoreSupplyDetailVo> getListBySupplyMasterId(Long supplyMasterId) {
        return this.baseMapper.getListBySupplyMasterId(supplyMasterId);
    }
}
