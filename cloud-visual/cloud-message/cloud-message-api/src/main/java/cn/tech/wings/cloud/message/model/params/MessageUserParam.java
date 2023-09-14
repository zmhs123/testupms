package cn.tech.wings.cloud.message.model.params;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 发送消息需要的用户信息
 * @ClassName: MessageDto
 * @Author: mgp
 * @Date: 2022/2/23 15:21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageUserParam {

	@ApiModelProperty(value = "接收人id（可不传）")
	private Long Id;

	@ApiModelProperty(value = "接收人手机号(发短信必传)")
	private String phone;

	@ApiModelProperty(value = "微信参数（发微信小程序，公众号消息必传）")
	private List<SendMsgUserWxParam> wxParams;

}
