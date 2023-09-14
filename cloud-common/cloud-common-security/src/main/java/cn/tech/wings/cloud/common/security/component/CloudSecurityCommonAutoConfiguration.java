package cn.tech.wings.cloud.common.security.component;

import cn.hutool.core.util.StrUtil;
import cn.tech.wings.cloud.common.core.constant.SecurityConstants;
import cn.tech.wings.cloud.common.core.util.KeyStrResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author cloud
 * @date 2022-04-28
 * <p>
 * 资源服务器 认证服务器 公用配置
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class CloudSecurityCommonAutoConfiguration {

	private final KeyStrResolver resolver;

	private final RedisConnectionFactory connectionFactory;

	@Bean
	public TokenStore tokenStore() {
		CloudRedisTokenStore tokenStore = new CloudRedisTokenStore(connectionFactory, resolver);
		tokenStore.setPrefix(SecurityConstants.PIGX_PREFIX + SecurityConstants.OAUTH_PREFIX);
		tokenStore.setAuthenticationKeyGenerator(new DefaultAuthenticationKeyGenerator() {
			@Override
			public String extractKey(OAuth2Authentication authentication) {
				// 增加租户隔离部分 租户ID:原生计算值
				return resolver.extract(super.extractKey(authentication), StrUtil.COLON);
			}
		});
		return tokenStore;
	}

	/**
	 * 认证状态检查
	 * @return UserDetailsChecker
	 */
	@Bean
	public UserDetailsChecker preAuthenticationChecks() {
		return new CloudPreAuthenticationChecks();
	}

}
