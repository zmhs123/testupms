package cn.tech.wings.cloud.res.mapper;

import cn.tech.wings.cloud.res.api.entity.ResTxNo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 事务mapper
 * </p>
 *
 * @author dijuli
 * @since 2019-10-09
 */
@Mapper
public interface ResTxNoMapper extends BaseMapper<ResTxNo> {
    //根据时间清除幂等数据
    int deleteTxNoByDate(@Param("date") String date);
}
