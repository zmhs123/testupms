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

package cn.tech.wings.cloud.common.security.listener;

import cn.hutool.core.collection.CollUtil;
import cn.tech.wings.cloud.common.security.handler.AuthenticationLogoutHandler;
import cn.tech.wings.cloud.common.security.service.CloudUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author cloud
 * @date 2021/06/22 退出事件监听器
 */
public class AuthenticationLogoutSuccessEventListener implements ApplicationListener<LogoutSuccessEvent> {

	@Autowired(required = false)
	private AuthenticationLogoutHandler logoutHandler;

	/**
	 * Handle an application event.
	 * @param event the event to respond to
	 */
	@Override
	public void onApplicationEvent(LogoutSuccessEvent event) {
		// 健壮性判断
		if (!(event.getSource() instanceof OAuth2Authentication)) {
			return;
		}

		OAuth2Authentication authentication = (OAuth2Authentication) event.getSource();
		if (logoutHandler != null && isUserAuthentication(authentication)) {
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			HttpServletResponse response = requestAttributes.getResponse();

			logoutHandler.handle(authentication, request, response);
		}
	}

	private boolean isUserAuthentication(Authentication authentication) {
		return authentication.getPrincipal() instanceof CloudUser
				|| CollUtil.isNotEmpty(authentication.getAuthorities());
	}

}
