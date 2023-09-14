package cn.tech.wings.cloud.message.event;

import cn.tech.wings.cloud.message.model.params.SendMessageParam;
import cn.tech.wings.cloud.message.model.result.TemplateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * app监听器
 * @Author mgp
 * @Date 2022/6/24
 **/
@Slf4j
@Component
public class MessageAppListener {

    /**
     * 发送App消息
     *
     * @Author mgp
     * @Date 2022/2/23
     **/
    @Async
    @Order
    @EventListener(MessageEvent.class)
    public void sendAppMsg(MessageEvent event) {


    }

    private void send(SendMessageParam param, TemplateResult template) {

    }

}
