package co.yixiang.yshop.module.stock.controller.admin.stockchecklist.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 商品盘库详情 Response VO")
@Data
@ExcelIgnoreUnannotated
public class StockCheckListRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("ID")
    private Long id;

    @Schema(description = "盘库主表ID", example = "1")
    @ExcelProperty("盘库主表ID")
    private Long stockStockcheckId;

    @Schema(description = "商品ID", example = "1")
    @ExcelProperty("商品ID")
    private Long stockGoodsId;

    @Schema(description = "商品名称")
    @ExcelProperty("商品名称")
    private String stockGoodsName;

    @Schema(description = "商品规格ID", example = "1")
    @ExcelProperty("商品规格ID")
    private Long stockGoodsValueId;

    @Schema(description = "规格名称")
    @ExcelProperty("规格名称")
    private String stockGoodsValueName;

    @Schema(description = "商品编码")
    @ExcelProperty("商品编码")
    private String goodsCode;

    @Schema(description = "单位名称")
    @ExcelProperty("单位名称")
    private String unitName;

    @Schema(description = "账面数量")
    @ExcelProperty("账面数量")
    private Integer bookNums;

    @Schema(description = "实盘数量")
    @ExcelProperty("实盘数量")
    private Integer actualNums;

    @Schema(description = "盈亏数量(实盘-账面)")
    @ExcelProperty("盈亏数量")
    private Integer diffNums;

    @Schema(description = "盘库商品备注")
    @ExcelProperty("备注")
    private String checkGoodsRemark;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "单据编号")
    @ExcelProperty("单据编号")
    private String docnum;

    @Schema(description = "盘库时间")
    @ExcelProperty("盘库时间")
    private LocalDateTime checkTime;

}
