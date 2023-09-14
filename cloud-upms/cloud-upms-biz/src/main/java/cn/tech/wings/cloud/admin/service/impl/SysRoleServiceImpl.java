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

package cn.tech.wings.cloud.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.tech.wings.cloud.admin.api.entity.SysRole;
import cn.tech.wings.cloud.admin.api.entity.SysRoleMenu;
import cn.tech.wings.cloud.admin.api.vo.RoleExcelVO;
import cn.tech.wings.cloud.admin.api.vo.RoleVO;
import cn.tech.wings.cloud.admin.mapper.SysRoleMapper;
import cn.tech.wings.cloud.admin.service.SysRoleMenuService;
import cn.tech.wings.cloud.admin.service.SysRoleService;
import cn.tech.wings.cloud.common.core.constant.CacheConstants;
import cn.tech.wings.cloud.common.core.exception.ErrorCodes;
import cn.tech.wings.cloud.common.core.util.MsgUtils;
import cn.tech.wings.cloud.common.core.util.R;
import cn.tech.wings.cloud.common.excel.vo.ErrorMessage;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author cloud
 * @since 2017-10-29
 */
@Service
@AllArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

	private SysRoleMenuService roleMenuService;

	/**
	 * 通过用户ID，查询角色信息
	 * @param userId
	 * @return
	 */
	@Override
	public List findRolesByUserId(Long userId) {
		return baseMapper.listRolesByUserId(userId);
	}

	/**
	 * 根据角色ID 查询角色列表，注意缓存删除
	 * @param roleIdList 角色ID列表
	 * @param key 缓存key
	 * @return
	 */
	@Override
	@Cacheable(value = CacheConstants.ROLE_DETAILS, key = "#key")
	public List<SysRole> findRolesByRoleIds(List<Long> roleIdList, String key) {
		return baseMapper.selectBatchIds(roleIdList);
	}

	/**
	 * 通过角色ID，删除角色,并清空角色菜单缓存
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean removeRoleById(Long id) {
		roleMenuService.remove(Wrappers.<SysRoleMenu>update().lambda().eq(SysRoleMenu::getRoleId, id));
		return this.removeById(id);
	}

	/**
	 * 根据角色菜单列表
	 * @param roleVo 角色&菜单列表
	 * @return
	 */
	@Override
	public Boolean updateRoleMenus(RoleVO roleVo) {
		return roleMenuService.saveRoleMenus(roleVo.getRoleId(), roleVo.getMenuIds());
	}

	/**
	 * 导入角色
	 * @param excelVOList 角色列表
	 * @param bindingResult 错误信息列表
	 * @return ok fail
	 */
	@Override
	public R importRole(List<RoleExcelVO> excelVOList, BindingResult bindingResult) {
		// 通用校验获取失败的数据
		List<ErrorMessage> errorMessageList = (List<ErrorMessage>) bindingResult.getTarget();

		// 个性化校验逻辑
		List<SysRole> roleList = this.list();

		for (RoleExcelVO excel : excelVOList) {

		}
		// 执行数据插入操作 组装 RoleDto
		for (RoleExcelVO excel : excelVOList) {
			Set<String> errorMsg = new HashSet<>();
			// 检验角色名称或者角色编码是否存在
			boolean existRole = roleList.stream().anyMatch(sysRole -> excel.getRoleName().equals(sysRole.getRoleName())
					|| excel.getRoleCode().equals(sysRole.getRoleCode()));

			if (existRole) {
				errorMsg.add(MsgUtils.getMessage(ErrorCodes.SYS_ROLE_NAMEORCODE_EXISTING, excel.getRoleName(),
						excel.getRoleCode()));
			}

			// 数据合法情况
			if (CollUtil.isEmpty(errorMsg)) {
				insertExcelRole(excel);
			}
			else {
				// 数据不合法情况
				errorMessageList.add(new ErrorMessage(excel.getLineNum(), errorMsg));
			}
		}
		if (CollUtil.isNotEmpty(errorMessageList)) {
			return R.failed(errorMessageList);
		}
		return R.ok();
	}

	/**
	 * 查询全部的角色
	 * @return list
	 */
	@Override
	public List<RoleExcelVO> listRole() {
		List<SysRole> roleList = this.list(Wrappers.emptyWrapper());
		// 转换成execl 对象输出
		return roleList.stream().map(role -> {
			RoleExcelVO roleExcelVO = new RoleExcelVO();
			BeanUtil.copyProperties(role, roleExcelVO);
			return roleExcelVO;
		}).collect(Collectors.toList());
	}

	/**
	 * 插入excel Role
	 */
	private void insertExcelRole(RoleExcelVO excel) {
		SysRole sysRole = new SysRole();
		sysRole.setRoleName(excel.getRoleName());
		sysRole.setRoleDesc(excel.getRoleDesc());
		sysRole.setRoleCode(excel.getRoleCode());
		this.save(sysRole);
	}

}
