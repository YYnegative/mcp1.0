package com.ebig.hdi.modules.goods.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.modules.goods.entity.GoodsSupplierSendEntity;
import com.ebig.hdi.modules.goods.service.GoodsSupplierSendService;
import com.ebig.hdi.modules.sys.controller.AbstractController;

/**
 * 供应商品下发
 *
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-07-26 11:49:04
 */
@RestController
@RequestMapping("goods/goodssuppliersend")
public class GoodsSupplierSendController extends AbstractController{
    @Autowired
    private GoodsSupplierSendService goodsSupplierSendService;

    /**
     * 查询可下发商品
     */
    @PostMapping("/sendableList")
    //@RequiresPermissions("goods:goodssuppliersend:list")
    public Hdi sendableList(@RequestBody Map<String, Object> params){
        PageUtils page = goodsSupplierSendService.selectSendableList(params);
        return Hdi.ok().put("page", page);
    }
    
    /**
     * 查询已下发商品
     */
    @PostMapping("/sentList")
    //@RequiresPermissions("goods:goodssuppliersend:list")
    public Hdi sentList(@RequestBody Map<String, Object> params){

        PageUtils page = goodsSupplierSendService.selectSentList(params);
        //测试未下发目录sql
//        List<TempPubSupplyGoodsEntity> tempPubSupplyGoodsEntities = goodsSupplierSendService.selectNotUploadNew(10);
//        System.out.println("-----notupload---"+tempPubSupplyGoodsEntities.size());
        return Hdi.ok().put("page", page);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("goods:goodssuppliersend:save")
    public Hdi save(@RequestBody List<GoodsSupplierSendEntity> goodsSupplierSendList){
    	for (GoodsSupplierSendEntity goodsSupplierSend : goodsSupplierSendList) {
	    	ValidatorUtils.validateEntity(goodsSupplierSend);
	    	goodsSupplierSend.setCreateId(getUserId());
    	}
    	goodsSupplierSendService.batchSave(goodsSupplierSendList);
        return Hdi.ok();
    }
    

}
