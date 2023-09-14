package cn.tech.wings.cloud.res.factory;

import cn.hutool.core.bean.BeanUtil;
import cn.tech.wings.cloud.res.api.model.AlbumTreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 菜单树构建
 *
 * @author di
 * @date 2019-07-26-17:38
 */
public class AlbumFactory {

    /**
     * 构造菜单树，菜单列表用
     *
     * @author di
     * @Date 2019-07-26 17:41
     */
    public static List<AlbumTreeNode> buildTreeNodes(List<Map<String, Object>> originMap) {

        ArrayList<AlbumTreeNode> albumTreeNodes = new ArrayList<>();

        for (Map<String, Object> map : originMap) {
            AlbumTreeNode menuTreeNode = BeanUtil.mapToBean(map, AlbumTreeNode.class, true);
            albumTreeNodes.add(menuTreeNode);
        }

        DefaultTreeBuildFactory<AlbumTreeNode> treeBuildFactory = new DefaultTreeBuildFactory<>();

        treeBuildFactory.setRootParentId("0");

        return treeBuildFactory.doTreeBuild(albumTreeNodes);
    }

}
