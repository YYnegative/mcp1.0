package com.ebig.hdi.modules.core.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.entity.UnicodeGoodsShipHistEntity;
import com.ebig.hdi.common.entity.UnicodeGoodsShipHistEntityVo;
import com.ebig.hdi.common.utils.Constant;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.core.dao.UnicodeGoodsShipHistDao;
import com.ebig.hdi.modules.core.service.UnicodeGoodsShipHistService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("unicodeGoodsShipHistService")
public class UnicodeGoodsShipHistServiceImpl extends ServiceImpl<UnicodeGoodsShipHistDao, UnicodeGoodsShipHistEntity> implements UnicodeGoodsShipHistService {

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

		Page<UnicodeGoodsShipHistEntityVo> page = new Page<UnicodeGoodsShipHistEntityVo>(currPage, pageSize);
		List<UnicodeGoodsShipHistEntityVo> list = this.baseMapper.selectHospitalGoodsHist(page, params);
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

		Page<UnicodeGoodsShipHistEntityVo > page = new Page<UnicodeGoodsShipHistEntityVo>(currPage, pageSize);
		List<UnicodeGoodsShipHistEntityVo > list = this.baseMapper.selectSupplierGoodsHist(page, params);
		page.setRecords(list);
		return new PageUtils(page);
    }

	@Override
	@DataFilter(subDept = true, user = false, tableAlias = "ugsh")
	public PageUtils queryPage(Map<String, Object> params) {
		int currPage = Integer.parseInt(params.get("page").toString());
		int pageSize = Integer.parseInt(params.get("limit").toString());
		// 手动数据过滤
		if (params.get(Constant.SQL_FILTER) != null) {
			String sqlFilter = params.get(Constant.SQL_FILTER).toString();
			params.put("deptIds", sqlFilter);
		}
		Page<UnicodeGoodsShipHistEntityVo > page = new Page<UnicodeGoodsShipHistEntityVo>(currPage, pageSize);
		List<UnicodeGoodsShipHistEntityVo > list = this.baseMapper.selectGoodsHist(page, params);
		page.setRecords(list);
		return new PageUtils(page);
	}

}
