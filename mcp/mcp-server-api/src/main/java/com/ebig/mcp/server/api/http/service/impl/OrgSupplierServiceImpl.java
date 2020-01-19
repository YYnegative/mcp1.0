package com.ebig.mcp.server.api.http.service.impl;

import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.ebig.hdi.common.entity.OrgSupplierInfoEntity;
import com.ebig.mcp.server.api.http.service.OrgSupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:供应商服务实现类
 * @author: wenchao
 * @time: 2019-10-17 16:30
 */
@Service
public class OrgSupplierServiceImpl implements OrgSupplierService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public OrgSupplierInfoEntity getSupplierInfo(String supplierCode) {
        String sql = "select id,supplier_code,dept_id from hdi_org_supplier_info where del_flag = 0 and supplier_code =?";
        List<OrgSupplierInfoEntity> list = jdbcTemplate.query(sql,new Object[]{supplierCode},new OrgSupplierInfoEntity());
        if(CollectionUtils.isNotEmpty(list)){
            return list.get(0);
        }
        return null;
    }
}
