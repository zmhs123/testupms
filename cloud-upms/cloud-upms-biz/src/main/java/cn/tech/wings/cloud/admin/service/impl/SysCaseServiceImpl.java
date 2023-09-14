package cn.tech.wings.cloud.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.tech.wings.cloud.admin.api.entity.SysCase;
import cn.tech.wings.cloud.admin.api.entity.SysFile;
import cn.tech.wings.cloud.admin.service.SysCaseService;
import cn.tech.wings.cloud.admin.mapper.SysCaseMapper;
import cn.tech.wings.cloud.admin.service.SysFileService;
import cn.tech.wings.cloud.common.security.util.SecurityUtils;
import cn.tech.wings.cloud.admin.api.feign.RemoteAccurateApiService;
import cn.tech.wings.cloud.admin.api.vo.*;
import cn.tech.wings.cloud.admin.enums.CaseStatusEnum;
import cn.tech.wings.cloud.admin.enums.ImportTypeEnum;
import cn.tech.wings.cloud.common.core.exception.ErrorCodes;
import cn.tech.wings.cloud.common.core.util.MsgUtils;
import cn.tech.wings.cloud.common.core.util.R;
import cn.tech.wings.cloud.common.excel.vo.ErrorMessage;
import cn.tech.wings.cloud.common.file.core.FileProperties;
import cn.tech.wings.cloud.common.file.core.FileTemplate;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.list.TreeList;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用例表
 *
 * @author lixuan_xing
 * @date 2022-12-12 17:19:11
 */
@Slf4j
@Service
@AllArgsConstructor
public class SysCaseServiceImpl extends ServiceImpl<SysCaseMapper, SysCase> implements SysCaseService {
	private final SysCaseMapper sysCaseMapper;
	private final FileTemplate fileTemplate;

	private final FileProperties properties;

	private final SysFileService sysFileService;

	private final RemoteAccurateApiService remoteAccurateApiService;

	public static final String ROOT_NODE = "$root$";

	@Override
	public R startRecording(SysCase sysCase) {
		if (sysCase.getId() == null) {
			return R.failed("用例id不能为空");
		}
		sysCase.setRecordingTime(LocalDateTime.now());
		sysCase.setStatus(CaseStatusEnum.LZZ.getValue());
		int i = sysCaseMapper.updateById(sysCase);
		return R.ok(i);
	}

	@Override
	public R endRecording(SysCase sysCase) {
		if (sysCase.getId() == null) {
			return R.failed("用例id不能为空");
		}
		sysCase.setEndRecordingTime(LocalDateTime.now());
		sysCase.setStatus(CaseStatusEnum.YLZ.getValue());
		int i = sysCaseMapper.updateById(sysCase);
		return R.ok(i);
	}

