/*
 *    Copyright (c) 2018-2025, cloud All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: cloud
 */

package cn.tech.wings.cloud.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.tech.wings.cloud.admin.api.entity.SysUser;
import cn.tech.wings.cloud.admin.mapper.SysUserMapper;
import cn.tech.wings.cloud.admin.service.MobileService;
import cn.tech.wings.cloud.common.core.constant.CacheConstants;
import cn.tech.wings.cloud.common.core.constant.SecurityConstants;
import cn.tech.wings.cloud.common.core.constant.enums.LoginTypeEnum;
import cn.tech.wings.cloud.common.core.exception.ErrorCodes;
import cn.tech.wings.cloud.common.core.util.MsgUtils;
import cn.tech.wings.cloud.common.core.util.R;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author cloud
 * @date 2018/11/14
 * <p>
 * 手机登录相关业务实现
 */
@Slf4j
@Service
@AllArgsConstructor
public class MobileServiceImpl implements MobileService {

	private final RedisTemplate redisTemplate;

	private final SysUserMapper userMapper;

	/**
	 * 发送手机验证码 TODO: 调用短信网关发送验证码,测试返回前端
	 * @param mobile mobile
	 * @return code
	 */
	@Override
	public R<Boolean> sendSmsCode(String mobile) {
		List<SysUser> userList = userMapper
				.selectList(Wrappers.<SysUser>query().lambda().eq(SysUser::getPhone, mobile));

		if (CollUtil.isEmpty(userList)) {
			log.info("手机号未注册:{}", mobile);
			return R.ok(Boolean.FALSE, MsgUtils.getMessage(ErrorCodes.SYS_APP_PHONE_UNREGISTERED, mobile));
		}

		Object codeObj = redisTemplate.opsForValue()
				.get(CacheConstants.DEFAULT_CODE_KEY + LoginTypeEnum.SMS.getType() + StringPool.AT + mobile);

		if (codeObj != null) {
			log.info("手机号验证码未过期:{}，{}", mobile, codeObj);
			return R.ok(Boolean.FALSE, MsgUtils.getMessage(ErrorCodes.SYS_APP_SMS_OFTEN));
		}

		String code = RandomUtil.randomNumbers(Integer.parseInt(SecurityConstants.CODE_SIZE));
		log.debug("手机号生成验证码成功:{},{}", mobile, code);
		redisTemplate.opsForValue().set(
				CacheConstants.DEFAULT_CODE_KEY + LoginTypeEnum.SMS.getType() + StringPool.AT + mobile, code,
				SecurityConstants.CODE_TIME, TimeUnit.SECONDS);
		return R.ok(Boolean.TRUE, code);
	}

}
