package cn.tech.wings.cloud.admin.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xinglx
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DyedSysCaseTreeNode {

    private SysCaseTreeNode node;

    private boolean dyedResult;

}
