package com.ebig.hdi.modules.drugs.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.utils.ReflectUitls;
import com.ebig.hdi.modules.drugs.dao.GoodsPlatformDrugsDao;
import com.ebig.hdi.modules.drugs.entity.GoodsPlatformDrugsApprovalEntity;
import com.ebig.hdi.modules.drugs.entity.GoodsPlatformDrugsEntity;
import com.ebig.hdi.modules.drugs.service.GoodsPlatformDrugsService;
import org.springframework.stereotype.Service;

@Service("goodsPlatformDrugsService")
public class GoodsPlatformDrugsServiceImpl extends ServiceImpl<GoodsPlatformDrugsDao, GoodsPlatformDrugsEntity>
		implements GoodsPlatformDrugsService {

	@Override
	public void insertByGoodsDrugsApprovalEntity(GoodsPlatformDrugsApprovalEntity goodsPlatformDrugsApprovalEntity) {
		GoodsPlatformDrugsEntity goodsPlatformDrugsEntity = new GoodsPlatformDrugsEntity();
		goodsPlatformDrugsEntity = ReflectUitls.transform(goodsPlatformDrugsApprovalEntity,goodsPlatformDrugsEntity.getClass());
		this.baseMapper.insert(goodsPlatformDrugsEntity);
	}

	@Override
	public void updateByGoodsDrugsApprovalEntity(GoodsPlatformDrugsApprovalEntity goodsPlatformDrugsApprovalEntity) {
		GoodsPlatformDrugsEntity goodsPlatformDrugsEntity = new GoodsPlatformDrugsEntity();
		goodsPlatformDrugsEntity = ReflectUitls.transform(goodsPlatformDrugsApprovalEntity,goodsPlatformDrugsEntity.getClass());
		this.baseMapper.updateById(goodsPlatformDrugsEntity);
	}
}
