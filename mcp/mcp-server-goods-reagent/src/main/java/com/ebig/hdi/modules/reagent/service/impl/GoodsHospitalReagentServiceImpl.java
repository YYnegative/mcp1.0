package com.ebig.hdi.modules.reagent.service.impl;

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
import com.ebig.hdi.common.utils.Constant;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.reagent.dao.GoodsHospitalReagentDao;
import com.ebig.hdi.modules.reagent.entity.GoodsHospitalReagentEntity;
import com.ebig.hdi.modules.reagent.entity.OrgFactorysInfoEntity;
import com.ebig.hdi.modules.reagent.service.GoodsHospitalReagentService;
import com.ebig.hdi.modules.reagent.service.GoodsHospitalReagentSpecsService;
import com.ebig.hdi.modules.reagent.service.OrgFactorysInfoService;
import com.ebig.hdi.modules.reagent.vo.GoodsHospitalReagentVO;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import com.ebig.hdi.modules.sys.service.SysSequenceService;
import com.ebig.hdi.modules.sys.service.SysUserService;


@Service("goodsHospitalReagentService")
public class GoodsHospitalReagentServiceImpl extends ServiceImpl<GoodsHospitalReagentDao, GoodsHospitalReagentEntity> implements GoodsHospitalReagentService {
	
	@Autowired
	private OrgFactorysInfoService orgFactorysInfoService;
	@Autowired
	private SysSequenceService sysSequenceService;
	@Autowired
	private GoodsHospitalReagentSpecsService goodsHospitalReagentSpecsService;
	@Autowired
	private SysUserService sysUserService;
	
    @Override
    @DataFilter(subDept = true, user = false, tableAlias = "t1")
    public PageUtils queryPage(Map<String, Object> params) {
    	GoodsHospitalReagentVO ghrVO = new GoodsHospitalReagentVO();
    	ghrVO.setHospitalName((String)params.get("hospitalName"));//医院名称
    	ghrVO.setReagentName((String)params.get("reagentName"));//试剂名称
    	ghrVO.setGoodsNature((Integer)params.get("goodsNature"));//商品属性
    	ghrVO.setTypeName((String)params.get("typeName"));//分类名称
    	ghrVO.setStatus((Integer)params.get("status"));//状态
    	ghrVO.setFactoryName((String)params.get("factoryName"));//生产厂商
    	ghrVO.setFileterDept((String)params.get(Constant.SQL_FILTER));//设置权限
    	
    	int currPage = Integer.parseInt(params.get("page").toString());
    	int pageSize = Integer.parseInt(params.get("limit").toString());
    	
    	Page<GoodsHospitalReagentVO> page = new Page<GoodsHospitalReagentVO>(currPage, pageSize);
    	
    	List<GoodsHospitalReagentVO> list = this.baseMapper.listForPage(page,ghrVO);
    	setApprovalsAndSpecs(list);
    	page.setRecords(list);
        return new PageUtils(page);
    }

    @Override
	@Transactional(rollbackFor = Exception.class)
	public void save(GoodsHospitalReagentVO goodsHospitalReagentVO) {
		List<GoodsHospitalReagentEntity> prList = this.selectList(new EntityWrapper<GoodsHospitalReagentEntity>()
				.eq("del_flag", DelFlagEnum.NORMAL.getKey())
				.eq("reagent_name", goodsHospitalReagentVO.getReagentName())
				.eq("hospital_id", goodsHospitalReagentVO.getHospitalId())
				);
		if(prList.size()!=0){
			throw new HdiException("本试剂名称已存在");
		}
		String factoryId = goodsHospitalReagentVO.getFactoryId();
		if(StringUtils.isBlank(factoryId)){
			//如果厂商id为空，则新加一个厂商信息，状态为待审批
			OrgFactorysInfoEntity factory = new OrgFactorysInfoEntity();
			String FactoryCode = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.FACTORY_CODE.getKey());
			factory.setFactoryCode(FactoryCode);
			if (StringUtils.isBlank(goodsHospitalReagentVO.getFactoryName())) {
				throw new HdiException("厂商名称不能为空");
			}
			
			if(orgFactorysInfoService.selectList(new EntityWrapper<OrgFactorysInfoEntity>()
					.eq("del_flag", DelFlagEnum.NORMAL.getKey())
					.eq("factory_name", goodsHospitalReagentVO.getFactoryName())).size()>0) {
				throw new HdiException("厂商名称已存在");
			}
			
			factory.setFactoryName(goodsHospitalReagentVO.getFactoryName());
			factory.setStatus(FactoryStatusEnum.DRAFT.getKey());
			factory.setCreateId(goodsHospitalReagentVO.getCreateId());
			factory.setCreateTime(new Date());
			SysUserEntity user = sysUserService.selectById(goodsHospitalReagentVO.getCreateId());
			if(user != null && TypeEnum.USER_PLATFORM.getKey().equals(user.getUserType())) {
				//平台用户录入，厂家信息属于该用户
				factory.setDeptId(user.getDeptId());
			}
			orgFactorysInfoService.insert(factory);
			factoryId = factory.getId().toString();
		}
		
