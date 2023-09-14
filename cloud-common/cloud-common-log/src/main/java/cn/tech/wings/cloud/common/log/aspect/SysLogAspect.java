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

package cn.tech.wings.cloud.common.log.aspect;

import cn.hutool.core.util.StrUtil;
import cn.tech.wings.cloud.common.log.annotation.SysLog;
import cn.tech.wings.cloud.admin.api.dto.SysLogDTO;
import cn.tech.wings.cloud.common.core.util.KeyStrResolver;
import cn.tech.wings.cloud.common.log.event.SysLogEvent;
import cn.tech.wings.cloud.common.log.util.LogTypeEnum;
import cn.tech.wings.cloud.common.log.util.SysLogUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.expression.EvaluationContext;

/**
 * 操作日志使用spring event异步入库
 *
 * @author L.cm
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class SysLogAspect {

	private final ApplicationEventPublisher publisher;

	private final KeyStrResolver tenantKeyStrResolver;

	@SneakyThrows
	@Around("@annotation(sysLog)")
	public Object around(ProceedingJoinPoint point, SysLog sysLog) {
		String strClassName = point.getTarget().getClass().getName();
		String strMethodName = point.getSignature().getName();
		log.debug("[类名]:{},[方法]:{}", strClassName, strMethodName);

		String value = sysLog.value();
		String expression = sysLog.expression();
		// 当前表达式存在 SPEL，会覆盖 value 的值
		if (StrUtil.isNotBlank(expression)) {
			// 解析SPEL
			MethodSignature signature = (MethodSignature) point.getSignature();
			EvaluationContext context = SysLogUtils.getContext(point.getArgs(), signature.getMethod());
			try {
				value = SysLogUtils.getValue(context, expression, String.class);
			}
			catch (Exception e) {
				// SPEL 表达式异常，则获取 value 的值
				log.error("@SysLog 解析SPEL {} 异常", expression);
			}
		}

		SysLogDTO logDTO = SysLogUtils.getSysLog();
		logDTO.setTitle(value);
		// 发送异步日志事件
		Long startTime = System.currentTimeMillis();
		Object obj;
		try {
			obj = point.proceed();
		}
		catch (Exception e) {
			logDTO.setLogType(LogTypeEnum.ERROR.getType());
			logDTO.setException(e.getMessage());
			throw e;
		}
		finally {
			Long endTime = System.currentTimeMillis();
			logDTO.setTime(endTime - startTime);
			logDTO.setTenantId(Long.parseLong(tenantKeyStrResolver.key()));
			publisher.publishEvent(new SysLogEvent(logDTO));
		}
		return obj;
	}

}
