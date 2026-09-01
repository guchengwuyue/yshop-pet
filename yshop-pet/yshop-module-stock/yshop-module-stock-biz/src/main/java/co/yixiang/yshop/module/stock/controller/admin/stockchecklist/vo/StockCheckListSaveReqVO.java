package co.yixiang.yshop.module.stock.controller.admin.stockchecklist.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - 商品盘库详情新增/修改 Request VO")
@Data
public class StockCheckListSaveReqVO {

    @Schema(description = "ID", example = "1")
    private Long id;

    @Schema(description = "盘库主表ID", example = "1")
    private Long stockStockcheckId;

    @Schema(description = "商品ID", example = "1")
    private Long stockGoodsId;

    @Schema(description = "商品规格ID", example = "1")
    private Long stockGoodsValueId;

    @Schema(description = "账面数量")
    private Integer bookNums;

    @Schema(description = "实盘数量")
    private Integer actualNums;

    @Schema(description = "盈亏数量(实盘-账面)")
    private Integer diffNums;

    @Schema(description = "盘库商品备注")
    private String checkGoodsRemark;

}
