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

package cn.tech.wings.cloud.gateway.config;

import cn.tech.wings.cloud.gateway.handler.ImageCodeCheckHandler;
import cn.tech.wings.cloud.gateway.handler.ImageCodeCreateHandler;
import cn.tech.wings.cloud.gateway.handler.LicenseHandler;
import cn.tech.wings.cloud.gateway.handler.CertificateHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

/**
 * @author cloud
 * @date 2018/7/5 路由配置信息
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class RouterFunctionConfiguration {

	private final ImageCodeCheckHandler imageCodeCheckHandler;

	private final ImageCodeCreateHandler imageCodeCreateHandler;

	private final LicenseHandler licenseHandler;

	private final CertificateHandler certificateHandler;

	@Bean
	public RouterFunction routerFunction() {
		return RouterFunctions
				.route(RequestPredicates.path("/code").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
						imageCodeCreateHandler)
				.andRoute(RequestPredicates.POST("/code/certificate").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
						certificateHandler)
				.andRoute(RequestPredicates.POST("/code/check").and(RequestPredicates.accept(MediaType.ALL)),
						imageCodeCheckHandler)
				.andRoute(RequestPredicates.POST("/gld").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
						licenseHandler);
	}

}
