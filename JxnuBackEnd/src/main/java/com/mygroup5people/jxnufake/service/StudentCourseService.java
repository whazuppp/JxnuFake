package com.mygroup5people.jxnufake.service;

import com.mygroup5people.jxnufake.vo.OfferingVO;
import com.mygroup5people.jxnufake.vo.TimetableVO;
import java.util.List;

public interface StudentCourseService {
    List<OfferingVO> list(Integer studentId, Integer semesterId);
    void select(Integer studentId, Integer offeringId);
    void withdraw(Integer studentId, Integer offeringId);
    TimetableVO timetable(Integer studentId, Integer semesterId);
}
