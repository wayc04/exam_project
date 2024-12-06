import axios from 'axios'
import {ElMessage} from 'element-plus'

const http = axios.create({
    baseURL: 'http://121.37.0.57:8080',
    // baseURL: 'http://localhost:8080',
    timeout:10000,
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
      Accept: 'application/json'
    }
})

// 添加拦截器
http.interceptors.request.use(function (config) {
    // 在发送请求之前做些什么
    const token = localStorage.getItem('token')
    // 不需要添加token的api
    const whiteUrl = ['/stulogin','/register','/login' ,'/deleteSubject','/findCourseById']
    if(token && !whiteUrl.includes(config.url)){
      config.headers['token'] = token
      
    }
    return config;
  }, function (error) {
    // 对请求错误做些什么
    return Promise.reject(error);
});

  // 添加响应拦截器
http.interceptors.response.use(function (response) {
    // 对接口异常的数据，给用户提示
    if(response.data.code === -1){
      ElMessage.warning(response.data.message)
    }
    if(response.data.code === -2){
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      window.location.href = window.location.origin
    }
    return response;
  }, function (error) {
    // 超出 2xx 范围的状态码都会触发该函数。
    // 对响应错误做点什么
    return Promise.reject(error);
});

export default http