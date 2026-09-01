package co.yixiang.yshop.module.stock.dal.dataobject.stockcheck;

import lombok.*;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import co.yixiang.yshop.framework.mybatis.core.dataobject.BaseDO;

/**
 * 商品盘库 DO
 *
 * @author yshop
 */
@TableName("yshop_stock_stockcheck")
@KeySequence("yshop_stock_stockcheck_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockCheckDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 单据编号
     */
    private String docnum;
    /**
     * 盘库时间
     */
    private LocalDateTime checkTime;
    /**
     * 盘点商品总数
     */
    private Integer checkTotalNums;
    /**
     * 盘盈合计
     */
    private Integer profitNums;
    /**
     * 盘亏合计
     */
    private Integer lossNums;
    /**
     * 备注
     */
    private String remark;
    /**
     * 0-未审核 1-已审核
     */
    private Integer isAudit;
    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

}
