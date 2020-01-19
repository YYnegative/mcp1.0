package com.ebig.hdi.modules.reagent.common;

import com.ebig.hdi.modules.consumables.entity.UnicodeConsumablesCateEntity;
import com.ebig.hdi.modules.consumables.service.UnicodeConsumablesCateService;
import com.ebig.hdi.modules.reagent.entity.UnicodeReagentCateEntity;
import com.ebig.hdi.modules.reagent.service.UnicodeReagentCateService;
import com.ebig.hdi.modules.sys.entity.SysDictEntity;
import com.ebig.hdi.modules.sys.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: mcp
 * @description: 商品分类转换
 * @email 791203472@qq.com
 * @author: ZhengHaiWen
 * @create: 2019-11-27 18:26
 **/

@Component
public class UnicodeGoodsUnitCode {
    private static String GOODSUNIT = "goods_unit",STOREWAY = "store_way";
    private  static  List<SysDictEntity> goodsUnits,storeWayList;
    private  static  List<UnicodeReagentCateEntity> unicodeConsumablesCateMapList;
    private static UnicodeReagentCateService unicodeReagentCateService;
    private static Map<Long,String> UnicodeReagentMap;
    private static Map<String,String> goodsUnit,storeWayMap;
    private static Map<String,Long> UnicodeReagent;

    private static SysDictService sysDictService;
    @Autowired
    public void setUnicodeConsumablesCateService(UnicodeReagentCateService unicodeReagentCateService){
        UnicodeGoodsUnitCode.unicodeReagentCateService = unicodeReagentCateService;
    }
    @Autowired
    public void setSysDictService(SysDictService sysDictService){
        UnicodeGoodsUnitCode.sysDictService = sysDictService;
    }
    private  UnicodeGoodsUnitCode(){}
    @PostConstruct
    public void init(){
        getUnicodeConsumablesCate();
        getgoodsUnit();
        getStoreWayMap();
    }
    private  static  void getUnicodeConsumablesCate(){
        unicodeConsumablesCateMapList=unicodeReagentCateService.selectAll();
        UnicodeReagentMap = unicodeConsumablesCateMapList.stream().collect(Collectors.toMap(UnicodeReagentCateEntity::getCateId, UnicodeReagentCateEntity::getCateName, (key1, key2) -> key2, HashMap::new));
        UnicodeReagent = unicodeConsumablesCateMapList.stream().collect(Collectors.toMap(UnicodeReagentCateEntity::getCateName, UnicodeReagentCateEntity::getCateId, (key1, key2) -> key2, HashMap::new));
    }

    private  static  void getgoodsUnit(){
        goodsUnits = sysDictService.selectDictByType(GOODSUNIT);
        goodsUnit = goodsUnits.stream().collect(Collectors.toMap(SysDictEntity::getValue, SysDictEntity::getCode, (key1, key2) -> key2, HashMap::new));
    }
    private  static void getStoreWayMap() {
        storeWayList = sysDictService.selectDictByType(STOREWAY);
        storeWayMap = storeWayList.stream().collect(Collectors.toMap(SysDictEntity::getValue, SysDictEntity::getCode, (key1, key2) -> key2, HashMap::new));
    }

    public static String getUnicodeReagentMap(Long cateId){
        if (UnicodeReagentMap.containsKey(cateId)){
            return UnicodeReagentMap.get(cateId);
        }else {
            getUnicodeConsumablesCate();
            return UnicodeReagentMap.get(cateId);
        }
    }
    public static  String getGoodsUnitCode(String id){
        if (goodsUnit.containsKey(id)){
            return  goodsUnit.get(id);
        }else {
            getgoodsUnit();
            return goodsUnit.get(id);
        }
    }
    public static Long getUnicodeReagentId(String name){
        if (UnicodeReagent.containsKey(name)){
            return UnicodeReagent.get(name);
        }else {
            getUnicodeConsumablesCate();
            return UnicodeReagent.get(name);
        }
    }

    public static String getStoreWay(String value){
        if (storeWayMap.containsKey(value)){
            return storeWayMap.get(value);
        }else {
            getStoreWayMap();
            return storeWayMap.get(value);
        }
    }
}
