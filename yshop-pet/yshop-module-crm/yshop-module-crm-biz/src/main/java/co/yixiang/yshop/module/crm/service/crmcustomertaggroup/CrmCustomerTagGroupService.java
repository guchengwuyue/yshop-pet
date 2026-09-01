package co.yixiang.yshop.module.crm.service.crmcustomertaggroup;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.module.crm.controller.admin.crmcustomertaggroup.vo.CrmCustomerTagGroupPageReqVO;
import co.yixiang.yshop.module.crm.controller.admin.crmcustomertaggroup.vo.CrmCustomerTagGroupRespVO;
import co.yixiang.yshop.module.crm.controller.admin.crmcustomertaggroup.vo.CrmCustomerTagGroupSaveReqVO;
import co.yixiang.yshop.module.crm.controller.admin.crmcustomertaggroup.vo.CrmCustomerTagSortVO;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 客户标签分组 Service 接口
 *
 * @author yshop
 */
public interface CrmCustomerTagGroupService {

    /**
     * 创建客户标签分组
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCustomerTagGroup(@Valid CrmCustomerTagGroupSaveReqVO createReqVO);


    /**
     * 更新客户标签分组
     *
     * @param updateReqVO 更新信息
     */
    void updateCustomerTagGroup(@Valid CrmCustomerTagGroupSaveReqVO updateReqVO);

    /**
     * 删除客户标签分组
     *
     * @param id 编号
     */
    void deleteCustomerTagGroup(Long id);

    /**
     * 获得客户标签分组
     *
     * @param id 编号
     * @return 客户标签分组
     */
    CrmCustomerTagGroupRespVO getCustomerTagGroup(Long id);

    /**
     * 获得客户标签分组分页
     *
     * @param pageReqVO 分页查询
     * @return 客户标签分组分页
     */
    PageResult<CrmCustomerTagGroupRespVO> getCustomerTagGroupPage(CrmCustomerTagGroupPageReqVO pageReqVO);

    /**
     * 更新客户标签排序
     *
     * @param sortVOS 排序信息
     */
    void updateCustomerTagSort(List<CrmCustomerTagSortVO> sortVOS);

}