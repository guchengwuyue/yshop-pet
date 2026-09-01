package co.yixiang.yshop.module.crm.controller.admin.crmcustomer;

import co.yixiang.yshop.framework.apilog.core.annotation.ApiAccessLog;
import co.yixiang.yshop.framework.common.pojo.CommonResult;
import co.yixiang.yshop.framework.common.pojo.PageParam;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.common.util.object.BeanUtils;
import co.yixiang.yshop.framework.excel.core.util.ExcelUtils;
import co.yixiang.yshop.module.crm.controller.admin.crmcustomer.vo.*;
import co.yixiang.yshop.module.crm.dal.dataobject.crmcustomer.CrmCustomerDO;
import co.yixiang.yshop.module.crm.service.crmcustomer.CrmCustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static co.yixiang.yshop.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static co.yixiang.yshop.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 客户")
@RestController
@RequestMapping("/crm/customer")
@Validated
public class CrmCustomerController {

    @Resource
    private CrmCustomerService customerService;

    @PostMapping("/create")
    @Operation(summary = "创建客户")
    @PreAuthorize("@ss.hasPermission('crm:customer:create')")
    public CommonResult<Long> createCustomer(@Valid @RequestBody CrmCustomerSaveReqVO createReqVO) {
        return success(customerService.createCustomer(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新客户")
    @PreAuthorize("@ss.hasPermission('crm:customer:update')")
    public CommonResult<Boolean> updateCustomer(@Valid @RequestBody CrmCustomerSaveReqVO updateReqVO) {
        customerService.updateCustomer(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除客户")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:customer:delete')")
    public CommonResult<Boolean> deleteCustomer(@RequestParam("id") Long id) {
        customerService.deleteCustomer(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得客户")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('crm:customer:query')")
    public CommonResult<CrmCustomerRespVO> getCustomer(@RequestParam("id") Long id) {
        return success(customerService.getCustomer(id));
    }

    @GetMapping("/page")
    @Operation(summary = "获得客户分页")
    @PreAuthorize("@ss.hasPermission('crm:customer:query')")
    public CommonResult<PageResult<CrmCustomerRespVO>> getCustomerPage(@Valid CrmCustomerPageReqVO pageReqVO) {
        PageResult<CrmCustomerDO> pageResult = customerService.getCustomerPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CrmCustomerRespVO.class));
    }

    @GetMapping("/page2")
    @Operation(summary = "获得查重客户分页")
    @PreAuthorize("@ss.hasPermission('crm:customer:query')")
    public CommonResult<PageResult<CrmCustomerRespVO>> getCustomerPage2(@Valid CrmCustomerPageReqVO pageReqVO) {
        return success(customerService.getCustomerPage2(pageReqVO));
    }

    @GetMapping("/nearby")
    @Operation(summary = "获得附近客户")
    public CommonResult<List<NearbyCustomerRespVO>> getNearbyCustomer(@Valid NearbyCustomerVO nearbyCustomerVO) {
        return success(customerService.getNearbyCustomer(nearbyCustomerVO));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出客户 Excel")
    @PreAuthorize("@ss.hasPermission('crm:customer:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCustomerExcel(@Valid CrmCustomerPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CrmCustomerDO> list = customerService.getCustomerPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "客户.xls", "数据", CrmCustomerRespVO.class,
                        BeanUtils.toBean(list, CrmCustomerRespVO.class));
    }

    @GetMapping("/get-import-template")
    @Operation(summary = "获得导入模板")
    public void importTemplate(HttpServletResponse response) throws IOException {
        // 手动创建导出 demo
        List<CrmCustomerImportVO> list = Arrays.asList(
                CrmCustomerImportVO.builder().name("yshop").mobile("18888888888").telephone("18888888888").tags("重要,厉害")
                        .remark("yshop").detailAddress("详细地址").lng(10.1).lat(10.2).weixin("wechat").qq("100000").build(),
                CrmCustomerImportVO.builder().name("yshop2").mobile("18888888888=0").telephone("18888888880").tags("重要2,厉害")
                        .remark("yshop2").detailAddress("详细地址2").lng(10.1).lat(10.2).weixin("wechat2").qq("1000002").build()
        );
        // 输出
        ExcelUtils.write(response, "客户导入模板.xls", "客户列表", CrmCustomerImportVO.class, list);
    }

    @PostMapping("/import")
    @Operation(summary = "导入客户")
    @Parameters({
            @Parameter(name = "file", description = "Excel 文件", required = true),
            @Parameter(name = "deptId", description = "部门ID"),
            @Parameter(name = "adminIds", description = "用户ID 多个逗号隔开"),
            @Parameter(name = "averageType", description = "分配方式")
    })
    @PreAuthorize("@ss.hasPermission('crm:customer:import')")
    public CommonResult<CustomerImportRespVO> importExcel(@RequestParam("file") MultipartFile file,
                                                          @RequestParam(value = "deptId",required = false) String deptId,
                                                          @RequestParam(value = "adminIds",required = false) String adminIds,
                                                          @RequestParam(value = "averageType",required = false) Integer averageType) throws Exception {
        List<CrmCustomerImportVO> list = ExcelUtils.read(file, CrmCustomerImportVO.class);
        return success(customerService.importList(list, deptId,adminIds,averageType));
    }




}