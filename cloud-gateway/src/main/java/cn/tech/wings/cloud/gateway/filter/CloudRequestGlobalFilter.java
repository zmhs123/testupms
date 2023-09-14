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

package cn.tech.wings.cloud.gateway.filter;

import cn.tech.wings.cloud.gateway.license.LicenseCheckModel;
import cn.tech.wings.cloud.gateway.license.LicenseVerify;
import cn.tech.wings.cloud.common.core.constant.CommonConstants;
import cn.tech.wings.cloud.common.core.constant.SecurityConstants;
import cn.tech.wings.cloud.common.core.exception.CheckedException;
import de.schlichtherle.license.LicenseContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;

/**
 * @author cloud
 * @date 2018/10/8
 * <p>
 * 全局拦截器，作用所有的微服务
 * <p>
 * 1. 对请求头中参数进行处理 from 参数进行清洗 2. 重写StripPrefix = 1,支持全局
 * <p>
 * 支持swagger添加X-Forwarded-Prefix header （F SR2 已经支持，不需要自己维护）
 */
@Component
@Slf4j
public class CloudRequestGlobalFilter implements GlobalFilter, Ordered {
	private static boolean checkFlag = false;
	private static List<String> llist = new ArrayList<>();

	static {
		llist.add("/auth/oauth/token");
		llist.add("/accurate/api/accurate/service/config/getAppList");
		llist.add("/accurate/api/accurate/service/config/add");

	}

	/**
	 * Process the Web request and (optionally) delegate to the next {@code WebFilter}
	 * through the given {@link GatewayFilterChain}.
	 *
	 * @param exchange the current server exchange
	 * @param chain    provides a way to delegate to the next filter
	 * @return {@code Mono<Void>} to indicate when request processing is complete
	 */
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		// 1. 清洗请求头中from 参数
		ServerHttpRequest request = exchange.getRequest().mutate().headers(httpHeaders -> {
			httpHeaders.remove(SecurityConstants.FROM);
			// 设置请求时间
			httpHeaders.put(CommonConstants.REQUEST_START_TIME,
					Collections.singletonList(String.valueOf(System.currentTimeMillis())));
//			httpHeaders.add("appCount",appCount);
		}).build();

		// 2. 重写StripPrefix
		addOriginalRequestUrl(exchange, request.getURI());
		String rawPath = request.getURI().getRawPath();
		String newPath = "/" + Arrays.stream(StringUtils.tokenizeToStringArray(rawPath, "/")).skip(1L)
				.collect(Collectors.joining("/"));
		ServerHttpRequest newRequest = request.mutate().path(newPath).build();
		exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, newRequest.getURI());
		if (llist.contains(rawPath)) {
			try {
				log.info("uri:"+rawPath);
				LicenseContent lc = LicenseVerify.verify();
				LicenseCheckModel m = (LicenseCheckModel)lc.getExtra();
				if(null == m.getAppCount()){
					checkFlag = true;
					throw new CheckedException("license error: appCount undefined!!!");
				}
				newRequest.mutate().headers(httpHeaders -> {
					// 设置应用数量
					httpHeaders.add("appCount",m.getAppCount());
				}).build();
				checkFlag = false;
			} catch (Exception e) {
				log.error("证书校验失败！", e);
				checkFlag = true;
				throw new CheckedException("license error");
			}
		}
		if (checkFlag == true) {
			throw new CheckedException("license error");
		}
		return chain.filter(exchange.mutate().request(newRequest.mutate().build()).build());
	}

	@Override
	public int getOrder() {
		return -1000;
	}

}
