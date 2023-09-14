package cn.tech.wings.cloud.res.service;

import cn.tech.wings.cloud.res.api.entity.Album;
import cn.tech.wings.cloud.res.api.model.params.AlbumParam;
import cn.tech.wings.cloud.res.api.model.result.AlbumResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 相册表 服务类
 * </p>
 *
 * @author dijuli
 * @since 2019-10-09
 */
public interface AlbumService extends IService<Album> {

    /**
     * 新增
     *
     * @author dijuli
     * @Date 2019-10-09
     */
    void add(AlbumParam param);

    /**
     * 删除
     *
     * @author dijuli
     * @Date 2019-10-09
     */
    void delete(AlbumParam param);

    /**
     * 更新
     *
     * @author dijuli
     * @Date 2019-10-09
     */
    void update(AlbumParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author dijuli
     * @Date 2019-10-09
     */
    AlbumResult findBySpec(AlbumParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author dijuli
     * @Date 2019-10-09
     */
    List<AlbumResult> findListBySpec(AlbumParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author dijuli
     * @Date 2019-10-09
     */
     IPage<AlbumResult> findPageBySpec(Page page, AlbumParam param);

    /**
     * 根据传过来的店铺id，更新res下面的相册表
     *
     * @author 高梦瑶
     * @Date 2019-10-09
     */
    void updatePicOrVideoSize(Map map);

    /**
     * 更新文件夹使用大小
     * @param a
     * @param fileSize
     * @return
     */
    boolean updateSize(Album a, long fileSize);

    /**
     * 根据ResType，OwnerId获取根相册
     * @param ownerId
     * @param resType
     * @return
     */
    AlbumResult selectRootAlbumByOwnerIdResType(Integer ownerId, String resType);

    /**
     * 根据treeid 获取相册树
     * @param treeId
     * @param tcCode
     * @return
     */
    List<Map<String, Object>> selectAlbumTree(String treeId, String tcCode);

    Album getRootAlbumByTreeId(String treeid);

    Album getByAlbumCode(String album_code);

    boolean deleteAlbum(Album a);
}
