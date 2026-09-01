package co.yixiang.yshop.module.crm.service.crmformdata;

import co.yixiang.yshop.module.crm.controller.admin.crmformdata.vo.CrmFormDataSaveReqVO;
import co.yixiang.yshop.module.crm.dal.dataobject.crmformdata.CrmFormDataDO;
import jakarta.validation.Valid;

/**
 * crm表单自定义数据 Service 接口
 *
 * @author yshop
 */
public interface CrmFormDataService {

    /**
     * 创建crm表单自定义数据
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createFormData(@Valid CrmFormDataSaveReqVO createReqVO);



    /**
     * 删除crm表单自定义数据
     *
     * @param id 编号
     */
    void deleteFormData(Long id);

    /**
     * 获得crm表单自定义数据
     * @param module 模块
     * @param id 编号
     * @return crm表单自定义数据
     */
    CrmFormDataDO getFormData(String module, Long id);



}