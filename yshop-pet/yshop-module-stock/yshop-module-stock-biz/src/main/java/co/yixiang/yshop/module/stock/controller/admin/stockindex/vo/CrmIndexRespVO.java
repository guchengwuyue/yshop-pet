package co.yixiang.yshop.module.stock.controller.admin.stockindex.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "管理后台 - 首页统计 Response VO")
@Data
@Builder
public class CrmIndexRespVO {


    private BrieCountVO brieCountVO;

    @Schema(description = "入库待审核")
    private Long todoCount;

    @Schema(description = "出库待审核")
    private Long doneCount;






}