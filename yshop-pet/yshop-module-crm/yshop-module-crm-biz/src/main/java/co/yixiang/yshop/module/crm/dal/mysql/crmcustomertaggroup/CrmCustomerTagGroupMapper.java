package co.yixiang.yshop.module.crm.dal.mysql.crmcustomertaggroup;

import java.util.*;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.mybatis.core.query.LambdaQueryWrapperX;
import co.yixiang.yshop.framework.mybatis.core.mapper.BaseMapperX;
import co.yixiang.yshop.module.crm.dal.dataobject.crmcustomertaggroup.CrmCustomerTagGroupDO;
import org.apache.ibatis.annotations.Mapper;
import co.yixiang.yshop.module.crm.controller.admin.crmcustomertaggroup.vo.*;

/**
 * 客户标签分组 Mapper
 *
 * @author yshop
 */
@Mapper
public interface CrmCustomerTagGroupMapper extends BaseMapperX<CrmCustomerTagGroupDO> {

    default PageResult<CrmCustomerTagGroupDO> selectPage(CrmCustomerTagGroupPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CrmCustomerTagGroupDO>()
                .likeIfPresent(CrmCustomerTagGroupDO::getGroupName, reqVO.getGroupName())
                .eqIfPresent(CrmCustomerTagGroupDO::getSort, reqVO.getSort())
                .betweenIfPresent(CrmCustomerTagGroupDO::getCreateTime, reqVO.getCreateTime())
                .orderByAsc(CrmCustomerTagGroupDO::getSort));
    }

}