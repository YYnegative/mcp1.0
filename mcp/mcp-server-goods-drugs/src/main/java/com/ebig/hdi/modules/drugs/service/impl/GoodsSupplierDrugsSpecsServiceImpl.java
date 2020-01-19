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
import com.ebig.hdi.common.enums.SupplierIsUploadEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.Query;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.drugs.dao.GoodsSupplierDrugsSpecsDao;
import com.ebig.hdi.modules.drugs.entity.GoodsSupplierDrugsEntity;
import com.ebig.hdi.modules.drugs.entity.GoodsSupplierDrugsSpecsEntity;
import com.ebig.hdi.modules.drugs.entity.UnicodeGoodsShipDrugsEntity;
import com.ebig.hdi.modules.drugs.service.GoodsSupplierDrugsService;
import com.ebig.hdi.modules.drugs.service.GoodsSupplierDrugsSpecsService;
import com.ebig.hdi.modules.drugs.service.UnicodeGoodsShipService;
import com.ebig.hdi.modules.drugs.vo.GoodsSupplierDrugsEntityVo;
import com.ebig.hdi.modules.sys.service.SysSequenceService;

@Service("goodsSupplierDrugsSpecsService")
public class GoodsSupplierDrugsSpecsServiceImpl
		extends ServiceImpl<GoodsSupplierDrugsSpecsDao, GoodsSupplierDrugsSpecsEntity>
		implements GoodsSupplierDrugsSpecsService {

	@Autowired
	private SysSequenceService sysSequenceService;

	@Autowired
	private GoodsSupplierDrugsService goodsSupplierDrugsService;
	
	@Autowired
	private UnicodeGoodsShipService unicodeGoodsShipService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String drugsId = params.get("drugsId")!=null ? params.get("drugsId").toString() : null;
		String status = params.get("status")!=null ? params.get("status").toString() : null;
		String specs = params.get("specs") != null ? params.get("specs").toString() : null;

		Page<GoodsSupplierDrugsSpecsEntity> page = this.selectPage(
				new Query<GoodsSupplierDrugsSpecsEntity>(params).getPage(),
				new EntityWrapper<GoodsSupplierDrugsSpecsEntity>()
						.eq(StringUtils.isNotBlank(drugsId), "drugs_id", drugsId)
						.eq(StringUtils.isNotBlank(status), "status", status)
						.like(StringUtils.isNotBlank(specs), "specs", specs));
		
		List<GoodsSupplierDrugsSpecsEntity> list = page.getRecords();
		
		checkCiteStatus(list);
		
		page.setRecords(list);

		return new PageUtils(page);
	}

	private void checkCiteStatus(List<GoodsSupplierDrugsSpecsEntity> specsList) {
		for (GoodsSupplierDrugsSpecsEntity specsEntity : specsList) {
			List<UnicodeGoodsShipDrugsEntity> list = unicodeGoodsShipService.selectList(
					new EntityWrapper<UnicodeGoodsShipDrugsEntity>()
					.eq("torg_type", MatchOrgTypeEnum.SUPPLIER.getKey())
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
	public void save(GoodsSupplierDrugsEntityVo goodsSupplierDrugsEntityVo) {
		GoodsSupplierDrugsSpecsEntity goodsSupplierDrugsSpecsEntity = new GoodsSupplierDrugsSpecsEntity();
		goodsSupplierDrugsSpecsEntity.setDrugsId(goodsSupplierDrugsEntityVo.getId());
		goodsSupplierDrugsSpecsEntity.setSpecs(goodsSupplierDrugsEntityVo.getSpecs());
		goodsSupplierDrugsSpecsEntity.setGuid(goodsSupplierDrugsEntityVo.getGuid());
		goodsSupplierDrugsSpecsEntity.setCreateId(goodsSupplierDrugsEntityVo.getCreateId());
		goodsSupplierDrugsSpecsEntity.setStatus(goodsSupplierDrugsEntityVo.getStatus());
		this.save(goodsSupplierDrugsSpecsEntity);

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(GoodsSupplierDrugsSpecsEntity goodsSupplierDrugsSpecs) {
		if (this.baseMapper.selectByDrugsIdAndSpecs(goodsSupplierDrugsSpecs.getDrugsId(),
				goodsSupplierDrugsSpecs.getSpecs()) != null) {
			throw new HdiException("药品规格：" + goodsSupplierDrugsSpecs.getSpecs() + "，已存在！");
		}
		if (!StringUtil.isEmpty(goodsSupplierDrugsSpecs.getGuid()) && this.baseMapper.selectByGuid(goodsSupplierDrugsSpecs.getGuid()) != null) {
			throw new HdiException("全球唯一码：" + goodsSupplierDrugsSpecs.getGuid() + "，已存在！");
		}
		// 设置规格编码
		goodsSupplierDrugsSpecs.setSpecsCode(String
				.valueOf(sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.SUPPLIER_DRUGS_SPECS_CODE.getKey())));
		goodsSupplierDrugsSpecs.setCreateTime(new Date());
		goodsSupplierDrugsSpecs.setSourcesSpecsId(UUID.randomUUID().toString());
		this.insert(goodsSupplierDrugsSpecs);

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insertOrUpdate(List<GoodsSupplierDrugsSpecsEntity> goodsSupplierDrugsSpecsList) {
		for (GoodsSupplierDrugsSpecsEntity goodsSupplierDrugsSpecs : goodsSupplierDrugsSpecsList) {
			if (goodsSupplierDrugsSpecs.getId() != null) {
				GoodsSupplierDrugsSpecsEntity specs = this.baseMapper.selectByDrugsIdAndSpecs(
						goodsSupplierDrugsSpecs.getDrugsId(), goodsSupplierDrugsSpecs.getSpecs());
				if (specs != null && !specs.getId().equals(goodsSupplierDrugsSpecs.getId())) {
					// 传入的规格在表中存在，且id不一致
					throw new HdiException("药品规格：" + goodsSupplierDrugsSpecs.getSpecs() + "，已存在！");
				}
				if (!StringUtil.isEmpty(goodsSupplierDrugsSpecs.getGuid())) {
					GoodsSupplierDrugsSpecsEntity guid = this.baseMapper.selectByGuid(goodsSupplierDrugsSpecs.getGuid());
					if (guid != null && !guid.getId().equals(goodsSupplierDrugsSpecs.getId())) {
						// 传入的全球唯一码在表中存在，且id不一致
						throw new HdiException("药品全球唯一码：" + goodsSupplierDrugsSpecs.getGuid() + "，已存在！");
					}
				}
				goodsSupplierDrugsSpecs.setEditTime(new Date());
				this.updateById(goodsSupplierDrugsSpecs);
			} else {
				this.save(goodsSupplierDrugsSpecs);
			}
		}

		// 设置未匹对(0:未匹对;1:已匹对)和设置未上传
		if (!StringUtil.isEmpty(goodsSupplierDrugsSpecsList)) {
			GoodsSupplierDrugsEntity goodsSupplierDrugsEntity = goodsSupplierDrugsService.selectById(goodsSupplierDrugsSpecsList.get(0).getDrugsId());
			goodsSupplierDrugsEntity.setIsMatch(IsMatchEnum.NO.getKey());
			goodsSupplierDrugsEntity.setIsUpload(SupplierIsUploadEnum.NO.getKey());
			goodsSupplierDrugsService.updateById(goodsSupplierDrugsEntity);
			
			//更新下发目录上商品为未上传状态
			goodsSupplierDrugsService.updateSupplierGoodsSendNotUpload(goodsSupplierDrugsEntity.getSupplierId(), goodsSupplierDrugsEntity.getId());
		}
	}

	@Override
	public List<GoodsSupplierDrugsSpecsEntity> selectListByDrugsId(Long drugsId) {
		return this.baseMapper.selectListByDrugsId(drugsId);
	}

}
