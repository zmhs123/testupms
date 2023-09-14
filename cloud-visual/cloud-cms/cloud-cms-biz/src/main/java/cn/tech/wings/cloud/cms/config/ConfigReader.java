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

package cn.tech.wings.cloud.cms.config;

import lombok.Getter;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author LCN on 2017/11/11
 */
@Component
@RefreshScope
@Getter
public class ConfigReader {

	/**
	 * 事务默认数据的位置，有最大时间
	 */
	private static final String KEY_PREFIX = "cms:default:";

	/**
	 * 负载均衡模块存储信息
	 */
	private static final String KEY_PREFIX_LOADBALANCE = "cms:loadbalance:";

	/**
	 * 补偿事务永久存储数据
	 */
	private static final String TX_MANAGER_COMPENSATE = "cms:compensate:";

}
