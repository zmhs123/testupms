package cn.tech.wings.cloud.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 消息表
 * </p>
 *
 * @author 庞凤楚
 * @since 2019-11-18
 */
@TableName("msg_message")
@Data
@ApiModel(value = "消息")
public class Message {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id")
    private Long id;

    @TableField("add_time")
    @ApiModelProperty(value = "创建时间")
    private Date addTime;

    @TableField("delete_status")
    @ApiModelProperty(value = "删除标识")
    private Boolean deleteStatus;

    @TableField("content")
    @ApiModelProperty(value = "消息内容")
    private String content;

    @TableField("title")
    @ApiModelProperty(value = "消息标题")
    private String title;

    @TableField("msg_type")
    @ApiModelProperty(value = "消息类型0纯文字")
    private Integer msgType;

    @TableField("type")
    @ApiModelProperty(value = "消息类型1短信 2微信小程序消息 3微信公众号消息 4APP消息")
    private Integer type;

    @TableField("from_user_id")
    @ApiModelProperty(value = "发送者用户id")
    private Long fromUserId;

    @TableField("to_user_id")
    @ApiModelProperty(value = "接收者用户id")
    private Long toUserId;

    @TableField("send_time")
    @ApiModelProperty(value = "消息发送时间戳，方便快速查询")
    private Long sendTime;

    @TableField("send_model")
    @ApiModelProperty(value = "发送形式 0 自动 1手动 判断是不是管理员再后台发送")
    private Boolean sendModel;

    @TableField("ident")
    @ApiModelProperty(value = "发送对象标识(手机号/openId)")
    private String ident;

    @TableField("result_content")
    @ApiModelProperty(value = "发送响应内容")
    private String resultContent;
}
