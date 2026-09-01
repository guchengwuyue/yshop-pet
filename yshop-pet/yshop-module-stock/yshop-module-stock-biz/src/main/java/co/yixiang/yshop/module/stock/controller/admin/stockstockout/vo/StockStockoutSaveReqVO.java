package co.yixiang.yshop.module.stock.controller.admin.stockstockout.vo;

import co.yixiang.yshop.module.stock.controller.admin.stockstockoutlist.vo.StockStockoutListSaveReqVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 商品出库新增/修改 Request VO")
@Data
public class StockStockoutSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "27424")
    private Long id;

    @Schema(description = "出库单编号")
    private String docnum;

    @Schema(description = "出库时间")
    private LocalDateTime outboundTime;

    @Schema(description = "客户ID", example = "21593")
    private Long stockCustomerId;

    @Schema(description = "出库总商品数")
    private Integer stockOutTotalNums;

    @Schema(description = "出库总金额")
    private BigDecimal totalOutAmount;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "0-未审核 1-已=审核")
    private Integer isAudit;

    @Schema(description = "审核时间")
    private LocalDateTime auditTime;

    @Schema(description = "出库商品列表")
    private List<StockStockoutListSaveReqVO> stockoutLists;

}
