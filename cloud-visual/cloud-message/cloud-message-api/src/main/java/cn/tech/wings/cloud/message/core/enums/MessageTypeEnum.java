package cn.tech.wings.cloud.message.core.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MGP
 * @Title: mall-cloud
 * @Package com.wing.mall.cloud.order.core.enums
 * @Description:信息使用用途枚举
 * @date 2019/10/14 14:52
 */
public enum MessageTypeEnum {

    /**
     * 短信
     */
    SMS_MSG(1,"短信"),
    /**
     * 微信小程序消息
     */
    WX_MA_MSG(2,"微信小程序消息"),
    /**
     * 微信公众号消息
     */
    WX_MP_MSG(3,"微信公众号消息"),
    /**
     * APP消息
     */
    APP_MSG(4,"APP消息");

    MessageTypeEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    private Integer value;

    private String label;

    public Integer getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
