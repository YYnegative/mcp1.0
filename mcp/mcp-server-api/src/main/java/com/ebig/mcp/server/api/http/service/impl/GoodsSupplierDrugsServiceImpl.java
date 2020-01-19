package com.ebig.mcp.server.api.http.service.impl;

import com.ebig.hdi.common.entity.GoodsSupplierDrugsSpecsEntity;
import com.ebig.hdi.common.entity.OrgFactoryInfoEntity;
import com.ebig.hdi.common.entity.OrgSupplierInfoEntity;
import com.ebig.hdi.common.enums.StatusEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.mcp.server.api.aspect.Log;
import com.ebig.mcp.server.api.http.dao.SupplierDrugsDao;
import com.ebig.mcp.server.api.http.dao.SupplierDrugsSpecsDao;
import com.ebig.mcp.server.api.http.service.GoodsSupplierDrugsService;
import com.ebig.mcp.server.api.http.service.OrgFactoryInfoService;
import com.ebig.mcp.server.api.http.vo.GoodsSupplierDrugsVo;
import com.ebig.mcp.server.api.http.vo.GoodsSupplierVo;
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
 * @description:供应商药品服务实现类
 * @author: wenchao
 * @time: 2019-10-15 15:37
 */
@Service
@Slf4j
public class GoodsSupplierDrugsServiceImpl implements GoodsSupplierDrugsService {

    /**
     * 每5000条数据执行一次
     */
    private static final Integer INIT_COUNT = 5000;

    @Autowired
    private OrgFactoryInfoService orgFactoryInfoService;

    @Autowired
    private SupplierDrugsDao dao;

    @Autowired
    private SupplierDrugsSpecsDao supplierDrugsSpecsDao;
    @Autowired
    private TransactionManager manager;

    @Override
    @Log(title = "供应商商品模块", action = "供应商药品数据上传")
    public void saveBatch(List<GoodsSupplierDrugsVo> list, OrgSupplierInfoEntity supplierInfo) {
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
            throw new HdiException("药品数据上传失败", e);
        }
    }

    /**
     * 保存
     *
     * @param subList
     * @param supplierInfo
     */
    private void save(List<GoodsSupplierDrugsVo> subList, OrgSupplierInfoEntity supplierInfo) {
        //根据厂商名称去重
        List<String> factoryNameList = subList.stream().map(GoodsSupplierDrugsVo::getFactoryName).collect(Collectors.toList());
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
        //查询已经存在的药品数据
        List<GoodsSupplierDrugsVo> updateDrugs = dao.getList(subList, supplierInfo);
        final List<GoodsSupplierDrugsVo> finalUpdateDrugs = updateDrugs;
        List<GoodsSupplierDrugsVo> insertDrugs = subList.stream().filter(t -> !finalUpdateDrugs.contains(t)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(insertDrugs)) {
            //插入药品数据(药品数据去重)
            List<GoodsSupplierDrugsVo> distinct1 = dao.insert(insertDrugs.stream().distinct().collect(Collectors.toList()), supplierInfo, map);
            //药品id赋值,插入规格数据
            List<GoodsSupplierDrugsSpecsEntity> list1 = structSpecsList(list1ToList2(insertDrugs, distinct1), supplierInfo);
            supplierDrugsSpecsDao.insertSpecs(list1.stream().distinct().collect(Collectors.toList()));
        }
        if (!CollectionUtils.isEmpty(updateDrugs)) {
            //更新药品数据 (药品数据去重)
            List<GoodsSupplierDrugsVo> distinct2 = dao.update(updateDrugs.stream().distinct().collect(Collectors.toList()), supplierInfo, map);
            //药品id赋值,更新药品数据后,更新或保存规格
            List<GoodsSupplierDrugsSpecsEntity> specsList = structSpecsList(list1ToList2(updateDrugs, distinct2), supplierInfo);
            List<GoodsSupplierDrugsSpecsEntity> list1 = supplierDrugsSpecsDao.getNotExistSpecs(specsList);
            supplierDrugsSpecsDao.insertSpecs(list1.stream().distinct().collect(Collectors.toList()));
            List<GoodsSupplierDrugsSpecsEntity> list2 = specsList.stream().filter(t -> !list1.contains(t)).collect(Collectors.toList());
            supplierDrugsSpecsDao.updateSpecs(list2.stream().distinct().collect(Collectors.toList()));
        }
    }

    /**
     * 将list2的id赋值给list1的id
     *
     * @param list1
     * @param list2
     * @return list1
     */
    private List<GoodsSupplierDrugsVo> list1ToList2(List<GoodsSupplierDrugsVo> list1, List<GoodsSupplierDrugsVo> list2) {
        for (GoodsSupplierDrugsVo vo : list1) {
            for (GoodsSupplierDrugsVo d : list2) {
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
    private List<GoodsSupplierDrugsSpecsEntity> structSpecsList(List<GoodsSupplierDrugsVo> list, OrgSupplierInfoEntity supplierInfo) {
        List<GoodsSupplierDrugsSpecsEntity> specsList = new ArrayList<>(list.size());
        GoodsSupplierDrugsSpecsEntity specsEntity;
        for (GoodsSupplierVo vo : list) {
            //封装规格数据
            specsEntity = new GoodsSupplierDrugsSpecsEntity();
            specsEntity.setDrugsId(vo.getId());
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
}
