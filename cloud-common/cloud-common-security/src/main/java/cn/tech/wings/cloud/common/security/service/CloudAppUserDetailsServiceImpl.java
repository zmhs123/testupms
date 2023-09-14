/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.tech.wings.cloud.common.security.service;

import cn.tech.wings.cloud.admin.api.dto.UserInfo;
import cn.tech.wings.cloud.admin.api.feign.RemoteUserService;
import cn.tech.wings.cloud.common.core.constant.SecurityConstants;
import cn.tech.wings.cloud.common.core.util.R;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 用户详细信息
 *
 * @author cloud hccake
 */
@Slf4j
@RequiredArgsConstructor
public class CloudAppUserDetailsServiceImpl implements CloudUserDetailsService {

	private final UserDetailsService cloudDefaultUserDetailsServiceImpl;

	private final RemoteUserService remoteUserService;

	/**
	 * 手机号登录
	 * @param phone 手机号
	 * @return
	 */
	@Override
	@SneakyThrows
	public UserDetails loadUserByUsername(String phone) {
		R<UserInfo> result = remoteUserService.social(phone, SecurityConstants.FROM_IN);
		return getUserDetails(result);
	}

	/**
	 * check-token 使用
	 * @param cloudUser user
	 * @return UserDetails
	 */
	@Override
	public UserDetails loadUserByUser(CloudUser cloudUser) {
		return cloudDefaultUserDetailsServiceImpl.loadUserByUsername(cloudUser.getUsername());
	}

	/**
	 * 支持所有的 mobile 类型
	 * @param clientId 目标客户端
	 * @param grantType 授权类型
	 * @return true/false
	 */
	@Override
	public boolean support(String clientId, String grantType) {
		return SecurityConstants.GRANT_MOBILE.equals(grantType);
	}

}
