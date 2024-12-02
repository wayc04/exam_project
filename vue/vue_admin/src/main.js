import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import './style.css'
import * as ElementPlus from '@element-plus/icons-vue'
import store from './store'

router.beforeEach((to, from) => {
  const userinfo = localStorage.getItem('userInfo')
  if(!userinfo && to.path !== '/login') {
    return '/login'
  } else if (userinfo && to.path === '/login') {
    return '/'
  } else {
    return true
  }
})

const app = createApp(App)

for (const key in ElementPlus) {
  if (Object.hasOwnProperty.call(ElementPlus, key)) {
    const element = ElementPlus[key]
    app.component(key, element)
  }
}

// 挂载路由
app.use(router)

app.use(store)

app.mount('#app')
