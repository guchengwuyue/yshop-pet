package co.yixiang.yshop.module.crm.functions;

import cn.hutool.core.util.StrUtil;
import co.yixiang.yshop.module.system.api.user.AdminUserApi;
import co.yixiang.yshop.module.system.api.user.dto.AdminUserRespDTO;
import com.mzt.logapi.service.IParseFunction;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 获取员工名字的实现类
 *
 * @author yshop
 */
@Slf4j
@Component
public class AdminUserFunction implements IParseFunction {

    public static final String NAME = "getAdminUserName";

    @Resource
    private AdminUserApi adminUserApi;

    @Override
    public String functionName() {
        return NAME;
    }

    @Override
    public String apply(Object value) {
        if (StrUtil.isEmptyIfStr(value)) {
            return "";
        }

        AdminUserRespDTO user = adminUserApi.getUser(Long.parseLong(value.toString()));
        return  user.getNickname();
    }

}
