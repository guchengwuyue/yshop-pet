package co.yixiang.yshop.module.stock.controller.admin.stockstockin.vo;

import co.yixiang.yshop.module.stock.controller.admin.stockstockinlist.vo.StockStockinListRespVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 商品入库 Response VO")
@Data
@ExcelIgnoreUnannotated
public class StockStockinRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "19374")
    @ExcelProperty("ID")
    private Long id;

    @Schema(description = "单据编号")
    @ExcelProperty("单据编号")
    private String docnum;

    @Schema(description = "供应商ID", example = "14787")
    @ExcelProperty("供应商ID")
    private Long stockSupplierId;

    @Schema(description = "供应商名称")
    @ExcelProperty("供应商名称")
    private String stockSupplierName;

    @Schema(description = "入库时间")
    @ExcelProperty("入库时间")
    private LocalDateTime inboundTime;

    @Schema(description = "入库商品总数")
    @ExcelProperty("入库商品总数")
    private Integer stockinTotalNums;

    @Schema(description = "单据总金额")
    @ExcelProperty("单据总金额")
    private BigDecimal totalAmount;

    @Schema(description = "备注", requiredMode = Schema.RequiredMode.REQUIRED, example = "你说的对")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "0-未审核 1-已=审核")
    @ExcelProperty("0-未审核 1-已=审核")
    private Integer isAudit;

    @Schema(description = "审核时间")
    @ExcelProperty("审核时间")
    private LocalDateTime auditTime;

    @Schema(description = "0-未结算  1-已结算")
    @ExcelProperty("0-未结算  1-已结算")
    private Integer isSettle;

    @Schema(description = "结算时间")
    @ExcelProperty("结算时间")
    private LocalDateTime settleTime;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "入库商品列表")
    @ExcelProperty("入库商品列表")
    private List<StockStockinListRespVO> stockStockinListList;

}