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

package com.ebig.hdi.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.sys.entity.SysDictEntity;

import java.util.List;
import java.util.Map;

/**
 * 数据字典
 *
 * @author Mark sunlightcs@gmail.com
 * @since 3.1.0 2018-01-27
 */
public interface SysDictService extends IService<SysDictEntity> {

    PageUtils queryPage(Map<String, Object> params);

	List<SysDictEntity> selectDictByType(String type);

	List<SysDictEntity> selectDictByTypeAndParentCode(String type, String parentCode);
	
	SysDictEntity selectDictByCode(String type, String code);

	SysDictEntity selectDictByCodeAndParentCode(String value, String parentCode) ;

	/**
	 * 检查商品单位名称是否存在，不存在生成一个并返回。
	 * @param goodsUnitName
	 * @return
     */
	SysDictEntity checkGoodsUnit(String goodsUnitName);

}

