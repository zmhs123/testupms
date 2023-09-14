package cn.tech.wings.cloud.common.websocket.config;

import cn.tech.wings.cloud.common.websocket.custom.CloudSessionKeyGenerator;
import cn.tech.wings.cloud.common.websocket.custom.UserAttributeHandshakeInterceptor;
import cn.tech.wings.cloud.common.websocket.handler.CustomPlanTextMessageHandler;
import cn.tech.wings.cloud.common.websocket.handler.CustomWebSocketHandler;
import cn.tech.wings.cloud.common.websocket.handler.PingJsonMessageHandler;
import cn.tech.wings.cloud.common.websocket.handler.PlanTextMessageHandler;
import cn.tech.wings.cloud.common.websocket.holder.MapSessionWebSocketHandlerDecorator;
import cn.tech.wings.cloud.common.websocket.holder.SessionKeyGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * @author Hccake 2021/1/5
 * @version 1.0
 */
@RequiredArgsConstructor
@EnableConfigurationProperties(WebSocketProperties.class)
public class WebSocketHandlerConfig {

	private final WebSocketProperties webSocketProperties;

	@Bean
	@ConditionalOnMissingBean(SessionKeyGenerator.class)
	public SessionKeyGenerator sessionKeyGenerator() {
		return new CloudSessionKeyGenerator();
	}

	@Bean
	public HandshakeInterceptor handshakeInterceptor() {
		return new UserAttributeHandshakeInterceptor();
	}

	@Bean
	@ConditionalOnMissingBean(PlanTextMessageHandler.class)
	public PlanTextMessageHandler planTextMessageHandler() {
		return new CustomPlanTextMessageHandler();
	}

	@Bean
	@ConditionalOnMissingBean({ TextWebSocketHandler.class, PlanTextMessageHandler.class })
	public WebSocketHandler webSocketHandler1(@Autowired(required = false) SessionKeyGenerator sessionKeyGenerator) {
		CustomWebSocketHandler customWebSocketHandler = new CustomWebSocketHandler();
		if (webSocketProperties.isMapSession()) {
			return new MapSessionWebSocketHandlerDecorator(customWebSocketHandler, sessionKeyGenerator);
		}
		return customWebSocketHandler;
	}

	@Bean
	@ConditionalOnBean(PlanTextMessageHandler.class)
	@ConditionalOnMissingBean(TextWebSocketHandler.class)
	public WebSocketHandler webSocketHandler2(@Autowired(required = false) SessionKeyGenerator sessionKeyGenerator,
			PlanTextMessageHandler planTextMessageHandler) {
		CustomWebSocketHandler customWebSocketHandler = new CustomWebSocketHandler(planTextMessageHandler);
		if (webSocketProperties.isMapSession()) {
			return new MapSessionWebSocketHandlerDecorator(customWebSocketHandler, sessionKeyGenerator);
		}
		return customWebSocketHandler;
	}

	@Bean
	@ConditionalOnProperty(prefix = WebSocketProperties.PREFIX, name = "heartbeat", havingValue = "true",
			matchIfMissing = true)
	public PingJsonMessageHandler pingJsonMessageHandler() {
		return new PingJsonMessageHandler();
	}

}
