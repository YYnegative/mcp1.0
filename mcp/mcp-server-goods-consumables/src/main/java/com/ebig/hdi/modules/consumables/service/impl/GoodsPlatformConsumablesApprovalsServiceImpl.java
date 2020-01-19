package com.ebig.hdi.modules.consumables.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ebig.hdi.common.exception.HdiException;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.enums.StatusEnum;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.Query;
import com.ebig.hdi.modules.consumables.dao.GoodsPlatformConsumablesApprovalsDao;
import com.ebig.hdi.modules.consumables.entity.GoodsPlatformConsumablesApprovalsEntity;
import com.ebig.hdi.modules.consumables.service.GoodsPlatformConsumablesApprovalsService;
import com.ebig.hdi.modules.consumables.vo.GoodsPlatformConsumablesEntityVo;


@Service("goodsPlatformConsumablesApprovalsService")
public class GoodsPlatformConsumablesApprovalsServiceImpl extends ServiceImpl<GoodsPlatformConsumablesApprovalsDao, GoodsPlatformConsumablesApprovalsEntity> implements GoodsPlatformConsumablesApprovalsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<GoodsPlatformConsumablesApprovalsEntity> page = this.selectPage(
                new Query<GoodsPlatformConsumablesApprovalsEntity>(params).getPage(),
                new EntityWrapper<GoodsPlatformConsumablesApprovalsEntity>()
        );

        return new PageUtils(page);
    }

	@Override
	public List<GoodsPlatformConsumablesApprovalsEntity> selectListByConsumablesId(Long consumablesId) {
		return this.baseMapper.selectListByConsumablesId(consumablesId);
	}

	@Override
	public void saveBatch(GoodsPlatformConsumablesEntityVo goodsPlatformConsumablesVo) {
		List<GoodsPlatformConsumablesApprovalsEntity> list = new ArrayList<>();
		List<String> allApprovals = this.baseMapper.selectAllApprovals();
		for (String approval : goodsPlatformConsumablesVo.getApprovals()) {
			if (allApprovals.contains(approval)){
				throw new HdiException("批准文号：" + approval + "，已存在！");
			}
		}

		for(String approvals : goodsPlatformConsumablesVo.getApprovals()) {
			GoodsPlatformConsumablesApprovalsEntity goodsPlatformConsumablesApprovalsEntity = new GoodsPlatformConsumablesApprovalsEntity();
			goodsPlatformConsumablesApprovalsEntity.setConsumablesId(goodsPlatformConsumablesVo.getId());
			goodsPlatformConsumablesApprovalsEntity.setApprovals(approvals);
			goodsPlatformConsumablesApprovalsEntity.setStatus(StatusEnum.USABLE.getKey());
			goodsPlatformConsumablesApprovalsEntity.setCreateId(goodsPlatformConsumablesVo.getCreateId());
			goodsPlatformConsumablesApprovalsEntity.setCreateTime(goodsPlatformConsumablesVo.getCreateTime());
			goodsPlatformConsumablesApprovalsEntity.setEditId(goodsPlatformConsumablesVo.getEditId());
			goodsPlatformConsumablesApprovalsEntity.setEditTime(goodsPlatformConsumablesVo.getEditTime());
			list.add(goodsPlatformConsumablesApprovalsEntity);
		}
		this.insertBatch(list);
		
	}

	@Override
	public GoodsPlatformConsumablesApprovalsEntity selectByApprovals(Long consumablesId, String approvals) {
		return this.baseMapper.selectByApprovals(consumablesId, approvals);
	}

}
