package cn.tech.wings.cloud.admin.controller;

import cn.tech.wings.cloud.common.log.annotation.SysLog;
import cn.tech.wings.cloud.common.security.annotation.Inner;
import cn.tech.wings.cloud.admin.api.entity.SysProductLine;
import cn.tech.wings.cloud.admin.api.vo.params.SysProductLineParam;
import cn.tech.wings.cloud.admin.service.SysProductLineService;
import cn.tech.wings.cloud.common.core.util.R;
import cn.tech.wings.cloud.common.excel.annotation.ResponseExcel;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产品线和产品表
 *
 * @author lixuanxing
 * @date 2022-12-05 09:46:01
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/productline" )
@Api(value = "productline", tags = "产品线和产品表管理")
public class SysProductLineController {

    private final SysProductLineService sysProductLineService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param sysProductLine 产品线和产品表
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('sys_product_line_page')" )
    public R getSysProductLinePage(Page page, SysProductLineParam sysProductLine) {
		int a = 1;
		for (int i = 0; i < 3; i++) {
			if(i < a){
				log.info("sysProductLine:i:{}", i);
			}else{
				log.info("sysProductLine:else:i:{}", i);
			}
		}
        return R.ok(sysProductLineService.getProductLinePage(page, sysProductLine));
    }


    /**
     * 通过id查询产品线和产品表
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('sys_product_line_view')" )
    public R getById(@PathVariable("id" ) Long id) {
        return R.ok(sysProductLineService.getById(id));
    }

    /**
     * 新增产品线和产品表
     * @param sysProductLine 产品线和产品表
     * @return R
     */
    @ApiOperation(value = "新增产品线和产品表", notes = "新增产品线和产品表")
    @SysLog("新增产品线和产品表" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('sys_product_line_add')" )
    public R save(@RequestBody SysProductLine sysProductLine) {
        return R.ok(sysProductLineService.save(sysProductLine));
    }

    /**
     * 修改产品线和产品表
     * @param sysProductLine 产品线和产品表
     * @return R
     */
    @ApiOperation(value = "修改产品线和产品表", notes = "修改产品线和产品表")
    @SysLog("修改产品线和产品表" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('sys_product_line_edit')" )
    public R updateById(@RequestBody SysProductLine sysProductLine) {
        return R.ok(sysProductLineService.updateSysProductLine(sysProductLine));
    }

    /**
     * 通过id删除产品线和产品表
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除产品线和产品表", notes = "通过id删除产品线和产品表")
    @SysLog("通过id删除产品线和产品表" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('sys_product_line_del')" )
    public R removeById(@PathVariable Long id) {
        return sysProductLineService.RemoveSysProductLine(id);
    }


    /**
     * 导出excel 表格
     * @param sysProductLine 查询条件
     * @return excel 文件流
     */
    @ResponseExcel
    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('sys_product_line_export')" )
    public List<SysProductLine> export(SysProductLine sysProductLine) {
        return sysProductLineService.list(Wrappers.query(sysProductLine));
    }

	/**
	 * 根据部门id获取产品线数据
	 * @param sysProductLine 产品线和产品表
	 * @return
	 */
	@ApiOperation(value = "根据部门id获取产品线数据", notes = "根据部门id获取产品线数据")
	@PostMapping("/getSysProductLineListByDeptId" )
//	@PreAuthorize("@pms.hasPermission('sys_product_line_by_dept_id')" )
	public R getSysProductLineList(@RequestBody SysProductLineParam sysProductLine) {
		return R.ok(sysProductLineService.getSysProductLineList(sysProductLine));
	}

	/**
	 * 根据部门id获取产品线数据
	 * @return
	 */
	@Inner(value = false)
	@ApiOperation(value = "根据部门id获取产品线数据", notes = "根据部门id获取产品线数据")
	@PostMapping("/getSysProductLineListByDeptIdBak" )
//	@PreAuthorize("@pms.hasPermission('sys_product_line_by_dept_id')" )
	public R getSysProductLineListBak() {
		return R.ok(sysProductLineService.getSysProductLineListBak());
	}

	/**
	 * 获取所有部门以及产品线的数据
	 * @return
	 */
	@Inner(value = false)
	@ApiOperation(value = "获取所有部门以及产品线的数据", notes = "获取所有部门以及产品线的数据")
	@PostMapping("/getAllDeptAndSysProductLineList" )
	public R getAllDeptAndSysProductLineList() {
		return sysProductLineService.getAllDeptAndSysProductLineList();
	}
}
