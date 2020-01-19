package com.ebig.mcp.server.api.http.dao;

import com.ebig.hdi.common.utils.StringUtil;
import com.ebig.mcp.server.api.http.entity.RefundsDetailEntity;
import com.ebig.mcp.server.api.http.entity.RefundsMasterEntity;
import com.ebig.mcp.server.api.http.vo.RefundsDetailVo;
import com.ebig.mcp.server.api.http.vo.RefundsMasterVo;
import com.ebig.mcp.server.api.http.vo.SaleBackVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component
public class SaleBackDao {

    @Autowired
    JdbcTemplate jdbcTemplate;


    /**
     * 根据ERP上传的条件获取对应的退货主单
     * @param saleBackVo
     * @return
     */
    public List<RefundsMasterVo> getRefundsMasterByIf(SaleBackVo saleBackVo){
        StringBuffer sql = new StringBuffer();
        ArrayList<Object> condition = new ArrayList<>();

        sql.append("SELECT distinct rm.id SALBACKSEQ, rm.refunds_no SALBACKNO,rm.sources_supplier_code supplierCode,rm.sources_supplier_name supplierName,");
        sql.append("sm.sourceid supplyid,sm.supplyNo supplyNo,rm.sources_hospital_code hospitalCode,rm.sources_hospital_name hospitalName,");
        sql.append("rm.storehouse_no storeHouseNo,rm.storehouse_name storeHouseName,rm.refunds_time purplanTime,rm.create_time createTime ");
        sql.append("from hdi_refunds_master rm LEFT JOIN hdi_refunds_detail rd on rm.id=rd.master_id and rd.del_flag=0 ");
        sql.append("LEFT JOIN hdi_core_supply_master sm on rd.supply_master_id=sm.supply_master_id and sm.del_flag=0 ");
        sql.append(" where rm.supplier_code=? ");
        condition.add(saleBackVo.getSupplierCode());
        if (!StringUtil.isEmpty(saleBackVo.getStatus())){
            sql.append(" and rm.status=?");
            condition.add(saleBackVo.getStatus());
        }
        if (!StringUtil.isEmpty(saleBackVo.getCreateTime())){
            sql.append(" and rm.create_time=?");
            condition.add(saleBackVo.getCreateTime());
        }else {
            if (!StringUtil.isEmpty(saleBackVo.getStartTime()) && !StringUtil.isEmpty(saleBackVo.getEndTime())){
                sql.append(" and ? < rm.create_time < ?");
                condition.add(saleBackVo.getStartTime());
                condition.add(saleBackVo.getEndTime());
            }else if (!StringUtil.isEmpty(saleBackVo.getStartTime())){
                sql.append(" and ? < rm.create_time ");
                condition.add(saleBackVo.getStartTime());
            }
        }

        List<RefundsMasterVo> RefundsMasterVos = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(RefundsMasterVo.class), condition.toArray());
        return RefundsMasterVos;
    }

    public List<RefundsDetailVo> getRefundsDetailByMasterId(Long masterId){
        StringBuffer sql = new StringBuffer();
        ArrayList<Object> condition = new ArrayList<>();

        sql.append("SELECT rd.id salbackid, sd.sourcedtlid supplydtlid,vgm.supplier_goods_name goodsName, ");
        sql.append("vgm.supplier_goods_specs_code goodsSpecsCode,vgm.supplier_goods_specs_name goodsSpecs, ");
        sql.append("vgm.supplier_goods_unit goodsUnit,rd.reality_refunds_number goodsAmount,rd.lot_no lotNo, ");
        sql.append("rd.refunds_price goodsPrice,hcl.proddate beginTime,hcl.invadate endTime,rd.refunds_remark expectTime ");
        sql.append("from hdi_refunds_detail rd  LEFT JOIN hdi_core_lot hcl on rd.lot_id=hcl.lotid and hcl.del_flag=0 ");
        sql.append("LEFT JOIN hdi_core_supply_detail sd on rd.supply_detail_id=sd.supply_detail_id ");
        sql.append("LEFT JOIN view_hdi_goods_specs_match vgm on vgm.platform_goods_id=rd.goods_id and vgm.platform_goods_specs_id=rd.specs_id ");
        sql.append(" where rd.master_id=? ");
        condition.add(masterId);

        List<RefundsDetailVo> RefundsDetailVos = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(RefundsDetailVo.class), condition.toArray());
        return RefundsDetailVos;
    }
}
