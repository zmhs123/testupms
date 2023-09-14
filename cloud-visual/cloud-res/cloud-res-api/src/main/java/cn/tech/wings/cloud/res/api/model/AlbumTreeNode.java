package cn.tech.wings.cloud.res.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 菜单树形节点
 * </p>
 *
 * @author di
 * @since 2019-04-01
 */
@Data
public class AlbumTreeNode implements Tree {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("主键id")
    private Integer id;

    @ApiModelProperty("父分类id")
    private Integer parentid;

    @ApiModelProperty("相册名称")
    private String albumName;

    @ApiModelProperty("相册序号-序号越大越靠前")
    private Integer albumSequence;

    @ApiModelProperty("相册编码")
    private String albumCode;

    @ApiModelProperty("相册下的文件数量")
    private Integer fileCount;

    @ApiModelProperty("子节点")
    private List<AlbumTreeNode> children;

    @Override
    public String getNodeId() {
        if (Objects.nonNull(id)) {
            return id.toString();
        } else {
            return "0";
        }
    }

    @Override
    public String getNodeParentId() {
        if (Objects.nonNull(parentid)) {
            return parentid.toString();
        } else {
            return "0";
        }
    }

    @Override
    public void setChildrenNodes(List children) {
        this.children = children;
    }
}
