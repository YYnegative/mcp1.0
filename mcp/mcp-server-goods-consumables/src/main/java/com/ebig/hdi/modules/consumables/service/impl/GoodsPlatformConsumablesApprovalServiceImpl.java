package com.ebig.hdi.modules.consumables.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.constant.ActivitiConstant;
import com.ebig.hdi.common.entity.OrgFactoryInfoEntity;
import com.ebig.hdi.common.enums.*;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.activiti.entity.ActApprovalEntity;
import com.ebig.hdi.modules.activiti.service.ActApprovalService;
import com.ebig.hdi.modules.consumables.dao.GoodsPlatformConsumablesApprovalDao;
import com.ebig.hdi.modules.consumables.entity.*;
import com.ebig.hdi.modules.consumables.param.GoodsPlatformConsumablesApprovalParam;
import com.ebig.hdi.modules.consumables.service.*;
import com.ebig.hdi.modules.consumables.vo.GoodsPlatformConsumablesEntityVo;
import com.ebig.hdi.modules.core.service.OrgFactoryInfoService;
import com.ebig.hdi.modules.sys.entity.SysDictEntity;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import com.ebig.hdi.modules.sys.service.SysDictService;
import com.ebig.hdi.modules.sys.service.SysSequenceService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service("goodsPlatformConsumablesApprovalService")
public class  GoodsPlatformConsumablesApprovalServiceImpl
		extends ServiceImpl<GoodsPlatformConsumablesApprovalDao, GoodsPlatformConsumablesApprovalEntity>
		implements GoodsPlatformConsumablesApprovalService {

	@Autowired
	private GoodsPlatformConsumablesSpecsService goodsPlatformConsumablesSpecsService;

	@Autowired
	private GoodsPlatformConsumablesApprovalsService goodsPlatformConsumablesApprovalsService;

	@Autowired
	private GoodsPlatformConsumablesService goodsPlatformConsumablesService;

	@Autowired
	private OrgFactoryInfoService orgFactoryInfoService;

	@Autowired
	private SysSequenceService sysSequenceService;

	@Autowired
	private UnicodeConsumablesCateService unicodeConsumablesCateService;

	@Autowired
	private ActApprovalService actApprovalService;

	@Autowired
	private SysDictService sysDictService;

	@Override
    @DataFilter(subDept = true, user = false, tableAlias = "c")
	public PageUtils queryPage(Map<String, Object> params) {
		int currPage = Integer.parseInt(params.get("page").toString());
		int pageSize = Integer.parseInt(params.get("limit").toString());

		Page<GoodsPlatformConsumablesEntityVo> page = new Page<>(currPage, pageSize);

		if (params != null && params.get("typeId") != null){
			List<Long> cateIds = new ArrayList<>();
			cateIds.add(Long.parseLong(params.get("typeId").toString()));
			unicodeConsumablesCateService.queryList(Long.valueOf(params.get("typeId").toString()),cateIds);
			
			params.put("typeId",cateIds);
		}
		List<GoodsPlatformConsumablesEntityVo> list = this.baseMapper.selectPlatformConsumablesList(page, params);

		for (GoodsPlatformConsumablesEntityVo goodsPlatformConsumables : list) {
			List<GoodsPlatformConsumablesSpecsEntity> specsList = goodsPlatformConsumablesSpecsService
					.selectListByConsumablesId(goodsPlatformConsumables.getId());
			goodsPlatformConsumables.setSpecsEntityList(specsList);
			List<GoodsPlatformConsumablesApprovalsEntity> approvalsList = goodsPlatformConsumablesApprovalsService
					.selectListByConsumablesId(goodsPlatformConsumables.getId());
			goodsPlatformConsumables.setApprovalsEntityList(approvalsList);
		}

		page.setRecords(list);

		return new PageUtils(page);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> save(GoodsPlatformConsumablesEntityVo goodsPlatformConsumablesVo, SysUserEntity user) {
		Map<String, Object> map = new HashMap<>(16);
		if (StringUtil.isEmpty(goodsPlatformConsumablesVo.getSpecs())) {
			map.put("errmessage", "请输入商品规格！");
			return map;
		}
		if (StringUtil.isEmpty(goodsPlatformConsumablesVo.getApprovals())) {
			map.put("errmessage", "请输入批准文号！");
			return map;
		}
		if (StringUtil.isEmpty(goodsPlatformConsumablesVo.getFactoryId())) {
			map.put("errmessage", "本生产厂商未建档，请先建档！");
			return map;
		}
		
		// 判断耗材名称是否存在
		GoodsPlatformConsumablesApprovalEntity consumablesName = this.baseMapper
				.selectByConsumablesName(goodsPlatformConsumablesVo.getConsumablesName());
		if (consumablesName != null) {
			map.put("errmessage", "耗材名称：" + goodsPlatformConsumablesVo.getConsumablesName() + "，已存在！");
			return map;
		}
		// 生成耗材编码（商品码）
		String consumablesCode = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.PLATFORM_CONSUMABLES_CODE.getKey());
		goodsPlatformConsumablesVo.setConsumablesCode(consumablesCode);

		String factoryCode = orgFactoryInfoService.selectById(goodsPlatformConsumablesVo.getFactoryId()).getFactoryCode();
		// 生成商品统一编码
		String goodsUnicode = unicodeConsumablesCateService.generatorGoodsUnicode(goodsPlatformConsumablesVo.getGoodsNature(), goodsPlatformConsumablesVo.getTypeId(), consumablesCode, factoryCode);

		goodsPlatformConsumablesVo.setGoodsUnicode(goodsUnicode);
		goodsPlatformConsumablesVo.setCreateTime(new Date());
		goodsPlatformConsumablesVo.setEditTime(new Date());
		goodsPlatformConsumablesVo.setCheckStatus(ApprovalTypeEnum.WAIT.getKey());
		goodsPlatformConsumablesVo.setDelFlag(DelFlagEnum.NORMAL.getKey());
		// 保存耗材信息
		this.baseMapper.insert(goodsPlatformConsumablesVo);
		// 保存商品规格
		Map<String, Object> saveMap = goodsPlatformConsumablesSpecsService.save(goodsPlatformConsumablesVo);
		if (!saveMap.isEmpty()){
			map.put("errmessage","全球唯一码：" + goodsPlatformConsumablesVo.getGuid() + "，已存在！");
			return map;
		}
		// 保存批准文号信息
		goodsPlatformConsumablesApprovalsService.saveBatch(goodsPlatformConsumablesVo);

		//发起审批
	    startApproval(goodsPlatformConsumablesVo,user);
		return map;

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(GoodsPlatformConsumablesEntityVo goodsPlatformConsumablesVo,SysUserEntity entity) {
		if (StringUtil.isEmpty(goodsPlatformConsumablesVo.getFactoryId())) {
			throw new HdiException("本生产厂商未建档，请先建档");
		}
		
		// 判断耗材名称是否存在
		GoodsPlatformConsumablesApprovalEntity consumablesName = this.baseMapper
				.selectByConsumablesName(goodsPlatformConsumablesVo.getConsumablesName());
		if (consumablesName != null && !consumablesName.getId().equals(goodsPlatformConsumablesVo.getId())) {
			throw new HdiException("耗材名称：" + goodsPlatformConsumablesVo.getConsumablesName() + "，已存在！");
		}

		// 判断批准文号是否已经存在（药品下规格唯一）
		List<GoodsPlatformConsumablesApprovalsEntity> approvalsList = goodsPlatformConsumablesVo.getApprovalsEntityList();
		List<GoodsPlatformConsumablesApprovalsEntity> allApprovals =this.baseMapper.selectApprovalsEntity();
		for (GoodsPlatformConsumablesApprovalsEntity approvalsEntity : allApprovals) {
			for (GoodsPlatformConsumablesApprovalsEntity goodsPlatformConsumablesApprovalsEntity : approvalsList) {
				if (approvalsEntity.getApprovals().equals(goodsPlatformConsumablesApprovalsEntity.getApprovals())&&!approvalsEntity.getId().equals(goodsPlatformConsumablesApprovalsEntity.getId())){
					throw new HdiException("批准文号：" + goodsPlatformConsumablesApprovalsEntity.getApprovals() + "，已存在！");
				}
			}
		}
		if (!StringUtil.isEmpty(approvalsList)) {
			for (GoodsPlatformConsumablesApprovalsEntity approvals : approvalsList) {
				// 判断是否有id，有id则修改，无id则新增
				if (approvals.getId() != null) {
					GoodsPlatformConsumablesApprovalsEntity goodsPlatformConsumablesApprovals = goodsPlatformConsumablesApprovalsService
							.selectByApprovals(goodsPlatformConsumablesVo.getId(), approvals.getApprovals());
					if (goodsPlatformConsumablesApprovals != null
							&& !goodsPlatformConsumablesApprovals.getId().equals(approvals.getId())
							&& StatusEnum.USABLE.getKey().equals(goodsPlatformConsumablesApprovals.getStatus())
							) {
						// 传入的批准文号在表中存在，且id不一致
						throw new HdiException("批准文号：" + approvals.getApprovals() + "，已存在！");
					}
					approvals.setConsumablesId(goodsPlatformConsumablesVo.getId());
					approvals.setEditId(goodsPlatformConsumablesVo.getEditId());
					approvals.setEditTime(new Date());
					goodsPlatformConsumablesApprovalsService.updateById(approvals);
				} else {

					GoodsPlatformConsumablesApprovalsEntity goodsPlatformDrugsApprovals = goodsPlatformConsumablesApprovalsService
							.selectByApprovals(goodsPlatformConsumablesVo.getId(), approvals.getApprovals());
					if (goodsPlatformDrugsApprovals != null) {
						if(StatusEnum.USABLE.getKey().equals(goodsPlatformDrugsApprovals.getStatus())) {
							throw new HdiException("批准文号：" + approvals.getApprovals() + "，已存在！");
						} else {
							goodsPlatformDrugsApprovals.setStatus(StatusEnum.USABLE.getKey());
							goodsPlatformConsumablesApprovalsService.updateById(goodsPlatformDrugsApprovals);
						}
					} else {
						approvals.setConsumablesId(goodsPlatformConsumablesVo.getId());
						approvals.setCreateId(goodsPlatformConsumablesVo.getEditId());
						approvals.setCreateTime(new Date());
						goodsPlatformConsumablesApprovalsService.insert(approvals);
					}
				}
			}
		}


		// 生成商品统一编码
		String factoryCode = orgFactoryInfoService.selectById(goodsPlatformConsumablesVo.getFactoryId()).getFactoryCode();
		String goodsUnicode = unicodeConsumablesCateService.generatorGoodsUnicode(goodsPlatformConsumablesVo.getGoodsNature(), goodsPlatformConsumablesVo.getTypeId(), goodsPlatformConsumablesVo.getConsumablesCode(), factoryCode);
		goodsPlatformConsumablesVo.setGoodsUnicode(goodsUnicode);

		goodsPlatformConsumablesVo.setEditTime(new Date());
		//如果审批状态为审批不通过，或者待审批，发起审批流
		 if(!goodsPlatformConsumablesVo.getCheckStatus().equals(ApprovalTypeEnum.PASS.getKey())){
			 //发起审批
			 goodsPlatformConsumablesVo.setCheckStatus(ApprovalTypeEnum.WAIT.getKey());
			 ActApprovalEntity approvalEntity = actApprovalService.getActApprovalEntity(entity, ChangeTypeEnum.UPDATE.getKey(),
					 ActTypeEnum.PLATFORM_CONSUMABLES.getKey(), goodsPlatformConsumablesVo.getId().toString(),
					 goodsPlatformConsumablesVo.getConsumablesCode(), goodsPlatformConsumablesVo.getConsumablesName());
			 Map<String, Object> approvalMap = new HashMap<>(16);
			 approvalMap.put("approvalEntity", approvalEntity);
			 actApprovalService.insert(approvalEntity);
			 ProcessInstance instance = actApprovalService.startProcess(entity.getUserId().toString(),
					 ActivitiConstant.ACTIVITI_PLATFORM_CONSUMABLES[0], ActivitiConstant.ACTIVITI_PLATFORM_CONSUMABLES[1], approvalMap);
			 if (instance!=null){
				 approvalEntity.setProcessId(instance.getProcessInstanceId());
				 approvalEntity.setApprovalCode(instance.getProcessInstanceId());
				 goodsPlatformConsumablesVo.setProcessId(instance.getProcessInstanceId());
			 }
			 this.baseMapper.updateById(goodsPlatformConsumablesVo);
			 actApprovalService.updateById(approvalEntity);
		 }else {
		 	//审批通过修改两张表
			 this.baseMapper.updateById(goodsPlatformConsumablesVo);
			 goodsPlatformConsumablesService.updateByGoodsDrugsApprovalEntity(goodsPlatformConsumablesVo);
		 }
	}

	public void startApproval(GoodsPlatformConsumablesEntityVo goodsPlatformConsumablesVo, SysUserEntity user) {
		ActApprovalEntity approvalEntity = actApprovalService.getActApprovalEntity(user, ChangeTypeEnum.ADD.getKey(),
				ActTypeEnum.PLATFORM_CONSUMABLES.getKey(), goodsPlatformConsumablesVo.getId().toString(),
				goodsPlatformConsumablesVo.getConsumablesCode(), goodsPlatformConsumablesVo.getConsumablesName());
		Map<String, Object> map = new HashMap<>(16);
		map.put("approvalEntity", approvalEntity);
		actApprovalService.insert(approvalEntity);
		ProcessInstance instance = actApprovalService.startProcess(user.getUserId().toString(),
				ActivitiConstant.ACTIVITI_PLATFORM_CONSUMABLES[0], ActivitiConstant.ACTIVITI_PLATFORM_CONSUMABLES[1], map);
		if (instance != null) {
			approvalEntity.setProcessId(instance.getProcessInstanceId());
			approvalEntity.setApprovalCode(instance.getProcessInstanceId());
			goodsPlatformConsumablesVo.setProcessId(instance.getProcessInstanceId());
		}
		goodsPlatformConsumablesVo.setCheckStatus(ApprovalTypeEnum.WAIT.getKey());
		this.baseMapper.updateById(goodsPlatformConsumablesVo);
		// 更新审批流

		actApprovalService.updateById(approvalEntity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long[] ids) {
		for(Long id : ids) {
			GoodsPlatformConsumablesApprovalEntity goodsPlatformConsumables = this.baseMapper.selectById(id);
			if (goodsPlatformConsumables.getCheckStatus().equals(ApprovalTypeEnum.PASS.getKey())){
				throw new HdiException("审批通过的商品不能被删除！！！");
			}
			goodsPlatformConsumables.setDelFlag(DelFlagEnum.DELETE.getKey());
			this.baseMapper.updateById(goodsPlatformConsumables);
		}

	}

	@Override
	public GoodsPlatformConsumablesApprovalEntity selectPlatformConsumablesById(Long id) {
		return this.baseMapper.selectPlatformConsumablesById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void toggle(Map<String, Object> params) {	
		List<Long> ids = JSON.parseArray(params.get("ids").toString(), Long.class);
		Integer status = Integer.valueOf(params.get("status").toString());
		for(Long id : ids) {
			//查待审批表记录
			GoodsPlatformConsumablesApprovalEntity goodsPlatformConsumablesApprovalEntity = this.baseMapper.selectById(id);
			goodsPlatformConsumablesApprovalEntity.setStatus(status);
			//查原表记录
			List<GoodsPlatformConsumablesEntity> list = this.baseMapper.selectGoodsPlatformConsumablesById(id);
			//如果原表中有数据
			if (CollectionUtils.isNotEmpty(list)){
				//如果原表有数据,修改原表状态
				this.baseMapper.updateGoodsPlatformConsumablesById(id,status);
			}
			//修改待审批表状态
			this.baseMapper.updateById(goodsPlatformConsumablesApprovalEntity);

		}
	}

	@Override
	@DataFilter(subDept = true, user = false, tableAlias = "c")
	public List<Map<String, Object>> getList(Map map) {
		List<UnicodeConsumablesCateEntity> unicodeConsumablesCateMapList=unicodeConsumablesCateService.selectAll();
		HashMap<Long, String> hashMap = unicodeConsumablesCateMapList.stream().collect(Collectors.toMap(UnicodeConsumablesCateEntity::getCateId, UnicodeConsumablesCateEntity::getCateName, (key1, key2) -> key2, HashMap::new));
		//将params拿出来,根据父id查子id存到typeIds里面
		GoodsPlatformConsumablesApprovalParam params = (GoodsPlatformConsumablesApprovalParam) map.get("queryParam");
		if (params != null && params.getTypeId() != null){
			List<Long> cateIds = new ArrayList<>();
			cateIds.add(Long.parseLong(params.getTypeId().toString()));
			unicodeConsumablesCateService.queryList(Long.valueOf(params.getTypeId().toString()),cateIds);
			params.setTypeIds(cateIds);
		}
		List<Map<String, Object>> list = this.baseMapper.getList(map);
		for (Map<String, Object> objectMap : list) {
			long typeId = Long.parseLong(objectMap.get("type_id").toString());
			String s = hashMap.get(typeId);
			objectMap.put("type_id",s);
		}
		return list;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> checkStatus(Map<String, Object> params, SysUserEntity user) {
		Integer checkStatus = (Integer) params.get("checkStatus");
		Map<String, Object> map = new HashMap<>(16);
		if (StringUtil.isEmpty(checkStatus)) {
			map.put("errorMessage", "状态为空");
			return map;
		}
		Integer goodsId = (Integer) params.get("goodsId");
		if (StringUtil.isEmpty(goodsId)) {
			map.put("errorMessage", "传入ID为空");
			return map;
		}
		if (!user.getUserType().equals(TypeEnum.USER_PLATFORM.getKey())){
			map.put("errorMessage", "非平台用户无平台商品审批权限");
			return map;
		}
		GoodsPlatformConsumablesApprovalEntity goodsPlatformConsumablesApprovalEntity = this.baseMapper.selectById(goodsId);
		if (checkStatus.equals(ApprovalTypeEnum.PASS.getKey())) {
			//审核通过（先修改待审批表）
			goodsPlatformConsumablesApprovalEntity.setCheckStatus(ApprovalTypeEnum.PASS.getKey());
			goodsPlatformConsumablesApprovalEntity.setEditTime(new Date());
			goodsPlatformConsumablesApprovalEntity.setEditId(user.getUserId());
			this.updateById(goodsPlatformConsumablesApprovalEntity);
			//根据耗材编码查询原表是否有数据
			List<GoodsPlatformConsumablesEntity> list=this.baseMapper.selectListByPlatformConsumablesCode(goodsPlatformConsumablesApprovalEntity.getConsumablesCode());
			//表中没数据则新增
			if (CollectionUtils.isEmpty(list)) {
				goodsPlatformConsumablesService.insertByFactoryInfoApproval(goodsPlatformConsumablesApprovalEntity);
			} else {
				//表中有数据则更新
				goodsPlatformConsumablesService.updateByFactoryInfoApproval(goodsPlatformConsumablesApprovalEntity);
			}
		}
		if (checkStatus.equals(ApprovalTypeEnum.FAIL.getKey())) {
			//审核不通过(修改待审批平台耗材表审批状态)
			goodsPlatformConsumablesApprovalEntity.setCheckStatus(ApprovalTypeEnum.FAIL.getKey());
			goodsPlatformConsumablesApprovalEntity.setEditTime(new Date());
			goodsPlatformConsumablesApprovalEntity.setEditId(user.getUserId());
			this.baseMapper.updateById(goodsPlatformConsumablesApprovalEntity);
		}
		return map;
	}

	@Transactional(rollbackFor = Exception.class)
	public String checkGoodsUnit(String srcGoodsUnit) {
		String goodsUnit = null;
		if (!StringUtil.isEmpty(srcGoodsUnit)) {
			List<SysDictEntity> dicts = sysDictService.selectList(new EntityWrapper<SysDictEntity>().eq("value", srcGoodsUnit));
			if (StringUtil.isEmpty(dicts)) {
				SysDictEntity dict = new SysDictEntity();
				dict.setName("商品单位");
				dict.setType("goods_unit");
				dict.setCode(sysSequenceService.selectSeqValueBySeqCode("GOODS_UNIT_CODE"));
				dict.setValue(srcGoodsUnit);
				sysDictService.insert(dict);
				goodsUnit = dict.getCode();
			} else {
				goodsUnit = dicts.get(0).getCode();
			}
		}
		return goodsUnit;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Map importData(String[][] rows, SysUserEntity sysUserEntity) {
		if (sysUserEntity == null) {
			throw new HdiException("导入失败，请先登录！！！");
		}
		if (!sysUserEntity.getUserType().equals(TypeEnum.USER_PLATFORM.getKey())) {
			throw new HdiException("当前登录用户无此权限");
		}
		StringBuilder sb = new StringBuilder();
		//储存方式map（用于前端value转code）
		List<SysDictEntity> storeWayList = sysDictService.selectDictByType("store_way");
		Map<String, String> storeMap = storeWayList.stream().collect(Collectors.toMap(SysDictEntity::getValue, SysDictEntity::getCode, (key1, key2) -> key2, HashMap::new));
		//查出所有厂商,转map
		List<OrgFactoryInfoEntity> factoryList = this.baseMapper.selectFactoryList();
		Map<String, Long> factoryMap = factoryList.stream().collect(Collectors.toMap(OrgFactoryInfoEntity::getFactoryName, OrgFactoryInfoEntity::getId, (key1, key2) -> key2, HashMap::new));
		//查出所有商品分类
		List<UnicodeConsumablesCateEntity> unicodeConsumablesCateEntityList = this.baseMapper.selectCate();
		Map<String, Long> consumablesCateMap = unicodeConsumablesCateEntityList.stream().collect(Collectors.toMap(UnicodeConsumablesCateEntity::getCateName, UnicodeConsumablesCateEntity::getCateId, (key1, key2) -> key2, HashMap::new));
		//商品属性map
		Map<String, Integer> goodsNatureMap = new HashMap<>();
		goodsNatureMap.put("国产", 0);
		goodsNatureMap.put("进口", 1);
		//保存返回信息
		String key = "successCount";
		Map<String, Object> map = new HashMap<>(16);
		if (sysUserEntity == null || rows.length <= 1) {
			map.put(key, "0");
			map.put("failCount", String.valueOf(rows.length - 1));
			sb.append("系统错误");
			map.put("errorMessage", sb.toString());
			return map;
		}
		Integer failCount = 0;
		Integer successCount = 0;
		//Map<String, Long> idMap = new HashMap<>();
		for (int i = 1; i < rows.length; i++) {
			GoodsPlatformConsumablesEntityVo goodsPlatformConsumablesEntityVo = new GoodsPlatformConsumablesEntityVo();
			String[] row = rows[i];
			if (StringUtil.isEmpty(row[0]) || StringUtil.isEmpty(row[1]) || StringUtil.isEmpty(row[2]) || StringUtil.isEmpty(row[3]) || StringUtil.isEmpty(row[6]) || StringUtil.isEmpty(row[7]) || StringUtil.isEmpty(row[8])) {
				sb.append("\n");
				sb.append("第 " + (i+2) + " 行数据错误，必填字段不能为空");
				failCount++;
				continue;
			}
			// 校验 名称 + 批准文号
			GoodsPlatformConsumablesEntityVo goodsConsumables = this.baseMapper.selectByConsumablesNameAndApprovals(row[0].trim(), row[6].trim());
			if (!StringUtil.isEmpty(goodsConsumables)) {
				// 校验规格是否存在
				GoodsPlatformConsumablesSpecsEntity goodsPlatformConsumablesSpecsEntity = goodsPlatformConsumablesSpecsService.selectByConsumablesIdAndSpecs(goodsConsumables.getId(), row[3].trim());
				if (StringUtil.isEmpty(goodsPlatformConsumablesSpecsEntity)) {
					goodsConsumables.setSpecs(row[3].trim());
					goodsConsumables.setGuid(row[9].trim());
					goodsConsumables.setFactoryName(row[2].trim());
					goodsConsumables.setTypeName(row[8].trim());
					//goodsConsumables.setId(idMap.get(row[0].trim()+row[6].trim()));
					goodsConsumables.setCreateId(sysUserEntity.getUserId());
					goodsPlatformConsumablesSpecsService.save(goodsConsumables);
					successCount++;
						continue;
				}else {
					successCount++;
					continue;
				}
			} else {
				//校验 名称 或 批准文号
				Integer errorApprovalsFlag = this.baseMapper.selectByGoodsNameOrApporvaols(row[0].trim(), row[6].trim());
				if (errorApprovalsFlag != null) {
					sb.append("\n");
					sb.append("第 ").append(i+2).append("行数据错误，请检查该数据中名称与批准文号是否正确");
					failCount++;
					continue;
				}
			}

				if (!"国产".equals(row[1].trim()) && !"进口".equals(row[1].trim())) {
					sb.append("\n");
					sb.append("第 " + (i+2) + " 行数据错误，商品属性错误！ 商品属性:" + row[1]);
					failCount++;
					continue;
				}
				Long factoryId = factoryMap.get(row[2].trim());
				if (factoryId == null) {
					sb.append("\n");
					sb.append("第 " + (i+2) + " 行数据错误，本生产厂商未建档，请先建档！ 生产厂商:" + row[2]);
					failCount++;
					continue;
				}
				String storeWay = storeMap.get(row[4].trim());
				//如果存储方式不为空，且不匹配
				if (!StringUtil.isEmpty(row[4].trim()) && StringUtil.isEmpty(storeWay)) {
					sb.append("\n");
					sb.append("第 " + (i+2) + " 行数据错误，存储方式错误！ 存储方式:" + row[4]);
					failCount++;
					continue;
				}

				Long typeId = consumablesCateMap.get(row[8].trim());
				if (StringUtil.isEmpty(typeId)) {
					sb.append("\n");
					sb.append("第 " + (i+2) + " 行数据错误，商品分类错误！ 商品分类:" + row[8]);
					failCount++;
					continue;
				}
				// 生成耗材编码（商品码）
				String consumablesCode = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.PLATFORM_CONSUMABLES_CODE.getKey());
				goodsPlatformConsumablesEntityVo.setConsumablesCode(consumablesCode);

				String factoryCode = orgFactoryInfoService.selectById(factoryId).getFactoryCode();
			    goodsPlatformConsumablesEntityVo.setGoodsNature("国产".equals(row[1].trim()) ? 0 : 1);
			    // 生成商品统一编码
				String goodsUnicode = unicodeConsumablesCateService.generatorGoodsUnicode(goodsPlatformConsumablesEntityVo.getGoodsNature(), typeId, consumablesCode, factoryCode);
				goodsPlatformConsumablesEntityVo.setGoodsUnicode(goodsUnicode);
				goodsPlatformConsumablesEntityVo.setConsumablesName(row[0].trim());
				goodsPlatformConsumablesEntityVo.setCommonName(row[5].trim());
				goodsPlatformConsumablesEntityVo.setTypeId(typeId);
				goodsPlatformConsumablesEntityVo.setFactoryId(factoryId != null ? factoryId.toString() : null);
				goodsPlatformConsumablesEntityVo.setStatus(1);
				goodsPlatformConsumablesEntityVo.setGoodsUnit(checkGoodsUnit(row[7].trim()));
				goodsPlatformConsumablesEntityVo.setStoreWay(storeWay);
				goodsPlatformConsumablesEntityVo.setSpecs(row[3].trim());
				goodsPlatformConsumablesEntityVo.setCreateId(sysUserEntity.getUserId());
				goodsPlatformConsumablesEntityVo.setCreateTime(new Date());
				goodsPlatformConsumablesEntityVo.setDeptId(sysUserEntity.getDeptId());
				goodsPlatformConsumablesEntityVo.setGuid(row[9].trim());
				//保存批准文号
				String str = row[6].trim();
				String[] approvals = new String[1];
				approvals[0] = str;
				goodsPlatformConsumablesEntityVo.setApprovals(approvals);
			this.baseMapper.insert(goodsPlatformConsumablesEntityVo);
			//idMap.put(row[0].trim()+row[6].trim(),goodsPlatformConsumablesEntityVo.getId());
			//保存规格
			goodsPlatformConsumablesSpecsService.save(goodsPlatformConsumablesEntityVo);
			//保存批准文号
			goodsPlatformConsumablesApprovalsService.saveBatch(goodsPlatformConsumablesEntityVo);
			startApproval(goodsPlatformConsumablesEntityVo, sysUserEntity);
				successCount++;
		}
			if (failCount > 0) {
				successCount = 0;
				map.put("errorMessage", sb.toString());
				map.put(key, successCount.toString());
				map.put("failCount", failCount.toString());
				return map;
			}
			map.put("errorMessage", sb.toString());
			map.put(key, successCount.toString());
			map.put("failCount", failCount.toString());
			return map;
		}
	}
