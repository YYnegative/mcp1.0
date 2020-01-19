package com.ebig.hdi.modules.org.service.impl;

import java.util.*;

import com.alibaba.fastjson.JSON;
import com.ebig.hdi.common.utils.ReflectUitls;
import com.ebig.hdi.modules.org.entity.OrgSupplierInfoApprovalEntity;
import com.ebig.hdi.modules.org.param.OrgSupplierInfoParam;
import com.ebig.hdi.modules.sys.entity.*;
import com.ebig.hdi.modules.sys.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.enums.DelFlagEnum;
import com.ebig.hdi.common.enums.IsGroupEnum;
import com.ebig.hdi.common.enums.SequenceEnum;
import com.ebig.hdi.common.enums.StatusEnum;
import com.ebig.hdi.common.enums.SysConfigEnum;
import com.ebig.hdi.common.enums.TypeEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.Constant;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.org.dao.OrgSupplierInfoDao;
import com.ebig.hdi.modules.org.entity.OrgSupplierInfoEntity;
import com.ebig.hdi.modules.org.service.OrgSupplierInfoService;

@Service("orgSupplierInfoService")
public class OrgSupplierInfoServiceImpl extends ServiceImpl<OrgSupplierInfoDao, OrgSupplierInfoEntity>
		implements OrgSupplierInfoService {

	@Autowired
	private SysSequenceService sysSequenceService;

	@Autowired
	private SysDeptService sysDeptService;

	@Autowired
	private SysRoleService sysRoleService;

	@Autowired
	private SysConfigService sysConfigService;

	@Autowired
	private SysRoleMenuService sysRoleMenuService;

	@Autowired
	private SysRoleDeptService sysRoleDeptService;

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private SysUserRoleService sysUserRoleService;

	@Autowired
	private SysDictService sysDictService;

	@Override
	@DataFilter(subDept = true, user = false, tableAlias = "t1")
	public PageUtils queryPage(Map<String, Object> params) {
		OrgSupplierInfoEntity osie = new OrgSupplierInfoEntity();
		osie.setSupplierName((String) params.get("supplierName"));
		osie.setProvinceCode((String) params.get("provinceCode"));
		osie.setCityCode((String) params.get("cityCode"));
		osie.setAreaCode((String) params.get("areaCode"));
		osie.setFileterDept((String) params.get(Constant.SQL_FILTER));
		osie.setStatus((Integer) params.get("status"));

		String sidx = (String) params.get("sidx");
		String order = (String) params.get("order");
		int currPage = Integer.parseInt(params.get("page").toString());
		int pageSize = Integer.parseInt(params.get("limit").toString());

		Page<OrgSupplierInfoEntity> page = new Page<OrgSupplierInfoEntity>(currPage, pageSize, order);
		if (sidx != null) {
			page.setAsc(sidx.equals("desc") ? false : true);
		} else {
			// 标志，设置默认按更新时间和创建时间排序
			osie.setIsDefaultOrder(1);
		}

		List<OrgSupplierInfoEntity> list = this.baseMapper.listForPage(page, osie);

		page.setRecords(list);
		return new PageUtils(page);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(OrgSupplierInfoEntity orgSupplierInfo) {
		List<OrgSupplierInfoEntity> supplierName = baseMapper.selectBySupplierName(orgSupplierInfo.getSupplierName());
		if (!StringUtil.isEmpty(supplierName)) {
			throw new HdiException("供应商名称已存在");
		}
		List<OrgSupplierInfoEntity> creditCode = baseMapper.selectByCreditCode(orgSupplierInfo.getCreditCode());
		if (!StringUtil.isEmpty(creditCode)) {
			throw new HdiException("供应商信用代码已存在");
		}
		// 创建系统机构信息
		SysDeptEntity sysDept = new SysDeptEntity();
		sysDept.setParentId(0L);
		sysDept.setName(orgSupplierInfo.getSupplierName());
		sysDept.setDelFlag(DelFlagEnum.NORMAL.getKey());
		sysDeptService.insert(sysDept);

		orgSupplierInfo.setParentId(0L);
		orgSupplierInfo.setChildNumber(0);
		// 获取供应商编码序列号
		String supplierCode = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.SUPPLIER_CODE.getKey());
		orgSupplierInfo.setSupplierCode(supplierCode);
		orgSupplierInfo.setIsGroup(IsGroupEnum.NO.getKey());
		orgSupplierInfo.setDeptId(sysDept.getDeptId());
		orgSupplierInfo.setCreateTime(new Date());
		this.insert(orgSupplierInfo);
		// 创建供应商角色信息
		SysRoleEntity sysRole = new SysRoleEntity();
		sysRole.setRoleName(orgSupplierInfo.getSupplierName() + "管理员");
		sysRole.setRemark(orgSupplierInfo.getSupplierName() + "管理员");
		sysRole.setDeptId(sysDept.getDeptId());
		sysRole.setCreateTime(new Date());
		sysRoleService.insert(sysRole);
		// 分配供应商菜单、数据权限
		SysConfigEntity supplierRoleMenu = sysConfigService.selectByParamKey(SysConfigEnum.SUPPLIER_ROLE_MENU.getKey());
		List<SysRoleMenuEntity> sysRoleMenuList = sysRoleMenuService
				.selectListByRoleId(supplierRoleMenu.getParamValue());
		List<SysRoleMenuEntity> rmList = new ArrayList<SysRoleMenuEntity>();
		for (SysRoleMenuEntity roleMenu : sysRoleMenuList) {
			SysRoleMenuEntity rm = new SysRoleMenuEntity();
			rm.setRoleId(sysRole.getRoleId());
			rm.setMenuId(roleMenu.getMenuId());
			rmList.add(rm);
		}
		sysRoleMenuService.insertBatch(rmList);
		SysRoleDeptEntity sysRoleDept = new SysRoleDeptEntity();
		sysRoleDept.setDeptId(sysDept.getDeptId());
		sysRoleDept.setRoleId(sysRole.getRoleId());
		sysRoleDeptService.insert(sysRoleDept);
		// 创建供应商管理员
		SysUserEntity sysUser = new SysUserEntity();
		sysUser.setUsername(String.valueOf(supplierCode));
		SysConfigEntity initialPassword = sysConfigService.selectByParamKey(SysConfigEnum.INITIAL_PASSWORD.getKey());
		sysUser.setPassword(initialPassword.getParamValue());
		if (!StringUtil.isEmpty(orgSupplierInfo.getPhone())) {
			sysUser.setMobile(orgSupplierInfo.getPhone());
		}
		if (!StringUtil.isEmpty(orgSupplierInfo.getEmail())) {
			sysUser.setEmail(orgSupplierInfo.getEmail());
		}
		sysUser.setStatus(StatusEnum.USABLE.getKey());
		sysUser.setDeptId(sysDept.getDeptId());
		sysUser.setUserType(TypeEnum.USER_SUPPLIER.getKey());
		sysUserService.save(sysUser);
		// 保存用户角色关系
		SysUserRoleEntity sysUserRole = new SysUserRoleEntity();
		sysUserRole.setUserId(sysUser.getUserId());
		sysUserRole.setRoleId(sysRole.getRoleId());
		sysUserRoleService.insert(sysUserRole);
	}

	@Override
	public void update(OrgSupplierInfoEntity orgSupplierInfo) {
		List<OrgSupplierInfoEntity> supplierName = baseMapper.selectBySupplierName(orgSupplierInfo.getSupplierName());
		if (!StringUtil.isEmpty(supplierName) && !supplierName.get(0).getId().equals(orgSupplierInfo.getId())) {
			throw new HdiException("供应商名称已存在");
		}
		List<OrgSupplierInfoEntity> creditCode = baseMapper.selectByCreditCode(orgSupplierInfo.getCreditCode());
		if (!StringUtil.isEmpty(creditCode) && !creditCode.get(0).getId().equals(orgSupplierInfo.getId())) {
			throw new HdiException("供应商信用代码已存在");
		}
		// 更新供应商信息
		orgSupplierInfo.setEditTime(new Date());
		this.updateById(orgSupplierInfo);
	}

	@Override
	public void bingding(OrgSupplierInfoEntity orgSupplierInfo) {
		List<Long> ids = new ArrayList<>();
		selectSubIds(orgSupplierInfo.getId(), ids);
		ids.add(orgSupplierInfo.getId());
		if(ids.contains(orgSupplierInfo.getParentId())) {
			throw new HdiException("不能选择子机构或自身作为上层父级！");
		}
		OrgSupplierInfoEntity currentSupplier = this.selectById(orgSupplierInfo.getId());
		SysConfigEntity groupRoleMenuConfigEntity = sysConfigService
				.selectByParamKey(SysConfigEnum.GROUP_ROLE_MENU.getKey());
		if (groupRoleMenuConfigEntity == null) {
			throw new HdiException("没有设置集团管理的角色");
		}
		Long groupRoleId = Long.valueOf(groupRoleMenuConfigEntity.getParamValue());
		List<Long> childRoleIdList = sysRoleDeptService.queryRoleIdList(currentSupplier.getDeptId());
		// 判断是否集团，同时修改是否集团标志和集团管理角色
		if (orgSupplierInfo.getParentId() == 0) {
			if (orgSupplierInfo.getChildNumber() == null) {
				throw new HdiException("供应商的子机构数不能为空");
			}
			orgSupplierInfo.setIsGroup(IsGroupEnum.YES.getKey());
			// 先判断供应商是否具有集团管理角色，如果没有则赋予供应商集团管理角色
			if (!childRoleIdList.contains(groupRoleId)) {
				SysRoleDeptEntity entity = new SysRoleDeptEntity();
				entity.setDeptId(currentSupplier.getDeptId());
				entity.setRoleId(groupRoleId);
				sysRoleDeptService.insert(entity);
			}
		} else {
			orgSupplierInfo.setChildNumber(0);
			orgSupplierInfo.setIsGroup(IsGroupEnum.NO.getKey());
			// 先判断供应商是否具有集团管理角色，如果有则删除与供应商集团管理角色相关的数据
			if (childRoleIdList.contains(groupRoleId)) {
				sysRoleDeptService.deleteByRoleIdAndDeptId(groupRoleId, currentSupplier.getDeptId());
			}
		}
		SysDeptEntity childDept = sysDeptService.selectById(currentSupplier.getDeptId());
		if (orgSupplierInfo.getParentId() != 0) {
			// 绑定 如果不是顶级供应商，则修改部门表对应的父id和把子供应商的机构给父供应商的所有角色
			// 修改当前供应商的部门对应的父部门id
			OrgSupplierInfoEntity parentSupplier = this.selectById(orgSupplierInfo.getParentId());
			childDept.setParentId(parentSupplier.getDeptId());
			sysDeptService.updateById(childDept);
			// 把当前供应商的机构（deptId）给父供应商的所有角色（sys_role_dept）
			List<Long> parentRoleIdList = sysRoleDeptService.queryRoleIdList(parentSupplier.getDeptId());
			for (Long parentRoleId : parentRoleIdList) {
				List<SysRoleDeptEntity> roleDeptEntityList = sysRoleDeptService.selectByRoleIdAndDeptId(parentRoleId,
						currentSupplier.getDeptId());
				if (roleDeptEntityList == null || roleDeptEntityList.size() == 0) {
					SysRoleDeptEntity entity = new SysRoleDeptEntity();
					entity.setDeptId(currentSupplier.getDeptId());
					entity.setRoleId(parentRoleId);
					sysRoleDeptService.insert(entity);
				}
			}
		} else if (currentSupplier.getParentId() != 0) {
			// 解绑 修改供应商机构的父机构为0 删除供应商机构与父供应商所有角色相关的角色机构中间表数据 给集团管理模块的权限
			OrgSupplierInfoEntity parentSupplier = this.selectById(currentSupplier.getParentId());
			List<Long> parentRoleIdList = sysRoleDeptService.queryRoleIdList(parentSupplier.getDeptId());
			childDept.setParentId(0L);
			sysDeptService.updateById(childDept);
			for (Long parentRoleId : parentRoleIdList) {
				sysRoleDeptService.deleteByRoleIdAndDeptId(parentRoleId, currentSupplier.getDeptId());
			}
		}
		orgSupplierInfo.setEditTime(new Date());
		this.updateById(orgSupplierInfo);
	}

	@Override
	public boolean judgeChildNumber(OrgSupplierInfoEntity orgSupplierInfo) {
		OrgSupplierInfoEntity parentSupplier = getParentSupplier(orgSupplierInfo);
		List<OrgSupplierInfoEntity> childSupplierList = new ArrayList<>();
		findAllChildSupplier(parentSupplier, childSupplierList, null);
		int childNumber = parentSupplier.getChildNumber() != null ? parentSupplier.getChildNumber() : 0;
		if (childNumber <= childSupplierList.size()) {
			return true;
		}
		return false;
	}

	/**
	 * 递归查询所有的子供应商,返回子供应商的数量
	 * 
	 * @param entity
	 * @param supplierList
	 * @return
	 */
	public void findAllChildSupplier(OrgSupplierInfoEntity entity, List<OrgSupplierInfoEntity> supplierList,
			Integer status) {
		List<OrgSupplierInfoEntity> childSupplierList = baseMapper.selectByParentIdAndStatus(entity.getId(), status);
		if (childSupplierList != null) {
			if (childSupplierList.size() == 0) {
				entity.setOpen(false);
				entity.setValue(String.valueOf(entity.getParentId()));
			}
			for (OrgSupplierInfoEntity childSupplier : childSupplierList) {
				if (supplierList != null) {
					supplierList.add(childSupplier);
				}
				List<OrgSupplierInfoEntity> list = entity.getList();
				if (list == null) {
					list = new ArrayList<>();
				}
				list.add(childSupplier);
				entity.setList(list);
				entity.setOpen(true);
				entity.setValue(String.valueOf(entity.getParentId()));
				findAllChildSupplier(childSupplier, supplierList, null);
			}
		}
	}

	/**
	 * 查询父供应商
	 * 
	 * @param entity
	 * @return
	 */
	public OrgSupplierInfoEntity getParentSupplier(OrgSupplierInfoEntity entity) {
		if (entity.getParentId() == 0) {
			return entity;
		} else {
			OrgSupplierInfoEntity parentSupplier = null;
			Long parentId = entity.getParentId();
			while (true) {
				parentSupplier = this.selectById(parentId);
				if (parentSupplier.getParentId() == 0) {
					return parentSupplier;
				} else {
					parentId = parentSupplier.getParentId();
				}
			}
		}
	}

	/**
	 * 通过部门id来查询出所有的权限（Menu）
	 * 
	 * @param deptId
	 * @return
	 */
	public List<Long> getMenuListByDeptId(Long deptId) {
		SysDeptEntity dept = sysDeptService.selectById(deptId);
		List<Long> roleIdList = sysRoleDeptService.queryRoleIdList(dept.getDeptId());
		Long[] roleIdArr = new Long[roleIdList.size()];
		roleIdList.toArray(roleIdArr);
		return sysRoleMenuService.queryMenuIdList(roleIdArr);
	}

	@Override
	@DataFilter(subDept = true, user = false)
	public List<OrgSupplierInfoEntity> queryTree(Map<String, Object> params) {
		// 先查询符合过滤条件的所有父供应商
		List<OrgSupplierInfoEntity> parentSupplierList = baseMapper.selectParent(
				params.get(Constant.SQL_FILTER) != null ? params.get(Constant.SQL_FILTER).toString() : null);
		// 递归查询子供应商
		if (!StringUtil.isEmpty(parentSupplierList)) {
			for (OrgSupplierInfoEntity parentSupplier : parentSupplierList) {
				findAllChildSupplier(parentSupplier, null, StatusEnum.USABLE.getKey());
			}
		}
		// 树根节点
		OrgSupplierInfoEntity rootSupplier = new OrgSupplierInfoEntity();
		rootSupplier.setId(0L);
		rootSupplier.setSupplierName("root");
		rootSupplier.setList(parentSupplierList);
		rootSupplier.setValue("0");
		List<OrgSupplierInfoEntity> treeSupplierList = new ArrayList<OrgSupplierInfoEntity>();
		treeSupplierList.add(rootSupplier);
		return treeSupplierList;
	}

	@Override
	public Long selectIdByUserId(Long userId) {
		SysUserEntity sysUserEntity = sysUserService.selectById(userId);

		if (StringUtil.isEmpty(sysUserEntity)) {
			throw new HdiException("用户不存在！");
		}

		if (!TypeEnum.USER_SUPPLIER.getKey().equals(sysUserEntity.getUserType())) {
			throw new HdiException("非供应商用户无法操作该功能！");
		}

		List<OrgSupplierInfoEntity> list = this
				.selectList(new EntityWrapper<OrgSupplierInfoEntity>().eq("dept_id", sysUserEntity.getDeptId()));
		if (StringUtil.isEmpty(list)) {
			throw new HdiException("该用户所属机构为非供应商！");
		}
		return list.get(0).getId();
	}

	@Override
	@DataFilter(subDept = true, user = false)
	public List<OrgSupplierInfoEntity> queryBySupplierName(Map<String, Object> params) {
		String supplierName = (String) params.get("supplierName");
		return this.selectList(new EntityWrapper<OrgSupplierInfoEntity>()
				.addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER))
				.like("supplier_name", supplierName).eq("status", StatusEnum.USABLE.getKey())
				.orderBy("create_time", false));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteBatchIds(List<Long> ids) {
		if (ids != null) {
			for (Long id : ids) {
				OrgSupplierInfoEntity supplier = baseMapper.selectById(id);
				// 通过供应商编码查询出用户信息
				SysUserEntity user = sysUserService.queryByUserName(supplier.getSupplierCode());
				// 用户不删除，只修改状态为禁用
				user.setStatus(StatusEnum.DISABLE.getKey());
				sysUserService.update(user);
				// 逻辑删除供应商
				baseMapper.deleteById(supplier);
			}
		}

	}

	@Override
	public String selectSourceSupplierId(Long supplierId, Long hospitalId) {

		return baseMapper.selectSourceSupplierId(supplierId, hospitalId);
	}

	@Override
	public List<OrgSupplierInfoEntity> selectByDeptId(Long deptId) {
		return baseMapper.selectByDeptId(deptId);
	}

	private void selectSubIds(Long id, List<Long> ids) {
		List<Long> subIds = this.baseMapper.selectSubIdsById(id);
		
		for (Long subId : subIds) {
			ids.add(subId);
			selectSubIds(subId, ids);
		}
	}

	public void insertByOrgSupplierInfoApproval(OrgSupplierInfoApprovalEntity orgSupplierInfoApprovalEntity) {
		OrgSupplierInfoEntity orgSupplierInfoEntity = new OrgSupplierInfoEntity();
		orgSupplierInfoEntity = ReflectUitls.transform(orgSupplierInfoApprovalEntity, orgSupplierInfoEntity.getClass());
		this.baseMapper.insert(orgSupplierInfoEntity);
	}

	public void updateByOrgSupplierInfoApproval(OrgSupplierInfoApprovalEntity orgSupplierInfoApprovalEntity) {
		OrgSupplierInfoEntity orgSupplierInfoEntity = new OrgSupplierInfoEntity();
		orgSupplierInfoEntity = ReflectUitls.transform(orgSupplierInfoApprovalEntity, orgSupplierInfoEntity.getClass());
		this.baseMapper.updateById(orgSupplierInfoEntity);
	}
}
