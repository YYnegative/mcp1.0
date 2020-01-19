package com.ebig.hdi.modules.surgery.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.enums.SequenceEnum;
import com.ebig.hdi.common.enums.SupplierIsUploadEnum;
import com.ebig.hdi.common.enums.SurgeryStageDetailDataSourceEnum;
import com.ebig.hdi.common.enums.SurgeryStageStatusEnum;
import com.ebig.hdi.common.enums.SurgeryStageTypeEnum;
import com.ebig.hdi.common.enums.SurgeryStatusEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.Constant;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.surgery.dao.SurgeryStageInfoDao;
import com.ebig.hdi.modules.surgery.entity.SurgeryInfoEntity;
import com.ebig.hdi.modules.surgery.entity.SurgeryStageDetailInfoEntity;
import com.ebig.hdi.modules.surgery.entity.SurgeryStageInfoEntity;
import com.ebig.hdi.modules.surgery.entity.vo.SurgeryStageDetailInfoVO;
import com.ebig.hdi.modules.surgery.entity.vo.SurgeryStageInfoVO;
import com.ebig.hdi.modules.surgery.service.SurgeryInfoService;
import com.ebig.hdi.modules.surgery.service.SurgeryStageDetailInfoService;
import com.ebig.hdi.modules.surgery.service.SurgeryStageInfoService;
import com.ebig.hdi.modules.sys.service.SysSequenceService;



@Service("surgeryStageInfoService")
public class SurgeryStageInfoServiceImpl extends ServiceImpl<SurgeryStageInfoDao, SurgeryStageInfoEntity> implements SurgeryStageInfoService {
	@Autowired
	private SurgeryInfoService surgeryInfoService;
	@Autowired
    private SysSequenceService sysSequenceService;
	@Autowired
	private SurgeryStageDetailInfoService surgeryStageDetailInfoService;
	
