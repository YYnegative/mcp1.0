package com.ebig.mcp.server.api.http.service.impl;


import com.ebig.hdi.common.entity.GoodsSupplierConsumablesApprovalsEntity;
import com.ebig.hdi.common.entity.GoodsSupplierConsumablesSpecsEntity;
import com.ebig.hdi.common.entity.OrgFactoryInfoEntity;
import com.ebig.hdi.common.entity.OrgSupplierInfoEntity;
import com.ebig.hdi.common.enums.StatusEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.mcp.server.api.aspect.Log;
import com.ebig.mcp.server.api.http.dao.SupplierConsumablesApprovalsDao;
import com.ebig.mcp.server.api.http.dao.SupplierConsumablesDao;
import com.ebig.mcp.server.api.http.dao.SupplierConsumablesSpecsDao;
import com.ebig.mcp.server.api.http.service.GoodsSupplierConsumablesService;
import com.ebig.mcp.server.api.http.service.OrgFactoryInfoService;
import com.ebig.mcp.server.api.http.vo.GoodsSupplierConsumablesVo;
import com.ebig.mcp.server.api.transaction.TransactionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @description:供应商耗材服务实现类
 * @author: wenchao
 * @time: 2019-10-15 15:37
 */
@Service
@Slf4j
public class GoodsSupplierConsumablesImpl implements GoodsSupplierConsumablesService {
    /**
     * 每5000条数据执行一次
     */
    private static final Integer COUNT = 5000;

    @Autowired
    private OrgFactoryInfoService orgFactoryInfoService;

    @Autowired
    private TransactionManager manager;

    @Autowired
    private SupplierConsumablesSpecsDao consumablesSpecsDao;

    @Autowired
    private SupplierConsumablesDao supplierConsumablesDao;


    @Autowired
    private SupplierConsumablesApprovalsDao approvalsDao;

    @Override
    @Log(title = "供应商商品模块", action = "供应商耗材数据上传")
    public void batchSave(List<GoodsSupplierConsumablesVo> list, OrgSupplierInfoEntity supplierInfo) {
        //数据拆分
        int listSize = list.size();
        int toIndex = COUNT;
        TransactionStatus status = null;
        try {
            for (int i = 0; i < listSize; i += COUNT) {
                //开启事务
                status = manager.begin();
                //作用为toIndex最后没有INIT_COUNT条数据则剩余几条就装几条
                if (i + COUNT > listSize) {
                    toIndex = listSize - i;
                }
                save(list.subList(i, i + toIndex), supplierInfo);
                manager.commit(status);
            }

        } catch (Exception e) {
            manager.rollback(status);
            log.error(e.getMessage());
            throw new HdiException("耗材数据上传失败", e);
        }
    }

    /**
     * 保存
     *
     * @param subList
     * @param supplierInfo
     */
    private void save(List<GoodsSupplierConsumablesVo> subList, OrgSupplierInfoEntity supplierInfo) {
        //根据厂商名称去重
        List<String> factoryNameList = subList.stream().map(GoodsSupplierConsumablesVo::getFactoryName).collect(Collectors.toList());
        factoryNameList = factoryNameList.stream().distinct().collect(Collectors.toList());
        List<OrgFactoryInfoEntity> factoryInfoList = orgFactoryInfoService.structOrgFactoryInfoList(factoryNameList);
        //查询已经存在的厂商
        List<OrgFactoryInfoEntity> existList = orgFactoryInfoService.getExistedFactoryInfo(factoryNameList);
        //将不存在的厂商保存
        List<OrgFactoryInfoEntity> insertList = factoryInfoList.stream().filter(t -> !existList.contains(t)).collect(Collectors.toList());
        //插入厂商数据
        if (!CollectionUtils.isEmpty(insertList)){
            insertList = orgFactoryInfoService.insertBatch(insertList);
        }
        factoryInfoList = new ArrayList<>(subList.size());
        factoryInfoList.addAll(existList);
        factoryInfoList.addAll(insertList);
        Map<String, Long> map = factoryInfoList.stream().collect(Collectors.toMap(OrgFactoryInfoEntity::getFactoryName, OrgFactoryInfoEntity::getId));
        //查询已经存在的耗材数据
        List<GoodsSupplierConsumablesVo> updateConsumables = supplierConsumablesDao.getGoodsSupplierConsumablesVos(subList, supplierInfo);
        final List<GoodsSupplierConsumablesVo> finalUpdateConsumables = updateConsumables;
        List<GoodsSupplierConsumablesVo> insertConsumables = subList.stream().filter(t -> !finalUpdateConsumables.contains(t)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(insertConsumables)) {
            //插入耗材数据(耗材数据去重)
            List<GoodsSupplierConsumablesVo> distinct1 = supplierConsumablesDao.insert(insertConsumables.stream().distinct().collect(Collectors.toList()), supplierInfo, map);
            //耗材id赋值,插入规格数据
            List<GoodsSupplierConsumablesSpecsEntity> list1 = structSpecsList(list1ToList2(insertConsumables, distinct1), supplierInfo);
            consumablesSpecsDao.insertSpecs(list1.stream().distinct().collect(Collectors.toList()));
            //插入批准文号
            List<GoodsSupplierConsumablesApprovalsEntity> list3 = this.structureApprovalsList(list1ToList2(insertConsumables, distinct1), supplierInfo);
            approvalsDao.insertBatchApprovals(list3.stream().distinct().collect(Collectors.toList()));
        }
        if (!CollectionUtils.isEmpty(updateConsumables)) {
            //更新耗材数据 (耗材数据去重)
            List<GoodsSupplierConsumablesVo> distinct2 = supplierConsumablesDao.update(updateConsumables.stream().distinct().collect(Collectors.toList()), supplierInfo, map);
            //耗材id赋值,更新耗材数据后,更新或保存规格
            List<GoodsSupplierConsumablesSpecsEntity> specsList = structSpecsList(list1ToList2(updateConsumables, distinct2), supplierInfo);
            List<GoodsSupplierConsumablesSpecsEntity> list1 = consumablesSpecsDao.getNotExistSpecs(specsList);
            consumablesSpecsDao.insertSpecs(list1.stream().distinct().collect(Collectors.toList()));
            final List<GoodsSupplierConsumablesSpecsEntity> finalList = list1;
            List<GoodsSupplierConsumablesSpecsEntity> list2 = specsList.stream().filter(t -> !finalList.contains(t)).collect(Collectors.toList());
            consumablesSpecsDao.updateSpecs(list2.stream().distinct().collect(Collectors.toList()));
            //更新耗材数据后查询不存在的批准文号,插入批准文号
            approvalsDao.insertBatchApprovals(this.structureApprovalsList(approvalsDao.getApprovals(list1ToList2(updateConsumables, distinct2)), supplierInfo));
        }

    }

