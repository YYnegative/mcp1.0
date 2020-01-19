package com.ebig.hdi.modules.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
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
import com.ebig.hdi.modules.core.service.*;
import com.ebig.hdi.modules.coretransform.dao.SpdPurbackDao;
import com.ebig.hdi.modules.coretransform.dao.TempSpdRgDao;
import com.ebig.hdi.modules.coretransform.dao.TempSpdRgdtlDao;
import com.ebig.hdi.modules.coretransform.entity.TempSpdRgEntity;
import com.ebig.hdi.modules.coretransform.entity.TempSpdRgdtlEntity;
import com.ebig.hdi.modules.org.entity.OrgHospitalInfoEntity;
import com.ebig.hdi.modules.org.entity.OrgSupplierInfoEntity;
import com.ebig.hdi.modules.org.service.OrgHospitalInfoService;
import com.ebig.hdi.modules.org.service.OrgSupplierInfoService;
import com.ebig.hdi.modules.sys.entity.SysDictEntity;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import com.ebig.hdi.modules.sys.service.SysConfigService;
import com.ebig.hdi.modules.sys.service.SysDictService;
import com.ebig.hdi.modules.sys.service.SysSequenceService;
import com.ebig.hdi.modules.unicode.entity.UnicodeSupplyShipEntity;
import com.ebig.hdi.modules.unicode.service.UnicodeSupplyShipService;
import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service("coreSupplyMasterService")
@Slf4j
public class CoreSupplyMasterServiceImpl extends ServiceImpl<CoreSupplyMasterDao, CoreSupplyMasterEntity>
        implements CoreSupplyMasterService {

    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;

    private static String BATCH_CODE_HEAD_HTML;

    private static String BATCH_CODE_BODY_HTML;

    private static String BATCH_CODE_END_HTML;

    // 临时文件夹获取java system变量中的临时路径，在web项目中是容器的temp文件夹,如果直接运行是系统临时文件夹.
    private static final String FILE_PATH_TEMPLATE = System.getProperty("java.io.tmpdir") + "/%s";

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private SysSequenceService sysSequenceService;

    @Autowired
    private CoreSupplyDetailService coreSupplyDetailService;

    @Autowired
    private CoreLotService coreLotService;

    @Autowired
    private CoreSupplyDetailServiceImpl coreSupplyDetailServiceImpl;

    @Autowired
    private CoreAcceptMasterServiceImpl coreAcceptMasterServiceImpl;

    @Autowired
    private TempSpdRgdtlDao tempSpdRgdtlDao;

    @Autowired
    private TempSpdRgDao tempSpdRgDao;

    @Autowired
    private CoreLabelMasterService coreLabelMasterService;

    @Autowired
    private CoreLabelMasterServiceImpl coreLabelMasterServiceImpl;

    @Autowired
    private CoreLabelDetailService coreLabelDetailService;

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private FastDFSClientUtils fastDFSClientUtils;

    @Autowired
    private CoreAcceptDetailService coreAcceptDetailService;
    @Autowired
    private CorePurchaseMasterDao corePurchaseMasterDao;
    @Autowired
    private CoreAcceptDetailServiceImpl coreAcceptDetailServiceImpl;

    @Autowired
    private UnicodeSupplyShipService unicodeSupplyShipService;

    @Autowired
    private OrgSupplierInfoService orgSupplierInfoService;

    @Autowired
    private OrgHospitalInfoService orgHospitalInfoService;

    @Autowired
    private CoreStorehouseService coreStorehouseService;

    @Autowired
    private PublicComboDataService publicComboDataService;

    @Autowired
    private CorePurchaseDetailService corePurchaseDetailService;
    @Autowired
    SpdPurbackDao spdPurbackDao;

    @Autowired
    private CoreLabelSizeService coreLabelSizeService;

    @Override
    @DataFilter(subDept = true, user = false, tableAlias = "csm")
    public PageUtils queryPage(Map<String, Object> params) {

        int currPage = Integer.parseInt(params.get("page").toString());
        int pageSize = Integer.parseInt(params.get("limit").toString());
        // 手动数据过滤
        if (params.get(Constant.SQL_FILTER) != null) {
            String sqlFilter = params.get(Constant.SQL_FILTER).toString();
            params.put("deptIds", sqlFilter);
        }

        Page<CoreSupplyMasterEntity> page = new Page<CoreSupplyMasterEntity>(currPage, pageSize);
        List<CoreSupplyMasterEntity> list = this.baseMapper.selectByDeptId(page, params);
        page.setRecords(list);
        return new PageUtils(page);
    }

    @Override
    @DataFilter(subDept = true, user = false, tableAlias = "csm")
    public PageUtils bedingungenQueryPage(Map<String, Object> params) {
        int currPage = Integer.parseInt(params.get("page").toString());
        int pageSize = Integer.parseInt(params.get("limit").toString());
        // 手动数据过滤
        if (params.get(Constant.SQL_FILTER) != null) {
            String sqlFilter = params.get(Constant.SQL_FILTER).toString();
            params.put("deptIds", sqlFilter);
        }

        Page<CoreSupplyMasterEntity> page = new Page<CoreSupplyMasterEntity>(currPage, pageSize);
        List<CoreSupplyMasterEntity> list = this.baseMapper.selectByBedingungen(page, params);
        page.setRecords(list);
        return new PageUtils(page);
    }

    //---更新供货状态---
    @Override
    public void updateSupplyStatus(CoreSupplyMasterEntity coreSupplyMaster, SysUserEntity user) {
        if (user.getUserType() != 1) {
            throw new HdiException("当前登录用户无此权限");
        }
        this.updateById(coreSupplyMaster);

    }

    @Override
    public void deleteMaster(List<CoreSupplyMasterEntity> listEntity, SysUserEntity user) {
        if (user.getUserType() != 1) {
            throw new HdiException("当前登录用户无此权限");
        }
        for (CoreSupplyMasterEntity coreSupplyMasterEntity : listEntity) {
            coreSupplyMasterEntity.setDelFlag(DelFlagEnum.DELETE.getKey());
            this.baseMapper.updateById(coreSupplyMasterEntity);
        }
    }

    /**
     * 描述: 新增供货单保存 <br/>
     *
     * @see com.ebig.hdi.modules.core.service.CoreSupplyMasterService#save(com.ebig.hdi.common.entity.MasterDetailsCommonEntity, java.lang.Long, java.lang.Long, java.lang.String, com.ebig.hdi.modules.sys.entity.SysUserEntity) <br/>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(MasterDetailsCommonEntity<CoreSupplyMasterEntity, CoreSupplyDetailEntity> entity, Long deptId,
                     Long userId, String username, SysUserEntity user) throws IOException {
        if (user.getUserType() != 1) {
            throw new HdiException("当前登录用户无此权限");
        }
        List<CoreSupplyDetailEntity> details = entity.getDetails();
        if (StringUtil.isEmpty(details)) {
            throw new HdiException("供货细单不能为空");
        }

        CoreSupplyMasterEntity coreSupplyMasterEntity = entity.getMaster();

        //如果前端传入采购计划编码，查找采购单赋值采购主单id。
        if (coreSupplyMasterEntity.getPurplanno() != null && coreSupplyMasterEntity.getPurplanno() != "") {
            CorePurchaseMasterEntity corePurchaseMasterEntity = corePurchaseMasterDao.selectByPurplanno(coreSupplyMasterEntity.getPurplanno());
            coreSupplyMasterEntity.setPurchaseMasterId(corePurchaseMasterEntity.getPurchaseMasterId());
        }
        //根据部门id,医院id，库房id 获取平台/原系统--供应商/医院/库房等信息；---改造后新增字段添加
        CoreSupplyMasterEntity supplierId = this.baseMapper.getSupplierIdNew(user.getDeptId(), coreSupplyMasterEntity.getStorehouseid(), coreSupplyMasterEntity.getId());
        if (supplierId == null) {
            throw new HdiException("没有供应商--医院对应关系");
        }
        coreSupplyMasterEntity.setSupplierId(supplierId.getSupplierId());
        coreSupplyMasterEntity.setSupplierCode(supplierId.getSupplierCode());
        coreSupplyMasterEntity.setSupplierName(supplierId.getSupplierName());
        coreSupplyMasterEntity.setHorgId(supplierId.getHorgId());
        coreSupplyMasterEntity.setHospitalCode(supplierId.getHospitalCode());
        coreSupplyMasterEntity.setHospitalName(supplierId.getHospitalName());
        coreSupplyMasterEntity.setSourcesSupplierId(supplierId.getSourcesSupplierId());
        coreSupplyMasterEntity.setSourcesSupplierName(supplierId.getSourcesSupplierName());
        coreSupplyMasterEntity.setSourcesHospitalId(supplierId.getSourcesHospitalId());
        coreSupplyMasterEntity.setSourcesHospitalName(supplierId.getSourcesHospitalName());
        coreSupplyMasterEntity.setSourcesStorehouseId(supplierId.getSourcesStorehouseId());
        coreSupplyMasterEntity.setStorehouseid(supplierId.getStorehouseid());
        coreSupplyMasterEntity.setStorehouseNo(supplierId.getStorehouseNo());
        coreSupplyMasterEntity.setStorehouseName(supplierId.getStorehouseName());

        String supplyno = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.CORE_SUPPLY_MASTER_NO.getKey());
        coreSupplyMasterEntity.setSupplyno(supplyno);
        coreSupplyMasterEntity.setDeptId(deptId);
        coreSupplyMasterEntity.setCremanid(userId);
        coreSupplyMasterEntity.setCredate(new Timestamp(System.currentTimeMillis()));
        coreSupplyMasterEntity.setHorgId(coreSupplyMasterEntity.getId());
        coreSupplyMasterEntity.setSupplyStatus(0);
        coreSupplyMasterEntity.setDatasource(1);
        coreSupplyMasterEntity.setDelFlag(0);
        //TODO 新增供货单--改造后
        this.baseMapper.insert(coreSupplyMasterEntity);

        for (CoreSupplyDetailEntity coreSupplyDetailEntity : details) {
            validateSupplyDetail(coreSupplyDetailEntity);
            //供货主单id从mybatisplus插入后自动获取自增的主键回来;
            coreSupplyDetailEntity.setSupplyMasterId(coreSupplyMasterEntity.getSupplyMasterId());

            coreSupplyDetailEntity.setGoodsname(coreSupplyDetailEntity.getGoodsname());
            coreSupplyDetailEntity.setCremanid(userId);
            coreSupplyDetailEntity.setCredate(new Timestamp(System.currentTimeMillis()));
            coreSupplyDetailEntity.setBatchCode(
                    sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.CORE_SUPPLY_DETAIL_BATCH_CODE.getKey()));
            //新增供应单明细表
            coreSupplyDetailService.insert(coreSupplyDetailEntity);

            // 生成二维码标签图片
            String tempPath = String.format(FILE_PATH_TEMPLATE, UUID.randomUUID() + ".jpg");
            // 二维码存放内容（商品规格编码 + 批号 + 批次码）
            Integer codeWidth = null;
            Integer codeHeight = null;
            try {
                codeWidth = Integer.valueOf(sysConfigService.getValue(SysConfigEnum.QRCODE_BATCH_WIDTH.getKey()));
                codeHeight = Integer.valueOf(sysConfigService.getValue(SysConfigEnum.QRCODE_BATCH_HEIGHT.getKey()));
            } catch (Exception e) {
                throw new HdiException("获取批次二维码高/宽度错误，请检查参数配置！");
            }
            String specsCode = coreSupplyDetailService.getHospitalGoodsSpecsCode(coreSupplyDetailEntity.getGoodsclass(),
                    coreSupplyDetailEntity.getGoodsid(), coreSupplyDetailEntity.getGoodstypeid(), coreSupplyMasterEntity.getHorgId(), coreSupplyMasterEntity.getSupplierId());
            QRCodeUtil.zxingCodeCreate(specsCode + coreSupplyDetailEntity.getLotno()
                    + coreSupplyDetailEntity.getBatchCode(), codeWidth, codeHeight, tempPath, "jpg");
            // 上传标签图片到FastDFS
            File pdfFile = new File(tempPath);
            // 上传文件到FastDFS
            String qrCodeUrl = fastDFSClientUtils.uploadFile(pdfFile);
            // 删除临时文件
            pdfFile.delete();
            // 更新商品批次码图片地址
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
        if (StringUtil.isEmpty(coreSupplyDetailEntity.getLotno())) {
            throw new HdiException("生产批号编码不能为空");
        }
        if (StringUtil.isEmpty(coreSupplyDetailEntity.getSupplyQty())) {
            throw new HdiException("供货数量不能为空");
        }
        if (StringUtil.isEmpty(coreSupplyDetailEntity.getSupplyUnitprice())) {
            throw new HdiException("供货单价不能为空");
        }
    }

    //---供货验收单展示---
    @Override
    public MasterDetailsCommonEntity<CoreAcceptMasterEntity, CoreAcceptDetailEntity> supplyAcceptList(
            CoreSupplyMasterEntity entity, SysUserEntity user) {
        if (user.getUserType() != 1) {
            throw new HdiException("当前登录用户无此权限");
        }
        MasterDetailsCommonEntity<CoreAcceptMasterEntity, CoreAcceptDetailEntity> masterDetailsCommonEntity = new MasterDetailsCommonEntity<>();
        CoreSupplyMasterEntity masterEntity = this.baseMapper.selectBySupplyMasterId(entity.getSupplyMasterId());

        Map<String, String> hashMap = new HashMap<>();
        String acceptno = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.CORE_ACCEPT_MASTER_ACCEPTNO.getKey());
        //供货单主单 与 验收单主单 字段不一样建立映射关系
        hashMap.put("supplyMasterId", "sourceid");
        hashMap.put("sourcesSupplierId", "hisSupplyid");
        hashMap.put("sourcesHospitalId", "uorganid");
        hashMap.put("sourcesStorehouseId", "ystorehouseid");

        //根据映射关系以及供货单主单信息生成验收单主单信息；
        CoreAcceptMasterEntity acceptMasterEntity = ReflectUitls.transform(masterEntity, CoreAcceptMasterEntity.class,
                hashMap, false);
        acceptMasterEntity.setAcceptno(acceptno);
        masterDetailsCommonEntity.setMaster(acceptMasterEntity);

        List<CoreAcceptDetailEntity> list = new ArrayList<>();
        //获取供货单明细单集合，根据映射生成验收单明细单集合；
        List<CoreSupplyDetailEntity> detailEntityList = coreSupplyDetailServiceImpl
                .selectSupplyDetail(masterEntity.getSupplyMasterId());
        for (CoreSupplyDetailEntity coreSupplyDetailEntity : detailEntityList) {
            //供货单明细单 与 验收单明细单 字段不一样建立映射关系
            Map<String, String> dHashMap = new HashMap<>();
            dHashMap.put("supplyQty", "acceptQty");
            dHashMap.put("supplyMasterId", "sourceid");
            dHashMap.put("supplyDetailId", "sourcedtlid");

            CoreAcceptDetailEntity detailEntity = ReflectUitls.transform(coreSupplyDetailEntity,
                    CoreAcceptDetailEntity.class, dHashMap, false);

            //将供应商商品信息转化为医院商品信息及平台商品信息
            CoreAcceptDetailEntity tempAcceptDetailEntity = coreAcceptDetailServiceImpl.selectHgoodsInfo(coreSupplyDetailEntity.getGoodsclass(), coreSupplyDetailEntity.getGoodstypeid(), coreSupplyDetailEntity.getGoodsid(), masterEntity.getHorgId(), masterEntity.getSupplierId());
            if (tempAcceptDetailEntity != null) {
//				BeanUtils.copyProperties(tempAcceptDetailEntity,detailEntity);
                detailEntity.setYgoodsid(tempAcceptDetailEntity.getYgoodsid());
                detailEntity.setYgoodsno(tempAcceptDetailEntity.getYgoodsno());
                detailEntity.setYgoodsname(tempAcceptDetailEntity.getYgoodsname());
                detailEntity.setYgoodstypeid(tempAcceptDetailEntity.getYgoodstypeid());
                detailEntity.setYgoodstypeno(tempAcceptDetailEntity.getYgoodstypeno());
                detailEntity.setYgoodstypename(tempAcceptDetailEntity.getYgoodstypename());
                detailEntity.setGoodsid(tempAcceptDetailEntity.getGoodsid());
                detailEntity.setGoodsno(tempAcceptDetailEntity.getGoodsno());
                detailEntity.setGoodsname(tempAcceptDetailEntity.getGoodsname());
                detailEntity.setGoodstypeid(tempAcceptDetailEntity.getGoodstypeid());
                detailEntity.setGoodstypeno(tempAcceptDetailEntity.getGoodstypeno());
                detailEntity.setGoodstype(tempAcceptDetailEntity.getGoodstype());
            }
            //验收数量，唯一码赋值
            Integer leaveAcceptQty = coreSupplyDetailServiceImpl.getDetailLeaveAcceptQty(detailEntity.getSourcedtlid());
            detailEntity.setAcceptQty(leaveAcceptQty.doubleValue());
            detailEntity.setUniqueId(Long.valueOf((coreSupplyDetailEntity.getSupplyMasterId().toString()
                    + coreSupplyDetailEntity.getSupplyDetailId().toString())));


            list.add(detailEntity);
        }
        masterDetailsCommonEntity.setDetails(list);

        return masterDetailsCommonEntity;
    }

    //---供货验收单保存---
    @Override
    public void supplyAcceptSave(MasterDetailsCommonEntity<CoreAcceptMasterEntity, CoreAcceptDetailEntity> entity,
                                 Long deptId, Long userId, String username) {
        CoreAcceptMasterEntity master = entity.getMaster();
        CoreSupplyMasterEntity coreSupplyMasterEntity = this.baseMapper.selectByMasterId(master.getSourceid());
        if (Objects.equals(SupplyStatusEnum.ACCEPTED.getKey(), coreSupplyMasterEntity.getSupplyStatus())) {
            throw new HdiException("生成验收单失败：已完成验收!");
        }
        Map<String, String> hashMap = new HashMap<>();
        //供货单主单 与 验收单主单 字段不一样建立映射关系
        hashMap.put("supplyMasterId", "sourceid");
        hashMap.put("sourcesSupplierId", "hisSupplyid");
        hashMap.put("sourcesHospitalId", "uorganid");
        hashMap.put("sourcesStorehouseId", "ystorehouseid");

        //根据映射关系以及供货单主单信息生成验收单主单信息；
        CoreAcceptMasterEntity acceptMasterEntity = ReflectUitls.transform(coreSupplyMasterEntity, CoreAcceptMasterEntity.class,
                hashMap, false);

        CoreSupplyMasterEntity supplierId = this.baseMapper.getSupplierId(deptId);

        acceptMasterEntity.setSupplierId(supplierId.getSupplierId());
        acceptMasterEntity.setSettleFlag(0);
        acceptMasterEntity.setDeptId(deptId);
        acceptMasterEntity.setCremanid(userId);
        acceptMasterEntity.setCredate(new Timestamp(System.currentTimeMillis()));
        acceptMasterEntity.setDatasource(1);
        //前端手动输入验收单编号或者系统生成验收单编号；
        if (master.getAcceptno() == null || master.getAcceptno() == "") {
            String acceptno = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.CORE_ACCEPT_MASTER_ACCEPTNO.getKey());
            acceptMasterEntity.setAcceptno(acceptno);
        } else {
            acceptMasterEntity.setAcceptno(master.getAcceptno());
        }
        acceptMasterEntity.setCredate(master.getCredate());
        CoreAcceptMasterEntity acceptMaster = coreAcceptMasterServiceImpl.selectAcceptMaster(deptId,
                master.getHorgId(), master.getStorehouseid());
        if (acceptMaster == null) {
            throw new HdiException("无验收单");
        }

        acceptMasterEntity.setUorganid(acceptMaster.getUorganid());
        acceptMasterEntity.setYstorehouseid(acceptMaster.getYstorehouseid());
        acceptMasterEntity.setHisSupplyid(acceptMaster.getHisSupplyid());
        acceptMasterEntity.setDelFlag(0);

        coreAcceptMasterServiceImpl.insert(acceptMasterEntity);

        List<CoreAcceptDetailEntity> details = entity.getDetails();
        for (CoreAcceptDetailEntity coreAcceptDetailEntity : details) {
            Integer leaveAcceptQty = coreSupplyDetailServiceImpl.getDetailLeaveAcceptQty(coreAcceptDetailEntity.getSourcedtlid());
            if (coreAcceptDetailEntity.getAcceptQty() > leaveAcceptQty.doubleValue()) {
                throw new HdiException("验收数量不能大于供货数量！");
            }
            List<Map<String, Object>> goodsList = this.baseMapper.selectByGoodsId(coreAcceptDetailEntity.getGoodsid(),
                    coreAcceptDetailEntity.getGoodsclass(), coreAcceptDetailEntity.getGoodstypeid());
            if (goodsList.size() > 0) {
                coreAcceptDetailEntity.setAcceptMasterId(acceptMasterEntity.getAcceptMasterId());
                coreAcceptDetailEntity.setGoodsclass((Integer) goodsList.get(0).get("goodsclass"));
                coreAcceptDetailEntity.setGoodsid((Long) goodsList.get(0).get("goodsid"));
                coreAcceptDetailEntity.setGoodstypeid((Long) goodsList.get(0).get("goodstypeid"));
                coreAcceptDetailEntity.setGoodsunit((String) goodsList.get(0).get("goodsunit"));
                coreAcceptDetailEntity.setYgoodstypeid((String) goodsList.get(0).get("Ygoodstypeid"));
                coreAcceptDetailEntity.setCremanid(userId);
                coreAcceptDetailEntity.setCredate(new Timestamp(System.currentTimeMillis()));
                coreAcceptDetailService.insert(coreAcceptDetailEntity);
            } else {
                throw new HdiException("该细单商品数据有误");
            }
        }

    }

    //---更新供货状态---
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSupplyStatus(Long supplyMasterId) {
        Integer supplyNumber = coreSupplyDetailService.getSupplyNumberBySupplyMasterId(supplyMasterId);
        Integer acceptNumber = coreAcceptDetailService.getAcceptNumberBySupplyMasterId(supplyMasterId);
        CoreSupplyMasterEntity coreSupplyMasterEntity = new CoreSupplyMasterEntity();
        coreSupplyMasterEntity.setSupplyMasterId(supplyMasterId);
        coreSupplyMasterEntity.setSupplyStatus(acceptNumber < supplyNumber ? SupplyStatusEnum.PART_ACCEPTED.getKey() : SupplyStatusEnum.ACCEPTED.getKey());
        this.baseMapper.updateById(coreSupplyMasterEntity);

    }

    //---生成标签保存---
    @Override
    public MasterDetailsCommonEntity<CoreSupplyMasterEntity, CoreSupplyDetailEntity> supplyLabelList(
            CoreSupplyMasterEntity coreSupplyMaster, SysUserEntity user) {
        if (user.getUserType() != 1) {
            throw new HdiException("当前登录用户无此权限");
        }
        MasterDetailsCommonEntity<CoreSupplyMasterEntity, CoreSupplyDetailEntity> masterDetailsCommonEntity = new MasterDetailsCommonEntity<>();
        CoreSupplyMasterEntity masterEntity = this.baseMapper
                .selectBySupplyMasterId(coreSupplyMaster.getSupplyMasterId());
        masterDetailsCommonEntity.setMaster(masterEntity);

        List<CoreSupplyDetailEntity> list = new ArrayList<>();
        List<CoreSupplyDetailEntity> selectSupplyDetail = coreSupplyDetailServiceImpl
                .selectSupplyDetail(coreSupplyMaster.getSupplyMasterId());
        for (CoreSupplyDetailEntity coreSupplyDetailEntity : selectSupplyDetail) {
            if (coreSupplyDetailEntity.getUnpackagedNumber() == null) {
                coreSupplyDetailEntity.setUnpackagedNumber(coreSupplyDetailEntity.getSupplyQty());
            }
            if (coreSupplyDetailEntity.getUnpackagedNumber() < 0) {
                coreSupplyDetailEntity.setUnpackagedNumber(0d);
            }
            coreSupplyDetailEntity.setLabelQty(coreSupplyDetailEntity.getUnpackagedNumber());
            coreSupplyDetailEntity.setDmlFlag(1);
            list.add(coreSupplyDetailEntity);
        }
        masterDetailsCommonEntity.setDetails(list);

        return masterDetailsCommonEntity;
    }

    //---生成标签保存---
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long supplyLabelSave(MasterDetailsCommonEntity<CoreSupplyMasterEntity, CoreSupplyDetailEntity> entity,
                                Long deptId, Long userId, String username) throws IOException {

        CoreLabelMasterEntity labelMaster = new CoreLabelMasterEntity();
//		CoreSupplyMasterEntity supplierId = this.baseMapper.getSupplierId(deptId); //改造前
        CoreSupplyMasterEntity coreSupplyMasterEntity = entity.getMaster();
        //根据部门id,医院id，库房id 获取平台/原系统--供应商/医院/库房等信息；---改造后新增字段添加

        CoreSupplyMasterEntity masterEntity = this.baseMapper.selectById(coreSupplyMasterEntity.getSupplyMasterId());

//		CoreLabelMasterEntity labelMaster = ReflectUitls.transform(masterEntity,CoreLabelMasterEntity.class);
//		CoreSupplyMasterEntity supplierId = this.baseMapper.getSupplierIdNew(coreSupplyMasterEntity.getDeptId(),coreSupplyMasterEntity.getStorehouseid(),coreSupplyMasterEntity.getHorgId());
        coreSupplyMasterEntity.setSupplierId(masterEntity.getSupplierId());
        labelMaster.setSupplierCode(masterEntity.getSupplierCode());
        labelMaster.setSupplierName(masterEntity.getSupplierName());
        labelMaster.setHospitalCode(masterEntity.getHospitalCode());
        labelMaster.setHospitalName(masterEntity.getHospitalName());
        labelMaster.setSourcesSupplierId(masterEntity.getSourcesSupplierId());
        labelMaster.setSourcesSupplierName(masterEntity.getSourcesSupplierName());
        labelMaster.setSourcesHospitalId(masterEntity.getSourcesHospitalId());
        labelMaster.setSourcesHospitalName(masterEntity.getSourcesHospitalName());
        labelMaster.setSourcesStorehouseId(masterEntity.getSourcesStorehouseId());
        labelMaster.setStorehouseNo(masterEntity.getStorehouseNo());
        labelMaster.setStorehouseName(masterEntity.getStorehouseName());
        labelMaster.setSupplierId(masterEntity.getSupplierId());
        labelMaster.setDeptId(deptId);
        labelMaster.setCremanid(userId);
//		labelMaster.setCremanname(username);
        labelMaster.setCredate(new Timestamp(System.currentTimeMillis()));
        labelMaster.setStorehouseid(entity.getMaster().getStorehouseid());
        labelMaster.setHorgId(entity.getMaster().getHorgId());
        labelMaster.setLabelstatus(0);
        labelMaster.setSourceid(entity.getMaster().getSupplyMasterId());
        labelMaster.setDelFlag(0);

        labelMaster.setLabelno(entity.getMaster().getSupplyno()
                + sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.CORE_LABEL_MASTER_LABELNO.getKey()));
        //添加标签主单
        coreLabelMasterServiceImpl.insert(labelMaster);

        // 生成二维码标签图片
        String tempPath = String.format(FILE_PATH_TEMPLATE, UUID.randomUUID() + ".jpg");
        Integer codeWidth = null;
        Integer codeHeight = null;
        try {

            codeWidth = Integer.valueOf(sysConfigService.getValue(SysConfigEnum.QRCODE_LABEL_WIDTH.getKey()));
            codeHeight = Integer.valueOf(sysConfigService.getValue(SysConfigEnum.QRCODE_LABEL_HEIGHT.getKey()));

        } catch (Exception e) {
            throw new HdiException("获取标签二维码高/宽度错误，请检查参数配置！");
        }
        QRCodeUtil.zxingCodeCreate(labelMaster.getLabelno(), codeWidth, codeHeight, tempPath, "jpg");
        // 上传标签图片到FastDFS
        File pdfFile = new File(tempPath);
        // 上传文件到FastDFS
        String qrCodeUrl = fastDFSClientUtils.uploadFile(pdfFile);
        // 删除临时文件
        pdfFile.delete();
        // 更新商品标签地址
        CoreLabelMasterEntity upLabelMaster = new CoreLabelMasterEntity();
        upLabelMaster.setLabelid(labelMaster.getLabelid());
        upLabelMaster.setImageUrl(qrCodeUrl);
        coreLabelMasterService.updateById(upLabelMaster);

        List<CoreSupplyDetailEntity> details = entity.getDetails();
        for (CoreSupplyDetailEntity coreSupplyDetailEntity : details) {
            CoreLabelDetailEntity labelDetailEntity = new CoreLabelDetailEntity();
            labelDetailEntity.setLabelid(labelMaster.getLabelid());
            //LabelQty供货单位/商品单位
            labelDetailEntity.setLabelQty(coreSupplyDetailEntity.getLabelQty());
            labelDetailEntity.setSupplyMasterId(coreSupplyDetailEntity.getSupplyMasterId());
            labelDetailEntity.setSupplyDetailId(coreSupplyDetailEntity.getSupplyDetailId());
            labelDetailEntity.setDmlFlag(coreSupplyDetailEntity.getDmlFlag());
            coreLabelDetailService.insert(labelDetailEntity);
        }
        return upLabelMaster.getLabelid();
    }

    //---一键生成标签保存---
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> autoSupplyLabelSave(MasterDetailsCommonEntity<CoreSupplyMasterEntity, CoreSupplyDetailEntity> entity, Long deptId, Long userId, String username) throws IOException {
        Map<String, Object> map = new HashMap<>(16);
        Integer failCount = 0;
        //记录行数
        Integer count = 0;
        //根据细单数量循环，每个细单生成一组标签
        List<CoreSupplyDetailEntity> details = entity.getDetails();
        for (CoreSupplyDetailEntity coreSupplyDetailEntity : details) {
            count++;
            //根据商品类型+供应商商品id查 供货单位与商品单位比值
            Integer goodsClass = coreSupplyDetailEntity.getGoodsclass();
            Long goodsId = coreSupplyDetailEntity.getGoodsid();
            //供货单位与商品单位比值
            String convertUnit = coreSupplyDetailService.selectConvert(goodsClass, goodsId);
            if (StringUtil.isEmpty(convertUnit)) {
                failCount++;
                continue;
            }
            //包装数量=供货单位与商品单位比值（向上取整）
            String[] split = convertUnit.split(":");
            Double labelQty = Math.ceil(Double.valueOf(split[0]) / Double.valueOf(split[1]));
            Double unpackagedNumber = coreSupplyDetailEntity.getUnpackagedNumber();

            double ceil = Math.ceil(unpackagedNumber / labelQty);
            //根据未包装数量/包装数量（向上取整）决定打几个包
            for (int i = 0; i < ceil; i++) {
                //判断包装数量，最后一个包装数量为最终剩下的数量
                if (labelQty > unpackagedNumber) {
                    labelQty = unpackagedNumber;
                } else {
                    unpackagedNumber = unpackagedNumber - labelQty;
                }
                CoreLabelMasterEntity labelMaster = new CoreLabelMasterEntity();
                CoreSupplyMasterEntity coreSupplyMasterEntity = entity.getMaster();
                CoreSupplyMasterEntity masterEntity = this.baseMapper.selectById(coreSupplyMasterEntity.getSupplyMasterId());
                coreSupplyMasterEntity.setSupplierId(masterEntity.getSupplierId());
                labelMaster.setSupplierCode(masterEntity.getSupplierCode());
                labelMaster.setSupplierName(masterEntity.getSupplierName());
                labelMaster.setHospitalCode(masterEntity.getHospitalCode());
                labelMaster.setHospitalName(masterEntity.getHospitalName());
                labelMaster.setSourcesSupplierId(masterEntity.getSourcesSupplierId());
                labelMaster.setSourcesSupplierName(masterEntity.getSourcesSupplierName());
                labelMaster.setSourcesHospitalId(masterEntity.getSourcesHospitalId());
                labelMaster.setSourcesHospitalName(masterEntity.getSourcesHospitalName());
                labelMaster.setSourcesStorehouseId(masterEntity.getSourcesStorehouseId());
                labelMaster.setStorehouseNo(masterEntity.getStorehouseNo());
                labelMaster.setStorehouseName(masterEntity.getStorehouseName());
                labelMaster.setSupplierId(masterEntity.getSupplierId());
                labelMaster.setDeptId(deptId);
                labelMaster.setCremanid(userId);
                labelMaster.setCredate(new Timestamp(System.currentTimeMillis()));
                labelMaster.setStorehouseid(entity.getMaster().getStorehouseid());
                labelMaster.setHorgId(entity.getMaster().getHorgId());
                labelMaster.setLabelstatus(0);
                labelMaster.setSourceid(entity.getMaster().getSupplyMasterId());
                labelMaster.setDelFlag(0);

                labelMaster.setLabelno(entity.getMaster().getSupplyno()
                        + sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.CORE_LABEL_MASTER_LABELNO.getKey()));
                //添加标签主单
                coreLabelMasterServiceImpl.insert(labelMaster);

                // 生成二维码标签图片
                String tempPath = String.format(FILE_PATH_TEMPLATE, UUID.randomUUID() + ".jpg");
                Integer codeWidth = null;
                Integer codeHeight = null;
                try {
                    codeWidth = Integer.valueOf(sysConfigService.getValue(SysConfigEnum.QRCODE_LABEL_WIDTH.getKey()));
                    codeHeight = Integer.valueOf(sysConfigService.getValue(SysConfigEnum.QRCODE_LABEL_HEIGHT.getKey()));

                } catch (Exception e) {
                    throw new HdiException("获取标签二维码高/宽度错误，请检查参数配置！");
                }
                QRCodeUtil.zxingCodeCreate(labelMaster.getLabelno(), codeWidth, codeHeight, tempPath, "jpg");
                // 上传标签图片到FastDFS
                File pdfFile = new File(tempPath);
                // 上传文件到FastDFS
                String qrCodeUrl = fastDFSClientUtils.uploadFile(pdfFile);
                // 删除临时文件
                pdfFile.delete();
                // 更新商品标签地址
                CoreLabelMasterEntity upLabelMaster = new CoreLabelMasterEntity();
                upLabelMaster.setLabelid(labelMaster.getLabelid());
                upLabelMaster.setImageUrl(qrCodeUrl);
                coreLabelMasterService.updateById(upLabelMaster);

                CoreLabelDetailEntity labelDetailEntity = new CoreLabelDetailEntity();
                labelDetailEntity.setLabelid(labelMaster.getLabelid());
                labelDetailEntity.setLabelQty(labelQty);
                labelDetailEntity.setSupplyMasterId(coreSupplyDetailEntity.getSupplyMasterId());
                labelDetailEntity.setSupplyDetailId(coreSupplyDetailEntity.getSupplyDetailId());
                labelDetailEntity.setDmlFlag(coreSupplyDetailEntity.getDmlFlag());
                coreLabelDetailService.insert(labelDetailEntity);
            }
        }
        if (failCount > 0) {
            map.put("failCount", failCount);
            return map;
        }
        return map;
    }

    //---供货单编辑---
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MasterDetailsCommonEntity<CoreSupplyMasterEntity, CoreSupplyDetailEntity> editList(
            CoreSupplyMasterEntity coreSupplyMaster, SysUserEntity user) {
        if (user.getUserType() != 1) {
            throw new HdiException("当前登录用户无此权限");
        }
        CoreSupplyMasterEntity masterEntity = this.baseMapper
                .selectBySupplyMasterId(coreSupplyMaster.getSupplyMasterId());
        if (masterEntity.getSupplyStatus() == 1 || masterEntity.getDatasource() == 2) {
            throw new HdiException("通过接口上传或已提交医院的供货单不可编辑");
        }

        MasterDetailsCommonEntity<CoreSupplyMasterEntity, CoreSupplyDetailEntity> masterDetailsCommonEntity = new MasterDetailsCommonEntity<>();
        masterDetailsCommonEntity.setMaster(masterEntity);

        List<CoreSupplyDetailEntity> detailEntityList = coreSupplyDetailServiceImpl
                .selectSupplyDetail(masterEntity.getSupplyMasterId());
        if (detailEntityList != null) {
            for (CoreSupplyDetailEntity coreSupplyDetailEntity : detailEntityList) {
                coreSupplyDetailEntity.setDmlFlag(2);
            }
            masterDetailsCommonEntity.setDetails(detailEntityList);
        }

        return masterDetailsCommonEntity;
    }

    //---供货单编辑保存---
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editSave(MasterDetailsCommonEntity<CoreSupplyMasterEntity, CoreSupplyDetailEntity> entity, Long userId,
                         String username, Long deptId) {
        CoreSupplyMasterEntity coreSupplyMasterEntity = entity.getMaster();

        //根据部门id,医院id，库房id 获取平台/原系统--供应商/医院/库房等信息；---改造后新增字段添加
        CoreSupplyMasterEntity supplierId = this.baseMapper.getSupplierIdNew(deptId, coreSupplyMasterEntity.getStorehouseid(), coreSupplyMasterEntity.getHorgId());
        coreSupplyMasterEntity.setSupplierId(supplierId.getSupplierId());
        coreSupplyMasterEntity.setDeptId(deptId);
        coreSupplyMasterEntity.setSupplierCode(supplierId.getSupplierCode());
        coreSupplyMasterEntity.setSupplierName(supplierId.getSupplierName());
        coreSupplyMasterEntity.setHorgId(supplierId.getHorgId());
        coreSupplyMasterEntity.setHospitalCode(supplierId.getHospitalCode());
        coreSupplyMasterEntity.setHospitalName(supplierId.getHospitalName());
        coreSupplyMasterEntity.setSourcesSupplierId(supplierId.getSourcesSupplierId());
        coreSupplyMasterEntity.setSourcesSupplierName(supplierId.getSourcesSupplierName());
        coreSupplyMasterEntity.setSourcesHospitalId(supplierId.getSourcesHospitalId());
        coreSupplyMasterEntity.setSourcesHospitalName(supplierId.getSourcesHospitalName());
        coreSupplyMasterEntity.setSourcesStorehouseId(supplierId.getSourcesStorehouseId());
        coreSupplyMasterEntity.setStorehouseid(supplierId.getStorehouseid());
        coreSupplyMasterEntity.setStorehouseNo(supplierId.getStorehouseNo());
        coreSupplyMasterEntity.setStorehouseName(supplierId.getStorehouseName());

        coreSupplyMasterEntity.setEditId(userId);
        coreSupplyMasterEntity.setEditTime(new Timestamp(System.currentTimeMillis()));

        //更新供货单主单信息
        this.baseMapper.updateById(coreSupplyMasterEntity);
        List<CoreSupplyDetailEntity> list = entity.getDetails();
        //更新供货单明细表信息 dmlFlag=2，新增的供货单明细表信息 dmlFlag=1；
        for (CoreSupplyDetailEntity coreSupplyDetailEntity : list) {

            //根据条件查询批号表的数据是否存在
            List<Map<String, Object>> lotMapList = coreSupplyDetailService.selectLot(
                    coreSupplyDetailEntity.getGoodsid(), coreSupplyDetailEntity.getGoodsclass(),
                    coreSupplyDetailEntity.getGoodstypeid(), coreSupplyDetailEntity.getLotno());
            if (StringUtil.isEmpty(lotMapList)) {
                //保存不存在的批号信息
                CoreLotEntity lotEntity = coreLotService.saveLot(coreSupplyDetailEntity.getGoodsid(),
                        coreSupplyDetailEntity.getGoodsclass(), coreSupplyDetailEntity.getGoodstypeid(),
                        coreSupplyDetailEntity.getLotno(), deptId, coreSupplyDetailEntity.getProddate(),
                        coreSupplyDetailEntity.getInvadate());

                coreSupplyDetailEntity.setLotid(lotEntity.getLotid());
            }

            if (coreSupplyDetailEntity.getDmlFlag() == 1) {
                coreSupplyDetailEntity.setCremanid(userId);
                coreSupplyDetailEntity.setCredate(new Timestamp(System.currentTimeMillis()));
                coreSupplyDetailEntity.setBatchCode(
                        sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.CORE_SUPPLY_DETAIL_BATCH_CODE.getKey()));
                coreSupplyDetailService.insert(coreSupplyDetailEntity);
                //生成二维码
                generateQRcode(coreSupplyMasterEntity, coreSupplyDetailEntity, userId);
            } else if (coreSupplyDetailEntity.getDmlFlag() == 2) {
                coreSupplyDetailEntity.setEditId(userId);
                coreSupplyDetailEntity.setEditTime(new Timestamp(System.currentTimeMillis()));
                //兼容
                CoreSupplyDetailEntity detailEntity = coreSupplyDetailService.selectById(coreSupplyDetailEntity.getSupplyDetailId());
                if (detailEntity != null && StringUtil.isEmpty(detailEntity.getBatchCode())) {
                    coreSupplyDetailEntity.setBatchCode(
                            sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.CORE_SUPPLY_DETAIL_BATCH_CODE.getKey()));
                }
                //生成二维码
                generateQRcode(coreSupplyMasterEntity, coreSupplyDetailEntity, userId);
                coreSupplyDetailService.updateById(coreSupplyDetailEntity);

            } else {
                coreSupplyDetailService.deleteById(coreSupplyDetailEntity.getSupplyDetailId());
            }
        }
    }

    // ----提交医院----
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitToHospital(CoreSupplyMasterEntity entity, SysUserEntity user) {
        if (!user.getUserType().equals(TypeEnum.USER_SUPPLIER.getKey())) {
            throw new HdiException("当前登录用户无此权限");
        }
        sendCoreSupplyData(entity);

    }

    /**
     * 下发供货单数据
     *
     * @param entity
     */
    @Override
    public void sendCoreSupplyData(CoreSupplyMasterEntity entity) {
        //获取供货主单
        CoreSupplyMasterEntity coreSupplyMasterEntity = this.baseMapper.selectByMasterId(entity.getSupplyMasterId());
        if (!SupplyStatusEnum.UNCOMMITTED.getKey().equals(coreSupplyMasterEntity.getSupplyStatus())) {
            throw new HdiException(MessageFormat.format("提交失败！供货单编号:{0}已提交医院，请勿重复提交！", coreSupplyMasterEntity.getSupplyno()));
        }
        // 获取该供货主单的所有商品标签主单
        List<CoreLabelMasterEntity> labelMasterEntityList = coreLabelMasterService
                .getCoreLabelMasterEntity(coreSupplyMasterEntity.getSupplyMasterId());

        // 校验是否存在未生成标签供货单商品
        Integer labelQty = this.baseMapper.selectLabelQtyByMasterId(entity.getSupplyMasterId());
        Integer supplyQty = this.baseMapper.selectSupplyQtyByMasterId(entity.getSupplyMasterId());
        if (labelQty < supplyQty) {
            throw new HdiException("供货单中存在未生成标签的商品，提交失败！");
        }

        for (CoreLabelMasterEntity coreLabelMasterEntity : labelMasterEntityList) {
            Map<String, Object> spdRg = tempSpdRgdtlDao.getSpdRgdtl(coreLabelMasterEntity.getLabelid());
            if (null != spdRg && (Long) spdRg.get("count") > 0) {
                throw new HdiException(MessageFormat.format("标签id:{0}已提交医院", coreLabelMasterEntity.getLabelid()));
            }

            TempSpdRgEntity tempSpdRgEntity = new TempSpdRgEntity();
            Map<String, Object> sourcesIdMap = this.baseMapper.selectByHorgIdAndSupplierId(coreSupplyMasterEntity.getHorgId(),
                    coreSupplyMasterEntity.getSupplierId());
            if (sourcesIdMap != null) {
                if (sourcesIdMap.get("uorganid") != null) {
                    tempSpdRgEntity.setUorganid(sourcesIdMap.get("uorganid").toString());
                }
                if (sourcesIdMap.get("uorganno") != null) {
                    tempSpdRgEntity.setUorganno(sourcesIdMap.get("uorganno").toString());
                }
                if (sourcesIdMap.get("uorganname") != null) {
                    tempSpdRgEntity.setUorganname(sourcesIdMap.get("uorganname").toString());
                }
                if (sourcesIdMap.get("supplyid") != null) {
                    tempSpdRgEntity.setSupplyid(sourcesIdMap.get("supplyid").toString());
                }
                if (sourcesIdMap.get("supplyno") != null) {
                    tempSpdRgEntity.setSupplyno(sourcesIdMap.get("supplyno").toString());
                }
                if (sourcesIdMap.get("supplyname") != null) {
                    tempSpdRgEntity.setSupplyname(sourcesIdMap.get("supplyname").toString());
                }
                tempSpdRgEntity.setRgno(SequenceEnum.TEMP_SPD_RG.getKey());
                tempSpdRgEntity.setRgstatus(new BigDecimal(ONE));
                tempSpdRgEntity.setRgtype(new BigDecimal(ONE));

                tempSpdRgEntity.setSourceid(coreSupplyMasterEntity.getSupplyMasterId().toString());

                tempSpdRgEntity.setOriginid(coreSupplyMasterEntity.getSalno());
                tempSpdRgEntity.setOriginno(coreSupplyMasterEntity.getSalno());
                Map<String, Object> orgdataidMap = this.baseMapper
                        .getOrgdataid(coreSupplyMasterEntity.getStorehouseid());
                if (null != orgdataidMap && null != orgdataidMap.get("orgdataid")) {
                    tempSpdRgEntity.setShaddressid((String) orgdataidMap.get("orgdataid"));
                }
                tempSpdRgEntity.setCredate(new Timestamp(System.currentTimeMillis()));

                String rgid = UUID.randomUUID().toString().replaceAll("-", "");
                tempSpdRgEntity.setRgid(rgid);
                tempSpdRgDao.insert(tempSpdRgEntity);

                List<CoreSupplyDetailEntity> list = coreSupplyDetailService
                        .selectBySupplyMasterId(coreSupplyMasterEntity.getSupplyMasterId());
                for (CoreSupplyDetailEntity coreSupplyDetailEntity : list) {
                    //获取标签明细单：
                    List<Map<String, Object>> labelEntitys = tempSpdRgdtlDao
                            .getLabelEntity(coreSupplyDetailEntity.getSupplyDetailId(), coreLabelMasterEntity.getLabelid());
                    for (Map<String, Object> map : labelEntitys) {
                        TempSpdRgdtlEntity tempSpdRgdtlEntity = tempSpdRgdtlDao.selectGoods(
                                coreSupplyDetailEntity.getGoodsid(), coreSupplyDetailEntity.getGoodstypeid(),
                                coreSupplyDetailEntity.getGoodstype(), coreSupplyDetailEntity.getGoodsclass(), coreSupplyMasterEntity.getHorgId());
                        tempSpdRgdtlEntity.setSourceid(String.valueOf(map.get("labelid")));
                        tempSpdRgdtlEntity.setSourcedtlid(String.valueOf(map.get("labeldtlid")));

                        //批准文号
                        if (coreSupplyDetailEntity.getGoodsclass() == 1) {
                            Map<String, Object> drugsApprovals = tempSpdRgdtlDao
                                    .selectDrugsApprovals(tempSpdRgdtlEntity.getHospitalGoodsId());
                            if (null != drugsApprovals && !StringUtil.isEmpty(drugsApprovals.get("approvals"))) {
                                tempSpdRgdtlEntity.setApprovedocno(String.valueOf(drugsApprovals.get("approvals")));
                            }
                        } else if (coreSupplyDetailEntity.getGoodsclass() == 2) {
                            Map<String, Object> reagentApprovals = tempSpdRgdtlDao
                                    .selectReagentApprovals(tempSpdRgdtlEntity.getHospitalGoodsId());
                            if (null != reagentApprovals && !StringUtil.isEmpty(reagentApprovals.get("approvals"))) {
                                tempSpdRgdtlEntity.setApprovedocno(String.valueOf(reagentApprovals.get("approvals")));
                            }
                        } else {
                            Map<String, Object> consumablesApprovals = tempSpdRgdtlDao
                                    .selectConsumablesApprovals(tempSpdRgdtlEntity.getHospitalGoodsId());
                            if (null != consumablesApprovals && !StringUtil.isEmpty(consumablesApprovals.get("approvals"))) {
                                tempSpdRgdtlEntity.setApprovedocno(String.valueOf(consumablesApprovals.get("approvals")));
                            }
                        }
                        //医院商品单位
                        if (!StringUtil.isEmpty(tempSpdRgdtlEntity.getHgoodsunit())) {
                            SysDictEntity dict = sysDictService.selectDictByCode("goods_unit", tempSpdRgdtlEntity.getHgoodsunit());
                            tempSpdRgdtlEntity.setHgoodsunit(dict.getValue());
                        }

                        //供应商商品单位
                        if (!StringUtil.isEmpty(tempSpdRgdtlEntity.getSgoodsunit())) {
                            SysDictEntity dict = sysDictService.selectDictByCode("goods_unit", tempSpdRgdtlEntity.getSgoodsunit());
                            tempSpdRgdtlEntity.setSgoodsunit(dict.getValue());
                        }

                        //库房
                        if (null != orgdataidMap && null != orgdataidMap.get("orgdataid")) {
                            tempSpdRgdtlEntity.setStorehouseid((String) orgdataidMap.get("orgdataid"));
                        }
                        // TODO 收货细单的原始主单细单标识；
                        tempSpdRgdtlEntity.setOriginid(coreSupplyMasterEntity.getSalno());
                        tempSpdRgdtlEntity.setOriginno(coreSupplyMasterEntity.getSalno());

                        tempSpdRgdtlEntity.setSunitprice(new BigDecimal(coreSupplyDetailEntity.getSupplyUnitprice()));
                        tempSpdRgdtlEntity.setSgoodsqty(new BigDecimal((Double) map.get("label_qty")));
                        tempSpdRgdtlEntity.setHgoodsqty(new BigDecimal((Double) map.get("label_qty")));

                        if (coreSupplyDetailEntity.getLotid() != null) {
                            tempSpdRgdtlEntity.setPlotid(String.valueOf(coreSupplyDetailEntity.getLotid()));
                        }
                        tempSpdRgdtlEntity.setPlotno(coreSupplyDetailEntity.getLotno());
                        tempSpdRgdtlEntity.setPproddate(coreSupplyDetailEntity.getProddate());
                        tempSpdRgdtlEntity.setPinvaliddate(coreSupplyDetailEntity.getInvadate());
                        tempSpdRgdtlEntity.setPvaliddate(DateUtils.addDateDays(coreSupplyDetailEntity.getInvadate(), -1));


                        tempSpdRgdtlEntity.setPurplandocid(coreSupplyDetailEntity.getPurplanid());
                        tempSpdRgdtlEntity.setPurplandtlid(coreSupplyDetailEntity.getPurplandtlid());
                        tempSpdRgdtlEntity.setCredate(new Timestamp(System.currentTimeMillis()));
                        tempSpdRgdtlEntity.setDmlFlag(ONE);
                        tempSpdRgdtlEntity.setRgid(tempSpdRgEntity.getRgid());
                        tempSpdRgdtlEntity.setLabelno(coreLabelMasterEntity.getLabelno());
                        //标签类型：1 整件 2 散件
                        tempSpdRgdtlEntity.setLabeltype(new BigDecimal(TWO));
                        tempSpdRgdtlEntity.setRgid(rgid);
                        tempSpdRgdtlEntity.setRgdtlid(UUID.randomUUID().toString().replaceAll("-", ""));
                        tempSpdRgdtlEntity.setBatchid(coreSupplyDetailEntity.getBatchCode());

                        tempSpdRgdtlDao.insert(tempSpdRgdtlEntity);
                    }
                }
            }
        }
        // 更新供货状态为已提交
        coreSupplyMasterEntity.setSupplyStatus(SupplyStatusEnum.COMMITTED.getKey());
        this.baseMapper.updateById(coreSupplyMasterEntity);
    }

    @Override
    public boolean existSupplyNo(String supplyNo) {
        return this.selectCount(new EntityWrapper<CoreSupplyMasterEntity>().eq("supplyno", supplyNo)) > 0;
    }

    @Override
    @DataFilter(subDept = true, user = false, tableAlias = "csm")
    public List<Map<String, Object>> getList(HashMap<String, Object> map) {
        // 手动数据过滤
        if (map.get(Constant.SQL_FILTER) != null) {
            String sqlFilter = map.get(Constant.SQL_FILTER).toString();
            map.put("deptIds", sqlFilter);
        }


        List<Map<String, Object>> list = this.baseMapper.getList(map);

        return list;
    }

    // HDI转换用 生成转换的供货主单
    @Override
    public void saveMaster(CoreSupplyMasterEntity coreSupplyMasterEntity) {
        this.baseMapper.insert(coreSupplyMasterEntity);
    }

    // 根据原数据标识 查询是否存在此主单
    @Override
    public CoreSupplyMasterEntity selectByOrgdataid(String orgdataid) {
        CoreSupplyMasterEntity selectByOrgdataid = this.baseMapper.selectByOrgdataid(orgdataid);

        return selectByOrgdataid;
    }

    @Override
    public void getBatchCodePdf(HttpServletResponse response, List<CoreSupplyDetailEntity> entityList, Long userId) throws Exception {

        // 获取批次码模板
        getBatchCodeTemplate();
        // 动态拼接批次码HTML
        StringBuilder html = new StringBuilder(BATCH_CODE_HEAD_HTML);
        for (CoreSupplyDetailEntity coreSupplyDetailEntity : entityList) {
            CoreSupplyDetailEntity detailEntity = coreSupplyDetailServiceImpl
                    .selectByDetailids(coreSupplyDetailEntity.getSupplyDetailId());
            //供货单ERP上传补生成批次码、上传图片到fdfs：
            if (StringUtil.isEmpty(detailEntity.getBatchCode())) {
                detailEntity.setBatchCode(
                        sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.CORE_SUPPLY_DETAIL_BATCH_CODE.getKey()));

            }
            if (StringUtil.isEmpty(detailEntity.getImageUrl())) {
                CoreSupplyMasterEntity coreSupplyMasterEntity = this.selectById(detailEntity.getSupplyMasterId());
                generateQRcode(coreSupplyMasterEntity, detailEntity, userId);
            }

            if (coreSupplyDetailEntity.getPrintAmount() != null) {
                for (int i = 0; i < coreSupplyDetailEntity.getPrintAmount(); i++) {
                    // XXX 截取超长字符串（临时措施！）名称 一行17汉字, 厂家一行17汉字, 规格一行11汉字
                    String goodsName = detailEntity.getGoodsname();
                    if (goodsName.length() > 17) {
                        goodsName = StringUtils.substring(goodsName, 0, 16) + "…";
                    }
                    String factoryName = detailEntity.getFactoryName();
                    if (factoryName.length() > 17) {
                        factoryName = StringUtils.substring(factoryName, 0, 16) + "…";
                    }
                    String specs = detailEntity.getGoodstype();
                    if (specs.length() > 11) {
                        specs = StringUtils.substring(specs, 0, 10) + "…";
                    }
                    String supplyDetailTemplate = BATCH_CODE_BODY_HTML
                            .replaceAll("@goodsName", goodsName)
                            .replaceAll("@factoryName", factoryName)
                            .replaceAll("@batchCode", detailEntity.getBatchCode())
                            .replaceAll("@qrCodeUrl", fastDFSClientUtils.getResAccessUrl(detailEntity.getImageUrl()))
                            .replaceAll("@lotNo", detailEntity.getLotno())
                            .replaceAll("@invaDate", DateUtils.format(detailEntity.getInvadate()))
                            .replaceAll("@specs", specs)
                            .replaceAll("@goodsUnit", detailEntity.getGoodsunit());
                    html.append(supplyDetailTemplate);
                }
            }
        }
        html.append(BATCH_CODE_END_HTML);
        //设置页面的大小
        String codeWidth = null;
        String codeHeight = null;
        try {
            CoreLabelSizeEntity coreLabelSizeEntity = coreLabelSizeService.getUserDetail(userId, LabelTypeEnum.LABEL.getKey());
            if (StringUtil.isEmpty(coreLabelSizeEntity)) {
                codeWidth = sysConfigService.getValue(SysConfigEnum.BATCH_CODE_WIDTH.getKey());
                codeHeight = sysConfigService.getValue(SysConfigEnum.BATCH_CODE_HEIGHT.getKey());
            } else {
                codeWidth = coreLabelSizeEntity.getPdfWidth().toString();
                codeHeight = coreLabelSizeEntity.getPdfHeight().toString();
            }
        } catch (Exception e) {
            throw new HdiException("获取批次码高/宽度错误，请检查参数配置！");
        }
        Rectangle pageSize = new Rectangle(Float.parseFloat(codeWidth), Float.parseFloat(codeHeight));
        Document document = new Document(pageSize, 2, 2, 2, 2);
        String pdfFilePath = Html2PdfUtil.getPdfUrl(html.toString(), document);

        StringUtil.responsePdf(response, pdfFilePath, "批次码");

        File pdfFile = new File(pdfFilePath);
        // 删除临时文件
        if (pdfFile.exists() && pdfFile.isFile()) {
            pdfFile.delete();
        }
    }

    private void getBatchCodeTemplate() {
        String html = sysConfigService.getValue(SysConfigEnum.BATCH_HTML.getKey());
        BATCH_CODE_BODY_HTML = StringUtils.substringBetween(html, "<body>", "</body>");
        BATCH_CODE_HEAD_HTML = StringUtils.substringBeforeLast(html, BATCH_CODE_BODY_HTML);
        BATCH_CODE_END_HTML = StringUtils.substringAfterLast(html, BATCH_CODE_BODY_HTML);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map importData(String[][] rows, Long userId, Long deptId) throws IOException {
        //保存返回信息
        StringBuilder sb = new StringBuilder();
        Map<String, String> map = new HashMap<>(16);
        if (rows.length <= 1) {
            map.put("successCount", "0");
            map.put("successCount", "0");
            sb.append("excel文件为空");
            map.put("errorMessage", sb.toString());
            return map;
        }
        if (userId == null || deptId == null) {
            map.put("successCount", "0");
            map.put("failCount", String.valueOf(rows.length - 1));
            sb.append("系统错误");
            map.put("errorMessage", sb.toString());
        }
        Integer successCount = 0;
        //masterMap用于保存主单信息
        Map<Integer, CoreSupplyMasterEntity> masterMap = new HashMap<>(16);
        Map<Integer, List<CoreSupplyDetailEntity>> detailmap = new HashMap<>(16);
        List<CoreSupplyDetailEntity> detailEntityList = null;
        for (int i = 1; i < rows.length; i++) {
            String[] row = rows[i];
            CoreSupplyMasterEntity mapMasterEntity = masterMap.get((row[0] + row[1] + row[2] + row[3] + row[4] + row[5] + row[6]).hashCode());
            if (mapMasterEntity == null) {
                detailEntityList = new ArrayList<>();
                CoreSupplyMasterEntity master = new CoreSupplyMasterEntity();
                if (StringUtil.isEmpty(StringUtil.trim(row[3]))) {
                    master.setSupplyno(sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.CORE_SUPPLY_MASTER_NO.getKey()));
                } else {
                    master.setSupplyno(StringUtil.trim(row[3]));
                }
                //查询平台医院信息
                if (StringUtil.isEmpty(StringUtil.trim(row[0]))) {
                    log.error("---------------------------生成供货主单失败，医院名称为空-----------------------");
                    sb.append("第" + (i + 1) + "行必填项医院名称为空");
                    sb.append("\\n");
                    continue;
                }
                OrgHospitalInfoEntity entity = orgHospitalInfoService.selectOne(new EntityWrapper<OrgHospitalInfoEntity>().eq("hospital_name", StringUtil.trim(row[0])));
                if (null == entity) {
                    log.error(MessageFormat.format("生成供货主单失败，医院{0}不存在", row[0]));
                    sb.append("第" + (i + 1) + "行医院名称数据错误");
                    sb.append("\n");
                    continue;
                }
                master.setHorgId(entity.getId());
                master.setHospitalCode(entity.getHospitalCode());
                master.setHospitalName(entity.getHospitalName());
                //查询平台供应商信息
                List<OrgSupplierInfoEntity> list = orgSupplierInfoService.selectByDeptId(deptId);
                if (StringUtil.isEmpty(list)) {
                    log.error("生成供货单失败，供应商不存在");
                    sb.append("\n");
                    continue;
                }
                master.setSupplierId(list.get(0).getId());
                master.setSupplierCode(list.get(0).getSupplierCode());
                master.setSupplierName(list.get(0).getSupplierName());
                master.setSupplyAddr(list.get(0).getSupplierAddress());
                //查询原医院，原供应商信息
                UnicodeSupplyShipEntity shipEntity = unicodeSupplyShipService.selectBySupplierIdAndHospitalId(master.getSupplierId(), entity.getId());
                if (shipEntity == null) {
                    log.error(MessageFormat.format(" 医院{0},供应商{1}匹对信息不存在 ", row[0], master.getSupplierName()));
                    sb.append("\n");
                    continue;
                }
                master.setSourcesHospitalId(shipEntity.getSourcesHospitalId());
                master.setSourcesHospitalCode(shipEntity.getSourcesHospitalCreditCode());
                master.setSourcesHospitalName(shipEntity.getSourcesHospitalName());
                master.setSourcesSupplierId(shipEntity.getSourcesSupplierId());
                master.setSourcesSupplierCode(shipEntity.getSourcesSupplierCreditCode());
                master.setSourcesSupplierName(shipEntity.getSourcesSupplierName());
                if (StringUtil.isEmpty(StringUtil.trim(row[1]))) {
                    log.error("---------------------------必填项库房名称为空-----------------------");
                    sb.append("第" + (i + 1) + "行必填项库房名称为空");
                    sb.append("\n");
                    continue;
                }
                CoreStorehouseEntity coreStorehouseEntity = coreStorehouseService
                        .selectOne(new EntityWrapper<CoreStorehouseEntity>().eq("horg_id", entity.getId()).eq("storehousename", StringUtil.trim(row[1])));
                if (coreStorehouseEntity == null) {
                    log.error(MessageFormat.format("生成供货主单失败医院' {0} ',供应商 ' {1} ' 匹对信息不存在", row[0], master.getSupplierName()));
                    sb.append("第" + (i + 1) + "行库房名称数据错误");
                    sb.append("\n");
                    continue;
                }
                //查询采购计划编号对应的采购单
                if (!StringUtil.isEmpty(StringUtil.trim(row[2]))) {
                    master.setPurplanno(StringUtil.trim(row[2]));
                    CorePurchaseMasterEntity purchaseMasterEntity = corePurchaseMasterDao.selectByPurplanno(StringUtil.trim(row[2]));
                    if (purchaseMasterEntity != null) {
                        master.setOrgdataid(purchaseMasterEntity.getOrgdataid());
                        master.setPurchaseMasterId(purchaseMasterEntity.getPurchaseMasterId());
                    }
                }
                master.setSalno(StringUtil.trim(row[4]));
                master.setStorehouseid(coreStorehouseEntity.getStorehouseid());
                master.setStorehouseNo(coreStorehouseEntity.getStorehouseno());
                master.setStorehouseName(coreStorehouseEntity.getStorehousename());
                master.setSupplyAddr(coreStorehouseEntity.getShaddress());
                master.setSourcesStorehouseId(coreStorehouseEntity.getOrgdataid());
                master.setDeptId(deptId);
                master.setCremanid(userId);
                master.setCredate(new Timestamp(System.currentTimeMillis()));
                master.setSupplyStatus(SupplyStatusEnum.UNCOMMITTED.getKey());
                master.setDatasource(DataSourceEnum.MANUAL.getKey());
                master.setDelFlag(DelFlagEnum.NORMAL.getKey());
                if (StringUtil.isEmpty(StringUtil.trim(row[5]))) {
                    sb.append("第" + (i + 1) + "行必填项供货类型为空");
                    sb.append("\n");
                    continue;
                }
                Integer type = SupplyTypeEnum.getKey(StringUtil.trim(row[5]));
                if (type == null) {
                    log.error(MessageFormat.format("生成供货单失败! 供货类型错误", row[5]));
                    sb.append("第" + (i + 1) + "行供货类型数据错误");
                    sb.append("\n");
                    continue;
                }
                master.setSupplyType(type);
                if (StringUtil.isEmpty(StringUtil.trim(row[6]))) {
                    sb.append("第" + (i + 1) + "行必填项供货时间为空");
                    sb.append("\n");
                    continue;
                }
                master.setSupplyTime(new Timestamp(DateUtils.stringToDate(StringUtil.trim(row[6]), "yyyy-MM-dd HH:mm:ss").getTime()));
                masterMap.put((row[0] + row[1] + row[2] + row[3] + row[4] + row[5] + row[6]).hashCode(), master);
                detailmap.put((row[0] + row[1] + row[2] + row[3] + row[4] + row[5] + row[6]).hashCode(), detailEntityList);

            }
            //插入供货细单
            mapMasterEntity = masterMap.get((row[0] + row[1] + row[2] + row[3] + row[4] + row[5] + row[6]).hashCode());
            CoreSupplyDetailEntity detailEntity = new CoreSupplyDetailEntity();
            detailEntity.setSupplyMasterId(mapMasterEntity.getSupplyMasterId());
            detailEntity.setPurchaseMasterId(mapMasterEntity.getPurchaseMasterId());
            List<Map<String, Object>> supplierGoodsList = publicComboDataService.querySupplierGoodsInfo(deptId, StringUtil.trim(row[7]));
            if (StringUtil.isEmpty(StringUtil.trim(row[7]))) {
                sb.append("第" + (i + 1) + "行供应商商品规格编码为空");
                sb.append("\n");
                continue;
            }
            if (StringUtil.isEmpty(supplierGoodsList)) {
                log.error(MessageFormat.format("生成供货细单失败！供应商商品不存在，供应商商品规格编码 {0}", row[7]));
                sb.append("第" + (i + 1) + "行供应商商品规格编码数据错误");
                sb.append("\n");
                continue;
            }
            List<CorePurchaseDetailEntity> list = corePurchaseDetailService.selectList(new EntityWrapper<CorePurchaseDetailEntity>().eq("hgoodstypeno", row[7])
                    .eq("purchase_master_id", mapMasterEntity.getPurchaseMasterId()).eq("orgdataid", mapMasterEntity.getOrgdataid()));
            if (CollectionUtils.isNotEmpty(list)) {
                detailEntity.setPurplandtlid(list.get(0).getOrgdatadtlid());
                detailEntity.setPurchaseDetailId(list.get(0).getPurchaseDetailId());
            }
            detailEntity.setPurplanid(mapMasterEntity.getOrgdataid());
            detailEntity.setGoodsclass(Integer.parseInt(supplierGoodsList.get(0).get("goodsclass").toString()));
            detailEntity.setGoodsid(Long.parseLong(supplierGoodsList.get(0).get("goodsid").toString()));
            detailEntity.setGoodsno(supplierGoodsList.get(0).get("goodsCode").toString());
            detailEntity.setGoodsname(supplierGoodsList.get(0).get("goodsName").toString());
            detailEntity.setGoodstypeid(Long.parseLong(supplierGoodsList.get(0).get("goodstypeid").toString()));
            detailEntity.setGoodstypeno(supplierGoodsList.get(0).get("goodstypeno").toString());
            detailEntity.setGoodstype(supplierGoodsList.get(0).get("goodstype").toString());
            detailEntity.setGoodsunit(supplierGoodsList.get(0).get("goodsUnit").toString());
            if (StringUtil.isEmpty(StringUtil.trim(row[12]))) {
                sb.append("第" + (i + 1) + "行必填项供货数量为空");
                sb.append("\n");
                continue;
            }
            detailEntity.setSupplyQty(Double.parseDouble(StringUtil.trim(row[12])));
            if (StringUtil.isEmpty(StringUtil.trim(row[13]))) {
                sb.append("第" + (i + 1) + "行必填项供货单价为空");
                sb.append("\n");
                continue;
            }
            detailEntity.setSupplyUnitprice(Double.parseDouble(StringUtil.trim(row[13])));

            if (StringUtil.isEmpty(StringUtil.trim(row[14]))) {
                sb.append("第" + (i + 1) + "行必填项生产批号为空");
                sb.append("\n");
                continue;
            }
            detailEntity.setLotno(StringUtil.trim(row[14]));

            if (StringUtil.isEmpty(StringUtil.trim(row[15]))) {
                sb.append("第" + (i + 1) + "行必填项生产日期为空");
                sb.append("\n");
                continue;
            }
            detailEntity.setProddate(new Timestamp(DateUtils.stringToDate(StringUtil.trim(row[15]), "yyyy-MM-dd HH:mm:ss").getTime()));
            if (StringUtil.isEmpty(StringUtil.trim(row[16]))) {
                sb.append("第" + (i + 1) + "行必填项失效日期为空");
                sb.append("\n");
                continue;
            }
            detailEntity.setInvadate(new Timestamp(DateUtils.stringToDate(StringUtil.trim(row[16]), "yyyy-MM-dd HH:mm:ss").getTime()));
            detailEntity.setCremanid(userId);
            detailEntity.setCredate(new Timestamp(System.currentTimeMillis()));
            detailEntity.setBatchCode(
                    sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.CORE_SUPPLY_DETAIL_BATCH_CODE.getKey()));
            detailEntityList.add(detailEntity);
            successCount++;
        }
        //获取供货主单
        List<CoreSupplyMasterEntity> supplyMasterEntityList = masterMap.values().stream().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(supplyMasterEntityList) && (rows.length - successCount) == 1) {
            this.insertBatch(supplyMasterEntityList);
            List<CoreSupplyDetailEntity> insertList = new ArrayList<>();
            detailmap.forEach((k, v) -> {
                for (CoreSupplyDetailEntity entity : v) {
                    entity.setSupplyMasterId(masterMap.get(k).getSupplyMasterId());
                    insertList.add(entity);
                }
            });
            coreSupplyDetailService.insertBatch(insertList);
            //生成二维码
            detailmap.forEach((k, v) -> {
                for (CoreSupplyDetailEntity entity : v) {
                    generateQRcode(masterMap.get(k), entity, userId);
                }
            });

            map.put("successCount", successCount.toString());
            map.put("failCount", String.valueOf(0));
        } else {
            map.put("successCount", "0");
            map.put("failCount", String.valueOf(rows.length - 1));
        }
        map.put("errorMessage", sb.toString());
        return map;

    }

    @Override
    public List<CoreSupplyMasterEntity> selectListByMap(Map<String, Object> params) {
        return this.baseMapper.selectPage(new Query<CoreSupplyMasterEntity>(params).getPage(),
                new EntityWrapper<CoreSupplyMasterEntity>().eq("supply_status", params.get("supply_status")));
    }

    /**
     * 生成二维码
     *
     * @param mapMasterEntity
     * @param detailEntity
     * @throws IOException
     */
    private void generateQRcode(CoreSupplyMasterEntity mapMasterEntity, CoreSupplyDetailEntity detailEntity, Long userId) {
        // 生成二维码标签图片
        String tempPath = String.format(FILE_PATH_TEMPLATE, UUID.randomUUID() + ".jpg");
        // 二维码存放内容（商品规格编码 + 批号 + 批次码）
        Integer codeWidth = null;
        Integer codeHeight = null;
        try {
            codeWidth = Integer.valueOf(sysConfigService.getValue(SysConfigEnum.QRCODE_BATCH_WIDTH.getKey()));
            codeHeight = Integer.valueOf(sysConfigService.getValue(SysConfigEnum.QRCODE_BATCH_HEIGHT.getKey()));

        } catch (Exception e) {
            throw new HdiException("获取批次二维码高/宽度错误，请检查参数配置！");
        }
        String specsCode = coreSupplyDetailService.getHospitalGoodsSpecsCode(detailEntity.getGoodsclass(),
                detailEntity.getGoodsid(), detailEntity.getGoodstypeid(), mapMasterEntity.getHorgId(), mapMasterEntity.getSupplierId());
        QRCodeUtil.zxingCodeCreate(specsCode + detailEntity.getLotno()
                + detailEntity.getBatchCode(), codeWidth, codeHeight, tempPath, "jpg");
        // 上传标签图片到FastDFS
        File pdfFile = new File(tempPath);
        // 上传文件到FastDFS
        String qrCodeUrl = null;
        try {
            qrCodeUrl = fastDFSClientUtils.uploadFile(pdfFile);
        } catch (IOException e) {
            log.error("上传文件到服务器失败");
            e.printStackTrace();
        }
        // 删除临时文件
        pdfFile.delete();
        // 更新批商品次码图片地址
        detailEntity.setSupplyDetailId(detailEntity.getSupplyDetailId());
        detailEntity.setBatchCode(detailEntity.getBatchCode());
        detailEntity.setImageUrl(qrCodeUrl);
        coreSupplyDetailService.updateById(detailEntity);
    }

}
