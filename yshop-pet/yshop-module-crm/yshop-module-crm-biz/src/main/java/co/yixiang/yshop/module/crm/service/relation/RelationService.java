package co.yixiang.yshop.module.crm.service.relation;

import co.yixiang.yshop.framework.security.core.util.SecurityFrameworkUtils;
import co.yixiang.yshop.module.crm.enums.RelationEnum;
import co.yixiang.yshop.module.system.api.user.AdminUserApi;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RelationService {
    @Resource
    private AdminUserApi adminUserApi;

    /**
     * 根据关系获取用户列表
     * @param relation
     * @return
     */
    public List<Long> getUserListIds(String relation) {
        List<Long> ids = new ArrayList<>();
        Long loginAdminId = SecurityFrameworkUtils.getLoginUserId();
        if(RelationEnum.MY.getValue().equals(relation)){
            ids.add(loginAdminId);
        }else if(RelationEnum.SUB.getValue().equals(relation)){
            ids = adminUserApi.getUserListBySubordinateIds(loginAdminId);
        }

        return ids;
    }

}
