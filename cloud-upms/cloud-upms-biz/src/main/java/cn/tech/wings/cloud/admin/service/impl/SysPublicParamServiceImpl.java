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

package cn.tech.wings.cloud.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.tech.wings.cloud.admin.api.entity.SysPublicParam;
import cn.tech.wings.cloud.admin.mapper.SysPublicParamMapper;
import cn.tech.wings.cloud.admin.service.SysPublicParamService;
import cn.tech.wings.cloud.common.core.constant.CacheConstants;
import cn.tech.wings.cloud.common.core.constant.enums.DictTypeEnum;
import cn.tech.wings.cloud.common.core.exception.ErrorCodes;
import cn.tech.wings.cloud.common.core.util.MsgUtils;
import cn.tech.wings.cloud.common.core.util.R;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 公共参数配置
 *
 * @author Lucky
 * @date 2019-04-29
 */
@Service
@AllArgsConstructor
public class SysPublicParamServiceImpl extends ServiceImpl<SysPublicParamMapper, SysPublicParam>
		implements SysPublicParamService {

	@Override
	@Cacheable(value = CacheConstants.PARAMS_DETAILS, key = "#publicKey", unless = "#result == null ")
	public String getSysPublicParamKeyToValue(String publicKey) {
		SysPublicParam sysPublicParam = this.baseMapper
				.selectOne(Wrappers.<SysPublicParam>lambdaQuery().eq(SysPublicParam::getPublicKey, publicKey));

		if (sysPublicParam != null) {
			return sysPublicParam.getPublicValue();
		}
		return null;
	}

	/**
	 * 更新参数
	 * @param sysPublicParam
	 * @return
	 */
	@Override
	@CacheEvict(value = CacheConstants.PARAMS_DETAILS, key = "#sysPublicParam.publicKey")
	public R updateParam(SysPublicParam sysPublicParam) {
		SysPublicParam param = this.getById(sysPublicParam.getPublicId());
		// 系统内置
		if (DictTypeEnum.SYSTEM.getType().equals(param.getSystemFlag())) {
			return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_PARAM_DELETE_SYSTEM));
		}
		return R.ok(this.updateById(sysPublicParam));
	}

	/**
	 * 删除参数
	 * @param publicId
	 * @return
	 */
	@Override
	@CacheEvict(value = CacheConstants.PARAMS_DETAILS, allEntries = true)
	public R removeParam(Long publicId) {
		SysPublicParam param = this.getById(publicId);
		// 系统内置
		if (DictTypeEnum.SYSTEM.getType().equals(param.getSystemFlag())) {
			return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_PARAM_DELETE_SYSTEM));
		}
		return R.ok(this.removeById(publicId));
	}

	/**
	 * 同步缓存
	 * @return R
	 */
	@Override
	@CacheEvict(value = CacheConstants.PARAMS_DETAILS, allEntries = true)
	public R syncParamCache() {
		return R.ok();
	}

}
