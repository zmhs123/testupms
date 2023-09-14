package cn.tech.wings.cloud.message.model.params;

import cn.tech.wings.cloud.message.core.enums.MessageMarkEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 发送消息Dto
 * @ClassName: MessageDto
 * @Author: mgp
 * @Date: 2022/2/23 15:21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageParam {

	@ApiModelProperty(value = "模板标识",required = true)
	private MessageMarkEnum markEnum;

	@ApiModelProperty(value = "消息接收人信息",required = true)
	private List<MessageUserParam> toUsers;

	@ApiModelProperty(value = "短信消息内容Map对象")
	private Map<String,Object> smsMsgContent;

	@ApiModelProperty(value = "wx消息内容Map对象")
	private Map<String,Object> wxMsgContent;

	@ApiModelProperty(value = "APP消息内容Map对象")
	private Map<String,Object> appMsgContent;

	@ApiModelProperty(value = "消息发送人id(尽量传)")
	private Long fromUserId;
}
