package com.ebig.hdi.modules.core.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.common.entity.UnicodeGoodsShipApprovalEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author：wenchao
 * @date：2019-12-23 16:25
 * @version：V1.0
 */
public interface UnicodeGoodsShipApprovalDao  extends BaseMapper<UnicodeGoodsShipApprovalEntity> {
    /**
     * 获取count条未匹配的记录
     * @param limit 获取的记录数
     * @return
     */
    List<UnicodeGoodsShipApprovalEntity> selectNotMatch(@Param("limit") Integer limit);
}
