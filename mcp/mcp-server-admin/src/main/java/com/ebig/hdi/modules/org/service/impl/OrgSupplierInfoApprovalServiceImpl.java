package com.ebig.hdi.modules.org.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.constant.ActivitiConstant;
import com.ebig.hdi.common.enums.*;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.Constant;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.activiti.entity.ActApprovalEntity;
import com.ebig.hdi.modules.activiti.service.ActApprovalService;
import com.ebig.hdi.modules.common.AddressUtil;
import com.ebig.hdi.modules.org.dao.OrgSupplierInfoApprovalDao;
import com.ebig.hdi.modules.org.entity.OrgSupplierInfoApprovalEntity;
import com.ebig.hdi.modules.org.entity.OrgSupplierInfoEntity;
import com.ebig.hdi.modules.org.param.OrgSupplierInfoParam;
import com.ebig.hdi.modules.org.service.OrgSupplierInfoApprovalService;
import com.ebig.hdi.modules.sys.entity.*;
import com.ebig.hdi.modules.sys.service.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service("orgSupplierInfoApprovalService")
public class OrgSupplierInfoApprovalServiceImpl extends ServiceImpl<OrgSupplierInfoApprovalDao, OrgSupplierInfoApprovalEntity> implements OrgSupplierInfoApprovalService {

    private static final Logger logger = LoggerFactory.getLogger(OrgSupplierInfoApprovalServiceImpl.class);

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
    private ActApprovalService actApprovalService;

    @Autowired
    private OrgSupplierInfoServiceImpl orgSupplierInfoService;

    @Autowired
    private SysDictService sysDictService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        OrgSupplierInfoApprovalEntity osie = new OrgSupplierInfoApprovalEntity();
        osie.setSupplierName((String) params.get("supplierName"));
        osie.setProvinceCode((String) params.get("provinceCode"));
        osie.setCityCode((String) params.get("cityCode"));
        osie.setAreaCode((String) params.get("areaCode"));
        osie.setStatus((Integer) params.get("status"));
        osie.setCheckStatus((Integer) params.get("checkStatus"));

        String sidx = (String) params.get("sidx");
        String order = (String) params.get("order");
        int currPage = Integer.parseInt(params.get("page").toString());
        int pageSize = Integer.parseInt(params.get("limit").toString());

        Page<OrgSupplierInfoApprovalEntity> page = new Page<OrgSupplierInfoApprovalEntity>(currPage, pageSize, order);
        if (sidx != null) {
            page.setAsc(sidx.equals("desc") ? false : true);
        } else {
            // 标志，设置默认按更新时间和创建时间排序
            osie.setIsDefaultOrder(1);
        }
        List<OrgSupplierInfoApprovalEntity> list = this.baseMapper.listForPage(page, osie);
        page.setRecords(list);
        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> save(OrgSupplierInfoApprovalEntity orgSupplierInfo, SysUserEntity entity) {
        List<OrgSupplierInfoApprovalEntity> supplierName = baseMapper.selectBySupplierName(orgSupplierInfo.getSupplierName());
        Map<String, Object> mapList = new HashMap<>(16);
        if (!StringUtil.isEmpty(supplierName)) {
            mapList.put("errmessage", "供应商名称已存在");
            logger.error("新增供应商:供应商{}名称已经存在", supplierName.get(0).getSupplierName());
            return mapList;
        }
        if (!(TypeEnum.USER_SUPPLIER.getKey().equals(entity.getUserType()) || TypeEnum.USER_PLATFORM.getKey().equals(entity.getUserType()))) {
            mapList.put("errmessage", "非平台用户或供应商用户无法操作该功能！");
            logger.error("新增供应商:用户的类型不符{}", entity.getUserType());
            return mapList;
        }
        List<OrgSupplierInfoApprovalEntity> creditCode = baseMapper.selectByCreditCode(orgSupplierInfo.getCreditCode());
        if (!StringUtil.isEmpty(creditCode)) {
            mapList.put("errmessage", "供应商信用代码已存在");
            logger.error("新增供应商:供应商信用代码{}已存在", creditCode);
            return mapList;
        }

        String supplierCode = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.SUPPLIER_CODE.getKey());
        orgSupplierInfo.setSupplierCode(supplierCode);
        orgSupplierInfo.setDelFlag(DelFlagEnum.NORMAL.getKey());
        orgSupplierInfo.setDataSource(DataSourceEnum.MANUAL.getKey());
        orgSupplierInfo.setCheckStatus(ApprovalTypeEnum.WAIT.getKey());
        orgSupplierInfo.setCreateTime(new Date());
        orgSupplierInfo.setCreateId(entity.getUserId());
        orgSupplierInfo.setParentId(0L);
        orgSupplierInfo.setChildNumber(0);
        orgSupplierInfo.setIsGroup(IsGroupEnum.NO.getKey());
        this.baseMapper.insert(orgSupplierInfo);

