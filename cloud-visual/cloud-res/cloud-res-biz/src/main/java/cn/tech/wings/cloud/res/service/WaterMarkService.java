package cn.tech.wings.cloud.res.service;

import cn.tech.wings.cloud.res.api.entity.WaterMark;
import cn.tech.wings.cloud.res.api.model.params.WaterMarkParam;
import cn.tech.wings.cloud.res.api.model.result.WaterMarkResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fengchu_pang
 * @since 2020-11-20
 */
public interface WaterMarkService extends IService<WaterMark> {

    /**
     * 新增
     *
     * @author fengchu_pang
     * @Date 2020-11-20
     */
    void add(WaterMarkParam param);

    /**
     * 删除
     *
     * @author fengchu_pang
     * @Date 2020-11-20
     */
    void delete(WaterMarkParam param);

    /**
     * 更新
     *
     * @author fengchu_pang
     * @Date 2020-11-20
     */
    void update(WaterMarkParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author fengchu_pang
     * @Date 2020-11-20
     */
    List<WaterMarkResult> findListBySpec(WaterMarkParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author fengchu_pang
     * @Date 2020-11-20
     */
	IPage<WaterMarkResult> findPageBySpec(Page page, WaterMarkParam param);

//    List<WaterMark> getAllByStoreId(Map<String, Object> params);


    List<WaterMark> getMgrWaterMark();
}
