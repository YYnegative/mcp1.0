package com.ebig.hdi.modules.reagent.service.impl;

import java.text.MessageFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ebig.hdi.common.entity.OrgFactoryInfoApprovalEntity;
import com.ebig.hdi.common.utils.CheckStringUtils;
import com.ebig.hdi.modules.core.service.OrgFactoryInfoApprovalService;
import com.ebig.hdi.modules.reagent.common.UnicodeGoodsUnitCode;
import com.sun.org.apache.bcel.internal.generic.RETURN;
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
import com.ebig.hdi.common.enums.SequenceEnum;
import com.ebig.hdi.common.enums.StatusEnum;
import com.ebig.hdi.common.enums.SupplierDataSource;
import com.ebig.hdi.common.enums.SupplierIsUploadEnum;
import com.ebig.hdi.common.enums.TypeEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.Constant;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.reagent.dao.GoodsSupplierReagentDao;
import com.ebig.hdi.modules.reagent.entity.GoodsSupplierReagentEntity;
import com.ebig.hdi.modules.reagent.entity.GoodsSupplierReagentSpecsEntity;
import com.ebig.hdi.modules.reagent.entity.OrgFactorysInfoEntity;
import com.ebig.hdi.modules.reagent.service.GoodsSupplierReagentService;
import com.ebig.hdi.modules.reagent.service.GoodsSupplierReagentSpecsService;
import com.ebig.hdi.modules.reagent.service.OrgFactorysInfoService;
import com.ebig.hdi.modules.reagent.vo.GoodsSupplierReagentVO;
import com.ebig.hdi.modules.sys.entity.SysDictEntity;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import com.ebig.hdi.modules.sys.service.SysDictService;
import com.ebig.hdi.modules.sys.service.SysSequenceService;
import com.ebig.hdi.modules.sys.service.SysUserService;
import org.springframework.util.Assert;


@Service("goodsSupplierReagentService")
public class GoodsSupplierReagentServiceImpl extends ServiceImpl<GoodsSupplierReagentDao, GoodsSupplierReagentEntity> implements GoodsSupplierReagentService {
	
	@Autowired
	private OrgFactorysInfoService orgFactorysInfoService;
	@Autowired
	private SysSequenceService sysSequenceService;
	@Autowired
	private GoodsSupplierReagentSpecsService goodsSupplierReagentSpecsService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysDictService sysDictService;

	@Autowired
	private OrgFactoryInfoApprovalService orgFactoryInfoApprovalService;


