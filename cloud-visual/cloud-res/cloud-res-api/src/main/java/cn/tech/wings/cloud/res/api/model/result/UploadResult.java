package cn.tech.wings.cloud.res.api.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 上传文件返回对象
 * </p>
 *
 * @author fengchu_pang
 * @since 2019-10-09
 */
@Data
@ApiModel(value = "上传文件返回对象")
public class UploadResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="path")
    private String path;

    @ApiModelProperty(value="业务要存储的路径")
    private String value;

	@ApiModelProperty(value="文件读取全路径,前端显示用")
    private String readPath;

    @ApiModelProperty(value="文件名称")
    private String fileName;


}
