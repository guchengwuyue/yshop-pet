package co.yixiang.yshop.module.stock.dal.dataobject.stockstockout;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import co.yixiang.yshop.framework.mybatis.core.dataobject.BaseDO;

/**
 * 商品出库 DO
 *
 * @author yshop
 */
@TableName("yshop_stock_stockout")
@KeySequence("yshop_stock_stockout_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockStockoutDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 出库单编号
     */
    private String docnum;
    /**
     * 出库时间
     */
    private LocalDateTime outboundTime;
    /**
     * 客户ID
     */
    private Long stockCustomerId;
    /**
     * 出库总商品数
     */
    private Integer stockOutTotalNums;
    /**
     * 出库总金额
     */
    private BigDecimal totalOutAmount;
    /**
     * 备注
     */
    private String remark;
    /**
     * 0-未审核 1-已=审核
     */
    private Integer isAudit;
    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

}