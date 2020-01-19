package com.ebig.hdi.modules.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.entity.OrgFactoryInfoApprovalEntity;
import com.ebig.hdi.common.entity.OrgFactoryInfoEntity;
import com.ebig.hdi.common.enums.DelFlagEnum;
import com.ebig.hdi.common.enums.SequenceEnum;
import com.ebig.hdi.common.enums.StatusEnum;
import com.ebig.hdi.common.enums.TypeEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.Query;
import com.ebig.hdi.common.utils.ReflectUitls;
import com.ebig.hdi.common.utils.StringUtil;

import com.ebig.hdi.modules.core.dao.OrgFactoryInfoDao;
import com.ebig.hdi.modules.core.param.OrgFactoryParam;
import com.ebig.hdi.modules.core.service.OrgFactoryInfoService;
import com.ebig.hdi.modules.sys.entity.SysDictEntity;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import com.ebig.hdi.modules.sys.service.SysDictService;
import com.ebig.hdi.modules.sys.service.SysSequenceService;
import com.ebig.hdi.modules.sys.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("orgFactoryInfoService_")
public class OrgFactoryInfoServiceImpl extends ServiceImpl<OrgFactoryInfoDao, OrgFactoryInfoEntity> implements OrgFactoryInfoService {

	@Autowired
    private SysSequenceService sysSequenceService;
	
	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private OrgFactoryInfoApprovalServiceImpl orgFactoryInfoApprovalService;

	@Autowired
	private SysDictService sysDictService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
    	String factoryName = (String) params.get("factoryName");
    	String countryCode = (String) params.get("countryCode");
    	String provinceCode = (String) params.get("provinceCode");
    	String cityCode = (String) params.get("cityCode");
    	Integer status = (Integer) params.get("status");
    	
        Page<OrgFactoryInfoEntity> page = this.selectPage(
                new Query<OrgFactoryInfoEntity>(params).getPage(),
                new EntityWrapper<OrgFactoryInfoEntity>()
                .like(StringUtils.isNotBlank(factoryName), "factory_name", factoryName)
                .eq(StringUtils.isNotBlank(countryCode), "country_code", countryCode)
                .eq(StringUtils.isNotBlank(provinceCode), "province_code", provinceCode)
                .eq(StringUtils.isNotBlank(cityCode), "city_code", cityCode)
                .eq(status != null, "status", status)
                .eq("del_flag", DelFlagEnum.NORMAL.getKey())
                .orderBy("edit_time", false)
                .orderBy("create_time", false)
        );

