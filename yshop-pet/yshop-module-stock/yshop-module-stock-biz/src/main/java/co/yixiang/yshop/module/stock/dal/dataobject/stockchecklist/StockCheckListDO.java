package co.yixiang.yshop.module.stock.dal.dataobject.stockchecklist;

import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import co.yixiang.yshop.framework.mybatis.core.dataobject.BaseDO;

/**
 * 商品盘库详情 DO
 *
 * @author yshop
 */
@TableName("yshop_stock_stockcheck_list")
@KeySequence("yshop_stock_stockcheck_list_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockCheckListDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 盘库主表ID
     */
    private Long stockStockcheckId;
    /**
     * 商品ID
     */
    private Long stockGoodsId;
    /**
     * 商品规格ID
     */
    private Long stockGoodsValueId;
    /**
     * 账面数量
     */
    private Integer bookNums;
    /**
     * 实盘数量
     */
    private Integer actualNums;
    /**
     * 盈亏数量(实盘-账面)
     */
    private Integer diffNums;
    /**
     * 盘库商品备注
     */
    private String checkGoodsRemark;

}
