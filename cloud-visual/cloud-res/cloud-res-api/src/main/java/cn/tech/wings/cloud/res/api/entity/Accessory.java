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
 * 附件表
 * </p>
 *
 * @author dijuli
 * @since 2019-10-09
 */
@TableName("res_accessory")
@Data
@ApiModel(value = "附件表")
public class Accessory extends Model<Accessory> {

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
     * 删除标识  0:正常  1:删除
     */
    @TableField("delete_status")
    private Boolean deleteStatus;

    /**
     * 文件后缀
     */
    @TableField("ext")
    private String ext;

    /**
     * 高度
     */
    @TableField("height")
    private Integer height;

    /**
     * 文件名
     */
    @TableField("name")
    private String name;

    /**
     * 路径
     */
    @TableField("path")
    private String path;

    /**
     * 大小
     */
    @TableField("size")
    private Long size;

    /**
     * 宽度
     */
    @TableField("width")
    private Integer width;

    /**
     * 相册id
     */
    @TableField("album_id")
    private Integer albumId;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 图片类型id--对应设置上传设置--存放类型
     */
    @TableField("config_id")
    private Integer configId;

}
