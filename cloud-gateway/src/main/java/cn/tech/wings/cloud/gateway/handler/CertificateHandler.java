package cn.tech.wings.cloud.gateway.handler;

import cn.tech.wings.cloud.common.core.util.R;
import cn.tech.wings.cloud.gateway.license.LicenseCheckModel;
import cn.tech.wings.cloud.gateway.license.LicenseVerify;
import cn.tech.wings.cloud.gateway.license.LicenseVerifyParam;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.schlichtherle.license.LicenseContent;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;


/**
 * 安装证书
 *
 * @author zm_hs
 * @date 2023/4/19 0019 上午 11:07
 * @return
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CertificateHandler implements HandlerFunction<ServerResponse> {

    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;
    private static Logger logger = LogManager.getLogger(CertificateHandler.class);

    /**
     * 证书subject
     */
    @Value("${license.subject}")
    private String subject;

    /**
     * 公钥别称
     */
    @Value("${license.publicAlias}")
    private String publicAlias;

    /**
     * 访问公钥库的密码
     */
    @Value("${license.storePass}")
    private String storePass;

    /**
     * 证书生成路径
     */
    @Value("${license.licensePath}")
    private String licensePath;

    /**
     * 密钥库存储路径
     */
    @Value("${license.publicKeysStorePath}")
    private String publicKeysStorePath;

    @Override
    @SneakyThrows
    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        logger.info("开始调用安装证书接口...");
        LicenseVerifyParam param = new LicenseVerifyParam();
        param.setSubject(subject);
        param.setPublicAlias(publicAlias);
        param.setStorePass(storePass);
        param.setLicensePath(licensePath);
        param.setPublicKeysStorePath(publicKeysStorePath);
        LicenseVerify licenseVerify = new LicenseVerify(redisTemplate);
        //安装证书
        LicenseContent result = licenseVerify.install(param);
        LicenseCheckModel m = (LicenseCheckModel) result.getExtra();
        String appCount = m.getAppCount();
        if (StringUtils.isEmpty(appCount)) {
            appCount = "∞";
        }
        logger.info("结束调用安装证书接口...");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (null == result)
            return ServerResponse.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(objectMapper.writeValueAsString(R.failed("证书安装失败！"))));
        String validTime = MessageFormat.format("证书安装成功，证书有效期：{0} - {1}，授权应用数：{2}", format.format(result.getNotBefore()), format.format(result.getNotAfter()), appCount);
        return ServerResponse.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(objectMapper.writeValueAsString(R.ok(validTime))));
    }
}
