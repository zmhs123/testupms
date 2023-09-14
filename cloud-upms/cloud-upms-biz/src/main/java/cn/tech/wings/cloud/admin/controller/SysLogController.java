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

package cn.tech.wings.cloud.admin.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.tech.wings.cloud.common.security.annotation.Inner;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.tech.wings.cloud.admin.api.dto.SysLogDTO;
import cn.tech.wings.cloud.admin.api.entity.SysLog;
import cn.tech.wings.cloud.admin.api.vo.PreLogVO;
import cn.tech.wings.cloud.admin.service.SysLogService;
import cn.tech.wings.cloud.common.core.util.R;
import cn.tech.wings.cloud.common.excel.annotation.ResponseExcel;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 日志表 前端控制器
 * </p>
 *
 * @author cloud
 * @since 2017-11-20
 */
@RestController
@AllArgsConstructor
@RequestMapping("/log")
@Api(value = "log", tags = "日志管理模块")
public class SysLogController {

	private final SysLogService sysLogService;

	/**
	 * 简单分页查询
	 * @param page 分页对象
	 * @param sysLog 系统日志
	 * @return
	 */
	@GetMapping("/page")
	public R getLogPage(Page page, SysLogDTO sysLog) {
		return R.ok(sysLogService.getLogByPage(page, sysLog));
	}

	/**
	 * 删除日志
	 * @param id ID
	 * @return success/false
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('sys_log_del')")
	public R removeById(@PathVariable Long id) {
		return R.ok(sysLogService.removeById(id));
	}

	/**
	 * 批量删除日志
	 * @param ids ID
	 * @return success/false
	 */
	@DeleteMapping
	@PreAuthorize("@pms.hasPermission('sys_log_del')")
	public R removeByIds(@RequestBody Long[] ids) {
		return R.ok(sysLogService.removeBatchByIds(CollUtil.toList(ids)));
	}

	/**
	 * 插入日志
	 * @param sysLog 日志实体
	 * @return success/false
	 */
	@Inner
	@PostMapping("/save")
	public R save(@Valid @RequestBody SysLogDTO sysLog) {
		return R.ok(sysLogService.saveLog(sysLog));
	}

	/**
	 * 批量插入前端异常日志
	 * @param preLogVoList 日志实体
	 * @return success/false
	 */
	@PostMapping("/logs")
	public R saveBatchLogs(@RequestBody List<PreLogVO> preLogVoList) {
		return R.ok(sysLogService.saveBatchLogs(preLogVoList));
	}

	/**
	 * 导出excel 表格
	 * @param sysLogDTO 查询条件
	 * @return
	 */
	@ResponseExcel
	@GetMapping("/export")
	@PreAuthorize("@pms.hasPermission('sys_log_export')")
	public List<SysLog> export(SysLogDTO sysLogDTO) {
		LambdaQueryWrapper<SysLog> wrapper = Wrappers.lambdaQuery();
		if (StrUtil.isNotBlank(sysLogDTO.getLogType())) {
			wrapper.eq(SysLog::getLogType, sysLogDTO.getLogType());
		}

		if (ArrayUtil.isNotEmpty(sysLogDTO.getCreateTime())) {
			wrapper.ge(SysLog::getCreateTime, sysLogDTO.getCreateTime()[0]).le(SysLog::getCreateTime,
					sysLogDTO.getCreateTime()[1]);
		}
		return sysLogService.list(wrapper);
	}

}
