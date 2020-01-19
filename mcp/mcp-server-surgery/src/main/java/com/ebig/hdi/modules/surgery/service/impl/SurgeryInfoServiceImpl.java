package com.ebig.hdi.modules.surgery.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.Constant;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.surgery.dao.SurgeryInfoDao;
import com.ebig.hdi.modules.surgery.entity.SurgeryInfoEntity;
import com.ebig.hdi.modules.surgery.entity.vo.SurgeryInfoVO;
import com.ebig.hdi.modules.surgery.entity.vo.SurgeryStageInfoVO;
import com.ebig.hdi.modules.surgery.service.SurgeryInfoService;


@Service("surgeryInfoService")
public class SurgeryInfoServiceImpl extends ServiceImpl<SurgeryInfoDao, SurgeryInfoEntity> implements SurgeryInfoService {

    @Override
    @DataFilter(subDept = true, user = false, tableAlias="t1")
    public PageUtils queryPage(Map<String, Object> params) {
    	/*if(params.get("supplierId") == null){
    		throw new HdiException("供应商id不能为空");
    		
    	}*/
    	SurgeryInfoVO siVO = new SurgeryInfoVO();
    	//siVO.setSupplierId(Long.valueOf(params.get("supplierId").toString()));
    	siVO.setHospitalName((String)params.get("hospitalName"));
    	siVO.setSurgeryNo((String)params.get("surgeryNo"));
    	siVO.setCustomName((String)params.get("customName"));
    	siVO.setSurgeryDateBeginStr((String)params.get("dateBeginStr"));
    	siVO.setSurgeryDateEndStr((String)params.get("dateEndStr"));
    	siVO.setStatus((Integer)params.get("status"));
    	siVO.setFileterDept((String)params.get(Constant.SQL_FILTER));
    	
    	String sidx = (String)params.get("sidx");
    	String order = (String)params.get("order");
    	int currPage = Integer.parseInt(params.get("page").toString());
    	int pageSize = Integer.parseInt(params.get("limit").toString());
        
    	Page<SurgeryInfoVO> page = new Page<SurgeryInfoVO>(currPage, pageSize,order);
    	if(sidx!=null){
    		page.setAsc(sidx.equals("desc")?false:true);
    	}else{
    		//标志，设置默认按更新时间和创建时间排序
    		siVO.setIsDefaultOrder(1);
    	}
    	
    	List<SurgeryInfoVO> list = this.baseMapper.listForPage(page,siVO);
    	page.setRecords(list);
        return new PageUtils(page);
    }

	@Override
	public SurgeryInfoVO selectById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SurgeryStageInfoVO createStage(Long id) {
		return baseMapper.createStageMaster(id);
	}

	@Override
	public List<SurgeryInfoEntity> querySurgeryNo(Map<String, Object> params) {
		if(params != null){
			if(params.get("customerId") == null){
				throw new HdiException("传入的医院id(customerId)不能为空");
			}
			if(params.get("supplierId") == null){
				throw new HdiException("传入的供应商id(supplierId)不能为空");
			}
			return baseMapper.querySurgeryNo(params);
		}
		return null;
	}

}
