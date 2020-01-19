package com.ebig.mcp.server.api.http.service.impl;

import com.ebig.hdi.common.entity.GoodsSupplierReagentSpecsEntity;
import com.ebig.hdi.common.entity.OrgFactoryInfoEntity;
import com.ebig.hdi.common.entity.OrgSupplierInfoEntity;
import com.ebig.hdi.common.enums.StatusEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.mcp.server.api.aspect.Log;
import com.ebig.mcp.server.api.http.dao.SupplierReagentDao;
import com.ebig.mcp.server.api.http.dao.SupplierReagentSpecsDao;
import com.ebig.mcp.server.api.http.service.GoodsSupplierReagentService;
import com.ebig.mcp.server.api.http.service.OrgFactoryInfoService;
import com.ebig.mcp.server.api.http.vo.GoodsSupplierReagentVo;
import com.ebig.mcp.server.api.http.vo.GoodsSupplierVo;
import com.ebig.mcp.server.api.transaction.TransactionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @description:供应商试剂服务实现类
 * @author: wenchao
 * @time: 2019-10-15 15:37
 */
@Service
@Slf4j
public class GoodsSupplierReagentServiceImpl implements GoodsSupplierReagentService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    /**
     * 每5000条数据执行一次
     */
    private static final Integer INIT_COUNT = 5000;

    @Autowired
    private OrgFactoryInfoService orgFactoryInfoService;

    @Autowired
    private TransactionManager manager;

    @Autowired
    private SupplierReagentDao supplierReagentDao;


    @Autowired
    private SupplierReagentSpecsDao supplierReagentSpecsDao;

    @Override
    @Log(title = "供应商商品模块", action = "供应商试剂数据上传")
    public void saveBatch(List<GoodsSupplierReagentVo> list, OrgSupplierInfoEntity supplierInfo) {
        //数据拆分
        int listSize = list.size();
        int toIndex = INIT_COUNT;
        TransactionStatus status = null;
        try {
            for (int i = 0; i < listSize; i += INIT_COUNT) {
                status = manager.begin();
                //作用为toIndex最后没有INIT_COUNT条数据则剩余几条就装几条
                if (i + INIT_COUNT > listSize) {
                    toIndex = listSize - i;
                }
                save(list.subList(i, i + toIndex), supplierInfo);
                manager.commit(status);

            }

        } catch (Exception e) {
            manager.rollback(status);
            log.error(e.getMessage());
            throw new HdiException("试剂数据上传失败", e);
        }

    }

    /**
     * 保存数据
     *
     * @param subList
     */
    private void save(List<GoodsSupplierReagentVo> subList, OrgSupplierInfoEntity supplierInfo) {
        //根据厂商名称去重
        List<String> factoryNameList = subList.stream().map(GoodsSupplierReagentVo::getFactoryName).collect(Collectors.toList());
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
        //查询已经存在的试剂数据
        List<GoodsSupplierReagentVo> updateReagent = supplierReagentDao.getSupplierReagent(subList, supplierInfo);
        final List<GoodsSupplierReagentVo> finalUpdateReagent = updateReagent;
        List<GoodsSupplierReagentVo> insertReagent = subList.stream().filter(t -> !finalUpdateReagent.contains(t)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(insertReagent)) {
            //插入药品数据(药品数据去重)
            List<GoodsSupplierReagentVo> distinct1 = supplierReagentDao.insert(insertReagent.stream().distinct().collect(Collectors.toList()), supplierInfo, map);
            //药品id赋值,插入规格数据
            List<GoodsSupplierReagentSpecsEntity> list1 = structSpecsList(list1ToList2(insertReagent, distinct1), supplierInfo);
            supplierReagentSpecsDao.insertSpecs(list1.stream().distinct().collect(Collectors.toList()));
        }
        if (!CollectionUtils.isEmpty(updateReagent)) {
            //更新药品数据 (药品数据去重)
            List<GoodsSupplierReagentVo> distinct2 = supplierReagentDao.update(updateReagent.stream().distinct().collect(Collectors.toList()), supplierInfo, map);
            //药品id赋值,更新药品数据后,更新或保存规格
            List<GoodsSupplierReagentSpecsEntity> specsList = structSpecsList(list1ToList2(updateReagent, distinct2), supplierInfo);
            List<GoodsSupplierReagentSpecsEntity> list1 = supplierReagentSpecsDao.getNotExistSpecs(specsList);
            supplierReagentSpecsDao.insertSpecs(list1.stream().distinct().collect(Collectors.toList()));
            List<GoodsSupplierReagentSpecsEntity> list2 = specsList.stream().filter(t -> !list1.contains(t)).collect(Collectors.toList());
            supplierReagentSpecsDao.updateSpecs(list2.stream().distinct().collect(Collectors.toList()));
        }
    }

    /**
     * 将list2的id赋值给list1的id
     *
     * @param list1
     * @param list2
     * @return list1
     */
    private List<GoodsSupplierReagentVo> list1ToList2(List<GoodsSupplierReagentVo> list1, List<GoodsSupplierReagentVo> list2) {
        for (GoodsSupplierReagentVo vo : list1) {
            for (GoodsSupplierReagentVo d : list2) {
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
    private List<GoodsSupplierReagentSpecsEntity> structSpecsList(List<GoodsSupplierReagentVo> list, OrgSupplierInfoEntity supplierInfo) {
        List<GoodsSupplierReagentSpecsEntity> specsList = new ArrayList<>(list.size());
        GoodsSupplierReagentSpecsEntity specsEntity;
        for (GoodsSupplierVo vo : list) {
            //封装规格数据
            specsEntity = new GoodsSupplierReagentSpecsEntity();
            specsEntity.setReagenId(vo.getId());
            specsEntity.setGuid(vo.getGuid());
            specsEntity.setSpecs(vo.getSpecs());
            specsEntity.setSourcesSpecsId(UUID.randomUUID().toString());
            specsEntity.setSpecsCode(vo.getSpecsCode());
            specsEntity.setCreateId(supplierInfo.getId());
            specsEntity.setStatus(StatusEnum.USABLE.getKey());
            specsList.add(specsEntity);
        }
        return specsList;
    }

}
