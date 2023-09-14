package cn.tech.wings.cloud.message.core.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xinglx
 * @Title: mall-cloud
 * @Package com.wing.mall.cloud.order.core.enums
 * @Description:信息发送类型枚举
 * @date 2019/10/14 14:52
 */
public enum ReceiverTypeEnum {
    /**
     * 信息发送类型 email|mobile
     */
    MOBILE("mobile","手机"),
    EMAIL("email","邮箱")
    ;

    ReceiverTypeEnum(String value, String label) {
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
        for (ReceiverTypeEnum receiverTypeEnum : ReceiverTypeEnum.values()) {
            Map<String,Object> map = new HashMap<>();
            map.put("value",receiverTypeEnum.getValue());
            map.put("label",receiverTypeEnum.getLabel());
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
        for (ReceiverTypeEnum activityTypeEnum : ReceiverTypeEnum.values()) {
            if (activityTypeEnum.getValue() == value) {
                return activityTypeEnum.getLabel();
            }
        }
        return "";
    }
}
