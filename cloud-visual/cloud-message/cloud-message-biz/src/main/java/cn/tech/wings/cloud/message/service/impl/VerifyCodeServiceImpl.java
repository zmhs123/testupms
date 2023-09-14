package cn.tech.wings.cloud.message.service.impl;

import cn.tech.wings.cloud.message.entity.VerifyCode;
import cn.tech.wings.cloud.message.mapper.VerifyCodeMapper;
import cn.tech.wings.cloud.message.service.VerifyCodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 短信服务表 服务实现类
 * </p>
 *
 * @author mgp
 * @since 2022-06-16
 */
@Service
@AllArgsConstructor
public class VerifyCodeServiceImpl extends ServiceImpl<VerifyCodeMapper, VerifyCode> implements VerifyCodeService {

	@Override
	public void clearVerifyCode() {
		this.baseMapper.clearVerifyCode();
	}

}
