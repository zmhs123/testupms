package cn.tech.wings.cloud.res.api.model.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 相册表
 * </p>
 *
 * @author dijuli
 * @since 2019-10-09
 */
@Data
@ApiModel(value="相册表对象模型")
public class AlbumParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="主键id")
    private Integer id;

    @ApiModelProperty(value="相册编码")
    private String albumCode;

    @ApiModelProperty(value="相册名称")
    private String albumName;

    @ApiModelProperty(value="相册序号-序号越大越靠前")
    private Integer albumSequence;

    @ApiModelProperty(value="父分类id")
    private Integer parentid;

    @ApiModelProperty(value="拥有者id（店铺id）")
    private Integer ownerId;

    @ApiModelProperty(value="资源类型：pic、vedio、file")
    private String resType;

	@ApiModelProperty(value="文件夹下可以使用总容量0为不限，单位字节")
    private Float totalSize;

	@ApiModelProperty(value="树id")
    private String treeid;

	@ApiModelProperty(value="交易中心code")
	private String tcCode;

	@ApiModelProperty(value="是否公有库资源 0: 非公有 1:公有")
	private Boolean publicStatus;

}
