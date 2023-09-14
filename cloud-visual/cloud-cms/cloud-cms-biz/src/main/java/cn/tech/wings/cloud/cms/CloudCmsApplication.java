package cn.tech.wings.cloud.cms;

import cn.tech.wings.cloud.common.feign.annotation.EnableCloudFeignClients;
import cn.tech.wings.cloud.common.job.annotation.EnableCloudXxlJob;
import cn.tech.wings.cloud.common.security.annotation.EnableCloudResourceServer;
import cn.tech.wings.cloud.common.swagger.annotation.EnableCloudSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author cloud
 * @date 2018年06月21日
 * <p>
 * 素材服务
 */
@EnableCloudSwagger2
@EnableCloudFeignClients
@EnableCloudResourceServer
@EnableDiscoveryClient
@SpringBootApplication
@EnableCloudXxlJob
public class CloudCmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudCmsApplication.class, args);
	}

}
