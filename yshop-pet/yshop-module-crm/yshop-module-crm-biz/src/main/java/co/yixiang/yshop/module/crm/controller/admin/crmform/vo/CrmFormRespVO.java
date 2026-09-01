package co.yixiang.yshop.module.crm.controller.admin.crmform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 线索 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CrmFormRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "27885")
    @ExcelProperty("ID")
    private Long id;

    @Schema(description = "模型")
    @ExcelProperty("模型")
    private String formModule;

    @Schema(description = "模型json明细")
    @ExcelProperty("模型json明细")
    private String formGroupJson;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}