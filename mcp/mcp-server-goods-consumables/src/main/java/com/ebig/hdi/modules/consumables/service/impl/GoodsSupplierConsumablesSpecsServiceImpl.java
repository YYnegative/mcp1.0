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
import com.ebig.hdi.modules.consumables.dao.GoodsSupplierConsumablesSpecsDao;
import com.ebig.hdi.modules.consumables.entity.GoodsSupplierConsumablesEntity;
import com.ebig.hdi.modules.consumables.entity.GoodsSupplierConsumablesSpecsEntity;
import com.ebig.hdi.modules.consumables.service.GoodsSupplierConsumablesService;
import com.ebig.hdi.modules.consumables.service.GoodsSupplierConsumablesSpecsService;
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

@Service("goodsSupplierConsumablesSpecsService")
public class GoodsSupplierConsumablesSpecsServiceImpl
		extends ServiceImpl<GoodsSupplierConsumablesSpecsDao, GoodsSupplierConsumablesSpecsEntity>
		implements GoodsSupplierConsumablesSpecsService {
	@Autowired
	private SysSequenceService sysSequenceService;
	
	@Autowired
	private GoodsSupplierConsumablesService goodsSupplierConsumablesService;

	@Autowired
	private UnicodeGoodsShipService unicodeGoodsShipService;
	
	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String consumablesId = params.get("consumablesId")!=null ? params.get("consumablesId").toString() : null;
		String status = params.get("status")!=null ? params.get("status").toString() : null;
		String specs = params.get("specs") != null ? params.get("specs").toString() : null;

		Page<GoodsSupplierConsumablesSpecsEntity> page = this.selectPage(
				new Query<GoodsSupplierConsumablesSpecsEntity>(params).getPage(),
				new EntityWrapper<GoodsSupplierConsumablesSpecsEntity>()
						.eq(StringUtils.isNotBlank(consumablesId), "consumables_id", consumablesId)
						.eq(StringUtils.isNotBlank(status), "status", status)
						.like(StringUtils.isNotBlank(specs), "specs", specs));
		
		List<GoodsSupplierConsumablesSpecsEntity> list = page.getRecords();

		checkCiteStatus(list);
		
		page.setRecords(list);
		
		return new PageUtils(page);
	}

	private void checkCiteStatus(List<GoodsSupplierConsumablesSpecsEntity> specsList) {
		for (GoodsSupplierConsumablesSpecsEntity specsEntity : specsList) {
			List<UnicodeGoodsShipEntity> list = unicodeGoodsShipService.selectList(
					new EntityWrapper<UnicodeGoodsShipEntity>()
					.eq("torg_type", MatchOrgTypeEnum.SUPPLIER.getKey())
					.eq("tgoods_type", MatchGoodsTypeEnum.CONSUMABLE.getKey())
					.eq("tgoods_id", specsEntity.getConsumablesId())
					.eq("tspecs_id", specsEntity.getId())
					.eq("ship_flag", IsMatchEnum.YES.getKey())
					.eq("del_flag", DelFlagEnum.NORMAL.getKey()));
			specsEntity.setIsCite(StringUtil.isEmpty(list) ? IsCiteEnum.NO.getKey() : IsCiteEnum.YES.getKey());
		}
	}

	@Override
	public List<GoodsSupplierConsumablesSpecsEntity> selectByConsumablesId(Long id) {
		return baseMapper.selectByConsumablesId(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insertOrUpdate(List<GoodsSupplierConsumablesSpecsEntity> goodsSupplierConsumablesSpecsList) {
		for (GoodsSupplierConsumablesSpecsEntity goodsSupplierConsumablesSpecs : goodsSupplierConsumablesSpecsList) {
			if (goodsSupplierConsumablesSpecs.getId() != null) {
				GoodsSupplierConsumablesSpecsEntity specs = this.baseMapper.selectByConsumablesIdAndSpecs(
						goodsSupplierConsumablesSpecs.getConsumablesId(), goodsSupplierConsumablesSpecs.getSpecs());
				if (specs != null && !specs.getId().equals(goodsSupplierConsumablesSpecs.getId())) {
					// 传入的规格在表中存在，且id不一致
					throw new HdiException("规格：" + goodsSupplierConsumablesSpecs.getSpecs() + "，已存在！");
				}
				if (!StringUtil.isEmpty(goodsSupplierConsumablesSpecs.getGuid())) {
					GoodsSupplierConsumablesSpecsEntity guid = this.baseMapper
							.selectByGuid(goodsSupplierConsumablesSpecs.getGuid());
					if (guid != null && !guid.getId().equals(goodsSupplierConsumablesSpecs.getId())) {
						// 传入的全球唯一码在表中存在，且id不一致
						throw new HdiException("全球唯一码：" + goodsSupplierConsumablesSpecs.getGuid() + "，已存在！");
					}
				}
				goodsSupplierConsumablesSpecs.setEditTime(new Date());
				this.updateById(goodsSupplierConsumablesSpecs);
			} else {
				this.save(goodsSupplierConsumablesSpecs);
				
			}
		}
		
		// 设置未匹对(0:未匹对;1:已匹对)和设置未上传
		if (!StringUtil.isEmpty(goodsSupplierConsumablesSpecsList)) {
			GoodsSupplierConsumablesEntity goodsSupplierConsumablesEntity = goodsSupplierConsumablesService.selectById(goodsSupplierConsumablesSpecsList.get(0).getConsumablesId());
			goodsSupplierConsumablesEntity.setIsMatch(IsMatchEnum.NO.getKey());
			goodsSupplierConsumablesEntity.setIsUpload(SupplierIsUploadEnum.NO.getKey());
			goodsSupplierConsumablesService.updateById(goodsSupplierConsumablesEntity);
			
			goodsSupplierConsumablesService.updateSupplierGoodsSendNotUpload(goodsSupplierConsumablesEntity.getSupplierId(), goodsSupplierConsumablesEntity.getId());
		}
	}

	private void save(GoodsSupplierConsumablesSpecsEntity goodsSupplierConsumablesSpecs) {
		if (this.baseMapper.selectByConsumablesIdAndSpecs(goodsSupplierConsumablesSpecs.getConsumablesId(),
				goodsSupplierConsumablesSpecs.getSpecs()) != null) {
			throw new HdiException("规格：" + goodsSupplierConsumablesSpecs.getSpecs() + "，已存在！");
		}
		if (!StringUtil.isEmpty(goodsSupplierConsumablesSpecs.getGuid()) && this.baseMapper.selectByGuid(goodsSupplierConsumablesSpecs.getGuid()) != null) {
			throw new HdiException("全球唯一码：" + goodsSupplierConsumablesSpecs.getGuid() + "，已存在！");
		}
		// 设置规格编码
		goodsSupplierConsumablesSpecs.setSpecsCode(String.valueOf(
				sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.SUPPLIER_CONSUMABLES_SPECS_CODE.getKey())));
		goodsSupplierConsumablesSpecs.setCreateTime(new Date());
		goodsSupplierConsumablesSpecs.setSourcesSpecsId(UUID.randomUUID().toString());
		this.insert(goodsSupplierConsumablesSpecs);
	}
}
