// export default router;
import { createRouter, createWebHistory } from 'vue-router';

// 首页 & 登录
import HomeView from '@/views/index/index.vue';
import LoginView from '@/views/login/index.vue';
import LayoutView from '@/views/layout/index.vue';

// 一级模块页面
import StudentHomeView from '@/views/studenthome/index.vue';
import TeacherHomeView from '@/views/teacherhome/index.vue';
import OfficeOnlineView from '@/views/office/index.vue';
import TeachingOrgView from '@/views/teaching-org/index.vue';
import TeachingRulesView from '@/views/teaching-rules/index.vue';
import TeachingResourceView from '@/views/teaching-resource/index.vue';
import InteractionView from '@/views/interaction/index.vue';
import StudyCenterView from '@/views/studycenter/index.vue';

// 教学组织子页面
import OrgFunctionsView from '@/views/teaching-org/org-functions.vue';
import MonitoringView from '@/views/teaching-org/monitoring.vue';
import MajorAssessView from '@/views/teaching-org/major-assess.vue';
import ProjectReviewView from '@/views/teaching-org/project-review.vue';
import AuditView from '@/views/teaching-org/audit.vue';
import DataCollectView from '@/views/teaching-org/data-collect.vue';

// 教学资源子页面
import OnlinePlatformView from '@/views/teaching-resource/online-platform.vue';
import MicroCourseView from '@/views/teaching-resource/micro-course.vue';

// 教学互动子页面
import CourseDiscussionView from '@/views/interaction/course-discussion.vue';
import StuOpinionView from '@/views/interaction/stuopinion.vue';

// 学习中心子页面
import CourseSelectView from '@/views/studycenter/course-select.vue';
import EvaluationView from '@/views/studycenter/evaluation.vue';
import NavView from '@/views/studycenter/nav.vue';
import TutoringView from '@/views/studycenter/tutoring.vue';

// 学生之家子页面
import ScheduleView from '@/views/studenthome/schedule.vue';
import BaseInfoView from '@/views/studenthome/baseinfo.vue';
import ChangePasswordView from '@/views/studenthome/changepassword.vue';
import AcademicRecordView from '@/views/studenthome/academicrecord.vue';
import NewTeacherView from '@/views/studenthome/newteacher.vue';
import CourseFeedbackView from '@/views/studenthome/coursefeedback.vue';
import ParentInfoView from '@/views/studenthome/parentinfo.vue';
import DualDegreeView from '@/views/studenthome/dualdegree.vue';
import RelatedApplyView from '@/views/studenthome/relatedapply.vue';
import LearningExpView from '@/views/studenthome/learningexp.vue';
import ProgramView from '@/views/studenthome/program.vue';
import CourseInfoView from '@/views/studenthome/courseinfo.vue';
import TimetableView from '@/views/studenthome/timetable.vue';
import StudentInfoView from '@/views/studenthome/studentinfo.vue';
import ExamView from '@/views/studenthome/exam.vue';
import ShortMsgView from '@/views/studenthome/shortmsg.vue';
import ClassroomView from '@/views/studenthome/classroom.vue';
import DegreeAuditView from '@/views/studenthome/degreeaudit.vue';
import GraduationView from '@/views/studenthome/graduation.vue';
import MakeupView from '@/views/studenthome/makeup.vue';
import ScoreQueryView from '@/views/studenthome/scorequery.vue';
import JudgeView from '@/views/studenthome/judge.vue';
import QuestionBoxView from '@/views/studenthome/questionbox.vue';
import ExamArrangeView from '@/views/studenthome/examarrange.vue';
import DoubleMajorView from '@/views/studenthome/doublemajor.vue';
import OutSchoolView from '@/views/studenthome/outschool.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: LayoutView,
      redirect: '/index',
      children: [
        { path: 'index', name: 'home', component: HomeView },

        {
          path: 'studenthome',
          component: StudentHomeView,
          children: [
            { path: 'schedule', component: ScheduleView },
            { path: 'baseinfo', component: BaseInfoView },
            { path: 'changepassword', component: ChangePasswordView },
            { path: 'academicrecord', component: AcademicRecordView },
            { path: 'newteacher', component: NewTeacherView },
            { path: 'coursefeedback', component: CourseFeedbackView },
            { path: 'parentinfo', component: ParentInfoView },
            { path: 'dualdegree', component: DualDegreeView },
            { path: 'relatedapply', component: RelatedApplyView },
            { path: 'learningexp', component: LearningExpView },

            { path: 'program', component: ProgramView },
            { path: 'courseinfo', component: CourseInfoView },
            { path: 'timetable', component: TimetableView },
            { path: 'studentinfo', component: StudentInfoView },
            { path: 'exam', component: ExamView },
            { path: 'shortmsg', component: ShortMsgView },
            { path: 'classroom', component: ClassroomView },
            { path: 'degreeaudit', component: DegreeAuditView },
            { path: 'graduation', component: GraduationView },
            { path: 'makeup', component: MakeupView },
            { path: 'scorequery', component: ScoreQueryView },

            { path: 'judge', component: JudgeView },
            { path: 'questionbox', component: QuestionBoxView },
            { path: 'examarrange', component: ExamArrangeView },
            { path: 'doublemajor', component: DoubleMajorView },
            { path: 'outschool', component: OutSchoolView }
          ]
        },

        { path: 'teacherhome', name: 'teacherhome', component: TeacherHomeView },
        { path: 'office', name: 'officeonline', component: OfficeOnlineView },

        {
          path: 'teaching-org',
          component: TeachingOrgView,
          redirect: '/teaching-org/org-functions',
          children: [
            { path: 'org-functions', component: OrgFunctionsView },
            { path: 'monitoring', component: MonitoringView },
            { path: 'major-assess', component: MajorAssessView },
            { path: 'project-review', component: ProjectReviewView },
            { path: 'audit', component: AuditView },
            { path: 'data-collect', component: DataCollectView }
          ]
        },

        { path: 'teaching-rules', component: TeachingRulesView },

        {
          path: 'teaching-resource',
          component: TeachingResourceView,
          children: [
            { path: 'online-platform', component: OnlinePlatformView },
            { path: 'micro-course', component: MicroCourseView }
          ]
        },

        {
          path: 'interaction',
          component: InteractionView,
          children: [
            { path: 'course-discussion', component: CourseDiscussionView },
            { path: 'stuopinion', component: StuOpinionView }
          ]
        },

        {
          path: 'studycenter',
          component: StudyCenterView,
          children: [
            { path: 'course-select', component: CourseSelectView },
            { path: 'evaluation', component: EvaluationView },
            { path: 'nav', component: NavView },
            { path: 'tutoring', component: TutoringView }
          ]
        }
      ]
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    }
  ]
});

export default router;
