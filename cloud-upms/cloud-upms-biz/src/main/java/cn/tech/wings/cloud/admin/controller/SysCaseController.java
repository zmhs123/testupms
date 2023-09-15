package cn.tech.wings.cloud.admin.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.tech.wings.cloud.admin.api.feign.RemoteAccurateApiService;
import cn.tech.wings.cloud.admin.api.vo.CaseExcelVO;
import cn.tech.wings.cloud.admin.service.SysCaseService;
import cn.tech.wings.cloud.common.excel.annotation.RequestExcel;
import cn.tech.wings.cloud.common.security.annotation.Inner;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.tech.wings.cloud.common.core.util.R;
import cn.tech.wings.cloud.common.log.annotation.SysLog;
import cn.tech.wings.cloud.admin.api.entity.SysCase;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import cn.tech.wings.cloud.common.excel.annotation.ResponseExcel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用例表
 *
 * @author lixuan_xing
 * @date 2022-12-12 17:19:11
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/case")
@Api(value = "case", tags = "用例表管理")
@Slf4j
public class SysCaseController {

	private final SysCaseService sysCaseService;

	private final RemoteAccurateApiService remoteAccurateApiService;

	/**
	 * 分页查询
	 *
	 * @param page    分页对象
	 * @param sysCase 用例表
	 * @return
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('sys_case_page')")
	public R getSysCasePage(Page page, SysCase sysCase) {
		LambdaQueryWrapper<SysCase> objectLambdaQueryWrapper = Wrappers.lambdaQuery();
		objectLambdaQueryWrapper
				.like(StrUtil.isNotBlank(sysCase.getPath()), SysCase::getPath, sysCase.getPath())
				.like(StrUtil.isNotBlank(sysCase.getName()), SysCase::getName, sysCase.getName())
				.like(StrUtil.isNotBlank(sysCase.getCaseCode()), SysCase::getCaseCode, sysCase.getCaseCode())
				.eq(sysCase.getProjectId() != null, SysCase::getProjectId, sysCase.getProjectId())
				.eq(sysCase.getStatus() != null, SysCase::getStatus, sysCase.getStatus())
				.orderByDesc(SysCase::getCreateTime);
		log.info("分页查询");
		return R.ok(sysCaseService.page(page, objectLambdaQueryWrapper));
	}


	/**
	 * 通过id查询用例表
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询", notes = "通过id查询")
	@GetMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('sys_case_view')")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(sysCaseService.getById(id));
	}

	/**
	 * 新增用例表
	 *
	 * @param sysCase 用例表
	 * @return R
	 */
	@ApiOperation(value = "新增用例表", notes = "新增用例表")
	@SysLog("新增用例表")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('sys_case_add')")
	public R save(@RequestBody SysCase sysCase) {
		LambdaQueryWrapper<SysCase> objectLambdaQueryWrapper = Wrappers.lambdaQuery();
		objectLambdaQueryWrapper
				.eq(StrUtil.isNotBlank(sysCase.getCaseCode()), SysCase::getCaseCode, sysCase.getCaseCode())
//				.eq(sysCase.getProjectId() != null ,SysCase::getProjectId,sysCase.getProjectId())
				.orderByDesc(SysCase::getCreateTime);
		List<SysCase> sysCaseList = sysCaseService.list(objectLambdaQueryWrapper);
		log.info("新增用例表");
		if (CollectionUtil.isNotEmpty(sysCaseList)) {
			return R.failed("用例编码已经存在");
		}
		return R.ok(sysCaseService.save(sysCase));
	}

	/**
	 * 修改用例表
	 *
	 * @param sysCase 用例表
	 * @return R
	 */
	@ApiOperation(value = "修改用例表", notes = "修改用例表")
	@SysLog("修改用例表")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('sys_case_edit')")
	public R updateById(@RequestBody SysCase sysCase) {
		return R.ok(sysCaseService.updateById(sysCase));
	}

