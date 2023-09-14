package cn.tech.wings.cloud.common.data.tenant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 多租户配置
 *
 * @author oathsign
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "cloud.tenant")
public class CloudTenantConfigProperties {

	/**
	 * 维护租户列名称
	 */
	private String column = "tenant_id";

	/**
	 * 多租户的数据表集合
	 */
	private List<String> tables = new ArrayList<>();

}
