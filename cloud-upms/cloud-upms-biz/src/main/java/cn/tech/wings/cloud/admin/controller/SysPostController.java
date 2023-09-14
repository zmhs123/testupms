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

import cn.hutool.core.util.StrUtil;
import cn.tech.wings.cloud.common.log.annotation.SysLog;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.tech.wings.cloud.admin.api.entity.SysPost;
import cn.tech.wings.cloud.admin.api.vo.PostExcelVO;
import cn.tech.wings.cloud.admin.service.SysPostService;
import cn.tech.wings.cloud.common.core.util.R;
import cn.tech.wings.cloud.common.excel.annotation.RequestExcel;
import cn.tech.wings.cloud.common.excel.annotation.ResponseExcel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 岗位信息表
 *
 * @author fxz
 * @date 2022-03-26 12:50:43
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
@Api(value = "post", tags = "岗位信息表管理")
public class SysPostController {

	private final SysPostService sysPostService;

	/**
	 * 获取岗位列表
	 * @return 岗位列表
	 */
	@GetMapping("/list")
	public R<List<SysPost>> listPosts() {
		return R.ok(sysPostService.list(Wrappers.emptyWrapper()));
	}

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param sysPost 岗位信息表
	 * @return
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('sys_post_view')")
	public R getSysPostPage(Page page, SysPost sysPost) {
		return R.ok(sysPostService.page(page, Wrappers.lambdaQuery(sysPost).orderByDesc(SysPost::getCreateTime)));
	}

	/**
	 * 通过id查询岗位信息表
	 * @param postId id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询", notes = "通过id查询")
	@GetMapping("/{postId}")
	@PreAuthorize("@pms.hasPermission('sys_post_view')")
	public R getById(@PathVariable("postId") Long postId) {
		return R.ok(sysPostService.getById(postId));
	}

	/**
	 * 新增岗位信息表
	 * @param sysPost 岗位信息表
	 * @return R
	 */
	@ApiOperation(value = "新增岗位信息表", notes = "新增岗位信息表")
	@SysLog("新增岗位信息表")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('sys_post_add')")
	public R save(@RequestBody SysPost sysPost) {
		List<SysPost> sysPostList = sysPostService.list(
				Wrappers.<SysPost>lambdaQuery()
						.eq(StrUtil.isNotBlank(sysPost.getPostCode()), SysPost::getPostCode, sysPost.getPostCode())
		);
		if (sysPostList.size() > 0) {
			return R.failed("岗位标识已存在");
		}
		return R.ok(sysPostService.save(sysPost));
	}

	/**
	 * 修改岗位信息表
	 * @param sysPost 岗位信息表
	 * @return R
	 */
	@ApiOperation(value = "修改岗位信息表", notes = "修改岗位信息表")
	@SysLog("修改岗位信息表")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('sys_post_edit')")
	public R updateById(@RequestBody SysPost sysPost) {
		List<SysPost> sysPostList = sysPostService.list(
				Wrappers.<SysPost>lambdaQuery()
						.eq(StrUtil.isNotBlank(sysPost.getPostCode()), SysPost::getPostCode, sysPost.getPostCode())
						.ne(SysPost::getPostId,sysPost.getPostId())
		);
		if (sysPostList.size() > 0) {
			return R.failed("岗位标识已存在");
		}
		return R.ok(sysPostService.updateById(sysPost));
	}

	/**
	 * 通过id删除岗位信息表
	 * @param postId id
	 * @return R
	 */
	@ApiOperation(value = "通过id删除岗位信息表", notes = "通过id删除岗位信息表")
	@SysLog("通过id删除岗位信息表")
	@DeleteMapping("/{postId}")
	@PreAuthorize("@pms.hasPermission('sys_post_del')")
	public R removeById(@PathVariable Long postId) {
		return R.ok(sysPostService.removeById(postId));
	}

	/**
	 * 导出excel 表格
	 * @return excel 文件流
	 */
	@ResponseExcel
	@GetMapping("/export")
	@PreAuthorize("@pms.hasPermission('sys_post_export')")
	public List<PostExcelVO> export() {
		return sysPostService.listPost();
	}

	/**
	 * 导入岗位
	 * @param excelVOList 岗位列表
	 * @param bindingResult 错误信息列表
	 * @return ok fail
	 */
	@PostMapping("/import")
	@PreAuthorize("@pms.hasPermission('sys_post_export')")
	public R importRole(@RequestExcel List<PostExcelVO> excelVOList, BindingResult bindingResult) {
		return sysPostService.importPost(excelVOList, bindingResult);
	}

}
