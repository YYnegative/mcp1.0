package com.ebig.mcp.server.api.http.controller;

import com.ebig.hdi.common.entity.OrgSupplierInfoEntity;
import com.ebig.hdi.common.enums.DelFlagEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.mcp.server.api.http.entity.SysDictEntity;
import com.ebig.mcp.server.api.http.service.OrgSupplierService;
import com.ebig.mcp.server.api.http.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @description:供应商供应商基础接口类
 * @author: wenchao
 * @time: 2019-10-15 14:29
 */
public class GoodsSupplierController {
    @Autowired
    private OrgSupplierService supplierService;


    @Autowired
    private SysDictService sysDictService;

    /**
     * 获取供应商信息
     * @param supplierCode
     * @return
     * @throws HdiException
     */
    protected OrgSupplierInfoEntity getSupplierInfo(String supplierCode) throws HdiException{
        if(StringUtil.isEmpty(supplierCode)){
             throw new HdiException("供应商编码为空");
        }
        OrgSupplierInfoEntity supplierInfo = supplierService.getSupplierInfo(supplierCode);
        if(supplierInfo == null){
            throw new HdiException("供应商信息不存在");
        }
        return supplierInfo;
    }
    protected SysDictEntity insert(String goodsUnit) throws HdiException{
        SysDictEntity dict = new SysDictEntity();
        dict.setName("商品单位");
        dict.setType("goods_unit");
        dict.setValue(goodsUnit);
        dict.setDelFlag(DelFlagEnum.NORMAL.getKey());
        sysDictService.insert(dict);
        return dict;
    }
}
