package com.ebig.hdi.modules.refunds.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.enums.*;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.Constant;
import com.ebig.hdi.common.utils.DateUtils;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.hdi.modules.core.entity.CoreStorehouseEntity;
import com.ebig.hdi.modules.core.entity.CoreSupplyDetailEntity;
import com.ebig.hdi.modules.core.entity.CoreSupplyMasterEntity;
import com.ebig.hdi.modules.core.service.CoreStorehouseService;
import com.ebig.hdi.modules.core.service.CoreSupplyDetailService;
import com.ebig.hdi.modules.core.service.CoreSupplyMasterService;
import com.ebig.hdi.modules.org.entity.OrgHospitalInfoEntity;
import com.ebig.hdi.modules.org.entity.OrgSupplierInfoEntity;
import com.ebig.hdi.modules.org.service.OrgHospitalInfoService;
import com.ebig.hdi.modules.org.service.OrgSupplierInfoService;
import com.ebig.hdi.modules.refunds.dao.RefundsMasterDao;
import com.ebig.hdi.modules.refunds.entity.RefundsApplyDetailEntity;
import com.ebig.hdi.modules.refunds.entity.RefundsApplyMasterEntity;
import com.ebig.hdi.modules.refunds.entity.RefundsDetailEntity;
import com.ebig.hdi.modules.refunds.entity.RefundsMasterEntity;
import com.ebig.hdi.modules.refunds.entity.vo.*;
import com.ebig.hdi.modules.refunds.param.RefundsMasterParam;
import com.ebig.hdi.modules.refunds.service.RefundsApplyDetailService;
import com.ebig.hdi.modules.refunds.service.RefundsApplyMasterService;
import com.ebig.hdi.modules.refunds.service.RefundsDetailService;
import com.ebig.hdi.modules.refunds.service.RefundsMasterService;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import com.ebig.hdi.modules.sys.service.SysSequenceService;
import com.ebig.hdi.modules.unicode.entity.UnicodeSupplyShipEntity;
import com.ebig.hdi.modules.unicode.service.UnicodeSupplyShipService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service("refundsMasterService")
@Slf4j
public class RefundsMasterServiceImpl extends ServiceImpl<RefundsMasterDao, RefundsMasterEntity> implements RefundsMasterService {

    @Autowired
    private RefundsDetailService refundsDetailService;
    @Autowired
    private RefundsApplyDetailService refundsApplyDetailService;
    @Autowired
    private RefundsApplyMasterService refundsApplyMasterService;
    @Autowired
    private SysSequenceService sysSequenceService;
    @Autowired
    private OrgSupplierInfoService orgSupplierInfoService;

    @Autowired
    private OrgHospitalInfoService orgHospitalInfoService;

    @Autowired
    private UnicodeSupplyShipService unicodeSupplyShipService;

    @Autowired
    private CoreSupplyMasterService coreSupplyMasterService;

    @Autowired
    private CoreStorehouseService coreStorehouseService;

    @Autowired
    private CoreSupplyDetailService coreSupplyDetailService;

    @Autowired
    private RefundsMasterDao refundsMasterDao;

    @Override
    @DataFilter(subDept = true, user = false, tableAlias = "t1")
    public PageUtils queryPage(Map<String, Object> params) {
        RefundsMasterVO rmVO = new RefundsMasterVO();
        rmVO.setHospitalName((String) params.get("hospitalName"));
        rmVO.setStoreHouseName((String) params.get("storeHouseName"));
        rmVO.setRefundsNo((String) params.get("refundsNo"));
        rmVO.setRefundsTimeBeginStr((String) params.get("refundsTimeBeginStr"));
        rmVO.setRefundsTimeEndStr((String) params.get("refundsTimeEndStr"));
        rmVO.setStatus((Integer) params.get("status"));
        rmVO.setFileterDept((String) params.get(Constant.SQL_FILTER));

        String sidx = (String) params.get("sidx");
        String order = (String) params.get("order");
        int currPage = Integer.parseInt(params.get("page").toString());
        int pageSize = Integer.parseInt(params.get("limit").toString());

        Page<RefundsMasterVO> page = new Page<>(currPage, pageSize, order);
        if (sidx != null) {
            page.setAsc(sidx.equals("desc"));
        } else {
            //标志，设置默认按更新时间和创建时间排序
            rmVO.setIsDefaultOrder(1);
        }
        //自定义分页查询列表带过滤条件
        List<RefundsMasterVO> list = this.baseMapper.listForPage(page, rmVO);
        page.setRecords(list);
        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(RefundsMasterVO refundsMasterVO) {
        if (refundsMasterVO != null) {
            //校验参数
            checkValue(refundsMasterVO);
            //设置初始状态为未提交
            refundsMasterVO.setStatus(RefundsTypeEnum.UNSUBMIT.getKey());
            refundsMasterVO.setCreateTime(new Date());
            refundsMasterVO.setDataSource(HospitalDataSource.SYSTEM.getKey());
            //设置原供应商id
            refundsMasterVO.setSourcesSupplierId(orgSupplierInfoService.selectSourceSupplierId(refundsMasterVO.getSupplierId(), refundsMasterVO.getHospitalId()));
            //自动生成退货单编号
            String refundsNo = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.REFUNDS_MASTER_NO.getKey());
            refundsMasterVO.setRefundsNo(refundsNo);
            //保存退货单
            baseMapper.insert(refundsMasterVO);

            //保存退货详情单
            List<RefundsDetailEntity> rdeList = refundsMasterVO.getRefundsDetailList();
            //循环设置退货单id和新增必填信息
            if (CollectionUtils.isNotEmpty(rdeList)) {
                int status = 3;
                Long applyMasterId = null;
                for (RefundsDetailEntity detail : rdeList) {
                    detail.setMasterId(refundsMasterVO.getId());

                    detail.setCreateId(refundsMasterVO.getCreateId());
                    detail.setCreateTime(refundsMasterVO.getCreateTime());
                    if (StringUtils.isNotBlank(refundsMasterVO.getRefundsApplyNo())) {
                        //修改退货申请细单的实际退货数量
                        RefundsApplyDetailEntity applyDetail = new RefundsApplyDetailEntity();
                        applyDetail.setRealityRefundsNumber(detail.getRealityRefundsNumber());
                        refundsApplyDetailService.updateById(applyDetail);
                        if (detail.getApplyRefundsNumber() != detail.getRealityRefundsNumber()) {
                            status = 4;
                        }
                    }

                }
                if (StringUtils.isNotBlank(refundsMasterVO.getRefundsApplyNo())) {
                    //修改退货申请主单的状态
                    RefundsApplyMasterEntity rame = new RefundsApplyMasterEntity();
                    rame.setId(applyMasterId);
                    rame.setStatus(status);
                    refundsApplyMasterService.updateById(rame);
                }
                //批量插入退货单详情数据
                refundsDetailService.insertBatch(rdeList);
            }
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RefundsMasterVO rmVO) {
        if (rmVO != null) {
            Long masterId = rmVO.getId();
            if (masterId == null) {
                throw new HdiException("修改传入的id不能为空");
            }
            //设置退货时间
            if (StringUtils.isNotBlank(rmVO.getRefundsTimeStr())) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date refundsTime = null;
                try {
                    refundsTime = sdf.parse(rmVO.getRefundsTimeStr());
                    rmVO.setRefundsTime(refundsTime);
                } catch (Exception e) {
                    throw new HdiException("退货时间格式不正确(1960-01-01 00:00:00)");
                }
            }
            //修改退货主单信息
            rmVO.setEditTime(new Date());
            baseMapper.updateById(rmVO);

            //修改退货申请详情信息
            List<RefundsDetailEntity> radeList = refundsDetailService.selectList(new EntityWrapper<RefundsDetailEntity>()
                    .eq("master_id", masterId)
                    .eq("del_flag", 0));//原来的详情信息
            List<RefundsDetailEntity> updateDetailList = rmVO.getRefundsDetailList();//修改的详情信息

            //获取数据库里的退货申请详情的idlist
            List<Long> idList = getIdList(radeList);

            //循环遍历 没有id的话直接插入，有id的话把id从idList里移除，idList里剩下的就是被删除了的id
            if (CollectionUtils.isNotEmpty(updateDetailList)) {
                int status = 3;
                Long applyMasterId = null;
                for (RefundsDetailEntity updateDetail : updateDetailList) {
                    if (updateDetail.getId() == null) {
                        //执行新增操作
                        updateDetail.setMasterId(masterId);
                        updateDetail.setCreateId(rmVO.getEditId());
                        updateDetail.setCreateTime(rmVO.getEditTime());
                        refundsDetailService.insert(updateDetail);
                    } else {
                        //从idList里移除id并且执行修改操作
                        idList.remove(updateDetail.getId());

                        //执行详情修改操作
                        updateDetail.setEditId(rmVO.getEditId());
                        updateDetail.setEditTime(rmVO.getEditTime());
                        refundsDetailService.updateById(updateDetail);
                    }
                    if (StringUtils.isNotBlank(rmVO.getRefundsApplyNo())) {
                        //修改退货申请细单的实际退货数量
                        RefundsApplyDetailEntity applyDetail = new RefundsApplyDetailEntity();
                        applyDetail.setRealityRefundsNumber(updateDetail.getRealityRefundsNumber());
                        refundsApplyDetailService.updateById(applyDetail);
                        if (applyDetail.getApplyRefundsNumber() != applyDetail.getRealityRefundsNumber()) {
                            status = 4;
                        }
                    }
                }
                if (StringUtils.isNotBlank(rmVO.getRefundsApplyNo())) {
                    //修改退货申请主单的状态
                    RefundsApplyMasterEntity rame = new RefundsApplyMasterEntity();
                    rame.setId(applyMasterId);
                    rame.setStatus(status);
                    refundsApplyMasterService.updateById(rame);
                }
                //删除idList里对应的详情记录
                if (CollectionUtils.isNotEmpty(idList)) {
                    refundsDetailService.deleteBatchIds(idList);
                }
            }
        }

    }

