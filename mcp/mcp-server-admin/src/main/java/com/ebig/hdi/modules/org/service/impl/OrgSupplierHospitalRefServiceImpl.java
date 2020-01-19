package com.ebig.hdi.modules.org.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.enums.DelFlagEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.Query;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.org.dao.OrgSupplierHospitalRefDao;
import com.ebig.hdi.modules.org.entity.OrgHospitalInfoEntity;
import com.ebig.hdi.modules.org.entity.OrgSupplierHospitalRefEntity;
import com.ebig.hdi.modules.org.entity.OrgSupplierInfoEntity;
import com.ebig.hdi.modules.org.service.OrgHospitalInfoService;
import com.ebig.hdi.modules.org.service.OrgSupplierHospitalRefService;
import com.ebig.hdi.modules.org.service.OrgSupplierInfoService;


@Service("orgSupplierHospitalRefService")
public class OrgSupplierHospitalRefServiceImpl extends ServiceImpl<OrgSupplierHospitalRefDao, OrgSupplierHospitalRefEntity> implements OrgSupplierHospitalRefService {

	@Autowired
	private OrgSupplierInfoService orgSupplierInfoService;
	
	@Autowired
	private OrgHospitalInfoService orgHospitalInfoService;
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<OrgSupplierHospitalRefEntity> page = this.selectPage(
                new Query<OrgSupplierHospitalRefEntity>(params).getPage(),
                new EntityWrapper<OrgSupplierHospitalRefEntity>()
        );

        return new PageUtils(page);
    }

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void binding(Map<String, Object> params) {
		Long id = Long.valueOf(params.get("id").toString());
		if(StringUtil.isEmpty(id)){
			throw new HdiException("供应商id不能为空");
		}
		String hospitalIds = (String) params.get("hospitalIds");
		//全部解绑
		List<OrgSupplierHospitalRefEntity> supplierHospitalRefList = baseMapper.selectBySupplierId(id);
        if(StringUtil.isEmpty(hospitalIds)){
        	for(OrgSupplierHospitalRefEntity ref : supplierHospitalRefList){
    			ref.setDelFlag(DelFlagEnum.DELETE.getKey());
    			ref.setEditId(Long.valueOf(params.get("createId").toString()));
    			ref.setEditTime(new Date());
    			this.updateById(ref);
    		}
        }else{
        	//判断供应商医院绑定关系是否已经存在
    		List<String> rqHospitalIds = Arrays.asList(hospitalIds.split(","));
    		List<String> dbHospitalIds = new ArrayList<String>();
    		List<String> dbdHospitalIds = new ArrayList<String>();
    		List<String> dbnHospitalIds = new ArrayList<String>();
            for(OrgSupplierHospitalRefEntity supplierHospitalRef : supplierHospitalRefList){
            	dbHospitalIds.add(String.valueOf(supplierHospitalRef.getHospitalId()));
            	if(DelFlagEnum.DELETE.getKey().equals(supplierHospitalRef.getDelFlag())){
            		dbdHospitalIds.add(String.valueOf(supplierHospitalRef.getHospitalId()));
            	}else{
            		dbnHospitalIds.add(String.valueOf(supplierHospitalRef.getHospitalId()));
            	}
    		}
            //获取供应商信息
            OrgSupplierInfoEntity supplierInfo = orgSupplierInfoService.selectById(id);
    		//如果请求医院ID不在数据中，则是供应商新绑定医院
            List<String> rcjHospitalIds = new ArrayList<String>(rqHospitalIds);
            rcjHospitalIds.removeAll(dbHospitalIds);
            if(!StringUtil.isEmpty(rcjHospitalIds)){
            	//获取医院信息
                List<OrgHospitalInfoEntity> hospitalList = orgHospitalInfoService.selectBatchIds(rcjHospitalIds);
            	for(OrgHospitalInfoEntity hospital : hospitalList){
    	            //保存供应商医院绑定关系
    	    		OrgSupplierHospitalRefEntity ref = new OrgSupplierHospitalRefEntity();
    	        	ref.setSupplierId(id);
    	        	ref.setHospitalId(hospital.getId());
    	        	ref.setDelFlag(DelFlagEnum.NORMAL.getKey());
    	        	ref.setDeptId(supplierInfo.getDeptId());
    	        	ref.setCreateId(Long.valueOf(params.get("createId").toString()));
    	        	ref.setCreateTime(new Date());
    	        	
    	        	this.insert(ref);
    	        }
            }
            //如果请求医院ID在数据中，并且状态为解绑，则重新绑定
        	List<String> jjHospitalIds = new ArrayList<String>(rqHospitalIds);
        	jjHospitalIds.retainAll(dbdHospitalIds);
        	if(!StringUtil.isEmpty(jjHospitalIds)){
        		List<OrgSupplierHospitalRefEntity> refList = baseMapper.selectByHospitalIds(jjHospitalIds);
        		for(OrgSupplierHospitalRefEntity ref : refList){
        			ref.setDelFlag(DelFlagEnum.NORMAL.getKey());
        			ref.setEditId(Long.valueOf(params.get("createId").toString()));
        			ref.setEditTime(new Date());
        			this.updateById(ref);
        		}
        	}
        	//如果数据中医院ID不在请求中，则进行解绑
        	List<String> dcjHospitalIds = new ArrayList<String>(dbHospitalIds);
        	dcjHospitalIds.removeAll(rqHospitalIds);
        	if(!StringUtil.isEmpty(dcjHospitalIds)){
        		List<OrgSupplierHospitalRefEntity> refList = baseMapper.selectByHospitalIds(dcjHospitalIds);
        		for(OrgSupplierHospitalRefEntity ref : refList){
        			ref.setDelFlag(DelFlagEnum.DELETE.getKey());
        			ref.setEditId(Long.valueOf(params.get("createId").toString()));
        			ref.setEditTime(new Date());
        			this.updateById(ref);
        		}
        	}
        }
	}

	@Override
	public List<OrgHospitalInfoEntity> queryAllHospital(Long id) {
		return baseMapper.queryAllHospital(id);
	}

	@Override
	public List<OrgHospitalInfoEntity> queryMatchHospital(Long id) {
		return baseMapper.queryMatchHospital(id);
	}

}
