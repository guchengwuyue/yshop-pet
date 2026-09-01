package co.yixiang.yshop.module.stock.controller.admin.stockiodetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 商品出入盘库明细 Response VO")
@Data
public class StockIoDetailRespVO {

    @Schema(description = "明细唯一键（类型+ID）", example = "in-1")
    private String rowKey;

    @Schema(description = "明细ID", example = "1")
    private Long id;

    @Schema(description = "出入类型：入库/出库/盘库", example = "入库")
    private String ioType;

    @Schema(description = "单据编号", example = "PK20260101001")
    private String docnum;

    @Schema(description = "商品ID", example = "1024")
    private Long stockGoodsId;

    @Schema(description = "商品规格ID", example = "2048")
    private Long stockGoodsValueId;

    @Schema(description = "商品代码", example = "08.031")
    private String goodsCode;

    @Schema(description = "商品名称", example = "帐篷")
    private String stockGoodsName;

    @Schema(description = "规格型号", example = "1人")
    private String stockGoodsValueName;

    @Schema(description = "单位", example = "个")
    private String unitName;

    @Schema(description = "单价", example = "412.00")
    private BigDecimal unitPrice;

    @Schema(description = "入库数量")
    private Integer stockinNums;

    @Schema(description = "入库总价")
    private BigDecimal stockinAmount;

    @Schema(description = "出库数量")
    private Integer stockOutNums;

    @Schema(description = "出库总价")
    private BigDecimal stockOutAmount;

    @Schema(description = "账面数量（盘库）")
    private Integer bookNums;

    @Schema(description = "实盘数量（盘库）")
    private Integer actualNums;

    @Schema(description = "盈亏数量（盘库，实盘-账面）")
    private Integer diffNums;

    @Schema(description = "出入盘时间")
    private LocalDateTime ioTime;

}
