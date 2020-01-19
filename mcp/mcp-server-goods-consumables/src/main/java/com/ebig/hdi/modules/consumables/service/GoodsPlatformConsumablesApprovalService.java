package com.ebig.hdi.modules.consumables.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.consumables.entity.GoodsPlatformConsumablesApprovalEntity;
import com.ebig.hdi.modules.consumables.vo.GoodsPlatformConsumablesEntityVo;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;

import java.util.List;
import java.util.Map;

/**
 * 平台耗材信息
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:37:46
 */
public interface GoodsPlatformConsumablesApprovalService extends IService<GoodsPlatformConsumablesApprovalEntity> {

    PageUtils queryPage(Map<String, Object> params);

	Map<String, Object> save(GoodsPlatformConsumablesEntityVo goodsPlatformConsumablesVo,SysUserEntity entity);

	void update(GoodsPlatformConsumablesEntityVo goodsPlatformConsumablesVo,SysUserEntity entity);

	void delete(Long[] ids);

	GoodsPlatformConsumablesApprovalEntity selectPlatformConsumablesById(Long id);

	void toggle(Map<String, Object> params);

	List<Map<String, Object>> getList(Map map);

	Map<String, Object> checkStatus(Map<String, Object> params, SysUserEntity user);

	Map<String, String> importData(String[][] rows, SysUserEntity user);
}

