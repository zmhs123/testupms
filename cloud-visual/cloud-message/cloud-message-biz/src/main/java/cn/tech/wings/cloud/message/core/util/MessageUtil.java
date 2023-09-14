package cn.tech.wings.cloud.message.core.util;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import cn.tech.wings.cloud.common.core.util.SpringContextHolder;
import cn.tech.wings.cloud.message.config.ConfigReader;
import cn.tech.wings.cloud.message.entity.Message;
import cn.tech.wings.cloud.message.entity.VerifyCode;
import cn.tech.wings.cloud.message.model.result.TemplateResult;
import cn.tech.wings.cloud.message.service.MessageService;
import cn.tech.wings.cloud.message.service.VerifyCodeService;
import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName MessageUtil
 * @Description:
 * @Author pfc
 * @Date 2019/11/18 14:04
 * @Version V1.0
 **/
@Slf4j
@AllArgsConstructor
public class MessageUtil {
    public final static String send_temp_msg_url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

    /**
     * 阿里大鱼product
     */
    static final String product = "Dysmsapi";
    /**
     * 产品域名,开发者无需替换
     */
    static final String domain = "dysmsapi.aliyuncs.com";

    private final ConfigReader configReader;

    /**
     * 发送普通短信通知
     *
     * @return
     */
    public static void sendSmsMessage(Message message, MessageService messageService, String MessageId, String contentJson, String phone) {
        ConfigReader configReader = SpringContextHolder.getBean(ConfigReader.class);
        try {
            //待发送
            message.setContent(contentJson);
            //发送
            log.info("发送短信内容" + contentJson);
            message.setSendTime(System.currentTimeMillis());
            SendSmsResponse response = sendSms(MessageId, configReader.getDaYuSignName(), contentJson,phone);
            log.info("发送短信结果：result:{}", JSON.toJSONString(response));
            if (response.getCode() != null && "OK".equals(response.getCode())) {
                //直接发送
            } else {
                //发送失败,记录日志,并保存,方便重试
                log.info("发送短信通知失败,响应结果如下:" + JSONUtil.toJsonStr(response));
            }
            //保存响应结果
            message.setResultContent(JSONUtil.toJsonStr(response));
            messageService.save(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送验证码短信
     *
     * @param verifyCode
     * @param verifyCodeService
     * @param MessageId
     * @return
     */
    public static VerifyCode sendVerifyCodeMessage(VerifyCode verifyCode, VerifyCodeService verifyCodeService, String MessageId) {
        ConfigReader configReader = SpringContextHolder.getBean(ConfigReader.class);
        String codeJson = "{ \"code\":" + "'" + verifyCode.getCode() + "'" +"}";
        try {
            //发送
            SendSmsResponse response = sendSms(MessageId, configReader.getDaYuSignName(), codeJson, verifyCode.getReceiver());
            verifyCode.setContent(codeJson);
            verifyCode.setMessageId(MessageId);
            if (response.getCode() != null && "OK".equals(response.getCode())) {
                //直接发送
                verifyCode.setSendStatus(1);
            } else {
                verifyCode.setSendStatus(0);
                log.info("发送短信通知失败,响应结果如下:" + JSONUtil.toJsonStr(response));
            }
            saveVerify(verifyCode, verifyCodeService);
            return verifyCode;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 保存消息
     */
    private static void saveVerify(VerifyCode verifyCode, VerifyCodeService verifyCodeService) {
        verifyCode.setId(null);
        verifyCode.setAddTime(new Date());
        verifyCode.setDeleteStatus(false);
        verifyCode.setUseStatus(false);
        verifyCode.setCode(verifyCode.getCode());
        verifyCodeService.save(verifyCode);
    }


    /**
     * 发送短信核心方法,调用阿里大鱼
     *
     * @param templateCode
     * @param signName
     * @param codeJson
     * @param mobile
     * @return
     * @throws Exception
     */
    public static SendSmsResponse sendSms(String templateCode, String signName, String codeJson, String mobile) throws Exception {
        ConfigReader configReader = SpringContextHolder.getBean(ConfigReader.class);
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", configReader.getDaYuAppAccessId(), configReader.getDaYuAppSecret());
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(mobile);
        request.setSignName(signName);
        request.setTemplateCode(templateCode);
        request.setTemplateParam(codeJson);
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        return sendSmsResponse;
    }

    /**
     * 发送微信小程序消息
     *
     * @Author mgp
     * @Date 2022/5/30
     **/
    public static void sendWxMa(Message message,MessageService messageService,WxMaService wxMaService, String appId, String openId, Map<String, Object> msgContent, TemplateResult template) {
        wxMaService.switchover(appId);
        //发送数据格式
        List<WxMaSubscribeMessage.MsgData> list = new ArrayList<>();
        String urlParam = "";
        //处理数据格式
        for (Iterator<Map.Entry<String, Object>> its = msgContent.entrySet().iterator(); its.hasNext(); ) {
            Map.Entry<String, Object> entry = its.next();
            if ("urlParam".equals(entry.getKey())) {//跳转链接用的参数
                urlParam = String.valueOf(entry.getValue());
                continue;
            }
            WxMaSubscribeMessage.MsgData wxMaTemplateData = new WxMaSubscribeMessage.MsgData();
            wxMaTemplateData.setName(entry.getKey());
            wxMaTemplateData.setValue(String.valueOf(entry.getValue()));
            list.add(wxMaTemplateData);
        }
        String pageUrl = template.getUrl() + urlParam;
        message.setContent(JSONUtil.toJsonStr(msgContent));
        message.setSendTime(System.currentTimeMillis());
        //发送模板消息
        try {
            wxMaService.getMsgService().sendSubscribeMsg(
                    WxMaSubscribeMessage.builder()
                            .templateId(template.getTemplateCode())
                            .toUser(openId)
                            .miniprogramState("trial")
                            .data(list)
                            .page(pageUrl)
                            .build()
            );
        } catch (WxErrorException e) {
            message.setResultContent(JSONUtil.toJsonStr(e));
            log.error("微信公众号模板消息推送失败，接收用户openid: {}", openId, e);
        }
        messageService.save(message);
    }


    /**
     * 发送微信公众号消息
     *
     * @Author mgp
     * @Date 2022/5/30
     **/
    public static void sendWxMp(Message message,MessageService messageService,WxMpService wxMpService, String appId, String openId, Map<String, Object> msgContent, TemplateResult template) {
        wxMpService.switchover(appId);
        //要发送的用户
        WxMpTemplateMessage wxMpTemplateMessage = JSON.parseObject(template.getContent(), WxMpTemplateMessage.class);
        message.setContent(JSONUtil.toJsonStr(msgContent));
        //模板内容替换
        if (!ObjectUtil.isEmpty(msgContent)) {
            msgContent.forEach((k, v) -> {
                List<WxMpTemplateData> data = wxMpTemplateMessage.getData();
                Map<String, List<WxMpTemplateData>> wxMpTemplateDataMap = data.stream().collect(Collectors.groupingBy(WxMpTemplateData::getName));
                if (wxMpTemplateDataMap.containsKey(k)) {
                    wxMpTemplateDataMap.get(k).get(0).setValue((String) v);
                }
            });
        }
        //发送人
        wxMpTemplateMessage.setToUser(openId);
        // 发送模板消息
        try {
            wxMpService.getTemplateMsgService().sendTemplateMsg(wxMpTemplateMessage);
        } catch (Exception e) {
            log.error("微信公众号模板消息推送失败，接收用户openid: {}", openId, e);
            message.setResultContent(JSONUtil.toJsonStr(e));
        }
        messageService.save(message);
    }

//    public static void main(String[] args) {
//        Map<String,String> map = new HashMap<>();
//        map.put("url","http://sdk.open.api.igexin.com/apiex.htm");
//        map.put("appKey","2kYPw3PXTE9UDjPaECf6A6");
//        map.put("masterSecret","ohaeLMEIT48ovwhddTTK78");
//        map.put("appId","J6hQ86nRYvAniGD1CGMIn7");
//        map.put("content","测试推送了！");
//        map.put("title","推送");
//        pushMessage(map,"b64815b84a681b53b4013c88569434f5");
//        System.out.println("推送成功！");
//    }


//    /**
//     * app推送消息
//     * @params pushMessage推送消息
//     * @params appPushList推送角色目标列表
//     */
//    public static void pushMessage(Map<String,String> map,String clientId) {
//        //获取配置信息
//        String url = map.get("url");
//        String appKey = map.get("appKey");
//        String masterSecret = map.get("masterSecret");
//        String appId = map.get("appId");
//        IGtPush push = new IGtPush(url, appKey, masterSecret);
//
//        //获取标题和内容
//        String content = map.get("content");
//        String title =  map.get("title");
//
//        Style0 style = new Style0();
//        // STEP2：设置推送标题、推送内容
//        style.setTitle(title);
//        style.setText(content);
//        // STEP3：设置响铃、震动等推送效果
//        style.setRing(true);  // 设置响铃
//        style.setVibrate(true);  // 设置震动
//
//        // STEP4：选择通知模板
//        NotificationTemplate template = new NotificationTemplate();
//        template.setAppId(appId);
//        template.setAppkey(appKey);
//        template.setStyle(style);
//        // 点击消息打开应用
//        template.setTransmissionType(1);
//        // 传递自定义消息
//
//        //透传的参数
//        Map<String,Object>  transmissionMap = new HashMap();
//        transmissionMap.put("appPushType",map.get("appPushType"));
//        transmissionMap.put("appPushId",map.get("appPushId"));
//        template.setTransmissionContent(JSONUtil.toJsonStr(transmissionMap));
//        //template.setTransmissionContent(JSONUtil.toJsonStr(content));
//
//
//        // STEP5：定义"AppMessage"类型消息对象,设置推送消息有效期等推送参数
//        // 采用toList方案，定义ListMessage消息类型
////        List<String> appIds = new ArrayList<String>();
////        appIds.add(appId);
//        ListMessage message = new ListMessage();
//        message.setData(template);
////        message.setAppIdList(appIds);
//        message.setOffline(true);
//        message.setOfflineExpireTime(1000 * 600);  // 时间单位为毫秒
//
//        String contentId = push.getContentId(message);
//        // 获取推送目标
//        List targets = new ArrayList();
//        Target target = new Target();
//        target.setAppId(appId);
//        target.setClientId(clientId);
//        targets.add(target);
//
//        // STEP6：执行推送，不采用toApp方案，采用toList方案
////        IPushResult ret = push.pushMessageToApp(message);
//        IPushResult ret = push.pushMessageToList(contentId, targets);
//        System.out.println(ret.getResponse().toString());
//    }


//    /**
//     * 苹果推送
//     * @param ClientId
//     * @param map
//     */
//    public static void sendSingleIos(Map<String, String> map,String ClientId) {
//        String url = map.get("url");
//        String appKey = map.get("appKey");
//        String masterSecret = map.get("masterSecret");
//        String appId = map.get("appId");
//        //获取标题和内容
//        String content = map.get("content");
//
//        System.out.println("url:"+url);
//        System.out.println("appKey:"+appKey);
//        System.out.println("masterSecret:"+masterSecret);
//        System.out.println("clientId:"+ClientId);
//        System.out.println("content:"+content);
//
//        IGtPush push = new IGtPush(url, appKey, masterSecret);
//
//        SingleMessage message = new SingleMessage();
//
//        TransmissionTemplate template = new TransmissionTemplate();
//        template.setAppId(appId);
//        template.setAppkey(appKey);
//
//        //透传的参数
//        Map<String,Object>  transmissionMap = new HashMap();
//        transmissionMap.put("appPushType",map.get("appPushType"));
//        transmissionMap.put("appPushId",map.get("appPushId"));
//        template.setTransmissionContent(JSONUtil.toJsonStr(transmissionMap));
//
//        // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
//        template.setTransmissionType(2);
//        APNPayload payload = new APNPayload();
//        //在已有数字基础上加1显示，设置为-1时，在已有数字上减1显示，设置为数字时，显示指定数字
//        payload.setAutoBadge("+1");
//        payload.setContentAvailable(1);
//        //ios 12.0 以上可以使用 Dictionary 类型的 sound
//        payload.setSound("default");
//        payload.setCategory("$由客户端定义");
//        //简单模式APNPayload.SimpleMsg
//        //payload.setAlertMsg(new APNPayload.SimpleAlertMsg(""+map.get("content")));
//        //字典模式使用下者
//        payload.setAlertMsg(getDictionaryAlertMsg(map));
//        template.setAPNInfo(payload);
//
//        message.setData(template);
//
//        message.setOffline(true);
//        //离线有效时间，单位为毫秒，可选
//        message.setOfflineExpireTime(24 * 1000 * 3600);
//        // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
//        message.setPushNetWorkType(0);
//        Target target = new Target();
//        target.setAppId(appId);
//        target.setClientId(ClientId);
//        //target.setClientId("cce9cf94814df91f059c7edece63c390");
//        //target.setClientId("6acda574b2fa7073c27c5cdc48d7ace1");
//        IPushResult ret = null;
//        try {
//            ret = push.pushMessageToSingle(message, target);
//        } catch (RequestException e) {
//            e.printStackTrace();
//            ret = push.pushMessageToSingle(message, target, e.getRequestId());
//        }
//    }
//
//    /**
//     * 设置苹果参数
//     * @return
//     */
//    private static APNPayload getAPNPayload(Map<String,String> map) {
//        APNPayload payload = new APNPayload();
//        //在已有数字基础上加1显示，设置为-1时，在已有数字上减1显示，设置为数字时，显示指定数字
//        payload.setAutoBadge("+1");
//        payload.setContentAvailable(0);
//        //ios 12.0 以上可以使用 Dictionary 类型的 sound
//        payload.setSound("default");
//        payload.setCategory("$由客户端定义");
//        payload.addCustomMsg("由客户自定义消息key", "由客户自定义消息value");
//
//        //简单模式APNPayload.SimpleMsg
//        //payload.setAlertMsg(new APNPayload.SimpleAlertMsg("hello"));
//        payload.setAlertMsg(getDictionaryAlertMsg(map));  //字典模式使用APNPayload.DictionaryAlertMsg
//
//        //设置语音播报类型，int类型，0.不可用 1.播放body 2.播放自定义文本
//        payload.setVoicePlayType(2);
//        //设置语音播报内容，String类型，非必须参数，用户自定义播放内容，仅在voicePlayMessage=2时生效
//        //注：当"定义类型"=2, "定义内容"为空时则忽略不播放
//        payload.setVoicePlayMessage("定义内容");
//
//        // 添加多媒体资源
//        payload.addMultiMedia(new MultiMedia().setResType(MultiMedia.MediaType.pic)
//                .setResUrl("资源文件地址")
//                .setOnlyWifi(true));
//
//        return payload;
//    }
//
//    /**
//     * 设置字典模式
//     * @param map
//     * @return
//     */
//    private static APNPayload.DictionaryAlertMsg getDictionaryAlertMsg(Map<String, String> map) {
//        APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
//        alertMsg.setBody(map.get("content"));
//        alertMsg.setLocKey("" + map.get("content"));
//        alertMsg.setActionLocKey("ActionLockey");
//        alertMsg.addLocArg("loc-args");
//        alertMsg.setLaunchImage("launch-image");
//        // IOS8.2以上版本支持
//        alertMsg.setTitle("" + map.get("title"));
//        alertMsg.setTitleLocKey("" + map.get("content"));
//        alertMsg.addTitleLocArg("TitleLocArg");
//        return alertMsg;
//    }
}
