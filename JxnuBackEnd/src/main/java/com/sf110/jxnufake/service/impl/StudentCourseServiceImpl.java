package com.sf110.jxnufake.service.impl;

import com.sf110.jxnufake.entity.Semester;
import com.sf110.jxnufake.exception.BusinessException;
import com.sf110.jxnufake.mapper.OfferingMapper;
import com.sf110.jxnufake.mapper.ReferenceMapper;
import com.sf110.jxnufake.mapper.StuMapper;
import com.sf110.jxnufake.mapper.StudentCourseMapper;
import com.sf110.jxnufake.service.StudentCourseService;
import com.sf110.jxnufake.vo.OfferingVO;
import com.sf110.jxnufake.vo.StudentInfoVO;
import com.sf110.jxnufake.vo.TimetableVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentCourseServiceImpl implements StudentCourseService {
    private final StudentCourseMapper studentCourseMapper;
    private final OfferingMapper offeringMapper;
    private final StuMapper stuMapper;
    private final ReferenceMapper referenceMapper;

    public List<OfferingVO> list(Integer studentId, Integer semesterId) {
        List<OfferingVO> offerings = studentCourseMapper.listSelections(studentId, semesterId);
        offerings.forEach(o -> o.setSchedules(offeringMapper.listSchedules(o.getId())));
        return offerings;
    }

    @Transactional
    public void select(Integer studentId, Integer offeringId) {
        OfferingVO offering = studentCourseMapper.lockOffering(offeringId);

        if (offering == null) {
            throw new BusinessException("开课班不存在");
        }

        StudentInfoVO student = stuMapper.getInfoById(studentId);
        if (student == null) {
            throw new BusinessException("学生不存在");
        }
        if (!Objects.equals(student.classId(), offering.getClassId())) {
            throw new BusinessException("所选课程与班级不匹配");
        }

        if (studentCourseMapper.countSelectedOffering(studentId, offeringId) > 0) {
            throw new BusinessException("不能重复选择同一开课班");
        }

        if (studentCourseMapper.countSelectedCourseInSemester(
                studentId,
                offering.getCourseId(),
                offering.getSemesterId()
        ) > 0) {
            throw new BusinessException("同一学期内同一课程只能选择一个开课班");
        }

        if (offering.getStudentCount() >= offering.getCapacity()) {
            throw new BusinessException("课程容量已满");
        }

        studentCourseMapper.insert(studentId, offeringId);
        log.info("学生 {} 选择开课班 {}", studentId, offeringId);
    }

    @Transactional
    public void withdraw(Integer studentId, Integer offeringId) {
        if (studentCourseMapper.deleteOwned(studentId, offeringId) == 0) {
            throw new BusinessException("未选择该开课班，无法退课");
        }

        log.info("学生 {} 退出开课班 {}", studentId, offeringId);
    }

    public TimetableVO timetable(Integer studentId, Integer semesterId) {
        StudentInfoVO student = stuMapper.getInfoById(studentId);
        if (student == null) {
            throw new BusinessException("学生不存在");
        }

        Semester semester = referenceMapper.selectSemesterById(semesterId);
        if (semester == null) {
            throw new BusinessException("学期不存在");
        }

        TimetableVO result = new TimetableVO();
        result.setStudentId(student.id());
        result.setStudentNo(student.stuId());
        result.setStudentName(student.name());
        result.setClassName(student.className());
        result.setSemesterId(semester.getId());
        result.setSemesterName(semester.getName());
        result.setCourses(list(studentId, semesterId));
        return result;
    }
}
