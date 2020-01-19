package com.ebig.hdi.modules.sys.controller;

import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.modules.sys.entity.SysMessageEntity;
import com.ebig.hdi.modules.sys.service.SysMessageService;

/**
 * 系统消息
 *
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-04-26 12:11:51
 */
@RestController
@RequestMapping("sys/sysmessage")
public class SysMessageController extends AbstractController {
	@Autowired
	private SysMessageService sysMessageService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	// @RequiresPermissions("sys:sysmessage:list")
	public Hdi list(@RequestParam Map<String, Object> params) {
		PageUtils page = sysMessageService.queryPage(params);

		return Hdi.ok().put("page", page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	// @RequiresPermissions("sys:sysmessage:info")
	public Hdi info(@PathVariable("id") Long id) {
		SysMessageEntity sysMessage = sysMessageService.selectById(id);

		return Hdi.ok().put("sysMessage", sysMessage);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	// @RequiresPermissions("sys:sysmessage:save")
	public Hdi save(@RequestBody SysMessageEntity sysMessage) {
		ValidatorUtils.validateEntity(sysMessage);
		sysMessage.setCreateId(getUserId());
		sysMessageService.save(sysMessage);

		return Hdi.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	// @RequiresPermissions("sys:sysmessage:update")
	public Hdi update(@RequestBody SysMessageEntity sysMessage) {
		ValidatorUtils.validateEntity(sysMessage);
		sysMessage.setEditId(getUserId());
		sysMessageService.update(sysMessage);

		return Hdi.ok();
	}

	/**
	 * 发布
	 */
	@RequestMapping("/publish")
	// @RequiresPermissions("sys:sysmessage:publish")
	public Hdi publish(@RequestBody Map<String, Object> params) {
		sysMessageService.publish(params);

		return Hdi.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("sys:sysmessage:delete")
	public Hdi delete(@RequestBody Long[] ids) {
		sysMessageService.delete(ids);

		return Hdi.ok();
	}

}