        //发起审批
        ActApprovalEntity approvalEntity = actApprovalService.getActApprovalEntity(entity, ChangeTypeEnum.ADD.getKey(),
                ActTypeEnum.SUPPLY.getKey(), orgSupplierInfo.getId().toString(),
                orgSupplierInfo.getSupplierCode(), orgSupplierInfo.getSupplierName());
        Map<String, Object> map = new HashMap<>(16);
        map.put("approvalEntity", approvalEntity);
        actApprovalService.insert(approvalEntity);
        ProcessInstance instance = actApprovalService.startProcess(entity.getUserId().toString(),
                ActivitiConstant.ACTIVITI_ORGSUPPLY[0], ActivitiConstant.ACTIVITI_ORGSUPPLY[1], map);
        if (instance != null) {
            approvalEntity.setProcessId(instance.getProcessInstanceId());
            approvalEntity.setApprovalCode(instance.getProcessInstanceId());
            orgSupplierInfo.setProcessId(instance.getProcessInstanceId());
        } else {
            mapList.put("errmessage", "发起审批流程失败,流程实例为空");
            logger.error("新增供应商:流程实例instance为空");
        }
        this.baseMapper.updateById(orgSupplierInfo);
        // 更新审批流
        actApprovalService.updateById(approvalEntity);
        return mapList;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(OrgSupplierInfoApprovalEntity orgSupplierInfo, SysUserEntity entity) {
        List<OrgSupplierInfoApprovalEntity> supplierName = this.baseMapper.selectBySupplierName(orgSupplierInfo.getSupplierName());
        if (!StringUtil.isEmpty(supplierName) && !supplierName.get(0).getId().equals(orgSupplierInfo.getId())) {
            throw new HdiException("供应商名称已存在");
        }
        List<OrgSupplierInfoApprovalEntity> creditCode = this.baseMapper.selectByCreditCode(orgSupplierInfo.getCreditCode());
        if (!StringUtil.isEmpty(creditCode) && !creditCode.get(0).getId().equals(orgSupplierInfo.getId())) {
            throw new HdiException("供应商信用代码已存在");
        }

        //发起审批
        ActApprovalEntity approvalEntity = actApprovalService.getActApprovalEntity(entity, ChangeTypeEnum.UPDATE.getKey(),
                ActTypeEnum.SUPPLY.getKey(), orgSupplierInfo.getId().toString(),
                orgSupplierInfo.getSupplierCode(), orgSupplierInfo.getSupplierName());
        Map<String, Object> map = new HashMap<>(16);
        map.put("approvalEntity", approvalEntity);
        actApprovalService.insert(approvalEntity);
        ProcessInstance instance = actApprovalService.startProcess(entity.getUserId().toString(),
                ActivitiConstant.ACTIVITI_ORGSUPPLY[0], ActivitiConstant.ACTIVITI_ORGSUPPLY[1], map);
        if (instance != null) {
            approvalEntity.setProcessId(instance.getProcessInstanceId());
            approvalEntity.setApprovalCode(instance.getProcessInstanceId());
            orgSupplierInfo.setProcessId(instance.getProcessInstanceId());
        }
        // 更新供应商信息
        orgSupplierInfo.setEditTime(new Date());
        orgSupplierInfo.setEditId(entity.getUserId());
        orgSupplierInfo.setCheckStatus(ApprovalTypeEnum.WAIT.getKey());
        this.updateById(orgSupplierInfo);
        actApprovalService.updateById(approvalEntity);
    }

