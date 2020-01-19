package com.ebig.hdi.modules.unicode.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.common.validator.group.AddGroup;
import com.ebig.hdi.common.validator.group.UpdateGroup;
import com.ebig.hdi.modules.sys.controller.AbstractController;
import com.ebig.hdi.modules.unicode.entity.UnicodeSupplyShipEntity;
import com.ebig.hdi.modules.unicode.service.UnicodeSupplyShipService;

/**
 * 
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-17 10:56:27
 */
@RestController
@RequestMapping("/unicode/unicodesupplyship")
public class UnicodeSupplyShipController extends AbstractController {
    @Autowired
    private UnicodeSupplyShipService unicodeSupplyShipService;

    /**
     * 列表分页查询
     */
    @PostMapping("/list")
    //@RequiresPermissions("unicode:unicodesupplyship:list")
    public Hdi list(@RequestBody Map<String, Object> params){
        PageUtils page = unicodeSupplyShipService.queryPage(params);

        return Hdi.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{shipId}")
    //@RequiresPermissions("unicode:unicodesupplyship:info")
    public Hdi info(@PathVariable("shipId") Long shipId){
        UnicodeSupplyShipEntity unicodeSupplyShip = unicodeSupplyShipService.selectById(shipId);

        return Hdi.ok().put("unicodeSupplyShip", unicodeSupplyShip);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    //@RequiresPermissions("unicode:unicodesupplyship:save")
    public Hdi save(@RequestBody UnicodeSupplyShipEntity unicodeSupplyShip){
    	ValidatorUtils.validateEntity(unicodeSupplyShip, AddGroup.class);
        unicodeSupplyShip.setCremanid(getUserId());
        unicodeSupplyShipService.save(unicodeSupplyShip);
        return Hdi.ok();
    }

    /**
     * 匹对
     */
    @PostMapping("/update")
    //@RequiresPermissions("unicode:unicodesupplyship:update")
    public Hdi update(@RequestBody UnicodeSupplyShipEntity unicodeSupplyShip){
        ValidatorUtils.validateEntity(unicodeSupplyShip, UpdateGroup.class);
        unicodeSupplyShip.setEditmanid(getUserId());
        unicodeSupplyShipService.update(unicodeSupplyShip);
        return Hdi.ok();
    }
    
    /**
     * 分页查询匹对供应商列表
     */
    @PostMapping("/matching")
    //@RequiresPermissions("unicode:unicodesupplyship:matching")
    public Hdi matching(@RequestBody Map<String, Object> params){
    	PageUtils page = unicodeSupplyShipService.matching(params);
        
        return Hdi.ok().put("page", page);
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    //@RequiresPermissions("unicode:unicodesupplyship:delete")
    public Hdi delete(@RequestBody Long[] ids){
    	unicodeSupplyShipService.deleteBatchIds(Arrays.asList(ids));
        return Hdi.ok();
    }

}
