import {createRouter, createWebHashHistory} from 'vue-router'


import Layout from '../views/Main.vue'
import Login from '../views/login/index.vue'
import Dashboard from '../views/dashboard/index.vue'
import Class from '../views/class/class/index.vue'
import Group from '../views/class/group/index.vue'




const routes = [
    {
        path:'/',
        component: Layout,
        name:'main',
        children: [
            // {
            //     path: 'dashboard',
            //     meta: { id: '1', name: '控制台', icon: 'Platform', path: '/dashboard', describe: '用于展示当前系统中的统计数据、统计报表及重要实时数据' },
            //     component: Dashboard
            // },
            {
                path: 'class',
                meta: { id: '2', name: '考试管理', icon: 'Grid' },
                children: [
                    // {
                    //     path: '',
                    //     alias: ['class'],
                    //     meta: { id: '1', name: '班级信息', icon: 'Avatar', path: '/class/class', describe: '管理员可以进行编辑，权限修改后需要登出才会生效' },
                    //     component: Class
                    // }
                    // ,
                    {
                        path: 'group',
                        meta: { id: '2', name: '考试信息', icon: 'Menu', path: '/class/group', describe: '菜单规则通常对应一个控制器的方法,同时菜单栏数据也从规则中获取' },
                        component: Group
                    }
                ]
            }
        ]
    },
    {
        path:'/login',
        component: Login
    },
    {
        path: '/dashboard',
        
        component: Dashboard
    }
]

const router = createRouter({
    routes,
    history: createWebHashHistory()
})

export default router