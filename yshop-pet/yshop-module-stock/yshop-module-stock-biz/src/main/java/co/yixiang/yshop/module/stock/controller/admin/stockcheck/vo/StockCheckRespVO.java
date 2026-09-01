package co.yixiang.yshop.module.stock.controller.admin.stockcheck.vo;

import co.yixiang.yshop.module.stock.controller.admin.stockchecklist.vo.StockCheckListRespVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 商品盘库 Response VO")
@Data
@ExcelIgnoreUnannotated
public class StockCheckRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("ID")
    private Long id;

    @Schema(description = "单据编号")
    @ExcelProperty("单据编号")
    private String docnum;

    @Schema(description = "盘库时间")
    @ExcelProperty("盘库时间")
    private LocalDateTime checkTime;

    @Schema(description = "盘点商品总数")
    @ExcelProperty("盘点商品总数")
    private Integer checkTotalNums;

    @Schema(description = "盘盈合计")
    @ExcelProperty("盘盈合计")
    private Integer profitNums;

    @Schema(description = "盘亏合计")
    @ExcelProperty("盘亏合计")
    private Integer lossNums;

    @Schema(description = "备注")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "0-未审核 1-已审核")
    @ExcelProperty("审核状态")
    private Integer isAudit;

    @Schema(description = "审核时间")
    @ExcelProperty("审核时间")
    private LocalDateTime auditTime;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "盘库商品列表")
    private List<StockCheckListRespVO> stockStockcheckListList;

}
