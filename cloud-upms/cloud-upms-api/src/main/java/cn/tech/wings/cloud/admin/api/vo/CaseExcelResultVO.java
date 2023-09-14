package cn.tech.wings.cloud.admin.api.vo;

import cn.tech.wings.cloud.common.excel.annotation.ExcelLine;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 用户excel 对应的实体
 *
 * @author cloud
 * @date 2021/8/4
 */
@Data
@ColumnWidth(30)
public class CaseExcelResultVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 序号
	 */
	@ExcelLine
	@ExcelIgnore
	private Long lineNum;

	/**
	 * 案例编号
	 */
	@NotBlank(message = "案例编号不能为空")
	@ExcelProperty("案例编号")
	private String caseCode;

	/**
	 * 测试需求编号
	 */
	@NotBlank(message = "测试需求编号")
	@ExcelProperty("测试需求编号")
	private String caseTestCode;


	/**
	 * 用例路径
	 */
	@NotBlank(message = "用例路径不能为空")
	@ExcelProperty("用例路径")
	private String path;

	/**
	 * 优先级
	 */
	@ExcelProperty("优先级")
	private Integer sortOrder;

	/**
	 * 案例名称
	 */
	@NotBlank(message = "案例名称不能为空")
	@ExcelProperty("案例名称")
	private String name;

	/**
	 * 前置条件
	 */
	@ExcelProperty("前置条件")
	private String preconditions;


	/**
	 * 测试数据
	 */
	@ExcelProperty("测试数据")
	@NotBlank(message = "测试数据不能为空")
	private String testData;

	/**
	 * 操作步骤
	 */
	@ExcelProperty("操作步骤")
	private String caseRemark;



	/**
	 * 预期结果
	 */
	@NotBlank(message = "预期结果不能为空")
	@ExcelProperty("预期结果")
	private String expectResult ;

	/**
	 * 案例类型
	 */
	@NotBlank(message = "案例类型不能为空")
	@ExcelProperty("案例类型")
	private String caseType ;

	/**
	 * 正反例
	 */
	@NotBlank(message = "正反例不能为空")
	@ExcelProperty("正反例")
	private String positive2negative ;

	/**
	 * 案例编写人员
	 */
	@NotBlank(message = "案例编写人员不能为空")
	@ExcelProperty("案例编写人员")
	private String createUser ;


	/**
	 * 备注
	 */
	@NotBlank(message = "备注不能为空")
	@ExcelProperty("备注")
	private String remarks;


	/**
	 * 错误信息
	 */
	@ExcelProperty("错误信息")
	private String errorMsg;

}
