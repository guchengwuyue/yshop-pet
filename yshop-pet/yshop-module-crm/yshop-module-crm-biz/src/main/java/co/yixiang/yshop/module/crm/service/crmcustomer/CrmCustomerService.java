package co.yixiang.yshop.module.crm.service.crmcustomer;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.module.crm.controller.admin.crmcustomer.vo.*;
import co.yixiang.yshop.module.crm.dal.dataobject.crmcustomer.CrmCustomerDO;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 客户 Service 接口
 *
 * @author yshop
 */
public interface CrmCustomerService {

    /**
     * 创建客户
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCustomer(@Valid CrmCustomerSaveReqVO createReqVO);

    /**
     * 更新客户
     *
     * @param updateReqVO 更新信息
     */
    void updateCustomer(@Valid CrmCustomerSaveReqVO updateReqVO);

    /**
     * 删除客户
     *
     * @param id 编号
     */
    void deleteCustomer(Long id);




    /**
     * 获得客户
     *
     * @param id 编号
     * @return 客户
     */
    CrmCustomerRespVO getCustomer(Long id);

    /**
     * 获得客户分页
     *
     * @param pageReqVO 分页查询
     * @return 客户分页
     */
    PageResult<CrmCustomerDO> getCustomerPage(CrmCustomerPageReqVO pageReqVO);

    /**
     * 获得客户分页2
     *
     * @param pageReqVO 分页查询
     * @return 客户分页
     */
    PageResult<CrmCustomerRespVO> getCustomerPage2(CrmCustomerPageReqVO pageReqVO);

    /**
     * 获得附近客户
     * @param nearbyCustomerVO
     * @return
     */
    List<NearbyCustomerRespVO> getNearbyCustomer(NearbyCustomerVO nearbyCustomerVO);

    /**
     * 客户导入
     * @param importUsers 导入文件
     * @param deptId 部门ID
     * @param adminIds 用户ID
     * @param averageType 导入类型
     * @return
     */
    CustomerImportRespVO importList(List<CrmCustomerImportVO> importUsers, String deptId, String adminIds, Integer averageType);


}