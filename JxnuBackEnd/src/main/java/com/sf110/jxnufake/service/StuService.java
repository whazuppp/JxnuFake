package com.sf110.jxnufake.service;

import com.sf110.jxnufake.pojo.LoginInfo;
import com.sf110.jxnufake.pojo.Stu;
import com.sf110.jxnufake.vo.StudentInfoVO;
import com.sf110.jxnufake.vo.StudentSummaryVO;

import java.util.List;

public interface StuService {
    LoginInfo login(Stu stu);
    void updateImage(Integer id, String imageUrl);
    boolean changePassword(Integer id, String oldPassword, String newPassword);
    Stu getById(Integer id);
    StudentInfoVO getInfo(Integer id);
    List<StudentSummaryVO> list(String field, String keyword, boolean exact);

}
