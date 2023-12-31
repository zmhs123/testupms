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

import cn.tech.wings.cloud.common.security.handler.RestResponseErrorHandler;
import org.springframework.beans.BeansException;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author cloud
 * @date 2018/11/10
 */
@ComponentScan("cn.tech.wings.cloud.common.security")
public class CloudResourceServerAutoConfiguration implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Bean
	@Primary
	@LoadBalanced
	public RestTemplate lbRestTemplate() {
		// 获取上下文配置的ClientHttpRequestInterceptor 实现
		Map<String, ClientHttpRequestInterceptor> beansOfType = applicationContext
				.getBeansOfType(ClientHttpRequestInterceptor.class);
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setInterceptors(new ArrayList<>(beansOfType.values()));
		restTemplate.setErrorHandler(new RestResponseErrorHandler());
		return restTemplate;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
