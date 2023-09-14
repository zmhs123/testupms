package cn.tech.wings.cloud.res.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;

/**
 * @author xinglx
 * @Title: mall-cloud
 * @Package com.wing.mall.cloud.account.modular.entity
 * @Description:解决幂等性事务表
 * @date 2019/11/5 16:46
 */
@Data
@TableName("res_tx_no")
public class ResTxNo extends Model<ResTxNo> {
    private static final long serialVersionUID = 7517838459802313674L;

    /**
     * 幂等性字符串
     */
    @TableId(value = "tx_no")
    private String txNo;

    /**
     * 创建时间
     */
    @TableField("add_time")
    private Date addTime;
}
