import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import axios from 'axios';

// // 添加请求拦截器
// axios.interceptors.request.use(
//   (config) => {
//     // 从 localStorage 中获取 token
//     const token = localStorage.getItem('jwt_token');
//     if (token) {
//       // 在请求头中添加 token 字段
//       config.headers.token = token;
//     }
//     return config;
//   },
//   (error) => {
//     // 请求错误处理
//     return Promise.reject(error);
//   }
// );

// 请求拦截：附加 token
axios.interceptors.request.use((config) => {
  const token = localStorage.getItem('jwt_token');
  if (token) config.headers.token = token;
  return config;
}, (error) => Promise.reject(error));

// 响应拦截：未登录统一处理
axios.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response?.status;
    if (status === 401) {
      localStorage.removeItem('jwt_token');
      // 友好提示并重定向
      // 使用 ElementUI 的消息需在组件里，这里只做跳转
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

Vue.use(ElementUI);

Vue.config.productionTip = false

// 设置 Axios 的全局 baseURL
axios.defaults.baseURL = 'http://localhost:8080';
// axios.defaults.baseURL = '/api';
axios.defaults.withCredentials = true;

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
