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

package cn.tech.wings.cloud.admin.mapper;

import cn.tech.wings.cloud.admin.api.entity.SysTenant;
import cn.tech.wings.cloud.common.data.datascope.CloudBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 租户
 *
 * @author cloud
 * @date 2019-05-15 15:55:41
 */
@Mapper
public interface SysTenantMapper extends CloudBaseMapper<SysTenant> {

}
