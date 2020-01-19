package com.ebig.hdi.modules.license.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.enums.StatusEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.modules.license.dao.LicenseClassifyInfoDao;
import com.ebig.hdi.modules.license.entity.LicenseClassifyInfoEntity;
import com.ebig.hdi.modules.license.service.LicenseClassifyInfoService;


@Service("licenseClassifyInfoService")
public class LicenseClassifyInfoServiceImpl extends ServiceImpl<LicenseClassifyInfoDao, LicenseClassifyInfoEntity> implements LicenseClassifyInfoService {

	@Override
	public List<LicenseClassifyInfoEntity> list(Map<String, Object> params) {
		Integer type = (Integer)params.get("type");
		String name = (String)params.get("name");
		
		EntityWrapper<LicenseClassifyInfoEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper
			.eq(type != null, "type", type)
			.like(StringUtils.isNotBlank(name), "name", name)
			.orderBy("type", true);
		return baseMapper.selectList(entityWrapper);
	}

	@Override
	public void save(LicenseClassifyInfoEntity licenseClassifyInfo) {
		if(licenseClassifyInfo == null){
			throw new HdiException("证照分类信息不能为空");
		}
		if(licenseClassifyInfo.getCreateId() == null){
			throw new HdiException("创建人id不能为空");
		}
		if(licenseClassifyInfo.getDeptId() == null){
			throw new HdiException("机构id不能为空");
		}
		licenseClassifyInfo.setCreateTime(new Date());
		licenseClassifyInfo.setStatus(StatusEnum.USABLE.getKey());
		if(licenseClassifyInfo.getIsWarning() == 0){
			licenseClassifyInfo.setEarlyDate(0);
		}
		baseMapper.insert(licenseClassifyInfo);
		
	}

	@Override
	public void delete(Long[] ids) {
		for (Long id : ids) {
			if (this.baseMapper.selectReferNumber(id) > 0) {
				throw new HdiException("证照分类已被引用，无法删除！");
			}
		}
		
		this.baseMapper.deleteBatchIds(Arrays.asList(ids));
		
	}

}
