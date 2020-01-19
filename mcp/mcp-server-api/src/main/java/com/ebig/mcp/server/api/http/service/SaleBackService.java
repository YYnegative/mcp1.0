package com.ebig.mcp.server.api.http.service;

import com.ebig.hdi.common.entity.MasterDetailsCommonEntity;
import com.ebig.mcp.server.api.http.vo.SaleBackVo;
import org.springframework.stereotype.Service;

import java.util.List;


public interface SaleBackService {

    List<MasterDetailsCommonEntity> downloadsaleback(SaleBackVo saleBackVo);
}
