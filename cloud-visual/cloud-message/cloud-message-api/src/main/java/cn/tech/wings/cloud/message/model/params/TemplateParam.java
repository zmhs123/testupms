package cn.tech.wings.cloud.message.model.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * <p>
 * 通知模板表
 * </p>
 *
 * @author 庞凤楚
 * @since 2019-11-15
 */
@Data
@ApiModel(value="通知模板表对象模型")
@SuperBuilder
public class TemplateParam implements Serializable {

    @ApiModelProperty(value="主键id")
    private Long id;

    @ApiModelProperty(value="模板内容",required = true)
    private String content;

    @ApiModelProperty(value="模板说明",required = true)
    private String info;

    @ApiModelProperty(value="标识",required = true)
    private String mark;

    @ApiModelProperty(value="是否开启 0否  1是")
    private Boolean open;

    @ApiModelProperty(value="模板标题",required = true)
    private String title;

    @ApiModelProperty(value="模板类型   msg系统消息   wx微信推送  sms短信",required = true)
    private String type;

    @ApiModelProperty(value="模板编号")
    private String templateCode;

    @ApiModelProperty(value="模板所属appid")
    private String appId;

}
