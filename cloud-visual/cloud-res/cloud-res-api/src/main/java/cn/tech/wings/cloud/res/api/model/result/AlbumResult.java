package cn.tech.wings.cloud.res.api.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 相册表
 * </p>
 *
 * @author dijuli
 * @since 2019-10-09
 */
@Data
@ApiModel(value = "相册返回对象")
public class AlbumResult implements Serializable {

    private static final long serialVersionUID = 1L;


	@ApiModelProperty(value="主键id")
    private Integer id;

	@ApiModelProperty(value="主键id")
    private Date addTime;

	@ApiModelProperty(value="删除标识")
    private Boolean deleteStatus;

	@ApiModelProperty(value="相册描述")
    private String albumInfo;

	@ApiModelProperty(value="是否默认  0:否   1:")
    private Boolean albumDefault;

	@ApiModelProperty(value="相册名称")
    private String albumName;

	@ApiModelProperty(value="相册序号-序号越大越靠前")
    private Integer albumSequence;

	@ApiModelProperty(value="封面")
    private String albumCoverUrl;

	@ApiModelProperty(value="相册编码")
    private String albumCode;

	@ApiModelProperty(value="相册下的文件数量")
    private Integer fileCount;

	@ApiModelProperty(value="父分类id")
    private Integer parentid;

	@ApiModelProperty(value="树id")
    private String treeid;

	@ApiModelProperty(value="资源类型：pic、video、file")
    private String resType;

	@ApiModelProperty(value="拥有者id")
    private Integer ownerId;

	@ApiModelProperty(value="文件夹下可以使用总容量0为不限，单位字节")
    private Float totalSize;

	@ApiModelProperty(value="文件夹下已使用大小")
    private Float useSize;

	@ApiModelProperty(value="0为叶子节点；1不是")
    private Integer isleaf;

	@ApiModelProperty(value="交易中心code")
	private String tcCode;

	@ApiModelProperty(value="0: 非公有 1:公有")
	private Boolean publicStatus;

}
