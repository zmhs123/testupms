package cn.tech.wings.cloud.message.controller;

import cn.tech.wings.cloud.common.core.util.R;
import cn.tech.wings.cloud.common.core.util.SpringContextHolder;
import cn.tech.wings.cloud.common.security.annotation.Inner;
import cn.tech.wings.cloud.message.core.enums.MsgUseEnum;
import cn.tech.wings.cloud.message.core.enums.ReceiverTypeEnum;
import cn.tech.wings.cloud.message.dto.VerifyCodeMessageData;
import cn.tech.wings.cloud.message.event.VerifyCodeEvent;
import cn.tech.wings.cloud.message.model.params.SendVerifyCodeParam;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


/**
 * 短信服务表控制器
 *
 * @author 庞凤楚
 * @Date 2019-11-16 11:08:17
 */
@Api(value = "短信服务表管理", tags = {"短信服务表操作接口"})
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/verify-code")
public class VerifyCodeController {

    /**
     * 获取短信验证码
     *
     * @return
     */
    @ApiOperation("获取短信验证码")
    @PostMapping("/send")
    @Inner(value = false)
    public R getMsgCode(@Valid @RequestBody SendVerifyCodeParam param) {
        log.info("短信验证码请求：param:{}", JSON.toJSONString(param));
        //包装发送消息的信息
        VerifyCodeMessageData verifyCodeMessageData = new VerifyCodeMessageData();
        verifyCodeMessageData.setReceiverType(ReceiverTypeEnum.MOBILE.getValue());
        verifyCodeMessageData.setReceiver(param.getPhone());
        verifyCodeMessageData.setUseType(param.getUseType());
        verifyCodeMessageData.setCode(param.getCode());
        if (param.getUseType().equals(MsgUseEnum.VALIDATION_PHONE.getValue())) {
            //登陆/注册
            verifyCodeMessageData.setMark(MsgUseEnum.VALIDATION_PHONE.getValue());
        } else if (param.getUseType().equals(MsgUseEnum.REPLACE_MOBILE.getValue())) {
            //更改手机号
            verifyCodeMessageData.setMark(MsgUseEnum.REPLACE_MOBILE.getValue());
        } else if (param.getUseType().equals(MsgUseEnum.CURRENCY_CODE.getValue())) {
            //业务通用验证码
            verifyCodeMessageData.setMark(MsgUseEnum.CURRENCY_CODE.getValue());
        } else {
            return R.failed("使用类型未开通");
        }
        SpringContextHolder.publishEvent(new VerifyCodeEvent(verifyCodeMessageData));
        return R.ok("验证码已发送,请耐心等待!");
    }

}


