package com.ebig.hdi.modules.drugs.service.impl;

import java.util.Date;
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
import com.ebig.hdi.common.enums.DelFlagEnum;
import com.ebig.hdi.common.enums.FactoryStatusEnum;
import com.ebig.hdi.common.enums.HospitalDataSource;
import com.ebig.hdi.common.enums.SequenceEnum;
import com.ebig.hdi.common.enums.TypeEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.drugs.dao.GoodsHospitalDrugsDao;
import com.ebig.hdi.modules.drugs.entity.GoodsHospitalDrugsEntity;
import com.ebig.hdi.modules.drugs.entity.GoodsHospitalDrugsSpecsEntity;
import com.ebig.hdi.modules.drugs.entity.OrgFactoryEntity;
import com.ebig.hdi.modules.drugs.service.GoodsHospitalDrugsService;
import com.ebig.hdi.modules.drugs.service.GoodsHospitalDrugsSpecsService;
import com.ebig.hdi.modules.drugs.service.OrgFactoryService;
import com.ebig.hdi.modules.drugs.vo.GoodsHospitalDrugsEntityVo;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import com.ebig.hdi.modules.sys.service.SysSequenceService;
import com.ebig.hdi.modules.sys.service.SysUserService;


@Service("goodsHospitalDrugsService")
public class GoodsHospitalDrugsServiceImpl extends ServiceImpl<GoodsHospitalDrugsDao, GoodsHospitalDrugsEntity> implements GoodsHospitalDrugsService {

	@Autowired
	private GoodsHospitalDrugsSpecsService goodsHospitalDrugsSpecsService;

	@Autowired
	private OrgFactoryService orgFactoryService;

	@Autowired
	private SysSequenceService sysSequenceService;
	
	@Autowired
	private SysUserService sysUserService;
	
    @Override
    @DataFilter(subDept = true, user = false, tableAlias = "d")
    public PageUtils queryPage(Map<String, Object> params) {
		int currPage = Integer.parseInt(params.get("page").toString());
		int pageSize = Integer.parseInt(params.get("limit").toString());

		Page<GoodsHospitalDrugsEntityVo> page = new Page<GoodsHospitalDrugsEntityVo>(currPage, pageSize);

		List<GoodsHospitalDrugsEntityVo> list = this.baseMapper.selectHospitalDrugsList(page, params);

		for(GoodsHospitalDrugsEntityVo goodsHospitalDrugs : list) {
			List<GoodsHospitalDrugsSpecsEntity> specsList = goodsHospitalDrugsSpecsService.selectListByDrugsId(goodsHospitalDrugs.getId());
			goodsHospitalDrugs.setSpecsEntityList(specsList);
		}
		
		page.setRecords(list);

		return new PageUtils(page);
    }

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(GoodsHospitalDrugsEntityVo goodsHospitalDrugsEntityVo) {
		// 判断药品名称是否存在
		GoodsHospitalDrugsEntity drugsName = this.baseMapper
				.selectByHospitalIdAndDrugsName(goodsHospitalDrugsEntityVo.getHospitalId(), goodsHospitalDrugsEntityVo.getDrugsName());
		if (drugsName != null) {
			throw new HdiException("药品名称：" + goodsHospitalDrugsEntityVo.getDrugsName() + "，已存在！");
		}

		// 生产厂商id为空则保存厂商信息
		if (StringUtil.isEmpty(goodsHospitalDrugsEntityVo.getFactoryId())) {
			// 保存厂商信息，状态为待审批
			OrgFactoryEntity orgFactoryEntity = new OrgFactoryEntity();
			String factoryCode = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.FACTORY_CODE.getKey());
			orgFactoryEntity.setFactoryCode(factoryCode);
			if (StringUtils.isBlank(goodsHospitalDrugsEntityVo.getFactoryName())) {
				throw new HdiException("厂商名称不能为空");
			}
			
			if(orgFactoryService.selectList(new EntityWrapper<OrgFactoryEntity>()
					.eq("del_flag", DelFlagEnum.NORMAL.getKey())
					.eq("factory_name", goodsHospitalDrugsEntityVo.getFactoryName())).size()>0) {
				throw new HdiException("厂商名称已存在");
			}
			
			orgFactoryEntity.setFactoryName(goodsHospitalDrugsEntityVo.getFactoryName());
			orgFactoryEntity.setStatus(FactoryStatusEnum.DRAFT.getKey());
			orgFactoryEntity.setDelFlag(DelFlagEnum.NORMAL.getKey());
			orgFactoryEntity.setCreateId(goodsHospitalDrugsEntityVo.getCreateId());
			orgFactoryEntity.setCreateTime(new Date());
			
			SysUserEntity user = sysUserService.selectById(goodsHospitalDrugsEntityVo.getCreateId());
			if(user != null && TypeEnum.USER_PLATFORM.getKey().equals(user.getUserType())) {
				//平台用户录入，厂家信息属于该用户
				orgFactoryEntity.setDeptId(user.getDeptId());
			}
			
			orgFactoryService.insert(orgFactoryEntity);
			
			goodsHospitalDrugsEntityVo.setFactoryId(String.valueOf(orgFactoryEntity.getId()));
		}
		
