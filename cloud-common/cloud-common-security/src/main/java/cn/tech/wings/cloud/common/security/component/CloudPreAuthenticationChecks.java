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

package cn.tech.wings.cloud.common.security.component;

import cn.tech.wings.cloud.common.security.util.CloudSecurityMessageSourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

/**
 * @author cloud
 * @date 2019-01-02
 */
@Slf4j
public class CloudPreAuthenticationChecks implements UserDetailsChecker {

	private MessageSourceAccessor messages = CloudSecurityMessageSourceUtil.getAccessor();

	@Override
	public void check(UserDetails user) {
		if (!user.isAccountNonLocked()) {
			log.debug("User account is locked");

			throw new LockedException(
					messages.getMessage("AbstractUserDetailsAuthenticationProvider.locked", "User account is locked"));
		}

		if (!user.isEnabled()) {
			log.debug("User account is disabled");

			throw new DisabledException(
					messages.getMessage("AbstractUserDetailsAuthenticationProvider.disabled", "User is disabled"));
		}

		if (!user.isAccountNonExpired()) {
			log.debug("User account is expired");

			throw new AccountExpiredException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.expired",
					"User account has expired"));
		}
	}

}
