package co.yixiang.yshop.module.stock.dal.dataobject.stockstockoutlist;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import co.yixiang.yshop.framework.mybatis.core.dataobject.BaseDO;

/**
 * 商品出库详情 DO
 *
 * @author yshop
 */
@TableName("yshop_stock_stockout_list")
@KeySequence("yshop_stock_stockout_list_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockStockoutListDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 出库ID
     */
    private Long stockStockoutId;
    /**
     * 商品规格ID
     */
    private Long stockGoodsValueId;
    /**
     * 商品ID
     */
    private Long stockGoodsId;
    /**
     * 出库单个商品数
     */
    private Integer stockOutNums;
    /**
     * 出库价格
     */
    private BigDecimal outboundPrice;
    /**
     * 出库金额
     */
    private BigDecimal amount;
    /**
     * 出库商品备注
     */
    private String stockOutGoodsRemark;

}