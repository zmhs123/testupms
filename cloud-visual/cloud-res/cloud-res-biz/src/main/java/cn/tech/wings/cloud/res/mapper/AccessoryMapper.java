package cn.tech.wings.cloud.res.mapper;

import cn.tech.wings.cloud.res.api.entity.Accessory;
import cn.tech.wings.cloud.res.api.model.params.AccessoryParam;
import cn.tech.wings.cloud.res.api.model.result.AccessoryResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 附件表 Mapper 接口
 * </p>
 *
 * @author dijuli
 * @since 2019-10-09
 */
@Mapper
public interface AccessoryMapper extends BaseMapper<Accessory> {

    /**
     * 获取列表
     *
     * @author dijuli
     * @Date 2019-10-09
     */
    List<AccessoryResult> customList(@Param("paramCondition") AccessoryParam paramCondition);

    /**
     * 获取map列表
     *
     * @author dijuli
     * @Date 2019-10-09
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") AccessoryParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author dijuli
     * @Date 2019-10-09
     */
    Page<AccessoryResult> customPageList(@Param("page") IPage page, @Param("paramCondition") AccessoryParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author dijuli
     * @Date 2019-10-09
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") AccessoryParam paramCondition);

}