    /**
     * 将list2的id赋值给list1的id
     *
     * @param list1
     * @param list2
     * @return list1
     */
    private List<GoodsSupplierConsumablesVo> list1ToList2(List<GoodsSupplierConsumablesVo> list1, List<GoodsSupplierConsumablesVo> list2) {
        for (GoodsSupplierConsumablesVo vo : list1) {
            for (GoodsSupplierConsumablesVo d : list2) {
                if (vo.equals(d)) {
                    vo.setId(d.getId());
                }
            }

        }
        return list1;
    }

    /**
     * 将规格数据提取并封装成规格对象
     *
     * @param list         包含规格数据的集合
     * @param supplierInfo 供应商信息
     * @return 规格对象集合
     */
    private List<GoodsSupplierConsumablesSpecsEntity> structSpecsList(List<GoodsSupplierConsumablesVo> list, OrgSupplierInfoEntity supplierInfo) {
        List<GoodsSupplierConsumablesSpecsEntity> specsList = new ArrayList<>(list.size());
        GoodsSupplierConsumablesSpecsEntity specsEntity;
        for (GoodsSupplierConsumablesVo vo : list) {
            //封装规格数据
            specsEntity = new GoodsSupplierConsumablesSpecsEntity();
            specsEntity.setConsumablesId(vo.getId());
            specsEntity.setGuid(vo.getGuid());
            specsEntity.setSpecs(vo.getSpecs());
            specsEntity.setSpecsCode(vo.getSpecsCode());
            specsEntity.setSourcesSpecsId(UUID.randomUUID().toString());
            specsEntity.setCreateId(supplierInfo.getId());
            specsEntity.setStatus(StatusEnum.USABLE.getKey());
            specsEntity.setEditId(supplierInfo.getId());
            specsList.add(specsEntity);
        }
        return specsList;
    }

    /**
     * 将批文数据提取并封装成批文对象
     *
     * @param list 包含批文数据的集合
     * @return 批文对象集合
     */
    private List<GoodsSupplierConsumablesApprovalsEntity> structureApprovalsList(List<GoodsSupplierConsumablesVo> list, OrgSupplierInfoEntity supplierInfo) {
        List<GoodsSupplierConsumablesApprovalsEntity> approvalsList = new ArrayList<>(list.size());
        GoodsSupplierConsumablesApprovalsEntity approvalsEntity;
        for (GoodsSupplierConsumablesVo vo : list) {
            //封装批准文号
            approvalsEntity = new GoodsSupplierConsumablesApprovalsEntity();
            approvalsEntity.setCreateId(supplierInfo.getId());
            approvalsEntity.setConsumablesId(vo.getId());
            approvalsEntity.setApprovals(vo.getApprovals());
            approvalsEntity.setStatus(StatusEnum.USABLE.getKey());
            approvalsList.add(approvalsEntity);
        }
        return approvalsList;
    }

}
