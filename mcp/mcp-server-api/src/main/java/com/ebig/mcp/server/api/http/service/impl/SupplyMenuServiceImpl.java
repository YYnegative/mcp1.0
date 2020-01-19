package com.ebig.mcp.server.api.http.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebig.hdi.common.entity.MasterDetailsCommonEntity;
import com.ebig.hdi.common.enums.SequenceEnum;
import com.ebig.hdi.common.enums.SysConfigEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.FastDFSClientUtils;
import com.ebig.hdi.common.utils.HttpUtils;
import com.ebig.hdi.common.utils.QRCodeUtil;
import com.ebig.mcp.server.api.http.dao.SupplyMenuDao;
import com.ebig.mcp.server.api.http.dao.SysConfigDao;
import com.ebig.mcp.server.api.http.entity.CoreSupplyDetailEntity;
import com.ebig.mcp.server.api.http.entity.CoreSupplyMasterEntity;
import com.ebig.mcp.server.api.http.service.SupplyMenuService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SupplyMenuServiceImpl implements SupplyMenuService {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    SupplyMenuDao supplyMenuDao;
    @Autowired
    SysConfigDao sysConfigDao;

//    @Autowired
//    FastDFSClientUtils storageClient;

    @Value("${seqCodeUrl}")
    private  String seqCodeUrl;


    // 临时文件夹获取java system变量中的临时路径，在web项目中是容器的temp文件夹,如果直接运行是系统临时文件夹.
    private static final String FILE_PATH_TEMPLATE = System.getProperty("java.io.tmpdir") + "/%s";

    private Integer SUPPLY_STATUS_UNCOMMIT=0;


    @SuppressWarnings("finally")
	@Override
    public Map<String,Object> uploadsupplymenu(List<MasterDetailsCommonEntity<CoreSupplyMasterEntity, CoreSupplyDetailEntity>> list) {
        Integer successCount=0;
        Integer errorCount=0;
        StringBuffer errorMsg = new StringBuffer();
        CoreSupplyMasterEntity supplyMaster = new CoreSupplyMasterEntity();

        for (int i = 0; i < list.size(); i++) {
            try{
                MasterDetailsCommonEntity<CoreSupplyMasterEntity, CoreSupplyDetailEntity> masterDetailsCommonEntity = list.get(i);
                 supplyMaster = masterDetailsCommonEntity.getMaster();
                List<CoreSupplyMasterEntity> masterEntitiesByNo = supplyMenuDao.getSupplyMasterByNo(SUPPLY_STATUS_UNCOMMIT,supplyMaster.getSupplyno(), supplyMaster.getSourcesSupplierCode());

                if (masterEntitiesByNo.size()>0){
                    //已存在供货单，进行供货单修改
                    if (supplyMaster.getDelFlag()==1){
                        supplyMaster.setDelFlag(0);
                    }
                    //修改供货主细单信息；
                    updateSupplyMaster(supplyMaster,masterDetailsCommonEntity,masterEntitiesByNo.get(0));

                }else {
                    //判断此供货单是否进入已提交后的状态，已提交则跳过此条供货单上传
                    List<CoreSupplyMasterEntity> supplyMasterCommit = supplyMenuDao.getSupplyMasterCommit(supplyMaster.getSupplyno(), supplyMaster.getSupplierCode());
                    if (supplyMasterCommit.size()>0){
                        errorCount++;
                        if (errorCount==1){
                            errorMsg.append("上传失败的供货单编号：");
                        }
                        errorMsg.append(supplyMaster.getSupplyno());
                        errorMsg.append("(已提交医院)、");
                        continue;
                    }

                    //未提交已提交状态都不存在则新增供货主单：
                    if (supplyMaster.getDelFlag()==1){
                        supplyMaster.setDelFlag(0);
                    }
                    //新增主细单信息；
                    addSupplyMaster(supplyMaster,masterDetailsCommonEntity);
                }

                successCount++;
            }catch (Exception e){
                log.info(e.getMessage(),e);
                errorCount++;
                if (errorCount==1){
                    errorMsg.append("上传失败的供货单编号：");
                }
                errorMsg.append(supplyMaster.getSupplyno());
                errorMsg.append(String.format("(%s)、",e.getMessage()));

            }finally {
                continue;
            }
        }

        //返回结果信息集；
        Map<String, Object> map = new HashMap<>();
        map.put("successCount",successCount);
        map.put("errorCount",errorCount);
        map.put("errorMsg",errorMsg);

        return map;
    }

    /**
     * 处理批次编码，批次码图片及路径
     * @param coreSupplyDetailEntity
     * @return
     * @throws IOException
     */
    @SuppressWarnings("unused")
	private CoreSupplyDetailEntity handleBatchCode(CoreSupplyDetailEntity coreSupplyDetailEntity) throws IOException {

        FastDFSClientUtils fastDFSClientUtils = new FastDFSClientUtils();
        //获取系统生成的批次编码

//        String batchNo = sysSequenceService.selectSeqValueBySeqCode(SequenceEnum.CORE_SUPPLY_DETAIL_BATCH_CODE.getKey());
        String batchNo = HttpUtils.doGet(seqCodeUrl+SequenceEnum.SUPPLIER_CONSUMABLES_CODE.getKey());
        // 生成二维码标签图片
        String tempPath = String.format(FILE_PATH_TEMPLATE, UUID.randomUUID() + ".jpg");
        // 二维码存放内容（商品规格编码 + 批号 + 批次码）
        Integer codeWidth = null;
        Integer codeHeight = null;
        try {
            codeWidth = Integer.valueOf(sysConfigDao.getBatchSize(SysConfigEnum.QRCODE_BATCH_WIDTH.getKey()));
            codeHeight = Integer.valueOf(sysConfigDao.getBatchSize(SysConfigEnum.QRCODE_BATCH_HEIGHT.getKey()));
        } catch (Exception e) {
            throw new HdiException("获取批次二维码高/宽度错误，请检查参数配置！");
        }
        String specsCode = coreSupplyDetailEntity.getGoodstypeno();
        QRCodeUtil.zxingCodeCreate(specsCode + coreSupplyDetailEntity.getLotno()
                + batchNo, codeWidth, codeHeight, tempPath, "jpg");
        // 上传标签图片到FastDFS
        File pdfFile = new File(tempPath);
        // 上传文件到FastDFS
//        InputStream inputStream = new FileInputStream(pdfFile);
//        StorePath storePath = storageClient.uploadFile(inputStream,pdfFile.length(), FilenameUtils.getExtension(pdfFile.getName()),null);
//        String qrCodeUrl = storePath.getFullPath();
//        String qrCodeUrl = storageClient.uploadFile(pdfFile);
        // 删除临时文件
        pdfFile.delete();
        // 更新商品批次码图片地址

        coreSupplyDetailEntity.setBatchCode(batchNo);
//        coreSupplyDetailEntity.setImageUrl(qrCodeUrl);

        return coreSupplyDetailEntity;
    }

    /**
     * 修改供货主单信息
     * @param supplyMaster
     * @param masterDetailsCommonEntity
     */
    @Transactional
    public void updateSupplyMaster(CoreSupplyMasterEntity supplyMaster, MasterDetailsCommonEntity<CoreSupplyMasterEntity, CoreSupplyDetailEntity> masterDetailsCommonEntity,CoreSupplyMasterEntity masterEntitiesByNo) throws Exception {

        supplyMenuDao.updateSupplyMaster(supplyMaster);
        List<CoreSupplyDetailEntity> commonEntityDetailList = masterDetailsCommonEntity.getDetails();
        for (int j = 0; j < commonEntityDetailList.size(); j++) {
            List<CoreSupplyDetailEntity> supplyDetailByNo = supplyMenuDao.getSupplyDetailByNo(supplyMaster.getId(), commonEntityDetailList.get(0).getSourcedtlid());
            if (supplyDetailByNo.size()>0){
                //存在细单则对细单进行修改
                supplyMenuDao.updateSupplyDetail(commonEntityDetailList.get(j));

            }else {
                //不存在细单则新增细单
                //处理批次编码，批次码图片及路径
                CoreSupplyDetailEntity supplyDetail= commonEntityDetailList.get(j);
//                supplyDetail=handleBatchCode(supplyDetail);
                supplyMenuDao.addSupplyDetail(supplyMaster.getSupplierId(), supplyMaster.getHorgId(), supplyDetail,masterEntitiesByNo);
            }
        }

    }

    /**
     * 新增供货主单信息
     * @param supplyMaster
     * @param masterDetailsCommonEntity
     */
    @Transactional
    public void addSupplyMaster(CoreSupplyMasterEntity supplyMaster, MasterDetailsCommonEntity<CoreSupplyMasterEntity, CoreSupplyDetailEntity> masterDetailsCommonEntity) throws Exception {
        supplyMenuDao.addSupplyMaster(supplyMaster);
        List<CoreSupplyMasterEntity> masterEntitiesByNoNew = supplyMenuDao.getSupplyMasterByNo(SUPPLY_STATUS_UNCOMMIT,supplyMaster.getSupplyno(), supplyMaster.getSourcesSupplierCode());

        //新增供货细单：
        List<CoreSupplyDetailEntity> commonEntityDetailList = masterDetailsCommonEntity.getDetails();
        for (int j = 0; j < commonEntityDetailList.size() ; j++) {
            CoreSupplyDetailEntity supplyDetail= commonEntityDetailList.get(j);
            supplyDetail.setSupplyMasterId(masterEntitiesByNoNew.get(0).getSupplyMasterId());
            //处理批次编码，批次码图片及路径
//            supplyDetail=handleBatchCode(supplyDetail);
            supplyMenuDao.addSupplyDetail(supplyMaster.getSupplierId(), supplyMaster.getHorgId(), supplyDetail,masterEntitiesByNoNew.get(0));
        }

    }

}
