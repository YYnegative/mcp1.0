package com.ebig.hdi.modules.reagent.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.ebig.hdi.common.enums.MatchOrgTypeEnum;
import com.ebig.hdi.common.enums.SequenceEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.Query;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.reagent.dao.GoodsHospitalReagentSpecsDao;
import com.ebig.hdi.modules.reagent.entity.GoodsHospitalReagentEntity;
import com.ebig.hdi.modules.reagent.entity.GoodsHospitalReagentSpecsEntity;
import com.ebig.hdi.modules.reagent.entity.UnicodeGoodsShipReagentEntity;
import com.ebig.hdi.modules.reagent.service.GoodsHospitalReagentService;
import com.ebig.hdi.modules.reagent.service.GoodsHospitalReagentSpecsService;
import com.ebig.hdi.modules.reagent.service.UnicodeGoodsShipService;
import com.ebig.hdi.modules.reagent.vo.GoodsHospitalReagentVO;
import com.ebig.hdi.modules.sys.service.SysSequenceService;

@Service("goodsHospitalReagentSpecsService")
public class GoodsHospitalReagentSpecsServiceImpl
		extends ServiceImpl<GoodsHospitalReagentSpecsDao, GoodsHospitalReagentSpecsEntity>
		implements GoodsHospitalReagentSpecsService {

	@Autowired
	private SysSequenceService sysSequenceService;
	
	@Autowired
	private GoodsHospitalReagentService goodsHospitalReagentService;
	
	@Autowired
	private UnicodeGoodsShipService unicodeGoodsShipService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String reagenId = params.get("reagenId")!=null ? params.get("reagenId").toString() : null;
		String status = params.get("status")!=null ? params.get("status").toString() : null;
		String specs = params.get("specs") != null ? params.get("specs").toString() : null;

		Page<GoodsHospitalReagentSpecsEntity> page = this.selectPage(
				new Query<GoodsHospitalReagentSpecsEntity>(params).getPage(),
				new EntityWrapper<GoodsHospitalReagentSpecsEntity>()
						.eq(StringUtils.isNotBlank(reagenId), "reagen_id", reagenId)
						.eq(StringUtils.isNotBlank(status), "status", status)
						.like(StringUtils.isNotBlank(specs), "specs", specs));
		
		List<GoodsHospitalReagentSpecsEntity> list = page.getRecords();

		checkCiteStatus(list);
		
		page.setRecords(list);
		
		return new PageUtils(page);
	}

	private void checkCiteStatus(List<GoodsHospitalReagentSpecsEntity> specsList) {
		for (GoodsHospitalReagentSpecsEntity specsEntity : specsList) {
			List<UnicodeGoodsShipReagentEntity> list = unicodeGoodsShipService.selectList(
					new EntityWrapper<UnicodeGoodsShipReagentEntity>()
					.eq("torg_type", MatchOrgTypeEnum.HOSPITAL.getKey())
					.eq("tgoods_type", MatchGoodsTypeEnum.REAGENT.getKey())
					.eq("tgoods_id", specsEntity.getReagenId())
					.eq("tspecs_id", specsEntity.getId())
					.eq("ship_flag", IsMatchEnum.YES.getKey())
					.eq("del_flag", DelFlagEnum.NORMAL.getKey()));
			specsEntity.setIsCite(StringUtil.isEmpty(list) ? IsCiteEnum.NO.getKey() : IsCiteEnum.YES.getKey());
		}
		
	}

	@Override
	public List<GoodsHospitalReagentSpecsEntity> selectListByReagentId(Long id) {
		return baseMapper.selectListByReagentId(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(GoodsHospitalReagentVO goodsHospitalReagentVO) {
		GoodsHospitalReagentSpecsEntity ghrse = new GoodsHospitalReagentSpecsEntity();
		ghrse.setReagenId(goodsHospitalReagentVO.getId());
		ghrse.setSpecs(goodsHospitalReagentVO.getSpecs());
		ghrse.setGuid(goodsHospitalReagentVO.getGuid());
		ghrse.setCreateId(goodsHospitalReagentVO.getCreateId());
		ghrse.setStatus(goodsHospitalReagentVO.getStatus());
		this.save(ghrse);
	}

	private void save(GoodsHospitalReagentSpecsEntity ghrse) {
		if (this.baseMapper.selectByReagenIdAndSpecs(ghrse.getReagenId(),
				ghrse.getSpecs()) != null) {
			throw new HdiException("规格：" + ghrse.getSpecs() + "，已存在！");
		}
		if (!StringUtil.isEmpty(ghrse.getGuid()) && this.baseMapper.selectByGuid(ghrse.getGuid()) != null) {
			throw new HdiException("全球唯一码：" + ghrse.getGuid() + "，已存在！");
		}
		
		ghrse.setSpecsCode(sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.HOSPITAL_REAGENT_SPECS_CODE.getKey()));
		ghrse.setCreateTime(new Date());
		ghrse.setSourcesSpecsId(UUID.randomUUID().toString());
		this.insert(ghrse);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insertOrUpdate(List<GoodsHospitalReagentSpecsEntity> goodsHospitalReagentSpecsList) {
		for (GoodsHospitalReagentSpecsEntity goodsHospitalReagentSpecs : goodsHospitalReagentSpecsList) {
			if (goodsHospitalReagentSpecs.getId() != null) {
				GoodsHospitalReagentSpecsEntity specs = this.baseMapper.selectByReagenIdAndSpecs(
						goodsHospitalReagentSpecs.getReagenId(), goodsHospitalReagentSpecs.getSpecs());
				if (specs != null && !specs.getId().equals(goodsHospitalReagentSpecs.getId())) {
					// 传入的规格在表中存在，且id不一致
					throw new HdiException("规格：" + goodsHospitalReagentSpecs.getSpecs() + "，已存在！");
				}
				if (!StringUtil.isEmpty(goodsHospitalReagentSpecs.getGuid())) {
					GoodsHospitalReagentSpecsEntity guid = this.baseMapper.selectByGuid(goodsHospitalReagentSpecs.getGuid());
					if (guid != null && !guid.getId().equals(goodsHospitalReagentSpecs.getId())) {
						// 传入的全球唯一码在表中存在，且id不一致
						throw new HdiException("全球唯一码：" + goodsHospitalReagentSpecs.getGuid() + "，已存在！");
					}
				}
				goodsHospitalReagentSpecs.setEditTime(new Date());
				this.updateById(goodsHospitalReagentSpecs);
			} else {
				this.save(goodsHospitalReagentSpecs);
			}
		}
		
		// 设置未匹对(0:未匹对;1:已匹对)
		if (!StringUtil.isEmpty(goodsHospitalReagentSpecsList)) {
			GoodsHospitalReagentEntity goodsHospitalReagentEntity = new GoodsHospitalReagentEntity();
			goodsHospitalReagentEntity.setId(goodsHospitalReagentSpecsList.get(0).getReagenId());
			goodsHospitalReagentEntity.setIsMatch(IsMatchEnum.NO.getKey());
			goodsHospitalReagentService.updateById(goodsHospitalReagentEntity);
		}
	}

}
