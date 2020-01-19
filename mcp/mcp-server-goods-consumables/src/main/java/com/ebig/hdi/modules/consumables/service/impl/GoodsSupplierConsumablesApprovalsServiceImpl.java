package com.ebig.hdi.modules.consumables.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.enums.SupplierIsUploadEnum;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.Query;
import com.ebig.hdi.modules.consumables.dao.GoodsSupplierConsumablesApprovalsDao;
import com.ebig.hdi.modules.consumables.entity.GoodsSupplierConsumablesApprovalsEntity;
import com.ebig.hdi.modules.consumables.entity.GoodsSupplierConsumablesEntity;
import com.ebig.hdi.modules.consumables.service.GoodsSupplierConsumablesApprovalsService;
import com.ebig.hdi.modules.consumables.service.GoodsSupplierConsumablesService;


@Service("goodsSupplierConsumablesApprovalsService")
public class GoodsSupplierConsumablesApprovalsServiceImpl extends ServiceImpl<GoodsSupplierConsumablesApprovalsDao, GoodsSupplierConsumablesApprovalsEntity> implements GoodsSupplierConsumablesApprovalsService {
	
	@Autowired
	private GoodsSupplierConsumablesService goodsSupplierConsumablesService;
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<GoodsSupplierConsumablesApprovalsEntity> page = this.selectPage(
                new Query<GoodsSupplierConsumablesApprovalsEntity>(params).getPage(),
                new EntityWrapper<GoodsSupplierConsumablesApprovalsEntity>()
        );

        return new PageUtils(page);
    }

	@Override
	public List<GoodsSupplierConsumablesApprovalsEntity> selectByConsumablesId(Long id) {
		return baseMapper.selectByConsumablesId(id);
	}

	@Override
	public void save(GoodsSupplierConsumablesApprovalsEntity goodsSupplierConsumablesApprovals) {
		this.baseMapper.insert(goodsSupplierConsumablesApprovals);
		GoodsSupplierConsumablesEntity entity = new GoodsSupplierConsumablesEntity();
		entity.setId(goodsSupplierConsumablesApprovals.getConsumablesId());
		entity.setIsUpload(SupplierIsUploadEnum.NO.getKey());
		goodsSupplierConsumablesService.updateById(entity);
		
	}

	@Override
	public GoodsSupplierConsumablesApprovalsEntity selectByApprovals(String approvals) {
		return this.baseMapper.selectByApprovals(approvals);
	}

}
