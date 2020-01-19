package com.ebig.hdi.modules.consumables.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.constant.ActivitiConstant;
import com.ebig.hdi.common.constant.PageConstant;
import com.ebig.hdi.common.entity.UnicodeGoodsShipApprovalEntity;
import com.ebig.hdi.common.entity.UnicodeGoodsShipEntity;
import com.ebig.hdi.common.entity.UnicodeGoodsShipHistEntity;
import com.ebig.hdi.common.enums.*;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.ReflectUitls;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.consumables.dao.GoodsShipApprovalConsumablesDao;
import com.ebig.hdi.modules.consumables.entity.GoodsHospitalConsumablesEntity;
import com.ebig.hdi.modules.consumables.entity.GoodsSupplierConsumablesEntity;
import com.ebig.hdi.modules.consumables.service.GoodsHospitalConsumablesService;
import com.ebig.hdi.modules.consumables.service.GoodsShipApprovalConsumablesService;
import com.ebig.hdi.modules.consumables.service.GoodsSupplierConsumablesService;
import com.ebig.hdi.modules.core.service.UnicodeGoodsShipApprovalService;
import com.ebig.hdi.modules.core.service.UnicodeGoodsShipHistService;
import com.ebig.hdi.modules.core.service.UnicodeGoodsShipService;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 耗材匹对服务实现类
 */
@Service("goodsShipApprovalConsumablesService")
public class GoodsShipApprovalConsumablesServiceImpl extends ServiceImpl<GoodsShipApprovalConsumablesDao, UnicodeGoodsShipApprovalEntity> implements GoodsShipApprovalConsumablesService {

    @Autowired
    private UnicodeGoodsShipHistService unicodeGoodsShipHistService;

    @Autowired
    private GoodsHospitalConsumablesService goodsHospitalConsumablesService;

    @Autowired
    private GoodsSupplierConsumablesService goodsSupplierConsumablesService;

    @Autowired
    private UnicodeGoodsShipApprovalService unicodeGoodsShipApprovalService;

    @Autowired
    private UnicodeGoodsShipService unicodeGoodsShipService;


    @Override
    @DataFilter(subDept = true, user = false, tableAlias = "t1")
    public PageUtils queryPageHospitalGoods(Map<String, Object> params) {
        int currPage = Integer.parseInt(params.get(PageConstant.PAGE).toString());
        int pageSize = Integer.parseInt(params.get(PageConstant.LIMIT).toString());

        Page<UnicodeGoodsShipApprovalEntity> page = new Page<>(currPage, pageSize);
        List<UnicodeGoodsShipApprovalEntity> list = this.baseMapper.selectHospitaGoods(page, params);
        page.setRecords(list);
        return new PageUtils(page);
    }

    @Override
    @DataFilter(subDept = true, user = false, tableAlias = "t1")
    public PageUtils queryPageSupplierGoods(Map<String, Object> params) {
        int currPage = Integer.parseInt(params.get(PageConstant.PAGE).toString());
        int pageSize = Integer.parseInt(params.get(PageConstant.LIMIT).toString());

        Page<UnicodeGoodsShipApprovalEntity> page = new Page<>(currPage, pageSize);
        List<UnicodeGoodsShipApprovalEntity> list = this.baseMapper.selectSupplierGoods(page, params);
        page.setRecords(list);
        return new PageUtils(page);
    }

    @Override
    public PageUtils pGoodsList(Map<String, Object> params) {
        int currPage = Integer.parseInt(params.get(PageConstant.PAGE).toString());
        int pageSize = Integer.parseInt(params.get(PageConstant.LIMIT).toString());
        params.put("goodsName", params.get("pGoodsName"));
        params.put("pspecs", params.get("pGoodsSpecs"));

        Page<UnicodeGoodsShipApprovalEntity> page = new Page<>(currPage, pageSize);
        List<UnicodeGoodsShipApprovalEntity> list = this.baseMapper.selectPGoodsList(page, params);
        if (CollectionUtils.isEmpty(list)) {
            params.put("approvals", "");
            list = this.baseMapper.selectPGoodsList(page, params);
        }
        page.setRecords(list);
        return new PageUtils(page);
    }

