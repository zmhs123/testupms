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
public enum MsgUseEnum {
    /**
     * 用途 用户注册
     * 用户登录/注册 validation_phone
     */
    VALIDATION_PHONE("validation_phone","用户登录/注册"),

    /**
     * 用途 更改手机号
     * 更改手机号 replace_mobile
     */
    REPLACE_MOBILE("replace_mobile","更改手机号"),

    /**
     * 用途 业务通用验证码
     *  currency_code
     */
    CURRENCY_CODE("currency_code","业务通用验证码");

    MsgUseEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    private String value;

    private String label;

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
    /**
     * 获取枚举的Map对象
     */
    public static List<Map<String,Object>> getEnumList() {
        List resultList = new ArrayList();
        for (MsgUseEnum msgUseEnum : MsgUseEnum.values()) {
            Map<String,Object> map = new HashMap<>();
            map.put("value",msgUseEnum.getValue());
            map.put("label",msgUseEnum.getLabel());
            resultList.add(map);
        }
        return resultList;
    }

    /**
     * 根据value获取label
     * @param value
     * @return
     */
    public static String getLabelStr(String value) {
        for (MsgUseEnum activityTypeEnum : MsgUseEnum.values()) {
            if (activityTypeEnum.getValue() == value) {
                return activityTypeEnum.getLabel();
            }
        }
        return "";
    }
}
