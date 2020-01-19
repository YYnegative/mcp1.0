/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.ebig.hdi.modules.sys.service.impl;

import java.awt.image.BufferedImage;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.DateUtils;
import com.ebig.hdi.modules.sys.dao.SysCaptchaDao;
import com.ebig.hdi.modules.sys.entity.SysCaptchaEntity;
import com.ebig.hdi.modules.sys.service.SysCaptchaService;
import com.google.code.kaptcha.Producer;

/**
 * 验证码
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service("sysCaptchaService")
public class SysCaptchaServiceImpl extends ServiceImpl<SysCaptchaDao, SysCaptchaEntity> implements SysCaptchaService {
    @Autowired
    private Producer producer;

    @Override
    public BufferedImage getCaptcha(String uuid) {
        if(StringUtils.isBlank(uuid)){
            throw new HdiException("uuid不能为空");
        }
        //生成文字验证码
        String code = producer.createText();

        SysCaptchaEntity captchaEntity = new SysCaptchaEntity();
        captchaEntity.setUuid(uuid);
        captchaEntity.setCode(code);
        //5分钟后过期
        captchaEntity.setExpireTime(DateUtils.addDateMinutes(new Date(), 5));
        this.insert(captchaEntity);

        return producer.createImage(code);
    }

    @Override
    public boolean validate(String uuid, String code) {
        SysCaptchaEntity captchaEntity = this.selectById(uuid);
        if(captchaEntity == null){
            return false;
        }

        //删除验证码
        this.deleteById(uuid);

        if(captchaEntity.getCode().equalsIgnoreCase(code) && captchaEntity.getExpireTime().getTime() >= System.currentTimeMillis()){
            return true;
        }

        return false;
    }
}
