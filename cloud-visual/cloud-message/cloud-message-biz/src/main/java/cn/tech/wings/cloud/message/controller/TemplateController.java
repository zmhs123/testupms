package cn.tech.wings.cloud.message.controller;

import cn.tech.wings.cloud.common.core.util.R;
import cn.tech.wings.cloud.message.model.params.TemplateParam;
import cn.tech.wings.cloud.message.model.result.TemplateResult;
import cn.tech.wings.cloud.message.service.TemplateService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * 通知模板表控制器
 *
 * @author mgp
 * @Date 2019-11-15 15:04:31
 */
@Api(value="通知模板表管理",tags={"通知模板表操作接口"})
@RestController
@RequestMapping("/template")
@AllArgsConstructor
public class TemplateController {

    private final TemplateService templateService;

    /**
     * 查询列表
     *
     * @author mgp
     * @Date 2019-11-15
     */
    @ApiOperation("查询列表接口")
    @PostMapping("/list")
    public R<IPage<TemplateResult>> list(
			@ApiParam("模板内容") @RequestParam(value = "content", required = false) String content,
			@ApiParam("模板标题") @RequestParam(value = "title", required = false) String title,
			@ApiParam("模板类型sms短信,wx_ma微信小程序,wx_mp微信公众号,appAPP消息") @RequestParam(value = "type", required = false) String type,
			@ApiParam("每页记录数") @RequestParam(value = "size", required = false,defaultValue = "10") Integer size,
			@ApiParam("当前页") @RequestParam(value = "current", required = false,defaultValue = "1") Integer current
	) {
        return R.ok(templateService.findListBySpec(Page.of(current,size), TemplateParam.builder().content(content).type(type).title(title).build()));
    }




}


