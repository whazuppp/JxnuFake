package com.mygroup5people.jxnufake.service.impl;

import com.mygroup5people.jxnufake.mapper.StuMapper;
import com.mygroup5people.jxnufake.pojo.LoginInfo;
import com.mygroup5people.jxnufake.pojo.Stu;
import com.mygroup5people.jxnufake.service.StuService;
import com.mygroup5people.jxnufake.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

}
