package com.ebig.hdi.modules.job.task;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ebig.hdi.common.entity.OrgFactoryInfoApprovalEntity;
import com.ebig.hdi.common.enums.*;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.consumables.entity.GoodsHospitalConsumablesApprovalsEntity;
import com.ebig.hdi.modules.consumables.entity.GoodsHospitalConsumablesEntity;
import com.ebig.hdi.modules.consumables.entity.GoodsHospitalConsumablesSpecsEntity;
import com.ebig.hdi.modules.consumables.service.GoodsHospitalConsumablesApprovalsService;
import com.ebig.hdi.modules.consumables.service.GoodsHospitalConsumablesService;
import com.ebig.hdi.modules.consumables.service.GoodsHospitalConsumablesSpecsService;
import com.ebig.hdi.modules.drugs.entity.GoodsHospitalDrugsEntity;
import com.ebig.hdi.modules.drugs.entity.GoodsHospitalDrugsSpecsEntity;
import com.ebig.hdi.modules.drugs.service.GoodsHospitalDrugsService;
import com.ebig.hdi.modules.drugs.service.GoodsHospitalDrugsSpecsService;
import com.ebig.hdi.modules.job.dao.TempPubGoodsDao;
import com.ebig.hdi.modules.job.entity.ScheduleJobEntity;
import com.ebig.hdi.modules.job.entity.TempPubGoodsEntity;
import com.ebig.hdi.modules.org.service.OrgFactoryInfoApprovalService;
import com.ebig.hdi.modules.reagent.entity.GoodsHospitalReagentEntity;
import com.ebig.hdi.modules.reagent.entity.GoodsHospitalReagentSpecsEntity;
import com.ebig.hdi.modules.reagent.service.GoodsHospitalReagentService;
import com.ebig.hdi.modules.reagent.service.GoodsHospitalReagentSpecsService;
import com.ebig.hdi.modules.sys.entity.SysDictEntity;
import com.ebig.hdi.modules.sys.service.SysConfigService;
import com.ebig.hdi.modules.sys.service.SysDictService;
import com.ebig.hdi.modules.sys.service.SysSequenceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 抽取商品临时表数据——医院商品
 * 
 * @author clang
 *
 */

@Component("hospitalGoodsInputTask")
@Slf4j
public class HospitalGoodsInputTask implements ITask {

	@Autowired
	private TempPubGoodsDao tempPubGoodsDao;
	@Autowired
	private SysSequenceService sysSequenceService;
	@Autowired
	private GoodsHospitalConsumablesService goodsHospitalConsumablesService;
	@Autowired
	private GoodsHospitalConsumablesSpecsService goodsHospitalConsumablesSpecsService;
	@Autowired
	private GoodsHospitalConsumablesApprovalsService goodsHospitalConsumablesApprovalsService;
	@Autowired
	private GoodsHospitalReagentService goodsHospitalReagentService;
	@Autowired
	private GoodsHospitalReagentSpecsService goodsHospitalReagentSpecsService;
	@Autowired
	private GoodsHospitalDrugsService goodsHospitalDrugsService;
	@Autowired
	private GoodsHospitalDrugsSpecsService goodsHospitalDrugsSpecsService;
	@Autowired
	private OrgFactoryInfoApprovalService orgFactoryInfoApprovalService;
	@Autowired
	private SysConfigService sysConfigService;
	@Autowired
	private SysDictService sysDictService;

	private static boolean flag = false;

	private static final String  SPECS = "specs";

	private static final String  SOURCES_SPECS_ID= "sources_specs_id";


	@Override
	@Transactional(rollbackFor = Exception.class)
	public void run(ScheduleJobEntity scheduleJob) {
		synchronized (HospitalGoodsInputTask.class) {
			if (flag) {
				log.info("hospitalGoodsInputTask定时任务已在执行，为避免重复处理数据，将退出本次任务！");
				return;
			}
			flag = true;
		}
		
		try {
			log.info("hospitalGoodsInputTask定时任务正在执行，参数为：{}", scheduleJob.getParams());
			// 开始插入数据
			inputHospitalGoods();
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			flag = false;
		}
	}

