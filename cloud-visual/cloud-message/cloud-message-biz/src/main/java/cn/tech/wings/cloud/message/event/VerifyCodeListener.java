package cn.tech.wings.cloud.message.event;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.tech.wings.cloud.message.core.util.MessageUtil;
import cn.tech.wings.cloud.message.dto.VerifyCodeMessageData;
import cn.tech.wings.cloud.message.entity.VerifyCode;
import cn.tech.wings.cloud.message.model.result.TemplateResult;
import cn.tech.wings.cloud.message.service.TemplateService;
import cn.tech.wings.cloud.message.service.VerifyCodeService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 验证码监听器
 * @Author mgp
 * @Date 2022/6/24
 **/
@Slf4j
@Component
public class VerifyCodeListener {

	@Autowired
	private TemplateService templateService;
	@Autowired
	private VerifyCodeService verifyCodeService;


	/**
	 * 发送短信验证码
	 * @Author mgp
	 * @Date 2022/2/23
	 **/
	@Async
	@Order
	@EventListener(VerifyCodeEvent.class)
	public void sendCodeMsg(VerifyCodeEvent event) {
		VerifyCode verifyCode = new VerifyCode();
		VerifyCodeMessageData source = (VerifyCodeMessageData) event.getSource();
		BeanUtil.copyProperties(source,verifyCode);
		List<TemplateResult> template = templateService.getObjByMark("sms_"+source.getMark());
		if(CollectionUtil.isEmpty(template)) {
			log.error("消息发送失败，模板不存在");
			return;
		}
		if(StringUtils.isBlank(source.getReceiver())) {
			log.error("消息发送失败，手机号为空");
			return;
		}
		MessageUtil.sendVerifyCodeMessage(verifyCode,verifyCodeService,template.get(0).getTemplateCode());
	}

}
