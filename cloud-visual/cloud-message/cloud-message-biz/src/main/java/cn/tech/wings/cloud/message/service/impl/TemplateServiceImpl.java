package cn.tech.wings.cloud.message.service.impl;

import cn.tech.wings.cloud.message.entity.Template;
import cn.tech.wings.cloud.message.model.params.TemplateParam;
import cn.tech.wings.cloud.message.model.result.TemplateResult;
import cn.tech.wings.cloud.message.mapper.TemplateMapper;
import cn.tech.wings.cloud.message.service.TemplateService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 通知模板表 服务实现类
 * </p>
 *
 * @author mgp
 * @since 2022-06-15
 */
@Service
public class TemplateServiceImpl extends ServiceImpl<TemplateMapper, Template> implements TemplateService {

	@Override
	public IPage<TemplateResult> findListBySpec(Page page, TemplateParam param) {
		return this.baseMapper.customPageList(page,param);
	}

	@Override
    public List<TemplateResult> getObjByMark(String mark){
        return this.baseMapper.getObjByMark(mark);
    }

}
