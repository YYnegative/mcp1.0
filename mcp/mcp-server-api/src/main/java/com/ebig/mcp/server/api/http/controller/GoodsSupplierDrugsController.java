package com.ebig.mcp.server.api.http.controller;

import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.ebig.hdi.common.entity.OrgSupplierInfoEntity;
import com.ebig.hdi.common.enums.HttpStatusEnums;
import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.mcp.server.api.http.entity.SysDictEntity;
import com.ebig.mcp.server.api.http.service.GoodsSupplierDrugsService;
import com.ebig.mcp.server.api.http.service.SysDictService;
import com.ebig.mcp.server.api.http.vo.GoodsSupplierDrugsVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description:供应商药品接口类
 * @author: wenchao
 * @time: 2019-10-15 14:29
 */
@RestController
@RequestMapping("/drugs/supplierdrugs")
@Slf4j
public class GoodsSupplierDrugsController extends GoodsSupplierController {

    @Autowired
    private GoodsSupplierDrugsService supplierDrugsService;


    @Autowired
    private SysDictService sysDictService;

    @PostMapping("/save")
    public Hdi save(@RequestBody List<GoodsSupplierDrugsVo> list, HttpServletRequest request) {
        try {
            //查询出存储方式
            List<SysDictEntity> dects = sysDictService.selectDictByType("store_way");
            //商品单位
            List<SysDictEntity> goodsUnits = sysDictService.selectDictByType("goods_unit");
            if (CollectionUtils.isNotEmpty(dects)) {
                Map<String, String> map = dects.stream().collect(Collectors.toMap(SysDictEntity::getValue, SysDictEntity::getCode));
                Map<String, String>  unitMap = goodsUnits.stream().collect(Collectors.toMap(SysDictEntity::getValue, SysDictEntity::getCode));
                OrgSupplierInfoEntity supplierInfo = super.getSupplierInfo(request.getHeader("supplierCode"));
                for (int i = 0; i < list.size(); i++) {
                    String message = StringUtil.checkFieldIsNull(list.get(i));
                    if (StringUtils.isNotEmpty(message)) {
                        return Hdi.error(HttpStatusEnums.ERRCODE_INVOKE_ERR.getKey(),"第" + (i + 1) + "个数据对象的" + message + "属性为空");
                    }
                    //存储方式转换
                    list.get(i).setStoreWay(map.get(list.get(i).getStoreWay()));
                    //商品单位转换
                    String code = unitMap.get(list.get(i).getGoodsUnit());
                    if(StringUtil.isEmpty(code)){
                        super.insert(list.get(i).getGoodsUnit());
                    }
                    list.get(i).setGoodsUnit(code);
                    //供货单位转换
                    String supplyUnit = unitMap.get(list.get(i).getSupplyUnit());
                    if(StringUtil.isEmpty(supplyUnit)){
                        super.insert(list.get(i).getSupplyUnit());
                    }
                    list.get(i).setSupplyUnit(supplyUnit);
                }
                supplierDrugsService.saveBatch(list, supplierInfo);
                return Hdi.ok();
            }
            return Hdi.error(HttpStatusEnums.ERRCODE_INVOKE_ERR.getKey(),HttpStatusEnums.ERRCODE_INVOKE_ERR.getValue());
        } catch (Exception e) {
            log.error(e.getMessage());
            return Hdi.error(e.getMessage());
        }
    }
}
