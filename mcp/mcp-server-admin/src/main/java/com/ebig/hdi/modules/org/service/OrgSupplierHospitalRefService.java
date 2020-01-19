package com.ebig.hdi.modules.org.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.org.entity.OrgHospitalInfoEntity;
import com.ebig.hdi.modules.org.entity.OrgSupplierHospitalRefEntity;

/**
 * 供应商医院绑定关系
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-04-23 09:27:39
 */
public interface OrgSupplierHospitalRefService extends IService<OrgSupplierHospitalRefEntity> {

    PageUtils queryPage(Map<String, Object> params);

	void binding(Map<String, Object> params);
	
	List<OrgHospitalInfoEntity> queryAllHospital(Long id);

	List<OrgHospitalInfoEntity> queryMatchHospital(Long id);
}

