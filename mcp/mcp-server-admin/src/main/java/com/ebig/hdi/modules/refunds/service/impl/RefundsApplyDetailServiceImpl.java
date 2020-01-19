package com.ebig.hdi.modules.refunds.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.refunds.dao.RefundsApplyDetailDao;
import com.ebig.hdi.modules.refunds.entity.RefundsApplyDetailEntity;
import com.ebig.hdi.modules.refunds.entity.RefundsApplyMasterEntity;
import com.ebig.hdi.modules.refunds.entity.vo.RefundsApplyDetailVO;
import com.ebig.hdi.modules.refunds.entity.vo.RefundsDetailVO;
import com.ebig.hdi.modules.refunds.service.RefundsApplyDetailService;
import com.ebig.hdi.modules.refunds.service.RefundsApplyMasterService;


@Service("refundsApplyDetailService")
public class RefundsApplyDetailServiceImpl extends ServiceImpl<RefundsApplyDetailDao, RefundsApplyDetailEntity> implements RefundsApplyDetailService {
	
	@Autowired
	private RefundsApplyMasterService refundsApplyMasterService;
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
    	if(params.get("id") == null){
    		throw new HdiException("传入的退货申请主单id不能为空");
    	}
    	int currPage = Integer.parseInt(params.get("page").toString());
    	int pageSize = Integer.parseInt(params.get("limit").toString());
    	
    	Page<RefundsApplyDetailVO> page = new Page<RefundsApplyDetailVO>(currPage, pageSize);
    	
    	List<RefundsApplyDetailVO> list = this.baseMapper.listForPage(page,Long.valueOf(params.get("id").toString()));
    	
    	page.setRecords(list);
        return new PageUtils(page);
    }

	@Override
	public List<RefundsApplyDetailVO> selectByMasterId(Long id) {
		return baseMapper.selectByMasterId(id);
	}

	@Override
	public List<RefundsDetailVO> changeDetail(Long id) {
		return baseMapper.changeDetail(id);
	}

	@Override
	public List<RefundsApplyDetailVO> selectByApplyNo(String no) {
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
