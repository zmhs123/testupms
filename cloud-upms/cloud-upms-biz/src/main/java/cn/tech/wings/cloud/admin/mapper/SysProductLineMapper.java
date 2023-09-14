package cn.tech.wings.cloud.admin.mapper;

import cn.tech.wings.cloud.admin.api.entity.SysProductLine;
import cn.tech.wings.cloud.admin.api.vo.params.SysProductLineParam;
import cn.tech.wings.cloud.admin.api.vo.result.SysProductLineResult;
import cn.tech.wings.cloud.common.data.datascope.CloudBaseMapper;
import cn.tech.wings.cloud.common.data.datascope.DataScope;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 产品线和产品表
 *
 * @author lixuanxing
 * @date 2022-12-05 09:46:01
 */
@Mapper
public interface SysProductLineMapper extends CloudBaseMapper<SysProductLine> {
	/**
	 * 分页查询用户信息（含角色）
	 * @param page 分页
	 * @param sysProductLineParam 查询参数
	 * @param dataScope
	 * @return list
	 */
	IPage<SysProductLineResult> getProductLinePage(Page page, @Param("query") SysProductLineParam sysProductLineParam, DataScope dataScope);
}
