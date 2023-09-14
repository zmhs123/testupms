/*
 *
 *      Copyright (c) 2018-2025, cloud All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the pig4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: cloud
 *
 */

package cn.tech.wings.cloud.admin.api.feign;

import cn.tech.wings.cloud.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @author cloud
 * @date 2019/6/19
 * <p>
 * 租户接口
 */
@FeignClient(contextId = "remoteAccurateApiService", value = ServiceNameConstants.KACCURATE_API_SERVICE)
public interface RemoteAccurateApiService {

	/**
	 * 根据caseId删除追溯关系
	 * @param map
	 * @return
	 */
	@PostMapping("/api/accurate/relation/removeByCaseIds")
	String removeByCaseIds(@RequestBody Map<String,Object> map);

}
