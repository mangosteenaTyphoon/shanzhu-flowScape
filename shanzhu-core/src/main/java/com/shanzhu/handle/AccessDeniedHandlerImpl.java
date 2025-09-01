package com.shanzhu.handle;

import com.shanzhu.enums.ResultCodeEnum;
import com.shanzhu.model.web.basecontroller.StrResponseController;
import com.shanzhu.utils.web.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * 用户访问权限不足处理
 * <p>
 * 因配置了全局异常处理
 * 请在 GlobalExceptionHandle.handleRuntimeException 进行配置
 */
@Component
public class AccessDeniedHandlerImpl extends StrResponseController implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        WebUtils.renderJson(response,error(ResultCodeEnum.ACCESS_ERROR));
    }
}
