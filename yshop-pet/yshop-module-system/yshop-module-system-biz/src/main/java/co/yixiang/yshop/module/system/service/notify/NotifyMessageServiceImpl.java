package co.yixiang.yshop.module.system.service.notify;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.common.util.object.BeanUtils;
import co.yixiang.yshop.module.system.controller.admin.notify.vo.message.NotifyMessageMyPageReqVO;
import co.yixiang.yshop.module.system.controller.admin.notify.vo.message.NotifyMessagePageReqVO;
import co.yixiang.yshop.module.system.controller.admin.notify.vo.message.NotifyMessageRespVO;
import co.yixiang.yshop.module.system.dal.dataobject.notify.NotifyMessageDO;
import co.yixiang.yshop.module.system.dal.dataobject.notify.NotifyTemplateDO;
import co.yixiang.yshop.module.system.dal.mysql.notify.NotifyMessageMapper;
import co.yixiang.yshop.module.system.dal.mysql.notify.NotifyTemplateMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 站内信 Service 实现类
 *
 * @author xrcoder
 */
@Service
@Validated
public class NotifyMessageServiceImpl implements NotifyMessageService {

    @Resource
    private NotifyMessageMapper notifyMessageMapper;
    @Resource
    private NotifyTemplateMapper notifyTemplateMapper;

    @Override
    public Long createNotifyMessage(Long userId, Integer userType,
                                    NotifyTemplateDO template, String templateContent, Map<String, Object> templateParams) {
        NotifyMessageDO message = new NotifyMessageDO().setUserId(userId).setUserType(userType)
                .setTemplateId(template.getId()).setTemplateCode(template.getCode())
                .setTemplateType(template.getType()).setTemplateNickname(template.getNickname())
                .setTemplateContent(templateContent).setTemplateParams(templateParams).setReadStatus(false);
        notifyMessageMapper.insert(message);
        return message.getId();
    }

    @Override
    public PageResult<NotifyMessageDO> getNotifyMessagePage(NotifyMessagePageReqVO pageReqVO) {
        return notifyMessageMapper.selectPage(pageReqVO);
    }

    @Override
    public PageResult<NotifyMessageRespVO> getMyMyNotifyMessagePage(NotifyMessageMyPageReqVO pageReqVO, Long userId, Integer userType) {

        PageResult<NotifyMessageDO> pageResult = notifyMessageMapper.selectPage(pageReqVO, userId, userType);
        PageResult<NotifyMessageRespVO> messageRespVO = BeanUtils.toBean(pageResult, NotifyMessageRespVO.class);
        //返回模板名称
        messageRespVO.getList().forEach(message -> {
            //获取模板
            NotifyTemplateDO template = notifyTemplateMapper.selectById(message.getTemplateId());

            message.setTemplateName(template.getName());
            message.setPcUrl(template.getPcUrl());
            message.setMobileUrl(template.getMobileUrl());
        });


        return messageRespVO;
    }

    @Override
    public NotifyMessageDO getNotifyMessage(Long id) {
        return notifyMessageMapper.selectById(id);
    }

    @Override
    public List<NotifyMessageDO> getUnreadNotifyMessageList(Long userId, Integer userType, Integer size) {
        return notifyMessageMapper.selectUnreadListByUserIdAndUserType(userId, userType, size);
    }

    @Override
    public Long getUnreadNotifyMessageCount(Long userId, Integer userType) {
        return notifyMessageMapper.selectUnreadCountByUserIdAndUserType(userId, userType);
    }

    @Override
    public int updateNotifyMessageRead(Collection<Long> ids, Long userId, Integer userType) {
        return notifyMessageMapper.updateListRead(ids, userId, userType);
    }

    @Override
    public int updateAllNotifyMessageRead(Long userId, Integer userType) {
        return notifyMessageMapper.updateListRead(userId, userType);
    }

}
