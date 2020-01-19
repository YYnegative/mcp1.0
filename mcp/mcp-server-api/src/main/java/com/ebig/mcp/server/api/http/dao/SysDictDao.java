package com.ebig.mcp.server.api.http.dao;

import com.ebig.mcp.server.api.http.entity.SysDictEntity;

import java.util.List;

/**
 * @description: 字典持久化类
 * @author: wenchao
 * @time: 2019-10-26 15:36
 */
public interface SysDictDao {

    /**
     * 根据类型查询
     * @param type
     * @return
     */
    List<SysDictEntity> selectDictByType(String type);

    String insert(SysDictEntity entity);
}