    @Override
    public RefundsMasterVO selectById(Long id) {
        if (id == null) {
            throw new HdiException("退货主单id不能为空");
        }
        RefundsMasterVO rmVO = baseMapper.selectRefundsById(id);
        //通过退货主单id查询出所对应的所有细单
        List<RefundsDetailVO> radeList = refundsDetailService.selectByMasterId(rmVO.getId());
        rmVO.setRefundsDetailVOList(radeList);
        return rmVO;
    }


    @Override
    public List<RefundsDetailVO> selectToSave(Map<String, Object> params) {
        if (params.get("hospitalId") == null || params.get("sourcesHospitalId") == null) {
            throw new HdiException("传入的医院id/原医院id不能为空");
        }
        if (params.get("storeHouseId") == null || params.get("sourcesStoreHouseId") == null) {
            throw new HdiException("传入的库房id/原库房id不能为空");
        }
        if (params.get("supplierId") == null) {
            throw new HdiException("传入的供应商id不能为空");
        }
        return baseMapper.selectToSave(params);
    }

    private List<Long> getIdList(List<RefundsDetailEntity> radeList) {
        if (radeList != null) {
            List<Long> idList = new ArrayList<>();
            for (RefundsDetailEntity entity : radeList) {
                idList.add(entity.getId());
            }
            return idList;
        }
        return new ArrayList<>();
    }

