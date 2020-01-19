package com.ebig.hdi.modules.sys.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.Query;
import com.ebig.hdi.modules.sys.dao.SysSequenceDao;
import com.ebig.hdi.modules.sys.entity.SysSequenceEntity;
import com.ebig.hdi.modules.sys.service.SysSequenceService;

@Service("sysSequenceService")
public class SysSequenceServiceImpl extends ServiceImpl<SysSequenceDao, SysSequenceEntity> implements SysSequenceService {

	@Autowired
	private SysSequenceDao sysSequenceDao;
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<SysSequenceEntity> page = this.selectPage(
                new Query<SysSequenceEntity>(params).getPage(),
                new EntityWrapper<SysSequenceEntity>()
        );

        return new PageUtils(page);
    }

	@Override
	public String selectSeqValueBySeqCode(String seqCode) {
		String result = "";
		synchronized(this){
			SysSequenceEntity sysSequence = sysSequenceDao.selectBySeqCode(seqCode);
			//跟新序列号值
			sysSequence.setCurrentVal(sysSequence.getCurrentVal() + 1L);
			this.updateById(sysSequence);
			result = sysSequence.getSeqPrefix() + String.format("%0" + sysSequence.getFormatLength() + "d", sysSequence.getCurrentVal());
		}
		return result;
	}

}
