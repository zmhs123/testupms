package cn.tech.wings.cloud.message.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author mgp 消息事件
 */
public class MessageEvent extends ApplicationEvent {
	public MessageEvent(Object source) {
		super(source);
	}
}
