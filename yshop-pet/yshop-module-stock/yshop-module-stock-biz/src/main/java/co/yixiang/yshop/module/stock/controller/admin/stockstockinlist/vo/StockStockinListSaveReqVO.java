package co.yixiang.yshop.module.stock.controller.admin.stockstockinlist.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 商品入库详情新增/修改 Request VO")
@Data
public class StockStockinListSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "15767")
    private Long id;

    @Schema(description = "入库主表ID", example = "18551")
    private Long stockStockinId;

    @Schema(description = "商品ID", example = "26720")
    private Long stockGoodsId;

    @Schema(description = "商品规格id", example = "1049")
    private Long stockGoodsValueId;

    @Schema(description = "入库数量")
    private Integer stockinNums;

    @Schema(description = "入库价格", example = "20071")
    private BigDecimal inboundPrice;

    @Schema(description = "入库金额")
    private BigDecimal amount;

    @Schema(description = "入库备注", example = "随便")
    private String stockinGoodsRemark;



}