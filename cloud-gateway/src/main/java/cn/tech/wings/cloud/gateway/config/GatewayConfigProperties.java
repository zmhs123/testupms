package cn.tech.wings.cloud.gateway.config;

import cn.tech.wings.cloud.gateway.filter.PasswordDecoderFilter;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author cloud
 * @date 2020/10/2
 * <p>
 * 网关通用配置文件
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties("gateway")
public class GatewayConfigProperties {

	/**
	 * 网关解密登录前端密码 秘钥 {@link PasswordDecoderFilter}
	 */
	public String encodeKey;

}
