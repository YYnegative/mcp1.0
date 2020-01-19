package com.ebig.hdi.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.sys.entity.SysMessageEntity;

import java.util.Map;

/**
 * 系统消息
 *
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-04-26 12:11:51
 */
public interface SysMessageService extends IService<SysMessageEntity> {

    PageUtils queryPage(Map<String, Object> params);

	void save(SysMessageEntity sysMessage);

	void update(SysMessageEntity sysMessage);

	void publish(Map<String, Object> params);

	void delete(Long[] ids);
}

