package com.ebig.hdi.modules.consumables.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.entity.OrgFactoryInfoApprovalEntity;
import com.ebig.hdi.common.entity.OrgSupplierInfoEntity;
import com.ebig.hdi.common.enums.*;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.Constant;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.consumables.dao.GoodsSupplierConsumablesDao;
import com.ebig.hdi.modules.consumables.entity.GoodsSupplierConsumablesApprovalsEntity;
import com.ebig.hdi.modules.consumables.entity.GoodsSupplierConsumablesEntity;
import com.ebig.hdi.modules.consumables.entity.GoodsSupplierConsumablesSpecsEntity;
import com.ebig.hdi.modules.consumables.entity.vo.GoodsSupplierConsumablesVO;
import com.ebig.hdi.modules.consumables.service.GoodsSupplierConsumablesApprovalsService;
import com.ebig.hdi.modules.consumables.service.GoodsSupplierConsumablesService;
import com.ebig.hdi.modules.consumables.service.GoodsSupplierConsumablesSpecsService;
import com.ebig.hdi.modules.core.service.OrgFactoryInfoApprovalService;
import com.ebig.hdi.modules.core.service.OrgFactoryInfoService;
import com.ebig.hdi.modules.sys.entity.SysDictEntity;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import com.ebig.hdi.modules.sys.service.SysDictService;
import com.ebig.hdi.modules.sys.service.SysSequenceService;
import com.ebig.hdi.modules.sys.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;



