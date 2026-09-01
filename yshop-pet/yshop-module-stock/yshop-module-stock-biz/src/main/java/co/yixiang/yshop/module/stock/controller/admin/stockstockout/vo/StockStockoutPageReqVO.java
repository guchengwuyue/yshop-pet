package co.yixiang.yshop.module.stock.controller.admin.stockstockout.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import co.yixiang.yshop.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static co.yixiang.yshop.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 商品出库分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StockStockoutPageReqVO extends PageParam {

    @Schema(description = "出库单编号")
    private String docnum;

    @Schema(description = "出库时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] outboundTime;

    @Schema(description = "客户ID", example = "21593")
    private Long stockCustomerId;

    @Schema(description = "客户名称")
    private String stockCustomerName;

    @Schema(description = "出库总商品数")
    private Integer stockOutTotalNums;

    @Schema(description = "出库总金额")
    private BigDecimal totalOutAmount;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "0-未审核 1-已=审核")
    private Integer isAudit;

    @Schema(description = "审核时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] auditTime;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}