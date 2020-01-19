package com.ebig.hdi.modules.drugs.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.modules.drugs.dao.OrgFactoryDao;
import com.ebig.hdi.modules.drugs.entity.OrgFactoryEntity;
import com.ebig.hdi.modules.drugs.service.OrgFactoryService;

@Service("orgFactoryService")
public class OrgFactoryServiceImpl extends ServiceImpl<OrgFactoryDao, OrgFactoryEntity> implements OrgFactoryService {

}
