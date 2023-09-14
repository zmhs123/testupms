package cn.tech.wings.cloud.message.mapper;

import cn.tech.wings.cloud.message.entity.VerifyCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 短信服务表 Mapper 接口
 * </p>
 *
 * @author mgp
 * @since 2022-06-16
 */
@Mapper
public interface VerifyCodeMapper extends BaseMapper<VerifyCode> {

	/**
	 * 清除24小时之外的验证码
	 * @return
	 */
    void clearVerifyCode();

}
