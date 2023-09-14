package cn.tech.wings.cloud.gateway.license;

import cn.tech.wings.cloud.common.core.constant.CommonConstants;
import de.schlichtherle.license.*;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import java.io.File;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.prefs.Preferences;

/**
 * @author Administrator
 * @version 1.0.0
 * @ProjectName LicenseVerify
 * @Description License校验类
 * @createTime 2022/4/30 0030 18:28
 */
@RequiredArgsConstructor
public class LicenseVerify {
    private static Logger logger = LogManager.getLogger(LicenseVerify.class);
    private final RedisTemplate redisTemplate;

    /**
     * @title install
     * @description 安装License证书
     * @author Administrator
     * @updateTime 2022/4/30 0030 18:29
     */
    public synchronized LicenseContent install(LicenseVerifyParam param) {
        LicenseContent result = null;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //1. 安装证书
        try {
            LicenseManager licenseManager = LicenseManagerHolder.getInstance(initLicenseParam(param));
            licenseManager.uninstall();
            if (null != redisTemplate) {
                if (redisTemplate.hasKey(CommonConstants.CERTIFICATE_VALID_TIME)) {
                    redisTemplate.delete(CommonConstants.CERTIFICATE_VALID_TIME);
                }
            }
            result = licenseManager.install(new File(param.getLicensePath()));
            LicenseCheckModel m = (LicenseCheckModel) result.getExtra();
            String appCount = m.getAppCount();
            if (StringUtils.isEmpty(appCount)) {
                appCount = "∞";
            }
            String validTime = MessageFormat.format("证书有效期：{0} - {1}，授权应用数：{2}", format.format(result.getNotBefore()), format.format(result.getNotAfter()), appCount);
            if (null != redisTemplate) {
                redisTemplate.opsForValue().set(CommonConstants.CERTIFICATE_VALID_TIME, validTime);
            }
            logger.info("证书安装成功，" + validTime);
        } catch (Exception e) {
            logger.error("证书安装失败！", e);
        }

        return result;
    }

    /**
     * @title verify
     * @description 校验License证书
     * @author Administrator
     * @updateTime 2022/4/30 0030 18:29
     */
    public static LicenseContent verify() throws Exception {
        LicenseManager licenseManager = LicenseManagerHolder.getInstance(null);
        //2. 校验证书
        return licenseManager.verify();
    }

    /**
     * @title initLicenseParam
     * @description 初始化证书生成参数
     * @author Administrator
     * @updateTime 2022/4/30 0030 18:29
     */
    private LicenseParam initLicenseParam(LicenseVerifyParam param) {
        Preferences preferences = Preferences.userNodeForPackage(LicenseVerify.class);

        CipherParam cipherParam = new DefaultCipherParam(param.getStorePass());

        KeyStoreParam publicStoreParam = new CustomKeyStoreParam(LicenseVerify.class
                , param.getPublicKeysStorePath()
                , param.getPublicAlias()
                , param.getStorePass()
                , null);

        return new DefaultLicenseParam(param.getSubject()
                , preferences
                , publicStoreParam
                , cipherParam);
    }

}
