package cn.tech.wings.cloud.message.core.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息mark枚举
 *
 * @author mgp
 */
public enum MessageMarkEnum {
    /**
     * 短信模板参数示例
     * {"code":"123"}
     */
    LOGIN_REGISTER("validation_phone", "登陆注册验证码"),
    /**
     * 短信模板参数示例
     * {"code":"123"}
     */
    REPLACE_MOBILE("replace_mobile", "更换绑定手机号验证码"),
    /**
     * 短信模板参数示例
     * {companyName:"xxx公司名称","date":"5"}
     */
    SIGN_ONLINE_START("sign_online_start", "通知签约"),
    SIGN_ONLINE_PARTY_COMPLETE("sign_online_party_complete", "一方签署完成"),
    SIGN_ONLINE_FILING("sign_online_filing", "归档提醒"),
    SIGN_ONLINE_COMPLETE("sign_online_complete", "完成签约"),
    /**
     * 1.短信模板参数示例
     * {"companyName":"xx公司","month":"5","dateStr":"5","code":"xxx"}
     *
     */
    /**
     * 2.小程序模板参数示例
     * {"thing1":"北京售电公司...",
     * "time2":"2022年5月",
     * "time3":"2022-05-28",
     * "thing4":"2022年5月份电量申报",
     * "urlParam":"跳转链接参数"}
     */
    COLLECT_ELECTRIC("collect_electric", "收集电量"),

    /**
     * 1.短信模板参数示例
     * {"companyName":"xxx公司","month":"5","url":"xxx"}
     * <p>
     * 2.小程序模板参数示例
     * {"time1":"2022年5月",
     * "thing2":"北京售电公司...",
     * "thing3":"北京用电公司...",
     * "character_string4":"2005MWh",
     * "urlParam":"跳转链接参数"}
     */
    BILL_PUSH_USE("bill_push_use", "账单推送(用电)"),

    /**
     * 小程序模板参数示例
     * {"thing3":"新政策今日开始实施",
     * "time5":"2022年6月24日10点27分",
     * "thing2":"公告/政策",
     * "thing6":"今日开始电量可自由交易",
     * "urlParam":"跳转链接参数"
     * }
     */
    MATTER_NOTICE("matter_notice", "待办事项通知");

    /**
     * 标识的value,一般存在数据库中
     */
    private String value;

    /**
     * 标识的label,对应的含义
     */
    private String label;

    MessageMarkEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * 获取枚举的Map对象
     */
    public static List<Map<String, Object>> getListMap() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MessageMarkEnum messageEnum : MessageMarkEnum.values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("label", messageEnum.getLabel());
            map.put("value", messageEnum.getValue());
            list.add(map);
        }
        return list;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
