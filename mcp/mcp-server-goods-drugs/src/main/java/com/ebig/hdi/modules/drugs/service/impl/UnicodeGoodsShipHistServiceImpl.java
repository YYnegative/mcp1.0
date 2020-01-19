package com.ebig.hdi.modules.drugs.service.impl;

import java.util.List;
import java.util.Map;

import com.ebig.hdi.modules.drugs.entity.UnicodeGoodsShipHistDrugsEntityVo;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.drugs.dao.UnicodeGoodsShipHistDrugsDao;
import com.ebig.hdi.modules.drugs.entity.UnicodeGoodsShipHistDrugsEntity;
import com.ebig.hdi.modules.drugs.service.UnicodeGoodsShipHistService;


@Service("unicodeGoodsShipHistDrugsService")
public class UnicodeGoodsShipHistServiceImpl extends ServiceImpl<UnicodeGoodsShipHistDrugsDao, UnicodeGoodsShipHistDrugsEntity> implements UnicodeGoodsShipHistService {

    @Override
    public PageUtils queryPageHospital(Map<String, Object> params) {
    	int currPage = Integer.parseInt(params.get("page").toString());
		int pageSize = Integer.parseInt(params.get("limit").toString());

		Page<UnicodeGoodsShipHistDrugsEntityVo > page = new Page<UnicodeGoodsShipHistDrugsEntityVo >(currPage, pageSize);
		List<UnicodeGoodsShipHistDrugsEntityVo > list = this.baseMapper.selectHospitalGoodsHist(page, params);
		page.setRecords(list);
		return new PageUtils(page);
    }
    
    
    @Override
    public PageUtils queryPageSupplier(Map<String, Object> params) {
    	int currPage = Integer.parseInt(params.get("page").toString());
		int pageSize = Integer.parseInt(params.get("limit").toString());

		Page<UnicodeGoodsShipHistDrugsEntityVo > page = new Page<UnicodeGoodsShipHistDrugsEntityVo >(currPage, pageSize);
		List<UnicodeGoodsShipHistDrugsEntityVo > list = this.baseMapper.selectSupplierGoodsHist(page, params);
		page.setRecords(list);
		return new PageUtils(page);
    }

}
