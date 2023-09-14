package cn.tech.wings.cloud.message.mapper;

import cn.tech.wings.cloud.common.data.datascope.CloudBaseMapper;
import cn.tech.wings.cloud.message.entity.Template;
import cn.tech.wings.cloud.message.model.params.TemplateParam;
import cn.tech.wings.cloud.message.model.result.TemplateResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 通知模板表 Mapper 接口
 * </p>
 *
 * @author mgp
 * @since 2022-06-15
 */
@Mapper
public interface TemplateMapper extends CloudBaseMapper<Template> {

    /**
     * 获取分页实体列表
     *
     * @param  page
     * @param  paramCondition
     * @return
     */
    Page<TemplateResult> customPageList(@Param("page") Page page, @Param("paramCondition") TemplateParam paramCondition);

    /**
     * 根据标识获取
     * @param mark
     * @return
     */
    List<TemplateResult> getObjByMark(@Param("mark") String mark);

}
