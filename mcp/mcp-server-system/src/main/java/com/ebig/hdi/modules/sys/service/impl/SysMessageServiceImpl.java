package com.ebig.hdi.modules.sys.service.impl;

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
import com.ebig.hdi.common.enums.MessageStatusEnum;
import com.ebig.hdi.common.enums.UserMessageStatusEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.Constant;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.Query;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.sys.dao.SysMessageDao;
import com.ebig.hdi.modules.sys.entity.SysMessageEntity;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import com.ebig.hdi.modules.sys.entity.SysUserMessageEntity;
import com.ebig.hdi.modules.sys.service.SysMessageService;
import com.ebig.hdi.modules.sys.service.SysUserMessageService;
import com.ebig.hdi.modules.sys.service.SysUserService;

@Service("sysMessageService")
public class SysMessageServiceImpl extends ServiceImpl<SysMessageDao, SysMessageEntity> implements SysMessageService {

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private SysUserMessageService sysUserMessageService;

	@Override
	@DataFilter(subDept = true, user = false)
	public PageUtils queryPage(Map<String, Object> params) {
		Page<SysMessageEntity> page = this.selectPage(new Query<SysMessageEntity>(params).getPage(),
				new EntityWrapper<SysMessageEntity>()
				.addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER))
				.eq(StringUtils.isNotBlank((String) params.get("type")), "type", params.get("type"))
				.orderBy("create_time", false));

		return new PageUtils(page);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(SysMessageEntity sysMessage) {
		sysMessage.setCreateTime(new Date());
		this.insert(sysMessage);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(SysMessageEntity sysMessage) {
		sysMessage.setEditTime(new Date());
		this.updateById(sysMessage);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void publish(Map<String, Object> params) {
		List<Long> userIds = JSON.parseArray(params.get("userIds").toString(), Long.class);
		if (StringUtil.isEmpty(userIds)) {
			throw new HdiException("请选择发布用户");
		}
		Long messageId = Long.valueOf(params.get("messageId").toString());
		SysMessageEntity sysMessageEntity = new SysMessageEntity();
		sysMessageEntity.setId(messageId);
		sysMessageEntity.setStatus(MessageStatusEnum.PUBLISH.getKey());
		this.updateById(sysMessageEntity);
		List<SysUserEntity> sysUserEntities = sysUserService.selectBatchIds(userIds);
		for (SysUserEntity sysUserEntity : sysUserEntities) {
			SysUserMessageEntity sysUserMessageEntity = new SysUserMessageEntity();
			sysUserMessageEntity.setUserId(sysUserEntity.getUserId());
			sysUserMessageEntity.setDeptId(String.valueOf(sysUserEntity.getDeptId()));
			sysUserMessageEntity.setMessageId(messageId);
			sysUserMessageEntity.setStatus(UserMessageStatusEnum.UNREAD.getKey());
			sysUserMessageService.insert(sysUserMessageEntity);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long[] ids) {
		for(Long id : ids) {
			SysMessageEntity sysMessage = new SysMessageEntity();
			sysMessage.setId(id);
			sysMessage.setStatus(MessageStatusEnum.DELETE.getKey());
			this.updateById(sysMessage);
		}
		
	}

}
