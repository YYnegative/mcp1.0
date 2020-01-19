package com.ebig.hdi.common.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.ebig.hdi.common.config.AppConfig;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.exception.FdfsUnsupportStorePathException;
import com.github.tobato.fastdfs.service.FastFileStorageClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FastDFSClientUtils {

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private AppConfig appConfig;   // 项目参数配置
    

    /**
     * 上传文件
     * @param file 文件对象
     * @return 文件访问地址
     * @throws IOException
     */
    public String uploadFile(MultipartFile file) throws IOException {
        StorePath storePath = storageClient.uploadFile(file.getInputStream(),file.getSize(), FilenameUtils.getExtension(file.getOriginalFilename()),null);
        //return getResAccessUrl(storePath);
        return storePath.getFullPath();
    }
    
    public String uploadFile(File file) throws IOException {
    	InputStream inputStream = new FileInputStream(file);
        StorePath storePath = storageClient.uploadFile(inputStream,file.length(), FilenameUtils.getExtension(file.getName()),null);
        return storePath.getFullPath();
    }

    /**
     * 将一段字符串生成一个文件上传
     * @param content 文件内容
     * @param fileExtension
     * @return
     */
    public String uploadFile(String content, String fileExtension) {
        byte[] buff = content.getBytes(Charset.forName("UTF-8"));
        ByteArrayInputStream stream = new ByteArrayInputStream(buff);
        StorePath storePath = storageClient.uploadFile(stream,buff.length, fileExtension,null);
        return getResAccessUrl(storePath);
    }

    // 封装图片完整URL地址
    private String getResAccessUrl(StorePath storePath) {
        String fileUrl = "http://" + appConfig.getResHost()
                + ":" + appConfig.getStoragePort() + "/" + storePath.getFullPath();
        return fileUrl;
    }
    /**
     * 函数功能说明 ：根据相对路径获取完整访问地址 <br/>
     * 修改者名字： <br/>
     * 修改日期： <br/>
     * 修改内容：<br/>
     * 作者：roncoo-lrx <br/>
     * 参数：@param fullPath
     * 参数：@return <br/>
     * return：String <br/>
     */
    public String getResAccessUrl(String fullPath) {
        String fileUrl = "http://" + appConfig.getResHost()
                + ":" + appConfig.getStoragePort() + "/" + fullPath;
        return fileUrl;
    }

    /**
     * 删除文件
     * @param fileUrl 文件访问地址
     * @return
     */
    public void deleteFile(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)) {
            return;
        }
        try {
        	fileUrl = "http://" + appConfig.getResHost() + ":" + appConfig.getStoragePort() + "/" + fileUrl;
            StorePath storePath = StorePath.praseFromUrl(fileUrl);
            storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
        } catch (FdfsUnsupportStorePathException e) {
            log.warn(e.getMessage());
        }
    }
}
