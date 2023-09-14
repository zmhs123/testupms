package cn.tech.wings.cloud.message.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 短信服务表
 * </p>
 *
 * @author 庞凤楚
 * @since 2019-11-18
 */
@Data
public class VerifyCodeMessageData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;


    /**
     * email|mobile
     */
    private String receiverType;

    /**
     * 验证码接收账号（邮箱或手机号）
     */
    private String receiver;

    /**
     * 用途 用户登录/注册 validation_phone
     */
    private String useType;

    /**
     * 状态:0:未使用1:已使用
     */
    private Boolean useStatus;

    /**
     * 验证码
     */
    private String code;

    /**
     * 发送状态 0：未发送    其他：已发送
     */
    private Integer sendStatus;

    /**
     * 短信内容参数
     */
    private String content;

    /**
     * 模板id
     */
    private String messageId;

    /**
     * 调用模板
     */
    private String mark;


}
