package com.ebig.hdi.modules.core.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.utils.Constant;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.core.dao.CoreLabelDetailDao;
import com.ebig.hdi.modules.core.entity.CoreLabelDetailEntity;
import com.ebig.hdi.modules.core.service.CoreLabelDetailService;


@Service("coreLabelDetailService")
public class CoreLabelDetailServiceImpl extends ServiceImpl<CoreLabelDetailDao, CoreLabelDetailEntity> implements CoreLabelDetailService {

    @Override
    public PageUtils queryPageDetail(Map<String, Object> params) {
		int currPage = Integer.parseInt(params.get("page").toString());
		int pageSize = Integer.parseInt(params.get("limit").toString());
		// 手动数据过滤
		if (params.get(Constant.SQL_FILTER) != null) {
			String sqlFilter = params.get(Constant.SQL_FILTER).toString();
			List<String> deptIds = Arrays
					.asList(StringUtils.substringBetween(sqlFilter, "(dept_id in(", "))").split(","));
			params.put("deptIds", deptIds);
		}
	
		Page<CoreLabelDetailEntity> page = new Page<CoreLabelDetailEntity>(currPage, pageSize);
		List<CoreLabelDetailEntity> list = this.baseMapper.selectByLabelId(page, params);
		page.setRecords(list);
		return new PageUtils(page);
    }

}
