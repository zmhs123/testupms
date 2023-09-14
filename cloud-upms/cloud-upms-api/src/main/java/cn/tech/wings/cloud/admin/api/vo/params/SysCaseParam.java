package cn.tech.wings.cloud.admin.api.vo.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
    import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lixuan_xing
 * @date 2022-12-12 17:19:11
 */
@Data
@ApiModel(value = "用例表Param")
public class SysCaseParam implements Serializable {

	private static final long serialVersionUID = 1L;


	@ApiModelProperty(value="id")
	private Long id;


	@ApiModelProperty(value="名称")
	private String name;


	@ApiModelProperty(value="编码")
	private String caseCode;


	@ApiModelProperty(value="排序")
	private Integer sortOrder;


	@ApiModelProperty(value="创建人")
	private String createBy;


	@ApiModelProperty(value="修改人")
	private String updateBy;


	@ApiModelProperty(value="创建时间")
	private LocalDateTime createTime;


	@ApiModelProperty(value="修改时间")
	private LocalDateTime updateTime;


	@ApiModelProperty(value="删除标记")
	private String delFlag;


	@ApiModelProperty(value="应用id")
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

	@ApiModelProperty(value = "用例描述")
	private String caseRemark;

	@ApiModelProperty(value = "用例路径")
	private String path;
}
