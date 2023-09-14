package cn.tech.wings.cloud.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("msg_template")
@Data
public class Template implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.ASSIGN_ID)
	@ApiModelProperty("主键id")
	private Long id;

	@TableField("add_time")
	@ApiModelProperty("添加时间")
	private Date addTime;

	@TableField("delete_status")
	@ApiModelProperty("删除标识  0否  1是")
	private Boolean deleteStatus;

	@TableField("content")
	@ApiModelProperty("模板内容")
	private String content;

	@TableField("info")
	@ApiModelProperty("模板说明")
	private String info;

	@TableField("mark")
	@ApiModelProperty("标识")
	private String mark;

	@TableField("open")
	@ApiModelProperty("是否开启 0否  1是")
	private Boolean open;

	@TableField("title")
	@ApiModelProperty("模板标题")
	private String title;

	@TableField("type")
	@ApiModelProperty("模板类型 msg系统消息 wx微信推送 sms短信")
	private String type;

	@TableField("template_code")
	@ApiModelProperty("模板编号")
	private String templateCode;

	@TableField("url")
	@ApiModelProperty("跳转链接")
	private String url;

	@TableField("app_id")
	@ApiModelProperty("模板所属微信appid")
	private String appId;
}
