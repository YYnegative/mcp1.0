package com.ebig.hdi.modules.reagent.service.impl;

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
import com.ebig.hdi.common.enums.DelFlagEnum;
import com.ebig.hdi.common.enums.IsCiteEnum;
import com.ebig.hdi.common.enums.IsMatchEnum;
import com.ebig.hdi.common.enums.MatchGoodsTypeEnum;
import com.ebig.hdi.common.enums.SequenceEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.Query;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.reagent.dao.GoodsPlatformReagentSpecsDao;
import com.ebig.hdi.modules.reagent.entity.GoodsPlatformReagentSpecsEntity;
import com.ebig.hdi.modules.reagent.entity.UnicodeGoodsShipReagentEntity;
import com.ebig.hdi.modules.reagent.service.GoodsPlatformReagentSpecsService;
import com.ebig.hdi.modules.reagent.service.UnicodeGoodsShipService;
import com.ebig.hdi.modules.reagent.vo.GoodsPlatformReagentVO;
import com.ebig.hdi.modules.sys.service.SysSequenceService;

@Service("goodsPlatformReagentSpecsService")
public class GoodsPlatformReagentSpecsServiceImpl
		extends ServiceImpl<GoodsPlatformReagentSpecsDao, GoodsPlatformReagentSpecsEntity>
		implements GoodsPlatformReagentSpecsService {

	@Autowired
	private SysSequenceService sysSequenceService;
	
	@Autowired
	private UnicodeGoodsShipService unicodeGoodsShipService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String reagenId = params.get("reagenId")!=null ? params.get("reagenId").toString() : null;
		String status = params.get("status")!=null ? params.get("status").toString() : null;
		String specs = params.get("specs") != null ? params.get("specs").toString() : null;
		
		Page<GoodsPlatformReagentSpecsEntity> page = this.selectPage(
				new Query<GoodsPlatformReagentSpecsEntity>(params).getPage(),
				new EntityWrapper<GoodsPlatformReagentSpecsEntity>()
						.eq(StringUtils.isNotBlank(reagenId), "reagen_id", reagenId)
						.eq(StringUtils.isNotBlank(status), "status", status)
						.like(StringUtils.isNotBlank(specs), "specs", specs));
		
		List<GoodsPlatformReagentSpecsEntity> list = page.getRecords();

		checkCiteStatus(list);
		
		page.setRecords(list);
		
		return new PageUtils(page);
	}

	private void checkCiteStatus(List<GoodsPlatformReagentSpecsEntity> specsList) {
		for (GoodsPlatformReagentSpecsEntity specsEntity : specsList) {
			List<UnicodeGoodsShipReagentEntity> list = unicodeGoodsShipService.selectList(
					new EntityWrapper<UnicodeGoodsShipReagentEntity>()
					.eq("tgoods_type", MatchGoodsTypeEnum.REAGENT.getKey())
					.eq("pgoods_id", specsEntity.getReagenId())
					.eq("pspecs_id", specsEntity.getId())
					.eq("ship_flag", IsMatchEnum.YES.getKey())
					.eq("del_flag", DelFlagEnum.NORMAL.getKey()));
			specsEntity.setIsCite(StringUtil.isEmpty(list) ? IsCiteEnum.NO.getKey() : IsCiteEnum.YES.getKey());
		}
		
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insertOrUpdate(List<GoodsPlatformReagentSpecsEntity> goodsPlatformReagentSpecsList) {
		for (GoodsPlatformReagentSpecsEntity goodsPlatformReagentSpecs : goodsPlatformReagentSpecsList) {
			if (goodsPlatformReagentSpecs.getId() != null) {
				GoodsPlatformReagentSpecsEntity specs = this.baseMapper.selectByReagenIdAndSpecs(
						goodsPlatformReagentSpecs.getReagenId(), goodsPlatformReagentSpecs.getSpecs());
				if (specs != null && !specs.getId().equals(goodsPlatformReagentSpecs.getId())) {
					// 传入的规格在表中存在，且id不一致
					throw new HdiException("规格：" + goodsPlatformReagentSpecs.getSpecs() + "，已存在！");
				}
				if (!StringUtil.isEmpty(goodsPlatformReagentSpecs.getGuid())) {
					GoodsPlatformReagentSpecsEntity guid = this.baseMapper.selectByGuid(goodsPlatformReagentSpecs.getGuid());
					if (guid != null && !guid.getId().equals(goodsPlatformReagentSpecs.getId())) {
						// 传入的全球唯一码在表中存在，且id不一致
						throw new HdiException("全球唯一码：" + goodsPlatformReagentSpecs.getGuid() + "，已存在！");
					}
				}
				goodsPlatformReagentSpecs.setEditTime(new Date());
				this.updateById(goodsPlatformReagentSpecs);
			} else {
				this.save(goodsPlatformReagentSpecs);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(GoodsPlatformReagentVO goodsPlatformReagentVO) {
		GoodsPlatformReagentSpecsEntity gprse = new GoodsPlatformReagentSpecsEntity();
		gprse.setReagenId(goodsPlatformReagentVO.getId());
		gprse.setSpecs(goodsPlatformReagentVO.getSpecs());
		gprse.setGuid(goodsPlatformReagentVO.getGuid());
		gprse.setCreateId(goodsPlatformReagentVO.getCreateId());
		gprse.setStatus(goodsPlatformReagentVO.getStatus());
		this.save(gprse);
		
	}

	private void save(GoodsPlatformReagentSpecsEntity gprse) {
		if (this.baseMapper.selectByReagenIdAndSpecs(gprse.getReagenId(),
				gprse.getSpecs()) != null) {
			throw new HdiException("规格：" + gprse.getSpecs() + "，已存在！");
		}
		if (!StringUtil.isEmpty(gprse.getGuid()) && this.baseMapper.selectByGuid(gprse.getGuid()) != null) {
			throw new HdiException("全球唯一码：" + gprse.getGuid() + "，已存在！");
		}
		
		gprse.setSpecsCode(sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.PLATFORM_REAGENT_SPECS_CODE.getKey()));
		gprse.setCreateTime(new Date());
		
		this.insert(gprse);
	}

	@Override
	public List<GoodsPlatformReagentSpecsEntity> selectListByReagentId(Long reagentId) {
		return this.baseMapper.selectListByReagentId(reagentId);
	}
	@Override
	public GoodsPlatformReagentSpecsEntity selectByReagentIdAndSpecs(Long id, String specs) {
		return this.baseMapper.selectByReagentIdAndSpecs(id,specs);
	}
}
