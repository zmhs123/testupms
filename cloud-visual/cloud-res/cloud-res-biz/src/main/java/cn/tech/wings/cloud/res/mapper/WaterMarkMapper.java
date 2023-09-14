package cn.tech.wings.cloud.res.mapper;

import cn.tech.wings.cloud.res.api.entity.WaterMark;
import cn.tech.wings.cloud.res.api.model.params.WaterMarkParam;
import cn.tech.wings.cloud.res.api.model.result.WaterMarkResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author fengchu_pang
 * @since 2020-11-20
 */
@Mapper
public interface WaterMarkMapper extends BaseMapper<WaterMark> {

    /**
     * 获取列表
     *
     * @author fengchu_pang
     * @Date 2020-11-20
     */
    List<WaterMarkResult> customList(@Param("paramCondition") WaterMarkParam paramCondition);

    /**
     * 获取map列表
     *
     * @author fengchu_pang
     * @Date 2020-11-20
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") WaterMarkParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author fengchu_pang
     * @Date 2020-11-20
     */
    Page<WaterMarkResult> customPageList(@Param("page") Page page, @Param("paramCondition") WaterMarkParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author fengchu_pang
     * @Date 2020-11-20
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") WaterMarkParam paramCondition);

//    List<WaterMark> getAllByStoreId(Map<String, Object> params);


    List<WaterMark> getMgrWaterMark();
}
