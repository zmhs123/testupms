package cn.tech.wings.cloud.message.dto;

import lombok.Data;

/**
 * <p>
 * 短信服务表
 * </p>
 *
 * @author 庞凤楚
 * @since 2019-11-18
 */
@Data
public class VerifyCodeData{

    /**
     * email|mobile
     */
    private String receiverType;

    /**
     * 验证码接收账号（邮箱或手机号）
     */
    private String receiver;

    /**
     * 用途 用户注册 validation_phone 修改密码  change_pwd 修改绑定手机号 change_phone 修改支付密码  change_pay_pwd
     */
    private String useType;

    /**
     * 验证码
     */
    private String code;

}
