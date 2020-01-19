package com.ebig.hdi.modules.reagent.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.enums.DelFlagEnum;
import com.ebig.hdi.common.enums.IsCiteEnum;
import com.ebig.hdi.common.enums.IsMatchEnum;
import com.ebig.hdi.common.enums.MatchGoodsTypeEnum;
import com.ebig.hdi.common.enums.MatchOrgTypeEnum;
import com.ebig.hdi.common.enums.SequenceEnum;
import com.ebig.hdi.common.enums.SupplierIsUploadEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.Query;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.reagent.dao.GoodsSupplierReagentSpecsDao;
import com.ebig.hdi.modules.reagent.entity.GoodsSupplierReagentEntity;
import com.ebig.hdi.modules.reagent.entity.GoodsSupplierReagentSpecsEntity;
import com.ebig.hdi.modules.reagent.entity.UnicodeGoodsShipReagentEntity;
import com.ebig.hdi.modules.reagent.service.GoodsSupplierReagentService;
import com.ebig.hdi.modules.reagent.service.GoodsSupplierReagentSpecsService;
import com.ebig.hdi.modules.reagent.service.UnicodeGoodsShipService;
import com.ebig.hdi.modules.sys.service.SysSequenceService;

@Service("goodsSupplierReagentSpecsService")
public class GoodsSupplierReagentSpecsServiceImpl
		extends ServiceImpl<GoodsSupplierReagentSpecsDao, GoodsSupplierReagentSpecsEntity>
		implements GoodsSupplierReagentSpecsService {
	@Autowired
	private SysSequenceService sysSequenceService;
	
	@Autowired
	private GoodsSupplierReagentService goodsSupplierReagentService;
	
	@Autowired
	private UnicodeGoodsShipService unicodeGoodsShipService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String reagenId = params.get("reagenId")!=null ? params.get("reagenId").toString() : null;
		String status = params.get("status")!=null ? params.get("status").toString() : null;
		String specs = params.get("specs") != null ? params.get("specs").toString() : null;

		Page<GoodsSupplierReagentSpecsEntity> page = this.selectPage(
				new Query<GoodsSupplierReagentSpecsEntity>(params).getPage(),
				new EntityWrapper<GoodsSupplierReagentSpecsEntity>()
						.eq(StringUtils.isNotBlank(reagenId), "reagen_id", reagenId)
						.eq(StringUtils.isNotBlank(status), "status", status)
						.like(StringUtils.isNotBlank(specs), "specs", specs));
		
		List<GoodsSupplierReagentSpecsEntity> list = page.getRecords();

		checkCiteStatus(list);
		
		page.setRecords(list);
		
		return new PageUtils(page);
	}

	private void checkCiteStatus(List<GoodsSupplierReagentSpecsEntity> specsList) {
		for (GoodsSupplierReagentSpecsEntity specsEntity : specsList) {
			List<UnicodeGoodsShipReagentEntity> list = unicodeGoodsShipService.selectList(
					new EntityWrapper<UnicodeGoodsShipReagentEntity>()
					.eq("torg_type", MatchOrgTypeEnum.SUPPLIER.getKey())
					.eq("tgoods_type", MatchGoodsTypeEnum.REAGENT.getKey())
					.eq("tgoods_id", specsEntity.getReagenId())
					.eq("tspecs_id", specsEntity.getId())
					.eq("ship_flag", IsMatchEnum.YES.getKey())
					.eq("del_flag", DelFlagEnum.NORMAL.getKey()));
			specsEntity.setIsCite(StringUtil.isEmpty(list) ? IsCiteEnum.NO.getKey() : IsCiteEnum.YES.getKey());
		}
		
	}

	@Override
	public List<GoodsSupplierReagentSpecsEntity> selectListByReagentId(Long id) {
		return baseMapper.selectListByReagentId(id);
	}

	@Override
	public void save(GoodsSupplierReagentSpecsEntity gsrse) {
		if (this.baseMapper.selectByReagenIdAndSpecs(gsrse.getReagenId(),
				gsrse.getSpecs()) != null) {
			throw new HdiException("规格名称：" + gsrse.getSpecs() + "，已存在！");
		}
		if (!StringUtil.isEmpty(gsrse.getGuid()) && this.baseMapper.selectByReagenIdAndGuid(gsrse.getReagenId(), gsrse.getGuid()) != null) {
			throw new HdiException("全球唯一码：" + gsrse.getGuid() + "，已存在！");
		}
		
		gsrse.setSpecsCode(sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.SUPPLIER_REAGENT_SPECS_CODE.getKey()));
		gsrse.setCreateTime(new Date());
		gsrse.setSourcesSpecsId(UUID.randomUUID().toString());
		this.insert(gsrse);
	}

	@Override
	public void insertOrUpdate(List<GoodsSupplierReagentSpecsEntity> goodsSupplierReagentSpecsList) {
		for (GoodsSupplierReagentSpecsEntity goodsSupplierReagentSpecs : goodsSupplierReagentSpecsList) {
			if (goodsSupplierReagentSpecs.getId() != null) {
				GoodsSupplierReagentSpecsEntity specs = this.baseMapper.selectByReagenIdAndSpecs(
						goodsSupplierReagentSpecs.getReagenId(), goodsSupplierReagentSpecs.getSpecs());
				if (specs != null && !specs.getId().equals(goodsSupplierReagentSpecs.getId())) {
					// 传入的规格在表中存在，且id不一致
					throw new HdiException("规格名称：" + goodsSupplierReagentSpecs.getSpecs() + "，已存在！");
				}
				if(!StringUtil.isEmpty(goodsSupplierReagentSpecs.getGuid())){
					GoodsSupplierReagentSpecsEntity guid = this.baseMapper.selectByReagenIdAndGuid(
							goodsSupplierReagentSpecs.getReagenId(), goodsSupplierReagentSpecs.getGuid());
					if (guid != null && !guid.getId().equals(goodsSupplierReagentSpecs.getId())) {
						// 传入的全球唯一码在表中存在，且id不一致
						throw new HdiException("全球唯一码：" + goodsSupplierReagentSpecs.getGuid() + "，已存在！");
					}
				}
				goodsSupplierReagentSpecs.setEditTime(new Date());
				this.updateById(goodsSupplierReagentSpecs);
			} else {
				this.save(goodsSupplierReagentSpecs);
			}
		}
		
		// 设置未匹对(0:未匹对;1:已匹对)和设置未上传
		if (!StringUtil.isEmpty(goodsSupplierReagentSpecsList)) {
			GoodsSupplierReagentEntity goodsSupplierReagentEntity = goodsSupplierReagentService.selectById(goodsSupplierReagentSpecsList.get(0).getReagenId());
			goodsSupplierReagentEntity.setIsMatch(IsMatchEnum.NO.getKey());
			goodsSupplierReagentEntity.setIsUpload(SupplierIsUploadEnum.NO.getKey());
			goodsSupplierReagentService.updateById(goodsSupplierReagentEntity);
			
			//更新下发目录上商品为未上传状态
			goodsSupplierReagentService.updateSupplierGoodsSendNotUpload(goodsSupplierReagentEntity.getSupplierId(), goodsSupplierReagentEntity.getId());
		}
	}

}
