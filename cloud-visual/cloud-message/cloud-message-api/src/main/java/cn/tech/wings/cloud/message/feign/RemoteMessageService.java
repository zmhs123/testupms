/*
 *
 *      Copyright (c) 2018-2025, cloud All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the pig4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: cloud
 *
 */

package cn.tech.wings.cloud.message.feign;

import cn.tech.wings.cloud.common.core.constant.SecurityConstants;
import cn.tech.wings.cloud.common.core.constant.ServiceNameConstants;
import cn.tech.wings.cloud.common.core.util.R;
import cn.tech.wings.cloud.message.model.params.SendEasyMessageParam;
import cn.tech.wings.cloud.message.model.params.SendMessageParam;
import cn.tech.wings.cloud.message.model.params.SendVerifyCodeParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * @author mgp
 * @date 2022/02/20
 */
@FeignClient(contextId = "remoteMessageService", value = ServiceNameConstants.CLOUD_MSG)
public interface RemoteMessageService {

    /**
     * 发送短信验证码
     *
     * @param param
     * @return R
     **/
    @PostMapping("/verify-code/send")
    R sendVerifyCode(SendVerifyCodeParam param, @RequestHeader(SecurityConstants.FROM) String from);

    /**
     * 消息发送(通过手机号，微信参数，其他参数发送)
     *
     * @param sendMessageParam
     * @return R
     **/
    @PostMapping("/message/send")
    R sendBase(SendMessageParam sendMessageParam, @RequestHeader(SecurityConstants.FROM) String from);


    /**
     * 消息发送（通过用户id发送）
     *
     * @param sendEasyMessageParam
     * @return R
     **/
    @PostMapping("/message/send-easy")
    R sendBaseEasy(SendEasyMessageParam sendEasyMessageParam);

    /**
     * 消息发送（批量发送）
     *
     * @param sendEasyMessageParams
     * @return R
     **/
    @PostMapping("/message/send-easy-batch")
    R sendBaseEasyBatch(@RequestBody List<SendEasyMessageParam> sendEasyMessageParams);

    /**
     * 消息发送（批量发送）
     *
     * @param sendMessageParams
     * @return R
     **/
    @PostMapping("/message/send-base-batch")
    R sendBaseBatch(@RequestBody List<SendMessageParam> sendMessageParams);


}
