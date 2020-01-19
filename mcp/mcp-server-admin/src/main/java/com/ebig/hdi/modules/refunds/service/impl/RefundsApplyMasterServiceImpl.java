package com.ebig.hdi.modules.refunds.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.enums.HospitalDataSource;
import com.ebig.hdi.common.enums.RefundsApplyTypeEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.Constant;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.org.service.OrgSupplierInfoService;
import com.ebig.hdi.modules.refunds.dao.RefundsApplyMasterDao;
import com.ebig.hdi.modules.refunds.entity.RefundsApplyDetailEntity;
import com.ebig.hdi.modules.refunds.entity.RefundsApplyMasterEntity;
import com.ebig.hdi.modules.refunds.entity.vo.RefundsApplyDetailVO;
import com.ebig.hdi.modules.refunds.entity.vo.RefundsApplyMasterVO;
import com.ebig.hdi.modules.refunds.entity.vo.RefundsDetailVO;
import com.ebig.hdi.modules.refunds.entity.vo.RefundsMasterVO;
import com.ebig.hdi.modules.refunds.service.RefundsApplyDetailService;
import com.ebig.hdi.modules.refunds.service.RefundsApplyMasterService;


@Service("refundsApplyMasterService")
public class RefundsApplyMasterServiceImpl extends ServiceImpl<RefundsApplyMasterDao, RefundsApplyMasterEntity> implements RefundsApplyMasterService {
	
	@Autowired
	private RefundsApplyDetailService refundsApplyDetailService;
	@Autowired
	private OrgSupplierInfoService orgSupplierInfoService;
	
    @Override
    @DataFilter(subDept = true, user = false, tableAlias="t1")
    public PageUtils queryPage(Map<String, Object> params) {
    	RefundsApplyMasterVO ramVO = new RefundsApplyMasterVO();
    	ramVO.setHospitalName((String)params.get("hospitalName"));
    	ramVO.setStoreHouseName((String)params.get("storeHouseName"));
    	ramVO.setRefundsApplyNo((String)params.get("refundsApplyNo"));
    	ramVO.setApplyTimeBeginStr((String)params.get("applyTimeBeginStr"));
    	ramVO.setApplyTimeEndStr((String)params.get("applyTimeEndStr"));
    	ramVO.setStatus((Integer)params.get("status"));
    	ramVO.setFileterDept((String)params.get(Constant.SQL_FILTER));
    	
    	String sidx = (String)params.get("sidx");
    	String order = (String)params.get("order");
    	int currPage = Integer.parseInt(params.get("page").toString());
    	int pageSize = Integer.parseInt(params.get("limit").toString());
        
    	Page<RefundsApplyMasterVO> page = new Page<RefundsApplyMasterVO>(currPage, pageSize,order);
    	if(sidx!=null){
    		page.setAsc(sidx.equals("desc")?false:true);
    	}else{
    		//标志，设置默认按更新时间和创建时间排序
    		ramVO.setIsDefaultOrder(1);
    	}
    	//自定义分页查询列表带过滤条件
    	List<RefundsApplyMasterVO> list = this.baseMapper.listForPage(page,ramVO);
    	//查询并设置退回
    	//setDetail(list);
    	
    	page.setRecords(list);
        return new PageUtils(page);
    }
    
