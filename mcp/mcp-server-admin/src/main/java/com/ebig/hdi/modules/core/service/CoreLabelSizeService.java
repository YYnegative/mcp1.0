package com.ebig.hdi.modules.core.service;


import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.core.entity.CoreLabelSizeEntity;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2020-01-08 10:08:48
 */
public interface CoreLabelSizeService extends IService<CoreLabelSizeEntity> {

    PageUtils queryPage(Map<String, Object> params);

    CoreLabelSizeEntity getUserDetail(Long userId,Integer typeId);

    CoreLabelSizeEntity selectByUserIdAndTypeId( Map<String, Object> params);

    void insertOrUpdateByUserIdAndTypeId(CoreLabelSizeEntity coreLabelSize, SysUserEntity user);

}

