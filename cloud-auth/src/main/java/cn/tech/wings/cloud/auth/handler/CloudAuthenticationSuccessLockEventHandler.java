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

package cn.tech.wings.cloud.auth.handler;

import cn.tech.wings.cloud.common.core.constant.CacheConstants;
import cn.tech.wings.cloud.common.core.util.KeyStrResolver;
import cn.tech.wings.cloud.common.security.handler.AuthenticationSuccessHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author cloud
 * @date 2022-04-11
 *
 * 登录操作次数锁定清楚功能
 */
@Slf4j
@Component
@AllArgsConstructor
public class CloudAuthenticationSuccessLockEventHandler implements AuthenticationSuccessHandler {

	private final RedisTemplate<String, String> redisTemplate;

	private final KeyStrResolver tenantKeyStrResolver;

	/**
	 * 处理登录成功方法
	 * <p>
	 * 获取到登录的authentication 对象
	 * @param authentication 登录对象
	 * @param request 请求
	 * @param response 返回
	 */
	@Async
	@Override
	public void handle(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
		String username = authentication.getName();
		String key = String.format("%s:%s:%s", CacheConstants.LOGIN_ERROR_TIMES, tenantKeyStrResolver.key(), username);
		redisTemplate.delete(key);
	}

}
