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

package cn.tech.wings.cloud.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.tech.wings.cloud.admin.api.entity.SysDept;
import cn.tech.wings.cloud.admin.api.entity.SysDeptRelation;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author cloud
 * @since 2018-02-12
 */
public interface SysDeptRelationService extends IService<SysDeptRelation> {

	/**
	 * 新建部门关系
	 * @param sysDept 部门
	 */
	void insertDeptRelation(SysDept sysDept);

	/**
	 * 通过ID删除部门关系
	 * @param id
	 */
	void deleteAllDeptRealtion(Long id);

	/**
	 * 更新部门关系
	 * @param relation
	 */
	void updateDeptRealtion(SysDeptRelation relation);

}
