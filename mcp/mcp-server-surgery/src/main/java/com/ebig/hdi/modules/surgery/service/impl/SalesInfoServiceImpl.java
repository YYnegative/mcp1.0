package com.ebig.hdi.modules.surgery.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.Constant;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.surgery.dao.SalesInfoDao;
import com.ebig.hdi.modules.surgery.entity.SalesInfoEntity;
import com.ebig.hdi.modules.surgery.entity.vo.SalesDetailInfoVO;
import com.ebig.hdi.modules.surgery.entity.vo.SalesInfoVO;
import com.ebig.hdi.modules.surgery.service.SalesDetailInfoService;
import com.ebig.hdi.modules.surgery.service.SalesInfoService;


@Service("salesInfoService")
public class SalesInfoServiceImpl extends ServiceImpl<SalesInfoDao, SalesInfoEntity> implements SalesInfoService {
	
	@Autowired
	private SalesDetailInfoService salesDetailInfoService;
	
    @Override
    @DataFilter(subDept = true, user = false, tableAlias="t1")
    public PageUtils queryPage(Map<String, Object> params) {
    	SalesInfoVO salesVO = new SalesInfoVO();
    	salesVO.setHospitalName((String)params.get("hospitalName"));
    	salesVO.setSalesNo((String)params.get("salesNo"));
    	//医院清台结算单编号
    	salesVO.setSurgeryTitle((String)params.get("surgeryTitle"));
    	salesVO.setCustomName((String)params.get("customName"));
    	salesVO.setStatus((Integer)params.get("status"));
    	salesVO.setFileterDept((String)params.get(Constant.SQL_FILTER));
    	
    	String sidx = (String)params.get("sidx");
    	String order = (String)params.get("order");
    	int currPage = Integer.parseInt(params.get("page").toString());
    	int pageSize = Integer.parseInt(params.get("limit").toString());
    	
    	Page<SalesInfoVO> page = new Page<SalesInfoVO>(currPage, pageSize,order);
    	if(sidx!=null){
    		page.setAsc(sidx.equals("desc")?false:true);
    	}else{
    		//标志，设置默认按更新时间和创建时间排序
    		salesVO.setIsDefaultOrder(1);
    	}

    	List<SalesInfoVO> list = this.baseMapper.listForPage(page,salesVO);
    	page.setRecords(list);
        return new PageUtils(page);
    }

	@Override
	public void save(SalesInfoVO salesInfoVO) {
		if(salesInfoVO != null){
			
			//检查必填参数
			checkValue(salesInfoVO);
			salesInfoVO.setSalesTime(setTime(salesInfoVO.getSalesTimeStr()));
			
			//保存数据
			baseMapper.insert(salesInfoVO);
			
			//保存细单
			List<SalesDetailInfoVO> detailVOList = salesInfoVO.getSalesDetailInfoVOList();
			for(SalesDetailInfoVO detailVO : detailVOList){
				detailVO.setPlotProddate(setTime(detailVO.getPlotProddateStr()));
				detailVO.setPlotValidto(setTime(detailVO.getPlotValidtoStr()));
				detailVO.setSalesId(salesInfoVO.getId());
				salesDetailInfoService.insert(detailVO);
			}
		}
		
	}
	
	@Override
	public void update(SalesInfoVO salesInfoVO) {
		if(salesInfoVO != null){
			if(salesInfoVO.getId() == null){
				throw new HdiException("传入的销售单id不能为空");
			}
			
			//保存销售单
			baseMapper.insert(salesInfoVO);
			
			List<SalesDetailInfoVO> detailList = salesInfoVO.getSalesDetailInfoVOList();
			
			for(SalesDetailInfoVO detailVO : detailList){
				
			}
		}
		
	}
	
	private void checkValue(SalesInfoVO salesInfoVO){
		if(salesInfoVO.getCustomerId() == null){
			throw new HdiException("传入的医院id不能为空");
		}
		if(salesInfoVO.getSellerId() == null){
			throw new HdiException("传入的供应商id不能为空");
		}
		if(StringUtils.isBlank(salesInfoVO.getSalesNo())){
			throw new HdiException("传入的销售单号不能为空");
		}
		if(salesInfoVO.getSellerId() == null){
			throw new HdiException("传入的销方id(供应商id)不能为空");
		}
	}
	
	private Date setTime(String dateStr){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = setTime(dateStr,sdf);
		if(date == null){
			sdf = new SimpleDateFormat("yyyy-MM-dd");
			date = setTime(dateStr,sdf);
			if(date == null){
				throw new HdiException("销售时间格式不对！格式（2000-01-01 12:00:00）或者（2000-01-01）");
			}
		}
		return date;
	}
	
	private Date setTime(String dateStr,SimpleDateFormat sdf){
		Date salesTime = null;
		try {
			if(StringUtils.isNotBlank(dateStr)){
				salesTime = sdf.parse(dateStr);
			}
		} catch (ParseException e) {
			
		}
		return salesTime;
	}

	

}
