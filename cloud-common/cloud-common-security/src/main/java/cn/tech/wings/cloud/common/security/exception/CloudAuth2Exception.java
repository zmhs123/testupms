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

package cn.tech.wings.cloud.common.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import cn.tech.wings.cloud.common.security.component.CloudAuth2ExceptionSerializer;
import lombok.Getter;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * @author cloud
 * @date 2018/7/8 自定义OAuth2Exception
 */
@JsonSerialize(using = CloudAuth2ExceptionSerializer.class)
public class CloudAuth2Exception extends OAuth2Exception {

	@Getter
	private String errorCode;

	public CloudAuth2Exception(String msg) {
		super(msg);
	}

	public CloudAuth2Exception(String msg, Throwable t) {
		super(msg, t);
	}

	public CloudAuth2Exception(String msg, String errorCode) {
		super(msg);
		this.errorCode = errorCode;
	}

}
