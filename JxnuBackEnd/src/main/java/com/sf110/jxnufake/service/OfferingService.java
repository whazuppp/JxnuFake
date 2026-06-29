package com.mygroup5people.jxnufake.service;

import com.mygroup5people.jxnufake.dto.OfferingRequest;
import com.mygroup5people.jxnufake.vo.OfferingVO;
import com.mygroup5people.jxnufake.vo.StudentSummaryVO;
import java.util.List;

public interface OfferingService {
    List<OfferingVO> list(Integer semesterId, String courseName, String teacherName, Integer classId);
    OfferingVO get(Integer id);
    OfferingVO create(OfferingRequest request);
    OfferingVO update(Integer id, OfferingRequest request);
    void delete(Integer id);
    List<StudentSummaryVO> listStudents(Integer offeringId, Integer currentStudentId);
}
