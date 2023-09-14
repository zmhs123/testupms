package cn.tech.wings.cloud.message.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author Mgp 2022/02/14
 */
@Component
@Data
@RefreshScope
public class ConfigReader {

	@Value("${cloud.msg.daYuSignName}")
	private String daYuSignName;

	@Value("${cloud.msg.wxPayH5Key}")
	private String wxPayH5Key;

	@Value("${cloud.msg.wxPayH5AppId}")
	private String wxPayH5AppId;

	@Value("${cloud.msg.daYuAppSecret}")
	private String daYuAppSecret;

	@Value("${cloud.msg.daYuAppAccessId}")
	private String daYuAppAccessId;

	@Value("${cloud.msg.wxMpAppId}")
	private String wxMpAppId;



}
