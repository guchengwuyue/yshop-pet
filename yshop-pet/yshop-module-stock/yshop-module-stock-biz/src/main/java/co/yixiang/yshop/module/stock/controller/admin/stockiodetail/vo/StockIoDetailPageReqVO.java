package co.yixiang.yshop.module.stock.controller.admin.stockiodetail.vo;

import co.yixiang.yshop.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 商品出入盘库明细分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StockIoDetailPageReqVO extends PageParam {

    @Schema(description = "商品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "商品ID不能为空")
    private Long stockGoodsId;

    @Schema(description = "搜索关键字（商品代码/名称/规格/类型/单据编号）")
    private String keyword;

}
