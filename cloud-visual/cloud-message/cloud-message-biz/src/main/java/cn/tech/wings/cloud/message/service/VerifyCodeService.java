package cn.tech.wings.cloud.message.service;

import cn.tech.wings.cloud.message.entity.VerifyCode;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * <p>
 * 短信服务表 服务类
 * </p>
 *
 * @author mgp
 * @since 2022-06-16
 */
public interface VerifyCodeService extends IService<VerifyCode> {


    /**
     * 清除验证码
     * @Author mgp
     * @Date 2022/6/24
     **/
    void clearVerifyCode();
}
