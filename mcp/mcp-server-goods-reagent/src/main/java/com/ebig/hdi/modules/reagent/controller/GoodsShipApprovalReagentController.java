package com.ebig.hdi.modules.reagent.controller;

import com.ebig.hdi.common.entity.UnicodeGoodsShipApprovalEntity;
import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.modules.core.service.UnicodeGoodsShipApprovalService;
import com.ebig.hdi.modules.reagent.service.GoodsShipApprovalReagentService;
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
@RestController("UnicodeGoodsShipReagentCont     roller")
@RequestMapping("/unicode/Reagent/unicodegoodsship")
public class GoodsShipApprovalReagentController extends AbstractController{
    @Autowired
    private GoodsShipApprovalReagentService goodsShipApprovalReagentService;


    @Autowired
    private UnicodeGoodsShipApprovalService unicodeGoodsShipApprovalService;

    /**
     * 医院商品匹对记录分页列表
     */
    @RequestMapping("/hospitalGoodsList")
    //@RequiresPermissions("unicode:unicodegoodsship:list")
    public Hdi hospitalGoodsList(@RequestBody Map<String, Object> params){
        PageUtils goodsList = goodsShipApprovalReagentService.queryPageHospitalGoods(params);

        return Hdi.ok().put("page", goodsList);
    }
    
    /**
     * 供应商商品匹对记录分页列表
     */
    @RequestMapping("/supplierGoodsList")
    //@RequiresPermissions("unicode:unicodegoodsship:list")
    public Hdi supplierGoodsList(@RequestBody Map<String, Object> params){
        PageUtils goodsList = goodsShipApprovalReagentService.queryPageSupplierGoods(params);

        return Hdi.ok().put("page", goodsList);
    }

    /**
     * 匹对医院商品分页查询
     */
    @PostMapping("/hospitalGoods")
    //@RequiresPermissions("unicode:unicodesupplyship:list")
    public Hdi hospitalGoods(@RequestBody Map<String, Object> params){
        PageUtils pageUtils = goodsShipApprovalReagentService.pGoodsList(params);

        return Hdi.ok().put("page", pageUtils);
    }
    
    /**
     * 匹对供应商商品分页查询
     */
    @PostMapping("/supplierGoods")
    //@RequiresPermissions("unicode:unicodesupplyship:list")
    public Hdi supplierGoods(@RequestBody Map<String, Object> params){
        PageUtils pageUtils = goodsShipApprovalReagentService.pGoodsList(params);
       
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
        goodsShipApprovalReagentService.updateHospitalPgoodsId(getUser(),unicodeGoodsShip);
        
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
        goodsShipApprovalReagentService.updateSupplierPgoodsId(getUser(),unicodeGoodsShip);//全部更新
        
        return Hdi.ok();
    }
    @PostMapping("/supplierCheckstatus")
    public Hdi checkStatus(@RequestBody Map<String,String> params){
        Map<String,String> map = goodsShipApprovalReagentService.checkStatus(params,getUser());
        if (!map.isEmpty()){
            return Hdi.error(map.get("errorMessage"));
        }
        return Hdi.ok();
    }

    @PostMapping("/hospitalCheckstatus")
    public Hdi hospitalCheckstatus(@RequestBody Map<String,String> params){
        Map<String,String> map = goodsShipApprovalReagentService.hospitalCheckstatus(params,getUser());
        if (!map.isEmpty()){
            return Hdi.error(map.get("errorMessage"));
        }
        return Hdi.ok();
    }





}
