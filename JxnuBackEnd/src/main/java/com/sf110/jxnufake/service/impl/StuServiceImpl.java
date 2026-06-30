package com.sf110.jxnufake.service.impl;

import com.sf110.jxnufake.mapper.StuMapper;
import com.sf110.jxnufake.pojo.LoginInfo;
import com.sf110.jxnufake.pojo.Stu;
import com.sf110.jxnufake.service.StuService;
import com.sf110.jxnufake.utils.JwtUtils;
import com.sf110.jxnufake.vo.StudentInfoVO;
import com.sf110.jxnufake.vo.StudentSummaryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class StuServiceImpl implements StuService {
    @Autowired
    private StuMapper stuMapper;

//    @Override
//    public LoginInfo login(Stu stu) {
//        Stu s = stuMapper.getByUsernameAndPassword(stu);
//        if (s != null) {
//            Map<String, Object> claims = new HashMap<>();
//            claims.put("id", s.getId());
//            claims.put("stuId", s.getStuId());
//            String token = JwtUtils.generateToken(claims);
//            s.setUsername(null);
//            return new LoginInfo(s.getId(), s.getStuId().toString(), s.getName(), token);
//        }
//        return null;
//    }
@Override
public LoginInfo login(Stu stu) {
    Stu s = stuMapper.getByUsernameAndPassword(stu);
    if (s != null) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", s.getId());
        claims.put("stuId", s.getStuId()); // 放进 token 中也行

        String token = JwtUtils.generateToken(claims);

        return new LoginInfo(
                s.getId(),                      // Integer
                s.getStuId(),                   // String
                s.getUsername(),                // String
                s.getName(),                    // String
                token                           // String
        );

    }
    return null;
}

    @Override
    public void updateImage(Integer id, String imageUrl) {
        stuMapper.updateImageById(id, imageUrl);
    }
    @Override
    public boolean changePassword(Integer id, String oldPassword, String newPassword) {
        Stu stu = stuMapper.getById(id);
        if (stu != null && stu.getPassword().equals(oldPassword)) {
            stuMapper.updatePassword(id, newPassword);
            return true;
        }
        return false;
    }
    @Override
    public Stu getById(Integer id) {
        return stuMapper.getById(id);
    }

    @Override
    public StudentInfoVO getInfo(Integer id) {
        return stuMapper.getInfoById(id);
    }

    @Override
    public List<StudentSummaryVO> list(String field, String keyword, boolean exact) {
        return stuMapper.list(field, keyword, exact);
    }

}
