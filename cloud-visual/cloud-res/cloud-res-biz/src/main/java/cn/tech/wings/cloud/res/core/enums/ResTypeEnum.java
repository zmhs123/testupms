package cn.tech.wings.cloud.res.core.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资源类型标识
 *
 * @author gaoyunfei
 * @Date 2019/10/15
 */
public enum ResTypeEnum {
    /**
     * 资源类型：pic、video、file
     */
    PIC("pic","照片"),
    VIDEO("video","视频"),
    FILE("file","文件");

    /**
     * 标识的key,一般存在数据库中
     */
    private String value;

    /**
     * 标识的key,对应的含义
     */
    private String label;

    ResTypeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * 获取枚举的Map对象
     */
    public static List<Map<String,Object>> getListMap() {
        List<Map<String,Object>> list = new ArrayList<>();
        for (ResTypeEnum flagEnum : ResTypeEnum.values()) {
            Map<String,Object> map = new HashMap<>();
            map.put("value",flagEnum.getValue());
            map.put("label",flagEnum.getLabel());
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
