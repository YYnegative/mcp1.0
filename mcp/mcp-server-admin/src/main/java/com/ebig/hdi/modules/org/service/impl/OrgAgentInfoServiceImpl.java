package com.ebig.hdi.modules.org.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.enums.DelFlagEnum;
import com.ebig.hdi.common.enums.SequenceEnum;
import com.ebig.hdi.common.enums.StatusEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.Constant;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.Query;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.org.dao.OrgAgentInfoDao;
import com.ebig.hdi.modules.org.entity.OrgAgentInfoEntity;
import com.ebig.hdi.modules.org.service.OrgAgentInfoService;
import com.ebig.hdi.modules.org.service.OrgSupplierInfoService;
import com.ebig.hdi.modules.sys.service.SysSequenceService;


@Service("orgAgentInfoService")
public class OrgAgentInfoServiceImpl extends ServiceImpl<OrgAgentInfoDao, OrgAgentInfoEntity> implements OrgAgentInfoService {
	
	@Autowired
    private SysSequenceService sysSequenceService;
	@Autowired
	private OrgSupplierInfoService orgSupplierInfoService;
	
    @Override
    @DataFilter(subDept = true, user = false)
    public PageUtils queryPage(Map<String, Object> params) {
    	String agentName = params.get("agentName")!=null?params.get("agentName").toString():null;
    	Integer status =  params.get("status")!=null?(Integer)params.get("status"):null;
    	String sidx = (String)params.get("sidx");
    	String order = (String)params.get("order");
    	
        Page<OrgAgentInfoEntity> page = this.selectPage(
                new Query<OrgAgentInfoEntity>(params).getPage(),
                new EntityWrapper<OrgAgentInfoEntity>()
                .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
                .like(StringUtils.isNotBlank(agentName),"agent_name", agentName)
                .eq(status!=null,"status" ,status)
                .orderBy(StringUtils.isBlank(sidx) || StringUtils.isBlank(order), "edit_time", false)
                .orderBy(StringUtils.isBlank(sidx) || StringUtils.isBlank(order), "create_time", false)
        );

        return new PageUtils(page);
    }

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(OrgAgentInfoEntity orgAgentInfoEntity) {
		List<OrgAgentInfoEntity> agentName = baseMapper.selectByAgentName(orgAgentInfoEntity.getAgentName());
		if(!StringUtil.isEmpty(agentName)){
			throw new HdiException("代理商名称已存在");
		}
		List<OrgAgentInfoEntity> creditCode = baseMapper.selectByCreditCode(orgAgentInfoEntity.getCreditCode());
		if(!StringUtil.isEmpty(creditCode)){
			throw new HdiException("代理商信用代码已存在");
		}
		Long supplierId = orgSupplierInfoService.selectIdByUserId(orgAgentInfoEntity.getCreateId());
		if(supplierId == null){
			throw new HdiException("查询不到供应商Id,请确认当前登录人信息");
		}
		orgAgentInfoEntity.setSupplierId(supplierId);
		//获取供应商编码序列号
		String agentCode = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.AGENT_CODE.getKey());
		orgAgentInfoEntity.setAgentCode(agentCode);
		orgAgentInfoEntity.setCreateTime(new Date());
		this.insert(orgAgentInfoEntity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(OrgAgentInfoEntity orgAgentInfoEntity) {
		Long id = orgAgentInfoEntity.getId();
		List<OrgAgentInfoEntity> agentName = baseMapper.selectByAgentName(orgAgentInfoEntity.getAgentName());
		if(!StringUtil.isEmpty(agentName)&&agentName.get(0).getId()!=id){
			throw new HdiException("代理商名称已存在");
		}
		List<OrgAgentInfoEntity> creditCode = baseMapper.selectByCreditCode(orgAgentInfoEntity.getCreditCode());
		if(!StringUtil.isEmpty(creditCode)&&creditCode.get(0).getId()!=id){
			throw new HdiException("代理商信用代码已存在");
		}
		orgAgentInfoEntity.setEditTime(new Date());
		this.updateById(orgAgentInfoEntity);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long[] ids) {
		for(Long id : ids) {			
			OrgAgentInfoEntity orgAgentInfoEntity= new OrgAgentInfoEntity();
			orgAgentInfoEntity.setId(id);
			//设置信息为删除状态
			orgAgentInfoEntity.setDelFlag((DelFlagEnum.DELETE.getKey()));
			this.updateById(orgAgentInfoEntity);
		}
	}

	@Override
	@DataFilter(subDept = true, user = false)
	public List<OrgAgentInfoEntity> queryByAgentName(Map<String, Object> params) {
		String agentName = (String) params.get("agentName");
		return this.selectList(
				new EntityWrapper<OrgAgentInfoEntity>()
				.addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
				.like(StringUtils.isNotBlank(agentName), "agent_name", agentName)
				.eq("status", StatusEnum.USABLE.getKey())
				.eq("del_flag", DelFlagEnum.NORMAL.getKey()));
	}

}
