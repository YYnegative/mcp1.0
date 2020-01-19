package com.ebig.hdi.modules.core.common;

import com.ebig.hdi.modules.sys.entity.SysDictEntity;
import com.ebig.hdi.modules.sys.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: mcp
 * @description: 区域工具类类
 * @email 791203472@qq.com
 * @author: ZhengHaiWen
 * @create: 2019-11-18 17:17
 **/

@Component(value = "AddressUtil_")
public  class AddressUtil {
    private static SysDictService sysDictService;
    private  static  HashMap<String,String> areaMap,cityMap,provinceMap,provinceValueMap;
    @Autowired
    public void setSysDictService(SysDictService sysDictService){
        AddressUtil.sysDictService = sysDictService;
    }
    @PostConstruct
    public void init(){
        List<SysDictEntity> areaList = sysDictService.selectDictByType("area");
        areaMap = areaList.stream().collect(Collectors.toMap(SysDictEntity::getCode, SysDictEntity::getValue, (key1, key2) -> key2, HashMap::new));
        List<SysDictEntity>   cityList = sysDictService.selectDictByType("city");
        cityMap = cityList.stream().collect(Collectors.toMap(SysDictEntity::getCode, SysDictEntity::getValue, (key1, key2) -> key2, HashMap::new));
        List<SysDictEntity>  provinceList = sysDictService.selectDictByType("province");
        provinceMap = provinceList.stream().collect(Collectors.toMap(SysDictEntity::getCode, SysDictEntity::getValue, (key1, key2) -> key2, HashMap::new));
        provinceValueMap = provinceList.stream().collect(Collectors.toMap(SysDictEntity::getValue, SysDictEntity::getCode, (key1, key2) -> key2, HashMap::new));
    }
    private AddressUtil(){ }
    public  static HashMap<String,String> getAreaMap(){
        return areaMap;
    }
    public  static HashMap<String,String> getCityMap(){ return cityMap; }
    public  static HashMap<String,String> getProvinceMap(){
        return  provinceMap;
    }
    public  static HashMap<String,String> getProvinceValueMap(){
        return  provinceValueMap;
    }

}
