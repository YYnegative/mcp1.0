package com.ebig.hdi.modules.consumables.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.entity.UnicodeGoodsShipEntity;
import com.ebig.hdi.common.enums.*;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.Query;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.consumables.dao.GoodsHospitalConsumablesSpecsDao;
import com.ebig.hdi.modules.consumables.entity.GoodsHospitalConsumablesEntity;
import com.ebig.hdi.modules.consumables.entity.GoodsHospitalConsumablesSpecsEntity;
import com.ebig.hdi.modules.consumables.service.GoodsHospitalConsumablesService;
import com.ebig.hdi.modules.consumables.service.GoodsHospitalConsumablesSpecsService;
import com.ebig.hdi.modules.consumables.vo.GoodsHospitalConsumablesEntityVo;
import com.ebig.hdi.modules.core.service.UnicodeGoodsShipService;
import com.ebig.hdi.modules.sys.service.SysSequenceService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service("goodsHospitalConsumablesSpecsService")
public class GoodsHospitalConsumablesSpecsServiceImpl
		extends ServiceImpl<GoodsHospitalConsumablesSpecsDao, GoodsHospitalConsumablesSpecsEntity>
		implements GoodsHospitalConsumablesSpecsService {

	@Autowired
	private SysSequenceService sysSequenceService;

	@Autowired
	private GoodsHospitalConsumablesService goodsHospitalConsumablesService;
	
	@Autowired
	private UnicodeGoodsShipService unicodeGoodsShipService;
	
	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String consumablesId = params.get("consumablesId")!=null ? params.get("consumablesId").toString() : null;
		String status = params.get("status")!=null ? params.get("status").toString() : null;
		String specs = params.get("specs") != null ? params.get("specs").toString() : null;

		Page<GoodsHospitalConsumablesSpecsEntity> page = this.selectPage(
				new Query<GoodsHospitalConsumablesSpecsEntity>(params).getPage(),
				new EntityWrapper<GoodsHospitalConsumablesSpecsEntity>()
						.eq(StringUtils.isNotBlank(consumablesId), "consumables_id", consumablesId)
						.eq(StringUtils.isNotBlank(status), "status", status)
						.like(StringUtils.isNotBlank(specs), "specs", specs));
		
		List<GoodsHospitalConsumablesSpecsEntity> list = page.getRecords();

		checkCiteStatus(list);
		
		page.setRecords(list);
		
		return new PageUtils(page);
	}

	private void checkCiteStatus(List<GoodsHospitalConsumablesSpecsEntity> specsList) {
		for (GoodsHospitalConsumablesSpecsEntity specsEntity : specsList) {
			List<UnicodeGoodsShipEntity> list = unicodeGoodsShipService.selectList(
					new EntityWrapper<UnicodeGoodsShipEntity>()
					.eq("torg_type", MatchOrgTypeEnum.HOSPITAL.getKey())
					.eq("tgoods_type", MatchGoodsTypeEnum.CONSUMABLE.getKey())
					.eq("tgoods_id", specsEntity.getConsumablesId())
					.eq("tspecs_id", specsEntity.getId())
					.eq("ship_flag", IsMatchEnum.YES.getKey())
					.eq("del_flag", DelFlagEnum.NORMAL.getKey()));
			specsEntity.setIsCite(StringUtil.isEmpty(list) ? IsCiteEnum.NO.getKey() : IsCiteEnum.YES.getKey());
		}
		
	}

	@Override
	public List<GoodsHospitalConsumablesSpecsEntity> selectListByConsumablesId(Long consumablesId) {
		return this.baseMapper.selectListByConsumablesId(consumablesId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(GoodsHospitalConsumablesEntityVo goodsHospitalConsumablesVo) {
		GoodsHospitalConsumablesSpecsEntity goodsHospitalDrugsSpecsEntity = new GoodsHospitalConsumablesSpecsEntity();
		goodsHospitalDrugsSpecsEntity.setConsumablesId(goodsHospitalConsumablesVo.getId());
		goodsHospitalDrugsSpecsEntity.setSpecs(goodsHospitalConsumablesVo.getSpecs());
		goodsHospitalDrugsSpecsEntity.setGuid(goodsHospitalConsumablesVo.getGuid());
		goodsHospitalDrugsSpecsEntity.setCreateId(goodsHospitalConsumablesVo.getCreateId());
		goodsHospitalDrugsSpecsEntity.setStatus(goodsHospitalConsumablesVo.getStatus());
		this.save(goodsHospitalDrugsSpecsEntity);

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(GoodsHospitalConsumablesSpecsEntity goodsHospitalConsumablesSpecs) {
		if (this.baseMapper.selectByConsumablesIdAndSpecs(goodsHospitalConsumablesSpecs.getConsumablesId(),
				goodsHospitalConsumablesSpecs.getSpecs()) != null) {
			throw new HdiException("规格：" + goodsHospitalConsumablesSpecs.getSpecs() + "，已存在！");
		}
		if (!StringUtil.isEmpty(goodsHospitalConsumablesSpecs.getGuid()) && this.baseMapper.selectByGuid(goodsHospitalConsumablesSpecs.getGuid()) != null) {
			throw new HdiException("全球唯一码：" + goodsHospitalConsumablesSpecs.getGuid() + "，已存在！");
		}
		// 设置规格编码
		goodsHospitalConsumablesSpecs.setSpecsCode(String.valueOf(
				sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.HOSPITAL_CONSUMABLES_SPECS_CODE.getKey())));
		goodsHospitalConsumablesSpecs.setCreateTime(new Date());
		
		goodsHospitalConsumablesSpecs.setSourcesSpecsId(UUID.randomUUID().toString());
		this.insert(goodsHospitalConsumablesSpecs);

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insertOrUpdate(List<GoodsHospitalConsumablesSpecsEntity> goodsHospitalConsumablesSpecsList) {
		for (GoodsHospitalConsumablesSpecsEntity goodsHospitalConsumablesSpecs : goodsHospitalConsumablesSpecsList) {
			if (goodsHospitalConsumablesSpecs.getId() != null) {
				GoodsHospitalConsumablesSpecsEntity specs = this.baseMapper.selectByConsumablesIdAndSpecs(
						goodsHospitalConsumablesSpecs.getConsumablesId(), goodsHospitalConsumablesSpecs.getSpecs());
				if (specs != null && !specs.getId().equals(goodsHospitalConsumablesSpecs.getId())) {
					// 传入的规格在表中存在，且id不一致
					throw new HdiException("规格：" + goodsHospitalConsumablesSpecs.getSpecs() + "，已存在！");
				}
				if (!StringUtil.isEmpty(goodsHospitalConsumablesSpecs.getGuid())) {
					GoodsHospitalConsumablesSpecsEntity guid = this.baseMapper
							.selectByGuid(goodsHospitalConsumablesSpecs.getGuid());
					if (guid != null && !guid.getId().equals(goodsHospitalConsumablesSpecs.getId())) {
						// 传入的全球唯一码在表中存在，且id不一致
						throw new HdiException("全球唯一码：" + goodsHospitalConsumablesSpecs.getGuid() + "，已存在！");
					}
				}
				goodsHospitalConsumablesSpecs.setEditTime(new Date());
				this.updateById(goodsHospitalConsumablesSpecs);
			} else {
				this.save(goodsHospitalConsumablesSpecs);
			}
		}
		
		// 设置未匹对(0:未匹对;1:已匹对)
		if (!StringUtil.isEmpty(goodsHospitalConsumablesSpecsList)) {
			GoodsHospitalConsumablesEntity goodsHospitalConsumablesEntity = new GoodsHospitalConsumablesEntity();
			goodsHospitalConsumablesEntity.setId(goodsHospitalConsumablesSpecsList.get(0).getConsumablesId());
			goodsHospitalConsumablesEntity.setIsMatch(IsMatchEnum.NO.getKey());
			goodsHospitalConsumablesService.updateById(goodsHospitalConsumablesEntity);
		}
	}
}
