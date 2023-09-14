package cn.tech.wings.cloud.message.service.impl;

import cn.tech.wings.cloud.message.entity.Message;
import cn.tech.wings.cloud.message.mapper.MessageMapper;
import cn.tech.wings.cloud.message.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 消息表 服务实现类
 * </p>
 *
 * @author mgp
 * @since 2022-06-16
 */
@Service
@Slf4j
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

}
