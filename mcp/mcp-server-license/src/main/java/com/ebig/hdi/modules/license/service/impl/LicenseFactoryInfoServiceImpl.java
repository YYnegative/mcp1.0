package com.ebig.hdi.modules.license.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.enums.DelFlagEnum;
import com.ebig.hdi.common.enums.FactoryStatusEnum;
import com.ebig.hdi.common.enums.SequenceEnum;
import com.ebig.hdi.common.enums.StatusEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.job.entity.ScheduleJobEntity;
import com.ebig.hdi.modules.license.dao.LicenseFactoryInfoDao;
import com.ebig.hdi.modules.license.entity.LicenseFactoryInfoEntity;
import com.ebig.hdi.modules.license.service.LicenseFactoryInfoService;
import com.ebig.hdi.modules.license.vo.LicenseFactoryInfoEntityVo;
import com.ebig.hdi.modules.sys.service.SysSequenceService;
import com.ebig.hdi.modules.sys.service.SysUserService;


@Service("licenseFactoryInfoService")
public class LicenseFactoryInfoServiceImpl extends ServiceImpl<LicenseFactoryInfoDao, LicenseFactoryInfoEntity> implements LicenseFactoryInfoService {

	@Autowired
	private SysSequenceService sysSequenceService;
	
	@Autowired
	private SysUserService sysUserService;
	
    @Override
    @DataFilter(subDept = true, user = false)
    public PageUtils queryPage(Map<String, Object> params) {
		int currPage = Integer.parseInt(params.get("page").toString());
		int pageSize = Integer.parseInt(params.get("limit").toString());
		
		Page<LicenseFactoryInfoEntityVo> page = new Page<LicenseFactoryInfoEntityVo>(currPage, pageSize);
		
		List<LicenseFactoryInfoEntityVo> list = this.baseMapper.selectLicenseFactoryInfoList(page, params);
				
		page.setRecords(list);

        return new PageUtils(page);
    }

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(LicenseFactoryInfoEntityVo licenseFactoryInfo) {
		if(licenseFactoryInfo.getCreateId() == null){
			throw new HdiException("创建人id不能为空");
		}
		if(licenseFactoryInfo.getDeptId() == null){
			throw new HdiException("机构id不能为空");
		}
		if(licenseFactoryInfo.getBeginTime().after(licenseFactoryInfo.getEndTime())) {
			throw new HdiException("请选择正确的证照效期！");
		}
		
		if(StringUtil.isEmpty(licenseFactoryInfo.getFactoryId())) {
			saveFactoryDraft(licenseFactoryInfo);
			
		}
		
		licenseFactoryInfo.setStatus(StatusEnum.USABLE.getKey());
		licenseFactoryInfo.setDelFlag(DelFlagEnum.NORMAL.getKey());
		licenseFactoryInfo.setCreateTime(new Date());
		this.baseMapper.insert(licenseFactoryInfo);
		
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(LicenseFactoryInfoEntityVo licenseFactoryInfo) {
		if(licenseFactoryInfo.getEditId() == null){
			throw new HdiException("修改人id不能为空");
		}
		if(licenseFactoryInfo.getBeginTime().after(licenseFactoryInfo.getEndTime())) {
			throw new HdiException("请选择正确的证照效期！");
		}

		if(StringUtil.isEmpty(licenseFactoryInfo.getFactoryId())) {
			//厂商信息不存在，生成一条待审批状态的记录
			Map<String, Object> factory = new HashMap<String, Object>();
			String factoryCode = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.FACTORY_CODE.getKey());
			factory.put("factoryCode", factoryCode);
			factory.put("factoryName", licenseFactoryInfo.getFactoryName());
			factory.put("status", FactoryStatusEnum.DRAFT.getKey());
			factory.put("delFlag", DelFlagEnum.NORMAL.getKey());
			factory.put("createId", licenseFactoryInfo.getEditId());
			factory.put("deptId", sysUserService.selectById(licenseFactoryInfo.getEditId()).getDeptId());
			factory.put("createTime", new Date());
			this.baseMapper.saveFactory(factory);
			
			licenseFactoryInfo.setFactoryId(Long.valueOf(factory.get("id").toString()));
			
		}
		
		licenseFactoryInfo.setEditTime(new Date());
		
		this.baseMapper.updateById(licenseFactoryInfo);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long[] ids) {
		for(Long id : ids) {
			LicenseFactoryInfoEntity licenseFactoryInfoEntity = new LicenseFactoryInfoEntity();
			licenseFactoryInfoEntity.setId(id);
			licenseFactoryInfoEntity.setDelFlag(DelFlagEnum.DELETE.getKey());
			this.baseMapper.updateById(licenseFactoryInfoEntity);
		}
		
	}

