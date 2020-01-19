package com.ebig.hdi.modules.drugs.service.impl;

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
import com.ebig.hdi.modules.drugs.dao.GoodsHospitalDrugsSpecsDao;
import com.ebig.hdi.modules.drugs.entity.GoodsHospitalDrugsEntity;
import com.ebig.hdi.modules.drugs.entity.GoodsHospitalDrugsSpecsEntity;
import com.ebig.hdi.modules.drugs.entity.UnicodeGoodsShipDrugsEntity;
import com.ebig.hdi.modules.drugs.service.GoodsHospitalDrugsService;
import com.ebig.hdi.modules.drugs.service.GoodsHospitalDrugsSpecsService;
import com.ebig.hdi.modules.drugs.service.UnicodeGoodsShipService;
import com.ebig.hdi.modules.drugs.vo.GoodsHospitalDrugsEntityVo;
import com.ebig.hdi.modules.sys.service.SysSequenceService;

@Service("goodsHospitalDrugsSpecsService")
public class GoodsHospitalDrugsSpecsServiceImpl
		extends ServiceImpl<GoodsHospitalDrugsSpecsDao, GoodsHospitalDrugsSpecsEntity>
		implements GoodsHospitalDrugsSpecsService {

	@Autowired
	private SysSequenceService sysSequenceService;

	@Autowired
	private GoodsHospitalDrugsService goodsHospitalDrugsService;

	@Autowired
	private UnicodeGoodsShipService unicodeGoodsShipService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String drugsId = params.get("drugsId")!=null ? params.get("drugsId").toString() : null;
		String status = params.get("status")!=null ? params.get("status").toString() : null;
		String specs = params.get("specs") != null ? params.get("specs").toString() : null;

		Page<GoodsHospitalDrugsSpecsEntity> page = this.selectPage(
				new Query<GoodsHospitalDrugsSpecsEntity>(params).getPage(),
				new EntityWrapper<GoodsHospitalDrugsSpecsEntity>()
						.eq(StringUtils.isNotBlank(drugsId), "drugs_id", drugsId)
						.eq(StringUtils.isNotBlank(status), "status", status)
						.like(StringUtils.isNotBlank(specs), "specs", specs));
		
		List<GoodsHospitalDrugsSpecsEntity> list = page.getRecords();
		
		checkCiteStatus(list);
		
		page.setRecords(list);

		return new PageUtils(page);
	}

	private void checkCiteStatus(List<GoodsHospitalDrugsSpecsEntity> specsList) {
		for (GoodsHospitalDrugsSpecsEntity specsEntity : specsList) {
			List<UnicodeGoodsShipDrugsEntity> list = unicodeGoodsShipService.selectList(
					new EntityWrapper<UnicodeGoodsShipDrugsEntity>()
					.eq("torg_type", MatchOrgTypeEnum.HOSPITAL.getKey())
					.eq("tgoods_type", MatchGoodsTypeEnum.DRUGS.getKey())
					.eq("tgoods_id", specsEntity.getDrugsId())
					.eq("tspecs_id", specsEntity.getId())
					.eq("ship_flag", IsMatchEnum.YES.getKey())
					.eq("del_flag", DelFlagEnum.NORMAL.getKey()));
			specsEntity.setIsCite(StringUtil.isEmpty(list) ? IsCiteEnum.NO.getKey() : IsCiteEnum.YES.getKey());
		}
		
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(GoodsHospitalDrugsSpecsEntity goodsHospitalDrugsSpecs) {
		if (this.baseMapper.selectByDrugsIdAndSpecs(goodsHospitalDrugsSpecs.getDrugsId(),
				goodsHospitalDrugsSpecs.getSpecs()) != null) {
			throw new HdiException("药品规格：" + goodsHospitalDrugsSpecs.getSpecs() + "，已存在！");
		}
		if (!StringUtil.isEmpty(goodsHospitalDrugsSpecs.getGuid()) && this.baseMapper.selectByGuid(goodsHospitalDrugsSpecs.getGuid()) != null) {
			throw new HdiException("全球唯一码：" + goodsHospitalDrugsSpecs.getGuid() + "，已存在！");
		}
		// 设置规格编码
		goodsHospitalDrugsSpecs.setSpecsCode(String
				.valueOf(sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.HOSPITAL_DRUGS_SPECS_CODE.getKey())));
		goodsHospitalDrugsSpecs.setCreateTime(new Date());
		goodsHospitalDrugsSpecs.setSourcesSpecsId(UUID.randomUUID().toString());
		this.insert(goodsHospitalDrugsSpecs);

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insertOrUpdate(List<GoodsHospitalDrugsSpecsEntity> goodsHospitalDrugsSpecsList) {
		for (GoodsHospitalDrugsSpecsEntity goodsHospitalDrugsSpecs : goodsHospitalDrugsSpecsList) {
			if (goodsHospitalDrugsSpecs.getId() != null) {
				GoodsHospitalDrugsSpecsEntity specs = this.baseMapper.selectByDrugsIdAndSpecs(
						goodsHospitalDrugsSpecs.getDrugsId(), goodsHospitalDrugsSpecs.getSpecs());
				if (specs != null && !specs.getId().equals(goodsHospitalDrugsSpecs.getId())) {
					// 传入的规格在表中存在，且id不一致
					throw new HdiException("药品规格：" + goodsHospitalDrugsSpecs.getSpecs() + "，已存在！");
				}
				if (!StringUtil.isEmpty(goodsHospitalDrugsSpecs.getGuid())) {
					GoodsHospitalDrugsSpecsEntity guid = this.baseMapper.selectByGuid(goodsHospitalDrugsSpecs.getGuid());
					if (guid != null && !guid.getId().equals(goodsHospitalDrugsSpecs.getId())) {
						// 传入的全球唯一码在表中存在，且id不一致
						throw new HdiException("药品全球唯一码：" + goodsHospitalDrugsSpecs.getGuid() + "，已存在！");
					}
				}
				goodsHospitalDrugsSpecs.setEditTime(new Date());
				this.updateById(goodsHospitalDrugsSpecs);
			} else {
				this.save(goodsHospitalDrugsSpecs);
			}
		}

		// 设置未匹对(0:未匹对;1:已匹对)
		if (!StringUtil.isEmpty(goodsHospitalDrugsSpecsList)) {
			GoodsHospitalDrugsEntity goodsHospitalDrugsEntity = new GoodsHospitalDrugsEntity();
			goodsHospitalDrugsEntity.setId(goodsHospitalDrugsSpecsList.get(0).getDrugsId());
			goodsHospitalDrugsEntity.setIsMatch(IsMatchEnum.NO.getKey());
			goodsHospitalDrugsService.updateById(goodsHospitalDrugsEntity);
		}

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(GoodsHospitalDrugsEntityVo goodsHospitalDrugsEntityVo) {
		GoodsHospitalDrugsSpecsEntity goodsHospitalDrugsSpecsEntity = new GoodsHospitalDrugsSpecsEntity();
		goodsHospitalDrugsSpecsEntity.setDrugsId(goodsHospitalDrugsEntityVo.getId());
		goodsHospitalDrugsSpecsEntity.setSpecs(goodsHospitalDrugsEntityVo.getSpecs());
		goodsHospitalDrugsSpecsEntity.setGuid(goodsHospitalDrugsEntityVo.getGuid());
		goodsHospitalDrugsSpecsEntity.setCreateId(goodsHospitalDrugsEntityVo.getCreateId());
		goodsHospitalDrugsSpecsEntity.setSourcesSpecsId(UUID.randomUUID().toString());
		goodsHospitalDrugsSpecsEntity.setStatus(goodsHospitalDrugsEntityVo.getStatus());
		this.save(goodsHospitalDrugsSpecsEntity);

	}

	@Override
	public List<GoodsHospitalDrugsSpecsEntity> selectListByDrugsId(Long drugsId) {
		return this.baseMapper.selectListByDrugsId(drugsId);
	}

}
