package cn.tech.wings.cloud.admin.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用例表
 *
 * @author lixuan_xing
 * @date 2022-12-12 17:19:11
 */
@Data
@TableName("sys_case")
@ApiModel(value = "用例表")
public class SysCase implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "案例名称")
    private String name;

	@NotBlank(message = "用例编码不能为空")
    @ApiModelProperty(value = "编码")
    private String caseCode;

    @ApiModelProperty(value = "排序")
    private Integer sortOrder;

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

    @ApiModelProperty(value = "应用id")
    private Long projectId;

	/**
	 * 开始录制时间
	 */
	@ApiModelProperty(value = "开始录制时间")
	private LocalDateTime recordingTime;

	/**
	 * 结束录制时间
	 */
	@ApiModelProperty(value = "结束录制时间")
	private LocalDateTime endRecordingTime;

	/**
	 * 状态 0未录制 1录制中 2录制完成
	 */
	@ApiModelProperty(value = "状态 0未录制 1录制中 2录制完成")
	private Integer status;

	@ApiModelProperty(value = "操作步骤")
	private String caseRemark;

	@ApiModelProperty(value = "用例路径")
	private String path;

	/**
	 * 测试需求编号
	 */
	@ApiModelProperty(value = "测试需求编号")
	private String caseTestCode;

	/**
	 * 前置条件
	 */
	@ApiModelProperty(value = "前置条件")
	private String preconditions;


	/**
	 * 测试数据
	 */
	@ApiModelProperty(value = "测试数据")
	private String testData;


	/**
	 * 预期结果
	 */
	@ApiModelProperty(value = "预期结果")
	private String expectResult ;

	/**
	 * 案例类型
	 */
	@ApiModelProperty(value = "案例类型")
	private String caseType ;

	/**
	 * 正反例
	 */
	@ApiModelProperty(value = "正反例")
	private String positive2negative ;

	/**
	 * 案例编写人员
	 */
	@ApiModelProperty(value = "案例编写人员")
	private String createUser ;


	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remarks;
}