	/**
	 * 把临时表里的医院商品数据转化为mcp里对应的医院商品数据
	 */
	private void inputHospitalGoods() {
		List<TempPubGoodsEntity> sourceGoodsList = tempPubGoodsDao
				.queryAllGoodsByType(CompanyTypeEnum.HOSPITAL.getKey());
		if (sourceGoodsList != null) {
			// 获取spd对应耗材的采购类别
			String sourceConsumables = sysConfigService.getValue("SOURCE_CONSUMABLES");
			List<String> sourceConsumablesList = new ArrayList<>();
			// 获取spd对应药品的采购类别
			String sourceDrugs = sysConfigService.getValue("SOURCE_DRUGS");
			List<String> sourceDrugsList = new ArrayList<>();
			// 获取spd对应试剂的采购类别
			String sourceReagent = sysConfigService.getValue("SOURCE_REAGENT");
			List<String> sourceReagentList = new ArrayList<>();

			// 判断配置表里是否有配置，如果有配置则放进集合里
			if (StringUtils.isBlank(sourceConsumables)) {
				log.error("没有在sys_config表里配置SOURCE_CONSUMABLES,hospitalGoodsInputTask定时器执行失败!");
				return;
			} else {
				String[] sourceConsumablesArr = sourceConsumables.split(",");
				Collections.addAll(sourceConsumablesList, sourceConsumablesArr);
			}
			if (StringUtils.isBlank(sourceDrugs)) {
				log.error("没有在sys_config表里配置SOURCE_DRUGS,hospitalGoodsInputTask定时器执行失败!");
				return;
			} else {
				String[] sourceDrugsArr = sourceDrugs.split(",");
				Collections.addAll(sourceDrugsList, sourceDrugsArr);
			}
			if (StringUtils.isBlank(sourceReagent)) {
				log.error("没有在sys_config表里配置SOURCE_REAGENT,hospitalGoodsInputTask定时器执行失败!");
				return;
			} else {
				String[] sourceReagentArr = sourceReagent.split(",");
				Collections.addAll(sourceReagentList, sourceReagentArr);
			}

			for (TempPubGoodsEntity sourceGoods : sourceGoodsList) {
				if (sourceGoods.getGoodscategorytype() == null) {
					log.error("temp_pub_goods 表中mgoodsid为" + sourceGoods.getMgoodsid() + " 数据的商品所属类别为空，该数据插入失败 !");
					continue;
				}
				// 获取厂商id
				String factoryId = null;
				if (StringUtils.isNotBlank(sourceGoods.getFactoryname())) {
					try {
						List<OrgFactoryInfoApprovalEntity> factoryList = orgFactoryInfoApprovalService
								.selectList(new EntityWrapper<OrgFactoryInfoApprovalEntity>()
										.eq("factory_name", sourceGoods.getFactoryname()).eq("del_flag", 0));
						if (StringUtil.isEmpty(factoryList)) {
							factoryId = createFactory(sourceGoods.getFactoryname()).toString();
						} else {
							factoryId = factoryList.get(0).getId().toString();
						}
					} catch (Exception e) {
						log.error("厂商:" + sourceGoods.getFactoryname() + ",获取厂商id失败，请手动维护厂商信息", e);
					}
				}

				try {
					if (sourceConsumablesList.contains(sourceGoods.getGoodscategorytype().toString())) {
						// 保存或修改医院耗材商品
						insertOrUpdateConsumables(sourceGoods, factoryId);

					} else if (sourceReagentList.contains(sourceGoods.getGoodscategorytype().toString())) {
						// 保存或修改医院试剂商品
						insertOrUpdateReagent(sourceGoods, factoryId);

					} else if (sourceDrugsList.contains(sourceGoods.getGoodscategorytype().toString())) {
						// 保存或修改医院药品商品
						insertOrUpdateDrugs(sourceGoods, factoryId);

					}
					// 删除临时表数据
					tempPubGoodsDao.deleteById(sourceGoods.getMgoodsid());
				} catch (Exception e) {
					log.error("temp_pub_goods 表中mgoodsid为" + sourceGoods.getMgoodsid() + " 数据插入失败 !", e);
				}

			}
		}

	}
	private void insertOrUpdateConsumables(TempPubGoodsEntity sourceGoods, String factoryId) {
		// 判断规格是否存在
		List<GoodsHospitalConsumablesSpecsEntity> specsList = goodsHospitalConsumablesSpecsService
				.selectList(new EntityWrapper<GoodsHospitalConsumablesSpecsEntity>()
						.eq(SOURCES_SPECS_ID, sourceGoods.getMgoodsid()).eq(SPECS, sourceGoods.getGoodstype()));
		if (!StringUtil.isEmpty(specsList)) {
			// 规格存在，更新规格
			GoodsHospitalConsumablesSpecsEntity specsUpdate = specsList.get(0);
			specsUpdate.setSourcesSpecsId(sourceGoods.getMgoodsid());
			// SPD、DSC商品编码对应的是MCP商品规格编码
			specsUpdate.setSpecsCode(sourceGoods.getGoodsno());
			specsUpdate.setSpecs(sourceGoods.getGoodstype());
			specsUpdate.setStatus(StatusEnum.USABLE.getKey());
			specsUpdate.setEditTime(new Date());
			goodsHospitalConsumablesSpecsService.updateById(specsUpdate);
			// 更新商品
			GoodsHospitalConsumablesEntity consumablesUpdate = goodsHospitalConsumablesService
					.selectById(specsUpdate.getConsumablesId());
			updateConsumables(sourceGoods, factoryId, consumablesUpdate);

			insertConsumablesApprovals(sourceGoods, consumablesUpdate);
			return;
		}

		// 规格不存在，判断商品是否存在
		GoodsHospitalConsumablesEntity consumables = goodsHospitalConsumablesService
				.selectByGoodsNameAndFactoryNameAndHospitalId(sourceGoods.getGoodsname(), sourceGoods.getFactoryname(),
						sourceGoods.getHospitalId());
		if (!StringUtil.isEmpty(consumables)) {
			updateConsumables(sourceGoods, factoryId, consumables);
		} else {
			// 商品不存在，插入商品
			consumables = new GoodsHospitalConsumablesEntity();
			consumables.setHospitalId(sourceGoods.getHospitalId());
			consumables.setConsumablesCode(
					sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.HOSPITAL_CONSUMABLES_CODE.getKey()));
			consumables.setConsumablesName(sourceGoods.getGoodsname());
			consumables.setCommonName(sourceGoods.getCommonname());
			consumables.setFactoryId(factoryId);
			consumables.setStatus(StatusEnum.USABLE.getKey());
			consumables.setGoodsUnit(checkGoodsUnit(sourceGoods.getGoodsunit()));
			consumables.setPicUrl(sourceGoods.getPhoto());
			consumables.setDeptId(sourceGoods.getDeptId());
			consumables.setDelFlag(DelFlagEnum.NORMAL.getKey());
			consumables.setIsMatch(IsMatchEnum.NO.getKey());
			consumables.setDataSource(HospitalDataSource.SPD.getKey());
			consumables.setCreateTime(new Date());
			goodsHospitalConsumablesService.insert(consumables);
		}
		// 插入规格
		GoodsHospitalConsumablesSpecsEntity specsInsert = new GoodsHospitalConsumablesSpecsEntity();
		specsInsert.setSourcesSpecsId(sourceGoods.getMgoodsid());
		specsInsert.setConsumablesId(consumables.getId());
		// SPD、DSC商品编码对应的是MCP商品规格编码
		specsInsert.setSpecsCode(sourceGoods.getGoodsno());
		specsInsert.setSpecs(sourceGoods.getGoodstype());
		specsInsert.setStatus(StatusEnum.USABLE.getKey());
		specsInsert.setCreateTime(new Date());
		goodsHospitalConsumablesSpecsService.insert(specsInsert);

