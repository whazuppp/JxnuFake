package com.mygroup5people.jxnufake.controller;

import com.mygroup5people.jxnufake.pojo.LoginInfo;
import com.mygroup5people.jxnufake.pojo.Result;
import com.mygroup5people.jxnufake.pojo.Stu;
import com.mygroup5people.jxnufake.service.StuService;
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