		String reagentCode = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.HOSPITAL_REAGENT_CODE.getKey());
		goodsHospitalReagentVO.setReagentCode(reagentCode);
		goodsHospitalReagentVO.setCreateTime(new Date());
		goodsHospitalReagentVO.setFactoryId(factoryId);
		goodsHospitalReagentVO.setDataSource(HospitalDataSource.SYSTEM.getKey());
		Map<String, Object> hospitalInfo = this.baseMapper.selectHospitalInfoByHospitalId(goodsHospitalReagentVO.getHospitalId());
		if(StringUtil.isEmpty(hospitalInfo)) {
			throw new HdiException("医院不存在！");
		}
		goodsHospitalReagentVO.setDeptId((Long)hospitalInfo.get("dept_id"));
		baseMapper.insert(goodsHospitalReagentVO);
		
		//保存试剂规格
		goodsHospitalReagentSpecsService.save(goodsHospitalReagentVO);
	}
    
    @Override
	@Transactional(rollbackFor = Exception.class)
	public void update(GoodsHospitalReagentVO goodsHospitalReagentVO) {
		List<GoodsHospitalReagentEntity> prList = this.selectList(new EntityWrapper<GoodsHospitalReagentEntity>()
				.eq("reagent_name", goodsHospitalReagentVO.getReagentName())
				.eq("del_flag", DelFlagEnum.NORMAL.getKey())
				.eq("hospital_id", goodsHospitalReagentVO.getHospitalId())
				.ne("id", goodsHospitalReagentVO.getId())
				);
		if(prList.size()!=0){
			throw new HdiException("本试剂名称已存在");
		}
		String factoryId = goodsHospitalReagentVO.getFactoryId();
		if(StringUtils.isBlank(factoryId)){
			//如果厂商id为空，则新加一个厂商信息，状态为待审批
			OrgFactorysInfoEntity factory = new OrgFactorysInfoEntity();
			String FactoryCode = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.FACTORY_CODE.getKey());
			factory.setFactoryCode(FactoryCode);
			if (StringUtils.isBlank(goodsHospitalReagentVO.getFactoryName())) {
				throw new HdiException("厂商名称不能为空");
			}
			
			if(orgFactorysInfoService.selectList(new EntityWrapper<OrgFactorysInfoEntity>()
					.eq("del_flag", DelFlagEnum.NORMAL.getKey())
					.eq("factory_name", goodsHospitalReagentVO.getFactoryName())).size()>0) {
				throw new HdiException("厂商名称已存在");
			}
			
			factory.setFactoryName(goodsHospitalReagentVO.getFactoryName());
			factory.setStatus(FactoryStatusEnum.DRAFT.getKey());
			factory.setCreateId(goodsHospitalReagentVO.getCreateId());
			factory.setCreateTime(new Date());
			SysUserEntity user = sysUserService.selectById(goodsHospitalReagentVO.getEditId());
			if(user != null && TypeEnum.USER_PLATFORM.getKey().equals(user.getUserType())) {
				//平台用户录入，厂家信息属于该用户
				factory.setDeptId(user.getDeptId());
			}
			orgFactorysInfoService.insert(factory);
			factoryId = factory.getId().toString();
		} 
		goodsHospitalReagentVO.setFactoryId(factoryId);
		goodsHospitalReagentVO.setEditTime(new Date());
		goodsHospitalReagentVO.setIsMatch(0);
		baseMapper.updateById(goodsHospitalReagentVO);
		
	}

	@Override
	public GoodsHospitalReagentVO selectById(Long id) {
		return baseMapper.selectReagentById(id);
	}
	
	private void setApprovalsAndSpecs(List<GoodsHospitalReagentVO> list){
		if(list != null){
			for(GoodsHospitalReagentVO ghrVO : list){
				ghrVO.setSpecsEntityList(goodsHospitalReagentSpecsService.selectListByReagentId(ghrVO.getId()));
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void toggle(Map<String, Object> params) {
		List<Long> ids = JSON.parseArray(params.get("ids").toString(), Long.class);
		for(Long id : ids) {
			GoodsHospitalReagentEntity goodsHospitalReagent = new GoodsHospitalReagentEntity();
			goodsHospitalReagent.setId(id);
			goodsHospitalReagent.setStatus(Integer.valueOf(params.get("status").toString()));
			this.baseMapper.updateById(goodsHospitalReagent);
		}
	}
	

	
	
	@Override
	public void updateIsMatch(Long goodsId) {
		GoodsHospitalReagentEntity goodsHospitalReagentEntity = new GoodsHospitalReagentEntity();
		goodsHospitalReagentEntity.setId(goodsId);
		goodsHospitalReagentEntity.setIsMatch(1);
		baseMapper.updateById(goodsHospitalReagentEntity);
		
	}

	@Override
	public GoodsHospitalReagentEntity selectByGoodsNameAndFactoryNameAndHospitalId(String goodsName, String factoryName, Long hospitalId) {
		return baseMapper.selectByGoodsNameAndFactoryNameAndHospitalId(goodsName,factoryName,hospitalId);
	}
	
	
	@Override
	public Map<String, Object> selectBySourcesId(String sourcesSpecsId) {
		Map<String, Object> consumablesMap = this.baseMapper.selectBySourcesId(sourcesSpecsId);
		return consumablesMap;
	}

}
