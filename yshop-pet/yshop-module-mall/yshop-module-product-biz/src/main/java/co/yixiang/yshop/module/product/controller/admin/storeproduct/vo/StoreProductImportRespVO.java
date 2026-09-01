package co.yixiang.yshop.module.product.controller.admin.storeproduct.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Schema(description = "管理后台 - 商品导入 Response VO")
@Data
@Builder
public class StoreProductImportRespVO {

    @Schema(description = "创建成功的商品名称数组", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> createNames;

    @Schema(description = "导入失败的商品集合，key 为商品名称，value 为失败原因", requiredMode = Schema.RequiredMode.REQUIRED)
    private Map<String, String> failureNames;

}
