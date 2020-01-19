package com.ebig.hdi.modules.core.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.utils.Constant;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.core.dao.CoreSupplyDetailDao;
import com.ebig.hdi.modules.core.entity.CoreSupplyDetailEntity;
import com.ebig.hdi.modules.core.service.CoreSupplyDetailService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("coreSupplyDetailService")
public class CoreSupplyDetailServiceImpl extends ServiceImpl<CoreSupplyDetailDao, CoreSupplyDetailEntity> implements CoreSupplyDetailService {

    @Override
	@DataFilter(subDept = true,user=false,tableAlias = "csm")
    public PageUtils queryPageDetail(Map<String, Object> params) {
		int currPage = Integer.parseInt(params.get("page").toString());
		int pageSize = Integer.parseInt(params.get("limit").toString());
		// 手动数据过滤
		if (params.get(Constant.SQL_FILTER) != null) {
			String sqlFilter = params.get(Constant.SQL_FILTER).toString();
//			List<String> deptIds = Arrays
//					.asList(StringUtils.substringBetween(sqlFilter, "(dept_id in(", "))").split(","));
			params.put("deptIds", sqlFilter);
		}
	
		Page<CoreSupplyDetailEntity> page = new Page<CoreSupplyDetailEntity>(currPage, pageSize);
		List<CoreSupplyDetailEntity> list = this.baseMapper.selectBysupplyMasterId(page, params);
		page.setRecords(list);
		return new PageUtils(page);
    }
    
    
    public List<CoreSupplyDetailEntity> selectSupplyDetail(Long supplyMasterId) {
    	
    	List<CoreSupplyDetailEntity> list = this.baseMapper.selectSupplyDetail(supplyMasterId);
    	return list;
    }
    
    

    @Override
    public List<Map<String, Object>> selectLot(Long goodsid, Integer goodsclass, Long goodstypeid, String lotno) {
    	List<Map<String,Object>> selectLot = this.baseMapper.selectLot(goodsid, goodsclass, goodstypeid, lotno);
    	
    	return selectLot;
    }
    
    
    //HDI转换用  生成转换的供货主单
    @Override
    public void saveSupplyDetail(CoreSupplyDetailEntity entity) {
    	this.baseMapper.insert(entity);
    	
    }
    
    
  //提交医院
    @Override
    public List<CoreSupplyDetailEntity> selectBySupplyMasterId(Long supplyMasterId) {
    	List<CoreSupplyDetailEntity> list = this.baseMapper.selectBySupplyMasterId(supplyMasterId);
    	return list;
    }
    
    
    public CoreSupplyDetailEntity selectByDetailids(Long supplyDetailId) {
    	CoreSupplyDetailEntity list = this.baseMapper.selectByDetailids(supplyDetailId);
    	return list;
    }
    
    
    @Override
    public List<CoreSupplyDetailEntity> queryDetailBatchCode(CoreSupplyDetailEntity coreSupplyDetail) {
    	List<CoreSupplyDetailEntity> queryDetailBatchCode = this.baseMapper.queryDetailBatchCode(coreSupplyDetail.getSupplyMasterId(),coreSupplyDetail.getGoodsname());
    	
    	return queryDetailBatchCode;
    }


	@Override
	public Integer getSupplyNumberByPurchaseMasterId(Long purchaseMasterId) {
		return this.baseMapper.getSupplyNumberByPurchaseMasterId(purchaseMasterId);
	}


	@Override
	public Integer getSupplyNumberBySupplyMasterId(Long supplyMasterId) {
		return this.baseMapper.getSupplyNumberBySupplyMasterId(supplyMasterId);
	}

	@Override
	public Integer getDetailLeaveAcceptQty(Long supplyDetailId) {
		return this.baseMapper.getDetailLeaveAcceptQty(supplyDetailId);
	}

	@Override
	public String getHospitalGoodsSpecsCode(Integer goodsclass, Long goodsid, Long goodstypeid, Long horgId,Long supplierId) {
		return this.baseMapper.getHospitalGoodsSpecsCode(goodsclass, goodsid, goodstypeid, horgId,supplierId);
	}
	
	@Override
	public String getSupplierGoodsSpecsCode(Integer goodsclass, Long goodsid, Long goodstypeid) {
		return this.baseMapper.getSupplierGoodsSpecsCode(goodsclass, goodsid, goodstypeid);
	}

	@Override
	public List<CoreSupplyDetailEntity> getList(String supplyno, String goodsname,String goodsType,String lotno) {
		return this.baseMapper.getList(supplyno, goodsname,goodsType,lotno);
	}

	@Override
	public String selectConvert(Integer goodsClass, Long goodsId) {
		return this.baseMapper.selectConvert(goodsClass,goodsId);
	}
}
