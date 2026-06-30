package com.sf110.jxnufake.mapper;

import com.sf110.jxnufake.vo.OfferingVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface StudentCourseMapper {
    OfferingVO lockOffering(Integer offeringId);
    Integer countSelectedOffering(@Param("studentId") Integer studentId, @Param("offeringId") Integer offeringId);
    Integer countSelectedCourseInSemester(@Param("studentId") Integer studentId, @Param("courseId") Integer courseId,
                                @Param("semesterId") Integer semesterId);
    void insert(@Param("studentId") Integer studentId, @Param("offeringId") Integer offeringId);
    Integer deleteOwned(@Param("studentId") Integer studentId, @Param("offeringId") Integer offeringId);
    List<OfferingVO> listSelections(@Param("studentId") Integer studentId, @Param("semesterId") Integer semesterId);
}