	/**
	 * 通过id删除用例表
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id删除用例表", notes = "通过id删除用例表")
	@SysLog("通过id删除用例表")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('sys_case_del')")
	public R removeById(@PathVariable Long id) {
		SysCase sysCase = sysCaseService.getById(id);
		sysCaseService.removeById(id);
		Map<String, Object> map = new HashMap<>();
		List<String> ids = new ArrayList<>();
		ids.add(sysCase.getCaseCode());
		map.put("ids", ids);
		map.put("projectId", sysCase.getProjectId().toString());
		// 删除用例对应的追溯关系
		remoteAccurateApiService.removeByCaseIds(map);
		return R.ok();
	}


	/**
	 * 导出excel 表格
	 *
	 * @param sysCase 查询条件
	 * @return excel 文件流
	 */
	@ResponseExcel
	@GetMapping("/export")
	@PreAuthorize("@pms.hasPermission('sys_case_export')")
	public List<SysCase> export(SysCase sysCase) {
		return sysCaseService.list(Wrappers.query(sysCase));
	}

	/**
	 * 开始录制
	 *
	 * @param sysCase 用例表
	 * @return R
	 */
	@ApiOperation(value = "开始录制", notes = "开始录制")
	@SysLog("开始录制")
	@PostMapping("/startRecording")
	@PreAuthorize("@pms.hasPermission('sys_case_start_recording')")
	public R startRecording(@RequestBody SysCase sysCase) {
		return sysCaseService.startRecording(sysCase);
	}

	/**
	 * 结束录制
	 *
	 * @param sysCase 用例表
	 * @return R
	 */
	@ApiOperation(value = "结束录制", notes = "结束录制")
	@SysLog("结束录制")
	@PostMapping("/endRecording")
	@PreAuthorize("@pms.hasPermission('sys_case_end_recording')")
	public R endRecording(@RequestBody SysCase sysCase) {
		return sysCaseService.endRecording(sysCase);
	}

	/**
	 * 导入用例
	 *
	 * @param projectId     应用id
	 * @param importType    导入类型 0跳过 1覆盖
	 * @param excelVOList   用户列表
	 * @param bindingResult 错误信息列表
	 * @return R
	 */
	@PostMapping("/import/{projectId}")
	@PreAuthorize("@pms.hasPermission('sys_case_export')")
	public R importCase(@PathVariable String projectId, @RequestParam(name = "importType") Integer importType, @RequestExcel List<CaseExcelVO> excelVOList, BindingResult bindingResult) {
		return sysCaseService.importCase(projectId, importType, excelVOList, bindingResult);
	}

	/**
	 * 根据用例code集合查询用例描述
	 *
	 * @param caseCodeList 用例code集合
	 * @return R
	 */
	@ApiOperation(value = "新增用例表", notes = "根据用例code集合查询用例描述")
	@SysLog("根据用例code集合查询用例描述")
	@PostMapping("/getCaseRemarkByCaseCode")
	@Inner(value = false)
	public R getCaseRemarkByCaseCode(@RequestBody List<String> caseCodeList) {
		if (CollectionUtil.isEmpty(caseCodeList)) {
			return R.failed("用例code集合不能为空");
		}
		LambdaQueryWrapper<SysCase> objectLambdaQueryWrapper = Wrappers.lambdaQuery();
		objectLambdaQueryWrapper
				.in(SysCase::getCaseCode, caseCodeList)
				.orderByDesc(SysCase::getCreateTime);
		List<SysCase> sysCaseList = sysCaseService.list(objectLambdaQueryWrapper);
		return R.ok(sysCaseList);
	}

	/**
	 * 获取用例路径树
	 *
	 * @param sysCase   用例表
	 * @param condition 关键字
	 * @return R
	 */
	@ApiOperation(value = "获取用例路径树", notes = "获取用例路径树")
	@PostMapping("/getCasePathTree")
	@Inner(value = false)
	public R getCasePathTree(@RequestBody SysCase sysCase, @RequestParam String condition) {
		return sysCaseService.getCasePathTree(sysCase, condition);
	}


	//模拟接口
	@PostMapping("/getProject")
	@ApiOperation(value = "获取项目列表", httpMethod = "POST")
	@Inner(value = false)
	public Object getProject(
			@ApiParam(name = "appId") @RequestParam(value = "appId", required = false) String appId,
			@ApiParam(name = "查询key") @RequestParam(value = "keyword", required = false) String keyword,
			@ApiParam(name = "sign") @RequestParam(value = "keyword", required = false) String sign) {
		String str = "{\"result\":[{\"projectExt\":\"\",\"projectId\":\"\",\"children\":[{\"projectExt\":\"透传参数\",\"projectId\":\"fs12NhQ341da79c1e423ca499adc01c459285a790f9c1e6\",\"children\":\"\",\"projectName\":\"庞凤楚测试项目\",\"type\":\"1\"}],\"projectName\":\"一层目录\",\"type\":\"2\"}],\"sign\":\"\",\"appId\":\"111111\",\"status\":\"success\"}";
		return JSON.parseObject(str);
	}

