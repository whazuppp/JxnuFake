package com.sf110.jxnufake.service.impl;

import com.sf110.jxnufake.dto.OfferingRequest;
import com.sf110.jxnufake.dto.ScheduleRequest;
import com.sf110.jxnufake.entity.Course;
import com.sf110.jxnufake.entity.CourseOffering;
import com.sf110.jxnufake.entity.CourseSchedule;
import com.sf110.jxnufake.exception.BusinessException;
import com.sf110.jxnufake.mapper.CourseMapper;
import com.sf110.jxnufake.mapper.OfferingMapper;
import com.sf110.jxnufake.mapper.ReferenceMapper;
import com.sf110.jxnufake.mapper.TeacherMapper;
import com.sf110.jxnufake.service.OfferingService;
import com.sf110.jxnufake.vo.OfferingVO;
import com.sf110.jxnufake.vo.StudentSummaryVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OfferingServiceImpl implements OfferingService {
    private final OfferingMapper offeringMapper;
    private final CourseMapper courseMapper;
    private final TeacherMapper teacherMapper;
    private final ReferenceMapper referenceMapper;

    public List<OfferingVO> list(Integer semesterId, String courseName, String teacherName, Integer classId) {
        List<OfferingVO> result = offeringMapper.list(semesterId, courseName, teacherName, classId);
        result.forEach(this::attachSchedules);
        return result;
    }

    public OfferingVO get(Integer id) {
        OfferingVO offering = requireOffering(id);
        attachSchedules(offering);
        return offering;
    }

    @Transactional
    public OfferingVO create(OfferingRequest request) {
        Course course = validateReferences(request);
        validateSchedules(request, course, null);
        CourseOffering offering = toEntity(request);
        offering.setCreateTime(LocalDateTime.now());
        offering.setUpdateTime(LocalDateTime.now());
        offeringMapper.insertOffering(offering);
        insertSchedules(offering.getId(), request.getSchedules());
        log.info("新增开课班: {}", offering.getId());
        return get(offering.getId());
    }

    @Transactional
    public OfferingVO update(Integer id, OfferingRequest request) {
        requireOffering(id);
        Course course = validateReferences(request);
        validateSchedules(request, course, id);
        CourseOffering offering = toEntity(request);
        offering.setId(id);
        offering.setUpdateTime(LocalDateTime.now());
        offeringMapper.updateOffering(offering);
        offeringMapper.deleteSchedules(id);
        insertSchedules(id, request.getSchedules());
        log.info("修改开课班: {}", id);
        return get(id);
    }

    @Transactional
    public void delete(Integer id) {
        requireOffering(id);
        if (offeringMapper.countEnrollments(id) > 0) {
            throw new BusinessException("开课班已有选课记录，不能删除");
        }
        offeringMapper.deleteOffering(id);
    }

    public List<StudentSummaryVO> listStudents(Integer offeringId, Integer currentStudentId) {
        requireOffering(offeringId);
        if (offeringMapper.countStudentEnrollment(currentStudentId, offeringId) == 0) {
            throw new BusinessException("只能查看本人已选开课班的学生名单");
        }
        return offeringMapper.listStudents(offeringId);
    }

    private Course validateReferences(OfferingRequest request) {
        Course course = courseMapper.selectById(request.getCourseId());
        if (course == null) throw new BusinessException("课程不存在");
        if (teacherMapper.selectById(request.getTeacherId()) == null) throw new BusinessException("教师不存在");
        if (referenceMapper.selectClassById(request.getClassId()) == null) throw new BusinessException("行政班不存在");
        if (referenceMapper.selectSemesterById(request.getSemesterId()) == null) throw new BusinessException("学期不存在");
        for (ScheduleRequest schedule : request.getSchedules()) {
            if (referenceMapper.selectClassroomById(schedule.getClassroomId()) == null) {
                throw new BusinessException("教室不存在");
            }
        }
        return course;
    }

    private void validateSchedules(OfferingRequest request, Course course, Integer excludeId) {
        int total = 0;
        for (ScheduleRequest schedule : request.getSchedules()) {
            int length = schedule.getEndPeriod() - schedule.getStartPeriod() + 1;
            if (schedule.getWeekday() < 1 || schedule.getWeekday() > 7 || schedule.getStartPeriod() < 1
                    || schedule.getEndPeriod() > 11 || (length != 2 && length != 3)) {
                throw new BusinessException("每个排课时段必须连续占用2或3节");
            }
            total += length;
        }
        if (total != course.getWeeklyPeriods()) throw new BusinessException("排课总节数必须等于课程每周课时");
        for (ScheduleRequest s : request.getSchedules()) {
            if (offeringMapper.countClassroomConflicts(request.getSemesterId(), s.getWeekday(), s.getStartPeriod(),
                    s.getEndPeriod(), s.getClassroomId(), excludeId) > 0)
                throw new BusinessException("教室在所选时间段已被占用");
            if (offeringMapper.countTeacherConflicts(request.getSemesterId(), s.getWeekday(), s.getStartPeriod(),
                    s.getEndPeriod(), request.getTeacherId(), excludeId) > 0)
                throw new BusinessException("教师在所选时间段已有课程");
            if (offeringMapper.countClassConflicts(request.getSemesterId(), s.getWeekday(), s.getStartPeriod(),
                    s.getEndPeriod(), request.getClassId(), excludeId) > 0)
                throw new BusinessException("行政班在所选时间段已有课程");
        }
    }

    private CourseOffering toEntity(OfferingRequest request) {
        CourseOffering o = new CourseOffering();
        o.setCourseId(request.getCourseId()); o.setTeacherId(request.getTeacherId());
        o.setClassId(request.getClassId()); o.setSemesterId(request.getSemesterId()); o.setCapacity(request.getCapacity());
        return o;
    }

    private void insertSchedules(Integer offeringId, List<ScheduleRequest> requests) {
        for (ScheduleRequest r : requests) {
            CourseSchedule s = new CourseSchedule();
            s.setOfferingId(offeringId); s.setWeekday(r.getWeekday()); s.setStartPeriod(r.getStartPeriod());
            s.setEndPeriod(r.getEndPeriod()); s.setClassroomId(r.getClassroomId());
            offeringMapper.insertSchedule(s);
        }
    }

    private OfferingVO requireOffering(Integer id) {
        OfferingVO offering = offeringMapper.selectById(id);
        if (offering == null) throw new BusinessException("开课班不存在");
        return offering;
    }

    private void attachSchedules(OfferingVO offering) {
        offering.setSchedules(offeringMapper.listSchedules(offering.getId()));
    }
}
