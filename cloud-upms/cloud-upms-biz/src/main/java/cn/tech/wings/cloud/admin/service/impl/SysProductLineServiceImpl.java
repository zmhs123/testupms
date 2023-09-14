package cn.tech.wings.cloud.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.tech.wings.cloud.admin.api.entity.SysDept;
import cn.tech.wings.cloud.admin.api.entity.SysProductLine;
import cn.tech.wings.cloud.admin.api.vo.params.SysProductLineParam;
import cn.tech.wings.cloud.admin.mapper.SysProductLineMapper;
import cn.tech.wings.cloud.admin.service.SysDeptService;
import cn.tech.wings.cloud.admin.service.SysProductLineService;
import cn.tech.wings.cloud.common.core.constant.CommonConstants;
import cn.tech.wings.cloud.common.core.util.R;
import cn.tech.wings.cloud.common.data.datascope.DataScope;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 产品线和产品表
 *
 * @author lixuanxing
 * @date 2022-12-05 09:46:01
 */
@Slf4j
@Service
@AllArgsConstructor
public class SysProductLineServiceImpl extends ServiceImpl<SysProductLineMapper, SysProductLine> implements SysProductLineService {

	private final SysProductLineMapper sysProductLineMapper;
	private final SysDeptService sysDeptService;
	@Override
	public R getSysProductLineList(SysProductLineParam sysProductLine) {
		Long deptId = sysProductLine.getDeptId();
		if (deptId == null) {
			return R.failed("部门id不能为空");
		}
		log.info("部门id:{}", deptId);
		List<SysProductLine> sysProductLines = sysProductLineMapper.selectList(
				Wrappers.<SysProductLine>lambdaQuery()
						.eq(SysProductLine::getDeptId, deptId)
						.eq(SysProductLine::getProductType, 0)
						.orderByDesc(SysProductLine::getCreateTime)
		);
		log.info("产品线:{}", sysProductLines);
		JSONObject jsonObject = new JSONObject();
		// 产品线
		JSONArray productLines = new JSONArray();
		for (SysProductLine productLine : sysProductLines) {
			JSONObject productLineJsonObject = new JSONObject();
			productLineJsonObject.put("id", productLine.getId());
			productLineJsonObject.put("name", productLine.getName());


			List<SysProductLine> sysProducts = sysProductLineMapper.selectList(
					Wrappers.<SysProductLine>lambdaQuery()
							.eq(SysProductLine::getParentId, productLine.getId())
							.eq(SysProductLine::getProductType, 1)
							.orderByDesc(SysProductLine::getCreateTime)
			);
			log.info("产品:{}", sysProductLines);
			// 产品
			JSONArray products = new JSONArray();
			for (SysProductLine sysProduct : sysProducts) {
				JSONObject productJsonObject = new JSONObject();
				productJsonObject.put("id", sysProduct.getId());
				productJsonObject.put("name", sysProduct.getName());
				products.add(productJsonObject);
			}
			productLineJsonObject.put("projects", products);
			productLines.add(productLineJsonObject);
		}
		jsonObject.put("products", productLines);
		return R.ok(jsonObject);
	}

	@Override
	public R getSysProductLineListBak() {
		/*
		[
			{
				"name": "测试-效率工程部",
				"id": "D0036",
				"defaultDept": true,
				"show": true,
				"products": [
					{
						"name": "KIM",
						"id": "PD333999",
						"projects": [
							{
								"id": "K1",
								"name": "khaos"
							}
						]
					}
				]
			}
		]
		*/
		JSONObject jsonObject = new JSONObject();
		// 产品线
		JSONArray productLines = new JSONArray();
		JSONObject productLineJsonObject = new JSONObject();
		productLineJsonObject.put("id", "PD333999");
		productLineJsonObject.put("name", "KIM");


		// 产品
		JSONArray products = new JSONArray();
		JSONObject productJsonObject = new JSONObject();
		productJsonObject.put("id", "K1");
		productJsonObject.put("name", "khaos");
		products.add(productJsonObject);

		productLineJsonObject.put("projects", products);
		productLines.add(productLineJsonObject);
		jsonObject.put("products", productLines);
		return R.ok(jsonObject);
	}

