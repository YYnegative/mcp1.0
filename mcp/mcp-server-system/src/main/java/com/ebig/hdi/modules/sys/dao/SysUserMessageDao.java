package com.ebig.hdi.modules.sys.dao;

import com.ebig.hdi.modules.sys.entity.SysUserMessageEntity;
import com.ebig.hdi.modules.sys.vo.SysUserMessageEntityVo;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * 系统用户消息
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-04-26 10:27:28
 */
public interface SysUserMessageDao extends BaseMapper<SysUserMessageEntity> {
	
	List<SysUserMessageEntityVo> listForPage(Page<SysUserMessageEntityVo> page, Map<String, Object> params);
	
	SysUserMessageEntityVo queryInfoById(Long id);
	
}
