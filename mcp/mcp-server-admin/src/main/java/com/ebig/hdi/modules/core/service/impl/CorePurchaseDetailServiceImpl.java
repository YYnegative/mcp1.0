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
import com.ebig.hdi.modules.core.dao.CorePurchaseDetailDao;
import com.ebig.hdi.modules.core.entity.CorePurchaseDetailEntity;
import com.ebig.hdi.modules.core.service.CorePurchaseDetailService;


@Service("corePurchaseDetailService")
public class CorePurchaseDetailServiceImpl extends ServiceImpl<CorePurchaseDetailDao, CorePurchaseDetailEntity> implements CorePurchaseDetailService {

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
	
		Page<CorePurchaseDetailEntity> page = new Page<CorePurchaseDetailEntity>(currPage, pageSize);
		List<CorePurchaseDetailEntity> list = this.baseMapper.selectByPurchaseMasterId(page, params);
		page.setRecords(list);
		return new PageUtils(page);
	}
    
    
	public List<CorePurchaseDetailEntity> queryDetail(Long purchaseMasterId){
		return baseMapper.selectByMasterid(purchaseMasterId);
	}
	
	public List<CorePurchaseDetailEntity> selectSupplierGoods(CorePurchaseDetailEntity entity){
		return baseMapper.selectSupplierGoods(entity.getHgoodsid(),entity.getGoodsclass(),entity.getHgoodstype());
	}
    
/*	public List<CorePurchaseDetailEntity> selectGoodsName(Long purchaseMasterId){
		List<CorePurchaseDetailEntity> list = baseMapper.selectGoodsName(purchaseMasterId);
		return list;
	}*/
	
/*	public List<CorePurchaseDetailEntity> selectHgoodsName(Long purchaseMasterId){
		List<CorePurchaseDetailEntity> list = baseMapper.selectHgoodsName(purchaseMasterId);
		return list;
	}*/
	
	@Override
	public List<CorePurchaseDetailEntity> queryDetails(Long purchaseMasterId, String purplanno, Long supplierId){
		return baseMapper.queryDetails(purchaseMasterId, purplanno, supplierId);
	}
	
	
	public Map<String, Object> selectYHospital(Long hgoodsid,Long hgoodstypeid,Integer goodsclass){
		return baseMapper.selectYHospital(goodsclass,hgoodsid,hgoodstypeid);
	}
	
	public List<Map<String, Object>> selectShipFlag(Long purchaseMasterId){
		return baseMapper.selectShipFlag(purchaseMasterId);
	}
	
	

	//HDI转化用   增加采购细单
	@Override
	public void saveCorePurchaseDetail(CorePurchaseDetailEntity entity) {
		this.baseMapper.insert(entity);
	}


	@Override
	public Integer selectDetailLeaveSupplyQty(Long purchaseDetailId) {
		return this.baseMapper.selectDetailLeaveSupplyQty(purchaseDetailId);
	}


	public Integer getPurchaseNumberByPurchaseMasterId(Long purchaseMasterId) {
		return this.baseMapper.getPurchaseNumberByPurchaseMasterId(purchaseMasterId);
	}

	public Map<String,Object> selectViewByGoodsNameAndSpecs(Long hospitalId,String hgoodsname, String hgoodstype,String factoryName) {
		return this.baseMapper.selectViewByGoodsNameAndSpecs( hospitalId,hgoodsname,hgoodstype,factoryName);
	}
}
