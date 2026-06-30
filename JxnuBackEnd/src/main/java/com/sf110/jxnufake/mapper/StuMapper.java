package com.sf110.jxnufake.mapper;

import com.sf110.jxnufake.pojo.Stu;
import com.sf110.jxnufake.vo.StudentInfoVO;
import com.sf110.jxnufake.vo.StudentSummaryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StuMapper {
    Stu getByUsernameAndPassword(Stu stu);

    Stu getById(Integer id);

    StudentInfoVO getInfoById(Integer id);

    List<StudentSummaryVO> list(@Param("field") String field,
                                @Param("keyword") String keyword,
                                @Param("exact") boolean exact);

    void updateImageById(@Param("id") Integer id, @Param("image") String image);

    void updatePassword(@Param("id") Integer id, @Param("newPassword") String newPassword);
}
