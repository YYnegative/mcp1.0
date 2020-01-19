package com.ebig.mcp.server.api.http.service.impl;

import com.ebig.hdi.common.entity.MasterDetailsCommonEntity;
import com.ebig.hdi.common.entity.OrgSupplierInfoEntity;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.mcp.server.api.http.dao.SaleBackDao;
import com.ebig.mcp.server.api.http.entity.RefundsDetailEntity;
import com.ebig.mcp.server.api.http.entity.RefundsMasterEntity;
import com.ebig.mcp.server.api.http.service.OrgSupplierService;
import com.ebig.mcp.server.api.http.service.SaleBackService;
import com.ebig.mcp.server.api.http.vo.RefundsDetailVo;
import com.ebig.mcp.server.api.http.vo.RefundsMasterVo;
import com.ebig.mcp.server.api.http.vo.SaleBackVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@Slf4j
public class SaleBackServiceImpl implements SaleBackService {

    @Autowired
    SaleBackDao saleBackDao;
    @Autowired
    private OrgSupplierService supplierService;

    /**
     * 获取ERP根据条件获取的退货单信息
     * @param saleBackVo
     * @return
     */
    @Override
    public List<MasterDetailsCommonEntity> downloadsaleback(SaleBackVo saleBackVo) {

        try {
           supplierService.getSupplierInfo(saleBackVo.getSupplierCode());
        } catch (Exception e) {
            throw new HdiException(e.getMessage());
        }

        List<MasterDetailsCommonEntity> resultList = new ArrayList<>();
        //根据条件获取退货主单信息
        List<RefundsMasterVo> refundsMasterByIf = null;
        try {
            refundsMasterByIf = saleBackDao.getRefundsMasterByIf(saleBackVo);
        } catch (Exception e) {
            throw new HdiException("没有找到退货主单信息");
        }

        for (int i = 0; i < refundsMasterByIf.size(); i++) {
            MasterDetailsCommonEntity masterDetailsCommonEntity = new MasterDetailsCommonEntity();
            masterDetailsCommonEntity.setMaster(refundsMasterByIf.get(i));
            List<RefundsDetailVo> refundsDetailList = saleBackDao.getRefundsDetailByMasterId(refundsMasterByIf.get(i).getSALBACKSEQ());
            masterDetailsCommonEntity.setDetails(refundsDetailList);
            resultList.add(masterDetailsCommonEntity);
        }

        return resultList;
    }
}
