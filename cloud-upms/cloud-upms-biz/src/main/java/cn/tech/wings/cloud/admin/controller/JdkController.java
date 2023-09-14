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

package cn.tech.wings.cloud.admin.controller;

import cn.tech.wings.cloud.admin.config.JdkConfig;
import cn.tech.wings.cloud.common.core.util.R;
import cn.tech.wings.cloud.common.security.annotation.Inner;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author cloud
 * @date 2018/11/14
 * <p>
 * jdk配置
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/jdk")
@Api(value = "jdk", tags = "jdk配置管理模块")
public class JdkController {
	private final JdkConfig jdkConfig;

	@Inner(value = false)
	@GetMapping("/getJdk")
	public R getJdk(@RequestHeader Map<String, String> headers) {
		log.info("headers:{}",JSON.toJSONString(headers));
		return R.ok(jdkConfig.getJdkPath());
	}

}
