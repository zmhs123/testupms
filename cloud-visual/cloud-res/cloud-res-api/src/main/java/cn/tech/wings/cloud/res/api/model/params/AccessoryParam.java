package cn.tech.wings.cloud.res.api.model.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 附件表
 * </p>
 *
 * @author dijuli
 * @since 2019-10-09
 */
@Data
@ApiModel(value="附件表对象模型")
public class AccessoryParam implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value="主键id")
    private Integer id;

    @ApiModelProperty(value="文件后缀")
    private String ext;

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

    @ApiModelProperty(value="图片类型id--对应设置上传设置--存放类型")
    private Integer configId;

	@ApiModelProperty(value="交易中心code")
	private String tcCode;

}
