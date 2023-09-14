package cn.tech.wings.cloud.admin.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.TreeSet;

/**
 * @author xinglx
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysCaseTreeVO {

    private List<SysCaseTreeNode> tree;

    private TreeSet<String> hitsNodes;
}
