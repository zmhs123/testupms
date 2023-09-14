package cn.tech.wings.cloud.common.idempotent;

import cn.tech.wings.cloud.common.idempotent.aspect.IdempotentAspect;
import cn.tech.wings.cloud.common.idempotent.expression.ExpressionResolver;
import cn.tech.wings.cloud.common.idempotent.expression.KeyResolver;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author cloud
 * @date 2020/9/25
 * <p>
 * 幂等插件初始化
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class IdempotentAutoConfiguration {

	/**
	 * 切面 拦截处理所有 @Idempotent
	 * @return Aspect
	 */
	@Bean
	public IdempotentAspect idempotentAspect() {
		return new IdempotentAspect();
	}

	/**
	 * key 解析器
	 * @return KeyResolver
	 */
	@Bean
	@ConditionalOnMissingBean(KeyResolver.class)
	public KeyResolver keyResolver() {
		return new ExpressionResolver();
	}

}
