package com.ebig.hdi.modules.refunds.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.refunds.dao.RefundsDetailDao;
import com.ebig.hdi.modules.refunds.entity.RefundsApplyMasterEntity;
import com.ebig.hdi.modules.refunds.entity.RefundsDetailEntity;
import com.ebig.hdi.modules.refunds.entity.RefundsMasterEntity;
import com.ebig.hdi.modules.refunds.entity.vo.RefundsDetailVO;
import com.ebig.hdi.modules.refunds.service.RefundsApplyMasterService;
import com.ebig.hdi.modules.refunds.service.RefundsDetailService;
import com.ebig.hdi.modules.refunds.service.RefundsMasterService;


@Service("refundsDetailService")
public class RefundsDetailServiceImpl extends ServiceImpl<RefundsDetailDao, RefundsDetailEntity> implements RefundsDetailService {
	@Autowired
	private RefundsApplyMasterService refundsApplyMasterService;
    @Override
    @DataFilter(subDept = true, user = false, tableAlias = "t1")
    public PageUtils queryPage(Map<String, Object> params) {
    	if(params.get("id") == null){
    		throw new HdiException("传入的退货主单id不能为空");
    	}
    	int currPage = Integer.parseInt(params.get("page").toString());
    	int pageSize = Integer.parseInt(params.get("limit").toString());
    	
    	Page<RefundsDetailVO> page = new Page<RefundsDetailVO>(currPage, pageSize);
    	
    	List<RefundsDetailVO> list = this.baseMapper.listForPage(page,Long.valueOf(params.get("id").toString()));
    	
    	page.setRecords(list);
        return new PageUtils(page);
    }

	@Override
	public List<RefundsDetailVO> selectByMasterId(Long id) {
		return baseMapper.selectByMasterId(id);
	}

	@Override
	public List<RefundsDetailVO> selectByApplyNo(String no) {
		if(StringUtils.isBlank(no)){
			throw new HdiException("传入的医院退货申请单编号不能为空");
		}
		//通过退货申请单编号查询出退货申请单
		RefundsApplyMasterEntity master = refundsApplyMasterService.selectOne(new EntityWrapper<RefundsApplyMasterEntity>()
				.eq("refunds_apply_no", no));
		if(master == null){
			throw new HdiException("输出的退货申请单号查询不出数据");
		}
		return baseMapper.selectByMasterId(master.getId());
	}

}
