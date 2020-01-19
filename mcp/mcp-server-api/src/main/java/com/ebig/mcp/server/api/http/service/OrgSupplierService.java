package com.ebig.mcp.server.api.http.service;

import com.ebig.hdi.common.entity.OrgSupplierInfoEntity;

/**
 * @description:供应商服务类
 * @author: wenchao
 * @time: 2019-10-17 15:54
 */
public interface OrgSupplierService {

    /**
     * 获取供应商id
     * @param supplierCode
     * @return
     */
    OrgSupplierInfoEntity getSupplierInfo(String supplierCode);
}
