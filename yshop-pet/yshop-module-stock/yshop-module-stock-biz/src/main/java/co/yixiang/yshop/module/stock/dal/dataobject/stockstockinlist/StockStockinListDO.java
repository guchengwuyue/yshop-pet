package co.yixiang.yshop.module.stock.dal.dataobject.stockstockinlist;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import co.yixiang.yshop.framework.mybatis.core.dataobject.BaseDO;

/**
 * 商品入库详情 DO
 *
 * @author yshop
 */
@TableName("yshop_stock_stockin_list")
@KeySequence("yshop_stock_stockin_list_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockStockinListDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 入库主表ID
     */
    private Long stockStockinId;
    /**
     * 商品ID
     */
    private Long stockGoodsId;
    /**
     * 商品规格id
     */
    private Long stockGoodsValueId;
    /**
     * 入库数量
     */
    private Integer stockinNums;
    /**
     * 入库价格
     */
    private BigDecimal inboundPrice;
    /**
     * 入库金额
     */
    private BigDecimal amount;
    /**
     * 入库备注
     */
    private String stockinGoodsRemark;


}