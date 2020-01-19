package com.ebig.hdi.modules.consumables.controller;

import com.ebig.hdi.common.entity.UnicodeGoodsShipApprovalEntity;
import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.modules.consumables.service.GoodsShipApprovalConsumablesService;
import com.ebig.hdi.modules.core.service.UnicodeGoodsShipApprovalService;
import com.ebig.hdi.modules.sys.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-21 15:18:03
 */
@RestController
@RequestMapping("/unicode/unicodegoodsship")
public class GoodsShipApprovalConsumablesController extends AbstractController{
    @Autowired
    private GoodsShipApprovalConsumablesService goodsShipApprovalConsumablesService;


    @Autowired
    private UnicodeGoodsShipApprovalService unicodeGoodsShipApprovalService;

    /**
     * 医院商品匹对记录分页列表
     */
    @RequestMapping("/hospitalGoodsList")
    //@RequiresPermissions("unicode:unicodegoodsship:list")
    public Hdi hospitalGoodsList(@RequestBody Map<String, Object> params){
        PageUtils goodsList = goodsShipApprovalConsumablesService.queryPageHospitalGoods(params);

        return Hdi.ok().put("page", goodsList);
    }
    
    /**
     * 供应商商品匹对记录分页列表
     */
    @RequestMapping("/supplierGoodsList")
    //@RequiresPermissions("unicode:unicodegoodsship:list")
    public Hdi supplierGoodsList(@RequestBody Map<String, Object> params){
        PageUtils goodsList = goodsShipApprovalConsumablesService.queryPageSupplierGoods(params);

        return Hdi.ok().put("page", goodsList);
    }
    
    
    /**
     * 匹对医院商品分页查询
     */
    @PostMapping("/hospitalGoods")
    //@RequiresPermissions("unicode:unicodesupplyship:list")
    public Hdi hospitalGoods(@RequestBody Map<String, Object> params){
        PageUtils pageUtils = goodsShipApprovalConsumablesService.pGoodsList(params);

        return Hdi.ok().put("page", pageUtils);
    }

    /**
     * 匹对供应商商品分页查询
     */
    @PostMapping("/supplierGoods")
    //@RequiresPermissions("unicode:unicodesupplyship:list")
    public Hdi supplierGoods(@RequestBody Map<String, Object> params){
        PageUtils pageUtils = goodsShipApprovalConsumablesService.pGoodsList(params);
        return Hdi.ok().put("page", pageUtils);
    }

    /**
     * 医院商品匹对
     */
    @RequestMapping("/updateHospitalPgoodsId")
    //@RequiresPermissions("unicode:unicodegoodsship:update")
    public Hdi updateHospitalPgoodsId(@RequestBody UnicodeGoodsShipApprovalEntity unicodeGoodsShip){
        ValidatorUtils.validateEntity(unicodeGoodsShip);
        //全部更新
        goodsShipApprovalConsumablesService.updateHospitalPgoodsId(getUser(),unicodeGoodsShip);

        return Hdi.ok();
    }
    /**
     * 供应商商品匹对
     */
    @RequestMapping("/updateSupplierPgoodsId")
    //@RequiresPermissions("unicode:unicodegoodsship:update")
    public Hdi updateSupplierPgoodsId(@RequestBody UnicodeGoodsShipApprovalEntity unicodeGoodsShip){
        ValidatorUtils.validateEntity(unicodeGoodsShip);
        unicodeGoodsShip.setEditmanid(getUserId());
        unicodeGoodsShip.setEditmanname(getUser().getUsername());
        goodsShipApprovalConsumablesService.updateSupplierPgoodsId(getUser(),unicodeGoodsShip);//全部更新
        return Hdi.ok();
    }

    @PostMapping("/supplierCheckstatus")
    public Hdi checkStatus(@RequestBody Map<String,String> params){
        Map<String,String> map = goodsShipApprovalConsumablesService.checkStatus(params,getUser());
        if (!map.isEmpty()){
            return Hdi.error(map.get("errorMessage"));
        }
        return Hdi.ok();
    }

    //医院耗材审批
    @PostMapping("/hospitalCheckstatus")
    public Hdi hospitalCheckstatus(@RequestBody Map<String,String> params){
        Map<String,String> map = goodsShipApprovalConsumablesService.hospitalCheckstatus(params,getUser());
        if (!map.isEmpty()){
            return Hdi.error(map.get("errorMessage"));
        }
        return Hdi.ok();
    }
}
