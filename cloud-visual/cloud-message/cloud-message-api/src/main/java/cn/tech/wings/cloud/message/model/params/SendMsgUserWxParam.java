package cn.tech.wings.cloud.message.model.params;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 消息表
 * </p>
 *
 * @author mgp
 * @since 2019-11-18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendMsgUserWxParam implements Serializable {

    private static final long serialVersionUID = 1L;

	@ApiModelProperty("用户OpenId")
    private String openId;

    @ApiModelProperty("用户所属appId")
    private String appId;


}