    @Override
	public RefundsApplyMasterVO selectById(Long id) {
    	RefundsApplyMasterVO ramVO = baseMapper.selectRefundsApplyById(id);
    	if(ramVO == null){
    		throw new HdiException("输入的退货申请主单id查不出数据");
    	}
    	//通过退货申请主单id查询出所对应的所有细单
    	List<RefundsApplyDetailVO> radeList = refundsApplyDetailService.selectByMasterId(ramVO.getId());
    	ramVO.setDetailVOList(radeList);
		return ramVO;
	}
    
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(RefundsApplyMasterVO ramVO) {
		if(ramVO != null){
			//校验必填参数并且设置申请时间
			checkValueAndSetTime(ramVO);
			//设置原供应商id
			ramVO.setSourcesSupplierId(orgSupplierInfoService.selectSourceSupplierId(ramVO.getSupplierId(), ramVO.getHospitalId()));
			//设置初始状态为未确定
			ramVO.setStatus(RefundsApplyTypeEnum.UNCONFIRMED.getKey());
			ramVO.setCreateTime(new Date());
			//设置为系统录入
			ramVO.setDataSource(HospitalDataSource.SYSTEM.getKey());
			baseMapper.insert(ramVO);
			
			//批量插入退货申请详细单
			List<RefundsApplyDetailEntity> radeList = ramVO.getDetailList();
			//设置详情单的退货申请单id和新增必填信息
			for(RefundsApplyDetailEntity detail : radeList){
				detail.setApplyMasterId(ramVO.getId());
				detail.setCreateId(ramVO.getCreateId());
				detail.setCreateTime(ramVO.getCreateTime());
			}
			//批量插入数据
			refundsApplyDetailService.insertBatch(radeList);
		}
		
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(RefundsApplyMasterVO ramVO) {
		if(ramVO != null){
			Long masterId = ramVO.getId();
			if(masterId == null){
				throw new HdiException("修改传入的id不能为空");
			}
			//设置申请时间
			if(StringUtils.isNotBlank(ramVO.getApplyTimeStr())){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date applyTime = null;
				try{
					applyTime = sdf.parse(ramVO.getApplyTimeStr());
					ramVO.setApplyTime(applyTime);
				}catch (Exception e) {
					throw new HdiException("申请时间格式不正确(1960-01-01 00:00:00)");
				}
			}
			//修改退货申请主单信息
			ramVO.setEditTime(new Date());
			baseMapper.updateById(ramVO);
			
			//修改退货申请详情信息
			List<RefundsApplyDetailEntity> radeList = refundsApplyDetailService.selectList(new EntityWrapper<RefundsApplyDetailEntity>()
					.eq("apply_master_id", masterId)
					.eq("del_flag", 0));//原来的详情信息
			List<RefundsApplyDetailEntity> updateDetailList = ramVO.getDetailList();//修改的详情信息
			
			//获取数据库里的退货申请详情的idlist
			List<Long> idList = getIdList(radeList);
			
			//循环遍历 没有id的话直接插入，有id的话把id从idList里移除，idList里剩下的就是被删除了的id
			for(RefundsApplyDetailEntity updateDetail : updateDetailList){
				if(updateDetail.getId() == null){
					//执行新增操作
					updateDetail.setApplyMasterId(masterId);
					updateDetail.setCreateId(ramVO.getEditId());
					updateDetail.setCreateTime(ramVO.getEditTime());
					refundsApplyDetailService.insert(updateDetail);
				}else{
					//从idList里移除id并且执行修改操作
					idList.remove(updateDetail.getId());
					
					//执行详情修改操作
					updateDetail.setEditId(ramVO.getEditId());
					updateDetail.setEditTime(ramVO.getEditTime());
					refundsApplyDetailService.updateById(updateDetail);
				}
			}
			
			//删除idList里对应的详情记录
			if(idList !=null && idList.size()  > 0 ){
				refundsApplyDetailService.deleteBatchIds(idList);
			}
		}
		
	}
	
	@Override
	public List<RefundsApplyDetailVO> selectToSave(Map<String, Object> params) {
		if(params.get("hospitalId") == null || params.get("sourcesHospitalId") == null){
			throw new HdiException("传入的医院id/原医院id不能为空");
		}
		if(params.get("storeHouseId") == null || params.get("sourcesStoreHouseId") == null){
			throw new HdiException("传入的库房id/原库房id不能为空");
		}
		if(params.get("supplierId") == null ){
			throw new HdiException("传入的供应商id不能为空");
		}
		return baseMapper.selectToSave(params);
	}
	
	@Override
	public List<String> selectRefundsApplyNo(Map<String, Object> params) {
		if(params.get("hospitalId") == null ){
			throw new HdiException("传入的医院id");
		}
		if(params.get("storeHouseId") == null){
			throw new HdiException("传入的库房id");
		}
		if(params.get("supplierId") == null){
			throw new HdiException("传入的供应商id不能为空");
		}
		return baseMapper.selectRefundsApplyNo(params);
	}
	
	@Override
	public RefundsMasterVO change(Long id) {
		if(id == null){
			throw new HdiException("传入的id不能为空");
		}
		//把退货申请主单转化为退货单
		RefundsMasterVO rbaseMapper =  baseMapper.changeMaster(id);
		//把退货申请细单转化为退货细单
		List<RefundsDetailVO> refundsDetailVOList = refundsApplyDetailService.changeDetail(id);
		rbaseMapper.setRefundsDetailVOList(refundsDetailVOList);
		return rbaseMapper;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void determine(Long[] ids,Long userId) {
		if(ids != null){
			RefundsApplyMasterEntity rame = new RefundsApplyMasterEntity();
			rame.setEditId(userId);
			rame.setEditTime(new Date());
			rame.setStatus(RefundsApplyTypeEnum.CONFIRMED.getKey());
			for(Long id : ids){
				rame.setId(id);
				baseMapper.updateById(rame);
			}
		}
		
	}
	
	/**
	 * 根据退货申请主单设置对应的退货申请详情单
	 * @param list
	 */
	private void setDetail(List<RefundsApplyMasterVO> list){
		if(list != null){
			//循环查询退货申请细单数据
			for(RefundsApplyMasterVO ramVO : list){
				ramVO.setDetailVOList(refundsApplyDetailService.selectByMasterId(ramVO.getId()));
			}
		}
	}
	
	private List<Long> getIdList(List<RefundsApplyDetailEntity> radeList){
		if(radeList != null){
			List<Long> idList = new ArrayList<>();
			for(RefundsApplyDetailEntity entity : radeList){
				idList.add(entity.getId());
			}
			return idList;
		}
		return null;
	}
	
	private void checkValueAndSetTime(RefundsApplyMasterVO ramVO){
		if(ramVO.getSupplierId() == null){
			throw new HdiException("供应商id不能为空");
		}
		if(ramVO.getHospitalId() == null){
			throw new HdiException("医院id不能为空");
		}
		if(ramVO.getStoreHouseId() == null){
			throw new HdiException("库房id不能为空");
		}
		if(StringUtils.isBlank(ramVO.getRefundsApplyNo())){
			throw new HdiException("医院退货申请单号不能为空");
		}
		if(StringUtils.isBlank(ramVO.getApplyTimeStr())){
			throw new HdiException("申请时间不能为空");
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date applyTime = null;
			try{
				applyTime = sdf.parse(ramVO.getApplyTimeStr());
				ramVO.setApplyTime(applyTime);
			}catch (Exception e) {
				throw new HdiException("申请时间格式不正确(1960-01-01 00:00:00)");
			}
		}
	}

	

	

	

	

	

	

}