    @Override
    public List<UnicodeGoodsShipApprovalEntity> listHospitalMatchGoods(Long torgId, Long pgoodsId, Long pspecsId, Long papprovalId) {
        return this.baseMapper.listHospitalMatchGoods(torgId, pgoodsId, pspecsId, papprovalId);
    }

    @Override
    public List<UnicodeGoodsShipApprovalEntity> selectListByColumn(Integer torgType, Integer tgoodsType, Long tgoodsId, Long tspecsId) {
        return this.baseMapper.selectListByColumn(torgType, tgoodsType, tgoodsId, tspecsId);
    }

    @Override
    public List<UnicodeGoodsShipApprovalEntity> listSupplierMatchGoods(Long torgId, Long pgoodsId, Long pspecsId, Long papprovalId) {
        return this.baseMapper.listSupplierMatchGoods(torgId, pgoodsId, pspecsId, papprovalId);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateHospitalPgoodsId(SysUserEntity sysUserEntity, UnicodeGoodsShipApprovalEntity entity) {

        if (entity != null && sysUserEntity != null) {
            UnicodeGoodsShipApprovalEntity ship = this.baseMapper.selectById(entity.getShipId());
            if (ship != null) {
                ship.setEditdate(new Timestamp(System.currentTimeMillis()));
                ship.setPgoodsId(entity.getId());
                ship.setCheckStatus(ApprovalTypeEnum.WAIT.getKey());
                ship.setShipFlag(IsMatchEnum.YES.getKey());
                ship.setEditmanid(sysUserEntity.getUserId());
                ship.setEditmanname(sysUserEntity.getUsername());
                ship.setPapprovalId(entity.getPapprovalId());
                ship.setPspecsId(entity.getPspecsId());
                List<UnicodeGoodsShipApprovalEntity> hospitalMatchGoodsList = this.baseMapper.listHospitalMatchGoods(ship.getTorgId(), ship.getPgoodsId(), entity.getPspecsId(), entity.getPapprovalId());

                if (CollectionUtils.isNotEmpty(hospitalMatchGoodsList)) {
                    throw new HdiException("匹对失败！该平台商品已存在匹对关系！");
                }

                // 更新商品匹对状态为已匹对
                GoodsHospitalConsumablesEntity goodsHospitalConsumablesEntity = new GoodsHospitalConsumablesEntity();
                goodsHospitalConsumablesEntity.setId(ship.getTgoodsId());
                goodsHospitalConsumablesEntity.setIsMatch(IsMatchEnum.YES.getKey());
                goodsHospitalConsumablesService.updateById(goodsHospitalConsumablesEntity);
                unicodeGoodsShipApprovalService.initiateApproval(sysUserEntity, ship, ChangeTypeEnum.UPDATE.getKey(),
                        ActTypeEnum.HOSPITAL_PLATFORM_CONSUMABLES_MATCH.getKey(), ActivitiConstant.ACTIVITI_HOSPITAL_PLATFORM_CONSUMABLES_MATCH);
                this.updateById(ship);
                // 将匹对记录插入匹对历史表
                UnicodeGoodsShipHistEntity hist = ReflectUitls.transform(ship,UnicodeGoodsShipHistEntity.class);
                hist.setOperType(OperationTypeEnum.MATCH.getKey());
                hist.setShipFlag(IsMatchEnum.YES.getKey());
                hist.setCremanid(sysUserEntity.getUserId());
                hist.setCremanname(sysUserEntity.getUsername());
                hist.setCredate(new Date());
                hist.setPgoodsName(entity.getPgoodsName());
                hist.setPapprovals(entity.getPapprovals());
                hist.setPspecs(entity.getPspecs());
                hist.setEditdate(null);
                hist.setEditmanname(null);
                hist.setPgoodsNature(entity.getPgoodsNature());
                hist.setPfactoryName(entity.getFactoryName());
                hist.setDelFlag(DelFlagEnum.NORMAL.getKey());
                unicodeGoodsShipHistService.insert(hist);
            }
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSupplierPgoodsId(SysUserEntity sysUserEntity, UnicodeGoodsShipApprovalEntity entity) {
        if (entity != null && sysUserEntity != null) {
            UnicodeGoodsShipApprovalEntity ship = this.baseMapper.selectById(entity.getShipId());
            if (ship != null) {
                ship.setEditdate(new Timestamp(System.currentTimeMillis()));
                ship.setPgoodsId(entity.getId());
                ship.setCheckStatus(ApprovalTypeEnum.WAIT.getKey());
                ship.setShipFlag(IsMatchEnum.YES.getKey());
                ship.setEditmanid(sysUserEntity.getUserId());
                ship.setEditmanname(sysUserEntity.getUsername());
                ship.setPapprovalId(entity.getPapprovalId());
                ship.setPspecsId(entity.getPspecsId());
                List<UnicodeGoodsShipApprovalEntity> supplierMatchGoodsList = this.baseMapper.listSupplierMatchGoods(ship.getTorgId(), ship.getPgoodsId(), entity.getPspecsId(), entity.getPapprovalId());

                if (CollectionUtils.isNotEmpty(supplierMatchGoodsList)) {
                    throw new HdiException("匹对失败！该平台商品已存在匹对关系！");
                }
                // 更新商品匹对状态为已匹对
                GoodsSupplierConsumablesEntity goodsSupplierConsumablesEntity = new GoodsSupplierConsumablesEntity();
                goodsSupplierConsumablesEntity.setId(ship.getTgoodsId());
                goodsSupplierConsumablesEntity.setIsMatch(IsMatchEnum.YES.getKey());
                goodsSupplierConsumablesService.updateById(goodsSupplierConsumablesEntity);
                unicodeGoodsShipApprovalService.initiateApproval(sysUserEntity, ship, ChangeTypeEnum.UPDATE.getKey(),
                        ActTypeEnum.SUPPLIER_PLATFORM_CONSUMABLES_MATCH.getKey(), ActivitiConstant.ACTIVITI_SUPPLIER_PLATFORM_CONSUMABLES_MATCH);
                this.updateById(ship);
                // 将匹对记录插入匹对历史表
                UnicodeGoodsShipHistEntity hist = ReflectUitls.transform(ship,UnicodeGoodsShipHistEntity.class);
                hist.setOperType(OperationTypeEnum.MATCH.getKey());
                hist.setShipFlag(IsMatchEnum.YES.getKey());
                hist.setCremanid(sysUserEntity.getUserId());
                hist.setCremanname(sysUserEntity.getUsername());
                hist.setCredate(new Date());
                hist.setPgoodsName(entity.getPgoodsName());
                hist.setPapprovals(entity.getPapprovals());
                hist.setPspecs(entity.getPspecs());
                hist.setEditdate(null);
                hist.setEditmanname(null);
                hist.setPfactoryName(entity.getFactoryName());
                hist.setPgoodsNature(entity.getPgoodsNature());
                hist.setDelFlag(DelFlagEnum.NORMAL.getKey());
                unicodeGoodsShipHistService.insert(hist);

            }
        }
    }


    @Override
    public Map<String, String> checkStatus(Map<String, String> params, SysUserEntity user) {
        Map<String, String> errorMap = new HashMap<>(16);
        Long shipId = Long.parseLong(params.get("shipId"));
        Integer checkStatus = Integer.parseInt(params.get("checkStatus"));
        if (StringUtil.isEmpty(checkStatus)) {
            errorMap.put("errorMessage", "状态为空");
            return errorMap;
        }
        if (StringUtil.isEmpty(shipId)) {
            errorMap.put("errorMessage", "传入ID为空");
            return errorMap;
        }
        if (!user.getUserType().equals(TypeEnum.USER_PLATFORM.getKey())) {
            errorMap.put("errorMessage", "非平台用户审批权限");
            return errorMap;
        }
        UnicodeGoodsShipApprovalEntity unicodeGoodsShipApprovalEntity = this.baseMapper.selectById(shipId);
        unicodeGoodsShipApprovalEntity.setEditdate(new Date());
        unicodeGoodsShipApprovalEntity.setEditmanid(user.getUserId());
        if (ApprovalTypeEnum.PASS.getKey().equals(checkStatus)) {
            //修改待审批表
            unicodeGoodsShipApprovalEntity.setCheckStatus(ApprovalTypeEnum.PASS.getKey());
            UnicodeGoodsShipEntity unicodeGoodsShipEntity = unicodeGoodsShipService.selectById(shipId);
            if (StringUtil.isEmpty(unicodeGoodsShipEntity)) {
                unicodeGoodsShipService.insert(unicodeGoodsShipApprovalEntity);
            } else {
                unicodeGoodsShipService.update(unicodeGoodsShipApprovalEntity);
            }
            //修改历史记录表
        }
        if (ApprovalTypeEnum.FAIL.getKey().equals(checkStatus)) {
            unicodeGoodsShipApprovalEntity.setShipFlag(IsMatchEnum.NO.getKey());
            unicodeGoodsShipApprovalEntity.setCheckStatus(ApprovalTypeEnum.FAIL.getKey());
        }
        this.baseMapper.updateById(unicodeGoodsShipApprovalEntity);
        return errorMap;
    }

    @Override
    public Map<String, String> hospitalCheckstatus(Map<String, String> params, SysUserEntity user) {
        Map<String, String> errorMap = new HashMap<>(16);
        Long shipId = Long.parseLong(params.get("shipId"));
        Integer checkStatus = Integer.parseInt(params.get("checkStatus"));
        if (StringUtil.isEmpty(checkStatus)) {
            errorMap.put("errorMessage", "状态为空");
            return errorMap;
        }
        if (StringUtil.isEmpty(shipId)) {
            errorMap.put("errorMessage", "传入ID为空");
            return errorMap;
        }
        if (!user.getUserType().equals(TypeEnum.USER_PLATFORM.getKey())) {
            errorMap.put("errorMessage", "非平台用户审批权限");
            return errorMap;
        }
        UnicodeGoodsShipApprovalEntity unicodeGoodsShipApprovalEntity = this.baseMapper.selectById(shipId);
        unicodeGoodsShipApprovalEntity.setEditdate(new Date());
        unicodeGoodsShipApprovalEntity.setEditmanid(user.getUserId());
        if (ApprovalTypeEnum.PASS.getKey().equals(checkStatus)) {
            //修改待审批表
            unicodeGoodsShipApprovalEntity.setCheckStatus(ApprovalTypeEnum.PASS.getKey());
            UnicodeGoodsShipEntity unicodeGoodsShipEntity = unicodeGoodsShipService.selectById(shipId);
            if (StringUtil.isEmpty(unicodeGoodsShipEntity)) {
                unicodeGoodsShipService.insert(unicodeGoodsShipApprovalEntity);
            } else {

                unicodeGoodsShipService.update(unicodeGoodsShipApprovalEntity);
            }
            //修改历史记录表
        }
        if (ApprovalTypeEnum.FAIL.getKey().equals(checkStatus)) {
            unicodeGoodsShipApprovalEntity.setShipFlag(IsMatchEnum.NO.getKey());
            unicodeGoodsShipApprovalEntity.setCheckStatus(ApprovalTypeEnum.FAIL.getKey());
        }
        this.baseMapper.updateById(unicodeGoodsShipApprovalEntity);
        return errorMap;
    }

}
