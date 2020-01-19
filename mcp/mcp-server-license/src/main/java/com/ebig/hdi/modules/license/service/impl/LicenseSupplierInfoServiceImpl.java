package com.ebig.hdi.modules.license.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.enums.ApprovalTypeEnum;
import com.ebig.hdi.common.enums.DelFlagEnum;
import com.ebig.hdi.common.enums.LicenseTypeEnum;
import com.ebig.hdi.common.enums.StatusEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.job.entity.ScheduleJobEntity;
import com.ebig.hdi.modules.license.dao.LicenseSupplierInfoDao;
import com.ebig.hdi.modules.license.entity.LicenseHospitalExamineEntity;
import com.ebig.hdi.modules.license.entity.LicenseSupplierInfoEntity;
import com.ebig.hdi.modules.license.service.LicenseHospitalExamineService;
import com.ebig.hdi.modules.license.service.LicenseSupplierInfoService;
import com.ebig.hdi.modules.license.vo.LicenseSupplierInfoEntityVo;


@Service("licenseSupplierInfoService")
public class LicenseSupplierInfoServiceImpl extends ServiceImpl<LicenseSupplierInfoDao, LicenseSupplierInfoEntity> implements LicenseSupplierInfoService {
	
	@Autowired
	private LicenseHospitalExamineService licenseHospitalExamineService;

