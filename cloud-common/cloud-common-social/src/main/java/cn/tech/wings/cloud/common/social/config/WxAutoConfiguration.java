package cn.tech.wings.cloud.common.social.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceOkHttpImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaRedisBetterConfigImpl;
import cn.tech.wings.cloud.common.social.constant.WxConfigProperties;
import cn.tech.wings.cloud.common.core.util.SpringContextHolder;
import me.chanjar.weixin.common.redis.RedisTemplateWxRedisOps;
import me.chanjar.weixin.common.redis.WxRedisOps;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceOkHttpImpl;
import me.chanjar.weixin.mp.config.impl.WxMpRedisConfigImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author: ZHENGXIAOCONG
 * @Description:
 * @date 2022/3/114:20
 */
@Configuration
public class WxAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(WxMpService.class)
	public WxMpService wxMpService() {
		WxMpService wxMpService = new WxMpServiceOkHttpImpl();

		StringRedisTemplate redisTemplate = SpringContextHolder.getBean(StringRedisTemplate.class);
		WxRedisOps redisOps = new RedisTemplateWxRedisOps(redisTemplate);

		WxMpRedisConfigImpl wxMpRedisConfig = new WxMpRedisConfigImpl(redisOps, WxConfigProperties.keyPrefix);
		wxMpRedisConfig.setAppId(WxConfigProperties.MP_APP_ID);
		wxMpRedisConfig.setSecret(WxConfigProperties.MP_APP_SECRET);

		wxMpService.addConfigStorage(wxMpRedisConfig.getAppId(), wxMpRedisConfig);
		return wxMpService;
	}

	@Bean
	@ConditionalOnMissingBean(WxMaService.class)
	public WxMaService wxMaService() {
		WxMaService wxMaService = new WxMaServiceOkHttpImpl();

		StringRedisTemplate redisTemplate = SpringContextHolder.getBean(StringRedisTemplate.class);
		WxRedisOps redisOps = new RedisTemplateWxRedisOps(redisTemplate);

		//售电端小程序
		WxMaRedisBetterConfigImpl wxMaRedisBetterConfig1 = new WxMaRedisBetterConfigImpl(redisOps, WxConfigProperties.keyPrefix);
		wxMaRedisBetterConfig1.setAppid(WxConfigProperties.ELECTRIC_SELL_MINI_APP_ID);
		wxMaRedisBetterConfig1.setSecret(WxConfigProperties.ELECTRIC_SELL_MINI_APP_SECRET);

		//用电端小程序
		WxMaRedisBetterConfigImpl wxMaRedisBetterConfig2 = new WxMaRedisBetterConfigImpl(redisOps, WxConfigProperties.keyPrefix);
		wxMaRedisBetterConfig2.setAppid(WxConfigProperties.ELECTRIC_USE_MINI_APP_ID);
		wxMaRedisBetterConfig2.setSecret(WxConfigProperties.ELECTRIC_USE_MINI_APP_SECRET);

		//经纪人端小程序
		WxMaRedisBetterConfigImpl wxMaRedisBetterConfig3 = new WxMaRedisBetterConfigImpl(redisOps, WxConfigProperties.keyPrefix);
		wxMaRedisBetterConfig3.setAppid(WxConfigProperties.ELECTRIC_AGENT_MINI_APP_ID);
		wxMaRedisBetterConfig3.setSecret(WxConfigProperties.ELECTRIC_AGENT_MINI_APP_SECRET);

		//测试
		WxMaRedisBetterConfigImpl wxMaRedisBetterConfig4 = new WxMaRedisBetterConfigImpl(redisOps, WxConfigProperties.keyPrefix);
		wxMaRedisBetterConfig4.setAppid(WxConfigProperties.TEST_USE_MINI_APP_ID);
		wxMaRedisBetterConfig4.setSecret(WxConfigProperties.TEST_USE_MINI_APP_SECRET);


		wxMaService.addConfig(wxMaRedisBetterConfig1.getAppid(), wxMaRedisBetterConfig1);
		wxMaService.addConfig(wxMaRedisBetterConfig2.getAppid(), wxMaRedisBetterConfig2);
		wxMaService.addConfig(wxMaRedisBetterConfig3.getAppid(), wxMaRedisBetterConfig3);
		wxMaService.addConfig(wxMaRedisBetterConfig4.getAppid(), wxMaRedisBetterConfig4);
		return wxMaService;
	}
}
