package com.ebig.mcp.server.api.http.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.mcp.server.api.http.entity.CorePurchaseDetailEntity;
import com.ebig.mcp.server.api.http.entity.CorePurchaseMasterEntity;
import com.ebig.mcp.server.api.http.entity.CoreSupplyDetailEntity;
import com.ebig.mcp.server.api.http.entity.CoreSupplyMasterEntity;
import com.ebig.mcp.server.api.http.entity.SysDictEntity;
import com.ebig.mcp.server.api.http.service.SysDictService;

import lombok.extern.slf4j.Slf4j;

/**
 * 供货单数据层
 */
@Component
@Slf4j
public class SupplyMenuDao {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    private SysDictService sysDictService;


    /**
     * 根据供货状态、供货主单编码 、供应商编码查询供货主单信息
     * @param supplyno
     * @param supplierCode
     * @return
     */
    public List<CoreSupplyMasterEntity> getSupplyMasterByNo(Integer supplyStatus,String supplyno,String supplierCode){

        StringBuffer sql = new StringBuffer();
        ArrayList<Object> condition = new ArrayList<>();
        sql.append("select *  from  hdi_core_supply_master where  supply_status=? and supplyno= ?  and supplier_code=? and del_flag=0");
        condition.add(supplyStatus);
        condition.add(supplyno);
        condition.add(supplierCode);
        List<CoreSupplyMasterEntity> masterEntitiesByNo = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(CoreSupplyMasterEntity.class),condition.toArray());
        return masterEntitiesByNo;
    }
    /**
     * 根据供货主单编码 跟供应商编码查询已经提交医院后状态的供货主单信息
     * @param supplyno
     * @param supplierCode
     * @return
     */
    public List<CoreSupplyMasterEntity> getSupplyMasterCommit(String supplyno,String supplierCode){

        StringBuffer sql = new StringBuffer();
        ArrayList<Object> condition = new ArrayList<>();
        sql.append("select *  from  hdi_core_supply_master where  supplyno= ?  and supplier_code=? and supply_status>0 and del_flag=0");
        condition.add(supplyno);
        condition.add(supplierCode);
        List<CoreSupplyMasterEntity> masterEntitiesByNo = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(CoreSupplyMasterEntity.class),condition.toArray());
        return masterEntitiesByNo;
    }
    /**
     * 根据供货细单来源细单id 跟供货主单id查询供货单信息
     * @param sourcedtlid
     * @param supplyMasterId
     * @return
     */
    public List<CoreSupplyDetailEntity> getSupplyDetailByNo(Long supplyMasterId,Long sourcedtlid){

        StringBuffer sql = new StringBuffer();
        ArrayList<Object> condition = new ArrayList<>();

        sql.append("select * from  hdi_core_supply_detail where  supply_master_id=? and sourcedtlid= ?");
       condition.add(supplyMasterId);
       condition.add(sourcedtlid);
        List<CoreSupplyDetailEntity> masterEntitiesByNo = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(CoreSupplyDetailEntity.class),condition.toArray());
        return masterEntitiesByNo;
    }

    /**
     * 修改供货主单信息
     * @param supplyMaster
     */
    public Integer updateSupplyMaster(CoreSupplyMasterEntity supplyMaster){
        StringBuffer sql = new StringBuffer();
        sql.append("update hdi_core_supply_master set purchase_master_id=?,purplanno=?,supply_time=?,supply_type=?,datasource=2,");
        if (supplyMaster.getSalno()!=null){
            sql.append("salno=?,");
        }
        sql.append("del_flag=? where supplyno=? and supplier_code = ?");
        ArrayList<Object> condition = new ArrayList<>();
        condition.add(supplyMaster.getPurchaseMasterId());
        condition.add(supplyMaster.getPurplanno());
        condition.add(supplyMaster.getSupplyTime());
        condition.add(supplyMaster.getSupplyType());
        if (supplyMaster.getSalno()!=null){
            condition.add(supplyMaster.getSalno());
        }
        condition.add(supplyMaster.getDelFlag());
        condition.add(supplyMaster.getSupplyno());
        condition.add(supplyMaster.getSourcesSupplierCode());

        int update = jdbcTemplate.update(sql.toString(), condition.toArray());
        return update;
    }

    /**
     * 新增供货主单信息
     * @param supplyMaster
     * @return
     */
    public Integer addSupplyMaster(CoreSupplyMasterEntity supplyMaster){
        StringBuffer sql = new StringBuffer();
        ArrayList<Object> condition = new ArrayList<>();
        List<CorePurchaseMasterEntity> purMasterByIdList = null;
        CorePurchaseMasterEntity purMasterById=null;
        try {
            purMasterByIdList = getPurMasterById(supplyMaster.getPurchaseMasterId());
            purMasterById=purMasterByIdList.get(0);
        } catch (Exception e) {
           throw new HdiException("采购主单不存在");
        }
        sql.append("insert into hdi_core_supply_master (supplyno,salno,sources_supplier_id,sources_supplier_code,sources_supplier_name,");
        sql.append("supplier_id,supplier_code,supplier_name,sources_hospital_id,sources_hospital_code,");
        sql.append("sources_hospital_name,dept_id,horg_id,hospital_code,hospital_name,");
        sql.append("sources_storehouse_id,storehouseid,storehouse_no,storehouse_name,purchase_master_id,");
        sql.append("purplanno,sourceid,supply_type,supply_time,supply_status,datasource,del_flag,credate)");
        sql.append(" values(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?,?,?,?)");

        condition.add(supplyMaster.getSupplyno());
        condition.add(supplyMaster.getSalno());
        condition.add(purMasterById.getSourcesSupplierId());
        condition.add(supplyMaster.getSourcesSupplierCode());
        condition.add(purMasterById.getSourcesSupplierName());

        condition.add(purMasterById.getSupplierId());
        condition.add(purMasterById.getSupplierCode());
        condition.add(purMasterById.getSupplierName());
        condition.add(purMasterById.getSourcesHospitalId());
        condition.add(purMasterById.getSourcesHospitalCode());

        condition.add(purMasterById.getSourcesHospitalName());
        condition.add(purMasterById.getDeptId());
        condition.add(purMasterById.getHorgId());
        condition.add(purMasterById.getHospitalCode());
        condition.add(purMasterById.getHospitalName());

        condition.add(purMasterById.getSourcesStorehouseId());
        condition.add(purMasterById.getStorehouseid());
        condition.add(purMasterById.getStorehouseNo());
        condition.add(purMasterById.getStorehouseName());
        condition.add(purMasterById.getPurchaseMasterId());

        condition.add(purMasterById.getPurplanno());
        condition.add(supplyMaster.getSourceid());
        condition.add(supplyMaster.getSupplyType());
        condition.add(supplyMaster.getSupplyTime());
        condition.add(0);
        condition.add(2);
        condition.add(0);
        condition.add(new Timestamp(System.currentTimeMillis()));

        int update = jdbcTemplate.update(sql.toString(), condition.toArray());

//        KeyHolder keyHolder1 = new GeneratedKeyHolder();
//
//         jdbcTemplate.update(new PreparedStatementCreator() {
//         @Override
//         public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//        // 设置返回的主键字段名
//         PreparedStatement ps = con.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
//         ps.setString(1, "1");
//         ps.setString(2, "2");
//         return ps;
//         }
//         }, keyHolder1);

        return update;
    }

    /**
     * 新增供货细单信息
     * @param supplierId 
     * @param hospital 
     * @param supplyDetail
     * @return
     */
    public Integer addSupplyDetail(Long supplierId, Long hospitalId, CoreSupplyDetailEntity supplyDetail,CoreSupplyMasterEntity masterEntitiesByNo){
        StringBuffer sql = new StringBuffer();
        ArrayList<Object> condition = new ArrayList<>();
        List<CorePurchaseDetailEntity> purDetailById = null;
        CorePurchaseDetailEntity purchaseDetail=null;
        try {
            purDetailById = getPurDetailById(supplyDetail.getPurchaseDetailId());
             purchaseDetail = purDetailById.get(0);
        } catch (Exception e) {
            throw new HdiException("采购细单不存在");
        }
        Map<String, Object> supplyGoodsByPlatformGoods = null;
        try {
            supplyGoodsByPlatformGoods = getSupplyGoodsByPlatformGoods(supplierId, supplyDetail.getGoodsclass(), hospitalId, purchaseDetail.getHgoodsid(), purchaseDetail.getHgoodstypeid()).get(0);
        } catch (Exception e) {
            throw new HdiException("采购细单商品有误");
        }
       List< Map<String, Object>> lotMap = null;
        String goodsunit=null;

        try {
            lotMap = jdbcTemplate.queryForList("select lotid from hdi_core_lot where lotno=?", new Object[]{supplyDetail.getLotno()});
        } catch (Exception e) {
        }

        List<SysDictEntity> dictEntities = null;

        try {
            dictEntities = sysDictService.selectDictByType("goods_unit");
            goodsunit=dictEntities.stream().collect(Collectors.toMap(SysDictEntity::getValue, SysDictEntity::getCode)).get(supplyDetail.getGoodsunit());
        } catch (Exception e) {
            log.error("商品单品字典转换错误:"+e.getMessage());
        }

           if (CollectionUtils.isEmpty(lotMap)){
               addLotno(supplyDetail,masterEntitiesByNo,supplyGoodsByPlatformGoods);
               lotMap = jdbcTemplate.queryForList("select lotid from hdi_core_lot where lotno=?", new Object[]{supplyDetail.getLotno()});
           }


        sql.append("insert into hdi_core_supply_detail (supply_master_id,purchase_master_id,purchase_detail_id,sourceid,sourcedtlid,");
        sql.append("goodsclass,goodsid,goodsno,goodsname,goodstypeid,");
        sql.append("goodstypeno,goodstype,goodsunit,supply_qty,supply_unitprice,");
        sql.append("lotid,lotno,proddate,invadate,credate )");
        sql.append(" values(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?)");

        condition.add(masterEntitiesByNo.getSupplyMasterId());
        condition.add(purchaseDetail.getPurchaseMasterId());
        condition.add(purchaseDetail.getPurchaseDetailId());
        condition.add(supplyDetail.getSourceid());
        condition.add(supplyDetail.getSourcedtlid());

        condition.add(supplyGoodsByPlatformGoods.get("goods_type"));
        condition.add(supplyGoodsByPlatformGoods.get("supplier_goods_id"));
        condition.add(supplyGoodsByPlatformGoods.get("supplier_goods_code"));
        condition.add(supplyGoodsByPlatformGoods.get("supplier_goods_name"));
        condition.add(supplyGoodsByPlatformGoods.get("supplier_goods_specs_id"));

        condition.add(supplyDetail.getGoodstypeno());
        condition.add(supplyDetail.getGoodstype());
        condition.add(goodsunit);
        condition.add(supplyDetail.getSupplyQty());
        condition.add(supplyDetail.getSupplyUnitprice());

        condition.add(lotMap.get(0).get("lotid"));
        condition.add(supplyDetail.getLotno());
        condition.add(supplyDetail.getProddate());
        condition.add(supplyDetail.getInvadate());
        condition.add(new Timestamp(System.currentTimeMillis()));

        int update = jdbcTemplate.update(sql.toString(), condition.toArray());
        return update;
    }

    /**
     * 修改供货细单信息
     * @param supplyDetail
     * @return
     */
    public Integer updateSupplyDetail(CoreSupplyDetailEntity supplyDetail){

        StringBuffer sql = new StringBuffer();
        ArrayList<Object> condition = new ArrayList<>();
//        List<CorePurchaseDetailEntity> purDetailById = getPurDetailById(supplyDetail.getPurchaseDetailId());
//        CorePurchaseDetailEntity purchaseDetail = purDetailById.get(0);
//        Map<String, Object> supplyGoodsByPlatformGoods = getSupplyGoodsByPlatformGoods(purchaseDetail.getHgoodsid(), purchaseDetail.getHgoodstypeid()).get(0);
        Map<String, Object> lotMap = null;
        try {
            lotMap = jdbcTemplate.queryForList("select lotid from hdi_core_lot where lotno=", new Object[]{supplyDetail.getLotno()}).get(0);
        } catch (DataAccessException e) {
            throw new HdiException("批号有误");
        }

        sql.append("update hdi_core_supply_detail set goodsunit=?,supply_qty=?,supply_unitprice=?,lotid=?,lotno=?,");
        sql.append("proddate=?,invadate=?,edit_time=?");

        condition.add(supplyDetail.getGoodsunit());
        condition.add(supplyDetail.getSupplyQty());
        condition.add(supplyDetail.getSupplyUnitprice());
        condition.add(lotMap.get("lotid"));
        condition.add(supplyDetail.getLotno());

        condition.add(supplyDetail.getProddate());
        condition.add(supplyDetail.getInvadate());
        condition.add(new Timestamp(System.currentTimeMillis()));

        int update = jdbcTemplate.update(sql.toString(), condition.toArray());
        return update;
    }


    private void addLotno(CoreSupplyDetailEntity supplyDetail,CoreSupplyMasterEntity masterEntitiesByNo, Map<String, Object> supplyGoods){
        ArrayList<Object> condition = new ArrayList<>();
        StringBuffer sql = new StringBuffer();
        List<Map<String, Object>> supplyInfo = jdbcTemplate.queryForList("select id,dept_id from hdi_org_supplier_info where supplier_code= ?" , new Object[]{masterEntitiesByNo.getSupplierCode()});
        sql.append("insert into hdi_core_lot (supplier_id,dept_id,goodsclass,goodsid,goodstypeid,lottype,lotstatus,lotno,proddate,invadate,del_flag) ");
        sql.append(" values(?,?,?,?,?, ?,?,?,?,?, ?)");

        condition.add(supplyInfo.get(0).get("id"));
        condition.add(supplyInfo.get(0).get("dept_id"));
        condition.add(supplyGoods.get("goods_type"));
        condition.add(supplyGoods.get("supplier_goods_id"));
        condition.add(supplyGoods.get("supplier_goods_specs_id"));
        condition.add(1);
        condition.add(1);
        condition.add(supplyDetail.getLotno());
        condition.add(supplyDetail.getProddate());
        condition.add(supplyDetail.getInvadate());
        condition.add(0);
        jdbcTemplate.update(sql.toString(), condition.toArray());

    }
    /**
     * 根据id获取采购主单信息
     * @param purMasterId
     * @return
     */
    public  List<CorePurchaseMasterEntity> getPurMasterById(Long purMasterId){
        StringBuffer sql = new StringBuffer();
        ArrayList<Object> condition = new ArrayList<>();
        sql.append("select * from hdi_core_purchase_master where del_flag=0 and purchase_master_id= ?");
        condition.add(purMasterId);
        List<CorePurchaseMasterEntity> purchaseMasterEntityList = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(CorePurchaseMasterEntity.class), condition.toArray());

        return purchaseMasterEntityList;
    }

    /**
     * 根据id获取采购细单信息
     * @param purDetailId
     * @return
     */
    public  List<CorePurchaseDetailEntity> getPurDetailById(Long purDetailId){
        StringBuffer sql = new StringBuffer();
        ArrayList<Object> condition = new ArrayList<>();
        sql.append("select * from hdi_core_purchase_detail where  purchase_detail_id= ?");
        condition.add(purDetailId);
        List<CorePurchaseDetailEntity> purchaseDetailEntityList = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(CorePurchaseDetailEntity.class), condition.toArray());

        return purchaseDetailEntityList;
    }

    /**
     * 根据平台商品获取供应商商品信息
     * @param supplierId
     * @param goodsclass 
     * @param hospitalId 
     * @param goodsId
     * @param goodsSpecsId 
     * @return
     */
    public List<Map<String,Object>> getSupplyGoodsByPlatformGoods(Long supplierId, Integer goodsclass,  Long hospitalId, Long goodsId, Long goodsSpecsId){
        StringBuffer sql = new StringBuffer();
        ArrayList<Object> condition = new ArrayList<>();
        sql.append("select supplier_goods_id,supplier_goods_code,supplier_goods_name,supplier_goods_specs_id,supplier_goods_specs_code,supplier_goods_specs_name,goods_type from view_hdi_goods_specs_match where supplier_id=? and goods_type=? and hospital_id=? and hospital_goods_id=? and hospital_goods_specs_id=?");
        condition.add(supplierId);
        condition.add(goodsclass);
        condition.add(hospitalId);
        condition.add(goodsId);
        condition.add(goodsSpecsId);
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql.toString(), condition.toArray());
        return maps;
    }
}
