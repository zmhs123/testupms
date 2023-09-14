package cn.tech.wings.cloud.message.event;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.tech.wings.cloud.message.core.enums.MessageTypeEnum;
import cn.tech.wings.cloud.message.core.util.MessageUtil;
import cn.tech.wings.cloud.message.entity.Message;
import cn.tech.wings.cloud.message.model.params.MessageUserParam;
import cn.tech.wings.cloud.message.model.params.SendMessageParam;
import cn.tech.wings.cloud.message.model.result.TemplateResult;
import cn.tech.wings.cloud.message.service.MessageService;
import cn.tech.wings.cloud.message.service.TemplateService;
import com.yunpian.sdk.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executor;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageSmsListener {
    @Autowired
    private TemplateService templateService;

    private final MessageService messageService;

    private final Executor executor;

    /**
     * 发送基础短信
     *
     * @Author mgp
     * @Date 2022/2/23
     **/
    @Async
    @Order
    @EventListener(MessageEvent.class)
    public void sendSmsMsg(MessageEvent event) {
        SendMessageParam param = (SendMessageParam) event.getSource();
        if (param.getMarkEnum() != null) {
            List<TemplateResult> template = templateService.getObjByMark("sms_" + param.getMarkEnum().getValue());
            if (CollectionUtil.isEmpty(template)) {
                return;
            }
            executor.execute(() -> {
                try {
                    send(param, template.get(0));
                } catch (Exception e) {
                    log.info("发送短信异常：{}", ExceptionUtil.stacktraceToString(e));
                }
            });

        }
    }

    private void send(SendMessageParam param, TemplateResult template) {
        for (MessageUserParam userParam : param.getToUsers()) {
            Message message = new Message();
            message.setType(MessageTypeEnum.SMS_MSG.getValue());
            message.setToUserId(userParam.getId());
            message.setFromUserId(param.getFromUserId());
            message.setSendModel(true);
            message.setIdent(userParam.getPhone());
            log.info("发送短信，短信模板：{}", template.getContent());
            MessageUtil.sendSmsMessage(message, messageService, template.getTemplateCode(), JsonUtil.toJson(param.getSmsMsgContent()), userParam.getPhone());
        }
    }

}
