package com.ebig.hdi.modules.license.service;

import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.license.entity.LicenseHospitalExamineEntity;

/**
 * 证照医院审批
 *
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-05-21 17:26:30
 */
public interface LicenseHospitalExamineService extends IService<LicenseHospitalExamineEntity> {

    PageUtils queryPage(Map<String, Object> params);

	void save(LicenseHospitalExamineEntity licenseHospitalExamine);
}

