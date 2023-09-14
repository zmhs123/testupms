package cn.tech.wings.cloud.common.websocket.config;

import cn.tech.wings.cloud.common.websocket.distribute.LocalMessageDistributor;
import cn.tech.wings.cloud.common.websocket.distribute.MessageDistributor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 本地的消息分发器配置
 *
 * @author hccake
 */
@ConditionalOnProperty(prefix = WebSocketProperties.PREFIX, name = "message-distributor",
		havingValue = MessageDistributorTypeConstants.LOCAL)
@Configuration(proxyBeanMethods = false)
public class LocalMessageDistributorConfiguration {

	/**
	 * 本地基于内存的消息分发，不支持集群
	 * @return LocalMessageDistributor
	 */
	@Bean
	@ConditionalOnMissingBean(MessageDistributor.class)
	public LocalMessageDistributor messageDistributor() {
		return new LocalMessageDistributor();
	}

}
