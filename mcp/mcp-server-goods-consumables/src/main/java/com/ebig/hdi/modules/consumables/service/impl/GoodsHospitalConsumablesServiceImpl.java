package com.ebig.hdi.modules.consumables.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ebig.hdi.common.entity.OrgFactoryInfoApprovalEntity;
import com.ebig.hdi.modules.core.service.OrgFactoryInfoApprovalService;
import com.ebig.hdi.modules.core.service.OrgFactoryInfoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.enums.ConsumablesTypeEnum;
import com.ebig.hdi.common.enums.DelFlagEnum;
import com.ebig.hdi.common.enums.FactoryStatusEnum;
import com.ebig.hdi.common.enums.HospitalDataSource;
import com.ebig.hdi.common.enums.SequenceEnum;
import com.ebig.hdi.common.enums.StatusEnum;
import com.ebig.hdi.common.enums.TypeEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.consumables.dao.GoodsHospitalConsumablesDao;
import com.ebig.hdi.modules.consumables.entity.GoodsHospitalConsumablesApprovalsEntity;
import com.ebig.hdi.modules.consumables.entity.GoodsHospitalConsumablesEntity;
import com.ebig.hdi.modules.consumables.entity.GoodsHospitalConsumablesSpecsEntity;
import com.ebig.hdi.modules.consumables.service.GoodsHospitalConsumablesApprovalsService;
import com.ebig.hdi.modules.consumables.service.GoodsHospitalConsumablesService;
import com.ebig.hdi.modules.consumables.service.GoodsHospitalConsumablesSpecsService;
import com.ebig.hdi.modules.consumables.vo.GoodsHospitalConsumablesEntityVo;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import com.ebig.hdi.modules.sys.service.SysSequenceService;
import com.ebig.hdi.modules.sys.service.SysUserService;


@Service("goodsHospitalConsumablesService")
public class GoodsHospitalConsumablesServiceImpl extends ServiceImpl<GoodsHospitalConsumablesDao, GoodsHospitalConsumablesEntity> implements GoodsHospitalConsumablesService {

	@Autowired
	private GoodsHospitalConsumablesSpecsService goodsHospitalConsumablesSpecsService;

	@Autowired
	private GoodsHospitalConsumablesApprovalsService goodsHospitalConsumablesApprovalsService;

	@Autowired
	private OrgFactoryInfoService orgFactoryInfoService;

	@Autowired
	private OrgFactoryInfoApprovalService orgFactoryInfoApprovalService;

	@Autowired
	private SysSequenceService sysSequenceService;
	
	@Autowired
	private SysUserService sysUserService;
	
