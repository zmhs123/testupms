package cn.tech.wings.cloud.res.api.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 附件表
 * </p>
 *
 * @author dijuli
 * @since 2019-10-09
 */
@Data
@ApiModel(value = "附件返回对象")
public class AccessoryResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="主键id")
    private Integer id;

    @ApiModelProperty(value="创建时间")
    private Date addTime;

    @ApiModelProperty(value="删除标识  0:正常  1:删除")
    private Boolean deleteStatus;

    @ApiModelProperty(value="文件后缀")
    private String ext;

    @ApiModelProperty(value="页数")
    private Integer pageNum;

    @ApiModelProperty(value="高度")
    private Integer height;

    @ApiModelProperty(value="文件名")
    private String name;

    @ApiModelProperty(value="路径")
    private String path;

    @ApiModelProperty(value="大小")
    private Float size;

    @ApiModelProperty(value="宽度")
    private Integer width;

    @ApiModelProperty(value="相册id")
    private Integer albumId;

    @ApiModelProperty(value="用户id")
    private Integer userId;

    @ApiModelProperty(value="图片类型id--对应设置上传设置--存放类型")
    private Integer configId;

	@ApiModelProperty(value="交易中心code")
	private String tcCode;
}
