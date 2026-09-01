package co.yixiang.yshop.framework.web.core.filter;

import cn.hutool.core.util.StrUtil;
import co.yixiang.yshop.framework.common.pojo.CommonResult;
import co.yixiang.yshop.framework.common.util.servlet.ServletUtils;
import co.yixiang.yshop.framework.web.core.util.WebFrameworkUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static co.yixiang.yshop.framework.common.exception.enums.GlobalErrorCodeConstants.DEMO_DENY;

/**
 * 演示 Filter，禁止用户发起写操作，避免影响测试数据
 *
 * @author yshop
 */
public class DemoFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String method = request.getMethod();
        String path = request.getRequestURI();
        //app端不过滤
       // Boolean res = StrUtil.contains(path,"/app-api/");



        //系统模块特殊处理
        Boolean resForm = StrUtil.contains(path,"/admin-api/crm/form/create");
        if(resForm){
            return false;
        }
        Boolean res6 = StrUtil.contains(path,"/admin-api/crm/");
        if(res6 && !StrUtil.equalsAnyIgnoreCase(method,  "DELETE") ) {
            return true;
        }

        Boolean upload = StrUtil.contains(path,"/admin-api/infra/file/upload");
        if(upload) {
            return false;
        }

        Boolean scrm = StrUtil.contains(path,"/admin-api/stock/");
        if(scrm && !StrUtil.equalsAnyIgnoreCase(method,  "DELETE") ) {
            return true;
        }

        //https://apicrm.yixiang.co/admin-api/system/notify-message/update-read?ids=13
        Boolean res66 = StrUtil.contains(path,"/admin-api/product/store-product/isFormatAttr");
        Boolean res67 = StrUtil.contains(path,"/admin-api/system/notify-message/update-read");
        if(res66 || res67) {
            return true;
        }


        Boolean res3 = StrUtil.contains(path,"/admin-api/system/auth/logout");

        Boolean res4 = StrUtil.contains(path,"/admin-api/system/auth/login");

        Boolean res5 = StrUtil.contains(path,"/admin-api/system/captcha/get");

        return  res3 || res4 || res5  || !StrUtil.equalsAnyIgnoreCase(method,  "POST","PUT", "DELETE")  // 写操作时，不进行过滤率
                || WebFrameworkUtils.getLoginUserId(request) == null; // 非登录用户时，不进行过滤
    }

//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) {
//        String method = request.getMethod();
//        return !StrUtil.equalsAnyIgnoreCase(method, "POST", "PUT", "DELETE")  // 写操作时，不进行过滤率
//                || WebFrameworkUtils.getLoginUserId(request) == null; // 非登录用户时，不进行过滤
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        // 直接返回 DEMO_DENY 的结果。即，请求不继续
        ServletUtils.writeJSON(response, CommonResult.error(DEMO_DENY));
    }

}
