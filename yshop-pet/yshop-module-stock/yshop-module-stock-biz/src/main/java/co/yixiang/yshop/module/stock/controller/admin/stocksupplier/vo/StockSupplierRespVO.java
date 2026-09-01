package co.yixiang.yshop.module.stock.controller.admin.stocksupplier.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 供应商 Response VO")
@Data
@ExcelIgnoreUnannotated
public class StockSupplierRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "16158")
    @ExcelProperty("id")
    private Long id;

    @Schema(description = "名称", example = "赵六")
    @ExcelProperty("名称")
    private String name;

    @Schema(description = "短名", example = "李四")
    @ExcelProperty("短名")
    private String shortName;

    @Schema(description = "代码")
    @ExcelProperty("代码")
    private String code;

    @Schema(description = "发货地址")
    @ExcelProperty("发货地址")
    private String shipAddress;

    @Schema(description = "联系地址")
    @ExcelProperty("联系地址")
    private String address;

    @Schema(description = "联系人")
    @ExcelProperty("联系人")
    private String contacts;

    @Schema(description = "联系电话")
    @ExcelProperty("联系电话")
    private String telphone;

    @Schema(description = "备注", example = "你猜")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}