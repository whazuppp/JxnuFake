package com.mygroup5people.jxnufake.controller;

import com.mygroup5people.jxnufake.pojo.Result;
import com.mygroup5people.jxnufake.service.StuService;
import com.mygroup5people.jxnufake.utils.AliOSSUtils;
import com.mygroup5people.jxnufake.utils.CurrentStudent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

@Slf4j
@RestController
public class StuController {
    @Autowired
    private StuService stuService;

    @Autowired
    private AliOSSUtils ali;
    @PostMapping("/stu/uploadAvatar")
    public Result uploadAvatar(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
        log.info("头像上传：{}", file.getOriginalFilename());

        // 从 token 中解析当前学生 ID
        Integer stuId = CurrentStudent.id(request);

        // 上传图片至 OSS
        String imageUrl = ali.upload(file);
        log.info("上传成功 URL：{}", imageUrl);

        // 更新数据库中的头像地址
        stuService.updateImage(stuId, imageUrl);

        return Result.success(imageUrl);
    }
    @PostMapping("/stu/changePassword")
    public Result changePassword(@RequestBody Map<String, String> map, HttpServletRequest request)throws Exception {
        String oldPassword = map.get("oldPassword");
        String newPassword = map.get("newPassword");

        // 从 token 中解析当前登录学生 id
        Integer stuId = CurrentStudent.id(request);

        boolean updated = stuService.changePassword(stuId, oldPassword, newPassword);
        if (updated) {
            return Result.success("修改成功");
        } else {
            return Result.error("原密码错误");
        }
    }
    @GetMapping("/student/info")
    public Result getStudentInfo(HttpServletRequest request) {
        return Result.success(stuService.getInfo(CurrentStudent.id(request)));
    }


}
