package co.yixiang.yshop.module.stock.controller.admin.stockcheck.vo;

import co.yixiang.yshop.module.stock.controller.admin.stockchecklist.vo.StockCheckListSaveReqVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 商品盘库新增/修改 Request VO")
@Data
public class StockCheckSaveReqVO {

    @Schema(description = "ID", example = "1")
    private Long id;

    @Schema(description = "单据编号")
    private String docnum;

    @Schema(description = "盘库时间")
    private LocalDateTime checkTime;

    @Schema(description = "盘点商品总数")
    private Integer checkTotalNums;

    @Schema(description = "盘盈合计")
    private Integer profitNums;

    @Schema(description = "盘亏合计")
    private Integer lossNums;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "0-未审核 1-已审核")
    private Integer isAudit;

    @Schema(description = "审核时间")
    private LocalDateTime auditTime;

    @Schema(description = "盘库商品列表")
    private List<StockCheckListSaveReqVO> checkLists;

}
