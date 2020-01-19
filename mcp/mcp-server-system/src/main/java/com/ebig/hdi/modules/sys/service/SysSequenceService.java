package com.ebig.hdi.modules.sys.service;

import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.sys.entity.SysSequenceEntity;

/**
 * 系统序列
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-04-24 13:06:34
 */
public interface SysSequenceService extends IService<SysSequenceEntity> {

    PageUtils queryPage(Map<String, Object> params);

    String selectSeqValueBySeqCode(String seqCode);
}

