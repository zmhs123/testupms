package cn.tech.wings.cloud.admin.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 产品线和产品表
 *
 * @author lixuanxing
 * @date 2022-12-05 09:46:01
 */
@Data
@TableName("sys_product_line")
@ApiModel(value = "产品线和产品表")
public class SysProductLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "排序")
    private Integer sortOrder;

    @ApiModelProperty(value = "类型 0产品线 1产品")
    private Integer productType;

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
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 修改时间
	 */
	@TableField(fill = FieldFill.UPDATE)
	@ApiModelProperty(value = "修改时间")
	private LocalDateTime updateTime;

	/**
	 * 0-正常，1-删除
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	@ApiModelProperty(value = "删除标记,1:已删除,0:正常")
	private String delFlag;

    @ApiModelProperty(value = "父id")
    private Long parentId;

    @ApiModelProperty(value = "部门id")
    private Long deptId;

}