	@Override
	public R getCasePathTree(SysCase sysCase, String condition) {
		LambdaQueryWrapper<SysCase> objectLambdaQueryWrapper = Wrappers.lambdaQuery();
		objectLambdaQueryWrapper
				.like(StrUtil.isNotBlank(sysCase.getName()), SysCase::getName, sysCase.getName())
				.like(StrUtil.isNotBlank(sysCase.getCaseCode()), SysCase::getCaseCode, sysCase.getCaseCode())
				.eq(sysCase.getProjectId() != null, SysCase::getProjectId, sysCase.getProjectId())
				.eq(sysCase.getStatus() != null, SysCase::getStatus, sysCase.getStatus())
				.orderByDesc(SysCase::getCreateTime);
		List<SysCase> sysCaseList = list(objectLambdaQueryWrapper);
		if (CollUtil.isEmpty(sysCaseList)) {
			return R.ok(new SysCaseTreeVO(new ArrayList<SysCaseTreeNode>(0), new TreeSet<String>()));
		}
		String ignoreCondition = condition.toLowerCase();
		boolean searchFlag = StringUtils.isNotEmpty(ignoreCondition);
		SysCaseTreeNode rootNode = new SysCaseTreeNode(ROOT_NODE, "root", "/", null);
		DyedSysCaseTreeNode dyedSysCaseTreeNode = new DyedSysCaseTreeNode(rootNode, false);
		List<DyedSysCaseTreeNode> dyedSysCaseTreeNodes = new TreeList<>();
		TreeSet<String> hitNodes = new TreeSet<>();
		dyedSysCaseTreeNodes.add(dyedSysCaseTreeNode);
		sysCaseList.forEach(item -> {
			String path = item.getPath();
			if (StrUtil.isNotBlank(path)) {
				String[] split = path.split("/");
				for (int i = 0; i < split.length; i++) {
					SysCaseTreeNode node = null;
					if (i == 0) {
						node = new SysCaseTreeNode(split[i], split[i], ROOT_NODE, null);
					} else {
						node = new SysCaseTreeNode(split[i], split[i], split[i - 1], null);
					}
					if (searchFlag && split[i].toLowerCase().contains(ignoreCondition)) {
						hitNodes.add(split[i]);
					}
					dyedSysCaseTreeNodes.add(
							new DyedSysCaseTreeNode(node, split[i].toLowerCase().contains(ignoreCondition)));
				}
				/*SysCaseTreeNode caseNode =
						new SysCaseTreeNode(item.getCaseCode(), StringUtils.isNotBlank(item.getName()) ? item.getName() : item.getCaseCode(), split[split.length - 1], null);
				if (searchFlag && item.getCaseCode().toLowerCase().contains(ignoreCondition)) {
					hitNodes.add(item.getCaseCode());
				}
				dyedSysCaseTreeNodes.add(
						new DyedSysCaseTreeNode(caseNode, item.getCaseCode().toLowerCase().contains(ignoreCondition)));*/
			}
		});
		// 去重
		List<DyedSysCaseTreeNode> distinctResultList = removeDyedDuplicateNode(dyedSysCaseTreeNodes);

		Map<String, List<DyedSysCaseTreeNode>> groupResult =
				distinctResultList.stream().collect(Collectors.groupingBy(item -> item.getNode().getParentNode()));

		DyedTmpResult dyedTmpResult = getTreeNodeByNodeMapWithCondition(groupResult, ROOT_NODE, false);
		return R.ok(new SysCaseTreeVO(
				dyedTmpResult.isDyedResult() ? dyedTmpResult.getChildNodes() : new ArrayList<>(0), hitNodes));
	}

	private DyedTmpResult getTreeNodeByNodeMapWithCondition(Map<String, List<DyedSysCaseTreeNode>> nodeMap,
															String parentNode, boolean parentDyedResult) {
//		log.info("nodeMap:{}", nodeMap);
//		log.info("parentNode:{}", parentNode);
//		log.info("parentDyedResult:{}", parentDyedResult);
		// 从上往下传递 染色结果 node.isDyedResult() | parentDyedResult
		List<DyedSysCaseTreeNode> childTreeNodes = nodeMap.get(parentNode);
		// 这里是给叶子结点做返回
		if (null == childTreeNodes) {
			// 必须到这里才能向上传递最终染色结果
			return new DyedTmpResult(null, parentDyedResult);
		}
		boolean dyedFlag = false;
		List<SysCaseTreeNode> dyedChildNodes = null;
		for (DyedSysCaseTreeNode node : childTreeNodes) {
			// 从上往下传递 染色结果，与当前自己的染色结果 逻辑或处理 ，得到最终的染色结果
			boolean dyedResult = node.isDyedResult() | parentDyedResult;
			if (nodeMap != null && node.getNode() != null && StrUtil.isNotBlank(node.getNode().getNode())) {
				DyedTmpResult dyedTmpResult =
						getTreeNodeByNodeMapWithCondition(nodeMap, node.getNode().getNode(), dyedResult);
				if (dyedTmpResult.isDyedResult()) {
					// 根据下层返回的染色结果决定拼接策略 返回的染色结果为true，将染色标记为true的结点放入childNodes中
					if (null == dyedChildNodes) {
						dyedChildNodes = new TreeList<>();
					}
					SysCaseTreeNode currNode = node.getNode();
					currNode.setChildNodes(dyedTmpResult.getChildNodes());
					dyedChildNodes.add(currNode);
				}
				//从下往上传递染色结果
				dyedFlag |= dyedTmpResult.isDyedResult();
			}
		}
		// 这里是给非叶子结点做返回
		return new DyedTmpResult(dyedChildNodes, dyedFlag);
	}

