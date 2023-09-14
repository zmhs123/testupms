package cn.tech.wings.cloud.res.core.enums.result;

import cn.tech.wings.cloud.res.core.enums.ResTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 枚举字典返回结果类
 * </p>
 *
 * @author gaoyunfei
 * @since 2019-09-21
 */
@Data
public class EnumDictResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 资源类型：pic、video、file
     */
    public List<Map<String,Object>> RES_TYPE = ResTypeEnum.getListMap();

}