    @Override
    @DataFilter(subDept = true, user = false, tableAlias="t1")
    public PageUtils queryPage(Map<String, Object> params) {
    	SurgeryStageInfoVO stageInfoVO = new SurgeryStageInfoVO();
    	stageInfoVO.setHospitalName((String)params.get("hospitalName"));
    	stageInfoVO.setSurgeryNo((String)params.get("surgeryNo"));
    	stageInfoVO.setSurgeryStageNo((String)params.get("surgeryStageNo"));
    	stageInfoVO.setSurgeryTitle((String)params.get("surgeryTitle"));
    	stageInfoVO.setCustomName((String)params.get("customName"));
    	stageInfoVO.setStatus((Integer)params.get("status"));
    	stageInfoVO.setFileterDept((String)params.get(Constant.SQL_FILTER));
    	
    	String sidx = (String)params.get("sidx");
    	String order = (String)params.get("order");
    	int currPage = Integer.parseInt(params.get("page").toString());
    	int pageSize = Integer.parseInt(params.get("limit").toString());
    	
    	Page<SurgeryStageInfoVO> page = new Page<SurgeryStageInfoVO>(currPage, pageSize,order);
    	if(sidx!=null){
    		page.setAsc(sidx.equals("desc")?false:true);
    	}else{
    		//标志，设置默认按更新时间和创建时间排序
    		stageInfoVO.setIsDefaultOrder(1);
    	}
    	
    	List<SurgeryStageInfoVO> list = this.baseMapper.listForPage(page,stageInfoVO);
    	page.setRecords(list);
        return new PageUtils(page);
    }

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(SurgeryStageInfoVO stageInfoVO) {
		if(stageInfoVO != null){
			if(stageInfoVO.getHospitalId() == null){
				throw new HdiException("传入的医院id不能为空");
			}
			
			//如果数据来源是跟台目录新增，则插入新增手术单信息
			if(stageInfoVO.getSurgeryId() == null){
				SurgeryInfoEntity surgery = new SurgeryInfoEntity();
				surgery.setHospitalId(stageInfoVO.getHospitalId());
				surgery.setSurgeryNo(stageInfoVO.getSurgeryNo());
				surgery.setSurgeryTitle(stageInfoVO.getSurgeryTitle());
				surgery.setCustomName(stageInfoVO.getCustomName());
				Date surgeryDate = null;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if(StringUtils.isNotBlank(stageInfoVO.getSurgeryDateStr())){
					try{
						surgeryDate = sdf.parse(stageInfoVO.getSurgeryDateStr());
					}catch (ParseException e) {
						try{
							sdf = new SimpleDateFormat("yyyy-MM-dd");
							surgeryDate = sdf.parse(stageInfoVO.getSurgeryDateStr());
						}catch (ParseException e1) {
							throw new HdiException("手术时间格式不对！格式（2000-01-01 12:00:00）或者（2000-01-01）");
						}
					}
					surgery.setSurgeryDate(surgeryDate);
				}
				surgery.setDeptId(stageInfoVO.getDeptId());
				surgery.setStatus(SurgeryStatusEnum.NO_DISTRIBUTION.getKey());
				surgeryInfoService.insert(surgery);
				stageInfoVO.setSurgeryId(surgery.getId());
			}
			
			//新增跟台目录主单
			String surgeryStageNo = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.SURGERY_STAGE_NO.getKey());
			//跟台目录编号生成规则：医院id+手术单id+自增流水方式编码
			stageInfoVO.setSurgeryStageNo(stageInfoVO.getHospitalId()+stageInfoVO.getSurgeryId()+surgeryStageNo);
			stageInfoVO.setCreateTime(new Date());
			//设置新添加的跟台目录主单的状态为未配送
			stageInfoVO.setStatus(SurgeryStageStatusEnum.UNDISTRIBUTED.getKey());
			stageInfoVO.setSourceStageId(UUID.randomUUID().toString());
			stageInfoVO.setIsUpload(SupplierIsUploadEnum.NO.getKey());
			baseMapper.insert(stageInfoVO);
			
			
			//新增跟台目录细单
			List<SurgeryStageDetailInfoVO> detailVOList = stageInfoVO.getSurgeryStageDetailInfoVOList();
			for(SurgeryStageDetailInfoVO detail : detailVOList){
				detail.setSurgeryStageId(stageInfoVO.getId());
				setTime(detail);//设置时间
				//设置数据来源为供应商
				detail.setDataSource(SurgeryStageDetailDataSourceEnum.SUPPLIER.getKey());
				detail.setSourceStageDetailId(UUID.randomUUID().toString());
				surgeryStageDetailInfoService.insert(detail);
			}
		}
		
	}

	@Override
	public SurgeryStageInfoVO selectById(Long id) {
		if(id == null){
			throw new HdiException("传入的跟台目录id不能为空");
		}
		SurgeryStageInfoVO stageInfoVo = baseMapper.selectSurgeryStageInfoById(id);
		if(stageInfoVo == null){
			throw new HdiException("传入的跟台目录id查询不出数据");
		}
		List<SurgeryStageDetailInfoVO> stageDetailInfoVOList = surgeryStageDetailInfoService.selectBySurgeryStageId(id);
		if(stageDetailInfoVOList != null && stageDetailInfoVOList.size() >0 ){
			stageInfoVo.setSurgeryStageDetailInfoVOList(stageDetailInfoVOList);
		}
		return stageInfoVo;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(SurgeryStageInfoVO stageInfoVO) {
		if(stageInfoVO != null){
			//修改主单为未上传
			stageInfoVO.setIsUpload(SupplierIsUploadEnum.NO.getKey());
			this.baseMapper.insert(stageInfoVO);
			
			
			List<SurgeryStageDetailInfoVO> stageDetailList = stageInfoVO.getSurgeryStageDetailInfoVOList();
			
			//获取数据库中存在的跟台目录细表id
			List<Long> idList = getIdList(stageInfoVO.getId());
			for(SurgeryStageDetailInfoVO entityVO : stageDetailList){
				setTime(entityVO);//设置时间
				if(entityVO.getId() == null){
					//如果id不存在则执行新增操作
					entityVO.setSourceStageDetailId(UUID.randomUUID().toString());
					surgeryStageDetailInfoService.insert(entityVO);
				}else{
					//如果id存在则执行修改操作
					idList.remove(entityVO.getId());//把id从id列表中移除
					surgeryStageDetailInfoService.updateById(entityVO);
				}
			}
			//删除idList里对应的详情记录
			if(idList != null && idList.size() >0 ){
				surgeryStageDetailInfoService.deleteBatchIds(idList);
			}
		}
		
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void submit(List<Long> idList) {
		if(idList != null){
			for(Long id : idList){
				SurgeryStageInfoEntity entity = new SurgeryStageInfoEntity();
				entity.setId(id);
				entity.setStatus(SurgeryStageStatusEnum.DISTRIBUTED.getKey());
				baseMapper.updateById(entity);
			}
		}
		
	}
	
	/**
	 * 通过主单id获取所有细单的idList
	 * @param masterId
	 * @return
	 */
	private List<Long> getIdList(Long masterId){
		List<SurgeryStageDetailInfoEntity> oldStageDetailList = surgeryStageDetailInfoService.selectList(new EntityWrapper<SurgeryStageDetailInfoEntity>()
				.eq("surgery_stage_id", masterId));//原来的跟台目录详情信息List
		List<Long> idList = new ArrayList<>();
		for(SurgeryStageDetailInfoEntity entity : oldStageDetailList){
			idList.add(entity.getId());
		}
		return idList;
	}
	
	private void setTime(SurgeryStageDetailInfoVO entityVO){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		boolean flag = setTime(entityVO,sdf);
		if(!flag){
			sdf = new SimpleDateFormat("yyyy-MM-dd");
			flag = setTime(entityVO,sdf);
			if(!flag){
				throw new HdiException("跟台细单的时间格式不对！格式（2000-01-01 12:00:00）或者（2000-01-01）");
			}
		}
	}
	
	private boolean setTime(SurgeryStageDetailInfoVO entityVO,SimpleDateFormat sdf){
		Date plotProddate = null;
		Date plotValidto = null;
		Date slotProddate = null;
		Date slotValidto = null;
		try {
			if(StringUtils.isNotBlank(entityVO.getPlotProddateStr())){
				plotProddate = sdf.parse(entityVO.getPlotProddateStr());
				entityVO.setPlotProddate(plotProddate);
			}
			if(StringUtils.isNotBlank(entityVO.getPlotValidtoStr())){
				plotValidto = sdf.parse(entityVO.getPlotValidtoStr());
				entityVO.setPlotValidto(plotValidto);
			}
			if(StringUtils.isNotBlank(entityVO.getSlotProddateStr())){
				slotProddate = sdf.parse(entityVO.getSlotProddateStr());
				entityVO.setSlotProddate(slotProddate);
			}
			if(StringUtils.isNotBlank(entityVO.getSlotValidtoStr())){
				slotValidto = sdf.parse(entityVO.getSlotValidtoStr());
				entityVO.setSlotValidto(slotValidto);
			}
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	

}
