package cn.tech.wings.cloud.admin.service;

import cn.tech.wings.cloud.admin.api.entity.SysCase;
import cn.tech.wings.cloud.admin.api.vo.CaseExcelVO;
import cn.tech.wings.cloud.common.core.util.R;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.validation.BindingResult;

import java.util.List;

/**
 * 用例表
 *
 * @author lixuan_xing
 * @date 2022-12-12 17:19:11
 */
public interface SysCaseService extends IService<SysCase> {
	/**
	 * 开始录制
	 * @param sysCase
	 * @return
	 */
	R startRecording(SysCase sysCase);
	/**
	 * 结束录制
	 * @param sysCase
	 * @return
	 */
	R endRecording(SysCase sysCase);

	/**
	 * 获取用例路径树
	 * @param sysCase
	 * @param condition
	 * @return
	 */
	R getCasePathTree(SysCase sysCase, String condition);

	/**
	 * excel 导入用例
	 * @param projectId 应用id
	 * @param importType 导入类型 0跳过 1覆盖
	 * @param excelVOList excel 列表数据
	 * @param bindingResult 错误数据
	 * @return ok fail
	 */
	R importCase(String projectId,Integer importType,List<CaseExcelVO> excelVOList, BindingResult bindingResult);

}
