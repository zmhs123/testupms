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

package cn.tech.wings.cloud.admin.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 岗位信息表
 *
 * @author fxz
 * @date 2022-03-26 12:50:43
 */
@Data
@TableName("sys_post")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "岗位信息表")
public class SysPost extends Model<SysPost> {

	private static final long serialVersionUID = 1L;

	/**
	 * 岗位ID
	 */
	@TableId(value = "post_id", type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "岗位ID")
	private Long postId;

	/**
	 * 岗位编码
	 */
	@NotBlank(message = "岗位编码不能为空")
	@ApiModelProperty(value = "岗位编码")
	private String postCode;

	/**
	 * 岗位名称
	 */
	@NotBlank(message = "岗位名称不能为空")
	@ApiModelProperty(value = "岗位名称")
	private String postName;

	/**
	 * 岗位排序
	 */
	@NotNull(message = "排序值不能为空")
	@ApiModelProperty(value = "岗位排序")
	private Integer postSort;

	/**
	 * 岗位描述
	 */
	@ApiModelProperty(value = "岗位描述")
	private String remark;

	/**
	 * 创建人
	 */
	@TableField(fill = FieldFill.INSERT)
	@ApiModelProperty(value = "创建人")
	private String createBy;

	/**
	 * 修改人
	 */
	@TableField(fill = FieldFill.UPDATE)
	@ApiModelProperty(value = "修改人")
	private String updateBy;

	/**
	 * 是否删除 -1：已删除 0：正常
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	@ApiModelProperty(value = "是否删除  -1：已删除  0：正常")
	private String delFlag;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	@TableField(fill = FieldFill.UPDATE)
	private LocalDateTime updateTime;

}