    @Override
    public void bingding(OrgSupplierInfoApprovalEntity orgSupplierInfo) {
        List<Long> ids = new ArrayList<>();
        selectSubIds(orgSupplierInfo.getId(), ids);
        ids.add(orgSupplierInfo.getId());
        if (ids.contains(orgSupplierInfo.getParentId())) {
            throw new HdiException("不能选择子机构或自身作为上层父级！");
        }
        OrgSupplierInfoApprovalEntity currentSupplier = this.selectById(orgSupplierInfo.getId());
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
            OrgSupplierInfoApprovalEntity parentSupplier = this.selectById(orgSupplierInfo.getParentId());
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
            OrgSupplierInfoApprovalEntity parentSupplier = this.selectById(currentSupplier.getParentId());
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
    public boolean judgeChildNumber(OrgSupplierInfoApprovalEntity orgSupplierInfo) {
        OrgSupplierInfoApprovalEntity parentSupplier = getParentSupplier(orgSupplierInfo);
        List<OrgSupplierInfoApprovalEntity> childSupplierList = new ArrayList<>();
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
    public void findAllChildSupplier(OrgSupplierInfoApprovalEntity entity, List<OrgSupplierInfoApprovalEntity> supplierList,
                                     Integer status) {
        List<OrgSupplierInfoApprovalEntity> childSupplierList = baseMapper.selectByParentIdAndStatus(entity.getId(), status);
        if (childSupplierList != null) {
            if (childSupplierList.size() == 0) {
                entity.setOpen(false);
                entity.setValue(String.valueOf(entity.getParentId()));
            }
            for (OrgSupplierInfoApprovalEntity childSupplier : childSupplierList) {
                if (supplierList != null) {
                    supplierList.add(childSupplier);
                }
                List<OrgSupplierInfoApprovalEntity> list = entity.getList();
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
    public OrgSupplierInfoApprovalEntity getParentSupplier(OrgSupplierInfoApprovalEntity entity) {
        if (entity.getParentId() == 0) {
            return entity;
        } else {
            OrgSupplierInfoApprovalEntity parentSupplier = null;
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
    public List<OrgSupplierInfoApprovalEntity> queryTree(Map<String, Object> params) {
        // 先查询符合过滤条件的所有父供应商
        List<OrgSupplierInfoApprovalEntity> parentSupplierList = baseMapper.selectParent(
                params.get(Constant.SQL_FILTER) != null ? params.get(Constant.SQL_FILTER).toString() : null);
        // 递归查询子供应商
        if (!StringUtil.isEmpty(parentSupplierList)) {
            for (OrgSupplierInfoApprovalEntity parentSupplier : parentSupplierList) {
                findAllChildSupplier(parentSupplier, null, StatusEnum.USABLE.getKey());
            }
        }
        // 树根节点
        OrgSupplierInfoApprovalEntity rootSupplier = new OrgSupplierInfoApprovalEntity();
        rootSupplier.setId(0L);
        rootSupplier.setSupplierName("root");
        rootSupplier.setList(parentSupplierList);
        rootSupplier.setValue("0");
        List<OrgSupplierInfoApprovalEntity> treeSupplierList = new ArrayList<OrgSupplierInfoApprovalEntity>();
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

        List<OrgSupplierInfoApprovalEntity> list = this
                .selectList(new EntityWrapper<OrgSupplierInfoApprovalEntity>().eq("dept_id", sysUserEntity.getDeptId()));
        if (StringUtil.isEmpty(list)) {
            throw new HdiException("该用户所属机构为非供应商！");
        }
        return list.get(0).getId();
    }

    @Override
    @DataFilter(subDept = true, user = false)
    public List<OrgSupplierInfoApprovalEntity> queryBySupplierName(Map<String, Object> params) {
        String supplierName = (String) params.get("supplierName");
        return this.selectList(new EntityWrapper<OrgSupplierInfoApprovalEntity>()
                .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER))
                .like("supplier_name", supplierName).eq("status", StatusEnum.USABLE.getKey())
                .orderBy("create_time", false));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatchIds(List<Long> ids) {
        if (ids != null) {
            for (Long id : ids) {
                OrgSupplierInfoApprovalEntity supplier = baseMapper.selectById(id);
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
    public List<OrgSupplierInfoApprovalEntity> selectByDeptId(Long deptId) {
        return baseMapper.selectByDeptId(deptId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> checkStatus(Map<String, Object> params, SysUserEntity entity) {
        Integer checkStatus = (Integer) params.get("checkStatus");
        Map<String, Object> map = new HashMap<>(16);
        if (StringUtil.isEmpty(checkStatus)) {
            map.put("errorMessage", "状态为空");
            return map;
        }
        Integer orgId = (Integer) params.get("orgId");
        if (StringUtil.isEmpty(orgId)) {
            map.put("errorMessage", "传入ID为空");
            return map;
        }
        OrgSupplierInfoApprovalEntity orgSupplierInfoApprovalEntity = this.baseMapper.selectById(orgId);
        orgSupplierInfoApprovalEntity.setEditTime(new Date());
        orgSupplierInfoApprovalEntity.setEditId(entity.getUserId());
        if (checkStatus.equals(ApprovalTypeEnum.PASS.getKey())) {
            //审核通过操作供应商信息表
            orgSupplierInfoApprovalEntity.setCheckStatus(ApprovalTypeEnum.PASS.getKey());
            this.updateById(orgSupplierInfoApprovalEntity);
            //查询供应商信息表有无数据
            OrgSupplierInfoEntity orgSupplierInfoEntity = this.baseMapper.selectBySupplierCode(orgSupplierInfoApprovalEntity.getSupplierCode());
            //如果供应商信息表没数据,则新增
            if (StringUtil.isEmpty(orgSupplierInfoEntity)) {
                // 创建系统机构信息
                SysDeptEntity sysDept = new SysDeptEntity();
                sysDept.setParentId(0L);
                sysDept.setName(orgSupplierInfoApprovalEntity.getSupplierName());
                sysDept.setDelFlag(DelFlagEnum.NORMAL.getKey());
                sysDeptService.insert(sysDept);
                orgSupplierInfoApprovalEntity.setDeptId(sysDept.getDeptId());
                this.baseMapper.updateById(orgSupplierInfoApprovalEntity);
                orgSupplierInfoService.insertByOrgSupplierInfoApproval(orgSupplierInfoApprovalEntity);

                // 创建供应商角色信息
                SysRoleEntity sysRole = new SysRoleEntity();
                sysRole.setRoleName(orgSupplierInfoApprovalEntity.getSupplierName() + "管理员");
                sysRole.setRemark(orgSupplierInfoApprovalEntity.getSupplierName() + "管理员");
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
                sysUser.setUsername(String.valueOf(orgSupplierInfoApprovalEntity.getSupplierCode()));
                SysConfigEntity initialPassword = sysConfigService.selectByParamKey(SysConfigEnum.INITIAL_PASSWORD.getKey());
                sysUser.setPassword(initialPassword.getParamValue());
                if (!StringUtil.isEmpty(orgSupplierInfoApprovalEntity.getPhone())) {
                    sysUser.setMobile(orgSupplierInfoApprovalEntity.getPhone());
                }
                if (!StringUtil.isEmpty(orgSupplierInfoApprovalEntity.getEmail())) {
                    sysUser.setEmail(orgSupplierInfoApprovalEntity.getEmail());
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
            } else {
                //如果供应商信息表有数据，则更新
                orgSupplierInfoService.updateByOrgSupplierInfoApproval(orgSupplierInfoApprovalEntity);
                //更新系统机构信息
                SysDeptEntity sysDept = sysDeptService.selectById(orgSupplierInfoApprovalEntity.getDeptId());
                sysDept.setName(orgSupplierInfoApprovalEntity.getSupplierName());
                sysDeptService.updateById(sysDept);
            }
        }
        if (checkStatus.equals(ApprovalTypeEnum.FAIL.getKey())) {
            //审核不通过操作供应商待审核表
            orgSupplierInfoApprovalEntity.setCheckStatus(ApprovalTypeEnum.FAIL.getKey());
            this.baseMapper.updateById(orgSupplierInfoApprovalEntity);
        }
        return map;
    }

    private void selectSubIds(Long id, List<Long> ids) {
        List<Long> subIds = this.baseMapper.selectSubIdsById(id);

        for (Long subId : subIds) {
            ids.add(subId);
            selectSubIds(subId, ids);
        }
    }

    @Override
    public Map importData(String[][] rows, SysUserEntity sysUserEntity) {
        ArrayList<OrgSupplierInfoApprovalEntity> orgSupplierList = new ArrayList(rows.length);
        SysDictEntity sysDictEntity = new SysDictEntity();
        List<?> codeList;
        Map<String, Object> saveMap;
        Map<String, Object> dictMap = new HashMap<>(4);
        dictMap.put("page", "1");
        dictMap.put("limit", "1");
        //保存返回信息
        StringBuilder sb = new StringBuilder();
        String key = "successCount";
        Map<String, Object> map = new HashMap<>(16);
        PageUtils page;
        if (!(TypeEnum.USER_SUPPLIER.getKey().equals(sysUserEntity.getUserType()) || TypeEnum.USER_PLATFORM.getKey().equals(sysUserEntity.getUserType()))) {
            map.put("errmessage", "非平台用户或供应商用户无法操作该功能！");
            return map;
        }
        if (sysUserEntity == null || rows.length <= 1) {
            map.put(key, "0");
            map.put("failCount", String.valueOf(rows.length - 1));
            sb.append("系统错误");
            map.put("errorMessage", sb.toString());
            return map;
        }
        Integer failCount = 0;
        Integer successCount = 0;
        for (int i = 1; i < rows.length; i++) {
            OrgSupplierInfoApprovalEntity orgSupplierInfoEntity = new OrgSupplierInfoApprovalEntity();

            String[] row = rows[i];
            if (StringUtil.isEmpty(row[0]) || StringUtil.isEmpty(row[1]) || StringUtil.isEmpty(row[2])) {
                sb.append("\n");
                sb.append("第 " + i + " 行数据错误，请检查数据是否正确");
                failCount++;
                continue;
            }
            List<OrgSupplierInfoApprovalEntity> supplierName = baseMapper.selectBySupplierName(row[0]);
            if (!StringUtil.isEmpty(supplierName)) {
                sb.append("\n");

                sb.append("第 " + i +"新增供应商:供应商名称已经存在:"+ supplierName.get(0).getSupplierName());
                failCount++;
                continue;
            }
            List<OrgSupplierInfoApprovalEntity> creditCode = baseMapper.selectByCreditCode(row[1]);
            if (!StringUtil.isEmpty(creditCode)) {
                sb.append("\n");
                sb.append("新增供应商:供应商信用代码已存在 :" +row[1]);
                failCount++;
                continue;
            }
            String str = row[2];
            String[] strs = str.split("/");
            if (strs.length != 3) {
                sb.append("\n");
                sb.append("第 " + i + " 行数据错误，输入区域不正确 区域："+str);
                failCount++;
                continue;
            }
            if (!AddressUtil.getProvinceValueMap().containsKey(strs[0])) {
                sb.append("\n");
                sb.append("第 " + i + " 行数据错误，输入省份或者直辖市不存在 区域："+str);
                failCount++;
                continue;
            }
            orgSupplierInfoEntity.setProvinceCode(AddressUtil.getProvinceValueMap().get(strs[0]));
            sysDictEntity = sysDictService.selectDictByCodeAndParentCode(strs[1], AddressUtil.getProvinceValueMap().get(strs[0]));
            if (StringUtil.isEmpty(sysDictEntity)) {
                sb.append("\n");
                sb.append("第 " + i + " 行数据错误，输入城市不存在 区域："+str);
                failCount++;
                continue;
            }
            orgSupplierInfoEntity.setCityCode(sysDictEntity.getCode());
            sysDictEntity = sysDictService.selectDictByCodeAndParentCode(strs[2], sysDictEntity.getCode());
            if (StringUtil.isEmpty(sysDictEntity)) {
                sb.append("第 " + i + " 行数据错误，区不存在 区域："+str);
                sb.append("\n");
                failCount++;
                continue;
            }
            orgSupplierInfoEntity.setAreaCode(sysDictEntity.getCode());
            if (!StringUtil.isEmpty(row[3])) {
                dictMap.put("value", row[3]);
                page = sysDictService.queryPage(dictMap);
                if (page.getList().size() == 0) {
                    sb.append("\n");
                    sb.append("第 " + i + " 行数据错误，输机构性质不存在 : "+row[3]);
                    failCount++;
                    continue;
                }
                codeList = page.getList();
                sysDictEntity = (SysDictEntity) codeList.get(0);
                orgSupplierInfoEntity.setSupplierNature(Integer.valueOf(sysDictEntity.getCode()));
            }
            orgSupplierInfoEntity.setCreateId(sysUserEntity.getUserId());
            orgSupplierInfoEntity.setSupplierName(row[0]);
            orgSupplierInfoEntity.setCreditCode(row[1]);
            orgSupplierInfoEntity.setCorporate(row[4]);
            orgSupplierInfoEntity.setSupplierAddress(row[5]);
            orgSupplierInfoEntity.setPhone(row[6]);
            orgSupplierInfoEntity.setEmail(row[7]);
            orgSupplierInfoEntity.setFax(row[8]);
            orgSupplierInfoEntity.setStatus(StatusEnum.USABLE.getKey());
            orgSupplierList.add(orgSupplierInfoEntity);
            orgSupplierInfoEntity = null;
            successCount++;
        }
        if(failCount >0){
            successCount = 0;
            map.put("errorMessage",sb.toString());
            map.put(key, successCount.toString());
            map.put("failCount",failCount.toString());
            return map;
        }
        orgSupplierList.stream().forEach(orgSupplier -> {
            save(orgSupplier,sysUserEntity);
        });
        map.put("errorMessage",sb.toString());
        map.put(key, successCount.toString());
        map.put("failCount",failCount.toString());
        return map;
    }

    @Override
    public List<Map<String, Object>> getList(String[] columns, OrgSupplierInfoParam orgSupplierInfoParam) {
        SysUserEntity sysUserEntity = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (!TypeEnum.USER_PLATFORM.getKey().equals(sysUserEntity.getUserType())){
            throw new HdiException("非平台用户无法操作该功能！");
        }
        List<Map<String, Object>> list = this.baseMapper.getList(columns, orgSupplierInfoParam);
        int size = list.size();

        String checkStatus;
        StringBuffer address = new StringBuffer();
        for (int i = 0; i < size; i++) {
            checkStatus = ApprovalTypeEnum.getName(Integer.valueOf(list.get(i).get("checkStatus").toString()));
            address.append(AddressUtil.getProvinceMap().get(list.get(i).get("provinceCode"))).append("/")
                    .append(AddressUtil.getCityMap().get(list.get(i).get("cityCode"))).append("/")
                    .append(AddressUtil.getAreaMap().get(list.get(i).get("areaCode")));
            list.get(i).put("address", address.toString());
            list.get(i).put("checkStatus", checkStatus);
            address.setLength(0);
        }
        return list;
    }

    @Override
    public void toggle(Map<String, Object> params) {
        List<Long> ids = JSON.parseArray(params.get("ids").toString(), Long.class);
        for (Long id : ids) {
            OrgSupplierInfoApprovalEntity orgSupplierInfoApprovalEntity = new OrgSupplierInfoApprovalEntity();
            orgSupplierInfoApprovalEntity.setId(id);
            orgSupplierInfoApprovalEntity.setStatus(Integer.valueOf(params.get("status").toString()));
            //查看原表是否存在数据
            Integer existCode = this.baseMapper.selectIfExist(orgSupplierInfoApprovalEntity.getId());
            if (existCode.equals(1)){
                OrgSupplierInfoEntity orgSupplierInfoEntity = new OrgSupplierInfoEntity();
                orgSupplierInfoEntity.setId(id);
                orgSupplierInfoEntity.setStatus(Integer.valueOf(params.get("status").toString()));
                orgSupplierInfoService.updateById(orgSupplierInfoEntity);
            }
            this.baseMapper.updateById(orgSupplierInfoApprovalEntity);
        }
    }

}
