package cn.tech.wings.cloud.res.mapper;

import cn.tech.wings.cloud.res.api.entity.Album;
import cn.tech.wings.cloud.res.api.model.params.AlbumParam;
import cn.tech.wings.cloud.res.api.model.result.AlbumResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 相册表 Mapper 接口
 * </p>
 *
 * @author dijuli
 * @since 2019-10-09
 */
@Mapper
public interface AlbumMapper extends BaseMapper<Album> {

    /**
     * 获取列表
     *
     * @author dijuli
     * @Date 2019-10-09
     */
    List<AlbumResult> customList(@Param("paramCondition") AlbumParam paramCondition);

    /**
     * 获取map列表
     *
     * @author dijuli
     * @Date 2019-10-09
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") AlbumParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author dijuli
     * @Date 2019-10-09
     */
    Page<AlbumResult> customPageList(@Param("page") Page page, @Param("paramCondition") AlbumParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author dijuli
     * @Date 2019-10-09
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") AlbumParam paramCondition);

    List<AlbumResult> findListBySpec(AlbumParam param);


    /**
     * 跨服务调用专用，专门查询父ID为null
     * */
    List<AlbumResult> findListForPassService(AlbumParam param);

    /**
     * 根据ResType，OwnerId获取根相册
     * @param ownerId
     * @param resType
     * @return
     */
    List<AlbumResult> selectRootAlbumByOwnerIdResType(@Param("ownerId") Integer ownerId, @Param("resType") String resType);

    /**
     * 根据 treeId 获取相册树
     * @param treeId
     * @param tcCode
     * @return
     */
    List<Map<String, Object>> selectAlbumTree(@Param("treeId") String treeId, @Param("tcCode") String tcCode);
}
