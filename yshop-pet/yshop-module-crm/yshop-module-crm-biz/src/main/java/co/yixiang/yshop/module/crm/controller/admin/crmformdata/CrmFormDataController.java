package co.yixiang.yshop.module.crm.controller.admin.crmformdata;

import co.yixiang.yshop.framework.common.pojo.CommonResult;
import co.yixiang.yshop.framework.common.util.object.BeanUtils;
import co.yixiang.yshop.module.crm.controller.admin.crmformdata.vo.CrmFormDataRespVO;
import co.yixiang.yshop.module.crm.controller.admin.crmformdata.vo.CrmFormDataSaveReqVO;
import co.yixiang.yshop.module.crm.dal.dataobject.crmformdata.CrmFormDataDO;
import co.yixiang.yshop.module.crm.service.crmformdata.CrmFormDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static co.yixiang.yshop.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - crm表单自定义数据")
@RestController
@RequestMapping("/crm/form-data")
@Validated
public class CrmFormDataController {

    @Resource
    private CrmFormDataService formDataService;

    @PostMapping("/create")
    @Operation(summary = "创建crm表单自定义数据")
    @PreAuthorize("@ss.hasPermission('crm:form-data:create')")
    public CommonResult<Long> createFormData(@Valid @RequestBody CrmFormDataSaveReqVO createReqVO) {
        return success(formDataService.createFormData(createReqVO));
    }



    @DeleteMapping("/delete")
    @Operation(summary = "删除crm表单自定义数据")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:form-data:delete')")
    public CommonResult<Boolean> deleteFormData(@RequestParam("id") Long id) {
        formDataService.deleteFormData(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得crm表单自定义数据")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('crm:form-data:query')")
    public CommonResult<CrmFormDataRespVO> getFormData(@RequestParam("module") String module, @RequestParam("id") Long id) {
        CrmFormDataDO formData = formDataService.getFormData(module,id);
        return success(BeanUtils.toBean(formData, CrmFormDataRespVO.class));
    }


}