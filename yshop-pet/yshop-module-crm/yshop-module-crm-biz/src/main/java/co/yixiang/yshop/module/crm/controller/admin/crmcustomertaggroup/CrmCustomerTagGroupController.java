package co.yixiang.yshop.module.crm.controller.admin.crmcustomertaggroup;

import co.yixiang.yshop.framework.common.pojo.CommonResult;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.module.crm.controller.admin.crmcustomertaggroup.vo.CrmCustomerTagGroupPageReqVO;
import co.yixiang.yshop.module.crm.controller.admin.crmcustomertaggroup.vo.CrmCustomerTagGroupRespVO;
import co.yixiang.yshop.module.crm.controller.admin.crmcustomertaggroup.vo.CrmCustomerTagGroupSaveReqVO;
import co.yixiang.yshop.module.crm.controller.admin.crmcustomertaggroup.vo.CrmCustomerTagSortVO;
import co.yixiang.yshop.module.crm.service.crmcustomertaggroup.CrmCustomerTagGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static co.yixiang.yshop.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 客户标签分组")
@RestController
@RequestMapping("/crm/customer-tag-group")
@Validated
public class CrmCustomerTagGroupController {

    @Resource
    private CrmCustomerTagGroupService customerTagGroupService;

    @PostMapping("/create")
    @Operation(summary = "创建客户标签分组")
    @PreAuthorize("@ss.hasPermission('crm:customer-tag-group:create')")
    public CommonResult<Long> createCustomerTagGroup(@Valid @RequestBody CrmCustomerTagGroupSaveReqVO createReqVO) {
        return success(customerTagGroupService.createCustomerTagGroup(createReqVO));
    }



    @PutMapping("/update")
    @Operation(summary = "更新客户标签分组")
    @PreAuthorize("@ss.hasPermission('crm:customer-tag-group:update')")
    public CommonResult<Boolean> updateCustomerTagGroup(@Valid @RequestBody CrmCustomerTagGroupSaveReqVO updateReqVO) {
        customerTagGroupService.updateCustomerTagGroup(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除客户标签分组")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:customer-tag-group:delete')")
    public CommonResult<Boolean> deleteCustomerTagGroup(@RequestParam("id") Long id) {
        customerTagGroupService.deleteCustomerTagGroup(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得客户标签分组")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('crm:customer-tag-group:query')")
    public CommonResult<CrmCustomerTagGroupRespVO> getCustomerTagGroup(@RequestParam("id") Long id) {
        CrmCustomerTagGroupRespVO customerTagGroup = customerTagGroupService.getCustomerTagGroup(id);
        return success(customerTagGroup);
    }

    @GetMapping("/page")
    @Operation(summary = "获得客户标签分组分页")
    @PreAuthorize("@ss.hasPermission('crm:customer-tag-group:query')")
    public CommonResult<PageResult<CrmCustomerTagGroupRespVO>> getCustomerTagGroupPage(@Valid CrmCustomerTagGroupPageReqVO pageReqVO) {
        PageResult<CrmCustomerTagGroupRespVO> pageResult = customerTagGroupService.getCustomerTagGroupPage(pageReqVO);
        return success(pageResult);
    }

    @PutMapping("/sort")
    @Operation(summary = "更新客户标签顺序")
    public CommonResult<Boolean> updateCustomerTagSort(@Valid @RequestBody List<CrmCustomerTagSortVO> sortVOS) {
        customerTagGroupService.updateCustomerTagSort(sortVOS);
        return success(true);
    }



}