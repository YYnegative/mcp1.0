package com.ebig.hdi.modules.consumables.controller;

import com.ebig.hdi.common.entity.UnicodeGoodsShipHistEntity;
import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.modules.core.service.UnicodeGoodsShipHistService;
import com.ebig.hdi.modules.sys.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

/**
 * 
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-22 09:26:07
 */
@RestController
@RequestMapping("/unicode/unicodegoodsshiphist")
public class UnicodeGoodsShipHistController extends AbstractController{
    @Autowired
    private UnicodeGoodsShipHistService unicodeGoodsShipHistService;

    /**
     * 医院商品历史列表
     */
    @RequestMapping("/hospitalList")
    //@RequiresPermissions("unicode:unicodegoodsshiphist:list")
    public Hdi hospitalList(@RequestBody Map<String, Object> params){
        PageUtils page = unicodeGoodsShipHistService.queryPageHospital(params);
        
        return Hdi.ok().put("page", page);
    }
    
    
    /**
     * 供应商商品历史列表
     */
    @RequestMapping("/supplierList")
    //@RequiresPermissions("unicode:unicodegoodsshiphist:list")
    public Hdi supplierList(@RequestBody Map<String, Object> params){
        PageUtils page = unicodeGoodsShipHistService.queryPageSupplier(params);
        
        return Hdi.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{shiphistId}")
    //@RequiresPermissions("unicode:unicodegoodsshiphist:info")
    public Hdi info(@PathVariable("shiphistId") Long shiphistId){
        UnicodeGoodsShipHistEntity unicodeGoodsShipHist = unicodeGoodsShipHistService.selectById(shiphistId);

        return Hdi.ok().put("unicodeGoodsShipHist", unicodeGoodsShipHist);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("unicode:unicodegoodsshiphist:save")
    public Hdi save(@RequestBody UnicodeGoodsShipHistEntity unicodeGoodsShipHist){
        unicodeGoodsShipHistService.insert(unicodeGoodsShipHist);

        return Hdi.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("unicode:unicodegoodsshiphist:update")
    public Hdi update(@RequestBody UnicodeGoodsShipHistEntity unicodeGoodsShipHist){
        ValidatorUtils.validateEntity(unicodeGoodsShipHist);
        unicodeGoodsShipHistService.updateAllColumnById(unicodeGoodsShipHist);//全部更新
        
        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("unicode:unicodegoodsshiphist:delete")
    public Hdi delete(@RequestBody Long[] shiphistIds){
        unicodeGoodsShipHistService.deleteBatchIds(Arrays.asList(shiphistIds));

        return Hdi.ok();
    }

}
