package com.sf110.jxnufake.controller;

import com.sf110.jxnufake.pojo.LoginInfo;
import com.sf110.jxnufake.pojo.Result;
import com.sf110.jxnufake.pojo.Stu;
import com.sf110.jxnufake.service.StuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController {
    @Autowired
    private StuService st;
    @PostMapping("/stu/login")
    public Result login(@RequestBody Stu stu){
        log.info("登录: {}", stu);
        LoginInfo info =  st.login(stu);
        if (info != null){
            return Result.success(info);
        }
        return Result.error("用户名或密码错误");
    }

}