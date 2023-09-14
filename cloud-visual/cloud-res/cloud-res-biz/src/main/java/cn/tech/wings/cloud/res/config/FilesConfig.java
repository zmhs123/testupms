package cn.tech.wings.cloud.res.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties("minio")
public class FilesConfig {
    /**minio的路径**/
    private String endpoint = "http://localhost:19000";

    /**minio的accessKey**/
    private String accessKey = "minioadmin";

    /**minio的secretKey**/
    private String secretKey = "minioadmin";

    /**下载地址**/
    private String httpUrl;

    /**图片大小限制**/
    private Long imgSize;

    /**文件大小限制**/
    private Long fileSize;
}
