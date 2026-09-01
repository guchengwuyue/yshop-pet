package co.yixiang.yshop.module.stock.controller.admin.stockstockinlist.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 商品入库详情 Response VO")
@Data
@ExcelIgnoreUnannotated
public class StockStockinListRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "15767")
    @ExcelProperty("id")
    private Long id;

    @Schema(description = "入库主表ID", example = "18551")
    @ExcelProperty("入库主表ID")
    private Long stockStockinId;


    @Schema(description = "商品ID", example = "26720")
    @ExcelProperty("商品ID")
    private Long stockGoodsId;

    @Schema(description = "商品名称", example = "商品名称")
    @ExcelProperty("商品名称")
    private String stockGoodsName;

    @Schema(description = "商品编码", example = "商品编码")
    @ExcelProperty("商品编码")
    private String goodsCode;

    @Schema(description = "商品规格id", example = "1049")
    @ExcelProperty("商品规格id")
    private Long stockGoodsValueId;

    @Schema(description = "商品规格名称", example = "商品规格名称")
    @ExcelProperty("商品规格名称")
    private String stockGoodsValueName;

    @Schema(description = "单位名称", example = "件")
    @ExcelProperty("单位名称")
    private String unitName;

    @Schema(description = "入库数量")
    @ExcelProperty("入库数量")
    private Integer stockinNums;

    @Schema(description = "入库价格", example = "20071")
    @ExcelProperty("入库价格")
    private BigDecimal inboundPrice;

    @Schema(description = "入库金额")
    @ExcelProperty("入库金额")
    private BigDecimal amount;

    @Schema(description = "入库备注", example = "随便")
    @ExcelProperty("入库备注")
    private String stockinGoodsRemark;


    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "单据编号", example = "15767")
    @ExcelProperty("单据编号")
    private String docNumber;

    @Schema(description = "供应商名称", example = "供应商名称")
    @ExcelProperty("供应商名称")
    private String supplierName;

    @Schema(description = "供应商ID", example = "15767")
    @ExcelProperty("供应商ID")
    private Long supplierId;

    @Schema(description = "入库时间")
    @ExcelProperty("入库时间")
    private LocalDateTime inboundTime;

}