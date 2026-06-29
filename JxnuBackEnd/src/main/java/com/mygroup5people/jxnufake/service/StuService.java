package com.mygroup5people.jxnufake.service;

import com.mygroup5people.jxnufake.pojo.LoginInfo;
import com.mygroup5people.jxnufake.pojo.Stu;

public interface StuService {
    LoginInfo login(Stu stu);
    void updateImage(Integer id, String imageUrl);
    boolean changePassword(Integer id, String oldPassword, String newPassword);
    Stu getById(Integer id);

}