	private List<DyedSysCaseTreeNode> removeDyedDuplicateNode(List<DyedSysCaseTreeNode> relationTreeNodes) {
		if (CollectionUtils.isEmpty(relationTreeNodes)) {
			return relationTreeNodes;
		}
		List<DyedSysCaseTreeNode> distinctResultList = relationTreeNodes.stream()
				.collect(
						Collectors.collectingAndThen(
								Collectors.toCollection(
										() -> new TreeSet<DyedSysCaseTreeNode>(
												Comparator.comparing(
														o -> o.getNode().getParentNode() + "/" + o.getNode().getNode())
										)
								), TreeList::new
						)
				);
		return distinctResultList;
	}

	@Override
	@SneakyThrows
	public R importCase(String projectId, Integer importType, List<CaseExcelVO> excelVOList, BindingResult bindingResult) {
		if (StrUtil.isBlank(projectId)) {
			return R.failed("应用id不能为空");
		}
		if (importType == null) {
			return R.failed("导入类型不能为空");
		}
		// 通用校验获取失败的数据
		List<ErrorMessage> errorMessageList = (List<ErrorMessage>) bindingResult.getTarget();

		List<SysCase> sysCaseList = list();
		List<CaseExcelVO> sysCaseUpdateList = new ArrayList<>();
		List<CaseExcelResultVO> caseExcelResultVOList = new ArrayList<>();


		// 执行数据插入操作 组装 UserDto
		for (CaseExcelVO excel : excelVOList) {
			CaseExcelResultVO caseExcelResultVO = new CaseExcelResultVO();
			BeanUtils.copyProperties(excel, caseExcelResultVO);
			Set<String> errorMsg = new HashSet<>();
			if (StringUtils.isBlank(excel.getCaseCode())) {
				errorMsg.add(MsgUtils.getMessage(ErrorCodes.SYS_CASE_IMPORT_CODE_EMPTY));
				// 数据不合法情况
				errorMessageList.add(new ErrorMessage(excel.getLineNum(), errorMsg));
				caseExcelResultVO.setErrorMsg(MsgUtils.getMessage(ErrorCodes.SYS_CASE_IMPORT_CODE_EMPTY));
			} else {
				if (excel.getSortOrder() == null) {
					excel.setSortOrder(4);
				}
				// 校验用例编码是否存在
				boolean exsitCaseCode = sysCaseList.stream()
						.anyMatch(sysCase -> excel.getCaseCode().equals(sysCase.getCaseCode())
								&& projectId.equals(sysCase.getProjectId().toString()));
				if (exsitCaseCode) {
					errorMsg.add(MsgUtils.getMessage(ErrorCodes.SYS_CASE_USERNAME_EXISTING, excel.getCaseCode()));
					// 数据不合法情况
					errorMessageList.add(new ErrorMessage(excel.getLineNum(), errorMsg));
					sysCaseUpdateList.add(excel);
					if (importType.intValue() == ImportTypeEnum.FG.getValue()) {
						caseExcelResultVO.setErrorMsg(ImportTypeEnum.FG.getDescription());
					} else {
						caseExcelResultVO.setErrorMsg(ImportTypeEnum.TG.getDescription());
					}
				} else {
					SysCase sysCase = new SysCase();
					sysCase.setName(excel.getName());
					sysCase.setProjectId(Long.parseLong(projectId));
					sysCase.setStatus(CaseStatusEnum.WLZ.getValue());
					sysCase.setCaseRemark(excel.getCaseRemark());
					sysCase.setCaseCode(excel.getCaseCode());
					sysCase.setPath(excel.getPath());
					sysCase.setSortOrder(excel.getSortOrder());
					sysCase.setCaseTestCode(excel.getCaseTestCode());
					sysCase.setPreconditions(excel.getPreconditions());
					sysCase.setTestData(excel.getTestData());
					sysCase.setExpectResult(excel.getExpectResult());
					sysCase.setCaseType(excel.getCaseType());
					sysCase.setPositive2negative(excel.getPositive2negative());
					sysCase.setCreateUser(excel.getCreateUser());
					sysCase.setRemarks(excel.getRemarks());
					save(sysCase);
				}
			}
			caseExcelResultVOList.add(caseExcelResultVO);
		}
		// 制作新文件返回给前端文件地址
		String fileName = IdUtil.simpleUUID() + StrUtil.DOT + "xlsx";
		File tempDirectory = FileUtils.getTempDirectory();
		File resultFile = new File(tempDirectory, fileName);
		EasyExcel.write(resultFile.getAbsolutePath(), CaseExcelResultVO.class).sheet("导入结果").doWrite(caseExcelResultVOList);
		MultipartFile multipartFile = new MockMultipartFile("file", resultFile.getName(), "application/vnd.ms-excel", Files.newInputStream(resultFile.toPath()));
		fileTemplate.putObject(properties.getBucketName(), fileName, multipartFile.getInputStream(), multipartFile.getContentType());

		SysFile sysFile = new SysFile();
		sysFile.setFileName(fileName);
		sysFile.setOriginal(multipartFile.getOriginalFilename());
		sysFile.setFileSize(multipartFile.getSize());
		sysFile.setType(FileUtil.extName(multipartFile.getOriginalFilename()));
		sysFile.setBucketName(properties.getBucketName());
		sysFileService.save(sysFile);

		Map<String, String> resultMap = new HashMap<>(4);
		resultMap.put("bucketName", properties.getBucketName());
		resultMap.put("fileName", fileName);
		resultMap.put("url", String.format("/admin/sys-file/%s/%s", properties.getBucketName(), fileName));

		// 覆盖的情况去统计更新,并且需要删除用例对应的追溯关系
		if (importType.intValue() == ImportTypeEnum.FG.getValue()) {
			if (CollUtil.isNotEmpty(sysCaseUpdateList)) {
				List<String> ids = new ArrayList<>();
				for (CaseExcelVO caseExcelVO : sysCaseUpdateList) {
					String caseCode = caseExcelVO.getCaseCode();
					LambdaUpdateWrapper<SysCase> objectLambdaUpdateWrapper = Wrappers.lambdaUpdate();
					objectLambdaUpdateWrapper
							.eq(StrUtil.isNotBlank(caseExcelVO.getCaseCode()), SysCase::getCaseCode, caseExcelVO.getCaseCode())
							.eq(SysCase::getProjectId, projectId)
							.set(SysCase::getProjectId, projectId)
							.set(SysCase::getUpdateBy, SecurityUtils.getUser().getUsername())
							.set(SysCase::getUpdateTime, LocalDateTime.now())
							.set(SysCase::getPath, caseExcelVO.getPath())
							.set(SysCase::getSortOrder, caseExcelVO.getSortOrder())
							.set(SysCase::getRecordingTime, null)
							.set(SysCase::getEndRecordingTime, null)
							//.set(SysCase::getStatus, CaseStatusEnum.WLZ.getValue())
							.set(StrUtil.isNotBlank(caseExcelVO.getName()), SysCase::getName, caseExcelVO.getName())
							.set(StrUtil.isNotBlank(caseExcelVO.getCaseRemark()), SysCase::getCaseRemark, caseExcelVO.getCaseRemark());
					update(objectLambdaUpdateWrapper);
					ids.add(caseCode);
				}

				// 删除用例对应的追溯关系
				Map<String, Object> map = new HashMap<>();
				map.put("ids", ids);
				map.put("projectId", projectId);
				remoteAccurateApiService.removeByCaseIds(map);
			}
		}
		return R.ok(resultMap);
	}
}
