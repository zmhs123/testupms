package cn.tech.wings.cloud.admin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix="accurate")
public class JdkConfig {
	/**
	 [
		 {
		 "label": "jdk8",
		 "value": "/home/soft/jdk1.8.0_181"
		 },
		 {
		 "label": "jdk11",
		 "value": "/usr/local/java/jdk-11.0.16.1"
		 }
	 ]
	 */
	private String jdkPath;
}
