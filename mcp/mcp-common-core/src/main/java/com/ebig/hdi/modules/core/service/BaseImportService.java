package com.ebig.hdi.modules.core.service;

import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @program: mcp
 * @description:
 * @email 791203472@qq.com
 * @author: ZhengHaiWen
 * @create: 2019-12-16 17:08
 **/
public interface BaseImportService {

    /**
     * @Description: 导入基类的导入方法
     * @Author: ZhengHaiWen
     * @Date: 2019/12/16
     */
    Hdi importFile(@RequestParam("file") MultipartFile file, SysUserEntity sysUserEntity);
}
