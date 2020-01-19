package com.ebig.hdi.modules.drugs.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.common.validator.group.AddGroup;
import com.ebig.hdi.common.validator.group.UpdateGroup;
import com.ebig.hdi.modules.drugs.entity.GoodsPlatformDrugsSpecsEntity;
import com.ebig.hdi.modules.drugs.service.GoodsPlatformDrugsSpecsService;
import com.ebig.hdi.modules.sys.controller.AbstractController;

/**
 * 平台药品规格
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-08 14:21:24
 */
@RestController
@RequestMapping("drugs/goodsplatformdrugsspecs")
public class GoodsPlatformDrugsSpecsController extends AbstractController {
	@Autowired
	private GoodsPlatformDrugsSpecsService goodsPlatformDrugsSpecsService;

	/**
	 * 列表
	 */
	@PostMapping("/list")
	//@RequiresPermissions("drugs:goodsplatformdrugsspecs:list")
	public Hdi list(@RequestBody Map<String, Object> params) {
		PageUtils page = goodsPlatformDrugsSpecsService.queryPage(params);

		return Hdi.ok().put("page", page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("drugs:goodsplatformdrugsspecs:info")
	public Hdi info(@PathVariable("id") Long id) {
		GoodsPlatformDrugsSpecsEntity goodsPlatformDrugsSpecs = goodsPlatformDrugsSpecsService.selectById(id);

		return Hdi.ok().put("goodsPlatformDrugsSpecs", goodsPlatformDrugsSpecs);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("drugs:goodsplatformdrugsspecs:save")
	public Hdi save(@RequestBody GoodsPlatformDrugsSpecsEntity goodsPlatformDrugsSpecs) {
		ValidatorUtils.validateEntity(goodsPlatformDrugsSpecs, AddGroup.class);
		goodsPlatformDrugsSpecs.setCreateId(getUserId());
		goodsPlatformDrugsSpecsService.save(goodsPlatformDrugsSpecs);

		return Hdi.ok();
	}

	/**
	 * 编辑
	 */
	@PostMapping("/update")
	//@RequiresPermissions("drugs:goodsplatformdrugsspecs:update")
	public Hdi update(@RequestBody List<GoodsPlatformDrugsSpecsEntity> goodsPlatformDrugsSpecsList) throws Exception {
		for (GoodsPlatformDrugsSpecsEntity goodsPlatformDrugsSpecs : goodsPlatformDrugsSpecsList) {
			ValidatorUtils.validateEntity(goodsPlatformDrugsSpecs, UpdateGroup.class);
			if (goodsPlatformDrugsSpecs.getId() != null) {
				goodsPlatformDrugsSpecs.setEditId(getUserId());
			} else {
				goodsPlatformDrugsSpecs.setCreateId(getUserId());
			}
		}
		goodsPlatformDrugsSpecsService.insertOrUpdate(goodsPlatformDrugsSpecsList);

		return Hdi.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("drugs:goodsplatformdrugsspecs:delete")
	public Hdi delete(@RequestBody Long[] ids) {
		goodsPlatformDrugsSpecsService.deleteBatchIds(Arrays.asList(ids));

		return Hdi.ok();
	}

}
