package co.yixiang.yshop.module.stock.controller.admin.stockstockin.vo;

import co.yixiang.yshop.module.stock.controller.admin.stockstockinlist.vo.StockStockinListSaveReqVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 商品入库新增/修改 Request VO")
@Data
public class StockStockinSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "19374")
    private Long id;

    @Schema(description = "单据编号")
    private String docnum;

    @Schema(description = "供应商ID", example = "14787")
    private Long stockSupplierId;

    @Schema(description = "入库时间")
    private LocalDateTime inboundTime;

    @Schema(description = "入库商品总数")
    private Integer stockinTotalNums;

    @Schema(description = "单据总金额")
    private BigDecimal totalAmount;

    @Schema(description = "备注", requiredMode = Schema.RequiredMode.REQUIRED, example = "你说的对")
    private String remark;

    @Schema(description = "0-未审核 1-已=审核")
    private Integer isAudit;

    @Schema(description = "审核时间")
    private LocalDateTime auditTime;

    @Schema(description = "0-未结算  1-已结算")
    private Integer isSettle;

    @Schema(description = "结算时间")
    private LocalDateTime settleTime;

    @Schema(description = "入库商品列表")
    private List<StockStockinListSaveReqVO> stockinLists;

}