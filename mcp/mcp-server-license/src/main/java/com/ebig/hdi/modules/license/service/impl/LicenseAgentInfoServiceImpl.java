package com.ebig.hdi.modules.license.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.enums.DelFlagEnum;
import com.ebig.hdi.common.enums.SequenceEnum;
import com.ebig.hdi.common.enums.StatusEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.Constant;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.license.dao.LicenseAgentInfoDao;
import com.ebig.hdi.modules.license.entity.LicenseAgentInfoEntity;
import com.ebig.hdi.modules.license.entity.vo.LicenseAgentInfoVO;
import com.ebig.hdi.modules.license.service.LicenseAgentInfoService;
import com.ebig.hdi.modules.sys.service.SysSequenceService;
import com.ebig.hdi.modules.sys.service.SysUserService;


@Service("licenseAgentInfoService")
public class LicenseAgentInfoServiceImpl extends ServiceImpl<LicenseAgentInfoDao, LicenseAgentInfoEntity> implements LicenseAgentInfoService {

	@Autowired
    private SysSequenceService sysSequenceService;
	
	@Autowired
	private SysUserService sysUserService;
	
    @Override
    @DataFilter(subDept = true, user = false)
    public PageUtils queryPage(Map<String, Object> params) {
    	
    	LicenseAgentInfoVO laiVO = new LicenseAgentInfoVO();
    	if(params.get("classifyId") != null){
    		laiVO.setClassifyId(Long.valueOf(params.get("classifyId").toString()));
    	}
    	laiVO.setAgentName((String)params.get("agentName"));
    	laiVO.setNameOrNumber((String)params.get("nameOrNumber"));
    	laiVO.setBeginTimeStr((String)params.get("beginTimeStr"));
    	laiVO.setEndTimeStr((String)params.get("endTimeStr"));
    	laiVO.setLicenseStatus((Integer)params.get("licenseStatus"));
    	laiVO.setFileterDept((String)params.get(Constant.SQL_FILTER));
    	
    	String sidx = (String)params.get("sidx");
    	String order = (String)params.get("order");
    	int currPage = Integer.parseInt(params.get("page").toString());
    	int pageSize = Integer.parseInt(params.get("limit").toString());
    	
    	Page<LicenseAgentInfoVO> page = new Page<LicenseAgentInfoVO>(currPage, pageSize,order);
    	if(sidx!=null){
    		page.setAsc(sidx.equals("desc")?false:true);
    	}else{
    		//标志，设置默认按更新时间和创建时间排序
    		laiVO.setIsDefaultOrder(1);
    	}

    	List<LicenseAgentInfoVO> list = this.baseMapper.listForPage(page,laiVO);
    	
    	page.setRecords(list);
        return new PageUtils(page);
    }

	@Override
	public LicenseAgentInfoVO selectByAgentId(Long id) {
		if(id == null){
			throw new HdiException("传入的代理商证照id不能为空");
		}
		LicenseAgentInfoVO currentEntity = baseMapper.selectByAgentId(id);
		if(currentEntity.getNewLicenseId() != null){
			//设置父证照
			currentEntity.setParentAgentLicense(baseMapper.selectByAgentId(currentEntity.getNewLicenseId()));
		}
		//设置子证照
		currentEntity.setChildAgentLicense(baseMapper.selectByNewLicenseId(currentEntity.getId()));
		return currentEntity;
	}