@Service("goodsSupplierConsumablesService")
public class GoodsSupplierConsumablesServiceImpl
		extends ServiceImpl<GoodsSupplierConsumablesDao, GoodsSupplierConsumablesEntity>
		implements GoodsSupplierConsumablesService {

	@Autowired
	private OrgFactoryInfoService orgFactoryInfoService;
	@Autowired
	private OrgFactoryInfoApprovalService orgFactoryInfoApprovalService;
	@Autowired
	private SysSequenceService sysSequenceService;
	@Autowired
	private GoodsSupplierConsumablesApprovalsService goodsSupplierConsumablesApprovalsService;
	@Autowired
	private GoodsSupplierConsumablesSpecsService goodsSupplierConsumablesSpecsService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysDictService sysDictService;

	@Override
	@DataFilter(subDept = true, user = false, tableAlias = "t1")
	public PageUtils queryPage(Map<String, Object> params) {

		GoodsSupplierConsumablesVO gscVO = new GoodsSupplierConsumablesVO();
		gscVO.setSupplierName((String) params.get("supplierName"));// 供应商名称
		gscVO.setConsumablesName((String) params.get("consumablesName"));// 耗材名称
		gscVO.setGoodsNature((Integer) params.get("goodsNature"));// 商品属性
		gscVO.setTypeId((String) params.get("typeId"));// 商品分类
		gscVO.setTypeName((String) params.get("typeName"));// 分类名称
		gscVO.setStatus((Integer) params.get("status"));// 状态
		gscVO.setFactoryName((String) params.get("factoryName"));// 生产厂商
		gscVO.setFileterDept((String) params.get(Constant.SQL_FILTER));// 设置权限

		int currPage = Integer.parseInt(params.get("page").toString());
		int pageSize = Integer.parseInt(params.get("limit").toString());

		Page<GoodsSupplierConsumablesVO> page = new Page<GoodsSupplierConsumablesVO>(currPage, pageSize);

		List<GoodsSupplierConsumablesVO> list = this.baseMapper.listForPage(page, gscVO);
		setApprovalsAndSpecs(list);

		page.setRecords(list);
		return new PageUtils(page);
	}

	@Override
	public GoodsSupplierConsumablesVO selectById(Long id) {
		GoodsSupplierConsumablesVO supplierConsumablesVO = baseMapper.selectConsumablesById(id);
		List<GoodsSupplierConsumablesVO> list = new ArrayList<>();
		list.add(supplierConsumablesVO);
		if (supplierConsumablesVO != null) {
			setApprovalsAndSpecs(list);
		}
		return list.get(0);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> save(GoodsSupplierConsumablesVO goodsSupplierConsumablesVO) {
		Map<String, Object> map = new HashMap<>(16);
		//批准文号/注册证号不允许重复
		GoodsSupplierConsumablesApprovalsEntity goodsSupplierConsumablesApprovalsEntity=this.baseMapper.selectByApprovals(goodsSupplierConsumablesVO.getApprovals());
		if (goodsSupplierConsumablesApprovalsEntity!=null){
			map.put("errmessage", "批准文号：" + goodsSupplierConsumablesApprovalsEntity.getApprovals() + "，已存在！");
			return map;
		}
		List<GoodsSupplierConsumablesEntity> consumablesCodeList = this
				.selectList(new EntityWrapper<GoodsSupplierConsumablesEntity>()
						.eq("del_flag", DelFlagEnum.NORMAL.getKey())
						.eq("consumables_code", goodsSupplierConsumablesVO.getConsumablesCode())
						.eq("supplier_id", goodsSupplierConsumablesVO.getSupplierId())
						);
		if (!StringUtil.isEmpty(consumablesCodeList)) {
			map.put("errmessage", "耗材编码：" + goodsSupplierConsumablesVO.getConsumablesName() + "，已存在！");
			return map;
		}
		String factoryId = goodsSupplierConsumablesVO.getFactoryId();
		//导入的时候不走这个判断
		if (StringUtils.isBlank(factoryId)) {
			// 如果厂商id为空，则新加一个厂商信息，状态为待审批
			OrgFactoryInfoApprovalEntity orgFactoryInfoApprovalEntity = new OrgFactoryInfoApprovalEntity();
			if (StringUtils.isBlank(goodsSupplierConsumablesVO.getFactoryName())) {
				throw new HdiException("厂商名称不能为空");
			}

			if (orgFactoryInfoApprovalService.selectList(new EntityWrapper<OrgFactoryInfoApprovalEntity>()
					.eq("del_flag", DelFlagEnum.NORMAL.getKey())
					.eq("factory_name",goodsSupplierConsumablesVO.getFactoryName())).size() > 0) {
				throw new HdiException("厂商名称已存在");
			}

			orgFactoryInfoApprovalEntity.setFactoryName(goodsSupplierConsumablesVO.getFactoryName());
			orgFactoryInfoApprovalEntity.setStatus(FactoryStatusEnum.USABLE.getKey());
			orgFactoryInfoApprovalEntity.setCreateId(goodsSupplierConsumablesVO.getCreateId());
			SysUserEntity user = sysUserService.selectById(goodsSupplierConsumablesVO.getCreateId());
			if (user != null && TypeEnum.USER_PLATFORM.getKey().equals(user.getUserType())) {
				// 平台用户录入，厂家信息属于该用户
				orgFactoryInfoApprovalEntity.setDeptId(user.getDeptId());
			}
			orgFactoryInfoApprovalService.save(orgFactoryInfoApprovalEntity,user);
			factoryId = orgFactoryInfoApprovalEntity.getId().toString();
		}

		String reagentCode = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.SUPPLIER_CONSUMABLES_CODE.getKey());
		goodsSupplierConsumablesVO.setConsumablesCode(reagentCode);
		Map<String, Object> supplierInfo = this.baseMapper
				.selectSupplierInfoBySupplierId(goodsSupplierConsumablesVO.getSupplierId());
		if (StringUtil.isEmpty(supplierInfo)) {
			map.put("errmessage", "供应商不存在！");
			return map;
		}
		goodsSupplierConsumablesVO.setDeptId((Long) supplierInfo.get("dept_id"));
		goodsSupplierConsumablesVO.setIsMatch(0);
		goodsSupplierConsumablesVO.setCreateTime(new Date());
		goodsSupplierConsumablesVO.setEditTime(new Date());
		goodsSupplierConsumablesVO.setFactoryId(factoryId);
		goodsSupplierConsumablesVO.setDataSource(SupplierDataSource.SYSTEM.getKey());
		// 如果是骨科耗材，保存类型为0
		if (ConsumablesTypeEnum.ORTHOPEDICS_CONSUMABLES.getValue().equals(goodsSupplierConsumablesVO.getTypeName())) {
			goodsSupplierConsumablesVO.setTypeId(ConsumablesTypeEnum.ORTHOPEDICS_CONSUMABLES.getKey());
		}
		// 初始为未上传
		goodsSupplierConsumablesVO.setIsUpload(SupplierIsUploadEnum.NO.getKey());
		baseMapper.insert(goodsSupplierConsumablesVO);
		Long currentId = goodsSupplierConsumablesVO.getId();

		// 保存批文信息
		String approvals = goodsSupplierConsumablesVO.getApprovals();
		GoodsSupplierConsumablesApprovalsEntity gscae = new GoodsSupplierConsumablesApprovalsEntity();
			gscae.setConsumablesId(currentId);
			gscae.setApprovals(approvals);
			gscae.setStatus(StatusEnum.USABLE.getKey());
			gscae.setCreateId(goodsSupplierConsumablesVO.getCreateId());
			gscae.setCreateTime(new Date());
			goodsSupplierConsumablesApprovalsService.insert(gscae);


		// 耗材类型为普通耗材时，保存耗材规格
		if (!ConsumablesTypeEnum.ORTHOPEDICS_CONSUMABLES.getValue().equals(goodsSupplierConsumablesVO.getTypeName())) {
			GoodsSupplierConsumablesSpecsEntity gscse = new GoodsSupplierConsumablesSpecsEntity();
			gscse.setSpecsCode(
					sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.SUPPLIER_CONSUMABLES_SPECS_CODE.getKey()));
			gscse.setConsumablesId(currentId);
			gscse.setSpecs(goodsSupplierConsumablesVO.getSpecs());
			gscse.setGuid(goodsSupplierConsumablesVO.getGuid());
			gscse.setStatus(StatusEnum.USABLE.getKey());
			gscse.setCreateId(goodsSupplierConsumablesVO.getCreateId());
			gscse.setCreateTime(new Date());
			gscse.setSourcesSpecsId(UUID.randomUUID().toString());
			goodsSupplierConsumablesSpecsService.insert(gscse);
		}
        return map;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> update(GoodsSupplierConsumablesVO goodsSupplierConsumablesVO) {
		Map<String, Object> map = new HashMap<>(16);
		String factoryId = goodsSupplierConsumablesVO.getFactoryId();
		OrgFactoryInfoApprovalEntity factory = new OrgFactoryInfoApprovalEntity();
		if (StringUtils.isBlank(factoryId)) {
			// 如果厂商id为空，则新加一个厂商信息，状态为待审批
			String FactoryCode = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.FACTORY_CODE.getKey());
			factory.setFactoryCode(FactoryCode);
			if (StringUtils.isBlank(goodsSupplierConsumablesVO.getFactoryName())) {
				map.put("errmessage", "厂商名称不能为空");
				return map;
			}

			if (orgFactoryInfoApprovalService.selectList(new EntityWrapper<OrgFactoryInfoApprovalEntity>()
					.eq("del_flag", DelFlagEnum.NORMAL.getKey())
					.eq("factory_name", goodsSupplierConsumablesVO.getFactoryName())).size() > 0) {
				map.put("errmessage", "厂商名称已存在");
				return map;
			}

			factory.setFactoryName(goodsSupplierConsumablesVO.getFactoryName());
			factory.setStatus(FactoryStatusEnum.DRAFT.getKey());
			factory.setCreateId(goodsSupplierConsumablesVO.getCreateId());
			SysUserEntity user = sysUserService.selectById(goodsSupplierConsumablesVO.getEditId());
			if (user != null && TypeEnum.USER_PLATFORM.getKey().equals(user.getUserType())) {
				// 平台用户录入，厂家信息属于该用户
				factory.setDeptId(user.getDeptId());
			}
			orgFactoryInfoApprovalService.save(factory,user);
			factoryId = factory.getId().toString();
			goodsSupplierConsumablesVO.setFactoryId(factoryId);
		}
		goodsSupplierConsumablesVO.setEditTime(new Date());
		//设置未匹对(0:未匹对;1:已匹对)
		goodsSupplierConsumablesVO.setIsMatch(IsMatchEnum.NO.getKey());
		goodsSupplierConsumablesVO.setIsUpload(SupplierIsUploadEnum.NO.getKey());
		baseMapper.updateById(goodsSupplierConsumablesVO);


		// 更新批文信息
		String approvals = goodsSupplierConsumablesVO.getApprovals();
		//先根据批准文号（唯一）查
		GoodsSupplierConsumablesApprovalsEntity gscae = goodsSupplierConsumablesApprovalsService.selectByApprovals(approvals);

			if (gscae!=null&&!goodsSupplierConsumablesVO.getId().equals(gscae.getConsumablesId())){
				map.put("errmessage", "批准文号：" + approvals + "，已存在！");
				return map;
			}
		List<GoodsSupplierConsumablesApprovalsEntity> gscaeList = goodsSupplierConsumablesApprovalsService.selectByConsumablesId(goodsSupplierConsumablesVO.getId());
		gscae=gscaeList.get(0);
		gscae.setConsumablesId(goodsSupplierConsumablesVO.getId());
		gscae.setEditId(goodsSupplierConsumablesVO.getEditId());
		gscae.setEditTime(new Date());
		gscae.setApprovals(approvals);
		goodsSupplierConsumablesApprovalsService.updateById(gscae);
		
		//更新下发目录上商品为未上传状态
		this.baseMapper.updateSupplierGoodsSendNotUpload(goodsSupplierConsumablesVO.getSupplierId(), goodsSupplierConsumablesVO.getId());
        return map;
	}

	private void setApprovalsAndSpecs(List<GoodsSupplierConsumablesVO> list) {

		if (list != null) {
			for (GoodsSupplierConsumablesVO entityVO : list) {
				List<GoodsSupplierConsumablesApprovalsEntity> approvalsList = goodsSupplierConsumablesApprovalsService.selectByConsumablesId(entityVO.getId());
				entityVO.setSpecsEntityList(
						goodsSupplierConsumablesSpecsService.selectByConsumablesId(entityVO.getId()));
				entityVO.setApprovals(approvalsList.get(0).getApprovals());
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void toggle(Map<String, Object> params) {
		List<Long> ids = JSON.parseArray(params.get("ids").toString(), Long.class);
		for (Long id : ids) {
			GoodsSupplierConsumablesEntity goodsSupplierConsumables = new GoodsSupplierConsumablesEntity();
			goodsSupplierConsumables.setId(id);
			goodsSupplierConsumables.setStatus(Integer.valueOf(params.get("status").toString()));
			this.baseMapper.updateById(goodsSupplierConsumables);
		}
	}

	// 定时任务
	@Override
	public void updateIsMatch(Long goodsId) {
		GoodsSupplierConsumablesEntity goodsSupplierConsumablesEntity = new GoodsSupplierConsumablesEntity();
		goodsSupplierConsumablesEntity.setId(goodsId);
		goodsSupplierConsumablesEntity.setIsMatch(1);
		baseMapper.updateById(goodsSupplierConsumablesEntity);

	}

	@Override
	public GoodsSupplierConsumablesEntity selectByGoodsNameAndFactoryNameAndSupplierId(String goodsName, String factoryName, Long supplierId) {
		return baseMapper.selectByGoodsNameAndFactoryNameAndSupplierId(goodsName, factoryName, supplierId);
	}

	@Override
	public void updateIsNoUpload(Long goodsId) {
		GoodsSupplierConsumablesEntity entity = new GoodsSupplierConsumablesEntity();
		entity.setId(goodsId);
		entity.setIsUpload(SupplierIsUploadEnum.NO.getKey());
		baseMapper.updateById(entity);
	}

	// HDI转换用
	@Override
	public List<Map<String, Object>> selectBySourcesIds(List<String> sourcesSpecsIds) {
		return this.baseMapper.selectBySourcesIds(sourcesSpecsIds);
	}

	@Override
	public List<GoodsSupplierConsumablesEntity> selectAll() {
		return baseMapper.selectAll();
	}

	@Override
	public void updateSupplierGoodsSendNotUpload(Long supplierId, Long goodsId) {
		this.baseMapper.updateSupplierGoodsSendNotUpload(supplierId, goodsId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Map<String, String> importData(String[][] rows, SysUserEntity user) {
		if (user == null) {
			throw new RuntimeException("导入失败，请先登录！！！");
		}
		StringBuilder sb = new StringBuilder();
		//储存方式map（用于前端value转code）
		List<SysDictEntity> storeWayList = sysDictService.selectDictByType("store_way");
		Map<String, String> storeMap = storeWayList.stream().collect(Collectors.toMap(SysDictEntity::getValue, SysDictEntity::getCode, (key1, key2) -> key2, HashMap::new));
		//商品单位map（用于前端value转code）
		List<SysDictEntity> goodsUnitList = sysDictService.selectDictByType("goods_unit");
		Map<String, String> goodsUnitMap = goodsUnitList.stream().collect(Collectors.toMap(SysDictEntity::getValue, SysDictEntity::getCode, (key1, key2) -> key2, HashMap::new));
		//查出所有厂商,转map
		List<OrgFactoryInfoApprovalEntity> factoryList = this.baseMapper.selectFactoryList();
		Map<String, Long> factoryMap = factoryList.stream().collect(Collectors.toMap(OrgFactoryInfoApprovalEntity::getFactoryName, OrgFactoryInfoApprovalEntity::getId, (key1, key2) -> key2, HashMap::new));
		//查出所有供应商，转map
		List<OrgSupplierInfoEntity> supplierList = this.baseMapper.selectSupplierList();
		Map<String, Long> supplierMap = supplierList.stream().collect(Collectors.toMap(OrgSupplierInfoEntity::getSupplierName, OrgSupplierInfoEntity::getId, (key1, key2) -> key2, HashMap::new));
		//商品属性map
		Map<String, Integer> goodsNatureMap = new HashMap<>();
		goodsNatureMap.put("国产",0);
		goodsNatureMap.put("进口",1);
		//返回保存信息
		String key = "successCount";
		Map<String, String> map = new HashMap<>(16);
		//导入去重map
		Map<String, Object> gscMap = new HashMap<>();
		if (user == null || rows.length <= 1) {
			map.put(key, "0");
			map.put("failCount", String.valueOf(rows.length - 1));
			sb.append("系统错误");
			map.put("errorMessage", sb.toString());
			return map;
		}
		Integer failCount = 0;
		Integer successCount = 0;
		for (int i = 1; i < rows.length; i++) {
			String[] row = rows[i];
			String consumablesName = row[1].trim();
			String commonName = row[2].trim();
			String goodsNature = row[3].trim();
			String typeName = row[4].trim();
			String factoryName = row[5].trim();
			String specs = row[6].trim();
			String guid = row[7].trim();
			String approvals = row[8].trim();
			String goodsUnit = row[9].trim();
			String supplyUnit = row[10].trim();
			String convertUnit = row[11].trim();
			String sunshinePno =row[13].trim();
			if (StringUtil.isEmpty(row[0].trim()) || StringUtil.isEmpty(consumablesName) || StringUtil.isEmpty(typeName) || StringUtil.isEmpty(factoryName) || StringUtil.isEmpty(specs) || StringUtil.isEmpty(approvals) || StringUtil.isEmpty(goodsUnit) || StringUtil.isEmpty(row[12].trim())) {
				sb.append("\n");
				sb.append("第 " + (i+2) + " 行数据错误，必填字段不能为空");
				failCount++;
				continue;
			}
			Long supplierId = supplierMap.get(row[0].trim());
			if (supplierId == null) {
				sb.append("\n");
				sb.append("第 " + (i+2) + " 行记录供应商名称查询不出对应的供应商记录！ 供应商:" + row[0].trim());
				failCount++;
				continue;
			}
			if (!"国产".equals(row[3].trim()) && !"进口".equals(row[3].trim()) && !StringUtil.isEmpty(row[3])) {
				sb.append("\n");
				sb.append("第 " + (i+2) + " 行数据错误，商品属性错误！ 商品属性:" + row[3].trim());
				failCount++;
				continue;
			}

			String storeWay = storeMap.get(row[12].trim());
			if (!StringUtil.isEmpty(row[12].trim()) && StringUtil.isEmpty(storeWay)) {
				sb.append("\n");
				sb.append("第 " + (i+2) + " 行数据错误，存储方式错误！ 存储方式:" + row[12].trim());
				failCount++;
				continue;
			}
			Long factoryId = factoryMap.get(row[5].trim());
			//如果厂商不存在，新增厂商审批
			if (factoryId == null) {
				OrgFactoryInfoApprovalEntity orgFactoryInfoApprovalEntity = new OrgFactoryInfoApprovalEntity();
				orgFactoryInfoApprovalEntity.setFactoryName(factoryName);
				orgFactoryInfoApprovalEntity.setStatus(FactoryStatusEnum.USABLE.getKey());
				orgFactoryInfoApprovalEntity.setCheckStatus(ApprovalTypeEnum.WAIT.getKey());
				orgFactoryInfoApprovalEntity.setEditId(user.getUserId());
				orgFactoryInfoApprovalService.save(orgFactoryInfoApprovalEntity,user);
				factoryId=orgFactoryInfoApprovalEntity.getId();
			}
			//保存数据
			GoodsSupplierConsumablesVO goodsSupplierConsumablesVO = new GoodsSupplierConsumablesVO();
			//查询是否存在重复数据

				List<GoodsSupplierConsumablesEntity> supplierConsumablesList = this.baseMapper.selectByGoodsNameAndApprovals(consumablesName, approvals);
				if (supplierConsumablesList != null && supplierConsumablesList.size() > 0) {
					GoodsSupplierConsumablesEntity goodsSupplierConsumablesEntity = supplierConsumablesList.get(0);
					String  nameAndapprovals = consumablesName+approvals;
					if (!gscMap.containsKey(nameAndapprovals)) {
						gscMap.put(nameAndapprovals, null);

						goodsSupplierConsumablesVO.setConsumablesName(consumablesName);
						goodsSupplierConsumablesVO.setFactoryId(factoryId != null ? factoryId.toString() : null);
						goodsSupplierConsumablesVO.setId(goodsSupplierConsumablesEntity.getId());
						goodsSupplierConsumablesVO.setSupplierId(supplierId);
						goodsSupplierConsumablesVO.setCommonName(commonName);
						if (!StringUtil.isEmpty(row[3])) {
							goodsSupplierConsumablesVO.setGoodsNature(goodsNature.equals("国产") ? 0 : 1);
						}
						goodsSupplierConsumablesVO.setTypeName(typeName);
						goodsSupplierConsumablesVO.setFactoryName(factoryName);
						goodsSupplierConsumablesVO.setGoodsUnit(goodsUnit);
						goodsSupplierConsumablesVO.setSupplyUnit(supplyUnit);
						goodsSupplierConsumablesVO.setConvertUnit(convertUnit);
						goodsSupplierConsumablesVO.setEditId(user.getUserId());
						goodsSupplierConsumablesVO.setEditTime(new Date());
						goodsSupplierConsumablesVO.setApprovals(approvals);
						goodsSupplierConsumablesVO.setStoreWay(storeWay);
						goodsSupplierConsumablesVO.setSunshinePno(sunshinePno);
						update(goodsSupplierConsumablesVO);
					}
						//查询是否存在规格相同的数据
						List<GoodsSupplierConsumablesSpecsEntity> specsList = goodsSupplierConsumablesSpecsService.selectList(new EntityWrapper<GoodsSupplierConsumablesSpecsEntity>()
								.eq("consumables_id", goodsSupplierConsumablesEntity.getId())
								.eq("specs", specs));
						GoodsSupplierConsumablesSpecsEntity specsEntity = null;
						if (specsList != null && specsList.size() > 0) {
							// 存在商品并且存在规格
							specsEntity = specsList.get(0);
							specsEntity.setEditId(user.getUserId());
						} else {
							// 存在商品但是不存在规格
							specsEntity = new GoodsSupplierConsumablesSpecsEntity();
							specsEntity.setCreateId(user.getUserId());
						}
						specsEntity.setSpecs(specs);
						specsEntity.setGuid(guid);
						specsEntity.setConsumablesId(goodsSupplierConsumablesEntity.getId());
						List<GoodsSupplierConsumablesSpecsEntity> list = new ArrayList<>();
						list.add(specsEntity);
						goodsSupplierConsumablesSpecsService.insertOrUpdate(list);

						successCount++;
						continue;
					}

			if((this.baseMapper.selectConsumablesNameAndApprovals(consumablesName,approvals))>0){
				sb.append("\n导入失败，商品名称填写不正确,第").append(i+2).append(" 数据，商品名称:").append(consumablesName);
				failCount++;
				continue;
			}
			else {
				goodsSupplierConsumablesVO.setFactoryId(factoryId != null ? factoryId.toString() : null);
				goodsSupplierConsumablesVO.setSupplierId(supplierId);
				goodsSupplierConsumablesVO.setConsumablesName(consumablesName);
				goodsSupplierConsumablesVO.setCommonName(commonName);
				if (!StringUtil.isEmpty(row[3])){
					goodsSupplierConsumablesVO.setGoodsNature(goodsNature.equals("国产") ? 0 : 1);
				}
				goodsSupplierConsumablesVO.setTypeName(typeName);
				goodsSupplierConsumablesVO.setFactoryName(factoryName);
				goodsSupplierConsumablesVO.setApprovals(approvals);
				goodsSupplierConsumablesVO.setStatus(StatusEnum.USABLE.getKey());
				goodsSupplierConsumablesVO.setGoodsUnit(checkGoodsUnit(goodsUnit));
				goodsSupplierConsumablesVO.setSupplyUnit(checkGoodsUnit(supplyUnit));
				goodsSupplierConsumablesVO.setConvertUnit(convertUnit);
				goodsSupplierConsumablesVO.setDeptId(user.getDeptId());
				goodsSupplierConsumablesVO.setCreateId(user.getUserId());
				goodsSupplierConsumablesVO.setSpecs(specs);
				goodsSupplierConsumablesVO.setGuid(guid);
				goodsSupplierConsumablesVO.setStoreWay(storeWay);
				goodsSupplierConsumablesVO.setSunshinePno(sunshinePno);
				Map<String, Object> saveMap = this.save(goodsSupplierConsumablesVO);
				if (!saveMap.isEmpty()) {
					sb.append("\n");
					sb.append("第 " + (i + 2) + " 行数据错误，" + saveMap.get("errorMessage"));
					failCount++;
					continue;
				}
			}
			successCount++;
		}
		if (failCount > 0) {
			throw new HdiException("错误条数" + failCount +"," + sb.toString());
		}

		map.put("errorMessage", sb.toString());
		map.put(key, successCount.toString());
		map.put("failCount", failCount.toString());
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
	@DataFilter(subDept = true, user = false, tableAlias = "t1")
	public List<Map<String, Object>> getList(HashMap<String, Object> map) {
		List<Map<String, Object>> list = this.baseMapper.getList(map);

		return list;
	}


}
