package cn.tech.wings.cloud.admin.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author xinglx
 * @Title: testJava
 * @Package cn.tech.wings.cloud.admin.api.vo
 * @Description:
 * @date 2023/1/29 10:14
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DyedTmpResult {
	List<SysCaseTreeNode> childNodes;
	boolean dyedResult;
}
