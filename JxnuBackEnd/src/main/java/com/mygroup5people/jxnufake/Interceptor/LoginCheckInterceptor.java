package com.mygroup5people.jxnufake.Interceptor;

import com.mygroup5people.jxnufake.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
//自定义拦截器
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {
    //目标资源方法执行前执行。 返回true：放行    返回false：不放行
    /*
    - postHandle方法：目标资源方法执行后执行
    - afterCompletion方法：视图渲染完毕后执行，最后执行
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1. 获取到请求路径
        String requestURI = request.getRequestURI(); // /employee/login

        //2. 判断是否是登录请求, 如果路径中包含 /login, 说明是登录操作, 放行  后面都是不是登录请求的情况
        if (requestURI.contains("/login")){
            log.info("登录请求, 放行");
            return true;
        }

        //3. 获取请求头中的token
        String token = request.getHeader("token");

        log.info("前端传来的token: {}", token);

        //4. 判断token是否存在, 如果不存在, 说明用户没有登录, 返回错误信息(响应401状态码)
        if (token == null || token.isEmpty()){
            log.info("令牌为空, 响应401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        //5. 如果token存在, 校验令牌, 如果校验失败 -> 返回错误信息(响应401状态码)
        try {
            JwtUtils.parseToken(token);
        } catch (Exception e) {
            log.info("令牌非法, 响应401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        //6. 校验通过, 放行
        log.info("令牌合法, 放行");
        return true;
    }
}
