package com.ebig.hdi.modules.job.task;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.ebig.hdi.common.constant.ActivitiConstant;
import com.ebig.hdi.common.entity.OrgFactoryInfoApprovalEntity;
import com.ebig.hdi.common.enums.*;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.activiti.entity.ActApprovalEntity;
import com.ebig.hdi.modules.activiti.service.ActApprovalService;
import com.ebig.hdi.modules.consumables.entity.GoodsSupplierConsumablesApprovalsEntity;
import com.ebig.hdi.modules.consumables.entity.GoodsSupplierConsumablesEntity;
import com.ebig.hdi.modules.consumables.entity.GoodsSupplierConsumablesSpecsEntity;
import com.ebig.hdi.modules.consumables.service.GoodsSupplierConsumablesApprovalsService;
import com.ebig.hdi.modules.consumables.service.GoodsSupplierConsumablesService;
import com.ebig.hdi.modules.consumables.service.GoodsSupplierConsumablesSpecsService;
import com.ebig.hdi.modules.drugs.entity.GoodsSupplierDrugsEntity;
import com.ebig.hdi.modules.drugs.entity.GoodsSupplierDrugsSpecsEntity;
import com.ebig.hdi.modules.drugs.service.GoodsSupplierDrugsService;
import com.ebig.hdi.modules.drugs.service.GoodsSupplierDrugsSpecsService;
import com.ebig.hdi.modules.job.dao.TempPubGoodsDao;
import com.ebig.hdi.modules.job.entity.ScheduleJobEntity;
import com.ebig.hdi.modules.job.entity.TempPubGoodsEntity;
import com.ebig.hdi.modules.org.service.OrgFactoryInfoApprovalService;
import com.ebig.hdi.modules.reagent.entity.GoodsSupplierReagentEntity;
import com.ebig.hdi.modules.reagent.entity.GoodsSupplierReagentSpecsEntity;
import com.ebig.hdi.modules.reagent.service.GoodsSupplierReagentService;
import com.ebig.hdi.modules.reagent.service.GoodsSupplierReagentSpecsService;
import com.ebig.hdi.modules.sys.entity.SysDictEntity;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import com.ebig.hdi.modules.sys.service.SysConfigService;
import com.ebig.hdi.modules.sys.service.SysDictService;
import com.ebig.hdi.modules.sys.service.SysSequenceService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 抽取商品临时表数据——供应商
 * @author clang
 *
 */

@Component("supplierGoodsInputTask")
@Slf4j
public class SupplierGoodsInputTask implements ITask{
	
	@Autowired
	private TempPubGoodsDao tempPubGoodsDao;
	@Autowired
	private SysSequenceService sysSequenceService;
	@Autowired
	private GoodsSupplierConsumablesService goodsSupplierConsumablesService;
	@Autowired
	private GoodsSupplierConsumablesSpecsService goodsSupplierConsumablesSpecsService;
	@Autowired
	private GoodsSupplierConsumablesApprovalsService goodsSupplierConsumablesApprovalsService;
	@Autowired
	private GoodsSupplierReagentService goodsSupplierReagentService;
	@Autowired
	private GoodsSupplierReagentSpecsService goodsSupplierReagentSpecsService;
	@Autowired
	private GoodsSupplierDrugsService goodsSupplierDrugsService;
	@Autowired
	private GoodsSupplierDrugsSpecsService goodsSupplierDrugsSpecsService;
	@Autowired
	private OrgFactoryInfoApprovalService orgFactoryInfoApprovalService;
	@Autowired
	private SysConfigService sysConfigService;
	@Autowired
	private SysDictService sysDictService; 
	
	private static boolean flag = false;

	private static final String MESSAGE = " 数据插入失败 !";
	private static final String  SPECS = "specs";

	private static final String  SOURCES_SPECS_ID= "sources_specs_id";

