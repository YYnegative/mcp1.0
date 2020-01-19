package com.ebig.hdi.modules.consumables.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.enums.StatusEnum;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.Query;
import com.ebig.hdi.modules.consumables.dao.GoodsHospitalConsumablesApprovalsDao;
import com.ebig.hdi.modules.consumables.entity.GoodsHospitalConsumablesApprovalsEntity;
import com.ebig.hdi.modules.consumables.service.GoodsHospitalConsumablesApprovalsService;
import com.ebig.hdi.modules.consumables.vo.GoodsHospitalConsumablesEntityVo;


@Service("goodsHospitalConsumablesApprovalsService")
public class GoodsHospitalConsumablesApprovalsServiceImpl extends ServiceImpl<GoodsHospitalConsumablesApprovalsDao, GoodsHospitalConsumablesApprovalsEntity> implements GoodsHospitalConsumablesApprovalsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<GoodsHospitalConsumablesApprovalsEntity> page = this.selectPage(
                new Query<GoodsHospitalConsumablesApprovalsEntity>(params).getPage(),
                new EntityWrapper<GoodsHospitalConsumablesApprovalsEntity>()
        );

        return new PageUtils(page);
    }

	@Override
	public List<GoodsHospitalConsumablesApprovalsEntity> selectListByConsumablesId(Long consumablesId) {
		return this.baseMapper.selectListByConsumablesId(consumablesId);
	}

	@Override
	public void saveBatch(GoodsHospitalConsumablesEntityVo goodsHospitalConsumablesVo) {
		List<GoodsHospitalConsumablesApprovalsEntity> list = new ArrayList<>();
		for(String approvals : goodsHospitalConsumablesVo.getApprovals()) {
			GoodsHospitalConsumablesApprovalsEntity goodsHospitalConsumablesApprovalsEntity = new GoodsHospitalConsumablesApprovalsEntity();
			goodsHospitalConsumablesApprovalsEntity.setConsumablesId(goodsHospitalConsumablesVo.getId());
			goodsHospitalConsumablesApprovalsEntity.setApprovals(approvals);
			goodsHospitalConsumablesApprovalsEntity.setStatus(StatusEnum.USABLE.getKey());
			goodsHospitalConsumablesApprovalsEntity.setCreateId(goodsHospitalConsumablesVo.getCreateId());
			goodsHospitalConsumablesApprovalsEntity.setCreateTime(goodsHospitalConsumablesVo.getCreateTime());
			list.add(goodsHospitalConsumablesApprovalsEntity);
		}
		this.insertBatch(list);
		
	}

	@Override
	public GoodsHospitalConsumablesApprovalsEntity selectByApprovals(Long consumablesId, String approvals) {
		return this.baseMapper.selectByApprovals(consumablesId, approvals);
	}

}