    @Override
    @DataFilter(subDept = true, user = false, tableAlias = "c")
    public PageUtils queryPage(Map<String, Object> params) {
		int currPage = Integer.parseInt(params.get("page").toString());
		int pageSize = Integer.parseInt(params.get("limit").toString());

		Page<GoodsHospitalConsumablesEntityVo> page = new Page<GoodsHospitalConsumablesEntityVo>(currPage, pageSize);

		List<GoodsHospitalConsumablesEntityVo> list = this.baseMapper.selectHospitalConsumablesList(page, params);

		for(GoodsHospitalConsumablesEntityVo goodsHospitalConsumables : list) {
			List<GoodsHospitalConsumablesSpecsEntity> specsList = goodsHospitalConsumablesSpecsService.selectListByConsumablesId(goodsHospitalConsumables.getId());
			goodsHospitalConsumables.setSpecsEntityList(specsList);
			List<GoodsHospitalConsumablesApprovalsEntity> approvalsList = goodsHospitalConsumablesApprovalsService.selectListByConsumablesId(goodsHospitalConsumables.getId());
			goodsHospitalConsumables.setApprovalsEntityList(approvalsList);
		}
		
		page.setRecords(list);

		return new PageUtils(page);
    }

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(GoodsHospitalConsumablesEntityVo goodsHospitalConsumablesVo) {
		// 判断耗材名称是否存在
		GoodsHospitalConsumablesEntity consumablesName = this.baseMapper
				.selectByHospitalIdAndConsumablesName(goodsHospitalConsumablesVo.getHospitalId(), goodsHospitalConsumablesVo.getConsumablesName());
		if (consumablesName != null && !consumablesName.getId().equals(goodsHospitalConsumablesVo.getId())) {
			throw new HdiException("耗材名称：" + goodsHospitalConsumablesVo.getConsumablesName() + "，已存在！");
		}

		// 判断批准文号是否已经存在
		List<GoodsHospitalConsumablesApprovalsEntity> approvalsList = goodsHospitalConsumablesVo.getApprovalsEntityList();
		if (!StringUtil.isEmpty(approvalsList)) {
			for (GoodsHospitalConsumablesApprovalsEntity approvals : approvalsList) {
				// 判断是否有id，有id则修改，无id则新增
				if (approvals.getId() != null) {
					GoodsHospitalConsumablesApprovalsEntity goodsHospitalConsumablesApprovals = goodsHospitalConsumablesApprovalsService
							.selectByApprovals(goodsHospitalConsumablesVo.getId(), approvals.getApprovals());
					if (goodsHospitalConsumablesApprovals != null
							&& !goodsHospitalConsumablesApprovals.getId().equals(approvals.getId())
							&& StatusEnum.USABLE.getKey().equals(goodsHospitalConsumablesApprovals.getStatus())
							) {
						// 传入的批准文号在表中存在，且id不一致，且为启用状态
						throw new HdiException("批准文号：" + approvals.getApprovals() + "，已存在！");
					}
					approvals.setConsumablesId(goodsHospitalConsumablesVo.getId());
					approvals.setEditId(goodsHospitalConsumablesVo.getEditId());
					approvals.setEditTime(new Date());
					goodsHospitalConsumablesApprovalsService.updateById(approvals);
				} else {
					GoodsHospitalConsumablesApprovalsEntity goodsHospitalConsumablesApprovals = goodsHospitalConsumablesApprovalsService
							.selectByApprovals(goodsHospitalConsumablesVo.getId(), approvals.getApprovals());
					if (goodsHospitalConsumablesApprovals != null) {
						if(StatusEnum.USABLE.getKey().equals(goodsHospitalConsumablesApprovals.getStatus())) {
							throw new HdiException("批准文号：" + approvals.getApprovals() + "，已存在！");
						} else {
							goodsHospitalConsumablesApprovals.setStatus(StatusEnum.USABLE.getKey());
							goodsHospitalConsumablesApprovalsService.updateById(goodsHospitalConsumablesApprovals);
						}
					} else {
						approvals.setConsumablesId(goodsHospitalConsumablesVo.getId());
						approvals.setCreateId(goodsHospitalConsumablesVo.getEditId());
						approvals.setCreateTime(new Date());
						goodsHospitalConsumablesApprovalsService.insert(approvals);
					}
				}
			}
		}

		// 生产厂商id为空则保存厂商信息
		if (StringUtil.isEmpty(goodsHospitalConsumablesVo.getFactoryId())) {
			// 保存厂商信息，状态为待审批
			OrgFactoryInfoApprovalEntity orgFactoryEntity = new OrgFactoryInfoApprovalEntity();
			String FactoryCode = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.FACTORY_CODE.getKey());
			orgFactoryEntity.setFactoryCode(FactoryCode);
			if (StringUtils.isBlank(goodsHospitalConsumablesVo.getFactoryName())) {
				throw new HdiException("厂商名称不能为空");
			}
			
			if(orgFactoryInfoApprovalService.selectList(new EntityWrapper<OrgFactoryInfoApprovalEntity>()
					.eq("del_flag", DelFlagEnum.NORMAL.getKey())
					.eq("factory_name", goodsHospitalConsumablesVo.getFactoryName())).size()>0) {
				throw new HdiException("厂商名称已存在");
			}
			
			orgFactoryEntity.setFactoryName(goodsHospitalConsumablesVo.getFactoryName());
			orgFactoryEntity.setStatus(FactoryStatusEnum.DRAFT.getKey());
			orgFactoryEntity.setCreateId(goodsHospitalConsumablesVo.getCreateId());
			orgFactoryEntity.setCreateTime(new Date());
			SysUserEntity user = sysUserService.selectById(goodsHospitalConsumablesVo.getEditId());
			if(user != null && TypeEnum.USER_PLATFORM.getKey().equals(user.getUserType())) {
				//平台用户录入，厂家信息属于该用户
				orgFactoryEntity.setDeptId(user.getDeptId());
			}
			orgFactoryInfoApprovalService.insert(orgFactoryEntity);
			goodsHospitalConsumablesVo.setFactoryId(String.valueOf(orgFactoryEntity.getId()));
		}

		//设置未匹对(0:未匹对;1:已匹对)
		goodsHospitalConsumablesVo.setIsMatch(0);
		
		goodsHospitalConsumablesVo.setEditTime(new Date());

