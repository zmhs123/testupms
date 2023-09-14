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

import cn.hutool.core.util.StrUtil;
import cn.tech.wings.cloud.admin.api.dto.SysLogDTO;
import cn.tech.wings.cloud.admin.api.feign.RemoteLogService;
import cn.tech.wings.cloud.common.core.constant.CommonConstants;
import cn.tech.wings.cloud.common.core.constant.SecurityConstants;
import cn.tech.wings.cloud.common.log.util.SysLogUtils;
import cn.tech.wings.cloud.common.security.handler.AuthenticationLogoutHandler;
import cn.tech.wings.cloud.common.security.service.CloudUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 退出事件处理
 *
 * @author cloud
 * @date 2021/06/22
 */
@Slf4j
@Component
@AllArgsConstructor
public class CloudAuthenticationLogoutEventHandler implements AuthenticationLogoutHandler {

	private final RemoteLogService logService;

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

		CloudUser cloudUser = (CloudUser) authentication.getPrincipal();
		SysLogDTO sysLog = SysLogUtils.getSysLog(request, cloudUser.getUsername());
		sysLog.setTitle(cloudUser.getUsername() + "用户退出登录");
		sysLog.setParams(cloudUser.getUsername());

		String startTimeStr = request.getHeader(CommonConstants.REQUEST_START_TIME);
		if (StrUtil.isNotBlank(startTimeStr)) {
			Long startTime = Long.parseLong(startTimeStr);
			Long endTime = System.currentTimeMillis();
			sysLog.setTime(endTime - startTime);
		}

		// 获取clientId 信息
		OAuth2Authentication auth2Authentication = (OAuth2Authentication) authentication;
		sysLog.setServiceId(auth2Authentication.getOAuth2Request().getClientId());
		sysLog.setTenantId(cloudUser.getTenantId());
		// 保存退出的token
		String token = request.getHeader(HttpHeaders.AUTHORIZATION);
		sysLog.setParams(token);

		logService.saveLog(sysLog, SecurityConstants.FROM_IN);
		log.info("用户：{} 退出成功, token:{}  已注销", cloudUser.getTenantId(), token);
	}

}
