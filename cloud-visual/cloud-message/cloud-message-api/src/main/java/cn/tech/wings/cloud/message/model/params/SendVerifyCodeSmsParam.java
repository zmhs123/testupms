package cn.tech.wings.cloud.message.model.params;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 发送消息Dto
 * @ClassName: MessageDto
 * @Author: mgp
 * @Date: 2022/2/23 15:21
 */
@Data
public class SendVerifyCodeSmsParam {

	@ApiModelProperty("手机号")
	@NotNull(message = "手机号不能为空")
	private String phone;

	@ApiModelProperty("使用类型(用户登录/注册:")
	@NotNull(message = "使用类型不能为空")
	private String useType;

	@ApiModelProperty("验证码")
	@NotNull(message = "验证码不能为空")
	private String code;

}
