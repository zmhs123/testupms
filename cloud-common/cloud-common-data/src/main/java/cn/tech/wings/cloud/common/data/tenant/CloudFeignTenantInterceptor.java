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

package cn.tech.wings.cloud.common.data.tenant;

import cn.tech.wings.cloud.common.core.constant.CommonConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

/**
 * @author cloud
 * @date 2018/9/14
 */
@Slf4j
public class CloudFeignTenantInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate requestTemplate) {
		if (TenantContextHolder.getTenantId() == null) {
			log.debug("TTL 中的 租户ID为空，feign拦截器 >> 跳过");
			return;
		}
		requestTemplate.header(CommonConstants.TENANT_ID, TenantContextHolder.getTenantId().toString());
	}

}
