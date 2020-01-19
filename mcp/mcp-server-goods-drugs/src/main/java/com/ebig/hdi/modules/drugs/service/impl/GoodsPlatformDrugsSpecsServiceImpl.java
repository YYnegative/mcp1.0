package com.ebig.hdi.modules.drugs.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ebig.hdi.modules.drugs.entity.GoodsHospitalDrugsSpecsEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.enums.DelFlagEnum;
import com.ebig.hdi.common.enums.IsCiteEnum;
import com.ebig.hdi.common.enums.IsMatchEnum;
import com.ebig.hdi.common.enums.MatchGoodsTypeEnum;
import com.ebig.hdi.common.enums.SequenceEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.Query;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.drugs.dao.GoodsPlatformDrugsSpecsDao;
import com.ebig.hdi.modules.drugs.entity.GoodsPlatformDrugsSpecsEntity;
import com.ebig.hdi.modules.drugs.entity.UnicodeGoodsShipDrugsEntity;
import com.ebig.hdi.modules.drugs.service.GoodsPlatformDrugsSpecsService;
import com.ebig.hdi.modules.drugs.service.UnicodeGoodsShipService;
import com.ebig.hdi.modules.drugs.vo.GoodsPlatformDrugsEntityVo;
import com.ebig.hdi.modules.sys.service.SysSequenceService;

@Service("goodsPlatformDrugsSpecsService")
public class GoodsPlatformDrugsSpecsServiceImpl
		extends ServiceImpl<GoodsPlatformDrugsSpecsDao, GoodsPlatformDrugsSpecsEntity>
		implements GoodsPlatformDrugsSpecsService {

	@Autowired
	private SysSequenceService sysSequenceService;
	
	@Autowired
	private UnicodeGoodsShipService unicodeGoodsShipService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String drugsId = params.get("drugsId")!=null ? params.get("drugsId").toString() : null;
		String status = params.get("status")!=null ? params.get("status").toString() : null;
		String specs = params.get("specs") != null ? params.get("specs").toString() : null;

		Page<GoodsPlatformDrugsSpecsEntity> page = this.selectPage(
				new Query<GoodsPlatformDrugsSpecsEntity>(params).getPage(),
				new EntityWrapper<GoodsPlatformDrugsSpecsEntity>()
						.eq(StringUtils.isNotBlank(drugsId), "drugs_id", drugsId)
						.eq(StringUtils.isNotBlank(status), "status", status)
						.like(StringUtils.isNotBlank(specs), "specs", specs));

		List<GoodsPlatformDrugsSpecsEntity> list = page.getRecords();
		
		checkCiteStatus(list);
		
		page.setRecords(list);
		
		return new PageUtils(page);
	}

	private void checkCiteStatus(List<GoodsPlatformDrugsSpecsEntity> specsList) {
		for (GoodsPlatformDrugsSpecsEntity specsEntity : specsList) {
			List<UnicodeGoodsShipDrugsEntity> list = unicodeGoodsShipService.selectList(
					new EntityWrapper<UnicodeGoodsShipDrugsEntity>()
					.eq("tgoods_type", MatchGoodsTypeEnum.DRUGS.getKey())
					.eq("pgoods_id", specsEntity.getDrugsId())
					.eq("pspecs_id", specsEntity.getId())
					.eq("ship_flag", IsMatchEnum.YES.getKey())
					.eq("del_flag", DelFlagEnum.NORMAL.getKey()));
			specsEntity.setIsCite(StringUtil.isEmpty(list) ? IsCiteEnum.NO.getKey() : IsCiteEnum.YES.getKey());
		}
		
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(GoodsPlatformDrugsEntityVo goodsPlatformDrugsEntityVo) {
		GoodsPlatformDrugsSpecsEntity goodsPlatformDrugsSpecsEntity = new GoodsPlatformDrugsSpecsEntity();
		goodsPlatformDrugsSpecsEntity.setDrugsId(goodsPlatformDrugsEntityVo.getId());
		goodsPlatformDrugsSpecsEntity.setSpecs(goodsPlatformDrugsEntityVo.getSpecs());
		goodsPlatformDrugsSpecsEntity.setGuid(goodsPlatformDrugsEntityVo.getGuid());
		goodsPlatformDrugsSpecsEntity.setCreateId(goodsPlatformDrugsEntityVo.getCreateId());
		goodsPlatformDrugsSpecsEntity.setStatus(goodsPlatformDrugsEntityVo.getStatus());
		this.save(goodsPlatformDrugsSpecsEntity);

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(GoodsPlatformDrugsSpecsEntity goodsPlatformDrugsSpecs) {
		if (this.baseMapper.selectByDrugsIdAndSpecs(goodsPlatformDrugsSpecs.getDrugsId(),
				goodsPlatformDrugsSpecs.getSpecs()) != null) {
			throw new HdiException("药品规格：" + goodsPlatformDrugsSpecs.getSpecs() + "，已存在！");
		}
		if (!StringUtil.isEmpty(goodsPlatformDrugsSpecs.getGuid()) && this.baseMapper.selectByGuid(goodsPlatformDrugsSpecs.getGuid()) != null) {
			throw new HdiException("全球唯一码：" + goodsPlatformDrugsSpecs.getGuid() + "，已存在！");
		}
		// 设置规格编码
		goodsPlatformDrugsSpecs.setSpecsCode(String
				.valueOf(sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.PLATFORM_DRUGS_SPECS_CODE.getKey())));
		goodsPlatformDrugsSpecs.setCreateTime(new Date());
		this.insert(goodsPlatformDrugsSpecs);

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insertOrUpdate(List<GoodsPlatformDrugsSpecsEntity> goodsPlatformDrugsSpecsList) throws Exception {
		for (GoodsPlatformDrugsSpecsEntity goodsPlatformDrugsSpecs : goodsPlatformDrugsSpecsList) {
			if (goodsPlatformDrugsSpecs.getId() != null) {
				GoodsPlatformDrugsSpecsEntity specs = this.baseMapper.selectByDrugsIdAndSpecs(
						goodsPlatformDrugsSpecs.getDrugsId(), goodsPlatformDrugsSpecs.getSpecs());
				if (specs != null && !specs.getId().equals(goodsPlatformDrugsSpecs.getId())) {
					// 传入的规格在表中存在，且id不一致
					throw new HdiException("药品规格：" + goodsPlatformDrugsSpecs.getSpecs() + "，已存在！");
				}
				if(!StringUtil.isEmpty(goodsPlatformDrugsSpecs.getGuid())) {
					GoodsPlatformDrugsSpecsEntity guid = this.baseMapper.selectByGuid(goodsPlatformDrugsSpecs.getGuid());
					if (guid != null && !guid.getId().equals(goodsPlatformDrugsSpecs.getId())) {
						// 传入的全球唯一码在表中存在，且id不一致
						throw new HdiException("药品全球唯一码：" + goodsPlatformDrugsSpecs.getGuid() + "，已存在！");
					}
				}
				goodsPlatformDrugsSpecs.setEditTime(new Date());
				this.updateById(goodsPlatformDrugsSpecs);
			} else {
				this.save(goodsPlatformDrugsSpecs);
			}
		}
	}

	@Override
	public List<GoodsPlatformDrugsSpecsEntity> selectListByDrugsId(Long drugsId) {
		return this.baseMapper.selectListByDrugsId(drugsId);
	}

	@Override
	public GoodsPlatformDrugsSpecsEntity selectByDrugsIdAndSpecs(Long id, String specs) {
		return this.baseMapper.selectByDrugsIdAndSpecs(id,specs);
	}

}
