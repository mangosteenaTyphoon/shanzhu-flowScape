package com.shanzhu.handle;

import com.shanzhu.model.security.LoginUser;
import com.shanzhu.model.web.basecontroller.StrResponseController;
import com.shanzhu.utils.security.LoginUserManager;
import com.shanzhu.utils.web.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class LogoutSuccessHandlerImpl extends StrResponseController implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = WebUtils.getToken(request);

        LoginUser loginUser = LoginUserManager.getLoginUser(token);

        if (loginUser != null) {
            LoginUserManager.removeLoginUserCache(token);
        }

        WebUtils.renderJson(response, success("退出成功"));
    }
}