	@Override
    @DataFilter(subDept = true, user = false, tableAlias = "t1")
    public PageUtils queryPage(Map<String, Object> params) {
    	GoodsSupplierReagentVO gsrVO = new GoodsSupplierReagentVO();
    	gsrVO.setSupplierName((String)params.get("supplierName"));//供应商名称
    	gsrVO.setReagentName((String)params.get("reagentName"));//试剂名称
    	gsrVO.setGoodsNature((Integer)params.get("goodsNature"));//商品属性
    	gsrVO.setTypeName((String)params.get("typeName"));//分类名称
    	gsrVO.setStatus((Integer)params.get("status"));//状态
    	gsrVO.setFactoryName((String)params.get("factoryName"));//生产厂商
    	gsrVO.setFileterDept((String)params.get(Constant.SQL_FILTER));//设置权限
    	int currPage = Integer.parseInt(params.get("page").toString());
    	int pageSize = Integer.parseInt(params.get("limit").toString());
    	Page<GoodsSupplierReagentVO> page = new Page<GoodsSupplierReagentVO>(currPage, pageSize);
    	List<GoodsSupplierReagentVO> list = this.baseMapper.listForPage(page,gsrVO);
    	setApprovalsAndSpecs(list);
    	page.setRecords(list);
        return new PageUtils(page);
    }

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(GoodsSupplierReagentVO goodsSupplierReagentVO ) {
    	StringBuilder message = new StringBuilder();
		checkEmptyAndSaveFactorty(goodsSupplierReagentVO,message);
		if(message.length() >0){
			throw new HdiException(message.toString());
		}
		insert(goodsSupplierReagentVO,message);
		if(message.length() >0){
			throw new HdiException(message.toString());
		}

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(GoodsSupplierReagentVO goodsSupplierReagentVO) {
		StringBuilder message = new StringBuilder();
		checkEmptyAndSaveFactorty(goodsSupplierReagentVO,message);
		if(message.length() >0){
			throw new HdiException(message.toString());
		}
		goodsSupplierReagentVO.setEditTime(new Date());
		//设置是否上传为未上传
		this.baseMapper.updateById(goodsSupplierReagentVO);
		//更新下发目录上商品为未上传状态
		this.baseMapper.updateSupplierGoodsSendNotUpload(goodsSupplierReagentVO.getSupplierId(), goodsSupplierReagentVO.getId());
	}
	
	@Override
	public GoodsSupplierReagentVO selectReagentById(Long id) {
		return baseMapper.selectReagentById(id);
	}
	
	private void setApprovalsAndSpecs(List<GoodsSupplierReagentVO> list){
		if(list != null){
			for(GoodsSupplierReagentVO gsrVO : list){
				gsrVO.setSpecsEntityList(goodsSupplierReagentSpecsService.selectListByReagentId(gsrVO.getId()));
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void toggle(Map<String, Object> params) {
		List<Long> ids = JSON.parseArray(params.get("ids").toString(), Long.class);
		for(Long id : ids) {
			GoodsSupplierReagentEntity goodsSupplierReagent = new GoodsSupplierReagentEntity();
			goodsSupplierReagent.setId(id);
			goodsSupplierReagent.setStatus(Integer.valueOf(params.get("status").toString()));
			this.baseMapper.updateById(goodsSupplierReagent);
		}
	}

	
	@Override
	public void updateIsMatch(Long goodsId) {
		GoodsSupplierReagentEntity goodsSupplierReagentEntity = new GoodsSupplierReagentEntity();
		goodsSupplierReagentEntity.setId(goodsId);
		goodsSupplierReagentEntity.setIsMatch(1);
		baseMapper.updateById(goodsSupplierReagentEntity);
	}

	@Override
	public GoodsSupplierReagentEntity selectByGoodsNameAndFactoryNameAndSupplierId(String goodsName, String factoryName, Long supplierId) {
		return baseMapper.selectByGoodsNameAndFactoryNameAndSupplierId(goodsName,factoryName,supplierId);
	}

	@Override
	public List<Map<String, Object>> selectBySourcesIds(List<String> sourcesSpecsIds){
		return this.baseMapper.selectBySourcesIds(sourcesSpecsIds);
	}
	@Override
	public void input(String[][] rows,Long userId,Long deptId) {
	}


	
	@Transactional(rollbackFor = Exception.class)
	public String 	checkGoodsUnit(String srcGoodsUnit) {
		String goodsUnit = null;
		if(!StringUtil.isEmpty(srcGoodsUnit)) {
			List<SysDictEntity> dicts = sysDictService.selectList(new EntityWrapper<SysDictEntity>().eq("value", srcGoodsUnit));
			if(StringUtil.isEmpty(dicts)) {
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
	public void updateSupplierGoodsSendNotUpload(Long supplierId, Long goodsId) {
		this.baseMapper.updateSupplierGoodsSendNotUpload(supplierId, goodsId);

	}

	@Override
	@DataFilter(subDept = true, user = false, tableAlias = "t1")
	public List<Map<String, Object>> getList(Map<String, Object> map) {
			List<Map<String, Object>> list = this.baseMapper.getList(map);
		return list;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Map<String, String> importData(String[][] rows, SysUserEntity user) {
		if (StringUtil.isEmpty(user) || rows.length <= 1) {
			throw new RuntimeException("系统错误！!");
		}
		Map<String, String> errorMessage = new HashMap<>(16);
		StringBuilder errMsg = new StringBuilder();
		StringBuilder message = new StringBuilder();

		HashMap<String,Boolean> nameAndapprovalsMap = new HashMap<>();
		final String SUCCESSCOUNT = "successCount",FAILCOUNT = "failCount",ERRORMESSAGE = "errorMessage";
		Integer failCount = 0,successCount = 0;
		for(int i =1 ; i< rows.length; i++){
			String[] row = rows[i];
			String supplierName = row[0], reagentName = row[1], commonName = row[2],goodsNature = row[3], typeName = row[4],factoryName = row[5],factoryId = null;
			String specs = row[6], guid = row[7],approvals = row[8], goodsUnit = row[9], supplyUnit = row[10], convertUnit = row[11], storeWay = row[12], sunshinePno = row[13];// 阳光
			//存储方式转化

			if(StringUtil.isEmpty(reagentName)){
				errMsg.append("\n导入失败，商品名称为空,第:").append(i).append(" 数据");
				failCount++;
				continue;
			}
			if (!StringUtil.isEmpty(goodsNature)) {
				if (!"国产".equals(goodsNature)) {
					if (!"进口".equals(goodsNature)) {
						errMsg.append("\n导入失败，商品属性填写不正确,第:").append(i).append(" 数据");
						failCount++;
						continue;
					}
				}
			}
			if(StringUtil.isEmpty(specs)){
				errMsg.append("\n导入失败，规格为空,第:").append(i).append(" 数据");
				failCount++;
				continue;
			}

			if(StringUtil.isEmpty(approvals)){
				errMsg.append("\n导入失败，批准文号为空,第:").append(i).append(" 数据");
				failCount++;
				continue;
			}
			GoodsSupplierReagentVO goodsSupplierReagentVO = new GoodsSupplierReagentVO();

			List<OrgFactorysInfoEntity> factoryList = orgFactorysInfoService.selectList(new EntityWrapper<OrgFactorysInfoEntity>()
					.eq("factory_name", factoryName)
					.eq("del_flag", 0));
			if(!(factoryList != null && factoryList.size() >0)){
				errMsg.append("\n导入失败，厂商名称不存在对应的数据,第:").append(i).append(" 数据");
				failCount++;
				continue;
			}
			factoryId = factoryList.get(0).getId().toString();

			List<GoodsSupplierReagentEntity> supplierReagentList = this.baseMapper.selectList(new EntityWrapper<GoodsSupplierReagentEntity>()
					.eq("reagent_name", reagentName)
					.eq("approvals", approvals));
			if(!StringUtil.isEmpty(supplierReagentList)){
				GoodsSupplierReagentEntity goodsSupplierReagentEntity =supplierReagentList.get(0);
				String  nameAndapprovals = reagentName+approvals;
				if(!nameAndapprovalsMap.containsKey(nameAndapprovals)){
					nameAndapprovalsMap.put(nameAndapprovals,null);
					GoodsSupplierReagentEntity reagent = supplierReagentList.get(0);
					goodsSupplierReagentVO.setReagentName(reagentName);
					goodsSupplierReagentVO.setFactoryId(factoryId);
					goodsSupplierReagentVO.setId(reagent.getId());
					goodsSupplierReagentVO.setSupplierId(reagent.getSupplierId());
					goodsSupplierReagentVO.setCommonName(commonName);
					if (!StringUtil.isEmpty(row[3])) {
						goodsSupplierReagentVO.setGoodsNature(goodsNature.equals("国产") ? 0 : 1);
					}
					goodsSupplierReagentVO.setTypeName(typeName);
					goodsSupplierReagentVO.setFactoryName(factoryName);
					goodsSupplierReagentVO.setGoodsUnit(checkGoodsUnit(goodsUnit));
					goodsSupplierReagentVO.setSupplyUnit(checkGoodsUnit(supplyUnit));
					goodsSupplierReagentVO.setConvertUnit(convertUnit);
					goodsSupplierReagentVO.setEditId(user.getUserId());
					goodsSupplierReagentVO.setStoreWay(storeWay);//储存方式

					checkEmptyAndSaveFactorty(goodsSupplierReagentVO,message);
					if(message.length() >0){
						errMsg.append("\n").append(message).append("第 ").append(i).append("  条数据");
						message.delete( 0, message.length() );
						failCount++;
						continue;
					}
					goodsSupplierReagentVO.setEditTime(new Date());
					//设置是否上传为未上传
					this.baseMapper.updateById(goodsSupplierReagentVO);
					//更新下发目录上商品为未上传状态
					this.baseMapper.updateSupplierGoodsSendNotUpload(goodsSupplierReagentVO.getSupplierId(), goodsSupplierReagentVO.getId());
				}

				//查询是否存在规格相同的数据
				List<GoodsSupplierReagentSpecsEntity> specsList = goodsSupplierReagentSpecsService.selectList(new EntityWrapper<GoodsSupplierReagentSpecsEntity>()
						.eq("reagen_id", goodsSupplierReagentEntity.getId())
						.eq("specs", specs));
				GoodsSupplierReagentSpecsEntity specsEntity = null;
				if(specsList != null && specsList.size() >0){
					// 存在商品并且存在规格
					specsEntity = specsList.get(0);
					specsEntity.setEditId(user.getUserId());
				}else{
					// 存在商品但是不存在规格
					specsEntity = new GoodsSupplierReagentSpecsEntity();
					specsEntity.setCreateId(user.getUserId());
				}
				specsEntity.setSpecs(specs);
				specsEntity.setGuid(guid);
				specsEntity.setStatus(1);
				specsEntity.setReagenId(goodsSupplierReagentEntity.getId());
				List<GoodsSupplierReagentSpecsEntity> list = new ArrayList<>();
				list.add(specsEntity);
				goodsSupplierReagentSpecsService.insertOrUpdate(list);
				continue;
			}else {
				if (StringUtil.isEmpty(goodsUnit)) {
					errMsg.append("\n导入失败，商品单位为空,第:").append(i).append(" 数据");
					failCount++;
					continue;
				}

				if (StringUtil.isEmpty(typeName)) {
					errMsg.append("\n导入失败，商品分类为空,第:").append(i).append(" 数据");
					failCount++;
					continue;
				}

				storeWay = UnicodeGoodsUnitCode.getStoreWay(storeWay);
				if (StringUtil.isEmpty(storeWay)) {
					errMsg.append("\n导入失败，储存方式值错误,第:").append(i).append(" 数据");
					failCount++;
					continue;
				}
				if ((this.baseMapper.selectReagentNameAndApprovals(reagentName, approvals)) > 0) {
					errMsg.append("\n导入失败，商品名称存在,第:").append(i).append(" 数据，商品名称:").append(reagentName);
					failCount++;
					continue;
				}


				if (StringUtils.isBlank(supplierName)) {
					errMsg.append("\n导入失败，供应商名称不能空,第:").append(i).append(" 数据");
					failCount++;
					continue;
				}
				List<Long> supplierIdList = this.baseMapper.selectSupplierIdByName(supplierName);
				if (supplierIdList == null || supplierIdList.size() <= 0) {
					errMsg.append("\n第 ").append(i).append(" 行数据错误，供应商名称查询不出对应的供应商记录");
					failCount++;
					continue;
				}
				if (StringUtils.isBlank(factoryName)) {
					errMsg.append("\n第 ").append(i).append(" 行数据错误，厂商名称不能空");
					failCount++;
					continue;
				}

				// 保存数据
				goodsSupplierReagentVO.setSunshinePno(sunshinePno);
				if (!StringUtil.isEmpty(row[3])) {
					goodsSupplierReagentVO.setGoodsNature("国产".equals(goodsNature) ? 0 : 1);
				}
				goodsSupplierReagentVO.setSupplierId(supplierIdList.get(0));
				goodsSupplierReagentVO.setReagentName(reagentName);
				goodsSupplierReagentVO.setFactoryId(factoryId);
				goodsSupplierReagentVO.setCommonName(commonName);
				goodsSupplierReagentVO.setTypeName(typeName);
				goodsSupplierReagentVO.setFactoryName(factoryName);
				goodsSupplierReagentVO.setGoodsUnit(checkGoodsUnit(goodsUnit));
				goodsSupplierReagentVO.setSupplyUnit(checkGoodsUnit(supplyUnit));
				goodsSupplierReagentVO.setConvertUnit(convertUnit);
				goodsSupplierReagentVO.setStatus(StatusEnum.USABLE.getKey());
				goodsSupplierReagentVO.setStoreWay(storeWay);//储存方式
				goodsSupplierReagentVO.setApprovals(approvals);
				goodsSupplierReagentVO.setSpecs(specs);
				goodsSupplierReagentVO.setGuid(guid);
				// 新增
				goodsSupplierReagentVO.setSupplierId(supplierIdList.get(0));
				goodsSupplierReagentVO.setCreateTime(new Date());
				goodsSupplierReagentVO.setStatus(StatusEnum.USABLE.getKey());
				goodsSupplierReagentVO.setDeptId(user.getDeptId());
				goodsSupplierReagentVO.setCreateId(user.getUserId());
				checkEmptyAndSaveFactorty(goodsSupplierReagentVO, message);
				if (message.length() > 0) {
					errMsg.append("\n").append(message).append("第 ").append(i).append("  条数据");
					message.delete(0, message.length());
					failCount++;
					continue;
				}
				insert(goodsSupplierReagentVO, message);
				if (message.length() > 0) {
					errMsg.append("\n").append(message).append("第 ").append(i).append("  条数据");
					message.delete(0, message.length());
					failCount++;
					continue;
				}
			}

		}if (failCount > 0){
			StringBuilder manage = new StringBuilder();
			manage.append("成功条数: 0,失败条数 :").append(rows.length-1).append("   ").append(errMsg);
			throw new HdiException(manage.toString());
		}
		successCount = rows.length-1;
		errorMessage.put(SUCCESSCOUNT, successCount.toString());
		errorMessage.put(FAILCOUNT,"0");
		errorMessage.put(ERRORMESSAGE,errMsg.toString());
		return errorMessage;

    }

	@Override
	public Map<String, String> importData(Object r, Object sysUserEntity) {
		String[][] rows = (String[][]) r;
		 SysUserEntity user = (SysUserEntity)sysUserEntity;
		Map<String, String> errorMessage = new HashMap<>(16);


		if (StringUtil.isEmpty(user) || rows.length <= 1) {
			throw new HdiException("系统错误！!");
		}
		StringBuilder errMsg = new StringBuilder();
		StringBuilder message = new StringBuilder();

		final String SUCCESSCOUNT = "successCount",FAILCOUNT = "failCount",ERRORMESSAGE = "errorMessage";
		Integer failCount = 0,successCount = 0;
		for(int i =1 ; i< rows.length; i++){
			String[] row = rows[i];
			String supplierName = row[0], reagentName = row[1], commonName = row[2],goodsNature = row[3], typeName = row[4],factoryName = row[5],factoryId = null;
			String specs = row[6], guid = row[7],approvals = row[8], goodsUnit = row[9], supplyUnit = row[10], convertUnit = row[11], storeWay = row[12], sunshinePno = row[13];// 阳光


			//存储方式转化
			storeWay  = UnicodeGoodsUnitCode.getStoreWay(storeWay);
			if(StringUtil.isEmpty(storeWay)){
				errMsg.append("\n导入失败，储存方式值错误,第:").append(i).append(" 数据");
				failCount++;
				continue;
			}
			if (!"国产".equals(goodsNature)) {
				if (!"进口".equals(goodsNature)) {
					errMsg.append("\n导入失败，商品属性填写不正确,第:").append(i).append(" 数据");
					failCount++;
					continue;
				}
			}
			if (StringUtils.isBlank(supplierName)) {
				errMsg.append("\n导入失败，供应商名称不能空,第:").append(i).append(" 数据");
				failCount++;
				continue;
			}
			List<Long> supplierIdList = this.baseMapper.selectSupplierIdByName(supplierName);
			if(supplierIdList == null || supplierIdList.size() <= 0){
				errMsg.append("\n第 ").append(i) .append(" 行数据错误，供应商名称查询不出对应的供应商记录");
				failCount++;
				continue;
			}
			if (StringUtils.isBlank(factoryName)) {
				errMsg.append("\n第 ").append(i) .append(" 行数据错误，厂商名称不能空");
				failCount++;
				continue;
			}
			List<OrgFactorysInfoEntity> factoryList = orgFactorysInfoService.selectList(new EntityWrapper<OrgFactorysInfoEntity>()
					.eq("factory_name", factoryName)
					.eq("del_flag", 0));
			if(!(factoryList != null && factoryList.size() >0)){
				errMsg.append("\n导入失败，厂商名称不存在对应的数据,第:").append(i).append(" 数据");
				failCount++;
				continue;
			}
			factoryId = factoryList.get(0).getId().toString();

			// 保存数据
			GoodsSupplierReagentVO goodsSupplierReagentVO = new GoodsSupplierReagentVO();
			goodsSupplierReagentVO.setSunshinePno(sunshinePno);
			goodsSupplierReagentVO.setGoodsNature("国产".equals(goodsNature)?0:1);
			goodsSupplierReagentVO.setSupplierId(supplierIdList.get(0));
			goodsSupplierReagentVO.setReagentName(reagentName);
			goodsSupplierReagentVO.setFactoryId(factoryId);
			goodsSupplierReagentVO.setCommonName(commonName);
			goodsSupplierReagentVO.setTypeName(typeName);
			goodsSupplierReagentVO.setFactoryName(factoryName);
			goodsSupplierReagentVO.setGoodsUnit(checkGoodsUnit(goodsUnit));
			goodsSupplierReagentVO.setSupplyUnit(checkGoodsUnit(supplyUnit));
			goodsSupplierReagentVO.setConvertUnit(convertUnit);
			goodsSupplierReagentVO.setStatus(StatusEnum.USABLE.getKey());
			goodsSupplierReagentVO.setStoreWay(storeWay);//储存方式
			goodsSupplierReagentVO.setApprovals(approvals);
			goodsSupplierReagentVO.setSpecs(specs);
			goodsSupplierReagentVO.setGuid(guid);
			//查询是否存在重复数据
			List<GoodsSupplierReagentEntity> supplierReagentList = this.baseMapper.selectList(new EntityWrapper<GoodsSupplierReagentEntity>()
					.eq("reagent_name", reagentName)
					.eq("approvals", approvals));
			if(supplierReagentList != null && supplierReagentList.size() > 0){
				// 先更新商品数据
				GoodsSupplierReagentEntity reagent = supplierReagentList.get(0);
				goodsSupplierReagentVO.setId(reagent.getId());
				goodsSupplierReagentVO.setSupplierId(reagent.getSupplierId());
				goodsSupplierReagentVO.setEditId(user.getUserId());
				goodsSupplierReagentVO.setEditTime(new Date());
				checkEmptyAndSaveFactorty(goodsSupplierReagentVO,message);
				if(message.length() >0){
					errMsg.append(message).append("第 ").append(i).append("  条数据");
					message.delete( 0, message.length() );
					failCount++;
					continue;
				}
				goodsSupplierReagentVO.setEditTime(new Date());
				//设置是否上传为未上传
				this.baseMapper.updateById(goodsSupplierReagentVO);
				//更新下发目录上商品为未上传状态
				this.baseMapper.updateSupplierGoodsSendNotUpload(goodsSupplierReagentVO.getSupplierId(), goodsSupplierReagentVO.getId());

				//查询是否存在规格相同的数据
				List<GoodsSupplierReagentSpecsEntity> specsList = goodsSupplierReagentSpecsService.selectList(new EntityWrapper<GoodsSupplierReagentSpecsEntity>()
						.eq("reagen_id", reagent.getId())
						.eq("specs", specs));
				GoodsSupplierReagentSpecsEntity specsEntity = null;
				if(specsList != null && specsList.size() >0){
					// 存在商品并且存在规格
					specsEntity = specsList.get(0);
					specsEntity.setEditId(user.getUserId());
				}else{
					// 存在商品但是不存在规格
					specsEntity = new GoodsSupplierReagentSpecsEntity();
					specsEntity.setCreateId(user.getUserId());
				}
				specsEntity.setSpecs(specs);
				specsEntity.setGuid(guid);
				specsEntity.setReagenId(goodsSupplierReagentVO.getId());
				List<GoodsSupplierReagentSpecsEntity> list = new ArrayList<>();
				list.add(specsEntity);
				goodsSupplierReagentSpecsService.insertOrUpdate(list);
			}else{
				// 新增
				goodsSupplierReagentVO.setSupplierId(supplierIdList.get(0));
				goodsSupplierReagentVO.setCreateTime(new Date());
				goodsSupplierReagentVO.setStatus(StatusEnum.USABLE.getKey());
				goodsSupplierReagentVO.setDeptId(user.getDeptId());
				goodsSupplierReagentVO.setCreateId(user.getUserId());
				checkEmptyAndSaveFactorty(goodsSupplierReagentVO,message);
				if(message.length() >0){
					errMsg.append("\n").append(message).append("第 ").append(i).append("  条数据");
					message.delete( 0, message.length() );
					failCount++;
					continue;
				}
				insert(goodsSupplierReagentVO,message);
				if(message.length() >0){
					errMsg.append("\n").append(message).append("第 ").append(i).append("  条数据");
					message.delete( 0, message.length() );
					failCount++;
					continue;
				}
			}
		}if (failCount > 0){
			StringBuilder manage = new StringBuilder();
			manage.append("成功条数: 0,失败条数").append(rows.length-1).append("   ").append(errMsg);
			throw new HdiException(manage.toString());
		}
		successCount = rows.length-1;
		errorMessage.put(SUCCESSCOUNT, successCount.toString());
		errorMessage.put(FAILCOUNT,"0");
		errorMessage.put(ERRORMESSAGE,errMsg.toString());
		return errorMessage;
	}


	private void checkEmptyAndSaveFactorty(GoodsSupplierReagentVO goodsSupplierReagentVO,StringBuilder sb) {
		if (this.baseMapper.selectApprovals(goodsSupplierReagentVO) >0) {
			sb.append(MessageFormat.format("批准文号:{0}，已存在", goodsSupplierReagentVO.getApprovals()));
			return;
		}
		if (StringUtil.isEmpty(goodsSupplierReagentVO.getFactoryId())) {
			OrgFactoryInfoApprovalEntity factory = new OrgFactoryInfoApprovalEntity();
			//如果厂商id为空，则新加一个厂商信息，状态为待审批
			String factoryCode = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.FACTORY_CODE.getKey());

			if (StringUtil.isEmpty(goodsSupplierReagentVO.getFactoryName())) {factory.setFactoryCode(factoryCode);
				sb.append("厂商名称不能为空");
				return;
			}
			if (!StringUtil.isEmpty(orgFactorysInfoService.selectList(new EntityWrapper<OrgFactorysInfoEntity>()
					.eq("del_flag", DelFlagEnum.NORMAL.getKey())
					.eq("factory_name", goodsSupplierReagentVO.getFactoryName())))) {
				sb.append("厂商名称已存在");
				return;
			}
			factory.setFactoryName(goodsSupplierReagentVO.getFactoryName());
			factory.setStatus(FactoryStatusEnum.DRAFT.getKey());
			factory.setCreateId(goodsSupplierReagentVO.getCreateId());
			factory.setDelFlag(DelFlagEnum.NORMAL.getKey());
			factory.setCreateTime(new Date());
			SysUserEntity user;
			if(StringUtil.isEmpty(goodsSupplierReagentVO.getCreateId())){
				user = sysUserService.selectById(goodsSupplierReagentVO.getEditId());
			}else {
				user = sysUserService.selectById(goodsSupplierReagentVO.getCreateId());
			}

			if (user != null && TypeEnum.USER_PLATFORM.getKey().equals(user.getUserType())) {
				//平台用户录入，厂家信息属于该用户
				factory.setDeptId(user.getDeptId());
			}
			orgFactoryInfoApprovalService.save(factory, user);
			goodsSupplierReagentVO.setFactoryId(factory.getId().toString());
		}
		goodsSupplierReagentVO.setIsUpload(SupplierIsUploadEnum.NO.getKey());
		goodsSupplierReagentVO.setIsMatch(0);

	}

	private  void  insert(GoodsSupplierReagentVO goodsSupplierReagentVO,StringBuilder sb){
		String reagentCode = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.SUPPLIER_REAGENT_CODE.getKey());
		goodsSupplierReagentVO.setReagentCode(reagentCode);
		goodsSupplierReagentVO.setCreateTime(new Date());
		//设置未匹对(0:未匹对;1:已匹对)
		goodsSupplierReagentVO.setDataSource(SupplierDataSource.SYSTEM.getKey());
		Map<String, Object> supplierInfo = this.baseMapper.selectSupplierInfoBySupplierId(goodsSupplierReagentVO.getSupplierId());
		if (StringUtil.isEmpty(supplierInfo)){
			sb.append("供应商不存在");
			return;
		}
		goodsSupplierReagentVO.setDeptId((Long)supplierInfo.get("dept_id"));
		//设置是否上传为未上传
		goodsSupplierReagentVO.setDelFlag(DelFlagEnum.NORMAL.getKey());
		baseMapper.insert(goodsSupplierReagentVO);

		//保存试剂规格
		GoodsSupplierReagentSpecsEntity goodsSupplierReagentSpecsEntity = new GoodsSupplierReagentSpecsEntity();
		goodsSupplierReagentSpecsEntity.setReagenId(goodsSupplierReagentVO.getId());
		goodsSupplierReagentSpecsEntity.setSpecs(goodsSupplierReagentVO.getSpecs());
		goodsSupplierReagentSpecsEntity.setGuid(goodsSupplierReagentVO.getGuid());
		goodsSupplierReagentSpecsEntity.setCreateId(goodsSupplierReagentVO.getCreateId());
		goodsSupplierReagentSpecsEntity.setStatus(goodsSupplierReagentVO.getStatus());
		goodsSupplierReagentSpecsService.save(goodsSupplierReagentSpecsEntity);
	}
}




