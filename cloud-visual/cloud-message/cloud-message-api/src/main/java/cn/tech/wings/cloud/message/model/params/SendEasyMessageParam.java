package cn.tech.wings.cloud.message.model.params;

import cn.tech.wings.cloud.message.core.enums.MessageMarkEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 发送消息Dto
 * @ClassName: MessageDto
 * @Author: mgp
 * @Date: 2022/2/23 15:21
 */
@Data
public class SendEasyMessageParam {

	@ApiModelProperty(value = "模板标识",required = true)
	private MessageMarkEnum markEnum;

	@ApiModelProperty("消息接收人信息")
	private List<Long> userIds;

	@ApiModelProperty(value = "短信消息内容Map对象")
	private Map<String,Object> smsMsgContent;

	@ApiModelProperty(value = "wx消息内容Map对象")
	private Map<String,Object> wxMsgContent;

	@ApiModelProperty(value = "APP消息内容Map对象")
	private Map<String,Object> appMsgContent;

	@ApiModelProperty(value = "消息发送人id(尽量传)")
	private Long fromUserId;
}
