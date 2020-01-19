/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ebig.hdi.modules.sys.controller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.modules.sys.entity.SysDictEntity;
import com.ebig.hdi.modules.sys.service.SysDictService;

/**
 * 数据字典
 *
 * @author Mark sunlightcs@gmail.com
 * @since 3.1.0 2018-01-27
 */
@RestController
@RequestMapping("sys/dict")
public class SysDictController {
	@Autowired
    private SysDictService sysDictService;
	
    /**
     * 函数功能说明 ：查询枚举类型字典 <br/>
     * 修改者名字： <br/>
     * 修改日期： <br/>
     * 修改内容：<br/>
     * 作者：roncoo-lrx <br/>
     * 参数：@param type
     * 参数：@return <br/>
     * return：Hdi <br/>
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/getEnumType/{type}")
    public Hdi getEnumType(@PathVariable("type") String type) {
        try {
            String [] signs = type.split(",");
            if(signs.length<1){
            	return Hdi.error("请传入正确的参数");
            }
            Map<String, Object> map = new HashMap<String, Object>();
            for(String str : signs){
                List<LinkedHashMap<String, Object>> list = new ArrayList<LinkedHashMap<String, Object>>();
                Class clazz = Class.forName("com.ebig.hdi.common.enums."+str);
                Method toKey = clazz.getMethod("getKey");
                Method toName = clazz.getMethod("getValue");
                //得到enum的所有实例
                Object[] objs = clazz.getEnumConstants();
                for (Object obj : objs) {
                    LinkedHashMap<String, Object> subMap = new LinkedHashMap<String, Object>();
                    subMap.put("key", toKey.invoke(obj));
                    subMap.put("value", toName.invoke(obj));
                    list.add(subMap);
                }
                map.put(str, list);
            }
            return Hdi.ok().put("list",map);
        } catch (Exception e) {
            throw new HdiException("字典查询出错" + e.getMessage());
        }
    }
	
	/**
	 * 根据类型获取字典
	 */
	@RequestMapping("/{type}")
    public Hdi selectDictByType(@PathVariable("type") String type){
        List<SysDictEntity> dictList = sysDictService.selectDictByType(type);
        return Hdi.ok().put("dictList", dictList);
    }
	
	/**
	 * 根据类型和父编码获取字典
	 */
	@RequestMapping("/{type}/{parentCode}")
    public Hdi selectDictByTypeAndParentCode(@PathVariable("type") String type, @PathVariable("parentCode") String parentCode){
        List<SysDictEntity> dictList = sysDictService.selectDictByTypeAndParentCode(type, parentCode);
        return Hdi.ok().put("dictList", dictList);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:dict:list")
    public Hdi list(@RequestParam Map<String, Object> params){
        PageUtils page = sysDictService.queryPage(params);

        return Hdi.ok().put("page", page);
    }


    /**
     * 根据字典码获取字典信息
     */
    @RequestMapping("/info/{type}/{code}")
    @RequiresPermissions("sys:dict:info")
    public Hdi info(@PathVariable("type") String type, @PathVariable("code") String code){
        SysDictEntity dict = sysDictService.selectDictByCode(type, code);

        return Hdi.ok().put("dict", dict);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:dict:save")
    public Hdi save(@RequestBody SysDictEntity dict){
        //校验类型
        ValidatorUtils.validateEntity(dict);

        sysDictService.insert(dict);

        return Hdi.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:dict:update")
    public Hdi update(@RequestBody SysDictEntity dict){
        //校验类型
        ValidatorUtils.validateEntity(dict);

        sysDictService.updateById(dict);

        return Hdi.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:dict:delete")
    public Hdi delete(@RequestBody Long[] ids){
        sysDictService.deleteBatchIds(Arrays.asList(ids));

        return Hdi.ok();
    }
}
