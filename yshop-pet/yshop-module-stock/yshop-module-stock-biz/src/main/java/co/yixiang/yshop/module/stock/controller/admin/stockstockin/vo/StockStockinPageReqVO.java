package co.yixiang.yshop.module.stock.controller.admin.stockstockin.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import co.yixiang.yshop.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static co.yixiang.yshop.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 商品入库分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StockStockinPageReqVO extends PageParam {

    @Schema(description = "单据编号")
    private String docnum;

    @Schema(description = "供应商ID", example = "14787")
    private Long stockSupplierId;

    @Schema(description = "供应商名称")
    private String stockSupplierName;

    @Schema(description = "入库时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] inboundTime;

    @Schema(description = "入库商品总数")
    private Integer stockinTotalNums;

    @Schema(description = "单据总金额")
    private BigDecimal totalAmount;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "0-未审核 1-已=审核")
    private Integer isAudit;

    @Schema(description = "审核时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] auditTime;

    @Schema(description = "0-未结算  1-已结算")
    private Integer isSettle;

    @Schema(description = "结算时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] settleTime;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}