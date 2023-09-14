package cn.tech.wings.cloud.res.api.model.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author fengchu_pang
 * @since 2020-11-20
 */
@Data
@ApiModel(value="对象模型")
public class WaterMarkParam implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value="id")
    private Integer id;

    @ApiModelProperty(value="删除标识  0正常  1删除")
    private Boolean deleteStatus;

    @ApiModelProperty(value="图片水银wm_image_alpha")
    private Float wmImageAlpha;

    @ApiModelProperty(value="图片水银是否开启")
    private Boolean wmImageOpen;

    @ApiModelProperty(value="图片水银位置")
    private Integer wmImagePos;

    @ApiModelProperty(value="水银文字")
    private String wmText;

    @ApiModelProperty(value="文字水银颜色")
    private String wmTextColor;

    @ApiModelProperty(value="文字水银wm_text_font")
    private String wmTextFont;

    @ApiModelProperty(value="文字水银尺寸")
    private Integer wmTextFontSize;

    @ApiModelProperty(value="文字水银是否开启")
    private Boolean wmTextOpen;

    @ApiModelProperty(value="文字水银位置")
    private Integer wmTextPos;

    @ApiModelProperty(value="图片水银图片id")
    private Integer wmImageId;

    @ApiModelProperty(value="是否后台管理员，0 不是  1 是")
    private Integer isAdmin;

}
