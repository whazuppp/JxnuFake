package com.sf110.jxnufake.service;

import com.sf110.jxnufake.vo.OfferingVO;
import com.sf110.jxnufake.vo.TimetableVO;
import java.util.List;

public interface StudentCourseService {
    List<OfferingVO> list(Integer studentId, Integer semesterId);
    void select(Integer studentId, Integer offeringId);
    void withdraw(Integer studentId, Integer offeringId);
    TimetableVO timetable(Integer studentId, Integer semesterId);
}
