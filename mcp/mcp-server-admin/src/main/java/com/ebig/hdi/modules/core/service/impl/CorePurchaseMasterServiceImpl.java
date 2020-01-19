package com.ebig.hdi.modules.core.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.entity.MasterDetailsCommonEntity;
import com.ebig.hdi.common.enums.*;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.*;
import com.ebig.hdi.modules.core.dao.CorePurchaseMasterDao;
import com.ebig.hdi.modules.core.dao.CoreSupplyMasterDao;
import com.ebig.hdi.modules.core.entity.*;
import com.ebig.hdi.modules.core.param.CorePurchaseParam;
import com.ebig.hdi.modules.core.service.*;
import com.ebig.hdi.modules.org.entity.OrgHospitalInfoEntity;
import com.ebig.hdi.modules.org.entity.OrgSupplierInfoEntity;
import com.ebig.hdi.modules.org.service.OrgHospitalInfoService;
import com.ebig.hdi.modules.org.service.OrgSupplierInfoService;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import com.ebig.hdi.modules.sys.service.SysConfigService;
import com.ebig.hdi.modules.sys.service.SysSequenceService;
import com.ebig.hdi.modules.unicode.entity.UnicodeSupplyShipEntity;
import com.ebig.hdi.modules.unicode.service.UnicodeSupplyShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.*;


@Service("corePurchaseMasterService")
public class CorePurchaseMasterServiceImpl extends ServiceImpl<CorePurchaseMasterDao, CorePurchaseMasterEntity> implements CorePurchaseMasterService {

    @Autowired
    private SysSequenceService sysSequenceService;

    @Autowired
    private CorePurchaseDetailServiceImpl corePurchaseDetailService;

    @Autowired
    private CoreStorehouseService coreStorehouseService;

    @Autowired
    private CoreSupplyMasterDao coreSupplyMasterDao;

    @Autowired
    private CoreSupplyDetailService coreSupplyDetailService;

    @Autowired
    private CoreLotService coreLotService;

    @Autowired
    private FastDFSClientUtils fastDFSClientUtils;


    @Autowired
    private OrgSupplierInfoService orgSupplierInfoService;

    @Autowired
    private OrgHospitalInfoService orgHospitalInfoService;


    @Autowired
    private UnicodeSupplyShipService unicodeSupplyShipService;
    @Autowired
    private CoreLabelSizeService coreLabelSizeService;
    @Autowired
    private SysConfigService sysConfigService;


    // 临时文件夹获取java system变量中的临时路径，在web项目中是容器的temp文件夹,如果直接运行是系统临时文件夹.
    private static final String FILE_PATH_TEMPLATE = System.getProperty("java.io.tmpdir") + "/%s";

    @Override
    @DataFilter(subDept = true, user = false, tableAlias = "cpm")
    public PageUtils queryPage(Map<String, Object> params) {
        int currPage = Integer.parseInt(params.get("page").toString());
        int pageSize = Integer.parseInt(params.get("limit").toString());
        // 手动数据过滤
        if (params.get(Constant.SQL_FILTER) != null) {
            String sqlFilter = params.get(Constant.SQL_FILTER).toString();
            params.put("deptIds", sqlFilter);
        }

        Page<CorePurchaseMasterEntity> page = new Page<CorePurchaseMasterEntity>(currPage, pageSize);
        List<CorePurchaseMasterEntity> list = this.baseMapper.selectByDeptId(page, params);
        page.setRecords(list);
        return new PageUtils(page);
    }

