package com.ebig.mcp.server.api.http.service;

import com.ebig.hdi.common.entity.OrgFactoryInfoEntity;

import java.util.List;

/**
 * 厂商服务类
 * @description:
 * @author: wenchao
 * @time: 2019-10-22 14:12
 */
public interface OrgFactoryInfoService {

    /**
     * 获取已经存在的厂商信息
     *
     * @param factoryNameList
     * @return
     */
    List<OrgFactoryInfoEntity> getExistedFactoryInfo(List<String> factoryNameList);

    /**
     * 批量保存厂商信息
     * @param list 厂商数据集合
     * @return 包含id的厂商数据集合
     */
    List<OrgFactoryInfoEntity> insertBatch(List<OrgFactoryInfoEntity> list);

    /**
     * 将厂商数据提取并封装成厂商对象
     * @param list 包含厂商数据的集合
     * @return 厂商对象集合
     */
    List<OrgFactoryInfoEntity> structOrgFactoryInfoList(List<String> list);




}