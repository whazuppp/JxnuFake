package com.mygroup5people.jxnufake.mapper;

import com.mygroup5people.jxnufake.pojo.Stu;
import com.mygroup5people.jxnufake.vo.StudentInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StuMapper {
    Stu getByUsernameAndPassword(Stu stu);

    Stu getById(Integer id);

    StudentInfoVO getInfoById(Integer id);

    void updateImageById(@Param("id") Integer id, @Param("image") String image);

    void updatePassword(@Param("id") Integer id, @Param("newPassword") String newPassword);
}
