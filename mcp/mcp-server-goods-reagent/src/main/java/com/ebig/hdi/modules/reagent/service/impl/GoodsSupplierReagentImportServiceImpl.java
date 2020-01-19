package com.ebig.hdi.modules.reagent.service.impl;

import com.ebig.hdi.modules.core.service.impl.BaseImportServiceImpl;
import com.ebig.hdi.modules.reagent.service.GoodsSupplierReagentImportService;
import com.ebig.hdi.modules.reagent.service.GoodsSupplierReagentService;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @program: mcp
 * @description:
 * @email 791203472@qq.com
 * @author: ZhengHaiWen
 * @create: 2019-12-16 17:11
 **/
@Service("GoodsSupplierReagentImportService")
public class GoodsSupplierReagentImportServiceImpl extends BaseImportServiceImpl implements GoodsSupplierReagentImportService {
    @Autowired
   private GoodsSupplierReagentService goodsSupplierReagentService;


    @Override
    public Map<String, String> importData(String[][] rows, SysUserEntity sysUserEntity) {
        return goodsSupplierReagentService.importData(rows,sysUserEntity);
    }
}
