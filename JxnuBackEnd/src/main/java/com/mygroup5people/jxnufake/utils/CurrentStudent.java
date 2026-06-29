package com.mygroup5people.jxnufake.utils;

import com.mygroup5people.jxnufake.exception.BusinessException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

public final class CurrentStudent {
    private CurrentStudent() {
    }

    public static Integer id(HttpServletRequest request) {
        String token = request.getHeader("token");
        try {
            Claims claims = JwtUtils.parseToken(token);
            Object id = claims.get("id");
            if (id instanceof Number number) {
                return number.intValue();
            }
            return Integer.valueOf(String.valueOf(id));
        } catch (Exception exception) {
            throw new BusinessException("登录状态无效，请重新登录");
        }
    }
}
