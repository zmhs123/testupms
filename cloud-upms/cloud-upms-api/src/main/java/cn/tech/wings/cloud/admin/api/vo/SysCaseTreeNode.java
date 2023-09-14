package cn.tech.wings.cloud.admin.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author xinglx
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysCaseTreeNode {

    private String node;

    private String nodeName;

    private String parentNode;

    private List<SysCaseTreeNode> childNodes;

}