	private static final String  MESSAGE_1= "temp_pub_goods 表中mgoodsid为";
	@Autowired
	private ActApprovalService actApprovalService;
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void run(ScheduleJobEntity scheduleJob) {
		synchronized (SupplierGoodsInputTask.class){
			if(!flag){
				try{
					flag = true;
					log.info("supplierGoodsInputTask定时任务正在执行，参数为：{}", scheduleJob.getParams());
					//开始插入数据
					inputSupplierGoods();
					flag = false;
					
				}catch(Exception e){
					log.info("supplierGoodsInputTask定时任务执行失败，参数为：{}", scheduleJob.getParams());
					flag = false;
				}
			}else{
				log.info("上次的supplierGoodsInputTask定时任务还没有运行完！");
			}
		}
	}
	
	/**
	 * 把临时表里的供应商商品数据转化为mcp里对应的供应商商品数据
	 */
	private void inputSupplierGoods(){
		List<TempPubGoodsEntity> sourceGoodsList = tempPubGoodsDao.queryAllGoodsByType(CompanyTypeEnum.SUPPLY.getKey());
		if(sourceGoodsList != null){
			//获取spd对应耗材的采购类别
			String sourceConsumables = sysConfigService.getValue("SOURCE_CONSUMABLES");
			List<String> sourceConsumablesList = new ArrayList<>();
			//获取spd对应药品的采购类别
			String sourceDrugs = sysConfigService.getValue("SOURCE_DRUGS");
			List<String> sourceDrugsList = new ArrayList<>();
			//获取spd对应试剂的采购类别
			String sourceReagent = sysConfigService.getValue("SOURCE_REAGENT");
			List<String> sourceReagentList = new ArrayList<>();
			
			// 判断配置表里是否有配置，如果有配置则放进集合里
			if(StringUtils.isBlank(sourceConsumables)){
				log.error("没有在sys_config表里配置SOURCE_CONSUMABLES,supplierGoodsInputTask定时器执行失败!");
				return;
			}else{
				String[] sourceConsumablesArr = sourceConsumables.split(",");
				Collections.addAll(sourceConsumablesList, sourceConsumablesArr);
			}
			if(StringUtils.isBlank(sourceDrugs)){
				log.error("没有在sys_config表里配置SOURCE_DRUGS,supplierGoodsInputTask定时器执行失败!");
				return;
			}else{
				String[] sourceDrugsArr = sourceDrugs.split(",");
				Collections.addAll(sourceDrugsList, sourceDrugsArr);
			}
			if(StringUtils.isBlank(sourceReagent)){
				log.error("没有在sys_config表里配置SOURCE_REAGENT,supplierGoodsInputTask定时器执行失败!");
				return;
			}else{
				String[] sourceReagentArr = sourceReagent.split(",");
				Collections.addAll(sourceReagentList, sourceReagentArr);
			}
			
			for(TempPubGoodsEntity sourceGoods : sourceGoodsList){
				if(sourceGoods.getGoodscategorytype() == null){
					log.error(MESSAGE_1 + sourceGoods.getMgoodsid() + " 数据的商品所属类别为空，该数据插入失败 !");
					continue;
				} 
				//获取厂商id
				String factoryId = null;
				if(StringUtils.isNotBlank(sourceGoods.getFactoryname())){
					List<OrgFactoryInfoApprovalEntity> factoryList = orgFactoryInfoApprovalService.selectList(new EntityWrapper<OrgFactoryInfoApprovalEntity>()
							.eq("factory_name", sourceGoods.getFactoryname())
							.eq("del_flag", 0));
					if(CollectionUtils.isEmpty(factoryList)){
						factoryId = createFactory(sourceGoods.getFactoryname()).toString();
					} else {
						factoryId = factoryList.get(0).getId().toString();
					}
				}
				
				if(sourceConsumablesList.contains(sourceGoods.getGoodscategorytype().toString())){
					//保存或修改供应商耗材商品
					try{
						//通过商品名字和厂商名字查询数据
						GoodsSupplierConsumablesEntity consumables = 
								goodsSupplierConsumablesService.selectByGoodsNameAndFactoryNameAndSupplierId(
										sourceGoods.getGoodsname(),sourceGoods.getFactoryname(),sourceGoods.getSupplierId());
						if(consumables == null){
							consumables = new GoodsSupplierConsumablesEntity();
						}
						consumables.setSupplierId(sourceGoods.getSupplierId());
						consumables.setConsumablesCode(sourceGoods.getGoodsno());
						consumables.setConsumablesName(sourceGoods.getGoodsname());
						consumables.setCommonName(sourceGoods.getCommonname());
						consumables.setFactoryId(factoryId);
						consumables.setStatus(StatusEnum.USABLE.getKey());
						consumables.setGoodsUnit(checkGoodsUnit(sourceGoods.getGoodsunit()));
						consumables.setPicUrl(sourceGoods.getPhoto());
						consumables.setDeptId(sourceGoods.getDeptId());
						consumables.setDelFlag(0);
						consumables.setIsMatch(0);
						consumables.setDataSource(SupplierDataSource.ERP.getKey());
						consumables.setIsUpload(SupplierIsUploadEnum.YES.getKey());
						if(consumables.getId() == null){
							consumables.setCreateTime(new Date());
							goodsSupplierConsumablesService.insert(consumables);
						}else{
							consumables.setEditTime(new Date());
							goodsSupplierConsumablesService.updateById(consumables);
						}
						
						//保存或修改供应商商品耗材规格数据
						GoodsSupplierConsumablesSpecsEntity specs = new GoodsSupplierConsumablesSpecsEntity();
						List<GoodsSupplierConsumablesSpecsEntity> specsList = goodsSupplierConsumablesSpecsService.selectList(new EntityWrapper<GoodsSupplierConsumablesSpecsEntity>()
								.eq(SOURCES_SPECS_ID, sourceGoods.getMgoodsid())
								.eq(SPECS, sourceGoods.getGoodstype()));
						if(CollectionUtils.isNotEmpty(specsList)){
							specs = specsList.get(0);
						}
						specs.setSourcesSpecsId(sourceGoods.getMgoodsid());
						specs.setConsumablesId(consumables.getId());
						specs.setSpecsCode(sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.SUPPLIER_CONSUMABLES_SPECS_CODE.getKey()));
						specs.setSpecs(sourceGoods.getGoodstype());
						specs.setStatus(StatusEnum.USABLE.getKey());
						if(specs.getId() == null){
							specs.setCreateTime(new Date());
							goodsSupplierConsumablesSpecsService.insert(specs);
						}else{
							specs.setEditTime(new Date());
							goodsSupplierConsumablesSpecsService.updateById(specs);
						}
						
						//保存或修改供应商商品耗材批准文号信息
						if(StringUtils.isNotBlank(sourceGoods.getApprovedocno()) || StringUtils.isNotBlank(sourceGoods.getRegisterdocno())){
							String approvalsName = StringUtils.isNotBlank(sourceGoods.getApprovedocno())?sourceGoods.getApprovedocno() : sourceGoods.getRegisterdocno();
							GoodsSupplierConsumablesApprovalsEntity approvals = new GoodsSupplierConsumablesApprovalsEntity();
							List<GoodsSupplierConsumablesApprovalsEntity> approvalsList = goodsSupplierConsumablesApprovalsService.selectList(new EntityWrapper<GoodsSupplierConsumablesApprovalsEntity>()
								.eq("consumables_id", consumables.getId())
								.eq("approvals", approvalsName));
							if(CollectionUtils.isEmpty(approvalsList)){
								approvals.setConsumablesId(consumables.getId());
								approvals.setApprovals(approvalsName);
								approvals.setStatus(StatusEnum.USABLE.getKey());
								approvals.setCreateTime(new Date());
								goodsSupplierConsumablesApprovalsService.insert(approvals);
							}
							
						}

						//插入完删除临时表数据
						tempPubGoodsDao.deleteById(sourceGoods.getMgoodsid());
						
					}catch(Exception e){
						log.error(MESSAGE_1 + sourceGoods.getMgoodsid() + MESSAGE);
					}
				}else if(sourceReagentList.contains(sourceGoods.getGoodscategorytype().toString())){
					//保存供应商试剂商品
					try{
						//通过商品名称和厂商名称查询是否存在记录
						GoodsSupplierReagentEntity reagent = 
								goodsSupplierReagentService.selectByGoodsNameAndFactoryNameAndSupplierId(
										sourceGoods.getGoodsname(),sourceGoods.getFactoryname(), sourceGoods.getSupplierId());
						if(reagent == null){
							reagent = new GoodsSupplierReagentEntity();
						}
						String approvalsName =
								 StringUtils.isNotBlank(sourceGoods.getApprovedocno())?sourceGoods.
								 getApprovedocno() : sourceGoods.getRegisterdocno();
						reagent.setApprovals(approvalsName);
						reagent.setSupplierId(sourceGoods.getSupplierId());
						reagent.setReagentCode(sourceGoods.getGoodsno());
						reagent.setReagentName(sourceGoods.getGoodsname());
						reagent.setCommonName(sourceGoods.getCommonname());
						reagent.setFactoryId(factoryId);
						reagent.setStatus(StatusEnum.USABLE.getKey());
						reagent.setGoodsUnit(checkGoodsUnit(sourceGoods.getGoodsunit()));
						reagent.setPicUrl(sourceGoods.getPhoto());
						reagent.setDeptId(sourceGoods.getDeptId());
						reagent.setDelFlag(0);
						reagent.setIsMatch(0);
						reagent.setDataSource(SupplierDataSource.ERP.getKey());	
						reagent.setIsUpload(SupplierIsUploadEnum.YES.getKey());
						if(reagent.getId() == null){
							reagent.setCreateTime(new Date());
							goodsSupplierReagentService.insert(reagent);
						}else{
							reagent.setEditTime(new Date());
							goodsSupplierReagentService.updateById(reagent);
						}
						
						//保存或修改供应商商品试剂规格数据
						GoodsSupplierReagentSpecsEntity specs = new GoodsSupplierReagentSpecsEntity();
						List<GoodsSupplierReagentSpecsEntity> specsList = goodsSupplierReagentSpecsService.selectList(new EntityWrapper<GoodsSupplierReagentSpecsEntity>()
								.eq(SOURCES_SPECS_ID, sourceGoods.getMgoodsid())
								.eq(SPECS, sourceGoods.getGoodstype()));
						if(CollectionUtils.isNotEmpty(specsList)){
							specs = specsList.get(0);
						}
						specs.setSourcesSpecsId(sourceGoods.getMgoodsid());
						specs.setReagenId(reagent.getId());
						specs.setSpecsCode(sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.SUPPLIER_REAGENT_SPECS_CODE.getKey()));
						specs.setSpecs(sourceGoods.getGoodstype());
						specs.setStatus(StatusEnum.USABLE.getKey());
						if(specs.getId() == null){
							specs.setCreateTime(new Date());
							goodsSupplierReagentSpecsService.insert(specs);
						}else{
							specs.setEditTime(new Date());
							goodsSupplierReagentSpecsService.updateById(specs);
						}
						
						//插入完删除临时表数据
						tempPubGoodsDao.deleteById(sourceGoods.getMgoodsid());
						
					}catch(Exception e){
						log.error(MESSAGE_1 + sourceGoods.getMgoodsid() + MESSAGE);
					}
				}else if(sourceDrugsList.contains(sourceGoods.getGoodscategorytype().toString())){
					//保存或修改供应商药品商品
					try{
						//通过商品名称和厂商名称查询是否存在记录
						GoodsSupplierDrugsEntity drugs = 
								goodsSupplierDrugsService.selectByGoodsNameAndFactoryNameAndSupplierId(
										sourceGoods.getGoodsname(),sourceGoods.getFactoryname(), sourceGoods.getSupplierId());
						if(drugs == null){
							drugs = new GoodsSupplierDrugsEntity();
						}
						String approvalsName =
								 StringUtils.isNotBlank(sourceGoods.getApprovedocno())?sourceGoods.
								 getApprovedocno() : sourceGoods.getRegisterdocno();
						drugs.setApprovals(approvalsName);
						drugs.setSupplierId(sourceGoods.getSupplierId());
						String drugsCode = sysSequenceService
								.selectSeqValueBySeqCode(SequenceEnum.SUPPLIER_DRUGS_CODE.getKey());
						drugs.setDrugsCode(drugsCode);
						drugs.setDrugsName(sourceGoods.getGoodsname());
						drugs.setCommonName(sourceGoods.getCommonname());
						drugs.setFactoryId(factoryId);
						drugs.setStatus(StatusEnum.USABLE.getKey());
						drugs.setGoodsUnit(checkGoodsUnit(sourceGoods.getGoodsunit()));
						drugs.setPicUrl(sourceGoods.getPhoto());
						drugs.setDeptId(sourceGoods.getDeptId());
						drugs.setDelFlag(0);
						drugs.setIsMatch(0);
						drugs.setDataSource(SupplierDataSource.ERP.getKey());	
						drugs.setIsUpload(SupplierIsUploadEnum.YES.getKey());
						if(drugs.getId() == null){
							drugs.setCreateTime(new Date());
							goodsSupplierDrugsService.insert(drugs);
						}else{
							drugs.setEditTime(new Date());
							goodsSupplierDrugsService.updateById(drugs);
						}
						
						//保存或修改供应商商品药品规格数据
						GoodsSupplierDrugsSpecsEntity specs = new GoodsSupplierDrugsSpecsEntity();
						List<GoodsSupplierDrugsSpecsEntity> specsList = goodsSupplierDrugsSpecsService.selectList(new EntityWrapper<GoodsSupplierDrugsSpecsEntity>()
								.eq(SOURCES_SPECS_ID, sourceGoods.getMgoodsid())
								.eq(SPECS, sourceGoods.getGoodstype()));
						if(CollectionUtils.isNotEmpty(specsList)){
							specs = specsList.get(0);
						}
						specs.setSourcesSpecsId(sourceGoods.getMgoodsid());
						specs.setDrugsId(drugs.getId());
						specs.setSpecsCode(sourceGoods.getGoodsno());
						specs.setSpecs(sourceGoods.getGoodstype());
						specs.setStatus(StatusEnum.USABLE.getKey());
						if(specs.getId() == null){
							specs.setCreateTime(new Date());
							goodsSupplierDrugsSpecsService.insert(specs);
						}else{
							specs.setEditTime(new Date());
							goodsSupplierDrugsSpecsService.updateById(specs);
						}
						//插入完删除临时表数据
						tempPubGoodsDao.deleteById(sourceGoods.getMgoodsid());
					}catch(Exception e){
						log.error(MESSAGE_1 + sourceGoods.getMgoodsid() + MESSAGE);
					}
				}
			}
		}
	}
	private Long createFactory(String factoryName) {
		OrgFactoryInfoApprovalEntity factoryInfo = new OrgFactoryInfoApprovalEntity();
		String factoryCode = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.FACTORY_CODE.getKey());
		factoryInfo.setFactoryCode(factoryCode);
		factoryInfo.setFactoryName(factoryName);
		factoryInfo.setStatus(FactoryStatusEnum.DRAFT.getKey());
		factoryInfo.setDelFlag(DelFlagEnum.NORMAL.getKey());
		factoryInfo.setCheckStatus(ApprovalTypeEnum.WAIT.getKey());
		factoryInfo.setCreateTime(new Date());
		orgFactoryInfoApprovalService.insert(factoryInfo);
		//发起审批
		SysUserEntity entity = new SysUserEntity();
		entity.setUserId(1L);
		entity.setUsername("admin");
		ActApprovalEntity approvalEntity = actApprovalService.getActApprovalEntity(entity, ChangeTypeEnum.ADD.getKey(),
				ActTypeEnum.FACTORY.getKey(), factoryInfo.getId().toString(),
				factoryInfo.getFactoryCode(), factoryInfo.getFactoryName());
		Map<String, Object> map = new HashMap<>(16);
		map.put("approvalEntity", approvalEntity);
		actApprovalService.insert(approvalEntity);
		ProcessInstance instance = actApprovalService.startProcess(entity.getUserId().toString(),
				ActivitiConstant.ACTIVITI_ORGFACTORY[0], ActivitiConstant.ACTIVITI_ORGFACTORY[1], map);
		if (instance != null) {
			approvalEntity.setProcessId(instance.getProcessInstanceId());
			approvalEntity.setApprovalCode(instance.getProcessInstanceId());
			factoryInfo.setProcessId(instance.getProcessInstanceId());
		}
		orgFactoryInfoApprovalService.updateById(factoryInfo);
		actApprovalService.updateById(approvalEntity);
		return factoryInfo.getId();
		
	}
	private String checkGoodsUnit(String srcGoodsUnit) {
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
}
