package cn.tech.wings.cloud.admin.service;

import cn.tech.wings.cloud.admin.api.entity.SysProductLine;
import cn.tech.wings.cloud.admin.api.vo.params.SysProductLineParam;
import cn.tech.wings.cloud.common.core.util.R;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 产品线和产品表
 *
 * @author lixuanxing
 * @date 2022-12-05 09:46:01
 */
public interface SysProductLineService extends IService<SysProductLine> {

	R getSysProductLineList(SysProductLineParam sysProductLine);
	R getSysProductLineListBak();

	/**
	 * 获取所有部门以及产品线的数据
	 * @return
	 */
	R getAllDeptAndSysProductLineList();

	/**
	 * 分页查询用户信息（含有角色信息）
	 * @param page 分页对象
	 * @param sysProductLineParam 参数列表
	 * @return
	 */
	IPage getProductLinePage(Page page, SysProductLineParam sysProductLineParam);

	/**
	 * 更新产品线和 产品
	 * @param sysProductLine
	 * @return
	 */
	boolean updateSysProductLine(SysProductLine sysProductLine);

	/**
	 * 删除产品线和 产品
	 * @param id
	 * @return
	 */
	R RemoveSysProductLine(Long id);
}
