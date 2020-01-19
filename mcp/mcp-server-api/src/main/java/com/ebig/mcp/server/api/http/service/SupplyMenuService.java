package com.ebig.mcp.server.api.http.service;

import java.util.List;
import java.util.Map;

import com.ebig.hdi.common.entity.MasterDetailsCommonEntity;
import com.ebig.mcp.server.api.http.entity.CoreSupplyDetailEntity;
import com.ebig.mcp.server.api.http.entity.CoreSupplyMasterEntity;


public interface SupplyMenuService {

   Map<String,Object> uploadsupplymenu(List<MasterDetailsCommonEntity<CoreSupplyMasterEntity, CoreSupplyDetailEntity>> list);
}
