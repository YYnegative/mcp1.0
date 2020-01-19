package com.ebig.hdi.modules.goods.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ebig.hdi.modules.job.entity.TempPubGoodsEntity;
import com.ebig.hdi.modules.job.entity.TempPubSupplyGoodsEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.enums.SupplierIsUploadEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.goods.dao.GoodsSupplierSendDao;
import com.ebig.hdi.modules.goods.entity.GoodsSupplierSendEntity;
import com.ebig.hdi.modules.goods.service.GoodsSupplierSendService;
import com.ebig.hdi.modules.goods.vo.GoodsSupplierSendEntityVo;


@Service("goodsSupplierSendService")
public class GoodsSupplierSendServiceImpl extends ServiceImpl<GoodsSupplierSendDao, GoodsSupplierSendEntity> implements GoodsSupplierSendService {

	@Override
	@DataFilter(subDept = true, user = false, tableAlias = "h")
	public PageUtils selectSendableList(Map<String, Object> params) {
		if(StringUtil.isEmpty(params.get("hospitalId"))) {
			throw new HdiException("请选择医院");
		}
		int currPage = Integer.parseInt(params.get("page").toString());
		int pageSize = Integer.parseInt(params.get("limit").toString());
		Page<GoodsSupplierSendEntityVo> page = new Page<GoodsSupplierSendEntityVo>(currPage, pageSize);
		
		List<GoodsSupplierSendEntityVo> list = this.baseMapper.selectSendableList(page, params);
		
		page.setRecords(list);
		
		return new PageUtils(page);
	}
	
	@Override
	@DataFilter(subDept = true, user = false, tableAlias = "t1")
	public PageUtils selectSentList(Map<String, Object> params) {
		int currPage = Integer.parseInt(params.get("page").toString());
		int pageSize = Integer.parseInt(params.get("limit").toString());
		Page<GoodsSupplierSendEntityVo> page = new Page<GoodsSupplierSendEntityVo>(currPage, pageSize);
		
		List<GoodsSupplierSendEntityVo> list = this.baseMapper.selectSentList(page, params);
		
		page.setRecords(list);
		
		return new PageUtils(page);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(GoodsSupplierSendEntity goodsSupplierSend) {
		goodsSupplierSend.setCreateTime(new Date());
		goodsSupplierSend.setIsUpload(SupplierIsUploadEnum.NO.getKey());
		this.baseMapper.insert(goodsSupplierSend);
		
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void batchSave(List<GoodsSupplierSendEntity> goodsSupplierSendList) {
		for (GoodsSupplierSendEntity goodsSupplierSend : goodsSupplierSendList) {
			save(goodsSupplierSend);
		}
		
	}

	@Override
	public List<TempPubGoodsEntity> selectNotUpload(int i) {
		return this.baseMapper.selectNotUpload(i);
	}

	@Override
	public List<TempPubSupplyGoodsEntity> selectNotUploadNew(int i) {
		return this.baseMapper.selectNotUploadNew(i);
	}

}
