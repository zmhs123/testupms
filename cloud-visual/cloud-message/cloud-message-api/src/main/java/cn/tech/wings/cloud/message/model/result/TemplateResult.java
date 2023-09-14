package cn.tech.wings.cloud.message.model.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 通知模板表
 * </p>
 *
 * @author 庞凤楚
 * @since 2019-11-15
 */
@Data
public class TemplateResult implements Serializable {

    private static final long serialVersionUID = 1L;

	@ApiModelProperty("主键id")
	private Long id;

	@ApiModelProperty("添加时间")
	private Date addTime;

	@ApiModelProperty("模板内容")
	private String content;

	@ApiModelProperty("模板说明")
	private String info;

	@ApiModelProperty("标识")
	private String mark;

	@ApiModelProperty("是否开启 0否  1是")
	private Boolean open;

	@ApiModelProperty("模板标题")
	private String title;

	@ApiModelProperty("模板类型 msg系统消息 wx微信推送 sms短信")
	private String type;

	@ApiModelProperty("模板编号")
	private String templateCode;

	@ApiModelProperty("跳转链接")
	private String url;

	@ApiModelProperty("模板所属微信appid")
	private String appId;

}
