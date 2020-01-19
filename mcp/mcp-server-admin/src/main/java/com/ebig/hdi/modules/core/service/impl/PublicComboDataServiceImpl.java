package com.ebig.hdi.modules.core.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebig.hdi.modules.core.dao.PublicComboDataDao;
import com.ebig.hdi.modules.core.entity.CoreLotEntity;
import com.ebig.hdi.modules.core.entity.CoreStorehouseEntity;
import com.ebig.hdi.modules.core.service.PublicComboDataService;
import com.ebig.hdi.modules.org.entity.OrgHospitalInfoEntity;

@Service("publicComboDataService")
public class PublicComboDataServiceImpl implements PublicComboDataService {

	@Autowired
	private PublicComboDataDao publicComboDataDao;
	
	@Override
	public List<OrgHospitalInfoEntity> getHospitalInfo(String value, Long deptId) {
		return publicComboDataDao.queryHospitalData(value, deptId);
	}

	@Override
	public List<CoreLotEntity> getLotInfo(Map<String, Object> params) {
		return publicComboDataDao.queryLotData(params);
	}

	@Override
	public List<CoreStorehouseEntity> getStorehouseInfo(Map<String, Object> params) {
		return publicComboDataDao.queryStorehouseData(params);
	}
	
	@Override
	public List<Map<String, Object>> queryHospitalGoodsInfo(Map<String, Object> params) {
		return publicComboDataDao.queryHospitalGoodsData(params);
	}

	@Override
	public List<Map<String, Object>> querySupplierGoodsInfo(Map<String, Object> params) {
		return publicComboDataDao.querySupplierGoodsData(params);
	}

	@Override
	public List<Map<String, Object>> querySupplierGoodsInfo(Long deptId, String goodstypeno) {
		return publicComboDataDao.querySupplierGoodsInfo(deptId,goodstypeno);
	}
}
