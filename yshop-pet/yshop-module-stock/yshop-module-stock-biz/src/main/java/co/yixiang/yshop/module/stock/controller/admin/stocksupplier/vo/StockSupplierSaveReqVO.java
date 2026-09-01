package co.yixiang.yshop.module.stock.controller.admin.stocksupplier.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 供应商新增/修改 Request VO")
@Data
public class StockSupplierSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "16158")
    private Long id;

    @Schema(description = "名称", example = "赵六")
    private String name;

    @Schema(description = "短名", example = "李四")
    private String shortName;

    @Schema(description = "代码")
    private String code;

    @Schema(description = "发货地址")
    private String shipAddress;

    @Schema(description = "联系地址")
    private String address;

    @Schema(description = "联系人")
    private String contacts;

    @Schema(description = "联系电话")
    private String telphone;

    @Schema(description = "备注", example = "你猜")
    private String remark;

}