	@PostMapping("/getEvn")
	@ApiOperation(value = "获取项目列表", httpMethod = "POST")
	@Inner(value = false)
	public Object getEvn(
			@ApiParam(name = "appId") @RequestParam(value = "appId", required = false) String appId,
			@ApiParam(name = "projectId") @RequestParam(value = "projectId", required = false) String projectId,
			@ApiParam(name = "sign") @RequestParam(value = "sign", required = false) String sign) {
		String str = "{\"appId\":\"111111\",\"status\":\"success\",\"result\":[{\"envId\":\"1\",\"envName\":\"test\",\"envDesc\":\"这是测试环境\"}],\"sign\":\"\"}";
		return JSON.parseObject(str);
	}

	@PostMapping("/execCase")
	@ApiOperation(value = "执行用例", httpMethod = "POST")
	@Inner(value = false)
	public Object execCase(
			@ApiParam(name = "appId") @RequestParam(value = "appId", required = false) String appId,
			@ApiParam(name = "projectId") @RequestParam(value = "projectId", required = false) String projectId,
			@ApiParam(name = "caseId") @RequestParam(value = "caseId", required = false) String caseId,
			@ApiParam(name = "envId") @RequestParam(value = "envId", required = false) String envId,
			@ApiParam(name = "projectExt") @RequestParam(value = "projectExt", required = false) String projectExt,
			@ApiParam(name = "sign") @RequestParam(value = "sign", required = false) String sign) {

		String url = "http://47.93.237.249/index.php/v2/api_studio/automated_test/test_case/execute";
		Map<String, Object> param = new HashMap(4);
		param.put("space_id", "xfZeKFL8c99b1ce64901c72789fb61bf2e1ec86ed1491a8");
		param.put("project_id", "fs12NhQ341da79c1e423ca499adc01c459285a790f9c1e6");
		param.put("case_id", Integer.parseInt(caseId));
		param.put("env_id", envId);
		String result = HttpRequest.post(url)
				.header("Eo-Secret-Key", "naAlze9f8c648ba06bd24a14ada2def85f087dfbe97b7eb")
				.form(param)
				.timeout(2000)
				.execute().body();

		log.info("执行用例" + result);
//		String str = "{\"app_id\":\"111111\",\"status\":\"success\",\"report_id\":\"308\",\"sign\":\"\"}";
		return JSON.parseObject(result);
	}


	@PostMapping("/getReportDetail")
	@ApiOperation(value = "获取用例报告", httpMethod = "POST")
	@Inner(value = false)
	public Object getReportDetail(
			@ApiParam(name = "appId") @RequestParam(value = "appId", required = false) String appId,
			@ApiParam(name = "projectId") @RequestParam(value = "projectId", required = false) String projectId,
			@ApiParam(name = "reportId") @RequestParam(value = "reportId", required = false) String reportId,
			@ApiParam(name = "projectExt") @RequestParam(value = "projectExt", required = false) String projectExt,
			@ApiParam(name = "sign") @RequestParam(value = "sign", required = false) String sign) {
		String url = "http://47.93.237.249/index.php/v2/api_studio/automated_test/report/get";
		Map<String, Object> param = new HashMap(4);
		param.put("space_id", "xfZeKFL8c99b1ce64901c72789fb61bf2e1ec86ed1491a8");
		param.put("project_id", "fs12NhQ341da79c1e423ca499adc01c459285a790f9c1e6");
		param.put("report_id", reportId);
		param.put("report_type", "test_case");
		String result = HttpRequest.post(url)
				.header("Eo-Secret-Key", "naAlze9f8c648ba06bd24a14ada2def85f087dfbe97b7eb")
				.form(param)
				.timeout(2000)
				.execute().body();
		log.info("获取指定的测试报告详情" + result);
		JSONObject object = JSON.parseObject(result);
		JSONObject result1 = JSON.parseObject(object.getString("result"));
		String str = "{\"appId\":\"\",\"status\":\"success\",\"result\":[{\"reportId\":\"" + reportId + "\",\"reportDownloadUrl\":\"" + result1.getString("report_download_url") + "\",\"testStatus\":\"success\"}],\"sign\":\"\"}";
		log.info("获取指定的测试报告详情str" + str);
		return JSON.parseObject(str);
	}


}