		//生成药品编码
		String drugsCode = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.HOSPITAL_DRUGS_CODE.getKey());
		goodsHospitalDrugsEntityVo.setDrugsCode(drugsCode);

		//设置未匹对(0:未匹对;1:已匹对)
		goodsHospitalDrugsEntityVo.setIsMatch(0);

		// 保存药品信息
		goodsHospitalDrugsEntityVo.setCreateTime(new Date());
		goodsHospitalDrugsEntityVo.setDataSource(HospitalDataSource.SYSTEM.getKey());
		Map<String, Object> hospitalInfo = this.baseMapper.selectHospitalInfoByHospitalId(goodsHospitalDrugsEntityVo.getHospitalId());
		if(StringUtil.isEmpty(hospitalInfo)) {
			throw new HdiException("医院不存在！");
		}
		goodsHospitalDrugsEntityVo.setDeptId((Long) hospitalInfo.get("dept_id"));
		this.baseMapper.insert(goodsHospitalDrugsEntityVo);

		// 保存商品规格
		goodsHospitalDrugsSpecsService.save(goodsHospitalDrugsEntityVo);
		
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(GoodsHospitalDrugsEntityVo goodsHospitalDrugsEntityVo) {
		// 判断药品名称是否存在
		GoodsHospitalDrugsEntity drugsName = this.baseMapper
				.selectByHospitalIdAndDrugsName(goodsHospitalDrugsEntityVo.getHospitalId(), goodsHospitalDrugsEntityVo.getDrugsName());
		if (drugsName != null && !drugsName.getId().equals(goodsHospitalDrugsEntityVo.getId())) {
			throw new HdiException("药品名称：" + goodsHospitalDrugsEntityVo.getDrugsName() + "，已存在！");
		}

		// 生产厂商id为空则保存厂商信息
		if (StringUtil.isEmpty(goodsHospitalDrugsEntityVo.getFactoryId())) {
			// 保存厂商信息，状态为待审批
			OrgFactoryEntity orgFactoryEntity = new OrgFactoryEntity();
			String factoryCode = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.FACTORY_CODE.getKey());
			orgFactoryEntity.setFactoryCode(factoryCode);
			if (StringUtils.isBlank(goodsHospitalDrugsEntityVo.getFactoryName())) {
				throw new HdiException("厂商名称不能为空");
			}
			
			if(orgFactoryService.selectList(new EntityWrapper<OrgFactoryEntity>()
					.eq("del_flag", DelFlagEnum.NORMAL.getKey())
					.eq("factory_name", goodsHospitalDrugsEntityVo.getFactoryName())).size()>0) {
				throw new HdiException("厂商名称已存在");
			}
			
			orgFactoryEntity.setFactoryName(goodsHospitalDrugsEntityVo.getFactoryName());
			orgFactoryEntity.setStatus(FactoryStatusEnum.DRAFT.getKey());
			orgFactoryEntity.setDelFlag(DelFlagEnum.NORMAL.getKey());
			orgFactoryEntity.setCreateId(goodsHospitalDrugsEntityVo.getEditId());
			orgFactoryEntity.setCreateTime(new Date());
			
			SysUserEntity user = sysUserService.selectById(goodsHospitalDrugsEntityVo.getEditId());
			if(user != null && TypeEnum.USER_PLATFORM.getKey().equals(user.getUserType())) {
				//平台用户录入，厂家信息属于该用户
				orgFactoryEntity.setDeptId(user.getDeptId());
			}
			
			orgFactoryService.insert(orgFactoryEntity);
			
			goodsHospitalDrugsEntityVo.setFactoryId(String.valueOf(orgFactoryEntity.getId()));
		}

		//设置未匹对(0:未匹对;1:已匹对)
		goodsHospitalDrugsEntityVo.setIsMatch(0);
		
		goodsHospitalDrugsEntityVo.setEditTime(new Date());

		this.baseMapper.updateById(goodsHospitalDrugsEntityVo);
		
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long[] ids) {
		for(Long id : ids) {
			GoodsHospitalDrugsEntity goodsHospitalDrugs = new GoodsHospitalDrugsEntity();
			goodsHospitalDrugs.setId(id);
			goodsHospitalDrugs.setDelFlag(DelFlagEnum.DELETE.getKey());
			this.baseMapper.updateById(goodsHospitalDrugs);
		}
	}

	@Override
	public GoodsHospitalDrugsEntityVo selectHospitalDrugsById(Long id) {
		return this.baseMapper.selectHospitalDrugsById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void toggle(Map<String, Object> params) {
		List<Long> ids = JSON.parseArray(params.get("ids").toString(), Long.class);
		for(Long id : ids) {
			GoodsHospitalDrugsEntity goodsHospitalDrugs = new GoodsHospitalDrugsEntity();
			goodsHospitalDrugs.setId(id);
			goodsHospitalDrugs.setStatus(Integer.valueOf(params.get("status").toString()));
			this.baseMapper.updateById(goodsHospitalDrugs);
		}
	}

	
	@Override
	public void updateIsMatch(Long goodsId) {
		GoodsHospitalDrugsEntity goodsHospitalDrugsEntity = new GoodsHospitalDrugsEntity();
		goodsHospitalDrugsEntity.setId(goodsId);
		goodsHospitalDrugsEntity.setIsMatch(1);
		baseMapper.updateById(goodsHospitalDrugsEntity);
		
	}

	@Override
	public GoodsHospitalDrugsEntity selectByGoodsNameAndFactoryNameAndHospitalId(String goodsName, String factoryName, Long hospitalId) {
		return baseMapper.selectByGoodsNameAndFactoryNameAndHospitalId(goodsName, factoryName, hospitalId);
	}
	
	
	@Override
	public  Map<String, Object> selectBySourcesId(String sourcesSpecsId) {
		Map<String, Object> selectBySourcesId = this.baseMapper.selectBySourcesId(sourcesSpecsId);
		return selectBySourcesId;
	}
}
