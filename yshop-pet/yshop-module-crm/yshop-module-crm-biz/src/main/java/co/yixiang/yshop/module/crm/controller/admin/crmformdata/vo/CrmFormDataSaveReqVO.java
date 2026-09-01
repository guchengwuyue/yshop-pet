package co.yixiang.yshop.module.crm.controller.admin.crmformdata.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - crm表单自定义数据新增/修改 Request VO")
@Data
public class CrmFormDataSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "14985")
    private Long id;

    @Schema(description = "模型")
    private String formModule;

    @Schema(description = "目标数据id", example = "1822")
    private Long formDataId;

    @Schema(description = "目标数据json")
    private String formDataJson;

}