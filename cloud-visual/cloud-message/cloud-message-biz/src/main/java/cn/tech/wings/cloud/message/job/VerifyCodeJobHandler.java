package cn.tech.wings.cloud.message.job;

import cn.tech.wings.cloud.message.service.VerifyCodeService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 验证码删除定时器
 * @Author: mgp
 * @Date: 2022/2/25 15:23
 */
@Slf4j
@Component
public class VerifyCodeJobHandler {

	@Autowired
	private VerifyCodeService verifyCodeService;

	@XxlJob("clearVerifyCodeJob")
	public void clearVerifyCodeJob(){
		verifyCodeService.clearVerifyCode();
	}





}
