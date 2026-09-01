package co.yixiang.yshop.module.crm.dal.mysql.crmformdata;

import java.util.*;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.mybatis.core.query.LambdaQueryWrapperX;
import co.yixiang.yshop.framework.mybatis.core.mapper.BaseMapperX;
import co.yixiang.yshop.module.crm.dal.dataobject.crmformdata.CrmFormDataDO;
import org.apache.ibatis.annotations.Mapper;
import co.yixiang.yshop.module.crm.controller.admin.crmformdata.vo.*;

/**
 * crm表单自定义数据 Mapper
 *
 * @author yshop
 */
@Mapper
public interface CrmFormDataMapper extends BaseMapperX<CrmFormDataDO> {

    default PageResult<CrmFormDataDO> selectPage(CrmFormDataPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CrmFormDataDO>()
                .eqIfPresent(CrmFormDataDO::getFormModule, reqVO.getFormModule())
                .eqIfPresent(CrmFormDataDO::getFormDataId, reqVO.getFormDataId())
                .eqIfPresent(CrmFormDataDO::getFormDataJson, reqVO.getFormDataJson())
                .betweenIfPresent(CrmFormDataDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CrmFormDataDO::getId));
    }

}