    @Override
    @DataFilter(subDept = true, user = false)
    public PageUtils queryPage(Map<String, Object> params) {
		int currPage = Integer.parseInt(params.get("page").toString());
		int pageSize = Integer.parseInt(params.get("limit").toString());
		
		Page<LicenseSupplierInfoEntityVo> page = new Page<LicenseSupplierInfoEntityVo>(currPage, pageSize);
		
		List<LicenseSupplierInfoEntityVo> list = this.baseMapper.selectLicenseSupplierInfoList(page, params);
				
		page.setRecords(list);

        return new PageUtils(page);
    }

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(LicenseSupplierInfoEntity licenseSupplierInfo) {
		if(licenseSupplierInfo.getCreateId() == null){
			throw new HdiException("创建人id不能为空");
		}
		if(licenseSupplierInfo.getDeptId() == null){
			throw new HdiException("机构id不能为空");
		}
		if(licenseSupplierInfo.getBeginTime().after(licenseSupplierInfo.getEndTime())) {
			throw new HdiException("请选择正确的证照效期！");
		}
		licenseSupplierInfo.setStatus(StatusEnum.USABLE.getKey());
		licenseSupplierInfo.setDelFlag(DelFlagEnum.NORMAL.getKey());
		licenseSupplierInfo.setCreateTime(new Date());
		this.baseMapper.insert(licenseSupplierInfo);
		
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(LicenseSupplierInfoEntity licenseSupplierInfo) {
		if(licenseSupplierInfo.getEditId() == null){
			throw new HdiException("修改人id不能为空");
		}
		if(licenseSupplierInfo.getBeginTime().after(licenseSupplierInfo.getEndTime())) {
			throw new HdiException("请选择正确的证照效期！");
		}
		licenseSupplierInfo.setEditTime(new Date());
		this.baseMapper.updateById(licenseSupplierInfo);
		
		//审核通过的证照需要重新提交给医院审批
		List<LicenseHospitalExamineEntity> list = licenseHospitalExamineService.selectList(
			new EntityWrapper<LicenseHospitalExamineEntity>()
			.eq("license_id", licenseSupplierInfo.getId())
			.eq("status", ApprovalTypeEnum.PASS.getKey())
		);
		if(!StringUtil.isEmpty(list)) {
			for(LicenseHospitalExamineEntity licenseHospitalExamineEntity : list) {
				//设置为未审批状态
				licenseHospitalExamineEntity.setStatus(0);
			}
			licenseHospitalExamineService.updateBatchById(list);
		}
		
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long[] ids) {

		for (Long id : ids) {
			//医院已审批通过的证照信息记录不能再删除
			List<LicenseHospitalExamineEntity> list = licenseHospitalExamineService.selectList(
					new EntityWrapper<LicenseHospitalExamineEntity>()
					.eq("status", ApprovalTypeEnum.PASS.getKey())
					.eq("license_id", id)
					.eq("license_type", 1));
			if(!StringUtil.isEmpty(list)) {
				throw new HdiException("删除失败!证照信息已通过医院审批！");
			}
			
			LicenseSupplierInfoEntity licenseSupplierInfo = new LicenseSupplierInfoEntity();
			licenseSupplierInfo.setId(id);
			licenseSupplierInfo.setDelFlag(DelFlagEnum.DELETE.getKey());
			this.baseMapper.updateById(licenseSupplierInfo);
		}
		
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void examine(Map<String, Object> params) {
		if(params.get("createId") == null){
			throw new HdiException("创建人id不能为空");
		}
		if(params.get("deptId") == null){
			throw new HdiException("机构id不能为空");
		}
		if(params.get("licenseId") == null){
			throw new HdiException("证照id不能为空");
		}
		if(params.get("hospitalIds") == null){
			throw new HdiException("请选择医院");
		}
		List<Long> hospitalIds = JSON.parseArray(params.get("hospitalIds").toString(), Long.class);
		if(StringUtil.isEmpty(hospitalIds)) {
			throw new HdiException("至少选择一家医院！");
		}
		
		LicenseSupplierInfoEntityVo licenseSupplierInfo = this.baseMapper.selectLicenseSupplierInfoById(Long.valueOf(params.get("licenseId").toString()));
		
		if(StringUtil.isEmpty(licenseSupplierInfo)) {
			throw new HdiException("证照id不存在或已停用！");
		}
		
		for (Long hospitalId : hospitalIds) {
			LicenseHospitalExamineEntity licenseHospitalExamineEntity = new LicenseHospitalExamineEntity();
			licenseHospitalExamineEntity.setCreateId(Long.valueOf(params.get("createId").toString()));
			licenseHospitalExamineEntity.setDeptId(Long.valueOf(params.get("deptId").toString()));
			licenseHospitalExamineEntity.setHospitalId(hospitalId);
			licenseHospitalExamineEntity.setLicenseType(LicenseTypeEnum.LICENSE_SUPPLIER.getKey());
			
			//保存供应商证照历史信息
			licenseHospitalExamineEntity.setLicenseId(licenseSupplierInfo.getId());
			licenseHospitalExamineEntity.setBusinessId(licenseSupplierInfo.getSupplierId());
			licenseHospitalExamineEntity.setBusinessName(licenseSupplierInfo.getSupplierName());
			licenseHospitalExamineEntity.setClassifyId(licenseSupplierInfo.getClassifyId());
			licenseHospitalExamineEntity.setName(licenseSupplierInfo.getName());
			licenseHospitalExamineEntity.setNumber(licenseSupplierInfo.getNumber());
			licenseHospitalExamineEntity.setBeginTime(licenseSupplierInfo.getBeginTime());
			licenseHospitalExamineEntity.setEndTime(licenseSupplierInfo.getEndTime());
			licenseHospitalExamineEntity.setPicUrl(licenseSupplierInfo.getPicUrl());			
			
			licenseHospitalExamineService.save(licenseHospitalExamineEntity);
		}
		
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void replace(LicenseSupplierInfoEntity licenseSupplierInfo) {
		if(licenseSupplierInfo.getCreateId() == null){
			throw new HdiException("创建人id不能为空");
		}
		if(licenseSupplierInfo.getDeptId() == null){
			throw new HdiException("机构id不能为空");
		}
		if(licenseSupplierInfo.getBeginTime().after(licenseSupplierInfo.getEndTime())) {
			throw new HdiException("请选择正确的证照效期！");
		}
		//保存新证
		Long oldLicenseId = licenseSupplierInfo.getId();
		licenseSupplierInfo.setId(null);
		licenseSupplierInfo.setStatus(StatusEnum.USABLE.getKey());
		licenseSupplierInfo.setDelFlag(DelFlagEnum.NORMAL.getKey());
		licenseSupplierInfo.setCreateTime(new Date());
		this.baseMapper.insert(licenseSupplierInfo);
		
		//旧证关联新证
		LicenseSupplierInfoEntity licenseSupplierInfoEntity = new LicenseSupplierInfoEntity();
		licenseSupplierInfoEntity.setId(oldLicenseId);
		licenseSupplierInfoEntity.setNewLicenseId(licenseSupplierInfo.getId());
		this.baseMapper.updateById(licenseSupplierInfoEntity);
		
		
	}
	
	@Override
	public LicenseSupplierInfoEntityVo selectLicenseSupplierInfoById(Long id) {
		return this.baseMapper.selectLicenseSupplierInfoById(id);
	}

	@Override
	public List<LicenseSupplierInfoEntityVo> selectRelativesById(Long id) {
		LicenseSupplierInfoEntityVo licenseSupplierInfo = this.baseMapper.selectLicenseSupplierInfoById(id);
		List<LicenseSupplierInfoEntityVo> newLicenseList = this.selectNewLicense(licenseSupplierInfo, new ArrayList<LicenseSupplierInfoEntityVo>());
		List<LicenseSupplierInfoEntityVo> oldLicenseList = this.selectOldLicense(licenseSupplierInfo, new ArrayList<LicenseSupplierInfoEntityVo>());
		//证照列表按新旧倒序排列
		Collections.reverse(oldLicenseList);
		newLicenseList.addAll(oldLicenseList);
		return newLicenseList;
	}
	
	@Override
	public Map<String, Object> goodsDetails(Map<String, Object> params) {
		if(params != null){
			Map<String,Object> resultMap = new HashMap<>();
			String supplierId = (String)params.get("supplierId");
			String agentId = (String)params.get("agentId");
			String factoryId = (String)params.get("factoryId");
			String goodsId = (String)params.get("goodsId");
			if(StringUtils.isBlank(supplierId)){
				throw new HdiException("供应商id不能为空");
			}
			//查询供应商证照
			resultMap.put("supplier", baseMapper.selectSupplierView(supplierId));
			//查询商品证照
			if(StringUtils.isNotBlank(goodsId)){
				resultMap.put("goods", baseMapper.selectGoodsView(supplierId, goodsId));
			}
			//查询厂商证照
			if(StringUtils.isNotBlank(factoryId)){
				resultMap.put("factory", baseMapper.selectFactoryView(supplierId, factoryId));
			}
			//查询代理商证照
			if(StringUtils.isNotBlank(agentId)){
				resultMap.put("agent", baseMapper.selectAgentView(supplierId, agentId));
			}
			return resultMap;
		}
		return null;
	}
	
	/**
	 * 递归获取新证
	 * @param license 当前证照对象
	 * @param licenseList 证照列表
	 * @return 新证列表
	 */
	private List<LicenseSupplierInfoEntityVo> selectNewLicense(LicenseSupplierInfoEntityVo license, List<LicenseSupplierInfoEntityVo> licenseList){
		if(license.getNewLicenseId() != null) {
			LicenseSupplierInfoEntityVo newLicense = this.baseMapper.selectLicenseSupplierInfoById(license.getNewLicenseId());
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
	private List<LicenseSupplierInfoEntityVo> selectOldLicense(LicenseSupplierInfoEntityVo license, List<LicenseSupplierInfoEntityVo> licenseList){
		LicenseSupplierInfoEntityVo oldLicense = this.baseMapper.selectLicenseSupplierInfoByNewLicenseId(license.getId());
		if (oldLicense != null) {
			selectOldLicense(oldLicense, licenseList);
			licenseList.add(oldLicense);
		}
		return licenseList;
		
	}

	@Override
	public PageUtils examineInfo(Map<String, Object> params) {
		params.put("licenseType", LicenseTypeEnum.LICENSE_SUPPLIER.getKey());
		return licenseHospitalExamineService.queryPage(params);
	}

	@Override
	public List<LicenseSupplierInfoEntity> selectBySupplierIdAndTime(Long supplierId, ScheduleJobEntity scheduleJob) {
		return baseMapper.selectBySupplierIdAndTime(supplierId,scheduleJob);
	}

	
	

}
