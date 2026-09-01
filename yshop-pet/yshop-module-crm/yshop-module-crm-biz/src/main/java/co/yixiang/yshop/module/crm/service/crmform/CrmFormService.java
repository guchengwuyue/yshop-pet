package co.yixiang.yshop.module.crm.service.crmform;

import co.yixiang.yshop.module.crm.controller.admin.crmform.vo.CrmFormSaveReqVO;
import co.yixiang.yshop.module.crm.dal.dataobject.crmform.CrmFormDO;
import jakarta.validation.Valid;

/**
 * 表单 Service 接口
 *
 * @author yshop
 */
public interface CrmFormService {

    /**
     * 创建表单
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createForm(@Valid CrmFormSaveReqVO createReqVO);



    /**
     * 获得表单
     *
     * @param module 编号
      * @return 表单
     */
    CrmFormDO getForm(String module);



}