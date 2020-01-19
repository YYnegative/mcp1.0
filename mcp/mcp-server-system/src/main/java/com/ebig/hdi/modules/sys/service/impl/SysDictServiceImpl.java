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

package com.ebig.hdi.modules.sys.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.Query;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.sys.dao.SysDictDao;
import com.ebig.hdi.modules.sys.entity.SysDictEntity;
import com.ebig.hdi.modules.sys.service.SysDictService;
import com.ebig.hdi.modules.sys.service.SysSequenceService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("sysDictService")
public class SysDictServiceImpl extends ServiceImpl<SysDictDao, SysDictEntity> implements SysDictService {

	@Autowired
	private SysDictDao sysDictDao;
	@Autowired
	private SysSequenceService sysSequenceService;

	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String name = (String)params.get("name");
        String value = (String)params.get("value");

        Page<SysDictEntity> page = this.selectPage(
                new Query<SysDictEntity>(params).getPage(),
                new EntityWrapper<SysDictEntity>()
                    .like(StringUtils.isNotBlank(name),"name", name)
                    .like(StringUtils.isNotBlank(value),"value", value)
        );

        return new PageUtils(page);
    }

	@Override
	public List<SysDictEntity> selectDictByType(String type) {
		return sysDictDao.selectDictByType(StringUtil.getInParam(type));
	}

	@Override
	public List<SysDictEntity> selectDictByTypeAndParentCode(String type, String parentCode) {
		return sysDictDao.selectDictByTypeAndParentCode(type, parentCode);
	}

	@Override
	public SysDictEntity selectDictByCode(String type, String code) {
		return sysDictDao.selectDictByCode(type, code);
	}

	@Override
	public  SysDictEntity selectDictByCodeAndParentCode(String value, String parentCode) {
		return sysDictDao.selectDictByCodeAndParentCode(value, parentCode);
	}

	@Override
	public SysDictEntity checkGoodsUnit(String goodsUnitName) {
		if (!StringUtil.isEmpty(goodsUnitName)) {
			List<SysDictEntity> dicts = this.selectList(new EntityWrapper<SysDictEntity>().eq("value", goodsUnitName));
			if (StringUtil.isEmpty(dicts)) {
				SysDictEntity dict = new SysDictEntity();
				dict.setName("商品单位");
				dict.setType("goods_unit");
				dict.setCode(sysSequenceService.selectSeqValueBySeqCode("GOODS_UNIT_CODE"));
				dict.setValue(goodsUnitName);
				this.insert(dict);
				return dict;
			} else {
				return dicts.get(0);
			}
		}
		return null;
	}

}
