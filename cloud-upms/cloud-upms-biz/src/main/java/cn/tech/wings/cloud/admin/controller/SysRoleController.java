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
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.tech.wings.cloud.common.log.annotation.SysLog;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.tech.wings.cloud.admin.api.entity.SysRole;
import cn.tech.wings.cloud.admin.api.vo.RoleExcelVO;
import cn.tech.wings.cloud.admin.api.vo.RoleVO;
import cn.tech.wings.cloud.admin.service.SysRoleService;
import cn.tech.wings.cloud.common.core.constant.CacheConstants;
import cn.tech.wings.cloud.common.core.util.R;
import cn.tech.wings.cloud.common.excel.annotation.RequestExcel;
import cn.tech.wings.cloud.common.excel.annotation.ResponseExcel;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author cloud
 * @date 2020-02-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/role")
@Api(value = "role", tags = "角色管理模块")
public class SysRoleController {

	private final SysRoleService sysRoleService;

	/**
	 * 通过ID查询角色信息
	 * @param id ID
	 * @return 角色信息
	 */
	@GetMapping("/{id}")
	public R getById(@PathVariable Long id) {
		return R.ok(sysRoleService.getById(id));
	}

	/**
	 * 通过角色编码查询角色信息
	 * @param code 角色Code
	 * @return 角色信息
	 */
	@GetMapping("/code/{code}")
	public R getByRoleCode(@PathVariable String code) {
		return R.ok(sysRoleService.list(Wrappers.<SysRole>lambdaQuery().eq(SysRole::getRoleCode, code)));
	}

	/**
	 * 添加角色
	 * @param sysRole 角色信息
	 * @return success、false
	 */
	@SysLog("添加角色")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('sys_role_add')")
	@CacheEvict(value = CacheConstants.ROLE_DETAILS, allEntries = true)
	public R save(@Valid @RequestBody SysRole sysRole) {
		List<SysRole> sysRoleList = sysRoleService.list(Wrappers.<SysRole>lambdaQuery()
				.eq(SysRole::getRoleCode, sysRole.getRoleCode())
		);
		if (CollectionUtil.isNotEmpty(sysRoleList)) {
			return R.failed("角色标识已存在");
		}
		return R.ok(sysRoleService.save(sysRole));
	}

	/**
	 * 修改角色
	 * @param sysRole 角色信息
	 * @return success/false
	 */
	@SysLog("修改角色")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('sys_role_edit')")
	@CacheEvict(value = CacheConstants.ROLE_DETAILS, allEntries = true)
	public R update(@Valid @RequestBody SysRole sysRole) {
		List<SysRole> sysRoleList = sysRoleService.list(Wrappers.<SysRole>lambdaQuery()
				.eq(SysRole::getRoleCode, sysRole.getRoleCode())
				.ne(SysRole::getRoleId, sysRole.getRoleId())
		);
		if (CollectionUtil.isNotEmpty(sysRoleList)) {
			return R.failed("角色标识已存在");
		}
		return R.ok(sysRoleService.updateById(sysRole));
	}

	/**
	 * 删除角色
	 * @param id
	 * @return
	 */
	@SysLog("删除角色")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('sys_role_del')")
	@CacheEvict(value = CacheConstants.ROLE_DETAILS, allEntries = true)
	public R removeById(@PathVariable Long id) {
		return R.ok(sysRoleService.removeRoleById(id));
	}

	/**
	 * 获取角色列表
	 * @return 角色列表
	 */
	@GetMapping("/list")
	public R listRoles() {
		return R.ok(sysRoleService.list(Wrappers.emptyWrapper()));
	}

	/**
	 * 分页查询角色信息
	 * @param page 分页对象
	 * @param role 查询条件
	 * @return 分页对象
	 */
	@GetMapping("/page")
	public R getRolePage(Page page, SysRole role) {
		LambdaQueryWrapper<SysRole> objectLambdaQueryWrapper = Wrappers.lambdaQuery();
		objectLambdaQueryWrapper
				.like(StrUtil.isNotBlank(role.getRoleName()),SysRole::getRoleName,role.getRoleName())
				.like(StrUtil.isNotBlank(role.getRoleCode()),SysRole::getRoleCode,role.getRoleCode())
				.eq(role.getDsType() != null ,SysRole::getDsType,role.getDsType())
				.orderByDesc(SysRole::getCreateTime);
		return R.ok(sysRoleService.page(page, objectLambdaQueryWrapper));
	}

	/**
	 * 更新角色菜单
	 * @param roleVo 角色对象
	 * @return success、false
	 */
	@SysLog("更新角色菜单")
	@PutMapping("/menu")
	@PreAuthorize("@pms.hasPermission('sys_role_perm')")
	public R saveRoleMenus(@RequestBody RoleVO roleVo) {
		return R.ok(sysRoleService.updateRoleMenus(roleVo));
	}

	/**
	 * 通过角色ID 查询角色列表
	 * @param roleIdList 角色ID
	 * @return
	 */
	@PostMapping("/getRoleList")
	public R getRoleList(@RequestBody List<Long> roleIdList) {
		return R.ok(sysRoleService.findRolesByRoleIds(roleIdList, CollUtil.join(roleIdList, StrUtil.UNDERLINE)));
	}

	/**
	 * 导出excel 表格
	 * @return
	 */
	@ResponseExcel
	@GetMapping("/export")
	@PreAuthorize("@pms.hasPermission('sys_role_export')")
	public List<RoleExcelVO> export() {
		return sysRoleService.listRole();
	}

	/**
	 * 导入角色
	 * @param excelVOList 角色列表
	 * @param bindingResult 错误信息列表
	 * @return ok fail
	 */
	@PostMapping("/import")
	@PreAuthorize("@pms.hasPermission('sys_role_export')")
	public R importRole(@RequestExcel List<RoleExcelVO> excelVOList, BindingResult bindingResult) {
		return sysRoleService.importRole(excelVOList, bindingResult);
	}

}