	@Override
	public LicenseFactoryInfoEntity selectLicenseFactoryInfoById(Long id) {
		return this.baseMapper.selectLicenseFactoryInfoById(id);
	}

	@Override
	public List<LicenseFactoryInfoEntity> selectRelativesById(Long id) {
		LicenseFactoryInfoEntity licenseFactoryInfo = this.baseMapper.selectLicenseFactoryInfoById(id);
		List<LicenseFactoryInfoEntity> newLicenseList = this.selectNewLicense(licenseFactoryInfo, new ArrayList<LicenseFactoryInfoEntity>());
		List<LicenseFactoryInfoEntity> oldLicenseList = this.selectOldLicense(licenseFactoryInfo, new ArrayList<LicenseFactoryInfoEntity>());
		//证照列表按新旧倒序排列
		Collections.reverse(oldLicenseList);
		newLicenseList.addAll(oldLicenseList);
		return newLicenseList;
	}

	/**
	 * 递归获取新证
	 * @param license 当前证照对象
	 * @param licenseList 证照列表
	 * @return 新证列表
	 */
	private List<LicenseFactoryInfoEntity> selectNewLicense(LicenseFactoryInfoEntity license, List<LicenseFactoryInfoEntity> licenseList){
		if(license.getNewLicenseId() != null) {
			LicenseFactoryInfoEntity newLicense = this.baseMapper.selectLicenseFactoryInfoById(license.getNewLicenseId());
			selectNewLicense(newLicense, licenseList);
			licenseList.add(newLicense);
		}
		return licenseList;

	}
	
	/**
	 * 递归获取旧证
	 * @param license 当前证照对象
	 * @param licenseList 证照列表
	 * @return 旧证列表
	 */
	private List<LicenseFactoryInfoEntity> selectOldLicense(LicenseFactoryInfoEntity license, List<LicenseFactoryInfoEntity> licenseList){
		LicenseFactoryInfoEntity oldLicense = this.baseMapper.selectLicenseFactoryInfoByNewLicenseId(license.getId());
		if (oldLicense != null) {
			selectOldLicense(oldLicense, licenseList);
			licenseList.add(oldLicense);
		}
		return licenseList;
		
	}

	@Override
	public void replace(LicenseFactoryInfoEntityVo licenseFactoryInfo) {
		if(licenseFactoryInfo.getCreateId() == null){
			throw new HdiException("创建人id不能为空");
		}
		if(licenseFactoryInfo.getDeptId() == null){
			throw new HdiException("机构id不能为空");
		}
		if(licenseFactoryInfo.getBeginTime().after(licenseFactoryInfo.getEndTime())) {
			throw new HdiException("请选择正确的证照效期！");
		}
		//保存新证
		Long oldLicenseId = licenseFactoryInfo.getId();
		licenseFactoryInfo.setId(null);
		
		if(StringUtil.isEmpty(licenseFactoryInfo.getFactoryId())) {
			//厂商信息不存在，生成一条待审批状态的记录
			saveFactoryDraft(licenseFactoryInfo);
		}
		
		licenseFactoryInfo.setStatus(StatusEnum.USABLE.getKey());
		licenseFactoryInfo.setDelFlag(DelFlagEnum.NORMAL.getKey());
		licenseFactoryInfo.setCreateTime(new Date());
		this.baseMapper.insert(licenseFactoryInfo);
		
		//旧证关联新证
		LicenseFactoryInfoEntity licenseFactoryInfoEntity = new LicenseFactoryInfoEntity();
		licenseFactoryInfoEntity.setId(oldLicenseId);
		licenseFactoryInfoEntity.setNewLicenseId(licenseFactoryInfo.getId());
		this.baseMapper.updateById(licenseFactoryInfoEntity);
		
	}

	private void saveFactoryDraft(LicenseFactoryInfoEntityVo licenseFactoryInfo) {
		Map<String, Object> factory = new HashMap<String, Object>();
		String factoryCode = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.FACTORY_CODE.getKey());
		factory.put("factoryCode", factoryCode);
		factory.put("factoryName", licenseFactoryInfo.getFactoryName());
		factory.put("status", FactoryStatusEnum.DRAFT.getKey());
		factory.put("delFlag", DelFlagEnum.NORMAL.getKey());
		factory.put("createId", licenseFactoryInfo.getCreateId());
		factory.put("deptId", licenseFactoryInfo.getDeptId());
		factory.put("createTime", new Date());
		this.baseMapper.saveFactory(factory);
		
		licenseFactoryInfo.setFactoryId(Long.valueOf(factory.get("id").toString()));
	}

	@Override
	public List<LicenseFactoryInfoEntity> selectBySupplierIdAndTime(Long supplierId, ScheduleJobEntity scheduleJob) {
		return baseMapper.selectBySupplierIdAndTime(supplierId, scheduleJob);
	}
}
