package co.yixiang.yshop.module.stock.dal.dataobject.stockstockin;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import co.yixiang.yshop.framework.mybatis.core.dataobject.BaseDO;

/**
 * 商品入库 DO
 *
 * @author yshop
 */
@TableName("yshop_stock_stockin")
@KeySequence("yshop_stock_stockin_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockStockinDO extends BaseDO {

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
     * 供应商ID
     */
    private Long stockSupplierId;
    /**
     * 入库时间
     */
    private LocalDateTime inboundTime;
    /**
     * 入库商品总数
     */
    private Integer stockinTotalNums;
    /**
     * 单据总金额
     */
    private BigDecimal totalAmount;
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
    /**
     * 0-未结算  1-已结算
     */
    private Integer isSettle;
    /**
     * 结算时间
     */
    private LocalDateTime settleTime;

}