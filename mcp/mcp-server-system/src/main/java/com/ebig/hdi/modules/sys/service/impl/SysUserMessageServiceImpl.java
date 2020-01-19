package com.ebig.hdi.modules.sys.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.enums.UserMessageStatusEnum;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.sys.dao.SysUserMessageDao;
import com.ebig.hdi.modules.sys.entity.SysUserMessageEntity;
import com.ebig.hdi.modules.sys.service.SysUserMessageService;
import com.ebig.hdi.modules.sys.vo.SysUserMessageEntityVo;

@Service("sysUserMessageService")
public class SysUserMessageServiceImpl extends ServiceImpl<SysUserMessageDao, SysUserMessageEntity>
		implements SysUserMessageService {

	@Override
	@DataFilter(subDept = true, user = false, tableAlias = "u")
	public PageUtils queryPage(Map<String, Object> params) {
		int currPage = Integer.parseInt(params.get("page").toString());
		int pageSize = Integer.parseInt(params.get("limit").toString());

		Page<SysUserMessageEntityVo> page = new Page<SysUserMessageEntityVo>(currPage, pageSize);
		
		List<SysUserMessageEntityVo> list = this.baseMapper.listForPage(page, params);
		
		page.setRecords(list);
		
		return new PageUtils(page);

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public SysUserMessageEntityVo queryInfoById(Long id) {
		SysUserMessageEntity sysUserMessageEntity = new SysUserMessageEntity();
		sysUserMessageEntity.setId(id);
		sysUserMessageEntity.setStatus(UserMessageStatusEnum.READ.getKey());
		sysUserMessageEntity.setReadTime(new Date());
		this.updateById(sysUserMessageEntity);
		return this.baseMapper.queryInfoById(id);
	}

	@Override
	public void delete(Long[] ids) {
		for (Long id : ids) {
			SysUserMessageEntity sysUserMessageEntity = new SysUserMessageEntity();
			sysUserMessageEntity.setId(id);
			// 设置信息为删除状态
			sysUserMessageEntity.setStatus(UserMessageStatusEnum.DELETE.getKey());
			this.updateById(sysUserMessageEntity);
		}
	}

}
