package com.ebig.hdi.modules.surgery.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.surgery.dao.SurgeryStageDetailInfoDao;
import com.ebig.hdi.modules.surgery.entity.SurgeryStageDetailInfoEntity;
import com.ebig.hdi.modules.surgery.entity.vo.SurgeryStageDetailInfoVO;
import com.ebig.hdi.modules.surgery.service.SurgeryStageDetailInfoService;


@Service("surgeryStageDetailInfoService")
public class SurgeryStageDetailInfoServiceImpl extends ServiceImpl<SurgeryStageDetailInfoDao, SurgeryStageDetailInfoEntity> implements SurgeryStageDetailInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
    	if(params.get("id") == null){
    		throw new HdiException("传入的跟台目录id不能为空");
    	}
    	int currPage = Integer.parseInt(params.get("page").toString());
    	int pageSize = Integer.parseInt(params.get("limit").toString());
    	
    	Page<SurgeryStageDetailInfoVO> page = new Page<SurgeryStageDetailInfoVO>(currPage, pageSize);
    	
    	List<SurgeryStageDetailInfoVO> list = this.baseMapper.listForPage(page,Long.valueOf(params.get("id").toString()));

    	page.setRecords(list);
        return new PageUtils(page);
    }

	@Override
	public List<SurgeryStageDetailInfoVO> selectToSave(Map<String, Object> params) {
		//校验参数
		if(params.get("supplierId") == null){
			throw new HdiException("传入的供应商id不能为空");
		}
		return baseMapper.selectToSave(params);
	}

	@Override
	public List<SurgeryStageDetailInfoVO> selectBySurgeryStageId(Long id) {
		if(id != null){
			return baseMapper.selectBySurgeryStageId(id);
		}
		return null;
	}

}
