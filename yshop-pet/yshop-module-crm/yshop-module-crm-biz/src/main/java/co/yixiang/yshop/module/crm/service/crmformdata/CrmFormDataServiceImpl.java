package co.yixiang.yshop.module.crm.service.crmformdata;

import co.yixiang.yshop.framework.common.util.object.BeanUtils;
import co.yixiang.yshop.module.crm.controller.admin.crmformdata.vo.CrmFormDataSaveReqVO;
import co.yixiang.yshop.module.crm.dal.dataobject.crmformdata.CrmFormDataDO;
import co.yixiang.yshop.module.crm.dal.mysql.crmformdata.CrmFormDataMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;
import static co.yixiang.yshop.module.crm.enums.ErrorCodeConstants.FORM_DATA_NOT_EXISTS;

/**
 * crm表单自定义数据 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
public class CrmFormDataServiceImpl implements CrmFormDataService {

    @Resource
    private CrmFormDataMapper formDataMapper;

    @Override
    public Long createFormData(CrmFormDataSaveReqVO createReqVO) {
        CrmFormDataDO formData = getFormData(createReqVO.getFormModule(), createReqVO.getFormDataId());
        if(formData == null){
            formData = BeanUtils.toBean(createReqVO, CrmFormDataDO.class);
            formDataMapper.insert(formData);
        }else{
            formData = formData.setFormDataJson(createReqVO.getFormDataJson());
            formDataMapper.updateById(formData);
        }
        // 返回
        return formData.getId();
    }



    @Override
    public void deleteFormData(Long id) {
        // 校验存在
        validateFormDataExists(id);
        // 删除
        formDataMapper.deleteById(id);
    }

    private void validateFormDataExists(Long id) {
        if (formDataMapper.selectById(id) == null) {
            throw exception(FORM_DATA_NOT_EXISTS);
        }
    }

    @Override
    public CrmFormDataDO getFormData(String module,Long id) {
        return  formDataMapper.selectOne(new LambdaQueryWrapper<CrmFormDataDO>()
                .eq(CrmFormDataDO::getFormModule, module)
                .eq(CrmFormDataDO::getFormDataId, id));
    }



}