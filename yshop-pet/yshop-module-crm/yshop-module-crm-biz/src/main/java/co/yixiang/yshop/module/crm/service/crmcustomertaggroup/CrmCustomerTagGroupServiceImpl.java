package co.yixiang.yshop.module.crm.service.crmcustomertaggroup;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.common.util.object.BeanUtils;
import co.yixiang.yshop.module.crm.controller.admin.crmcustomertaggroup.vo.*;
import co.yixiang.yshop.module.crm.dal.dataobject.crmcustomertaggroup.CrmCustomerTagGroupDO;
import co.yixiang.yshop.module.crm.dal.dataobject.crmcustomertaggroupdetail.CrmCustomerTagGroupDetailDO;
import co.yixiang.yshop.module.crm.dal.mysql.crmcustomertaggroup.CrmCustomerTagGroupMapper;
import co.yixiang.yshop.module.crm.dal.mysql.crmcustomertaggroupdetail.CrmCustomerTagGroupDetailMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;
import static co.yixiang.yshop.module.crm.enums.ErrorCodeConstants.CUSTOMER_TAG_GROUP_NOT_EXISTS;

/**
 * 客户标签分组 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
@Slf4j
public class CrmCustomerTagGroupServiceImpl implements CrmCustomerTagGroupService {

    @Resource
    private CrmCustomerTagGroupMapper customerTagGroupMapper;
    @Resource
    private CrmCustomerTagGroupDetailMapper customerTagGroupDetailMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createCustomerTagGroup(CrmCustomerTagGroupSaveReqVO createReqVO) {
        // 插入
        CrmCustomerTagGroupDO customerTagGroup = BeanUtils.toBean(createReqVO, CrmCustomerTagGroupDO.class);
        customerTagGroupMapper.insert(customerTagGroup);
        //插入标签列表详情
        List<CrmCustomerTagGroupDetailDO> groupDetailDOS = new ArrayList<>();
        for (CrmCustomerTagGroupDetailVO detailVO : createReqVO.getTagsList()) {
            CrmCustomerTagGroupDetailDO detailDO = BeanUtils.toBean(detailVO, CrmCustomerTagGroupDetailDO.class);
            detailDO.setGroupId(customerTagGroup.getId());
            groupDetailDOS.add(detailDO);
        }
        customerTagGroupDetailMapper.insertBatch(groupDetailDOS);

        // 返回
        return customerTagGroup.getId();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCustomerTagGroup(CrmCustomerTagGroupSaveReqVO updateReqVO) {
        // 校验存在
        validateCustomerTagGroupExists(updateReqVO.getId());
        // 更新
        CrmCustomerTagGroupDO updateObj = BeanUtils.toBean(updateReqVO, CrmCustomerTagGroupDO.class);
        //更新标签明细
        //List<CrmCustomerTagGroupDetailDO> groupDetailDOS = new ArrayList<>();
        for (CrmCustomerTagGroupDetailVO detailVO : updateReqVO.getTagsList()) {

            CrmCustomerTagGroupDetailDO detailDO = BeanUtils.toBean(detailVO, CrmCustomerTagGroupDetailDO.class);
            detailDO.setGroupId(updateObj.getId());
            if(detailDO.getId() == null){
                customerTagGroupDetailMapper.insert(detailDO);
            }else{
                customerTagGroupDetailMapper.updateById(detailDO);
            }
            //groupDetailDOS.add(detailDO);
        }
        //customerTagGroupDetailMapper.updateBatch(groupDetailDOS);

        customerTagGroupMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCustomerTagGroup(Long id) {
        // 校验存在
        validateCustomerTagGroupExists(id);
        // 删除
        customerTagGroupMapper.deleteById(id);
        customerTagGroupDetailMapper.delete(new LambdaQueryWrapper<CrmCustomerTagGroupDetailDO>()
                .eq(CrmCustomerTagGroupDetailDO::getGroupId,id));
    }

    private void validateCustomerTagGroupExists(Long id) {
        if (customerTagGroupMapper.selectById(id) == null) {
            throw exception(CUSTOMER_TAG_GROUP_NOT_EXISTS);
        }
    }

    @Override
    public CrmCustomerTagGroupRespVO getCustomerTagGroup(Long id) {
        CrmCustomerTagGroupDO customerTagGroup = customerTagGroupMapper.selectById(id);
        CrmCustomerTagGroupRespVO respVO = BeanUtils.toBean(customerTagGroup, CrmCustomerTagGroupRespVO.class);
        List<CrmCustomerTagGroupDetailDO> groupDetailDOS = customerTagGroupDetailMapper.selectList(new LambdaQueryWrapper<CrmCustomerTagGroupDetailDO>()
                .eq(CrmCustomerTagGroupDetailDO::getGroupId,id).orderByAsc(CrmCustomerTagGroupDetailDO::getSort));
        respVO.setTagsList(BeanUtils.toBean(groupDetailDOS, CrmCustomerTagGroupDetailVO.class));
        return respVO;
    }

    @Override
    public PageResult<CrmCustomerTagGroupRespVO> getCustomerTagGroupPage(CrmCustomerTagGroupPageReqVO pageReqVO) {
        PageResult<CrmCustomerTagGroupDO> pageResult = customerTagGroupMapper.selectPage(pageReqVO);
        PageResult<CrmCustomerTagGroupRespVO> list = BeanUtils.toBean(pageResult, CrmCustomerTagGroupRespVO.class);
        //获取标签详情
        for (CrmCustomerTagGroupRespVO respVO : list.getList()) {
            List<CrmCustomerTagGroupDetailDO> groupDetailDOS = customerTagGroupDetailMapper
                    .selectList(new LambdaQueryWrapper<CrmCustomerTagGroupDetailDO>()
                            .eq(CrmCustomerTagGroupDetailDO::getGroupId,respVO.getId())
                            .orderByAsc(CrmCustomerTagGroupDetailDO::getSort));
            respVO.setTagsList(BeanUtils.toBean(groupDetailDOS, CrmCustomerTagGroupDetailVO.class));
        }

        return list;
    }

    @Override
    public void updateCustomerTagSort(List<CrmCustomerTagSortVO> sortVOS){
        //更新标签顺序
        customerTagGroupMapper.updateBatch(BeanUtils.toBean(sortVOS, CrmCustomerTagGroupDO.class));

    }

}