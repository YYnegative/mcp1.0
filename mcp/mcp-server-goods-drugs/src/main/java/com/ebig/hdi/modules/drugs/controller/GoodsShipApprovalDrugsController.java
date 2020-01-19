package com.ebig.hdi.modules.drugs.controller;

import com.ebig.hdi.common.entity.UnicodeGoodsShipApprovalEntity;
import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.modules.drugs.service.GoodsShipApprovalDrugsService;
import com.ebig.hdi.modules.sys.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-21 15:18:03
 */
@RestController("unicodeGoodsShipDrugsController")
@RequestMapping("/unicode/drugs/unicodegoodsship")
public class GoodsShipApprovalDrugsController extends AbstractController{
    @Autowired
    private GoodsShipApprovalDrugsService goodsShipApprovalDrugsService;

    /**
     * 医院商品匹对记录分页列表
     */
    @RequestMapping("/hospitalGoodsList")
    //@RequiresPermissions("unicode:unicodegoodsship:list")
    public Hdi hospitalGoodsList(@RequestBody Map<String, Object> params){
        PageUtils goodsList = goodsShipApprovalDrugsService.queryPageHospitalGoods(params);

        return Hdi.ok().put("page", goodsList);
    }
    
    /**
     * 供应商商品匹对记录分页列表
     */
    @RequestMapping("/supplierGoodsList")
    //@RequiresPermissions("unicode:unicodegoodsship:list")
    public Hdi supplierGoodsList(@RequestBody Map<String, Object> params){
        PageUtils goodsList = goodsShipApprovalDrugsService.queryPageSupplierGoods(params);

        return Hdi.ok().put("page", goodsList);
    }
    
    /**
     * 匹对医院商品分页查询
     */
    @PostMapping("/hospitalGoods")
    //@RequiresPermissions("unicode:unicodesupplyship:list")
    public Hdi hospitalGoods(@RequestBody Map<String, Object> params){
        PageUtils pageUtils = goodsShipApprovalDrugsService.pGoodsList(params);

        return Hdi.ok().put("page", pageUtils);
    }
    
    /**
     * 匹对供应商商品分页查询
     */
    @PostMapping("/supplierGoods")
    //@RequiresPermissions("unicode:unicodesupplyship:list")
    public Hdi supplierGoods(@RequestBody Map<String, Object> params){
        PageUtils pageUtils = goodsShipApprovalDrugsService.pGoodsList(params);
       
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
        goodsShipApprovalDrugsService.updateHospitalPgoodsId(getUser(),unicodeGoodsShip);
        
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
        goodsShipApprovalDrugsService.updateSupplierPgoodsId(getUser(),unicodeGoodsShip);//全部更新
        
        return Hdi.ok();
    }
    @PostMapping("/supplierCheckstatus")
    public Hdi checkStatus(@RequestBody Map<String,String> params){
        Map<String,String> map = goodsShipApprovalDrugsService.checkStatus(params,getUser());
        if (!map.isEmpty()){
            return Hdi.error(map.get("errorMessage"));
        }
        return Hdi.ok();
    }

    @PostMapping("/hospitalCheckstatus")
    public Hdi hospitalCheckstatus(@RequestBody Map<String,String> params){
        Map<String,String> map = goodsShipApprovalDrugsService.hospitalCheckstatus(params,getUser());
        if (!map.isEmpty()){
            return Hdi.error(map.get("errorMessage"));
        }
        return Hdi.ok();
    }

}
