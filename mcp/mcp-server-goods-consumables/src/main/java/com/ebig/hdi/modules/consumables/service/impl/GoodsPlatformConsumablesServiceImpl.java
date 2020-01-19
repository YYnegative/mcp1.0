package com.ebig.hdi.modules.consumables.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.utils.ReflectUitls;
import com.ebig.hdi.modules.consumables.dao.GoodsPlatformConsumablesDao;
import com.ebig.hdi.modules.consumables.entity.GoodsPlatformConsumablesApprovalEntity;
import com.ebig.hdi.modules.consumables.entity.GoodsPlatformConsumablesEntity;
import com.ebig.hdi.modules.consumables.service.GoodsPlatformConsumablesService;
import com.ebig.hdi.modules.consumables.vo.GoodsPlatformConsumablesEntityVo;
import org.springframework.stereotype.Service;

@Service("goodsPlatformConsumablesService")
public class GoodsPlatformConsumablesServiceImpl
		extends ServiceImpl<GoodsPlatformConsumablesDao, GoodsPlatformConsumablesEntity>
		implements GoodsPlatformConsumablesService {
	@Override
	public void insertByFactoryInfoApproval(GoodsPlatformConsumablesApprovalEntity goodsPlatformConsumablesApprovalEntity) {
		GoodsPlatformConsumablesEntity goodsPlatformConsumablesEntity = new GoodsPlatformConsumablesEntity();
		goodsPlatformConsumablesEntity = ReflectUitls.transform(goodsPlatformConsumablesApprovalEntity, goodsPlatformConsumablesEntity.getClass());
		this.baseMapper.insert(goodsPlatformConsumablesEntity);
	}

	@Override
	public void updateByFactoryInfoApproval(GoodsPlatformConsumablesApprovalEntity goodsPlatformConsumablesApprovalEntity) {
		GoodsPlatformConsumablesEntity goodsPlatformConsumablesEntity = new GoodsPlatformConsumablesEntity();
		goodsPlatformConsumablesEntity = ReflectUitls.transform(goodsPlatformConsumablesApprovalEntity, goodsPlatformConsumablesEntity.getClass());
		this.baseMapper.updateById(goodsPlatformConsumablesEntity);
	}

	@Override
	public void updateByGoodsDrugsApprovalEntity(GoodsPlatformConsumablesEntityVo goodsPlatformConsumablesVo) {
		GoodsPlatformConsumablesEntity goodsPlatformConsumablesEntity = new GoodsPlatformConsumablesEntity();
		goodsPlatformConsumablesEntity = ReflectUitls.transform(goodsPlatformConsumablesVo,goodsPlatformConsumablesEntity.getClass());
		this.baseMapper.updateById(goodsPlatformConsumablesEntity);
	}

}