        return new PageUtils(page);
    }
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(OrgFactoryInfoEntity orgFactoryInfo) {
		SysUserEntity sysUserEntity = sysUserService.selectById(orgFactoryInfo.getCreateId());
		if (StringUtil.isEmpty(sysUserEntity)) {
			throw new HdiException("用户不存在！");
		}
		
		if(!TypeEnum.USER_PLATFORM.getKey().equals(sysUserEntity.getUserType())) {
			throw new HdiException("非平台用户无法操作该功能！");
		}
		
		List<OrgFactoryInfoEntity> factoryName = baseMapper.selectByFactoryName(orgFactoryInfo.getFactoryName());
		if(!StringUtil.isEmpty(factoryName)){
			throw new HdiException("厂商名称已存在");
		}
		List<OrgFactoryInfoEntity> creditCode = baseMapper.selectByCreditCode(orgFactoryInfo.getCreditCode());
		if(!StringUtil.isEmpty(creditCode)){
			throw new HdiException("厂商信用代码已存在");
		}
		//获取厂商编码序列号
		String FactoryCode = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.FACTORY_CODE.getKey());
		orgFactoryInfo.setFactoryCode(FactoryCode);
		orgFactoryInfo.setCreateTime(new Date());
		orgFactoryInfo.setDelFlag(DelFlagEnum.NORMAL.getKey());
		this.insert(orgFactoryInfo);

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(OrgFactoryInfoEntity orgFactoryInfo) {
		List<OrgFactoryInfoEntity> factoryName = baseMapper.selectByFactoryName(orgFactoryInfo.getFactoryName());
		if(!StringUtil.isEmpty(factoryName) && !orgFactoryInfo.getId().equals(factoryName.get(0).getId())){
			throw new HdiException("厂商名称已存在");
		}
		List<OrgFactoryInfoEntity> creditCode = baseMapper.selectByCreditCode(orgFactoryInfo.getCreditCode());
		if(!StringUtil.isEmpty(creditCode) && !orgFactoryInfo.getId().equals(creditCode.get(0).getId())){
			throw new HdiException("厂商信用代码已存在");
		}
		this.updateById(orgFactoryInfo);
	}

	@Override
	public List<OrgFactoryInfoEntity> queryByFactoryName(Map<String, Object> params) {
		String factoryName = (String) params.get("factoryName");
		return this.selectList(
				new EntityWrapper<OrgFactoryInfoEntity>()
				.like(StringUtils.isNotBlank(factoryName), "factory_name", factoryName)
				.eq("status", StatusEnum.USABLE.getKey())
				.eq("del_flag", DelFlagEnum.NORMAL.getKey())
				);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long[] ids) {
		for(Long id : ids) {
			OrgFactoryInfoEntity orgFactoryInfo = new OrgFactoryInfoEntity();
			orgFactoryInfo.setId(id);
			orgFactoryInfo.setDelFlag(DelFlagEnum.DELETE.getKey());
			this.updateById(orgFactoryInfo);
		}
	}

	@Override
	public List<Map<String, Object>> getList(String[] columns, OrgFactoryParam queryParam) {
		List<Map<String, Object>> list = this.baseMapper.getList(columns, queryParam);
		return list;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void toggle(Map<String, Object> params) {
    	orgFactoryInfoApprovalService.toggle(params);

		List<Long> ids = JSON.parseArray(params.get("ids").toString(), Long.class);
		for (Long id : ids) {
         OrgFactoryInfoEntity orgFactoryInfo=new OrgFactoryInfoEntity();
         orgFactoryInfo.setId(id);
         orgFactoryInfo.setStatus(Integer.valueOf(params.get("status").toString()));
         this.baseMapper.updateById(orgFactoryInfo);
		}
	}

	@Override
	public Map importData(String[][] rows, Long userId, Long deptId) {
		OrgFactoryInfoEntity orgFactoryInfoEntity = new  OrgFactoryInfoEntity();
		List<OrgFactoryInfoEntity> factorylits;
		boolean insert;
		StringBuilder sb = new StringBuilder();
		SysDictEntity sysDictEntity = new SysDictEntity();
		List<?> codeList;
		//保存返回信息
		String key = "successCount";
		Map<String, Object> map = new HashMap<>(16);
		Map<String, Object> cityMap = new HashMap<>(4);
		PageUtils page;
		cityMap.put("page","1");
		cityMap.put("limit","1");
		if (userId == null || deptId == null || rows.length <= 1) {
			map.put(key, "0");
			map.put("failCount", String.valueOf(rows.length - 1));
			sb.append("系统错误");
			map.put("errorMessage", sb.toString());
			return map;
		}
		Integer failCount = 0;
		Integer successCount = 0;
		for (int i = 1; i < rows.length; i++) {
			String[] row = rows[i];
			if (StringUtil.isEmpty(row[0]) || StringUtil.isEmpty(row[1])){
				sb.append("第 " + i  + " 行数据错误，请检查数据是否正确");
				sb.append("\n");
				failCount++;
				continue;
			}
			 factorylits = baseMapper.selectByCreditCodeOrFactoryName(row[0],row[1]);
			if(!StringUtil.isEmpty(factorylits)){
				sb.append("第 " + i  + " 行数据错误，厂商名称与信用代码已经存在");
				sb.append("\n");
				failCount++;
				continue;
			}
			String str = row[2];
			//分割地区字符串
			String[]  strs=str.split("/");
			if (strs.length != 3){
				sb.append("第 " + i  + " 行数据错误，输入厂商区域格式错误");
				sb.append("\n");
				failCount++;
				continue;
			}
			cityMap.put("value",strs[0]);
			page  = sysDictService.queryPage(cityMap);
			if(page.getList().size() == 0){
				sb.append("第 " + i  + " 行数据错误，省份或者直辖市不存在");
				sb.append("\n");
				failCount++;
				continue;
			}
			codeList = page.getList();
			sysDictEntity = (SysDictEntity) codeList.get(0);
			orgFactoryInfoEntity.setCountryCode(sysDictEntity.getParentCode());
			orgFactoryInfoEntity.setProvinceCode(sysDictEntity.getCode());

			cityMap.put("value",strs[1]);
			page  = sysDictService.queryPage(cityMap);
			if(page.getList().size() == 0){
				sb.append("第 " + i  + " 行数据错误，城市不存在");
				sb.append("\n");
				failCount++;
				continue;
			}
			codeList = page.getList();
			sysDictEntity = (SysDictEntity) codeList.get(0);
			orgFactoryInfoEntity.setCityCode(sysDictEntity.getCode());

			cityMap.put("value",strs[2]);
			page  = sysDictService.queryPage(cityMap);
			if(page.getList().size() == 0){
				sb.append("第 " + i  + " 行数据错误，区不存在");
				sb.append("\n");
				failCount++;
				continue;
			}codeList = page.getList();
			sysDictEntity = (SysDictEntity) codeList.get(0);
			orgFactoryInfoEntity.setAreaCode(sysDictEntity.getCode());
			orgFactoryInfoEntity.setStatus(StatusEnum.USABLE.getKey());
			orgFactoryInfoEntity.setFactoryName(row[0]);
			orgFactoryInfoEntity.setCreditCode(row[1]);
			orgFactoryInfoEntity.setDeptId(deptId);
			orgFactoryInfoEntity.setCreateId(userId);
			//获取厂商编码序列号
			String FactoryCode = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.FACTORY_CODE.getKey());
			orgFactoryInfoEntity.setFactoryCode(FactoryCode);
			orgFactoryInfoEntity.setCreateTime(new Date());
			orgFactoryInfoEntity.setDelFlag(DelFlagEnum.NORMAL.getKey());
			insert = this.insert(orgFactoryInfoEntity);
			if (!insert){
				sb.append("第 " + i  + " 行数据错误!");
				sb.append("\n");
				failCount++;
				continue;
			}
			successCount++;
		}
		map.put(key, successCount.toString());
		map.put("failCount",failCount.toString());
		return map;
	}

	@Override
	public void updateByFactoryInfoApproval(OrgFactoryInfoApprovalEntity orgFactoryInfoApproval) {
		OrgFactoryInfoEntity orgFactoryInfoEntity = new OrgFactoryInfoEntity();
		orgFactoryInfoEntity = ReflectUitls.transform(orgFactoryInfoApproval, orgFactoryInfoEntity.getClass());
		this.baseMapper.updateById(orgFactoryInfoEntity);

	}

	@Override
	public void insertByFactoryInfoApproval(OrgFactoryInfoApprovalEntity orgFactoryInfoApproval) {
		OrgFactoryInfoEntity orgFactoryInfoEntity = new OrgFactoryInfoEntity();
		orgFactoryInfoEntity = ReflectUitls.transform(orgFactoryInfoApproval, orgFactoryInfoEntity.getClass());
		this.baseMapper.insert(orgFactoryInfoEntity);
	}

}
