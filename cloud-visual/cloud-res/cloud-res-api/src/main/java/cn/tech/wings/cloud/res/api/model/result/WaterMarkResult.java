package cn.tech.wings.cloud.res.api.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
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
@ApiModel(value = "水印返回对象")
public class WaterMarkResult implements Serializable {

    private static final long serialVersionUID = 1L;


	@ApiModelProperty(value="id")
    private Integer id;

	@ApiModelProperty(value="创建时间")
    private Date addTime;

	@ApiModelProperty(value="删除标识")
    private Boolean deleteStatus;

	@ApiModelProperty(value="图片水印wm_image_alpha")
    private Float wmImageAlpha;

	@ApiModelProperty(value="图片水印是否开启")
    private Boolean wmImageOpen;

	@ApiModelProperty(value="图片水印位置")
    private Integer wmImagePos;

	@ApiModelProperty(value="水印文字")
    private String wmText;

	@ApiModelProperty(value="文字水印颜色")
    private String wmTextColor;

	@ApiModelProperty(value="文字水印wm_text_font")
    private String wmTextFont;

	@ApiModelProperty(value="文字水印尺寸")
    private Integer wmTextFontSize;

	@ApiModelProperty(value="文字水印是否开启")
    private Boolean wmTextOpen;

	@ApiModelProperty(value="文字水印位置")
    private Integer wmTextPos;

	@ApiModelProperty(value="图片水印图片id")
    private Integer wmImageId;

	@ApiModelProperty(value="是否后台管理员")
    private Integer isAdmin;

}
