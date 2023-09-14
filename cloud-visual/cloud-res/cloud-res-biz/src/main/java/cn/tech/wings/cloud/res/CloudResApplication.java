package cn.tech.wings.cloud.res;

import cn.tech.wings.cloud.common.feign.annotation.EnableCloudFeignClients;
import cn.tech.wings.cloud.common.security.annotation.EnableCloudResourceServer;
import cn.tech.wings.cloud.common.swagger.annotation.EnableCloudSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author etap
 * <p>
 * 项目启动类
 */
@EnableCloudSwagger2
@EnableCloudFeignClients
@EnableDiscoveryClient
@EnableCloudResourceServer
@SpringBootApplication
public class CloudResApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudResApplication.class, args);
    }
}
