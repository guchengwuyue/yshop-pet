package co.yixiang.yshop.module.crm.controller.admin.crmform;

import co.yixiang.yshop.framework.common.pojo.CommonResult;
import co.yixiang.yshop.framework.common.util.object.BeanUtils;
import co.yixiang.yshop.module.crm.controller.admin.crmform.vo.CrmFormRespVO;
import co.yixiang.yshop.module.crm.controller.admin.crmform.vo.CrmFormSaveReqVO;
import co.yixiang.yshop.module.crm.dal.dataobject.crmform.CrmFormDO;
import co.yixiang.yshop.module.crm.service.crmform.CrmFormService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static co.yixiang.yshop.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 表单")
@RestController
@RequestMapping("/crm/form")
@Validated
public class CrmFormController {

    @Resource
    private CrmFormService formService;

    @PostMapping("/create")
    @Operation(summary = "创建表单")
    @PreAuthorize("@ss.hasPermission('crm:form:create')")
    public CommonResult<Long> createForm(@Valid @RequestBody CrmFormSaveReqVO createReqVO) {
        return success(formService.createForm(createReqVO));
    }



    @GetMapping("/get")
    @Operation(summary = "获得表单")
    @Parameter(name = "module", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('crm:form:query')")
    public CommonResult<CrmFormRespVO> getForm(@RequestParam("module") String module) {
        CrmFormDO form = formService.getForm(module);
        return success(BeanUtils.toBean(form, CrmFormRespVO.class));
    }





}