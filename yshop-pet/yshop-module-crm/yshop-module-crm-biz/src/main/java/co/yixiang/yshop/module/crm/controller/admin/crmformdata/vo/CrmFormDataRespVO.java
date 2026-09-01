package co.yixiang.yshop.module.crm.controller.admin.crmformdata.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - crm表单自定义数据 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CrmFormDataRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "14985")
    @ExcelProperty("ID")
    private Long id;

    @Schema(description = "模型")
    @ExcelProperty("模型")
    private String formModule;

    @Schema(description = "目标数据id", example = "1822")
    @ExcelProperty("目标数据id")
    private Long formDataId;

    @Schema(description = "目标数据json")
    @ExcelProperty("目标数据json")
    private String formDataJson;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}