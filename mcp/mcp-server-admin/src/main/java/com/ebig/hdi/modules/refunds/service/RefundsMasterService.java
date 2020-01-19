package com.ebig.hdi.modules.refunds.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.refunds.entity.RefundsMasterEntity;
import com.ebig.hdi.modules.refunds.entity.vo.*;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;

import java.util.List;
import java.util.Map;

/**
 * 退货单信息
 *
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-05-28 16:24:21
 */
public interface RefundsMasterService extends IService<RefundsMasterEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    RefundsMasterVO selectById(Long id);
    
    void save(RefundsMasterVO refundsMasterVO);
    
    void update(RefundsMasterVO refundsMasterVO);
    
    List<RefundsDetailVO> selectToSave(Map<String,Object> params);

	void submit(Long[] ids, Long userId);

    /**
     * 获取退货单数据
     * @param queryParam
     * @return
     */
    List<Map<String,Object>> getList(Map<String, Object> queryParam);

    /**
     * 批量导入
     * @param rows
     * @return
     */
    Map importData(String[][] rows, SysUserEntity userEntity);

    /**
     * 新增退货单
     * @param refundsMasterVo
     */
    Map saveRefunds(Long deptId,Long userId,SaveRefundsMasterVo refundsMasterVo);

    /**
     * 更新退货单
     * @param deptId
     * @param userId
     * @param refundsMasterVo
     * @return
     */
    Map updateRefunds(Long deptId, Long userId, SaveRefundsMasterVo refundsMasterVo);

    /**
     * 根据供货单返回细单(商品  (商品分类id  + 商品 ID +厂商名+单位code))
     * @param params
     * @return
     */
    List<ReturnDetailVo> returnDetails(Map<String, Object> params);

    /**
     * 返回供货单
     * @param deptId
     * @param params
     * @return
     */
    List<ReturnSupplyVo> returnSupplyNo(Long deptId,Map<String, Object> params);

    /**
     * 返回商品规格Id + 规格名称
     * @param params
     * @return
     */
    List<Map<String, Object>> returnGoodsSpecs(Map<String, Object> params);

    /**
     * 返回批号Id + 名称
     * @param params
     * @return
     */
    List<Map<String, Object>> returnLot(Map<String, Object> params);

    void deleteRefunds(Long[] ids);
}

