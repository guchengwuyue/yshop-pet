package co.yixiang.yshop.module.crm.controller.admin.crmcustomertaggroup.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 客户标签排序 Request VO")
@Data
public class CrmCustomerTagSortVO {

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "28561")
    private Long id;


    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer sort;



}