package cn.tech.wings.cloud.message;

import cn.tech.wings.cloud.common.feign.annotation.EnableCloudFeignClients;
import cn.tech.wings.cloud.common.job.annotation.EnableCloudXxlJob;
import cn.tech.wings.cloud.common.security.annotation.EnableCloudResourceServer;
import cn.tech.wings.cloud.common.swagger.annotation.EnableCloudSwagger2;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/**
 * 消息基础服务
 *
 * @author mgp
 * @Date 2022/05/16 21:39
 */
@EnableCloudSwagger2
@EnableCloudFeignClients
@EnableCloudResourceServer
@EnableDiscoveryClient
@SpringBootApplication
@EnableCloudXxlJob
public class CloudMessageApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(CloudMessageApplication.class).run(args);
    }
}


