package com.ebig.hdi.modules.reagent.service.impl;
import	java.lang.Thread.State;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.constant.ActivitiConstant;
import com.ebig.hdi.common.enums.*;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.StringUtil;

import com.ebig.hdi.modules.activiti.entity.ActApprovalEntity;
import com.ebig.hdi.modules.activiti.service.ActApprovalService;
import com.ebig.hdi.modules.reagent.common.UnicodeGoodsUnitCode;
import com.ebig.hdi.modules.reagent.dao.GoodsPlatformReagentApprovalDao;
import com.ebig.hdi.modules.reagent.dao.GoodsPlatformReagentSpecsDao;

import com.ebig.hdi.modules.reagent.entity.*;
import com.ebig.hdi.modules.reagent.param.GoodsPlatformReagentParam;
import com.ebig.hdi.modules.reagent.service.*;
import com.ebig.hdi.modules.reagent.vo.GoodsPlatformReagentVO;
import com.ebig.hdi.modules.sys.entity.SysDictEntity;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import com.ebig.hdi.modules.sys.service.SysDictService;
import com.ebig.hdi.modules.sys.service.SysSequenceService;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;


@SuppressWarnings("AlibabaCollectionInitShouldAssignCapacity")
@Service("goodsPlatformReagentApprovalService")
public class GoodsPlatformReagentApprovalServiceImpl extends ServiceImpl<GoodsPlatformReagentApprovalDao, GoodsPlatformReagentApprovalEntity> implements GoodsPlatformReagentApprovalService {

	private static final Logger logger = LoggerFactory.getLogger(GoodsPlatformReagentApprovalServiceImpl.class);

	@Autowired
	private OrgFactorysInfoService orgFactorysInfoService;
	@Autowired
	private GoodsPlatformReagentSpecsService goodsPlatformReagentSpecsService;
	@Autowired
	private SysSequenceService sysSequenceService;
	@Autowired
	private UnicodeReagentCateService unicodeReagentCateService;
	@Autowired
	private ActApprovalService actApprovalService;

	@Autowired
	private GoodsPlatformReagentService goodsPlatformReagentService;

	@Autowired
	private SysDictService sysDictService;


	@Autowired
	private GoodsPlatformReagentSpecsDao goodsPlatformReagentSpecsDao;

