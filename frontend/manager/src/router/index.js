import Vue from 'vue'
import VueRouter from 'vue-router'
// import HomeView from '../views/HomeView.vue'
import UserView from '../views/element/UserView.vue'
import QuestionView from '../views/element/QuestionView.vue'
import LoginView from '@/views/element/LoginView.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/user',
    name: 'user',
    component: UserView,
    meta: {
      requiresAuth: true // 需要登录才能访问
    }
  },
  {
    path: '/question',
    name: 'question',
    component: QuestionView,
    meta: {
      requiresAuth: true // 需要登录才能访问
    }
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView
  },
  
  // {
  //   path: '/',
  //   name: 'home',
  //   component: HomeView
  // },
  // {
  //   path: '/about',
  //   name: 'about',
  //   // route level code-splitting
  //   // this generates a separate chunk (about.[hash].js) for this route
  //   // which is lazy-loaded when the route is visited.
  //   component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  // }
]

const router = new VueRouter({
  routes
})

// 全局导航守卫
router.beforeEach((to, from, next) => {
  // 检查路由是否需要登录
  if (to.matched.some(record => record.meta.requiresAuth)) {
    const token = localStorage.getItem('jwt_token'); // 从 localStorage 获取 token
    if (!token) {
      // 如果没有 token，跳转到登录页面
      next('/login');
    } else {
      // 如果有 token，继续访问目标页面
      next();
    }
  } else {
    // 如果路由不需要登录，直接继续
    next();
  }
});

export default router