	@Override
	public void save(LicenseAgentInfoVO licenseAgentInfoVO) {
		if(licenseAgentInfoVO != null){
			checkValue(licenseAgentInfoVO);
			setTime(licenseAgentInfoVO);
			
			if(licenseAgentInfoVO.getCreateId() == null){
				throw new HdiException("创建人id不能为空");
			}
			if(licenseAgentInfoVO.getDeptId() == null){
				throw new HdiException("机构id不能为空");
			}
			
			if(StringUtil.isEmpty(licenseAgentInfoVO.getAgentId())){
				
				List<Map<String, Object>> agentName = baseMapper.selectByAgentName(licenseAgentInfoVO.getAgentName());
				if(!StringUtil.isEmpty(agentName)){
					throw new HdiException("代理商名称已存在");
				}
				List<Map<String, Object>> creditCode = baseMapper.selectByCreditCode(licenseAgentInfoVO.getCreditCode());
				if(!StringUtil.isEmpty(creditCode)){
					throw new HdiException("代理商信用代码已存在");
				}
				
				//代理商信息不存在，生成一条待审批状态的记录
				Map<String, Object> agent = new HashMap<String, Object>();
				
				agent.put("agentName", licenseAgentInfoVO.getAgentName());
				agent.put("creditCode", licenseAgentInfoVO.getCreditCode());
				agent.put("supplierId",licenseAgentInfoVO.getSupplierId());
				agent.put("status", StatusEnum.USABLE.getKey());
				agent.put("delFlag", DelFlagEnum.NORMAL.getKey());
				agent.put("createId", licenseAgentInfoVO.getCreateId());
				agent.put("deptId", sysUserService.selectById(licenseAgentInfoVO.getCreateId()).getDeptId());
				agent.put("createTime", new Date());
				//获取代理商编码序列号
				String agentCode = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.AGENT_CODE.getKey());
				agent.put("agentCode", agentCode);
				
				this.baseMapper.saveAgent(agent);
				
				licenseAgentInfoVO.setAgentId(Long.valueOf(agent.get("id").toString()));
			}
			
			licenseAgentInfoVO.setStatus(StatusEnum.USABLE.getKey());
			licenseAgentInfoVO.setCreateTime(new Date());
			baseMapper.insert(licenseAgentInfoVO);
		}
		
	}
	
	@Override
	public void update(LicenseAgentInfoVO licenseAgentInfoVO) {
		if(licenseAgentInfoVO != null){
			if(licenseAgentInfoVO.getId() == null){
				throw new HdiException("主键id不能为空");
			}
			if(licenseAgentInfoVO.getEditId() == null){
				throw new HdiException("修改人id不能为空");
			}
			checkValue(licenseAgentInfoVO);
			setTime(licenseAgentInfoVO);
			if(StringUtil.isEmpty(licenseAgentInfoVO.getAgentId())){
				
				List<Map<String, Object>> agentName = baseMapper.selectByAgentName(licenseAgentInfoVO.getAgentName());
				if(!StringUtil.isEmpty(agentName)){
					throw new HdiException("代理商名称已存在");
				}
				List<Map<String, Object>> creditCode = baseMapper.selectByCreditCode(licenseAgentInfoVO.getCreditCode());
				if(!StringUtil.isEmpty(creditCode)){
					throw new HdiException("代理商信用代码已存在");
				}
				
				//代理商信息不存在，生成一条待审批状态的记录
				Map<String, Object> agent = new HashMap<String, Object>();
				
				agent.put("agentName", licenseAgentInfoVO.getAgentName());
				agent.put("creditCode", licenseAgentInfoVO.getCreditCode());
				agent.put("supplierId",licenseAgentInfoVO.getSupplierId());
				agent.put("status", StatusEnum.USABLE.getKey());
				agent.put("delFlag", DelFlagEnum.NORMAL.getKey());
				agent.put("createId", licenseAgentInfoVO.getCreateId());
				agent.put("deptId", sysUserService.selectById(licenseAgentInfoVO.getEditId()).getDeptId());
				agent.put("createTime", new Date());
				//获取代理商编码序列号
				String agentCode = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.AGENT_CODE.getKey());
				agent.put("agentCode", agentCode);
				
				this.baseMapper.saveAgent(agent);
				
				licenseAgentInfoVO.setAgentId(Long.valueOf(agent.get("id").toString()));
			}
			licenseAgentInfoVO.setEditTime(new Date());
			baseMapper.updateById(licenseAgentInfoVO);
		}
		
	}
	
	@Override
	public void replace(LicenseAgentInfoVO licenseAgentInfoVO) {
		if(licenseAgentInfoVO != null){
			Long id = licenseAgentInfoVO.getId();
			if(id == null){
				throw new HdiException("主键id不能为空");
			}
			if(licenseAgentInfoVO.getAgentId() == null){
				throw new HdiException("代理商id不能为空");
			}
			checkValue(licenseAgentInfoVO);
			setTime(licenseAgentInfoVO);
			licenseAgentInfoVO.setId(null);
			licenseAgentInfoVO.setCreateTime(new Date());
			licenseAgentInfoVO.setStatus(StatusEnum.USABLE.getKey());
			baseMapper.insert(licenseAgentInfoVO);
			LicenseAgentInfoEntity old = new LicenseAgentInfoEntity();
			old.setId(id);
			old.setEditId(licenseAgentInfoVO.getCreateId());
			old.setEditTime(new Date());
			old.setNewLicenseId(licenseAgentInfoVO.getId());
			baseMapper.updateById(old);
		}
		
	}
	
	
	private void checkValue(LicenseAgentInfoVO licenseAgentInfoVO){
		if(licenseAgentInfoVO.getSupplierId() == null){
			throw new HdiException("供应商id不能为空");
		}
		if(licenseAgentInfoVO.getCreditCode() == null){
			throw new HdiException("统一社会信用代码不能为空");
		}
		if(licenseAgentInfoVO.getClassifyId() == null){
			throw new HdiException("证照分类id不能为空");
		}
		if(StringUtils.isBlank(licenseAgentInfoVO.getName())){
			throw new HdiException("证照名称不能为空");
		}
		if(StringUtils.isBlank(licenseAgentInfoVO.getNumber())){
			throw new HdiException("证照编号不能为空");
		}
		if(StringUtils.isBlank(licenseAgentInfoVO.getBeginTimeStr())){
			throw new HdiException("效期开始时间");
		}
		if(StringUtils.isBlank(licenseAgentInfoVO.getEndTimeStr())){
			throw new HdiException("效期结束时间");
		}
		
	}
	
	/**
	 * 转换字符串为date类型
	 * @param licenseGoodsInfoVO
	 */
	private void setTime(LicenseAgentInfoVO licenseAgentInfoVO){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date beginTime = null;
		Date endTime = null;
		try{
			beginTime = sdf.parse(licenseAgentInfoVO.getBeginTimeStr());
			endTime = sdf.parse(licenseAgentInfoVO.getEndTimeStr());
		}catch(Exception e){
			throw new HdiException("时间格式不正确(1960-01-01 00:00:00)");
		}
		licenseAgentInfoVO.setBeginTime(beginTime);
		licenseAgentInfoVO.setEndTime(endTime);
	}

	

	
	
}
