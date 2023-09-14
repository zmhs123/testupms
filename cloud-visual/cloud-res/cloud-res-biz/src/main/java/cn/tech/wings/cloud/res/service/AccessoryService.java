package cn.tech.wings.cloud.res.service;

import cn.tech.wings.cloud.res.api.entity.Accessory;
import cn.tech.wings.cloud.res.api.entity.Album;
import cn.tech.wings.cloud.res.api.model.params.AccessoryParam;
import cn.tech.wings.cloud.res.api.model.result.AccessoryResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 附件表 服务类
 * </p>
 *
 * @author dijuli
 * @since 2019-10-09
 */
public interface AccessoryService extends IService<Accessory> {

    /**
     * 新增
     *
     * @author dijuli
     * @Date 2019-10-09
     */
    void add(AccessoryParam param);

    /**
     * 删除
     *
     * @author dijuli
     * @Date 2019-10-09
     */
    void delete(AccessoryParam param);

    /**
     * 更新
     *
     * @author dijuli
     * @Date 2019-10-09
     */
    void update(AccessoryParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author dijuli
     * @Date 2019-10-09
     */
    AccessoryResult findBySpec(AccessoryParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author dijuli
     * @Date 2019-10-09
     */
    List<AccessoryResult> findListBySpec(AccessoryParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author dijuli
     * @Date 2019-10-09
     */
	IPage<AccessoryResult> findPageBySpec(AccessoryParam param, IPage page);

    void deleteAccessory(Album a, Accessory acc);

    Accessory getObjById(Long wmImageId);
}
