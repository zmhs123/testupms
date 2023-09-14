package cn.tech.wings.cloud.message.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import cn.tech.wings.cloud.common.core.util.R;
import cn.tech.wings.cloud.common.core.util.SpringContextHolder;
import cn.tech.wings.cloud.common.security.annotation.Inner;
import cn.tech.wings.cloud.message.event.MessageEvent;
import cn.tech.wings.cloud.message.model.params.SendMessageParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 消息表控制器
 *
 * @author mgp
 * @Date 2019-11-16 11:08:17
 */
@Api(value="消息管理",tags="消息操作接口")
@RestController
@RequestMapping("/message")
@AllArgsConstructor
@Slf4j
public class MessageController {


	/**
	 * 发送APP推送/WX消息/普通消息/系统消息/站内信接口
	 * 有用户参数
	 * @author mgp
	 * @Date 2022-02-23
	 */
	@ApiOperation("消息发送")
	@PostMapping("/send")
	@Inner(value = false)
	public R sendBase(@RequestBody SendMessageParam messageParam) {
		SpringContextHolder.publishEvent(new MessageEvent(messageParam));
		return R.ok();
	}



	/**
	 * 发送APP推送/WX消息/普通消息/系统消息/站内信接口
	 * 批量发送（有用户参数）
	 * @author mgp
	 * @Date 2022-02-23
	 */
	@ApiOperation("批量发送（有用户参数）")
	@PostMapping("/send-base-batch")
	@Inner(value = false)
	public R sendBaseBatch(@RequestBody List<SendMessageParam> sendMessageParams) {
		//获取用户信息
		log.info("进入send-base-batch<<<" + JSONUtil.toJsonStr(sendMessageParams));
		if (CollectionUtil.isEmpty(sendMessageParams)) {
			return R.failed("发送失败,未接收到发送参数");
		}
		for (SendMessageParam sendMessageParam : sendMessageParams) {
			try {
				R r = this.sendBase(sendMessageParam);
				if(r.getCode() != 0) {
					log.error("批量发送失败:参数<<<"+JSONUtil.toJsonStr(sendMessageParam)+">>>失败原因："+r.getMsg());
				}
			} catch (Exception e) {
				log.error("批量发送异常:参数<<<"+JSONUtil.toJsonStr(sendMessageParam)+">>>异常："+e.getMessage());
			}
		}
		return R.ok();
	}
}


