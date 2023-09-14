package cn.tech.wings.cloud.res.api.model.result;

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
@ApiModel(value = "附件跨服务返回对象")
public class AccessoryFeignResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="主键id")
    private Integer id;

    @ApiModelProperty(value="文件后缀")
    private String ext;

    @ApiModelProperty(value="文件名")
    private String name;

    @ApiModelProperty(value="路径")
    private String path;

    @ApiModelProperty(value="大小")
    private Float size;

	@ApiModelProperty(value="文件读取全路径,公有库用")
    private String readPath;

}
