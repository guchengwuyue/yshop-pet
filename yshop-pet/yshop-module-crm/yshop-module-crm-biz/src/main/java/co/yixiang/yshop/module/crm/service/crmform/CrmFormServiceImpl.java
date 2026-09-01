package co.yixiang.yshop.module.crm.service.crmform;

import co.yixiang.yshop.framework.common.util.object.BeanUtils;
import co.yixiang.yshop.module.crm.controller.admin.crmform.vo.CrmFormSaveReqVO;
import co.yixiang.yshop.module.crm.dal.dataobject.crmform.CrmFormDO;
import co.yixiang.yshop.module.crm.dal.mysql.crmform.CrmFormMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * 表单 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
public class CrmFormServiceImpl implements CrmFormService {

    @Resource
    private CrmFormMapper formMapper;

    @Override
    public Long createForm(CrmFormSaveReqVO createReqVO) {
        CrmFormDO form = formMapper.selectOne(new LambdaQueryWrapper<CrmFormDO>()
                .eq(CrmFormDO::getFormModule, createReqVO.getFormModule()));
        if(form == null){
            // 插入
            form = BeanUtils.toBean(createReqVO, CrmFormDO.class);
            formMapper.insert(form);
        }else{
            //更新
            form = form.setFormGroupJson(createReqVO.getFormGroupJson());
            formMapper.updateById(form);
        }


        // 返回
        return form.getId();
    }


    @Override
    public CrmFormDO getForm(String module) {
        return  formMapper.selectOne(new LambdaQueryWrapper<CrmFormDO>()
                .eq(CrmFormDO::getFormModule, module));
    }



}