		this.baseMapper.updateById(goodsHospitalConsumablesVo);
		
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(GoodsHospitalConsumablesEntityVo goodsHospitalConsumablesVo) {
		// 判断耗材名称是否存在
		GoodsHospitalConsumablesEntity consumablesName = this.baseMapper
				.selectByHospitalIdAndConsumablesName(goodsHospitalConsumablesVo.getHospitalId(), goodsHospitalConsumablesVo.getConsumablesName());
		if (consumablesName != null && !consumablesName.getId().equals(goodsHospitalConsumablesVo.getId())) {
			throw new HdiException("耗材名称：" + goodsHospitalConsumablesVo.getConsumablesName() + "，已存在！");
		}

		// 生产厂商id为空则保存厂商信息
		if (StringUtil.isEmpty(goodsHospitalConsumablesVo.getFactoryId())) {
			// 保存厂商信息，状态为待审批
			OrgFactoryInfoApprovalEntity orgFactoryEntity = new OrgFactoryInfoApprovalEntity();
			String FactoryCode = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.FACTORY_CODE.getKey());
			orgFactoryEntity.setFactoryCode(FactoryCode);
			if (StringUtils.isBlank(goodsHospitalConsumablesVo.getFactoryName())) {
				throw new HdiException("厂商名称不能为空");
			}
			
			if(orgFactoryInfoApprovalService.selectList(new EntityWrapper<OrgFactoryInfoApprovalEntity>()
					.eq("del_flag", DelFlagEnum.NORMAL.getKey())
					.eq("factory_name", goodsHospitalConsumablesVo.getFactoryName())).size()>0) {
				throw new HdiException("厂商名称已存在");
			}
			
			orgFactoryEntity.setFactoryName(goodsHospitalConsumablesVo.getFactoryName());
			orgFactoryEntity.setStatus(FactoryStatusEnum.DRAFT.getKey());
			orgFactoryEntity.setCreateId(goodsHospitalConsumablesVo.getCreateId());
			orgFactoryEntity.setCreateTime(new Date());
			SysUserEntity user = sysUserService.selectById(goodsHospitalConsumablesVo.getCreateId());
			if(user != null && TypeEnum.USER_PLATFORM.getKey().equals(user.getUserType())) {
				//平台用户录入，厂家信息属于该用户
				orgFactoryEntity.setDeptId(user.getDeptId());
			}
			orgFactoryInfoApprovalService.insert(orgFactoryEntity);
			goodsHospitalConsumablesVo.setFactoryId(String.valueOf(orgFactoryEntity.getId()));
		}
		
		//生成耗材编码
		String consumablesCode = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.HOSPITAL_CONSUMABLES_CODE.getKey());
		goodsHospitalConsumablesVo.setConsumablesCode(consumablesCode);

		//设置未匹对(0:未匹对;1:已匹对)
		goodsHospitalConsumablesVo.setIsMatch(0);

		// 保存耗材信息
		if(ConsumablesTypeEnum.ORTHOPEDICS_CONSUMABLES.getValue().equals(goodsHospitalConsumablesVo.getTypeName())) {
			goodsHospitalConsumablesVo.setTypeId(ConsumablesTypeEnum.ORTHOPEDICS_CONSUMABLES.getKey());
		}
		goodsHospitalConsumablesVo.setDataSource(HospitalDataSource.SYSTEM.getKey());
		goodsHospitalConsumablesVo.setCreateTime(new Date());
		Map<String, Object> hospitalInfo = this.baseMapper.selectHospitalInfoByHospitalId(goodsHospitalConsumablesVo.getHospitalId());
		if(StringUtil.isEmpty(hospitalInfo)) {
			throw new HdiException("医院不存在！");
		}
		goodsHospitalConsumablesVo.setDeptId((Long)hospitalInfo.get("dept_id"));
		this.baseMapper.insert(goodsHospitalConsumablesVo);

		// 普通耗材保存商品规格
		if(!ConsumablesTypeEnum.ORTHOPEDICS_CONSUMABLES.getValue().equals(goodsHospitalConsumablesVo.getTypeName())) {

			goodsHospitalConsumablesSpecsService.save(goodsHospitalConsumablesVo);
		}

		// 保存批准文号信息
		goodsHospitalConsumablesApprovalsService.saveBatch(goodsHospitalConsumablesVo);
		
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long[] ids) {
		for(Long id : ids) {
			GoodsHospitalConsumablesEntity goodsHospitalConsumables = new GoodsHospitalConsumablesEntity();
			goodsHospitalConsumables.setId(id);
			goodsHospitalConsumables.setDelFlag(DelFlagEnum.DELETE.getKey());
			this.baseMapper.updateById(goodsHospitalConsumables);
		}
		
	}

	@Override
	public GoodsHospitalConsumablesEntityVo selectHospitalConsumablesById(Long id) {
		return this.baseMapper.selectHospitalConsumablesById(id);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void toggle(Map<String, Object> params) {
		List<Long> ids = JSON.parseArray(params.get("ids").toString(), Long.class);
		for(Long id : ids) {
			GoodsHospitalConsumablesEntity goodsHospitalConsumables = new GoodsHospitalConsumablesEntity();
			goodsHospitalConsumables.setId(id);
			goodsHospitalConsumables.setStatus(Integer.valueOf(params.get("status").toString()));
			this.baseMapper.updateById(goodsHospitalConsumables);
		}
	}
	
	
	
	//定时任务
	@Override
	public void updateIsMatch(Long goodsId) {
		GoodsHospitalConsumablesEntity hospitalConsumablesEntity = new GoodsHospitalConsumablesEntity();
		hospitalConsumablesEntity.setId(goodsId);
		hospitalConsumablesEntity.setIsMatch(1);
		baseMapper.updateById(hospitalConsumablesEntity);
		
	}

	@Override
	public GoodsHospitalConsumablesEntity selectByGoodsNameAndFactoryNameAndHospitalId(String goodsName, String factoryName, Long hospitalId) {
		return baseMapper.selectByGoodsNameAndFactoryNameAndHospitalId(goodsName,factoryName, hospitalId);
	}
	
	
	//HDI转换用
	@Override
	public Map<String, Object> selectBySourcesId(String sourcesSpecsId) {
		Map<String, Object> map = this.baseMapper.selectBySourcesId(sourcesSpecsId);
		return map;
	}
}
