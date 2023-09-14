package cn.tech.wings.cloud.message.model.params;

import cn.tech.wings.cloud.message.model.result.TemplateResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * 发送小程序消息模板
 * </p>
 *
 * @author mgp
 * @since 2019-11-15
 */
@Data
@ApiModel(value="发送小程序消息模板")
@SuperBuilder
public class SendXcxMessageParam implements Serializable {

    @ApiModelProperty(value="模板信息")
    private TemplateResult templateResult;

	@ApiModelProperty(value="用户openId")
	private String toUser;

    @ApiModelProperty(value="发送内容")
    private Map<String, Object> param;

}