	@Override
	@DataFilter(subDept = true, user = false, tableAlias = "t1")
	public PageUtils queryPage(Map<String, Object> params) {
		int currPage = Integer.parseInt(params.get("page").toString());
		int pageSize = Integer.parseInt(params.get("limit").toString());

		Page<GoodsPlatformReagentVO> page = new Page<GoodsPlatformReagentVO>(currPage, pageSize);
		if (params != null && params.get("typeId") != null){
			List<Long> cateIds = new ArrayList<>();
			cateIds.add(Long.parseLong(params.get("typeId").toString()));
			 unicodeReagentCateService.queryList(Long.valueOf(params.get("typeId").toString()),cateIds);
			params.put("typeId",cateIds);
		}
		List<GoodsPlatformReagentVO> list = this.baseMapper.selectPlatformReagentList(page, params);

		for(GoodsPlatformReagentVO goodsPlatformReagent : list) {
			List<GoodsPlatformReagentSpecsEntity> specsList = goodsPlatformReagentSpecsService.selectListByReagentId(goodsPlatformReagent.getId());
			goodsPlatformReagent.setSpecsEntityList(specsList);
		}

		page.setRecords(list);
		return new PageUtils(page);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(GoodsPlatformReagentVO goodsPlatformReagentVO, SysUserEntity sysUserEntity) {
		if(checkReagentNameOnly(goodsPlatformReagentVO)){
			throw new HdiException("本试剂名称已存在");
		}
		if((this.baseMapper.selectCountByApprovals(goodsPlatformReagentVO.getApprovals())) > 0)
		{
			throw new HdiException("批文不唯一");
		}
		goodsPlatformReagentVO.setReagentCode(createReagentCode());
		goodsPlatformReagentVO.setGoodsUnicode(createGoodsUnicode(goodsPlatformReagentVO,getFactorysInfoById(goodsPlatformReagentVO.getFactoryId())));
		goodsPlatformReagentVO.setCreateTime(new Date());
		baseMapper.insert(goodsPlatformReagentVO);
		goodsPlatformReagentSpecsService.save(goodsPlatformReagentVO);
		startApprovalProcess(goodsPlatformReagentVO,sysUserEntity,ChangeTypeEnum.ADD.getKey());
	}

	private boolean checkApprovalsOnly(GoodsPlatformReagentVO goodsPlatformReagentVO) {
		return (this.baseMapper.checkApprovalsOnly(goodsPlatformReagentVO.getApprovals(),goodsPlatformReagentVO.getId()) >0) ? true : false;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Map goodsPlatformReagentImportData(String[][] rows, SysUserEntity sysUserEntity) {
		if (StringUtil.isEmpty(sysUserEntity) || rows.length <= 1) {
			throw new HdiException("系统错误！!");
		}
		ArrayList<GoodsPlatformReagentVO>  goodsPlatformReagentVOs =  new ArrayList(rows.length);
		StringBuilder errorMessage = new StringBuilder();
		String[] row;
		String  SUCCESSCOUNT = "successCount",FAILCOUNT = "failCount",ERRORMESSAGE = "errorMessage";
		Integer failCount = 0, successCount = 0,specsCount =0;
		Map<String, Object> results = new HashMap<>(16);
		if (!TypeEnum.USER_PLATFORM.getKey().equals(sysUserEntity.getUserType())) {
			throw new HdiException("当前登录用户无此权限");
		}
		for (int i = 1; i < rows.length; i++) {
			row = rows[i];
			GoodsPlatformReagentVO goodsPlatformReagent = new GoodsPlatformReagentVO();
			if(checkIsEmpty(row)){
                errorMessage.append(" \n 第 ").append( i ).append( " 行数据错误，必填字段不能为空");
                failCount++;
                continue;
            }
			// 校验 名称 + 批准文号
			 goodsPlatformReagent = this.baseMapper.selectByReagentNameAndApprovals(row[0], row[5]);
			if(!StringUtil.isEmpty(goodsPlatformReagent)){
				goodsPlatformReagent.setGuid(row[7]);
				goodsPlatformReagent.setSpecs(row[3]);
				// 校验规格
				GoodsPlatformReagentSpecsEntity goodsPlatformReagentSpecsEntity = goodsPlatformReagentSpecsService.selectByReagentIdAndSpecs(goodsPlatformReagent.getId(), row[3]);
				if (StringUtil.isEmpty(goodsPlatformReagentSpecsEntity)) {
					if (!StringUtil.isEmpty(row[7])){
						if (!checkGuidOnly(row[7])) {
							goodsPlatformReagentSpecsService.save(goodsPlatformReagent);
						}
					}else {
						goodsPlatformReagentSpecsService.save(goodsPlatformReagent);
					}
				}
			}else {
				GoodsPlatformReagentVO goodsPlatformReagentVO = new GoodsPlatformReagentVO();
				if (StringUtil.isEmpty(UnicodeGoodsUnitCode.getUnicodeReagentId(row[2]))) {
					errorMessage.append("\n 第 ").append(i).append(" 数据的商品分类不存在 : ").append(row[2]);
					failCount++;
					continue;
				}
				OrgFactorysInfoEntity factory = getFactorysInfoByName(row[4]);
				if (StringUtil.isEmpty(factory)) {
					errorMessage.append("\n 第 ").append(i).append(" 数据的生产厂商未在系统建档 :").append(row[4]);
					failCount++;
					continue;
				}
				arrayConvertEntity(goodsPlatformReagentVO, row, factory, sysUserEntity);
				if (checkReagentNameOnly(goodsPlatformReagentVO)) {
					errorMessage.append("\n 第 ").append(i).append(" 数据的试剂名称已经存在 : ").append(goodsPlatformReagentVO.getReagentName());
					failCount++;
					continue;
				}
				if ("国产".equals(row[1])) {
					goodsPlatformReagentVO.setGoodsNature(0);
				} else if ("进口".equals(row[1])){
					goodsPlatformReagentVO.setGoodsNature(1);
				}else{
					errorMessage.append("\n 第 ").append(i).append(" 商品属性错误 : ").append(row[1]);
					failCount++;
					continue;
				}
				if ((this.baseMapper.selectCountByApprovals(goodsPlatformReagentVO.getApprovals())) > 0) {
					errorMessage.append("\n 第 ").append(i).append(" 数据的注册批号已经存在 : ").append(goodsPlatformReagentVO.getApprovals());
					failCount++;
					continue;
				}
				if (!StringUtil.isEmpty(row[7])) {
					if (checkGuidOnly(row[7])) {
						errorMessage.append("\n 第 ").append(i).append(" 数据的全球唯一码已经存在 : ").append(row[7]);
						failCount++;
						continue;
					}
					goodsPlatformReagentVO.setGuid(row[7]);
				}
				this.baseMapper.insert(goodsPlatformReagentVO);
				goodsPlatformReagentSpecsService.save(goodsPlatformReagentVO);
				startApprovalProcess(goodsPlatformReagentVO,sysUserEntity,ChangeTypeEnum.ADD.getKey());
			}
		}
		if (failCount > 0){

			StringBuilder sb = new StringBuilder();
			sb.append("导入失败！").append(errorMessage);
			throw new HdiException(sb.toString());
		}
		successCount = rows.length - 1;

		results.put(ERRORMESSAGE,errorMessage.toString());
		results.put(SUCCESSCOUNT,successCount.toString());
		results.put(FAILCOUNT, failCount.toString());
		return results;
	}

    @Override
	@DataFilter(subDept = true, user = false, tableAlias = "t1")
	public List<Map<String, Object>> getList(Map map) {
		GoodsPlatformReagentParam goodsPlatformReagentParam =  (GoodsPlatformReagentParam)map.get("queryParam");
        if (!(StringUtils.isEmpty(goodsPlatformReagentParam) || StringUtils.isEmpty(goodsPlatformReagentParam.getTypeId()))){
            Map<String, Object> pcateIMap = new HashMap<>();
			pcateIMap.put("pcateId",goodsPlatformReagentParam.getTypeId());
            List<Long> cateIds = new ArrayList<>();
            cateIds.add(Long.parseLong(goodsPlatformReagentParam.getTypeId()));
            List<ReagentTreeNode> list = unicodeReagentCateService.queryNode(pcateIMap);
            if (CollectionUtils.isNotEmpty(list)) {
                for (ReagentTreeNode node: list ) {
                    cateIds.add(node.getCateId());
                }
            }
            goodsPlatformReagentParam.setTypeIds(cateIds);
        }


        List<Map<String, Object>> list = this.baseMapper.selectPlatformReagent(map);
       for(Map<String, Object>  date : list) {
		   date.put("typeId",UnicodeGoodsUnitCode.getUnicodeReagentMap((Long) date.get("type_id")));
        }
        return list;
    }

    @Override
	@Transactional(rollbackFor = Exception.class)
	public void update(GoodsPlatformReagentVO goodsPlatformReagentVO ,SysUserEntity sysUserEntity) {
		GoodsPlatformReagentApprovalEntity  goodsPlatformReagentApprovalEntity= this.baseMapper.selectById(goodsPlatformReagentVO.getId());
		if(!goodsPlatformReagentApprovalEntity.getReagentName().equals(goodsPlatformReagentVO.getReagentName())){
			if(checkReagentNameOnly(goodsPlatformReagentVO)){
				throw new HdiException("本试剂名称已存在");
			}
		}
		if(checkApprovalsOnly(goodsPlatformReagentVO)){
			throw new HdiException("批文不唯一");
		}
		// 生成试剂统一编码
		String factoryCode = orgFactorysInfoService.selectById(goodsPlatformReagentVO.getFactoryId()).getFactoryCode();
		String goodsUnicode = unicodeReagentCateService.generatorGoodsUnicode(goodsPlatformReagentVO.getGoodsNature(), goodsPlatformReagentVO.getTypeId(), goodsPlatformReagentVO.getReagentCode(), factoryCode);
		goodsPlatformReagentVO.setGoodsUnicode(goodsUnicode);
		goodsPlatformReagentVO.setEditTime(new Date());
		baseMapper.updateById(goodsPlatformReagentVO);
		if (ApprovalTypeEnum.FAIL.getKey().equals(goodsPlatformReagentVO.getCheckStatus())) {
			startApprovalProcess(goodsPlatformReagentVO, sysUserEntity, ChangeTypeEnum.UPDATE.getKey());
		}
		if(ApprovalTypeEnum.PASS.getKey().equals(goodsPlatformReagentVO.getCheckStatus())){
			goodsPlatformReagentService.updateByFactoryInfoApproval(this.baseMapper.selectById(goodsPlatformReagentVO.getId()));

		}
    }

	@Override
	public Map<String, Object> checkStatus(Map<String, Object> params, SysUserEntity user) {
		Integer checkStatus = (Integer) params.get("checkStatus");
		Map<String, Object> map = new HashMap<>(16);
		if (StringUtil.isEmpty(checkStatus)) {
			map.put("errorMessage", "状态为空");
			return map;
		}
		Integer id = (Integer) params.get("goodsId");
		if (StringUtil.isEmpty(id)) {
			map.put("errorMessage", "传入ID为空");
			return map;
		}
		GoodsPlatformReagentApprovalEntity goodsPlatformReagentApprovalEntity = this.baseMapper.selectById(id);
		if (checkStatus.equals(ApprovalTypeEnum.PASS.getKey())) {
			//审核通过（先修改待审批表）
			goodsPlatformReagentApprovalEntity.setCheckStatus(ApprovalTypeEnum.PASS.getKey());
			goodsPlatformReagentApprovalEntity.setEditTime(new Date());
			goodsPlatformReagentApprovalEntity.setEditId(user.getUserId());
			this.updateById(goodsPlatformReagentApprovalEntity);
			//根据耗材编码查询原表是否有数据
			GoodsPlatformReagentApprovalEntity goodsPlatformReagentApproval=this.baseMapper.selectListByPlatformReagentCode(goodsPlatformReagentApprovalEntity.getReagentCode());
			//表中没数据则新增
			if (StringUtils.isEmpty(goodsPlatformReagentApproval)) {
				goodsPlatformReagentService.insertByFactoryInfoApproval(goodsPlatformReagentApprovalEntity);
			} else {
				//表中有数据则更新
				goodsPlatformReagentService.updateByFactoryInfoApproval(goodsPlatformReagentApprovalEntity);
			}
		}
		if (checkStatus.equals(ApprovalTypeEnum.FAIL.getKey())) {
			//审核不通过(修改待审批平台耗材表审批状态)
			goodsPlatformReagentApprovalEntity.setCheckStatus(ApprovalTypeEnum.FAIL.getKey());
			goodsPlatformReagentApprovalEntity.setEditTime(new Date());
			goodsPlatformReagentApprovalEntity.setEditId(user.getUserId());
			this.baseMapper.updateById(goodsPlatformReagentApprovalEntity);
		}

		return map;
	}

	@Override
	public GoodsPlatformReagentVO selectById(Long id) {
		return baseMapper.selectPlatformReagentById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void toggle(Map<String, Object> params) {
		List<Long> ids = JSON.parseArray(params.get("ids").toString(), Long.class);
	  	Integer	status =Integer.valueOf(params.get("status").toString());
		for(Long id : ids) {
			GoodsPlatformReagentApprovalEntity goodsPlatformReagentApproval = new GoodsPlatformReagentApprovalEntity();
			GoodsPlatformReagentEntity goodsPlatformReagent = new GoodsPlatformReagentEntity();
			goodsPlatformReagent = goodsPlatformReagentService.selectById(id);
			if(!StringUtils.isEmpty(goodsPlatformReagent)){
				goodsPlatformReagent.setStatus(status);
				goodsPlatformReagentService.updateById(goodsPlatformReagent);
			}
			goodsPlatformReagentApproval.setId(id);
			goodsPlatformReagentApproval.setStatus(status);
			this.baseMapper.updateById(goodsPlatformReagentApproval);
		}
	}

	@Override
	public void delete(Long[] ids) {
		for (Long id : ids) {
			GoodsPlatformReagentApprovalEntity GoodsPlatformReagent = this.baseMapper.selectById(id);
			if (!GoodsPlatformReagent.getCheckStatus().equals(ApprovalTypeEnum.PASS.getKey())){
				GoodsPlatformReagent.setDelFlag(DelFlagEnum.DELETE.getKey());
				GoodsPlatformReagentEntity goodsPlatformDrugsEntity = goodsPlatformReagentService.selectById(id);
				if (!StringUtil.isEmpty(goodsPlatformDrugsEntity)) {
					goodsPlatformDrugsEntity.setDelFlag(DelFlagEnum.DELETE.getKey());
					goodsPlatformReagentService.updateById(goodsPlatformDrugsEntity);
				}
				this.baseMapper.updateById(GoodsPlatformReagent);
			}else {
				throw new HdiException("已审批通过的记录不能删除");
			}
		}

	}

	private boolean checkIsEmpty(String [] row){
		return  (StringUtil.isEmpty(row[0]) || StringUtil.isEmpty(row[1]) || StringUtil.isEmpty(row[2])
				|| StringUtil.isEmpty(row[3]) || StringUtil.isEmpty(row[4]) || StringUtil.isEmpty(row[5])
				|| StringUtil.isEmpty(row[6]) );
	}


	private void arrayConvertEntity(GoodsPlatformReagentVO goodsPlatformReagentVO, String[] row,OrgFactorysInfoEntity factory,SysUserEntity sysUserEntity) {
		goodsPlatformReagentVO.setReagentName(row[0]);
        goodsPlatformReagentVO.setTypeId(UnicodeGoodsUnitCode.getUnicodeReagentId(row[2]));
		goodsPlatformReagentVO.setSpecs(row[3]);
		goodsPlatformReagentVO.setFactoryId(factory.getId().toString());
		goodsPlatformReagentVO.setFactoryName(factory.getFactoryName());
		goodsPlatformReagentVO.setReagentCode(createReagentCode());
		goodsPlatformReagentVO.setGoodsUnit(checkGoodsUnit(row[6]));

		goodsPlatformReagentVO.setGoodsUnicode(createGoodsUnicode(goodsPlatformReagentVO,factory));
		goodsPlatformReagentVO.setApprovals(row[5]);
		goodsPlatformReagentVO.setCommonName(row[8]);
		goodsPlatformReagentVO.setStoreWay(UnicodeGoodsUnitCode.getStoreWay(row[9]));
		goodsPlatformReagentVO.setStatus(1);
		goodsPlatformReagentVO.setDeptId(sysUserEntity.getDeptId());
		goodsPlatformReagentVO.setCreateId(sysUserEntity.getUserId());
		goodsPlatformReagentVO.setCreateTime(new Date());
	}


	private boolean checkReagentNameOnly(GoodsPlatformReagentVO goodsPlatformReagentVO){
		List<GoodsPlatformReagentApprovalEntity> prList = this.selectList(new EntityWrapper<GoodsPlatformReagentApprovalEntity>()
				.eq("del_flag", DelFlagEnum.NORMAL.getKey())
				.eq("reagent_name", goodsPlatformReagentVO.getReagentName())
		);
		return prList.size() != 0;
	}
	private boolean checkGuidOnly(String guid){
		return (goodsPlatformReagentSpecsDao.selectByGuid(guid) != null);
	}

	private  String createReagentCode(){
		return sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.PLATFORM_REAGENT_CODE.getKey());
	}

	private  String createGoodsUnicode(GoodsPlatformReagentVO goodsPlatformReagentVO,OrgFactorysInfoEntity factory){
		return unicodeReagentCateService.generatorGoodsUnicode(goodsPlatformReagentVO.getGoodsNature(), goodsPlatformReagentVO.getTypeId(), goodsPlatformReagentVO.getReagentCode(), factory.getFactoryCode());
	}

	private   OrgFactorysInfoEntity getFactorysInfoById(String FactoryId){
		return orgFactorysInfoService.selectById(FactoryId);
	}

	private   OrgFactorysInfoEntity getFactorysInfoByName(String FactoryName){
		return orgFactorysInfoService.selectByName(FactoryName);
	}

	private  void startApprovalProcess(GoodsPlatformReagentVO goodsPlatformReagentVO,SysUserEntity sysUseentity,Integer flag){
		ActApprovalEntity approvalEntity = actApprovalService.getActApprovalEntity(sysUseentity, flag,
				ActTypeEnum.PLATFORM_REAGENT.getKey(), goodsPlatformReagentVO.getId().toString(),
				goodsPlatformReagentVO.getReagentCode(), goodsPlatformReagentVO.getReagentName());
		Map<String, Object> map = new HashMap<>(16);
		Map<String,String > returnInfo = new HashMap<>(16);
		map.put("approvalEntity", approvalEntity);
		actApprovalService.insert(approvalEntity);
		ProcessInstance instance = actApprovalService.startProcess(sysUseentity.getUserId().toString(),
				ActivitiConstant.ACTIVITI_ORGGOODSPLATFORMREAGENT[0], ActivitiConstant.ACTIVITI_ORGGOODSPLATFORMREAGENT[1], map);
		if (instance != null) {
			approvalEntity.setProcessId(instance.getProcessInstanceId());
			approvalEntity.setApprovalCode(instance.getProcessInstanceId());
			goodsPlatformReagentVO.setProcessId(instance.getProcessInstanceId());
		} else {
			throw new HdiException("发起审批流程失败,流程实例为空");
		}
		goodsPlatformReagentVO.setCheckStatus(ApprovalTypeEnum.WAIT.getKey());
		this.baseMapper.updateById(goodsPlatformReagentVO);
		// 更新审批流
		actApprovalService.updateById(approvalEntity);
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

}
