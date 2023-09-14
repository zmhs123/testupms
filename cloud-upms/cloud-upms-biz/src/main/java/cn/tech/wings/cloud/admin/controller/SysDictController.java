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

import cn.hutool.core.util.StrUtil;
import cn.tech.wings.cloud.common.log.annotation.SysLog;
import cn.tech.wings.cloud.common.security.annotation.Inner;
import cn.tech.wings.cloud.admin.api.entity.SysDict;
import cn.tech.wings.cloud.admin.api.entity.SysDictItem;
import cn.tech.wings.cloud.admin.service.SysDictItemService;
import cn.tech.wings.cloud.admin.service.SysDictService;
import cn.tech.wings.cloud.common.core.constant.CacheConstants;
import cn.tech.wings.cloud.common.core.util.R;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典表 前端控制器
 * </p>
 *
 * @author cloud
 * @since 2019-03-19
 */
@RestController
@AllArgsConstructor
@RequestMapping("/dict")
@Api(value = "dict", tags = "字典管理模块")
public class SysDictController {

	private final SysDictService sysDictService;

	private final SysDictItemService sysDictItemService;

	/**
	 * 通过ID查询字典信息
	 * @param id ID
	 * @return 字典信息
	 */
	@GetMapping("/{id}")
	public R getById(@PathVariable Long id) {
		return R.ok(sysDictService.getById(id));
	}

	/**
	 * 分页查询字典信息
	 * @param page 分页对象
	 * @return 分页对象
	 */
	@GetMapping("/page")
	public R<IPage> getDictPage(Page page, SysDict sysDict) {
		LambdaQueryWrapper<SysDict> objectLambdaQueryWrapper = Wrappers.lambdaQuery();
		objectLambdaQueryWrapper
				.like(StrUtil.isNotBlank(sysDict.getDictType()),SysDict::getDictType,sysDict.getDictType())
				.eq(StrUtil.isNotBlank(sysDict.getSystemFlag()),SysDict::getSystemFlag,sysDict.getSystemFlag())
				.orderByDesc(SysDict::getCreateTime);
		return R.ok(sysDictService.page(page, objectLambdaQueryWrapper));
	}

	/**
	 * 通过字典类型查找字典
	 * @param type 类型
	 * @return 同类型字典
	 */
	@GetMapping("/type/{type}")
	@Cacheable(value = CacheConstants.DICT_DETAILS, key = "#type", unless = "#result.data.isEmpty()")
	public R<List<SysDictItem>> getDictByType(@PathVariable String type) {
		return R.ok(sysDictItemService.list(Wrappers.<SysDictItem>query().lambda().eq(SysDictItem::getDictType, type)));
	}

	/**
	 * 所有字典
	 * @return 同类型字典
	 */
	@ApiOperation("list")
	@GetMapping("/list")
//	@Cacheable(value = CacheConstants.DICT_ITEM_DETAILS, unless = "#result.data.isEmpty()")
	@Inner(value = false)
	public R<Map<String,Object>> getDictList() {
		return R.ok(sysDictItemService.ListAll());
	}

	/**
	 * 添加字典
	 * @param sysDict 字典信息
	 * @return success、false
	 */
	@SysLog("添加字典")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('sys_dict_add')")
	public R save(@Valid @RequestBody SysDict sysDict) {
		return R.ok(sysDictService.save(sysDict));
	}

	/**
	 * 删除字典，并且清除字典缓存
	 * @param id ID
	 * @return R
	 */
	@SysLog("删除字典")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('sys_dict_del')")
	public R removeById(@PathVariable Long id) {
		return sysDictService.removeDict(id);
	}

	/**
	 * 修改字典
	 * @param sysDict 字典信息
	 * @return success/false
	 */
	@PutMapping
	@SysLog("修改字典")
	@PreAuthorize("@pms.hasPermission('sys_dict_edit')")
	public R updateById(@Valid @RequestBody SysDict sysDict) {
		return sysDictService.updateDict(sysDict);
	}

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param sysDictItem 字典项
	 * @return
	 */
	@GetMapping("/item/page")
	public R getSysDictItemPage(Page page, SysDictItem sysDictItem) {
		return R.ok(sysDictItemService.page(page, Wrappers.query(sysDictItem)));
	}

	/**
	 * 通过id查询字典项
	 * @param id id
	 * @return R
	 */
	@GetMapping("/item/{id}")
	public R getDictItemById(@PathVariable("id") Long id) {
		return R.ok(sysDictItemService.getById(id));
	}

	/**
	 * 新增字典项
	 * @param sysDictItem 字典项
	 * @return R
	 */
	@SysLog("新增字典项")
	@PostMapping("/item")
	@CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
	public R save(@RequestBody SysDictItem sysDictItem) {
		return R.ok(sysDictItemService.save(sysDictItem));
	}

	/**
	 * 修改字典项
	 * @param sysDictItem 字典项
	 * @return R
	 */
	@SysLog("修改字典项")
	@PutMapping("/item")
	public R updateById(@RequestBody SysDictItem sysDictItem) {
		return sysDictItemService.updateDictItem(sysDictItem);
	}

	/**
	 * 通过id删除字典项
	 * @param id id
	 * @return R
	 */
	@SysLog("删除字典项")
	@DeleteMapping("/item/{id}")
	public R removeDictItemById(@PathVariable Long id) {
		return sysDictItemService.removeDictItem(id);
	}

	/**
	 * 同步缓存字典
	 * @return R
	 */
	@SysLog("同步字典")
	@PutMapping("/sync")
	public R sync() {
		return sysDictService.syncDictCache();
	}

}
