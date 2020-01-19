/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.ebig.hdi.modules.sys.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.modules.sys.dao.SysUserTokenDao;
import com.ebig.hdi.modules.sys.entity.SysUserTokenEntity;
import com.ebig.hdi.modules.sys.oauth2.TokenGenerator;
import com.ebig.hdi.modules.sys.service.SysUserTokenService;

@Service("sysUserTokenService")
public class SysUserTokenServiceImpl extends ServiceImpl<SysUserTokenDao, SysUserTokenEntity> implements SysUserTokenService {
	//12小时后过期
	private final static int EXPIRE = 3600 * 12;


	@Override
	public Hdi createToken(long userId) {
		//生成一个token
		String token = TokenGenerator.generateValue();

		//当前时间
		Date now = new Date();
		//过期时间
		Date expireTime = new Date(now.getTime() + EXPIRE * 1000);

		//判断是否生成过token
		SysUserTokenEntity tokenEntity = this.selectById(userId);
		if(tokenEntity == null){
			tokenEntity = new SysUserTokenEntity();
			tokenEntity.setUserId(userId);
			tokenEntity.setToken(token);
			tokenEntity.setUpdateTime(now);
			tokenEntity.setExpireTime(expireTime);

			//保存token
			this.insert(tokenEntity);
		}else{
			tokenEntity.setToken(token);
			tokenEntity.setUpdateTime(now);
			tokenEntity.setExpireTime(expireTime);

			//更新token
			this.updateById(tokenEntity);
		}

		Hdi r = Hdi.ok().put("token", token).put("expire", EXPIRE);

		return r;
	}

	@Override
	public void logout(long userId) {
		//生成一个token
		String token = TokenGenerator.generateValue();

		//修改token
		SysUserTokenEntity tokenEntity = new SysUserTokenEntity();
		tokenEntity.setUserId(userId);
		tokenEntity.setToken(token);
		this.updateById(tokenEntity);
	}
}
