package co.yixiang.yshop.module.crm.controller.admin.crmform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 线索新增/修改 Request VO")
@Data
public class CrmFormSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "27885")
    private Long id;

    @Schema(description = "模型")
    private String formModule;

    @Schema(description = "模型json明细")
    private String formGroupJson;

}