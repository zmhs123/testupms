package cn.tech.wings.cloud.common.security.component;

import cn.tech.wings.cloud.common.core.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;

/**
 * @author cloud
 * @date 2021/10/3
 */
@Slf4j
@EnableScheduling
@ConditionalOnBean(AuthorizationServerConfigurerAdapter.class)
public class CloudTokenStoreCleanSchedule {

	@Scheduled(cron = "@hourly")
	public void doMaintenance() {
		CloudRedisTokenStore tokenStore = SpringContextHolder.getBean(CloudRedisTokenStore.class);
		long maintenance = tokenStore.doMaintenance();
		log.debug("清理Redis ZADD 过期 token 数量: {}", maintenance);
	}

}