    private void checkValue(RefundsMasterVO refundsMasterVO) {
        if (refundsMasterVO.getSupplierId() == null) {
            throw new HdiException("原系统医院供应商id不能为空");
        }
        if (StringUtils.isBlank(refundsMasterVO.getSourcesHospitalId()) || refundsMasterVO.getHospitalId() == null) {
            throw new HdiException("医院id/原系统医院id不能为空");
        }
        if (StringUtils.isBlank(refundsMasterVO.getSourcesStoreHouseId()) || refundsMasterVO.getStoreHouseId() == null) {
            throw new HdiException("库房id/原库房id不能为空");
        }
        if (StringUtils.isBlank(refundsMasterVO.getRefundsTimeStr())) {
            throw new HdiException("退货时间不能为空");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date refundsTime = null;
            try {
                refundsTime = sdf.parse(refundsMasterVO.getRefundsTimeStr());
                refundsMasterVO.setRefundsTime(refundsTime);
            } catch (Exception e) {
                throw new HdiException("退货时间格式不正确(1960-01-01 00:00:00)");
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submit(Long[] ids, Long userId) {
        if (ids != null) {
            RefundsMasterEntity refundsMasterEntity = new RefundsMasterEntity();
            refundsMasterEntity.setEditId(userId);
            refundsMasterEntity.setEditTime(new Date());
            refundsMasterEntity.setStatus(RefundsTypeEnum.SUBMITED.getKey());
            for (Long id : ids) {
                refundsMasterEntity.setId(id);
                baseMapper.updateById(refundsMasterEntity);
            }
        }

    }

    @Override
    @DataFilter(subDept = true, user = false, tableAlias = "m")
    public List<Map<String, Object>> getList(Map<String, Object> queryParam) {
        RefundsMasterParam rmVO = new RefundsMasterParam();
        rmVO.setHospitalName((String) queryParam.get("hospitalName"));
        rmVO.setStoreHouseName((String) queryParam.get("storeHouseName"));
        rmVO.setRefundsNo((String) queryParam.get("refundsNo"));
        rmVO.setRefundsTimeBeginStr((String) queryParam.get("refundsTimeBeginStr"));
        rmVO.setRefundsTimeEndStr((String) queryParam.get("refundsTimeEndStr"));
        rmVO.setStatus((Integer) queryParam.get("status"));
        rmVO.setFileterDept((String) queryParam.get(Constant.SQL_FILTER));
        return this.baseMapper.getList((String[]) queryParam.get("columns"), rmVO);
    }

    @Override
    public Map importData(String[][] rows, SysUserEntity user) {
        if(user == null){
            throw new HdiException("导入失败，请先登录！！！");
        }
        if (!user.getUserType().equals(TypeEnum.USER_SUPPLIER.getKey())) {
            throw new HdiException("当前登录用户无此权限");
        }
        //保存返回信息
        StringBuilder sb = new StringBuilder();
        String key = "successCount";
        Map<String, String> map = new HashMap<>(16);
        if (user.getUserId() == null || user.getDeptId() == null || rows.length <= 1) {
            map.put(key, "0");
            map.put("failCount", String.valueOf(rows.length - 1));
            sb.append("系统错误");
            map.put("errorMessage", sb.toString());
            return map;
        }
        Integer successCount = 0;
        Map<Integer, RefundsMasterEntity> masterMap = new HashMap<>(16);
        Map<Integer, List<RefundsDetailEntity>> detailMap = new HashMap<>(16);
        List<RefundsDetailEntity> detailEntityList = null;
        //查询平台供应商信息
        List<OrgSupplierInfoEntity> supplierInfoList = orgSupplierInfoService.selectByDeptId(user.getDeptId());
        if (StringUtil.isEmpty(supplierInfoList)) {
            log.error("生成退货主单失败，供应商不存在");
            sb.append("\n");
            return map;
        }
        boolean flag = false;
        for (int i = 1; i < rows.length; i++) {
            if (isEmpty(sb, i, rows[i])) {
                flag = true;
                continue;
            }
        }
        if (flag) {
            map.put(key, "0");
            map.put("failCount", String.valueOf(rows.length - 1));
            map.put("errorMessage", sb.toString());
            return map;
        }
        for (int i = 1; i < rows.length; i++) {
            String[] row = rows[i];
            RefundsMasterEntity mapMasterEntity = masterMap.get((StringUtil.trim(row[0])
                    + StringUtil.trim(row[1]) + StringUtil.trim(row[2])
                    + StringUtil.trim(row[3]) + StringUtil.trim(row[4])).hashCode());
            if (mapMasterEntity == null) {
                detailEntityList = new ArrayList<>();
                RefundsMasterEntity master = getRefundsMasterEntity(user.getUserId(),user.getDeptId(), sb, supplierInfoList, i, row);
                if (master == null) {
                    continue;
                }
                masterMap.put((StringUtil.trim(row[0]) + StringUtil.trim(row[1]) + StringUtil.trim(row[2])
                        + StringUtil.trim(row[3]) + StringUtil.trim(row[4])).hashCode(), master);
                detailMap.put((StringUtil.trim(row[0]) + StringUtil.trim(row[1]) + StringUtil.trim(row[2])
                        + StringUtil.trim(row[3]) + StringUtil.trim(row[4])).hashCode(), detailEntityList);

            }
            //插入供货细单
            mapMasterEntity = masterMap.get((StringUtil.trim(row[0]) + StringUtil.trim(row[1]) + StringUtil.trim(row[2])
                    + StringUtil.trim(row[3]) + StringUtil.trim(row[4])).hashCode());
            //根据供货单编号查询供货细单信息
            List<CoreSupplyDetailEntity> list =
                    coreSupplyDetailService.getList(StringUtil.trim(row[5]),
                            StringUtil.trim(row[6]), StringUtil.trim(row[8]),StringUtil.trim(row[10]));
            if (CollectionUtils.isEmpty(list)) {
                log.error(MessageFormat.format("生成退货细单失败,供货单号' {0} '或商品名称 ' {1} ',商品规格' {2}'", row[5], row[6], row[8]));
                sb.append("第" + (i + 2) + "行数据错误，请检查商品名称，商品规格,生产批号是否正确");
                sb.append("\n");
                continue;
            }
            RefundsDetailEntity detailEntity = getRefundsDetailEntity(user.getUserId(), user.getDeptId(), sb, i, row, mapMasterEntity, list);
            if (detailEntity == null) {
                continue;
            }
            detailEntityList.add(detailEntity);
            successCount++;
        }
        //获取供货主单
        List<RefundsMasterEntity> masterEntityList = masterMap.values().stream().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(masterEntityList) && (rows.length - successCount) == 1) {
            this.insertBatch(masterEntityList);
            List<RefundsDetailEntity> insertList = new ArrayList<>();
            detailMap.forEach((Integer k, List<RefundsDetailEntity> v) -> {
                for (RefundsDetailEntity entity : v) {
                    entity.setMasterId(masterMap.get(k).getId());
                    insertList.add(entity);
                }
            });
            refundsDetailService.insertBatch(insertList);
            map.put(key, successCount.toString());
            map.put("failCount", "0");
        } else {
            map.put(key, "0");
            map.put("failCount", String.valueOf(rows.length - 1));
        }
        map.put("errorMessage", sb.toString());
        return map;

    }

    /**
     * 校验
     *
     * @param sb
     * @param i
     * @param row
     * @return
     */
    private boolean isEmpty(StringBuilder sb, int i, String[] row) {

        if (StringUtil.isEmpty(StringUtil.trim(row[4]))) {
            log.error("---------------------------必填项退货时间为空-----------------------");
            sb.append("第" + (i + 2) + "行必填项退货时间为空");
            sb.append("\n");
            return true;
        }

        if (StringUtil.isEmpty(StringUtil.trim(row[5]))) {
            log.error("---------------------------必填项供货单编号为空-----------------------");
            sb.append("第" + (i + 2) + "行必填项供货单编号为空");
            sb.append("\n");
            return true;
        }
        if (StringUtil.isEmpty(StringUtil.trim(row[6]))) {
            log.error("---------------------------必填项商品名称为空-----------------------");
            sb.append("第" + (i + 2) + "行必填项商品名称为空");
            sb.append("\n");
            return true;
        }
        if (StringUtil.isEmpty(StringUtil.trim(row[8]))) {
            log.error("---------------------------必填项商品规格为空-----------------------");
            sb.append("第" + (i + 2) + "行必填项商品规格为空");
            sb.append("\n");
            return true;
        }
        if (StringUtil.isEmpty(StringUtil.trim(row[10]))) {
            log.error("---------------------------必填项生产批号为空-----------------------");
            sb.append("第" + (i + 2) + "行必填项生产批号为空");
            sb.append("\n");
            return true;
        }
        if (StringUtil.isEmpty(StringUtil.trim(row[11]))) {
            log.error("---------------------------必填项退货数量为空-----------------------");
            sb.append("第" + (i + 2) + "行必填项退货数量为空");
            sb.append("\n");
            return true;
        }
        if (StringUtil.isEmpty(StringUtil.trim(row[12]))) {
            log.error("---------------------------必填项退货单价为空-----------------------");
            sb.append("第" + (i + 2) + "行必填项退货单价为空");
            sb.append("\n");
            return true;
        }
        if (StringUtil.isEmpty(StringUtil.trim(row[13]))) {
            log.error("---------------------------必填项退货原因为空-----------------------");
            sb.append("第" + (i + 2) + "行必填项退货原因为空");
            sb.append("\n");
            return true;
        }
        return false;
    }

    private RefundsDetailEntity getRefundsDetailEntity(Long userId, Long deptId, StringBuilder sb, int i, String[] row, RefundsMasterEntity mapMasterEntity, List<CoreSupplyDetailEntity> list) {
        RefundsDetailEntity detailEntity = new RefundsDetailEntity();
        CoreSupplyDetailEntity supply = list.get(0);
        detailEntity.setSupplyMasterId(supply.getSupplyMasterId());
        detailEntity.setSupplyDetailId(supply.getSupplyDetailId().toString());
        detailEntity.setLotId(supply.getLotid());
        detailEntity.setLotNo(supply.getLotno());
        detailEntity.setGoodsUnitCode(supply.getGoodsunit());
        detailEntity.setSupplyno(StringUtil.trim(row[5]));
        //查询原医院商品信息,平台医院商品信息

        Map<String, Object> matchMap = getMap(mapMasterEntity.getHospitalId(), mapMasterEntity.getSupplierId(), StringUtil.trim(row[6]), row[8]);
        if (matchMap == null || matchMap.isEmpty()) {
            log.error(MessageFormat.format("生成退货细单失败,商品名称' {1} '或商品规格' {2}'", row[6], row[8]));
            sb.append("第" + (i + 2) + "行商品名称或规格数据错误");
            sb.append("\n");
            return null;
        }


        detailEntity.setGoodsType(Integer.parseInt(matchMap.get("goods_type").toString()));
        detailEntity.setSourcesGoodsName(matchMap.get("hospital_goods_name").toString());
        detailEntity.setSourcesSpecsId(matchMap.get("hospital_sources_specs_id").toString());
        detailEntity.setSourcesSpecsCode(matchMap.get("hospital_goods_specs_code").toString());
        detailEntity.setSourcesSpecsName(matchMap.get("hospital_goods_specs_name").toString());

        detailEntity.setGoodsId(Long.parseLong(matchMap.get("hospital_goods_id").toString()));
        detailEntity.setGoodsCode(matchMap.get("hospital_goods_code").toString());
        detailEntity.setGoodsName(matchMap.get("hospital_goods_name").toString());
        detailEntity.setSpecsId(Long.parseLong(matchMap.get("hospital_goods_specs_id").toString()));
        detailEntity.setSpecsCode(matchMap.get("hospital_goods_specs_code").toString());
        detailEntity.setSpecsName(matchMap.get("hospital_goods_specs_name").toString());
        Integer realityRefundsNumber = Integer.parseInt(StringUtil.trim(row[11]));
        if (supply.getSupplyQty() < realityRefundsNumber) {
            sb.append("第" + (i + 2) + "行退货数量不能大于供货数量");
            sb.append("\n");
            return null;
        }
        detailEntity.setRealityRefundsNumber(Integer.parseInt(StringUtil.trim(row[11])));
        detailEntity.setRefundsPrice(new BigDecimal(StringUtil.trim(row[12])));
        detailEntity.setRefundsRemark(StringUtil.trim(row[13]));
        detailEntity.setDeptId(deptId);
        detailEntity.setCreateId(userId);
        detailEntity.setCreateTime(new Date());
        detailEntity.setDelFlag(DelFlagEnum.NORMAL.getKey());
        return detailEntity;
    }

    private RefundsMasterEntity getRefundsMasterEntity(Long userId, Long deptId, StringBuilder sb, List<OrgSupplierInfoEntity> supplierInfoList, int i, String[] row) {
        RefundsMasterEntity master = new RefundsMasterEntity();
        CoreSupplyMasterEntity entity = coreSupplyMasterService.selectOne(new EntityWrapper<CoreSupplyMasterEntity>().eq("supplyno", StringUtil.trim(row[5])));
        if (entity == null) {
            sb.append("第" + (i + 2) + "行供货单编号不存在");
            sb.append("\n");
            return null;
        }
        if (SupplyStatusEnum.UNCOMMITTED.getKey().equals(entity.getSupplyStatus())) {
            sb.append("第" + (i + 2) + "行数据错误,该供货单编号没有提交到医院");
            sb.append("\n");
            return null;
        }
        master.setHospitalId(entity.getHorgId());
        master.setHospitalCode(entity.getHospitalCode());
        master.setHospitalName(entity.getHospitalName());

        master.setSupplierId(entity.getSupplierId());
        master.setSupplierCode(entity.getSupplierCode());
        master.setSupplierName(entity.getSupplierName());

        master.setSourcesHospitalId(entity.getSourcesHospitalId());
        master.setSourcesHospitalCode(entity.getSourcesHospitalCode());
        master.setSourcesHospitalName(entity.getSourcesHospitalName());
        master.setSourcesSupplierId(entity.getSourcesSupplierId());
        master.setSourcesSupplierCode(entity.getSourcesSupplierCode());
        master.setSourcesSupplierName(entity.getSourcesSupplierName());

        master.setStoreHouseId(entity.getStorehouseid());
        master.setStorehouseNo(entity.getStorehouseNo());
        master.setStoreHouseName(entity.getStorehouseName());
        master.setRefundsApplyNo(row[2]);
        master.setRegressionNumber(row[3]);
        master.setRefundsTime(DateUtils.stringToDate(StringUtil.trim(row[4]), DateUtils.DATE_TIME_PATTERN));

        master.setStatus(RefundsTypeEnum.UNSUBMIT.getKey());
        master.setCreateTime(new Date());
        master.setDataSource(HospitalDataSource.SYSTEM.getKey());
        //自动生成退货单编号
        master.setRefundsNo(sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.REFUNDS_MASTER_NO.getKey()));
        master.setSourcesStoreHouseId(entity.getSourcesStorehouseId());
        master.setDeptId(deptId);
        master.setCreateId(userId);
        master.setDelFlag(DelFlagEnum.NORMAL.getKey());
        return master;
    }

    /**
     * /查询原医院商品信息,平台医院商品信息
     *
     * @param hospitalId             医院id
     * @param supplierId             供应商id
     * @param supplierGoodsName      供应商商品名称
     * @param supplierGoodsSpecsName 供应商商品规格名称
     * @return
     */
    private Map<String, Object> getMap(Long hospitalId, Long supplierId, String supplierGoodsName, String supplierGoodsSpecsName) {
        return this.baseMapper.getMap(hospitalId, supplierId, supplierGoodsName, supplierGoodsSpecsName);
    }

    private Map<String, Object> getMaster(Long hospitalId, Long supplierId, Integer goodsType, Long supplierGoodsId, Long supplierGoodsSpecsId) {
        return this.baseMapper.getMaster(hospitalId, supplierId, goodsType, supplierGoodsId, supplierGoodsSpecsId);
    }

    /**
     * 新增退货单
     *
     * @param refundsMasterVo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map saveRefunds(Long deptId, Long userId, SaveRefundsMasterVo refundsMasterVo) {

        List<SaveRefundsDetailVo> refundsDetailVos = refundsMasterVo.getSaveRefundsDetailVoList();
        Map<String, Object> map = new HashMap<>(16);
        //查询医院信息
        OrgHospitalInfoEntity hospitalInfo = orgHospitalInfoService.selectOne(new EntityWrapper<OrgHospitalInfoEntity>().eq("id", refundsMasterVo.getHospitalId()));
        if (hospitalInfo == null) {
            log.error("---------------------------新增退货单失败,医院名称为空-----------------------");
            map.put("errmessage", "医院信息为空");
            return map;
        }
        //查询平台供应商信息
        List<OrgSupplierInfoEntity> supplierInfo = orgSupplierInfoService.selectByDeptId(deptId);
        if (CollectionUtils.isEmpty(supplierInfo)) {
            log.error("---------------------------新增退货单失败,供应商数据为空-----------------------");
            map.put("errmessage", "供应商数据为空");
            return map;
        }
        //退货主单信息设置
        RefundsMasterEntity refundsMasterEntity = new RefundsMasterEntity();
        refundsMasterEntity.setStoreHouseName(refundsMasterVo.getStoreHouseName());
        //退货单号唯一问题
        Integer  exist = this.baseMapper.selectByRefundsNo(refundsMasterVo.getRefundsNo());
        if (exist != 0) {
            map.put("errmessage", "已存在该退货单号");
            return map;
        } else {
            refundsMasterEntity.setRefundsNo(refundsMasterVo.getRefundsNo());
        }
        refundsMasterEntity.setRefundsApplyNo(refundsMasterVo.getRefundsApplyNo());
        refundsMasterEntity.setRefundsTime(refundsMasterVo.getRefundsTime());
        //销退单号
        refundsMasterEntity.setRegressionNumber(refundsMasterVo.getRefundsNo());

        refundsMasterEntity.setHospitalId(hospitalInfo.getId());
        refundsMasterEntity.setHospitalName(hospitalInfo.getHospitalName());
        refundsMasterEntity.setHospitalCode(hospitalInfo.getHospitalCode());

        refundsMasterEntity.setSupplierId(supplierInfo.get(0).getId());
        refundsMasterEntity.setSupplierCode(supplierInfo.get(0).getSupplierCode());
        refundsMasterEntity.setSupplierName(supplierInfo.get(0).getSupplierName());

        //查询原医院，原供应商信息
        UnicodeSupplyShipEntity shipEntity = unicodeSupplyShipService.selectBySupplierIdAndHospitalId(refundsMasterEntity.getSupplierId(), refundsMasterEntity.getHospitalId());
        if (shipEntity == null) {
            log.error("---------------------------新增退货单失败,供应商医院配对失败-----------------------");
            map.put("errmessage", "供应商医院配对失败");
            return map;
        }

        refundsMasterEntity.setSourcesHospitalId(shipEntity.getSourcesHospitalId());
        refundsMasterEntity.setSourcesHospitalCode(shipEntity.getSourcesHospitalCreditCode());
        refundsMasterEntity.setSourcesHospitalName(shipEntity.getSourcesHospitalName());
        refundsMasterEntity.setSourcesSupplierId(shipEntity.getSourcesSupplierId());
        refundsMasterEntity.setSourcesSupplierCode(shipEntity.getSourcesSupplierCreditCode());
        refundsMasterEntity.setSourcesSupplierName(shipEntity.getSourcesSupplierName());

        //关联查询库房
        CoreStorehouseEntity coreStorehouseEntity = coreStorehouseService
                .selectOne(new EntityWrapper<CoreStorehouseEntity>().eq("horg_id", hospitalInfo.getId()).eq("storehouseid", refundsMasterVo.getStoreHouseId()));
        if (coreStorehouseEntity == null) {
            log.error("---------------------------新增退货单失败,医院库房匹对失败-----------------------");
            map.put("errmessage", "医院库房匹对失败");
            return map;
        }
        refundsMasterEntity.setSourcesStoreHouseId(coreStorehouseEntity.getOrgdataid());
        refundsMasterEntity.setStoreHouseId(coreStorehouseEntity.getStorehouseid());
        refundsMasterEntity.setStorehouseNo(coreStorehouseEntity.getStorehouseno());
        refundsMasterEntity.setStoreHouseName(coreStorehouseEntity.getStorehousename());
        //状态
        refundsMasterEntity.setStatus(RefundsTypeEnum.UNSUBMIT.getKey());
        refundsMasterEntity.setDeptId(deptId);
        refundsMasterEntity.setCreateId(userId);
        refundsMasterEntity.setCreateTime(new Date());
        refundsMasterEntity.setDelFlag(DelFlagEnum.NORMAL.getKey());
        refundsMasterEntity.setDataSource(HospitalDataSource.SYSTEM.getKey());

        List<RefundsDetailEntity> refundsDetailEntityList = new ArrayList<>();
        for (SaveRefundsDetailVo refundsDetailVo : refundsDetailVos) {
            RefundsDetailEntity refundsDetailEntity = new RefundsDetailEntity();
            //退货细单信息设置
            List<CoreSupplyDetailEntity> coreSupplyDetailEntity = coreSupplyDetailService.getList(refundsDetailVo.getSupplyNo(), refundsDetailVo.getGoodsName(),null,refundsDetailVo.getLotNo());
            if (CollectionUtils.isEmpty(coreSupplyDetailEntity)) {
                log.error("---------------------------新增退货单失败,退货细单信息错误或该单号未提交-----------------------");
                map.put("errmessage", "退货细单信息错误或该供货单号未提交");
                return map;
            }
            refundsDetailEntity.setMasterId(refundsMasterVo.getId());
            refundsDetailEntity.setSupplyDetailId(coreSupplyDetailEntity.get(0).getSupplyDetailId().toString());
            refundsDetailEntity.setSupplyMasterId(coreSupplyDetailEntity.get(0).getSupplyMasterId());

            //查询原医院商品信息,平台医院商品信息
            Map<String, Object> matchMap = getMaster(refundsMasterVo.getHospitalId(),
                    refundsMasterVo.getSupplierId(), refundsDetailVo.getGoodsType(),
                    refundsDetailVo.getGoodsId(), refundsDetailVo.getSpecsId());
            if (matchMap.isEmpty()) {
                log.error("---------------------------新增退货单失败,原商品信息匹对错误-----------------------");
                map.put("errmessage", "原商品信息匹对错误");
                return map;
            }
            refundsDetailEntity.setGoodsType(Integer.parseInt(matchMap.get("goods_type").toString()));
            refundsDetailEntity.setSourcesGoodsName(matchMap.get("hospital_goods_name").toString());
            refundsDetailEntity.setSourcesSpecsId(matchMap.get("hospital_sources_specs_id").toString());
            refundsDetailEntity.setSourcesSpecsCode(matchMap.get("hospital_goods_specs_code").toString());
            refundsDetailEntity.setSourcesSpecsName(matchMap.get("hospital_goods_specs_name").toString());
            refundsDetailEntity.setGoodsId(Long.parseLong(matchMap.get("hospital_goods_id").toString()));
            refundsDetailEntity.setGoodsCode(matchMap.get("hospital_goods_code").toString());
            refundsDetailEntity.setGoodsName(matchMap.get("hospital_goods_name").toString());
            refundsDetailEntity.setSpecsId(Long.parseLong(matchMap.get("hospital_goods_specs_id").toString()));
            refundsDetailEntity.setSpecsCode(matchMap.get("hospital_goods_specs_code").toString());
            refundsDetailEntity.setSpecsName(matchMap.get("hospital_goods_specs_name").toString());


            refundsDetailEntity.setLotId(refundsDetailVo.getLotId());
            refundsDetailEntity.setLotNo(refundsDetailVo.getLotNo());
            refundsDetailEntity.setDeptId(deptId);
            refundsDetailEntity.setCreateId(userId);
            refundsDetailEntity.setCreateTime(new Date());
            refundsDetailEntity.setDelFlag(DelFlagEnum.NORMAL.getKey());
            refundsDetailEntity.setSupplyno(refundsDetailVo.getSupplyNo());
            //申请退货数量
            //refundsDetailEntity.setApplyRefundsNumber(refundsDetailVo.getApplyRefundsNumber());

            refundsDetailEntity.setRefundsRemark(refundsDetailVo.getRefundsRemark());

            refundsDetailEntity.setGoodsUnitCode(refundsDetailVo.getGoodsUnit());
            //校验 退货单数量应小于等于供货数量
            if (refundsDetailVo.getRefundsNumber() <= coreSupplyDetailEntity.get(0).getSupplyQty()) {
                refundsDetailEntity.setRealityRefundsNumber(refundsDetailVo.getRefundsNumber());
            } else {
                map.put("errmessage", "退货单数量应小于等于供货数量");
                return map;
            }
            refundsDetailEntity.setRefundsPrice(refundsDetailVo.getRefundsPrice());
            refundsDetailEntityList.add(refundsDetailEntity);
        }
        this.baseMapper.insert(refundsMasterEntity);
        for (RefundsDetailEntity refundsDetailVo : refundsDetailEntityList) {
            refundsDetailVo.setMasterId(refundsMasterEntity.getId());
        }
        refundsDetailService.insertBatch(refundsDetailEntityList);
        return map;

    }
    /**
     * 更新退货单
     *
     * @param deptId
     * @param userId
     * @param refundsMasterVo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map updateRefunds(Long deptId, Long userId, SaveRefundsMasterVo refundsMasterVo) {

        Long refundsMasterId = refundsMasterVo.getRefundsMasterId();
        refundsDetailService.delete(new EntityWrapper<RefundsDetailEntity>().eq("master_id", refundsMasterId));
        this.baseMapper.delete(new EntityWrapper<RefundsMasterEntity>().eq("id", refundsMasterId));

        List<SaveRefundsDetailVo> refundsDetailVos = refundsMasterVo.getSaveRefundsDetailVoList();
        Map<String, Object> map = new HashMap<>(16);
        //查询医院信息
        OrgHospitalInfoEntity hospitalInfo = orgHospitalInfoService.selectOne(new EntityWrapper<OrgHospitalInfoEntity>().eq("id", refundsMasterVo.getHospitalId()));
        if (hospitalInfo == null) {
            log.error("---------------------------修改退货单失败,医院名称为空-----------------------");
            map.put("errmessage", "医院信息为空");
            return map;
        }
        //查询平台供应商信息
        List<OrgSupplierInfoEntity> supplierInfo = orgSupplierInfoService.selectByDeptId(deptId);
        if (CollectionUtils.isEmpty(supplierInfo)) {
            log.error("---------------------------修改退货单失败,供应商数据为空-----------------------");
            map.put("errmessage", "供应商数据为空");
            return map;
        }
        //退货主单信息设置
        RefundsMasterEntity refundsMasterEntity = new RefundsMasterEntity();
        refundsMasterEntity.setStoreHouseName(refundsMasterVo.getStoreHouseName());

        refundsMasterEntity.setRefundsNo(refundsMasterVo.getRefundsNo());
        //退货单号唯一问题
        Integer  exist = this.baseMapper.selectByRefundsNo(refundsMasterVo.getRefundsNo());
        if(exist != 0){
            map.put("errmessage","已存在该退货单号");
            return map;
        }else {
            refundsMasterEntity.setRefundsNo(refundsMasterVo.getRefundsNo());
        }
        refundsMasterEntity.setRefundsApplyNo(refundsMasterVo.getRefundsApplyNo());
        refundsMasterEntity.setRefundsTime(refundsMasterVo.getRefundsTime());
        //销退单号
        refundsMasterEntity.setRegressionNumber(refundsMasterVo.getRefundsNo());

        refundsMasterEntity.setHospitalId(hospitalInfo.getId());
        refundsMasterEntity.setHospitalName(hospitalInfo.getHospitalName());
        refundsMasterEntity.setHospitalCode(hospitalInfo.getHospitalCode());

        refundsMasterEntity.setSupplierId(supplierInfo.get(0).getId());
        refundsMasterEntity.setSupplierCode(supplierInfo.get(0).getSupplierCode());
        refundsMasterEntity.setSupplierName(supplierInfo.get(0).getSupplierName());

        //查询原医院，原供应商信息
        UnicodeSupplyShipEntity shipEntity = unicodeSupplyShipService.selectBySupplierIdAndHospitalId(refundsMasterEntity.getSupplierId(), refundsMasterEntity.getHospitalId());
        if (shipEntity == null) {
            log.error("---------------------------修改退货单失败,供应商医院配对失败-----------------------");
            map.put("errmessage", "供应商医院配对失败");
            return map;
        }

        refundsMasterEntity.setSourcesHospitalId(shipEntity.getSourcesHospitalId());
        refundsMasterEntity.setSourcesHospitalCode(shipEntity.getSourcesHospitalCreditCode());
        refundsMasterEntity.setSourcesHospitalName(shipEntity.getSourcesHospitalName());
        refundsMasterEntity.setSourcesSupplierId(shipEntity.getSourcesSupplierId());
        refundsMasterEntity.setSourcesSupplierCode(shipEntity.getSourcesSupplierCreditCode());
        refundsMasterEntity.setSourcesSupplierName(shipEntity.getSourcesSupplierName());

        //关联查询库房
        CoreStorehouseEntity coreStorehouseEntity = coreStorehouseService
                .selectOne(new EntityWrapper<CoreStorehouseEntity>().eq("horg_id", hospitalInfo.getId()).eq("storehouseid", refundsMasterVo.getStoreHouseId()));
        if (coreStorehouseEntity == null) {
            log.error("---------------------------修改退货单失败,医院库房匹对失败-----------------------");
            map.put("errmessage", "医院库房匹对失败");
            return map;
        }
        refundsMasterEntity.setSourcesStoreHouseId(coreStorehouseEntity.getOrgdataid());
        refundsMasterEntity.setStoreHouseId(coreStorehouseEntity.getStorehouseid());
        refundsMasterEntity.setStorehouseNo(coreStorehouseEntity.getStorehouseno());


        //状态
        refundsMasterEntity.setStatus(RefundsTypeEnum.UNSUBMIT.getKey());
        refundsMasterEntity.setDeptId(deptId);
        refundsMasterEntity.setCreateId(userId);
        refundsMasterEntity.setCreateTime(new Date());
        refundsMasterEntity.setDelFlag(DelFlagEnum.NORMAL.getKey());
        refundsMasterEntity.setDataSource(HospitalDataSource.SYSTEM.getKey());

        refundsMasterEntity.setEditTime(new Date());
        refundsMasterEntity.setEditId(userId);
        List<RefundsDetailEntity> refundsDetailEntityList = new ArrayList<>();
        for (SaveRefundsDetailVo refundsDetailVo : refundsDetailVos) {
            RefundsDetailEntity refundsDetailEntity = new RefundsDetailEntity();
            //退货细单信息设置
            List<CoreSupplyDetailEntity> coreSupplyDetailEntity = coreSupplyDetailService.getList(refundsDetailVo.getSupplyNo(), refundsDetailVo.getGoodsName(),null,refundsDetailVo.getLotNo());
            if (CollectionUtils.isEmpty(coreSupplyDetailEntity)) {
                log.error("---------------------------修改退货单失败,退货细单信息错误或该单号未提交-----------------------");
                map.put("errmessage", "退货细单信息错误或该单号未提交");
                return map;
            }

            refundsDetailEntity.setMasterId(refundsMasterVo.getId());
            refundsDetailEntity.setSupplyDetailId(coreSupplyDetailEntity.get(0).getSupplyDetailId().toString());
            refundsDetailEntity.setSupplyMasterId(coreSupplyDetailEntity.get(0).getSupplyMasterId());

            //查询原医院商品信息,平台医院商品信息
            Map<String, Object> matchMap = getMaster(refundsMasterVo.getHospitalId(),
                    refundsMasterVo.getSupplierId(), refundsDetailVo.getGoodsType(),
                    refundsDetailVo.getGoodsId(), refundsDetailVo.getSpecsId());
            if (matchMap.isEmpty()) {
                log.error("---------------------------修改退货单失败,原商品信息匹对错误-----------------------");
                map.put("errmessage", "原商品信息匹对错误");
                return map;
            }

            refundsDetailEntity.setGoodsType(Integer.parseInt(matchMap.get("goods_type").toString()));
            refundsDetailEntity.setSourcesGoodsName(matchMap.get("hospital_goods_name").toString());
            refundsDetailEntity.setSourcesSpecsId(matchMap.get("hospital_sources_specs_id").toString());
            refundsDetailEntity.setSourcesSpecsCode(matchMap.get("hospital_goods_specs_code").toString());
            refundsDetailEntity.setSourcesSpecsName(matchMap.get("hospital_goods_specs_name").toString());
            refundsDetailEntity.setGoodsId(Long.parseLong(matchMap.get("hospital_goods_id").toString()));
            refundsDetailEntity.setGoodsCode(matchMap.get("hospital_goods_code").toString());
            refundsDetailEntity.setGoodsName(matchMap.get("hospital_goods_name").toString());
            refundsDetailEntity.setSpecsId(Long.parseLong(matchMap.get("hospital_goods_specs_id").toString()));
            refundsDetailEntity.setSpecsCode(matchMap.get("hospital_goods_specs_code").toString());
            refundsDetailEntity.setSpecsName(matchMap.get("hospital_goods_specs_name").toString());
            refundsDetailEntity.setLotId(refundsDetailVo.getLotId());

            refundsDetailEntity.setDeptId(deptId);
            refundsDetailEntity.setCreateId(userId);
            refundsDetailEntity.setCreateTime(new Date());
            refundsDetailEntity.setDelFlag(DelFlagEnum.NORMAL.getKey());
            refundsDetailEntity.setSupplyno(refundsDetailVo.getSupplyNo());
            //申请退货数量
            //refundsDetailEntity.setApplyRefundsNumber(refundsDetailVo.getApplyRefundsNumber());
            refundsDetailEntity.setRealityRefundsNumber(refundsDetailVo.getRealityRefundsNumber());
            refundsDetailEntity.setRefundsRemark(refundsDetailVo.getRefundsRemark());
            refundsDetailEntity.setRefundsPrice(refundsDetailVo.getRefundsPrice());
            refundsDetailEntity.setEditTime(new Date());
            refundsDetailEntity.setEditId(userId);
            refundsDetailEntity.setGoodsUnitCode(refundsDetailVo.getGoodsUnit());
            refundsDetailEntity.setRealityRefundsNumber(refundsDetailVo.getRefundsNumber());
            //校验 退货单数量应小于等于供货数量
            if(refundsDetailVo.getRefundsNumber()>coreSupplyDetailEntity.get(0).getSupplyQty()){
                map.put("errmessage", "退货单数量应小于等于供货数量");
                return map;
            }else {
                refundsDetailEntity.setRealityRefundsNumber(refundsDetailVo.getRefundsNumber());
            }
            //校验 退货单数量应小于等于供货数量
            if (refundsDetailVo.getRefundsNumber() <= coreSupplyDetailEntity.get(0).getSupplyQty()) {
                refundsDetailEntity.setRealityRefundsNumber(refundsDetailVo.getRefundsNumber());
            } else {
                map.put("errmessage", "退货单数量应小于等于供货数量");
                return map;
            }
            refundsDetailEntity.setLotNo(refundsDetailVo.getLotNo());
            refundsDetailEntityList.add(refundsDetailEntity);
        }
        this.baseMapper.insert(refundsMasterEntity);
        for (RefundsDetailEntity refundsDetailVo : refundsDetailEntityList) {
            refundsDetailVo.setMasterId(refundsMasterEntity.getId());
        }
        refundsDetailService.insertBatch(refundsDetailEntityList);
        return map;

    }


    @Override
    public List<ReturnDetailVo> returnDetails(Map<String, Object> params) {
        List<ReturnDetailVo> returnDetailVoList = refundsMasterDao.getDetailAndFactoryName(params.get("supplyNo").toString());
        return returnDetailVoList;
    }

    @Override
    public List<ReturnSupplyVo> returnSupplyNo(Long deptId, Map<String, Object> params) {
        if (StringUtil.isEmpty(params.get("supplyNo").toString())) {
            List<ReturnSupplyVo> supplyNoList = new ArrayList<>(16);
            List<CoreSupplyMasterEntity> coreSupplyMasterEntities = coreSupplyMasterService.selectList(new EntityWrapper<CoreSupplyMasterEntity>()
                    .eq("horg_id", params.get("hospitalId"))
                    .eq("storehouseid", params.get("storeHouseId"))
                    .eq("dept_id", deptId)
                    .eq("supply_status", SupplyStatusEnum.COMMITTED.getKey())
                    .eq("del_flag", DelFlagEnum.NORMAL.getKey()));
            for (CoreSupplyMasterEntity coreSupplyMasterEntity : coreSupplyMasterEntities) {
                ReturnSupplyVo returnSupplyVo = new ReturnSupplyVo();
                returnSupplyVo.setSupplyNo(coreSupplyMasterEntity.getSupplyno());
                returnSupplyVo.setSupplyMasterId(coreSupplyMasterEntity.getSupplyMasterId());
                supplyNoList.add(returnSupplyVo);
            }
            return supplyNoList;
        } else {
            List<ReturnSupplyVo> supplyNoList = new ArrayList<>(16);
            List<CoreSupplyMasterEntity> coreSupplyMasterEntities = coreSupplyMasterService.selectList(new EntityWrapper<CoreSupplyMasterEntity>()
                    .eq("horg_id", params.get("hospitalId"))
                    .eq("storehouseid", params.get("storeHouseId"))
                    .eq("dept_id", deptId)
                    .like("supplyno", params.get("supplyNo").toString())
                    .eq("supply_status", SupplyStatusEnum.COMMITTED.getKey())
                    .eq("del_flag", DelFlagEnum.NORMAL.getKey()));
            for (CoreSupplyMasterEntity coreSupplyMasterEntity : coreSupplyMasterEntities) {
                ReturnSupplyVo returnSupplyVo = new ReturnSupplyVo();
                returnSupplyVo.setSupplyNo(coreSupplyMasterEntity.getSupplyno());
                returnSupplyVo.setSupplyMasterId(coreSupplyMasterEntity.getSupplyMasterId());
                supplyNoList.add(returnSupplyVo);
            }
            return supplyNoList;
        }
    }

    @Override
    public List<Map<String, Object>> returnGoodsSpecs( Map<String, Object> params) {
        String supplyNo = params.get("supplyNo").toString();
        Integer goodsId = Integer.parseInt(params.get("goodsId").toString());
        Integer goodsClass = Integer.parseInt(params.get("goodsClass").toString());
        List<Map<String,Object>> specsMap = refundsMasterDao.getGoodsSpecsMap(supplyNo,goodsId,goodsClass);
        if (CollectionUtils.isEmpty(specsMap)){
            log.error("----------------查询规格列表失败--------------");
            throw new HdiException("查询规格列表失败");
        }
        return specsMap;
    }

    @Override
    public List<Map<String, Object>> returnLot(Map<String, Object> params) {
        String supplyNo = params.get("supplyNo").toString();
        Integer goodsId = Integer.parseInt(params.get("goodsId").toString());
        Integer goodsClass = Integer.parseInt(params.get("goodsClass").toString());
        Integer specsId = Integer.parseInt(params.get("specsId").toString());
        List<Map<String,Object>> lotMap = refundsMasterDao.getLotMap(supplyNo,goodsId,goodsClass,specsId);
        if (CollectionUtils.isEmpty(lotMap)){
            log.error("----------------查询生产批号列表失败--------------");
            throw new HdiException("查询生产批号列表失败");
        }
        return lotMap;
    }

    @Override
    public void deleteRefunds(Long[] ids) {
        for (Long id : ids) {
            RefundsMasterEntity refundsMasterEntity = new RefundsMasterEntity();
            refundsMasterEntity.setId(id);
            List<RefundsDetailEntity> refundsDetailEntityList = refundsMasterDao.selectDetail(refundsMasterEntity.getId());
            for (RefundsDetailEntity refundsDetailEntity : refundsDetailEntityList) {
                refundsDetailEntity.setDelFlag(DelFlagEnum.DELETE.getKey());
                refundsDetailService.updateById(refundsDetailEntity);
            }
            refundsMasterEntity.setDelFlag(DelFlagEnum.DELETE.getKey());
            this.updateById(refundsMasterEntity);
        }
    }
}