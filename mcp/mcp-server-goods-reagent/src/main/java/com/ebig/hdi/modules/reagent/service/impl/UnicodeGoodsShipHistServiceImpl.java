package com.ebig.hdi.modules.reagent.service.impl;

import java.util.List;
import java.util.Map;

import com.ebig.hdi.modules.reagent.entity.UnicodeGoodsShipHistReagentEntityVo;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.utils.Constant;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.reagent.dao.UnicodeGoodsShipHistReagentDao;
import com.ebig.hdi.modules.reagent.entity.UnicodeGoodsShipHistReagentEntity;
import com.ebig.hdi.modules.reagent.service.UnicodeGoodsShipHistService;


@Service("unicodeGoodsShipHistReagentService")
public class UnicodeGoodsShipHistServiceImpl extends ServiceImpl<UnicodeGoodsShipHistReagentDao, UnicodeGoodsShipHistReagentEntity> implements UnicodeGoodsShipHistService {

    @Override
    @DataFilter(subDept = true, user = false, tableAlias = "ugsh")
    public PageUtils queryPageHospital(Map<String, Object> params) {
    	int currPage = Integer.parseInt(params.get("page").toString());
		int pageSize = Integer.parseInt(params.get("limit").toString());
		// 手动数据过滤
		if (params.get(Constant.SQL_FILTER) != null) {
			String sqlFilter = params.get(Constant.SQL_FILTER).toString();
			params.put("deptIds", sqlFilter);
		}

		Page<UnicodeGoodsShipHistReagentEntityVo > page = new Page<UnicodeGoodsShipHistReagentEntityVo >(currPage, pageSize);
		List<UnicodeGoodsShipHistReagentEntityVo > list = this.baseMapper.selectHospitalGoodsHist(page, params);
		page.setRecords(list);
		return new PageUtils(page);
    }
    
    
    @Override
    @DataFilter(subDept = true, user = false, tableAlias = "ugsh")
    public PageUtils queryPageSupplier(Map<String, Object> params) {
    	int currPage = Integer.parseInt(params.get("page").toString());
		int pageSize = Integer.parseInt(params.get("limit").toString());
		// 手动数据过滤
		if (params.get(Constant.SQL_FILTER) != null) {
			String sqlFilter = params.get(Constant.SQL_FILTER).toString();
			params.put("deptIds", sqlFilter);
		}

		Page<UnicodeGoodsShipHistReagentEntityVo > page = new Page<UnicodeGoodsShipHistReagentEntityVo >(currPage, pageSize);
		List<UnicodeGoodsShipHistReagentEntityVo > list = this.baseMapper.selectSupplierGoodsHist(page, params);
		page.setRecords(list);
		return new PageUtils(page);
    }

}
