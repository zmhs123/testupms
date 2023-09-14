package cn.tech.wings.cloud.message.mapper;

import cn.tech.wings.cloud.common.data.datascope.CloudBaseMapper;
import cn.tech.wings.cloud.message.entity.Message;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 消息表 Mapper 接口
 * </p>
 *
 * @author mgp
 * @since 2022-06-16
 */
@Mapper
public interface MessageMapper extends CloudBaseMapper<Message> {

}
