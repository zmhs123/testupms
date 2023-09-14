package cn.tech.wings.cloud.res.controller;

import cn.tech.wings.cloud.common.core.util.R;
import cn.tech.wings.cloud.res.service.WaterMarkService;
import cn.tech.wings.cloud.res.api.entity.WaterMark;
import cn.tech.wings.cloud.res.api.model.params.WaterMarkParam;
import cn.tech.wings.cloud.res.api.model.result.WaterMarkResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 控制器
 *
 * @author fengchu_pang
 * @Date 2020-11-20 17:07:40
 */
@Api(value="管理",tags={"水印相关接口"})
@RestController
@AllArgsConstructor
@RequestMapping("/water-mark")
public class WaterMarkController {

    private final WaterMarkService waterMarkService;

    /**
     * 新增接口
     *
     * @author fengchu_pang
     * @Date 2020-11-20
     */
    @ApiOperation("添加水印接口")
    @ApiImplicitParams({
        @ApiImplicitParam(name="waterMarkParam",value="实体类",dataType="WaterMarkParam", paramType = "query",example="xxx"),
    })
    @ResponseBody
    @PostMapping
	@PreAuthorize("@pms.hasPermission('res_water_mark_add')")
    public R addItem(@RequestBody WaterMarkParam waterMarkParam) {
		waterMarkParam.setIsAdmin(1);
//		waterMarkParam.setStoreId(null);
        this.waterMarkService.add(waterMarkParam);
        return R.ok();
    }

    /**
     * 编辑接口
     *
     * @author fengchu_pang
     * @Date 2020-11-20
     */
    @ApiOperation("编辑水印接口")
    @ApiImplicitParams({
        @ApiImplicitParam(name="waterMarkParam",value="实体类",dataType="WaterMarkParam", paramType = "query",example="xxx"),
    })
    @ResponseBody
    @PutMapping
	@PreAuthorize("@pms.hasPermission('res_water_mark_edit')")
    public R editItem(@RequestBody WaterMarkParam waterMarkParam) {
//        if(waterMarkParam.getStoreId()!=null){
//            waterMarkParam.setStoreId(null);
//        }
        this.waterMarkService.update(waterMarkParam);
        return R.ok();
    }

    /**
     * 删除接口
     *
     * @author fengchu_pang
     * @Date 2020-11-20
     */
    @ApiOperation("删除水印接口")
    @ApiImplicitParams({
        @ApiImplicitParam(name="waterMarkParam",value="实体类",dataType="WaterMarkParam", paramType = "query",example="xxx"),
    })
    @ResponseBody
    @DeleteMapping
	@PreAuthorize("@pms.hasPermission('res_water_mark_delete')")
    public R delete(WaterMarkParam waterMarkParam) {
        this.waterMarkService.delete(waterMarkParam);
        return R.ok();
    }

    /**
     * 查看详情接口
     *
     * @author fengchu_pang
     * @Date 2020-11-20
     */
    @ApiOperation("查看水印详情接口")
    @ApiImplicitParams({
        @ApiImplicitParam(name="waterMarkParam",value="实体类",dataType="WaterMarkParam", paramType = "query",example="xxx"),
    })
    @ResponseBody
    @GetMapping
	@PreAuthorize("@pms.hasPermission('res_water_mark_detail')")
    public R<WaterMark> detail() {
        List<WaterMark> detail = this.waterMarkService.getMgrWaterMark();
        if(detail.size()>0){
            return R.ok(detail.get(0));
        }else {
            return R.failed();
        }

    }

    /**
     * 查询列表
     *
     * @author fengchu_pang
     * @Date 2020-11-20
     */
    @ApiOperation("查询水印列表接口")
    @ApiImplicitParams({
        @ApiImplicitParam(name="waterMarkParam",value="实体类",dataType="WaterMarkParam", paramType = "query",example="xxx"),
    })
    @ResponseBody
    @GetMapping("/list")
	@PreAuthorize("@pms.hasPermission('res_water_mark_list')")
    public R<IPage<WaterMarkResult>> list(Page page, WaterMarkParam waterMarkParam) {
        return R.ok(this.waterMarkService.findPageBySpec(page,waterMarkParam));
    }

}


