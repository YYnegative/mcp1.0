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
import com.ebig.hdi.modules.core.dao.CoreAcceptDetailDao;
import com.ebig.hdi.modules.core.entity.CoreAcceptDetailEntity;
import com.ebig.hdi.modules.core.service.CoreAcceptDetailService;


@Service("coreAcceptDetailService")
public class CoreAcceptDetailServiceImpl extends ServiceImpl<CoreAcceptDetailDao, CoreAcceptDetailEntity> implements CoreAcceptDetailService {

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
	
		Page<CoreAcceptDetailEntity> page = new Page<CoreAcceptDetailEntity>(currPage, pageSize);
		List<CoreAcceptDetailEntity> list = this.baseMapper.selectByAcceptMasterId(page, params);
		page.setRecords(list);
		return new PageUtils(page);
    }
    
    
    public List<CoreAcceptDetailEntity> selectAcceptDetail(Integer goodsclass,Long goodsid,Long goodstypeid,Long hospitalId,Long supplierId) {
    	List<CoreAcceptDetailEntity> acceptDetail = this.baseMapper.selectAcceptDetail(goodsclass, goodstypeid, goodsid,hospitalId,supplierId);
    	
    	return acceptDetail;
    }

    public List<CoreAcceptDetailEntity> selectAcceptDetails(Integer goodsclass,Long goodsid,Long goodstypeid,Long hospitalId,Long supplierId) {
    	List<CoreAcceptDetailEntity> acceptDetail = this.baseMapper.selectAcceptDetails(goodsclass, goodstypeid, goodsid,hospitalId,supplierId);
    	
    	return acceptDetail;
    }

    public CoreAcceptDetailEntity selectHgoodsInfo(Integer goodsclass,Long goodstypeid,Long goodsid,Long hospitalId,Long supplierId){
    	return this.baseMapper.selectHgoodsInfo(goodsclass,goodstypeid,goodsid,hospitalId,supplierId);
	}

	@Override
	public Integer getAcceptNumberBySupplyMasterId(Long supplyMasterId) {
		return this.baseMapper.getAcceptNumberBySupplyMasterId(supplyMasterId);
	}
}
