package com.ebig.hdi.modules.sys.service;

import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.sys.entity.SysUserMessageEntity;
import com.ebig.hdi.modules.sys.vo.SysUserMessageEntityVo;

/**
 * 系统用户消息
 *
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-04-26 12:11:51
 */
public interface SysUserMessageService extends IService<SysUserMessageEntity> {

    PageUtils queryPage(Map<String, Object> params);

	SysUserMessageEntityVo queryInfoById(Long id);

	void delete(Long[] ids);

}

