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
 *
 * </p>
 *
 * @author fengchu_pang
 * @since 2020-11-20
 */
@Data
@TableName("res_water_mark")
@ApiModel(value = "水印表")
public class WaterMark extends Model<WaterMark> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 创建时间
     */
    @TableField("add_time")
    private Date addTime;

    /**
     * 删除标识  0正常  1删除
     */
    @TableField("delete_status")
    private Boolean deleteStatus;

    /**
     * 图片水银wm_image_alpha
     */
    @TableField("wm_image_alpha")
    private Float wmImageAlpha;

    /**
     * 图片水银是否开启
     */
    @TableField("wm_image_open")
    private Boolean wmImageOpen;

    /**
     * 图片水银位置
     */
    @TableField("wm_image_pos")
    private Integer wmImagePos;

    /**
     * 水银文字
     */
    @TableField("wm_text")
    private String wmText;

    /**
     * 文字水银颜色
     */
    @TableField("wm_text_color")
    private String wmTextColor;

    /**
     * 文字水银wm_text_font
     */
    @TableField("wm_text_font")
    private String wmTextFont;

    /**
     * 文字水银尺寸
     */
    @TableField("wm_text_font_size")
    private Integer wmTextFontSize;

    /**
     * 文字水银是否开启
     */
    @TableField("wm_text_open")
    private Boolean wmTextOpen;

    /**
     * 文字水银位置
     */
    @TableField("wm_text_pos")
    private Integer wmTextPos;

    /**
     * 图片水银图片id
     */
    @TableField("wm_image_id")
    private Long wmImageId;

    /**
     * 是否后台管理员，0 不是  1 是
     */
    @TableField("is_admin")
    private Integer isAdmin;

}
