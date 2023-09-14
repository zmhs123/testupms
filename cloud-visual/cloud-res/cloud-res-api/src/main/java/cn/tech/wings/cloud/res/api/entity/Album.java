package cn.tech.wings.cloud.res.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 相册表
 * </p>
 *
 * @author dijuli
 * @since 2019-10-09
 */
@TableName("res_album")
@Data
@ApiModel(value = "相册表")
public class Album extends Model<Album> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 创建时间
     */
    @TableField("add_time")
    private Date addTime;

    /**
     * 删除标识，0:正常   1:删除
     */
    @TableField("delete_status")
    private Boolean deleteStatus;

    /**
     * 相册描述
     */
    @TableField("album_info")
    private String albumInfo;

    /**
     * 是否默认  0:否   1:是
     */
    @TableField("album_default")
    private Boolean albumDefault;

    /**
     * 相册名称
     */
    @TableField("album_name")
    private String albumName;

    /**
     * 相册序号-序号越大越靠前
     */
    @TableField("album_sequence")
    private Integer albumSequence;

    /**
     * 封面
     */
    @TableField("album_cover_url")
    private String albumCoverUrl;

    /**
     * 相册编码
     */
    @TableField("album_code")
    private String albumCode;

    /**
     * 相册下的文件数量
     */
    @TableField("file_count")
    private Integer fileCount;

    /**
     * 父分类id
     */
    @TableField("parentid")
    private Integer parentid;

    /**
     * 树id
     */
    @TableField("treeid")
    private String treeid;

    /**
     * 资源类型：pic、video、file
     */
    @TableField("res_type")
    private String resType;

    /**
     * 拥有者id（店铺id）
     */
    @TableField("owner_id")
    private Integer ownerId;

    /**
     * 文件夹下可以使用总容量0为不限，单位字节
     */
    @TableField("total_size")
    private Float totalSize;

    /**
     * 文件夹下已使用大小
     */
    @TableField("use_size")
    private Long useSize;

    /**
     * 0为叶子节点；1不是
     */
    @TableField("isleaf")
    private Integer isleaf;

	/**
	 * 交易中心code
	 */
	@TableField("tc_code")
	private String tcCode;


	/**
	 * 0: 非公有 1:公有
	 */
	@TableField("public_status")
	private Boolean publicStatus;

}
