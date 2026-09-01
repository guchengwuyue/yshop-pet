package co.yixiang.yshop.module.stock.controller.admin.stockchecklist.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import co.yixiang.yshop.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static co.yixiang.yshop.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 商品盘库详情分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StockCheckListPageReqVO extends PageParam {

    @Schema(description = "盘库主表ID", example = "1")
    private Long stockStockcheckId;

    @Schema(description = "商品ID", example = "1")
    private Long stockGoodsId;

    @Schema(description = "商品规格ID", example = "1")
    private Long stockGoodsValueId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
