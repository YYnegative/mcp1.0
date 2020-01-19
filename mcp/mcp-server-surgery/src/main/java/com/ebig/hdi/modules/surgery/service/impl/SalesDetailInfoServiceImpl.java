package com.ebig.hdi.modules.surgery.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.surgery.dao.SalesDetailInfoDao;
import com.ebig.hdi.modules.surgery.entity.SalesDetailInfoEntity;
import com.ebig.hdi.modules.surgery.entity.vo.SalesDetailInfoVO;
import com.ebig.hdi.modules.surgery.service.SalesDetailInfoService;


@Service("salesDetailInfoService")
public class SalesDetailInfoServiceImpl extends ServiceImpl<SalesDetailInfoDao, SalesDetailInfoEntity> implements SalesDetailInfoService {

    @Override
    @DataFilter(subDept = true, user = false, tableAlias="t1")
    public PageUtils queryPage(Map<String, Object> params) {
    	if(params.get("id") == null){
    		throw new HdiException("传入的销售单id不能为空");
    	}
    	int currPage = Integer.parseInt(params.get("page").toString());
    	int pageSize = Integer.parseInt(params.get("limit").toString());

    	Page<SalesDetailInfoVO> page = new Page<SalesDetailInfoVO>(currPage, pageSize);
    	
    	List<SalesDetailInfoVO> list = this.baseMapper.listForPage(page,Long.valueOf(params.get("id").toString()));

    	page.setRecords(list);
        return new PageUtils(page);
    }

}
