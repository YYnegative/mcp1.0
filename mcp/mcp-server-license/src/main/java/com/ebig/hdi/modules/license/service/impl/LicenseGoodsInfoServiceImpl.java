package com.ebig.hdi.modules.license.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.enums.LicenseTypeEnum;
import com.ebig.hdi.common.enums.StatusEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.Constant;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.job.entity.ScheduleJobEntity;
import com.ebig.hdi.modules.license.dao.LicenseGoodsInfoDao;
import com.ebig.hdi.modules.license.entity.LicenseGoodsInfoEntity;
import com.ebig.hdi.modules.license.entity.LicenseHospitalExamineEntity;
import com.ebig.hdi.modules.license.entity.vo.LicenseGoodsInfoVO;
import com.ebig.hdi.modules.license.service.LicenseGoodsInfoService;
import com.ebig.hdi.modules.license.service.LicenseHospitalExamineService;

@Service("licenseGoodsInfoService")
public class LicenseGoodsInfoServiceImpl extends ServiceImpl<LicenseGoodsInfoDao, LicenseGoodsInfoEntity>
		implements LicenseGoodsInfoService {

	@Autowired
	private LicenseHospitalExamineService licenseHospitalExamineService;

	@Override
	@DataFilter(subDept = true, user = false)
	public PageUtils queryPage(Map<String, Object> params) {
		LicenseGoodsInfoVO lgiVO = new LicenseGoodsInfoVO();
		if (params.get("classifyId") != null) {
			lgiVO.setClassifyId(Long.valueOf(params.get("classifyId").toString()));
		}
		lgiVO.setGoodsName((String) params.get("goodsName"));
		lgiVO.setFactoryName((String) params.get("factoryName"));
		lgiVO.setNameOrNumber((String) params.get("nameOrNumber"));
		lgiVO.setLicenseStatus((Integer) params.get("licenseStatus"));
		lgiVO.setBeginTimeStr((String) params.get("beginTimeStr"));
		lgiVO.setEndTimeStr((String) params.get("endTimeStr"));
		lgiVO.setFileterDept((String) params.get(Constant.SQL_FILTER));

		String sidx = (String) params.get("sidx");
		String order = (String) params.get("order");
		int currPage = Integer.parseInt(params.get("page").toString());
		int pageSize = Integer.parseInt(params.get("limit").toString());

		Page<LicenseGoodsInfoVO> page = new Page<LicenseGoodsInfoVO>(currPage, pageSize, order);
		if (sidx != null) {
			page.setAsc(sidx.equals("desc") ? false : true);
		} else {
			// 标志，设置默认按更新时间和创建时间排序
			lgiVO.setIsDefaultOrder(1);
		}

		List<LicenseGoodsInfoVO> list = this.baseMapper.listForPage(page, lgiVO);

		page.setRecords(list);
		return new PageUtils(page);
	}

	@Override
	public LicenseGoodsInfoVO selectById(Long id) {
		if (id == null) {
			throw new HdiException("传入的商品证照id不能为空");
		}
		LicenseGoodsInfoVO currentEntity = baseMapper.selectLicenseById(id);
		if (currentEntity.getNewLicenseId() != null) {
			// 设置父证照
			currentEntity.setParentAgentLicense(baseMapper.selectLicenseById(currentEntity.getNewLicenseId()));
		}
		// 设置子证照
		currentEntity.setChildAgentLicense(baseMapper.selectByNewLicenseId(currentEntity.getId()));
		return currentEntity;
	}

	@Override
	public List<Map<String, Object>> allGoods(Long supplierId) {
		return baseMapper.allGoods(supplierId);
	}

	@Override
	public void save(LicenseGoodsInfoVO licenseGoodsInfoVO) {
		checkValue(licenseGoodsInfoVO);

		// 把字符串类型的时间转换为date类型
		setTime(licenseGoodsInfoVO);
		licenseGoodsInfoVO.setStatus(StatusEnum.USABLE.getKey());
		licenseGoodsInfoVO.setCreateTime(new Date());

		baseMapper.insert(licenseGoodsInfoVO);
	}

	@Override
	public void update(LicenseGoodsInfoVO licenseGoodsInfoVO) {
		if (licenseGoodsInfoVO.getId() == null) {
			throw new HdiException("主键id不能为空");
		}
		if (licenseGoodsInfoVO.getEditId() == null) {
			throw new HdiException("修改人id不能为空");
		}
		// 把字符串类型的时间转换为date类型
		setTime(licenseGoodsInfoVO);
		licenseGoodsInfoVO.setEditTime(new Date());
		baseMapper.updateById(licenseGoodsInfoVO);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void replace(LicenseGoodsInfoVO licenseGoodsInfoVO) {
		checkValue(licenseGoodsInfoVO);
		Long id = licenseGoodsInfoVO.getId();
		if (id == null) {
			throw new HdiException("主键id不能为空");
		}
		// 先插入新证记录
		// 把字符串类型的时间转换为date类型
		setTime(licenseGoodsInfoVO);

		licenseGoodsInfoVO.setStatus(StatusEnum.USABLE.getKey());
		licenseGoodsInfoVO.setId(null);
		licenseGoodsInfoVO.setCreateTime(new Date());
		baseMapper.insert(licenseGoodsInfoVO);

		// 把新证id插入旧证的数据中
		LicenseGoodsInfoVO old = new LicenseGoodsInfoVO();
		old.setId(id);
		old.setNewLicenseId(licenseGoodsInfoVO.getId());
		old.setEditId(licenseGoodsInfoVO.getCreateId());
		old.setEditTime(new Date());
		baseMapper.updateById(old);
	}

	@Override
	public List<LicenseGoodsInfoVO> details(Long id) {
		List<LicenseGoodsInfoVO> resultList = new ArrayList<>();
		// 先判断该证照是不是旧证照
		LicenseGoodsInfoVO lgiVO = this.selectById(id);
		if (lgiVO.getNewLicenseId() != null) {
			setAllParentLicenseGoods(resultList, lgiVO);
		}
		resultList.add(lgiVO);
		setAllChildLicenseGoods(resultList, lgiVO);
		return resultList;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void examine(Map<String, Object> params) {
		if (params.get("createId") == null) {
			throw new HdiException("创建人id不能为空");
		}
		if (params.get("deptId") == null) {
			throw new HdiException("机构id不能为空");
		}
		if (params.get("licenseId") == null) {
			throw new HdiException("证照id不能为空");
		}
		if (params.get("hospitalIds") == null) {
			throw new HdiException("请选择医院");
		}
		List<Long> hospitalIds = JSON.parseArray(params.get("hospitalIds").toString(), Long.class);
		if (StringUtil.isEmpty(hospitalIds)) {
			throw new HdiException("至少选择一家医院！");
		}

		LicenseGoodsInfoVO licenseGoodsInfo = this.baseMapper.selectLicenseById(Long.valueOf(params.get("licenseId").toString()));

		if (StringUtil.isEmpty(licenseGoodsInfo)) {
			throw new HdiException("证照不存在或已停用！");
		}

		for (Long hospitalId : hospitalIds) {
			LicenseHospitalExamineEntity licenseHospitalExamineEntity = new LicenseHospitalExamineEntity();
			licenseHospitalExamineEntity.setCreateId(Long.valueOf(params.get("createId").toString()));
			licenseHospitalExamineEntity.setDeptId(Long.valueOf(params.get("deptId").toString()));
			licenseHospitalExamineEntity.setHospitalId(hospitalId);
			licenseHospitalExamineEntity.setLicenseType(LicenseTypeEnum.LICENSE_GOODS.getKey());

			// 保存商品历史信息
			licenseHospitalExamineEntity.setLicenseId(licenseGoodsInfo.getId());
			licenseHospitalExamineEntity.setBusinessId(licenseGoodsInfo.getGoodsId());
			licenseHospitalExamineEntity.setBusinessName(licenseGoodsInfo.getGoodsName());
			licenseHospitalExamineEntity.setClassifyId(licenseGoodsInfo.getClassifyId());
			licenseHospitalExamineEntity.setName(licenseGoodsInfo.getName());
			licenseHospitalExamineEntity.setNumber(licenseGoodsInfo.getNumber());
			licenseHospitalExamineEntity.setBeginTime(licenseGoodsInfo.getBeginTime());
			licenseHospitalExamineEntity.setEndTime(licenseGoodsInfo.getEndTime());
			licenseHospitalExamineEntity.setPicUrl(licenseGoodsInfo.getPicUrl());

			licenseHospitalExamineService.save(licenseHospitalExamineEntity);
		}
	}

	@Override
	public PageUtils examineInfo(Map<String, Object> params) {
		params.put("licenseType", LicenseTypeEnum.LICENSE_GOODS.getKey());
		return licenseHospitalExamineService.queryPage(params);
	}

	/**
	 * 递归往上设置证照商品
	 * 
	 * @param resultList
	 * @param lgiVO
	 */
	private void setAllParentLicenseGoods(List<LicenseGoodsInfoVO> resultList, LicenseGoodsInfoVO lgiVO) {
		LicenseGoodsInfoVO entity = baseMapper.selectLicenseById(lgiVO.getNewLicenseId());
		resultList.add(0, entity);
		if (entity.getNewLicenseId() == null) {
			return;
		}
		setAllParentLicenseGoods(resultList, entity);
	}

	/**
	 * 递归往下设置证照商品
	 * 
	 * @param resultList
	 * @param lgiVO
	 */
	private void setAllChildLicenseGoods(List<LicenseGoodsInfoVO> resultList, LicenseGoodsInfoVO lgiVO) {
		LicenseGoodsInfoVO entity = baseMapper.selectByNewLicenseId(lgiVO.getId());
		if (entity == null) {
			return;
		}
		resultList.add(entity);
		setAllChildLicenseGoods(resultList, entity);
	}

	/**
	 * 转换字符串为date类型
	 * 
	 * @param licenseGoodsInfoVO
	 */
	private void setTime(LicenseGoodsInfoVO licenseGoodsInfoVO) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date beginTime = null;
		Date endTime = null;
		try {
			beginTime = sdf.parse(licenseGoodsInfoVO.getBeginTimeStr());
			endTime = sdf.parse(licenseGoodsInfoVO.getEndTimeStr());
		} catch (Exception e) {
			e.printStackTrace();
		}
		licenseGoodsInfoVO.setBeginTime(beginTime);
		licenseGoodsInfoVO.setEndTime(endTime);
	}

	/**
	 * 校验必填属性
	 * 
	 * @param licenseGoodsInfoVO
	 */
	private void checkValue(LicenseGoodsInfoVO licenseGoodsInfoVO) {
		if (licenseGoodsInfoVO.getGoodsId() == null) {
			throw new HdiException("商品id不能为空");
		}
		if (licenseGoodsInfoVO.getGoodsType() == null) {
			throw new HdiException("商品类别不能为空");
		}
		if (licenseGoodsInfoVO.getSupplierId() == null) {
			throw new HdiException("供应商id不能为空");
		}
		if (licenseGoodsInfoVO.getClassifyId() == null) {
			throw new HdiException("证照分类id不能为空");
		}
		if (StringUtils.isBlank(licenseGoodsInfoVO.getName())) {
			throw new HdiException("证照名称不能为空");
		}
		if (StringUtils.isBlank(licenseGoodsInfoVO.getNumber())) {
			throw new HdiException("证照编号不能为空");
		}
		if (StringUtils.isBlank(licenseGoodsInfoVO.getBeginTimeStr())) {
			throw new HdiException("证照效期开始时间不能为空");
		}
		if (StringUtils.isBlank(licenseGoodsInfoVO.getEndTimeStr())) {
			throw new HdiException("证照效期结束时间不能为空");
		}
		if (licenseGoodsInfoVO.getDeptId() == null) {
			throw new HdiException("机构id不能为空");
		}
		if (licenseGoodsInfoVO.getCreateId() == null) {
			throw new HdiException("创建人id不能为空");
		}
	}

	@Override
	public List<LicenseGoodsInfoEntity> selectBySupplierIdAndTime(Long supplierId, ScheduleJobEntity scheduleJob) {
		return baseMapper.selectBySupplierIdAndTime(supplierId,scheduleJob);
	}

}