		// 批文不存在则插入批文
		insertConsumablesApprovals(sourceGoods, consumables);
	}

	private void insertConsumablesApprovals(TempPubGoodsEntity sourceGoods,
			GoodsHospitalConsumablesEntity consumables) {
		if (!StringUtil.isEmpty(sourceGoods.getRegisterdocno())) {
			String approvalsName = sourceGoods.getRegisterdocno();
			GoodsHospitalConsumablesApprovalsEntity approvals = new GoodsHospitalConsumablesApprovalsEntity();
			List<GoodsHospitalConsumablesApprovalsEntity> approvalsList = goodsHospitalConsumablesApprovalsService
					.selectList(new EntityWrapper<GoodsHospitalConsumablesApprovalsEntity>()
							.eq("consumables_id", consumables.getId()).eq("approvals", approvalsName));
			if (StringUtil.isEmpty(approvalsList)) {
				approvals.setConsumablesId(consumables.getId());
				approvals.setApprovals(approvalsName);
				approvals.setStatus(StatusEnum.USABLE.getKey());
				approvals.setCreateTime(new Date());
				goodsHospitalConsumablesApprovalsService.insert(approvals);
			}

		}
	}

	private void updateConsumables(TempPubGoodsEntity sourceGoods, String factoryId,
			GoodsHospitalConsumablesEntity consumablesUpdate) {
		consumablesUpdate.setHospitalId(sourceGoods.getHospitalId());
		consumablesUpdate.setConsumablesCode(
				sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.HOSPITAL_CONSUMABLES_CODE.getKey()));
		consumablesUpdate.setConsumablesName(sourceGoods.getGoodsname());
		consumablesUpdate.setCommonName(sourceGoods.getCommonname());
		consumablesUpdate.setFactoryId(factoryId);
		consumablesUpdate.setGoodsUnit(checkGoodsUnit(sourceGoods.getGoodsunit()));
		consumablesUpdate.setPicUrl(sourceGoods.getPhoto());
		consumablesUpdate.setDeptId(sourceGoods.getDeptId());
		consumablesUpdate.setIsMatch(IsMatchEnum.NO.getKey());
		consumablesUpdate.setEditTime(new Date());
		goodsHospitalConsumablesService.updateById(consumablesUpdate);
	}
	private void insertOrUpdateDrugs(TempPubGoodsEntity sourceGoods, String factoryId) {
		// 判断规格是否存在
		List<GoodsHospitalDrugsSpecsEntity> specsList = goodsHospitalDrugsSpecsService
				.selectList(new EntityWrapper<GoodsHospitalDrugsSpecsEntity>()
						.eq(SOURCES_SPECS_ID, sourceGoods.getMgoodsid()).eq(SPECS, sourceGoods.getGoodstype()));
		if (!StringUtil.isEmpty(specsList)) {
			// 规格存在，更新规格
			GoodsHospitalDrugsSpecsEntity specsUpdate = specsList.get(0);
			specsUpdate.setSourcesSpecsId(sourceGoods.getMgoodsid());
			// SPD、DSC商品编码对应的是MCP商品规格编码
			specsUpdate.setSpecsCode(sourceGoods.getGoodsno());
			specsUpdate.setSpecs(sourceGoods.getGoodstype());
			specsUpdate.setStatus(StatusEnum.USABLE.getKey());
			specsUpdate.setEditTime(new Date());
			goodsHospitalDrugsSpecsService.updateById(specsUpdate);
			// 更新商品
			GoodsHospitalDrugsEntity drugsUpdate = goodsHospitalDrugsService.selectById(specsUpdate.getDrugsId());
			updateDrugs(sourceGoods, factoryId, drugsUpdate);
			return;
		}

		// 规格不存在，判断商品是否存在
		GoodsHospitalDrugsEntity drugs = goodsHospitalDrugsService.selectByGoodsNameAndFactoryNameAndHospitalId(
				sourceGoods.getGoodsname(), sourceGoods.getFactoryname(), sourceGoods.getHospitalId());

		if (!StringUtil.isEmpty(drugs)) {
			updateDrugs(sourceGoods, factoryId, drugs);
		} else {
			// 商品不存在，插入商品
			drugs = new GoodsHospitalDrugsEntity();
			if (StringUtil.isEmpty(sourceGoods.getApprovedocno())){
				drugs.setApprovals(sourceGoods.getRegisterdocno());
			}else {
				drugs.setApprovals(sourceGoods.getApprovedocno());
			}
			drugs.setHospitalId(sourceGoods.getHospitalId());
			drugs.setDrugsCode(sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.HOSPITAL_DRUGS_CODE.getKey()));
			drugs.setDrugsName(sourceGoods.getGoodsname());
			drugs.setCommonName(sourceGoods.getCommonname());
			drugs.setFactoryId(factoryId);
			drugs.setStatus(StatusEnum.USABLE.getKey());
			drugs.setGoodsUnit(checkGoodsUnit(sourceGoods.getGoodsunit()));
			drugs.setPicUrl(sourceGoods.getPhoto());
			drugs.setDeptId(sourceGoods.getDeptId());
			drugs.setDelFlag(DelFlagEnum.NORMAL.getKey());
			drugs.setIsMatch(IsMatchEnum.NO.getKey());
			drugs.setDataSource(HospitalDataSource.SPD.getKey());
			drugs.setCreateTime(new Date());
			goodsHospitalDrugsService.insert(drugs);
		}
		// 插入规格
		GoodsHospitalDrugsSpecsEntity specsInsert = new GoodsHospitalDrugsSpecsEntity();
		specsInsert.setSourcesSpecsId(sourceGoods.getMgoodsid());
		specsInsert.setDrugsId(drugs.getId());
		// SPD、DSC商品编码对应的是MCP商品规格编码
		specsInsert.setSpecsCode(sourceGoods.getGoodsno());
		specsInsert.setSpecs(sourceGoods.getGoodstype());
		specsInsert.setStatus(StatusEnum.USABLE.getKey());
		specsInsert.setCreateTime(new Date());
		goodsHospitalDrugsSpecsService.insert(specsInsert);

	}

	private void updateDrugs(TempPubGoodsEntity sourceGoods, String factoryId, GoodsHospitalDrugsEntity drugsUpdate) {
		if (StringUtil.isEmpty(sourceGoods.getApprovedocno())){
			drugsUpdate.setApprovals(sourceGoods.getRegisterdocno());
		}else {
			drugsUpdate.setApprovals(sourceGoods.getApprovedocno());
		}
		drugsUpdate.setHospitalId(sourceGoods.getHospitalId());
		drugsUpdate.setDrugsCode(sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.HOSPITAL_DRUGS_CODE.getKey()));
		drugsUpdate.setDrugsName(sourceGoods.getGoodsname());
		drugsUpdate.setCommonName(sourceGoods.getCommonname());
		drugsUpdate.setFactoryId(factoryId);
		drugsUpdate.setGoodsUnit(checkGoodsUnit(sourceGoods.getGoodsunit()));
		drugsUpdate.setPicUrl(sourceGoods.getPhoto());
		drugsUpdate.setDeptId(sourceGoods.getDeptId());
		drugsUpdate.setIsMatch(IsMatchEnum.NO.getKey());
		drugsUpdate.setEditTime(new Date());
		goodsHospitalDrugsService.updateById(drugsUpdate);
	}
	private void insertOrUpdateReagent(TempPubGoodsEntity sourceGoods, String factoryId) {
		// 判断规格是否存在
		List<GoodsHospitalReagentSpecsEntity> specsList = goodsHospitalReagentSpecsService
				.selectList(new EntityWrapper<GoodsHospitalReagentSpecsEntity>()
						.eq(SOURCES_SPECS_ID, sourceGoods.getMgoodsid()).eq(SPECS, sourceGoods.getGoodstype()));
		if (!StringUtil.isEmpty(specsList)) {
			// 规格存在，更新规格
			GoodsHospitalReagentSpecsEntity specsUpdate = specsList.get(0);
			specsUpdate.setSourcesSpecsId(sourceGoods.getMgoodsid());
			// SPD、DSC商品编码对应的是MCP商品规格编码
			specsUpdate.setSpecsCode(sourceGoods.getGoodsno());
			specsUpdate.setSpecs(sourceGoods.getGoodstype());
			specsUpdate.setEditTime(new Date());
			goodsHospitalReagentSpecsService.updateById(specsUpdate);
			// 更新商品
			GoodsHospitalReagentEntity reagentUpdate = goodsHospitalReagentService
					.selectById(specsUpdate.getReagenId());
			updateReagent(sourceGoods, factoryId, reagentUpdate);
			return;
		}

		// 规格不存在，判断商品是否存在
		GoodsHospitalReagentEntity reagent = goodsHospitalReagentService.selectByGoodsNameAndFactoryNameAndHospitalId(
				sourceGoods.getGoodsname(), sourceGoods.getFactoryname(), sourceGoods.getHospitalId());
		if (!StringUtil.isEmpty(reagent)) {
			updateReagent(sourceGoods, factoryId, reagent);
		} else {
			// 商品不存在，插入商品
			reagent = new GoodsHospitalReagentEntity();
			if (StringUtil.isEmpty(sourceGoods.getApprovedocno())){
				reagent.setApprovals(sourceGoods.getRegisterdocno());
			}else {
				reagent.setApprovals(sourceGoods.getApprovedocno());
			}
			reagent.setHospitalId(sourceGoods.getHospitalId());
			reagent.setReagentCode(
					sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.HOSPITAL_REAGENT_SPECS_CODE.getKey()));
			reagent.setReagentName(sourceGoods.getGoodsname());
			reagent.setCommonName(sourceGoods.getCommonname());
			reagent.setFactoryId(factoryId);
			reagent.setStatus(StatusEnum.USABLE.getKey());
			reagent.setGoodsUnit(checkGoodsUnit(sourceGoods.getGoodsunit()));
			reagent.setPicUrl(sourceGoods.getPhoto());
			reagent.setDeptId(sourceGoods.getDeptId());
			reagent.setDelFlag(DelFlagEnum.NORMAL.getKey());
			reagent.setIsMatch(IsMatchEnum.NO.getKey());
			reagent.setDataSource(HospitalDataSource.SPD.getKey());
			reagent.setCreateTime(new Date());
			goodsHospitalReagentService.insert(reagent);
		}
		// 插入规格
		GoodsHospitalReagentSpecsEntity specsInsert = new GoodsHospitalReagentSpecsEntity();
		specsInsert.setSourcesSpecsId(sourceGoods.getMgoodsid());
		specsInsert.setReagenId(reagent.getId());
		specsInsert.setSpecsCode(sourceGoods.getGoodsno());
		specsInsert.setSpecs(sourceGoods.getGoodstype());
		specsInsert.setStatus(StatusEnum.USABLE.getKey());
		specsInsert.setCreateTime(new Date());
		goodsHospitalReagentSpecsService.insert(specsInsert);

	}

	private void updateReagent(TempPubGoodsEntity sourceGoods, String factoryId,
			GoodsHospitalReagentEntity reagentUpdate) {
		if (StringUtil.isEmpty(sourceGoods.getApprovedocno())){
			reagentUpdate.setApprovals(sourceGoods.getRegisterdocno());
		}else {
			reagentUpdate.setApprovals(sourceGoods.getApprovedocno());
		}
		reagentUpdate.setHospitalId(sourceGoods.getHospitalId());
		reagentUpdate.setReagentCode(
				sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.HOSPITAL_REAGENT_CODE.getKey()));
		reagentUpdate.setReagentName(sourceGoods.getGoodsname());
		reagentUpdate.setCommonName(sourceGoods.getCommonname());
		reagentUpdate.setFactoryId(factoryId);
		reagentUpdate.setGoodsUnit(checkGoodsUnit(sourceGoods.getGoodsunit()));
		reagentUpdate.setPicUrl(sourceGoods.getPhoto());
		reagentUpdate.setDeptId(sourceGoods.getDeptId());
		reagentUpdate.setIsMatch(IsMatchEnum.NO.getKey());
		reagentUpdate.setEditTime(new Date());
		goodsHospitalReagentService.updateById(reagentUpdate);
	}
	private String checkGoodsUnit(String srcGoodsUnit) {
		SysDictEntity entity = sysDictService.checkGoodsUnit(srcGoodsUnit);
		if(entity != null){
			return entity.getCode();
		}
		return null;
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
		return factoryInfo.getId();

	}
}
