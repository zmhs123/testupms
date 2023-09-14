package cn.tech.wings.cloud.message.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author mgp 消息事件
 */
public class VerifyCodeEvent extends ApplicationEvent {
	public VerifyCodeEvent(Object source) {
		super(source);
	}
}
