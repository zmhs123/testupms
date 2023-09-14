package cn.tech.wings.cloud.common.security.util;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

/**
 * @author cloud
 * @date 2020/9/4
 * <p>
 * @see org.springframework.security.core.SpringSecurityMessageSource cloud 框架自身异常处理，
 * 建议所有异常都使用此工具类型 避免无法复写 SpringSecurityMessageSource
 */
public class CloudSecurityMessageSourceUtil extends ReloadableResourceBundleMessageSource {

	// ~ Constructors
	// ===================================================================================================

	public CloudSecurityMessageSourceUtil() {
		setBasename("classpath:messages/messages");
		setDefaultLocale(Locale.CHINA);
	}

	// ~ Methods
	// ========================================================================================================

	public static MessageSourceAccessor getAccessor() {
		return new MessageSourceAccessor(new CloudSecurityMessageSourceUtil());
	}

}