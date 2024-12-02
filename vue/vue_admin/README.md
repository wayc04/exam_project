### Vue learning

---

#### router

`npm install vue-router`

创建路由和界面

路由模式：

+ Hash模式：路由切换不刷新页面
+ Memory模式：无法前进后退
+ HTML5模式：常见

```javascript
// main.js
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

const app = createApp(App)

// 挂载路由
app.use(router)

app.mount('#app')

```

```vue
// app.vue
<script setup>
</script>

<template>
  <RouterView />
</template>

<style scoped>

</style>

```

```javascript
// router/index.js
import {createRouter, createWebHashHistory} from 'vue-router'


import Layout from '../views/Main.vue'
import Login from '../views/login/index.vue'


const routes = [
    {
        path:'/',
        component: Layout
    },
    {
        path:'/login',
        component: Login
    }
]

const router = createRouter({
    routes,
    history: createWebHashHistory()
})

export default router
```

#### Element-UI 按需注入

`npm install element-plus`

按需注入

`npm install -D unplugin-vue-components unplugin-auto-import`

在 `vite.config.js`中导入：

```js
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

// https://vite.dev/config/


export default defineConfig({
  // ...
  plugins: [
    // ...
    vue(),
    AutoImport({
      resolvers: [ElementPlusResolver()],
    }),
    Components({
      resolvers: [ElementPlusResolver()],
    }),
  ],
})
```

#### less

`npm install less`

#### ICON

`npm install @element-plus/icons-vue`

#### VUEX

采用vuex实现跨组件间的通信。

`npm install vuex@next`

创建新文件夹store

创建store实例：



Axios

`npm install axios`
