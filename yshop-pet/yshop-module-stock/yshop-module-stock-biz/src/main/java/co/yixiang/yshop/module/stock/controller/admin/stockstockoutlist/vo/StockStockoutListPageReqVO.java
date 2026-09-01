package co.yixiang.yshop.module.stock.controller.admin.stockstockoutlist.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import co.yixiang.yshop.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static co.yixiang.yshop.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 商品出库详情分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StockStockoutListPageReqVO extends PageParam {

    @Schema(description = "出库ID", example = "3551")
    private Long stockStockoutId;

    @Schema(description = "商品规格ID", example = "11918")
    private Long stockGoodsValueId;

    @Schema(description = "商品ID", example = "4749")
    private Long stockGoodsId;

    @Schema(description = "出库单个商品数")
    private Integer stockOutNums;

    @Schema(description = "出库价格", example = "24554")
    private BigDecimal outboundPrice;

    @Schema(description = "出库金额")
    private BigDecimal amount;

    @Schema(description = "出库商品备注", example = "你说的对")
    private String stockOutGoodsRemark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "单据编号", example = "CKD202608070001")
    private String docNumber;

    @Schema(description = "客户名称", example = "客户名称")
    private String customerName;

    @Schema(description = "出库时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] outboundTime;

    @Schema(description = "商品名称", example = "商品名称")
    private String stockGoodsName;

    @Schema(description = "商品编码", example = "商品编码")
    private String goodsCode;

}
