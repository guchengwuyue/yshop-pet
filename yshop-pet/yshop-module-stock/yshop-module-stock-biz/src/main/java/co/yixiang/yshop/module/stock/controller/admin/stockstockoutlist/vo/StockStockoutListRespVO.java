package co.yixiang.yshop.module.stock.controller.admin.stockstockoutlist.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 商品出库详情 Response VO")
@Data
@ExcelIgnoreUnannotated
public class StockStockoutListRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "21268")
    @ExcelProperty("ID")
    private Long id;

    @Schema(description = "出库ID", example = "3551")
    @ExcelProperty("出库ID")
    private Long stockStockoutId;

    @Schema(description = "商品规格ID", example = "11918")
    @ExcelProperty("商品规格ID")
    private Long stockGoodsValueId;

    @Schema(description = "商品规格名称", example = "商品规格名称")
    @ExcelProperty("商品规格名称")
    private String stockGoodsValueName;

    @Schema(description = "商品ID", example = "4749")
    @ExcelProperty("商品ID")
    private Long stockGoodsId;

    @Schema(description = "商品名称", example = "商品名称")
    @ExcelProperty("商品名称")
    private String stockGoodsName;

    @Schema(description = "商品编码", example = "商品编码")
    @ExcelProperty("商品编码")
    private String goodsCode;

    @Schema(description = "单位名称", example = "件")
    @ExcelProperty("单位名称")
    private String unitName;

    @Schema(description = "出库单个商品数")
    @ExcelProperty("出库单个商品数")
    private Integer stockOutNums;

    @Schema(description = "出库价格", example = "24554")
    @ExcelProperty("出库价格")
    private BigDecimal outboundPrice;

    @Schema(description = "出库金额")
    @ExcelProperty("出库金额")
    private BigDecimal amount;

    @Schema(description = "出库商品备注", example = "你说的对")
    @ExcelProperty("出库商品备注")
    private String stockOutGoodsRemark;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "单据编号", example = "CKD202608070001")
    @ExcelProperty("单据编号")
    private String docNumber;

    @Schema(description = "客户名称", example = "客户名称")
    @ExcelProperty("客户名称")
    private String customerName;

    @Schema(description = "客户ID", example = "21593")
    @ExcelProperty("客户ID")
    private Long customerId;

    @Schema(description = "出库时间")
    @ExcelProperty("出库时间")
    private LocalDateTime outboundTime;

}
