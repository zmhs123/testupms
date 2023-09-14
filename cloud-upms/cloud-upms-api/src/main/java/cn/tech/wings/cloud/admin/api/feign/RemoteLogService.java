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

import cn.tech.wings.cloud.common.core.constant.SecurityConstants;
import cn.tech.wings.cloud.common.core.constant.ServiceNameConstants;
import cn.tech.wings.cloud.common.core.util.R;
import cn.tech.wings.cloud.admin.api.dto.SysLogDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author cloud
 * @date 2018/6/28
 */
@FeignClient(contextId = "remoteLogService", value = ServiceNameConstants.UPMS_SERVICE)
public interface RemoteLogService {

	/**
	 * 保存日志
	 * @param sysLog 日志实体
	 * @param from 是否内部调用
	 * @return succes、false
	 */
	@PostMapping("/log/save")
    R<Boolean> saveLog(@RequestBody SysLogDTO sysLog, @RequestHeader(SecurityConstants.FROM) String from);

}
