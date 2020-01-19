package com.ebig.hdi.modules.drugs.common;

import com.ebig.hdi.modules.drugs.entity.UnicodeDrugsCateEntity;
import com.ebig.hdi.modules.drugs.service.UnicodeDrugsCateService;
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
 * 类功能说明：
 * 类修改者：
 * 修改日期：
 * 修改说明：
 * company：广东以大供应链管理有限公司
 *
 * @author： yy
 * @Date： 2019/12/3 0003 上午 9:44
 * @version： V1.0
 */
@Component
public class UnicodeDrugsCate {
    private static String GOODSUNIT = "goods_unit";
    private  static List<SysDictEntity> goodsUnits;
    private  static  List<UnicodeDrugsCateEntity> unicodeDrugsCateEntityList;
    private static UnicodeDrugsCateService unicodeDrugsCateService;
    private static Map<Long,String> UnicodeDrugsMap;
    private static Map<String,String> goodsUnit;

    private static SysDictService sysDictService;
    @Autowired
    public void setUnicodeDrugsCateService(UnicodeDrugsCateService unicodeDrugsCateService){
        UnicodeDrugsCate.unicodeDrugsCateService = unicodeDrugsCateService;
    }
    @Autowired
    public void setSysDictService(SysDictService sysDictService){
        UnicodeDrugsCate.sysDictService = sysDictService;
    }

    @PostConstruct
    public void init(){
        getUnicodeDrugsCate();
        getgoodsUnit();

    }
    private  static  void getUnicodeDrugsCate(){
        unicodeDrugsCateEntityList=unicodeDrugsCateService.selectAll();
        UnicodeDrugsMap = unicodeDrugsCateEntityList.stream().collect(Collectors.toMap(UnicodeDrugsCateEntity::getCateId, UnicodeDrugsCateEntity::getCateName, (key1, key2) -> key2, HashMap::new));
    }

    private  static  void getgoodsUnit(){
        goodsUnits = sysDictService.selectDictByType(GOODSUNIT);
        goodsUnit = goodsUnits.stream().collect(Collectors.toMap(SysDictEntity::getValue, SysDictEntity::getCode, (key1, key2) -> key2, HashMap::new));
    }
    public static String getUnicodeDrugsMap(Long cateId){
        if (UnicodeDrugsMap.containsKey(cateId)){
            return UnicodeDrugsMap.get(cateId);
        }else {
            getUnicodeDrugsCate();
            return UnicodeDrugsMap.get(cateId);
        }
    }
    public static  String getGoodsUnitCode(String name){
        if (goodsUnit.containsKey(name)){
            return  goodsUnit.get(name);
        }else {
            getgoodsUnit();
            return goodsUnit.get(name);
        }
    }
    
}