
package com.ebig.mcp.server.api.http.service.impl;

import com.ebig.mcp.server.api.http.dao.SysDictDao;
import com.ebig.mcp.server.api.http.entity.SysDictEntity;
import com.ebig.mcp.server.api.http.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysDictServiceImpl  implements SysDictService {

	@Autowired
	private SysDictDao sysDictDao;


	@Override
	public List<SysDictEntity> selectDictByType(String type) {
		return sysDictDao.selectDictByType(type);
	}

	@Override
	public String insert(SysDictEntity entity) {
		return sysDictDao.insert(entity);
	}
}
