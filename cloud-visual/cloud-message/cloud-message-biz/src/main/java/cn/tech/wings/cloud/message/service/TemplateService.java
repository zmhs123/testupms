package cn.tech.wings.cloud.message.service;

import cn.tech.wings.cloud.message.entity.Template;
import cn.tech.wings.cloud.message.model.params.TemplateParam;
import cn.tech.wings.cloud.message.model.result.TemplateResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 通知模板表 服务类
 * </p>
 *
 * @author mgp
 * @since 2022-06-15
 */
public interface TemplateService extends IService<Template> {

	/**
	 * 查询列表，Specification模式
	 *
	 * @param page
	 * @param param
	 * @return TemplateResult
	 */
	IPage<TemplateResult> findListBySpec(Page page, TemplateParam param);

    /**
     * 根据mark获取template对象
     * @param mark
     * @return
     */
     List<TemplateResult> getObjByMark(String mark);

}