    @Override
    @DataFilter(subDept = true, user = false, tableAlias = "cpm")
    public PageUtils bedingungenQueryPage(Map<String, Object> params) {
        int currPage = Integer.parseInt(params.get("page").toString());
        int pageSize = Integer.parseInt(params.get("limit").toString());
        // 手动数据过滤
        if (params.get(Constant.SQL_FILTER) != null) {
            String sqlFilter = params.get(Constant.SQL_FILTER).toString();
            params.put("deptIds", sqlFilter);
        }

        Page<CorePurchaseMasterEntity> page = new Page<CorePurchaseMasterEntity>(currPage, pageSize);
        List<CorePurchaseMasterEntity> list = this.baseMapper.selectByBedingungen(page, params);
        page.setRecords(list);
        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePurchaseStatus(CorePurchaseMasterEntity corePurchaseMasterEntity, SysUserEntity user) {
        if (!user.getUserType().equals(TypeEnum.USER_SUPPLIER.getKey())) {
            throw new HdiException("当前登录用户无此权限");
        }

        this.updateById(corePurchaseMasterEntity);

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(MasterDetailsCommonEntity<CorePurchaseMasterEntity, CorePurchaseDetailEntity> masterdetailsCommonEntity, Long deptId, Long userId, String username, SysUserEntity user) {
        if (!user.getUserType().equals(TypeEnum.USER_SUPPLIER.getKey())) {
            throw new HdiException("当前登录用户无此权限");
        }
        List<CorePurchaseDetailEntity> details = masterdetailsCommonEntity.getDetails();
        if (StringUtil.isEmpty(details)) {
            throw new HdiException("采购细单不能为空");
        }
        CorePurchaseMasterEntity master = masterdetailsCommonEntity.getMaster();
        CorePurchaseMasterEntity purplanno = this.baseMapper.selectByPurplanno(master.getPurplanno());
        if (!StringUtil.isEmpty(purplanno)) {
            throw new HdiException(MessageFormat.format("采购计划编号:{0}，已存在！", purplanno.getPurplanno()));
        }
        if (StringUtil.isEmpty(user.getDeptId())) {
            throw new HdiException("当前供应商不存在所属部门");
        }
        if (StringUtil.isEmpty(master.getId())) {
            throw new HdiException("医院id不能为空");
        }
        //查询平台供应商信息
        List<OrgSupplierInfoEntity> list = orgSupplierInfoService.selectByDeptId(user.getDeptId());
        if (CollectionUtils.isNotEmpty(list)) {
            master.setSupplierId(list.get(0).getId());
            master.setSupplierCode(list.get(0).getSupplierCode());
            master.setSupplierName(list.get(0).getSupplierName());
            //查询原医院，原供应商信息
            UnicodeSupplyShipEntity entity = unicodeSupplyShipService.selectBySupplierIdAndHospitalId(master.getSupplierId(), master.getId());
            if (entity != null) {
                master.setSourcesSupplierId(entity.getSourcesSupplierId());
                master.setSourcesSupplierCode(entity.getSourcesSupplierCreditCode());
                master.setSourcesSupplierName(entity.getSourcesSupplierName());
                master.setSourcesHospitalId(entity.getSourcesHospitalId());
                master.setSourcesHospitalCode(entity.getSourcesHospitalCreditCode());
                master.setSourcesHospitalName(entity.getSourcesHospitalName());
            }

        }
        master.setDeptId(deptId);
        master.setCremanid(userId);
        master.setCredate(new Timestamp(System.currentTimeMillis()));
        master.setPurchasestatus(PurchaseStatusEnum.UNCONFIRMED.getKey());
        master.setDatasource(DataSourceEnum.MANUAL.getKey());
        //查询平台医院信息
        OrgHospitalInfoEntity entity = orgHospitalInfoService.selectById(master.getId());
        if (null != entity) {
            master.setHorgId(entity.getId());
            master.setHospitalCode(entity.getHospitalCode());
            master.setHospitalName(entity.getHospitalName());
        }
        master.setDelFlag(DelFlagEnum.NORMAL.getKey());
        CoreStorehouseEntity coreStorehouseEntity = coreStorehouseService.selectSupplyAddr(master.getStorehouseid());
        if (coreStorehouseEntity != null) {
            master.setSourcesStorehouseId(coreStorehouseEntity.getOrgdataid());
            master.setStorehouseNo(coreStorehouseEntity.getStorehouseno());
            master.setStorehouseName(coreStorehouseEntity.getStorehousename());
            master.setSupplyAddr(coreStorehouseEntity.getShaddress());
        }
        this.baseMapper.insert(master);
        for (CorePurchaseDetailEntity detailEntity : details) {
            //设置供应商商品数量
            Map<String, Object> map = corePurchaseDetailService.selectYHospital(detailEntity.getHgoodsid(), detailEntity.getHgoodstypeid(), detailEntity.getGoodsclass());
            if (!StringUtil.isEmpty(map)) {
                detailEntity.setYhgoodstypeid(map.get("yhgoodstypeid").toString());
                detailEntity.setHgoodsno(map.get("goods_code").toString());
                detailEntity.setHgoodstypeid(Long.valueOf(map.get("goods_specs_id").toString()));
                detailEntity.setHgoodstypeno(map.get("specs_code").toString());
            }
            detailEntity.setPurchaseMasterId(master.getPurchaseMasterId());
            detailEntity.setCreateId(userId);
            detailEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
            corePurchaseDetailService.insert(detailEntity);
        }
    }

    @Override
    public void deleteMaster(List<CorePurchaseMasterEntity> listEntity, SysUserEntity user) {
        if (!user.getUserType().equals(TypeEnum.USER_SUPPLIER.getKey())) {
            throw new HdiException("当前登录用户无此权限");
        }
        for (CorePurchaseMasterEntity corePurchaseMasterEntity : listEntity) {
            corePurchaseMasterEntity.setDelFlag(DelFlagEnum.DELETE.getKey());
            this.baseMapper.updateById(corePurchaseMasterEntity);
        }
    }


    @Override
    public MasterDetailsCommonEntity<CorePurchaseMasterEntity, CorePurchaseDetailEntity> editList(CorePurchaseMasterEntity entity, SysUserEntity user) {
        if (!user.getUserType().equals(TypeEnum.USER_SUPPLIER.getKey())) {
            throw new HdiException("当前登录用户无此权限");
        }
        MasterDetailsCommonEntity<CorePurchaseMasterEntity, CorePurchaseDetailEntity> commonEntity = new MasterDetailsCommonEntity<CorePurchaseMasterEntity, CorePurchaseDetailEntity>();
        CorePurchaseMasterEntity masterEntity = this.baseMapper.selectByMasterId(entity.getPurchaseMasterId());
        commonEntity.setMaster(masterEntity);
        List<CorePurchaseDetailEntity> detailList = corePurchaseDetailService.queryDetail(entity.getPurchaseMasterId());
        commonEntity.setDetails(detailList);

        return commonEntity;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(MasterDetailsCommonEntity<CorePurchaseMasterEntity, CorePurchaseDetailEntity> entity, Long deptId, Long userId, String username) {
        CorePurchaseMasterEntity corePurchaseMasterEntity = entity.getMaster();
        this.baseMapper.updateById(corePurchaseMasterEntity);
        List<CorePurchaseDetailEntity> details = entity.getDetails();
        if (StringUtil.isEmpty(details)) {
            throw new HdiException("采购细单不能为空");
        }
        for (CorePurchaseDetailEntity corePurchaseDetailEntity : details) {
            if (corePurchaseDetailEntity.getDmlFlag() == 1) {
                Map<String, Object> map = corePurchaseDetailService.selectYHospital(corePurchaseDetailEntity.getHgoodsid(), corePurchaseDetailEntity.getHgoodstypeid(), corePurchaseDetailEntity.getGoodsclass());
                if (!StringUtil.isEmpty(map)) {
                    corePurchaseDetailEntity.setYhgoodstypeid(map.get("yhgoodstypeid").toString());
                    corePurchaseDetailEntity.setHgoodsno(map.get("goods_code").toString());
                    corePurchaseDetailEntity.setHgoodstypeid(Long.valueOf(map.get("goods_specs_id").toString()));
                    corePurchaseDetailEntity.setHgoodstypeno(map.get("specs_code").toString());
                }
                corePurchaseDetailService.insert(corePurchaseDetailEntity);
            } else if (corePurchaseDetailEntity.getDmlFlag() == 2) {
                corePurchaseDetailService.updateById(corePurchaseDetailEntity);
            } else if (corePurchaseDetailEntity.getDmlFlag() == 3) {
                corePurchaseDetailService.deleteById(corePurchaseDetailEntity.getPurchaseDetailId());
            }
        }
    }


    @Override
    public MasterDetailsCommonEntity<CoreSupplyMasterEntity, CorePurchaseDetailEntity> supplyList(CorePurchaseMasterEntity corePurchaseMaster, SysUserEntity user) {
        if (!user.getUserType().equals(TypeEnum.USER_SUPPLIER.getKey())) {
            throw new HdiException("当前登录用户无此权限");
        }
        MasterDetailsCommonEntity<CoreSupplyMasterEntity, CorePurchaseDetailEntity> masterDetailsCommonEntity = new MasterDetailsCommonEntity<>();
        CorePurchaseMasterEntity masterEntity = this.baseMapper.selectMasterId(corePurchaseMaster.getPurchaseMasterId());
//        if (masterEntity==null){
//            throw new HdiException("没有该供货单");
//        }
        CoreSupplyMasterEntity supplyMasterEntity = ReflectUitls.transform(masterEntity, CoreSupplyMasterEntity.class);
        supplyMasterEntity.setSupplyTime(new Timestamp(System.currentTimeMillis()));
        supplyMasterEntity.setCredate(new Timestamp(System.currentTimeMillis()));
        masterDetailsCommonEntity.setMaster(supplyMasterEntity);

        List<CorePurchaseDetailEntity> details = corePurchaseDetailService.queryDetails(corePurchaseMaster.getPurchaseMasterId(), corePurchaseMaster.getPurplanno(), masterEntity.getSupplierId());
        masterDetailsCommonEntity.setDetails(details);
        return masterDetailsCommonEntity;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSupply(MasterDetailsCommonEntity<CoreSupplyMasterEntity, CoreSupplyDetailEntity> entity, Long deptId, Long userId, String username) throws IOException {
        CoreSupplyMasterEntity master = entity.getMaster();
        CorePurchaseMasterEntity corePurchaseMasterEntity = this.baseMapper.selectById(master.getPurchaseMasterId());
        if (PurchaseStatusEnum.PROVIDED.getKey().equals(corePurchaseMasterEntity.getPurchasestatus())) {
            throw new HdiException("生成供货单失败：已完成供货!");
        }
        //对象转换
        CoreSupplyMasterEntity supplyMasterEntity = ReflectUitls.transform(corePurchaseMasterEntity, CoreSupplyMasterEntity.class);
        supplyMasterEntity.setSupplyno(sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.CORE_SUPPLY_MASTER_NO.getKey()));
        supplyMasterEntity.setSalno(master.getSalno());
        supplyMasterEntity.setPurchaseDetailId(master.getPurchaseDetailId());
        supplyMasterEntity.setPurchaseMasterId(master.getPurchaseMasterId());
        supplyMasterEntity.setPurplanno(master.getPurplanno());
        supplyMasterEntity.setSupplyAddr(master.getSupplyAddr());
        supplyMasterEntity.setSupplyType(master.getSupplyType());
        supplyMasterEntity.setSupplyTime(master.getSupplyTime());
        supplyMasterEntity.setDeptId(deptId);
        supplyMasterEntity.setCremanid(userId);
        supplyMasterEntity.setCredate(new Timestamp(System.currentTimeMillis()));
        supplyMasterEntity.setSupplyStatus(SupplyStatusEnum.UNCOMMITTED.getKey());
        supplyMasterEntity.setDatasource(DataSourceEnum.MANUAL.getKey());
        supplyMasterEntity.setDelFlag(DelFlagEnum.NORMAL.getKey());
        this.coreSupplyMasterDao.insert(supplyMasterEntity);

        List<CoreSupplyDetailEntity> details = entity.getDetails();
        for (CoreSupplyDetailEntity coreSupplyDetailEntity : details) {
            validateSupplyDetail(coreSupplyDetailEntity);
            Integer leaveSupplyQty = corePurchaseDetailService.selectDetailLeaveSupplyQty(coreSupplyDetailEntity.getPurchaseDetailId());
            if (coreSupplyDetailEntity.getSupplyQty() > leaveSupplyQty.doubleValue()) {
                throw new HdiException("供货数量不能大于采购数量！");
            }
            coreSupplyDetailEntity.setSupplyMasterId(supplyMasterEntity.getSupplyMasterId());
            coreSupplyDetailEntity.setCremanid(userId);
            coreSupplyDetailEntity.setCredate(new Timestamp(System.currentTimeMillis()));
            coreSupplyDetailEntity.setBatchCode(sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.CORE_SUPPLY_DETAIL_BATCH_CODE.getKey()));
            coreSupplyDetailService.insert(coreSupplyDetailEntity);


            //生成二维码标签图片
            String tempPath = String.format(FILE_PATH_TEMPLATE, UUID.randomUUID() + ".jpg");
            // 二维码存放内容（商品规格编码 + 批号 + 批次码）
            String specsCode = coreSupplyDetailService.getHospitalGoodsSpecsCode(coreSupplyDetailEntity.getGoodsclass(),
                    coreSupplyDetailEntity.getGoodsid(), coreSupplyDetailEntity.getGoodstypeid(), supplyMasterEntity.getHorgId(), supplyMasterEntity.getSupplierId());
            Integer codeWidth = null;
            Integer codeHeight = null;
            try {

                codeWidth = Integer.valueOf(sysConfigService.getValue(SysConfigEnum.QRCODE_BATCH_WIDTH.getKey()));
                codeHeight = Integer.valueOf(sysConfigService.getValue(SysConfigEnum.QRCODE_BATCH_HEIGHT.getKey()));


            } catch (Exception e) {
                throw new HdiException("获取批次二维码高/宽度错误，请检查参数配置！");
            }
            QRCodeUtil.zxingCodeCreate(specsCode + coreSupplyDetailEntity.getLotno() + coreSupplyDetailEntity.getBatchCode(), codeWidth, codeHeight, tempPath, "jpg");
            //上传标签图片到FastDFS
            File pdfFile = new File(tempPath);
            //上传文件到FastDFS
            String qrCodeUrl = fastDFSClientUtils.uploadFile(pdfFile);
            //删除临时文件
            pdfFile.delete();
            //更新批商品次码图片地址
            CoreSupplyDetailEntity upcoreSupplyDetailEntity = new CoreSupplyDetailEntity();
            upcoreSupplyDetailEntity.setSupplyDetailId(coreSupplyDetailEntity.getSupplyDetailId());
            upcoreSupplyDetailEntity.setImageUrl(qrCodeUrl);
            coreSupplyDetailService.updateById(upcoreSupplyDetailEntity);
        }

    }

    private void validateSupplyDetail(CoreSupplyDetailEntity coreSupplyDetailEntity) {
        if (StringUtil.isEmpty(coreSupplyDetailEntity.getGoodsid())) {
            throw new HdiException("商品id不能为空");
        }
        if (StringUtil.isEmpty(coreSupplyDetailEntity.getGoodstypeid())) {
            throw new HdiException("商品规格id不能为空");
        }
        if (StringUtil.isEmpty(coreSupplyDetailEntity.getSupplyQty())) {
            throw new HdiException("供货数量不能为空");
        }
        if (StringUtil.isEmpty(coreSupplyDetailEntity.getSupplyUnitprice())) {
            throw new HdiException("供货单价不能为空");
        }
    }


    //HDI转换用   查询是否存在此原始数据标识对应的主单    
    @Override
    public CorePurchaseMasterEntity selectByOrgdataid(String orgdataid) {
        CorePurchaseMasterEntity corePurchaseMasterEntity = this.baseMapper.selectByOrgdataid(orgdataid);
        return corePurchaseMasterEntity;
    }


    //HDI转换用   生成主单
    @Override
    public void saveCorePurchaseMaster(CorePurchaseMasterEntity entity) {
        this.baseMapper.insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePurchasestatus(Long purchaseMasterId) {
        Integer purchaseNumber = corePurchaseDetailService.getPurchaseNumberByPurchaseMasterId(purchaseMasterId);
        Integer supplyNumber = coreSupplyDetailService.getSupplyNumberByPurchaseMasterId(purchaseMasterId);
        CorePurchaseMasterEntity corePurchaseMasterEntity = new CorePurchaseMasterEntity();
        corePurchaseMasterEntity.setPurchaseMasterId(purchaseMasterId);
        //供货量小于采购量则部分供货，否则已供货(采购单状态 0 已作废 1 未确认 2 已确认3 已供货 4 部分供货)
        corePurchaseMasterEntity.setPurchasestatus(supplyNumber < purchaseNumber ? PurchaseStatusEnum.PART_PROVIDED.getKey() : PurchaseStatusEnum.PROVIDED.getKey());
        this.baseMapper.updateById(corePurchaseMasterEntity);
    }

    @Override
    public List<CorePurchaseMasterEntity> getInfoByPurplanno(String purplanno, Long supplierId) {
        return this.baseMapper.selectLikePurplanno(purplanno, supplierId);
    }

    @Override
    public List<Map<String, Object>> getList(String[] columns, CorePurchaseParam queryParam) {
        return this.baseMapper.getList(columns, queryParam);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> importData(String[][] rows, SysUserEntity user) {

        Map<String, String> errorMessage = new HashMap<>(16);

        if (!user.getUserType().equals(TypeEnum.USER_SUPPLIER.getKey())) {
            throw new HdiException("当前登录用户无此权限");
        }
        StringBuilder errMsg = new StringBuilder();

        String SUCCESSCOUNT = "successCount", FAILCOUNT = "failCount", ERRORMESSAGE = "errorMessage";
        Integer failCount = 0, successCount = 0;
        for (int i = 1; i < rows.length; i++) {
            String[] row = rows[i];
            CorePurchaseMasterEntity corePurchaseMasterEntity = new CorePurchaseMasterEntity();
            CorePurchaseDetailEntity corePurchaseDetailEntity = new CorePurchaseDetailEntity();
            //test1
            for (int i1 = 0; i1 < row.length; i1++) {
                row[i1] = row[i1].trim();
            }
            String hospitalName = row[0], storehouseName = row[1], purplanno = row[2];
            String hgoodsname = row[5], factoryName = row[6], hgoodstype = row[7], hgoodsunit = row[8];

            if (StringUtil.isEmpty(hospitalName) || StringUtil.isEmpty(storehouseName) || StringUtil.isEmpty(purplanno) || StringUtil.isEmpty(row[3])
                    || StringUtil.isEmpty(row[4]) || StringUtil.isEmpty(row[5]) || StringUtil.isEmpty(row[9]) || StringUtil.isEmpty(row[10]) || StringUtil.isEmpty(factoryName)) {
                errMsg.append("\n第 ").append(i + 2).append(" 行数据错误，必填字段不能为空");
                failCount++;
                continue;
            }
            Date expecttime = DateUtils.stringToDate(row[4], DateUtils.DATE_TIME_PATTERN);
            Date purplantime = DateUtils.stringToDate(row[3], DateUtils.DATE_TIME_PATTERN);
            Double hqty = Double.parseDouble(row[9]);
            Double hunitprice = Double.parseDouble(row[10]);
            if (hqty < 0 || hunitprice < 0) {
                errMsg.append("\n第 ").append(i + 2).append(" 行数据错误，采购单价或采购数量不能小于0:");
                failCount++;
                continue;
            }

            List<OrgHospitalInfoEntity> orgHospitalInfoEntity = orgHospitalInfoService.selectByHospitalName(hospitalName);
            if (CollectionUtils.isEmpty(orgHospitalInfoEntity)) {
                errMsg.append("\n第 ").append(i + 2).append(" 行数据错误，医院:").append(hospitalName).append("，平台不存在该医院");
                failCount++;
                continue;
            }
            List<CoreStorehouseEntity> coreStorehouseEntity = coreStorehouseService.selectByStorehouseName(storehouseName);
            if (CollectionUtils.isEmpty(coreStorehouseEntity)) {
                errMsg.append("\n第 ").append(i + 2).append(" 行数据错误，库房:").append(storehouseName).append("，不存在该库房");
                failCount++;
                continue;
            }


            //查询平台供应商信息
            List<OrgSupplierInfoEntity> list = orgSupplierInfoService.selectByDeptId(user.getDeptId());
            if (CollectionUtils.isNotEmpty(list)) {
                corePurchaseMasterEntity.setSupplierId(list.get(0).getId());
                corePurchaseMasterEntity.setSupplierCode(list.get(0).getSupplierCode());
                corePurchaseMasterEntity.setSupplierName(list.get(0).getSupplierName());
                //查询原医院，原供应商信息
            }

            //平台医院
            corePurchaseMasterEntity.setHospitalName(hospitalName);
            corePurchaseMasterEntity.setHorgId(orgHospitalInfoEntity.get(0).getId());
            corePurchaseMasterEntity.setHospitalCode(orgHospitalInfoEntity.get(0).getHospitalCode());
            //库房
            corePurchaseMasterEntity.setStorehouseName(storehouseName);
            corePurchaseMasterEntity.setSourcesStorehouseId(coreStorehouseEntity.get(0).getOrgdataid());
            corePurchaseMasterEntity.setStorehouseNo(coreStorehouseEntity.get(0).getStorehouseno());
            corePurchaseMasterEntity.setStorehouseid(coreStorehouseEntity.get(0).getStorehouseid());

            corePurchaseMasterEntity.setSupplyAddr(coreStorehouseEntity.get(0).getShaddress());
            corePurchaseMasterEntity.setPurplanno(purplanno);
            corePurchaseMasterEntity.setExpecttime(expecttime);
            corePurchaseMasterEntity.setPurplantime(purplantime);
            corePurchaseMasterEntity.setDeptId(user.getDeptId());
            corePurchaseMasterEntity.setCremanid(user.getUserId());
            corePurchaseMasterEntity.setCredate(new Timestamp(System.currentTimeMillis()));
            corePurchaseMasterEntity.setPurchasestatus(PurchaseStatusEnum.UNCONFIRMED.getKey());
            corePurchaseMasterEntity.setDatasource(DataSourceEnum.MANUAL.getKey());
            corePurchaseMasterEntity.setDelFlag(DelFlagEnum.NORMAL.getKey());
            this.baseMapper.insert(corePurchaseMasterEntity);
            //细单
            corePurchaseDetailEntity.setPurchaseMasterId(corePurchaseMasterEntity.getPurchaseMasterId());
            Map<String, Object> map = corePurchaseDetailService.selectViewByGoodsNameAndSpecs(orgHospitalInfoEntity.get(0).getId(), hgoodsname, hgoodstype, factoryName);
            if (map == null) {
                errMsg.append("\n第 ").append(i + 2).append(" 行数据错误:该医院或厂商不存在该商品数据校验");
                failCount++;
                continue;
            }

            corePurchaseDetailEntity.setCreateId(user.getUserId());
            corePurchaseDetailEntity.setCreateTime(new Date());
            corePurchaseDetailEntity.setGoodsclass(Integer.parseInt(map.get("goods_type").toString()));
            corePurchaseDetailEntity.setHgoodstypeid(Long.parseLong(map.get("goods_specs_id").toString()));
            corePurchaseDetailEntity.setHgoodstypeno(map.get("specs_code").toString());
            corePurchaseDetailEntity.setHgoodsname(hgoodsname);
            corePurchaseDetailEntity.setHgoodstype(hgoodstype);
            corePurchaseDetailEntity.setHgoodsunit(hgoodsunit);
            corePurchaseDetailEntity.setHqty(hqty);
            corePurchaseDetailEntity.setHunitprice(hunitprice);
            corePurchaseDetailEntity.setHgoodsid(Long.parseLong(map.get("goods_id").toString()));
            corePurchaseDetailEntity.setHgoodsno(map.get("goods_code").toString());
            corePurchaseDetailService.insert(corePurchaseDetailEntity);
            successCount++;
        }
        if (failCount > 0) {
            StringBuilder manage = new StringBuilder();
            manage.append("成功条数: 0,失败条数").append(rows.length - 1).append("   ").append(errMsg);
            throw new HdiException(manage.toString());
        }
        successCount = rows.length - 1;
        errorMessage.put(SUCCESSCOUNT, successCount.toString());
        errorMessage.put(FAILCOUNT, "0");
        errorMessage.put(ERRORMESSAGE, errMsg.toString());
        return errorMessage;
    }


}
