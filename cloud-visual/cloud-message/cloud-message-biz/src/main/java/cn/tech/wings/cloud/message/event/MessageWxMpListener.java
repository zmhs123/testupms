package cn.tech.wings.cloud.message.event;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import cn.tech.wings.cloud.message.core.enums.MessageTypeEnum;
import cn.tech.wings.cloud.message.core.util.MessageUtil;
import cn.tech.wings.cloud.message.entity.Message;
import cn.tech.wings.cloud.message.model.params.MessageUserParam;
import cn.tech.wings.cloud.message.model.params.SendMessageParam;
import cn.tech.wings.cloud.message.model.params.SendMsgUserWxParam;
import cn.tech.wings.cloud.message.model.result.TemplateResult;
import cn.tech.wings.cloud.message.service.MessageService;
import cn.tech.wings.cloud.message.service.TemplateService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 微信公众号监听器
 * @Author mgp
 * @Date 2022/6/24
 **/
@Slf4j
@Component
public class MessageWxMpListener {

	@Autowired
	private TemplateService templateService;
	@Autowired
	private WxMpService wxMpService;
	@Autowired
	private MessageService messageService;
	/**
	 * 发送公众号消息
	 * @Author 贺陆伟
	 * @Date 2022-04-27
	 **/
	@Async
	@Order
	@EventListener(MessageEvent.class)
	public void sendWxMpMsg(MessageEvent event) {
		SendMessageParam param = (SendMessageParam) event.getSource();
		if(param.getMarkEnum() != null){
			List<TemplateResult> template = templateService.getObjByMark("wx_mp_"+param.getMarkEnum().getValue());
			if(CollectionUtil.isEmpty(template)) {return;}
			send(param,template);
		}
	}

	private void send(SendMessageParam param, List<TemplateResult> templates) {
		log.info("进入发送公众号监听器参数：" + JSONUtil.toJsonStr(param));
		Map<String, Object> msgContent = param.getWxMsgContent();
		List<MessageUserParam> toUsers = param.getToUsers();
		for (MessageUserParam userParam : toUsers) {
			List<SendMsgUserWxParam> wxParams = userParam.getWxParams();
			if (CollectionUtil.isEmpty(wxParams)) {return;}
			//根据appId配置
			for (SendMsgUserWxParam wxParam : wxParams) {
				if(StringUtils.isBlank(wxParam.getAppId()) || StringUtils.isBlank(wxParam.getOpenId())){
					log.error("公众号消息发送失败：用户id("+userParam.getId()+")appid或openid为空");
					continue;
				}
				Optional<TemplateResult> first = templates.stream().filter(e -> e.getAppId().equals(wxParam.getAppId())).findFirst();
				if(!first.isPresent()) {continue;}
				TemplateResult template = first.get();
				Message message = new Message();
				message.setType(MessageTypeEnum.WX_MP_MSG.getValue());
				message.setToUserId(userParam.getId());
				message.setFromUserId(param.getFromUserId());
				message.setSendModel(true);
				message.setIdent(wxParam.getOpenId());
				MessageUtil.sendWxMp(message,messageService,wxMpService, wxParam.getAppId(), wxParam.getOpenId(), msgContent, template);
			}
		}
	}

}
