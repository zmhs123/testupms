package cn.tech.wings.cloud.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 短信服务表
 * </p>
 *
 * @author 庞凤楚
 * @since 2019-11-18
 */
@Data
@TableName("msg_verify_code")
public class VerifyCode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 添加时间
     */
    @TableField("add_time")
    private Date addTime;

    /**
     * 删除标识  0正常  1删除
     */
    @TableField("delete_status")
    private Boolean deleteStatus;

    /**
     * email|mobile
     */
    @TableField("receiver_type")
    private String receiverType;

    /**
     * 验证码接收账号（邮箱或手机号）
     */
    @TableField("receiver")
    private String receiver;

    /**
     * 用途 用户注册 validation_phone 修改密码  change_pwd 修改绑定手机号 change_phone 修改支付密码  change_pay_pwd
     */
    @TableField("use_type")
    private String useType;

    /**
     * 状态:0:未使用1:已使用
     */
    @TableField("use_status")
    private Boolean useStatus;

    /**
     * 验证码
     */
    @TableField("code")
    private String code;

    /**
     * 发送状态 0：未发送    其他：已发送
     */
    @TableField("send_status")
    private Integer sendStatus;

    /**
     * 短信内容参数
     */
    @TableField("content")
    private String content;

    /**
     * 模板id
     */
    @TableField("message_id")
    private String messageId;


    /**
     * 发送结果
     */
    @TableField("result")
    private String result;

    /**
     * 重试次数
     */
    @TableField("retry_count")
    private int retryCount;
}
