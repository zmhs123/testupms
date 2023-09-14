package cn.tech.wings.cloud.admin.api.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lixuanxing
 * @date 2022-12-05 09:46:01
 */
@Data
@ApiModel(value = "产品线和产品表Result")
public class SysProductLineResult implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value="id")
    private Long id;


    @ApiModelProperty(value="名称")
    private String name;


    @ApiModelProperty(value="排序")
    private Integer sortOrder;


    @ApiModelProperty(value="类型 0产品线 1产品")
    private Integer productType;


    @ApiModelProperty(value="创建人")
    private String createBy;


    @ApiModelProperty(value="修改人")
    private String updateBy;


    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;


    @ApiModelProperty(value="修改时间")
    private LocalDateTime updateTime;


    @ApiModelProperty(value="删除标记")
    private String delFlag;


    @ApiModelProperty(value="父id")
    private Long parentId;


    @ApiModelProperty(value="部门id")
    private Long deptId;


    @ApiModelProperty(value="部门名称")
    private String deptName;

}