	@Override
	public R getAllDeptAndSysProductLineList() {
		List<SysDept> list = sysDeptService.list(Wrappers.<SysDept>lambdaQuery()
				.eq(SysDept::getDelFlag, CommonConstants.STATUS_NORMAL)
				.orderByDesc(SysDept::getCreateTime));
		JSONArray jsonArray = new JSONArray();
		for (SysDept sysDept : list) {
			JSONObject sysDeptJsonObject = new JSONObject();
			sysDeptJsonObject.put("id", sysDept.getDeptId().toString());
			sysDeptJsonObject.put("name", sysDept.getName());
			JSONArray products = new JSONArray();
			List<SysProductLine> sysProductLines = sysProductLineMapper.selectList(
					Wrappers.<SysProductLine>lambdaQuery()
							.eq(SysProductLine::getDeptId, sysDept.getDeptId())
							.eq(SysProductLine::getProductType, 0)
							.orderByDesc(SysProductLine::getCreateTime)
			);
			log.info("产品线:{}", sysProductLines);
			for (SysProductLine productLine : sysProductLines) {
				JSONObject productLineJsonObject = new JSONObject();
				productLineJsonObject.put("id", productLine.getId().toString());
				productLineJsonObject.put("name", productLine.getName());
				productLineJsonObject.put("icon", "");


				List<SysProductLine> sysProducts = sysProductLineMapper.selectList(
						Wrappers.<SysProductLine>lambdaQuery()
								.eq(SysProductLine::getParentId, productLine.getId())
								.eq(SysProductLine::getProductType, 1)
								.orderByDesc(SysProductLine::getCreateTime)
				);
				log.info("产品:{}", sysProductLines);
				// 产品
				JSONArray projects = new JSONArray();
				for (SysProductLine sysProduct : sysProducts) {
					JSONObject projectJsonObject = new JSONObject();
					projectJsonObject.put("id", sysProduct.getId().toString());
					projectJsonObject.put("name", sysProduct.getName());
					projectJsonObject.put("projectId", sysProduct.getId().toString());
					projectJsonObject.put("projectName", sysProduct.getName());
					projects.add(projectJsonObject);
				}
				productLineJsonObject.put("projects", projects);
				products.add(productLineJsonObject);
			}

			sysDeptJsonObject.put("products", products);
			sysDeptJsonObject.put("show", true);
			sysDeptJsonObject.put("defaultDept", true);
			jsonArray.add(sysDeptJsonObject);
		}
		return R.ok(jsonArray);
	}

	@Override
	public IPage getProductLinePage(Page page, SysProductLineParam sysProductLineParam) {
		return sysProductLineMapper.getProductLinePage(page, sysProductLineParam, DataScope.of());
	}

	@Override
	public boolean updateSysProductLine(SysProductLine sysProductLine) {
		int updateById = sysProductLineMapper.updateById(sysProductLine);
		Integer productType = sysProductLine.getProductType();
		if (productType.intValue() == 0) {
			// 是产品线  更新下面的产品的部门id
			List<SysProductLine> sysProducts = sysProductLineMapper.selectList(
					Wrappers.<SysProductLine>lambdaQuery()
							.eq(SysProductLine::getParentId, sysProductLine.getId())
							.eq(SysProductLine::getProductType, 1)
			);
			for (SysProductLine sysProduct : sysProducts) {
				sysProduct.setDeptId(sysProductLine.getDeptId());
				sysProductLineMapper.updateById(sysProduct);
			}
		}
		return SqlHelper.retBool(updateById);
	}

	@Override
	public R RemoveSysProductLine(Long id) {
		if (id == null) {
			return R.failed("数据不存在");
		}
		SysProductLine sysProductLine = sysProductLineMapper.selectById(id);
		if (sysProductLine == null) {
			return R.failed("数据不存在");
		}
		Integer productType = sysProductLine.getProductType();
		if (productType.intValue() == 0) {
			// 是产品线  需要看下下面有没有产品
			List<SysProductLine> sysProducts = sysProductLineMapper.selectList(
					Wrappers.<SysProductLine>lambdaQuery()
							.eq(SysProductLine::getParentId, sysProductLine.getId())
							.eq(SysProductLine::getProductType, 1)
			);
			if (CollectionUtil.isNotEmpty(sysProducts)) {
				return R.failed("当前产品线下还有产品不能删除");
			}
		}else{

		}
		int deleteById = sysProductLineMapper.deleteById(id);
		return R.ok(deleteById);
	}
}
