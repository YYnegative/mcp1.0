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
import com.ebig.hdi.modules.consumables.dao.GoodsPlatformConsumablesSpecsDao;
import com.ebig.hdi.modules.consumables.entity.GoodsPlatformConsumablesSpecsEntity;
import com.ebig.hdi.modules.consumables.service.GoodsPlatformConsumablesSpecsService;
import com.ebig.hdi.modules.consumables.vo.GoodsPlatformConsumablesEntityVo;
import com.ebig.hdi.modules.core.service.UnicodeGoodsShipService;
import com.ebig.hdi.modules.sys.service.SysSequenceService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("goodsPlatformConsumablesSpecsService")
public class GoodsPlatformConsumablesSpecsServiceImpl extends ServiceImpl<GoodsPlatformConsumablesSpecsDao, GoodsPlatformConsumablesSpecsEntity> implements GoodsPlatformConsumablesSpecsService {

	@Autowired
	private SysSequenceService sysSequenceService;
	
	@Autowired
	private UnicodeGoodsShipService unicodeGoodsShipService;
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
		String consumablesId = params.get("consumablesId")!=null ? params.get("consumablesId").toString() : null;
		String status = params.get("status")!=null ? params.get("status").toString() : null;
		String specs = params.get("specs") != null ? params.get("specs").toString() : null;
    	
        Page<GoodsPlatformConsumablesSpecsEntity> page = this.selectPage(
                new Query<GoodsPlatformConsumablesSpecsEntity>(params).getPage(),
                new EntityWrapper<GoodsPlatformConsumablesSpecsEntity>()
						.eq(StringUtils.isNotBlank(consumablesId), "consumables_id", consumablesId)
						.eq(StringUtils.isNotBlank(status), "status", status)
						.like(StringUtils.isNotBlank(specs), "specs", specs)
        );
        
        List<GoodsPlatformConsumablesSpecsEntity> list = page.getRecords();
        
		checkCiteStatus(list);
		
		page.setRecords(list);

        return new PageUtils(page);
    }

	private void checkCiteStatus(List<GoodsPlatformConsumablesSpecsEntity> specsList) {
		for (GoodsPlatformConsumablesSpecsEntity specsEntity : specsList) {
			List<UnicodeGoodsShipEntity> list = unicodeGoodsShipService.selectList(
					new EntityWrapper<UnicodeGoodsShipEntity>()
					.eq("tgoods_type", MatchGoodsTypeEnum.CONSUMABLE.getKey())
					.eq("pgoods_id", specsEntity.getConsumablesId())
					.eq("pspecs_id", specsEntity.getId())
					.eq("ship_flag", IsMatchEnum.YES.getKey())
					.eq("del_flag", DelFlagEnum.NORMAL.getKey()));
			specsEntity.setIsCite(StringUtil.isEmpty(list) ? IsCiteEnum.NO.getKey() : IsCiteEnum.YES.getKey());
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(GoodsPlatformConsumablesSpecsEntity goodsPlatformConsumablesSpecs) {
		if (this.baseMapper.selectByConsumablesIdAndSpecs(goodsPlatformConsumablesSpecs.getConsumablesId(),
				goodsPlatformConsumablesSpecs.getSpecs()) != null) {
			throw new HdiException("规格：" + goodsPlatformConsumablesSpecs.getSpecs() + "，已存在！");
		}
		if (!StringUtil.isEmpty(goodsPlatformConsumablesSpecs.getGuid()) && this.baseMapper.selectByGuid(goodsPlatformConsumablesSpecs.getGuid()) != null) {
			throw new HdiException("全球唯一码：" + goodsPlatformConsumablesSpecs.getGuid() + "，已存在！");
		}
		// 设置规格编码
		goodsPlatformConsumablesSpecs.setSpecsCode(String
				.valueOf(sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.PLATFORM_CONSUMABLES_SPECS_CODE.getKey())));
		goodsPlatformConsumablesSpecs.setCreateTime(new Date());
		goodsPlatformConsumablesSpecs.setEditTime(new Date());
		this.insert(goodsPlatformConsumablesSpecs);
		
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insertOrUpdate(List<GoodsPlatformConsumablesSpecsEntity> goodsPlatformConsumablesSpecsList) {
		for (GoodsPlatformConsumablesSpecsEntity goodsPlatformConsumablesSpecs : goodsPlatformConsumablesSpecsList) {
			if (goodsPlatformConsumablesSpecs.getId() != null) {
				GoodsPlatformConsumablesSpecsEntity specs = this.baseMapper.selectByConsumablesIdAndSpecs(goodsPlatformConsumablesSpecs.getConsumablesId(),
						goodsPlatformConsumablesSpecs.getSpecs());
				if (specs != null && !specs.getId().equals(goodsPlatformConsumablesSpecs.getId())) {
					// 传入的规格在表中存在，且id不一致
					throw new HdiException("规格：" + goodsPlatformConsumablesSpecs.getSpecs() + "，已存在！");
				}
				if (!StringUtil.isEmpty(goodsPlatformConsumablesSpecs.getGuid())) {
					GoodsPlatformConsumablesSpecsEntity guid = this.baseMapper.selectByGuid(goodsPlatformConsumablesSpecs.getGuid());
					if(guid != null && !guid.getId().equals(goodsPlatformConsumablesSpecs.getId())){
						// 传入的全球唯一码在表中存在，且id不一致
						throw new HdiException("全球唯一码：" + goodsPlatformConsumablesSpecs.getGuid() + "，已存在！");
					}
				}
				goodsPlatformConsumablesSpecs.setEditTime(new Date());
				this.updateById(goodsPlatformConsumablesSpecs);
			} else {
				this.save(goodsPlatformConsumablesSpecs);
			}
		}
		
	}

	@Override
	public List<GoodsPlatformConsumablesSpecsEntity> selectListByConsumablesId(Long consumablesId) {
		return this.baseMapper.selectListByConsumablesId(consumablesId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public  Map<String, Object> save(GoodsPlatformConsumablesEntityVo goodsPlatformConsumablesVo) {
		Map<String, Object> map = new HashMap<>(16);
		GoodsPlatformConsumablesSpecsEntity goodsPlatformConsumablesSpecsEntity = new GoodsPlatformConsumablesSpecsEntity();
		goodsPlatformConsumablesSpecsEntity.setConsumablesId(goodsPlatformConsumablesVo.getId());
		goodsPlatformConsumablesSpecsEntity.setSpecs(goodsPlatformConsumablesVo.getSpecs());
		goodsPlatformConsumablesSpecsEntity.setGuid(goodsPlatformConsumablesVo.getGuid());
		goodsPlatformConsumablesSpecsEntity.setCreateId(goodsPlatformConsumablesVo.getCreateId());
		goodsPlatformConsumablesSpecsEntity.setStatus(goodsPlatformConsumablesVo.getStatus());
		goodsPlatformConsumablesSpecsEntity.setEditId(goodsPlatformConsumablesVo.getEditId());
		goodsPlatformConsumablesSpecsEntity.setEditTime(goodsPlatformConsumablesVo.getEditTime());
		goodsPlatformConsumablesSpecsEntity.setSpecsCode(String.valueOf(
				sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.PLATFORM_CONSUMABLES_SPECS_CODE.getKey())));
		this.save(goodsPlatformConsumablesSpecsEntity);
		return map;
	}

	@Override
	public GoodsPlatformConsumablesSpecsEntity selectByConsumablesIdAndSpecs(Long id, String specs) {
		return this.baseMapper.selectByConsumablesIdAndSpecs(id,specs);
	}

}
