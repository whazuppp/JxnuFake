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
import TeachingResourceView from '@/views/teaching-resource/index.vue';
import InteractionView from '@/views/interaction/index.vue';
import StudyCenterView from '@/views/studycenter/index.vue';

// 教学组织子页面
import OrgFunctionsView from '@/views/teaching-org/org-functions.vue';

// 教学资源子页面
import OnlinePlatformView from '@/views/teaching-resource/online-platform.vue';
import MicroCourseView from '@/views/teaching-resource/micro-course.vue';

// 教学互动子页面
import StuOpinionView from '@/views/interaction/stuopinion.vue';

// 学习中心子页面
import CourseSelectView from '@/views/studycenter/course-select.vue';
import TutoringView from '@/views/studycenter/tutoring.vue';

// 学生之家子页面
import ScheduleView from '@/views/studenthome/schedule.vue';
import BaseInfoView from '@/views/studenthome/baseinfo.vue';
import ChangePasswordView from '@/views/studenthome/changepassword.vue';
import NewTeacherView from '@/views/studenthome/newteacher.vue';
import DualDegreeView from '@/views/studenthome/dualdegree.vue';
import RelatedApplyView from '@/views/studenthome/relatedapply.vue';
import ProgramView from '@/views/studenthome/program.vue';
import StudentInfoView from '@/views/studenthome/studentinfo.vue';
import TeacherInfoView from '@/views/studenthome/teacherinfo.vue';
import GraduationView from '@/views/studenthome/graduation.vue';
import JudgeView from '@/views/studenthome/judge.vue';
import ExamArrangeView from '@/views/studenthome/examarrange.vue';
import { readLoginUser } from '@/utils/auth';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: LayoutView,
      meta: { requiresAuth: true },
      redirect: '/index',
      children: [
        { path: 'index', name: 'home', component: HomeView },

        {
          path: 'studenthome',
          component: StudentHomeView,
          redirect: '/studenthome/schedule',
          children: [
            { path: 'schedule', component: ScheduleView },
            { path: 'baseinfo', component: BaseInfoView },
            { path: 'changepassword', component: ChangePasswordView },
            { path: 'newteacher', component: NewTeacherView },
            { path: 'dualdegree', component: DualDegreeView },
            { path: 'relatedapply', component: RelatedApplyView },

            { path: 'program', component: ProgramView },
            { path: 'studentinfo', component: StudentInfoView },
            { path: 'teacherinfo', component: TeacherInfoView },
            { path: 'graduation', component: GraduationView },

            { path: 'judge', component: JudgeView },
            { path: 'examarrange', component: ExamArrangeView }
          ]
        },

        { path: 'teacherhome', name: 'teacherhome', component: TeacherHomeView },
        { path: 'office', name: 'officeonline', component: OfficeOnlineView },

        {
          path: 'teaching-org',
          component: TeachingOrgView,
          redirect: '/teaching-org/org-functions',
          children: [
            { path: 'org-functions', component: OrgFunctionsView }
          ]
        },

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
            { path: 'stuopinion', component: StuOpinionView }
          ]
        },

        {
          path: 'studycenter',
          component: StudyCenterView,
          children: [
            { path: 'course-select', component: CourseSelectView },
            { path: 'tutoring', component: TutoringView }
          ]
        }
      ]
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    { path: '/:pathMatch(.*)*', redirect: '/index' }
  ]
});

router.beforeEach((to) => {
  const authenticated = Boolean(readLoginUser()?.token)
  if (to.matched.some(record => record.meta.requiresAuth) && !authenticated) return '/login'
  if (to.path === '/login' && authenticated) return '/studenthome/schedule'
})

export default router;
