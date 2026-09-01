package co.yixiang.yshop.module.stock.controller.admin.stockstockoutlist.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 商品出库详情新增/修改 Request VO")
@Data
public class StockStockoutListSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "21268")
    private Long id;

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

}