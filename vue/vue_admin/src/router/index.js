import {createRouter, createWebHashHistory} from 'vue-router'


import Layout from '../views/Main.vue'
import Login from '../views/login/index.vue'
import Dashboard from '../views/dashboard/index.vue'
import Class from '../views/class/class/index.vue'
import Group from '../views/class/group/index.vue'
import Staff from '../views/vppz/staff/index.vue'
import Order from '../views/vppz/order/index.vue'
import Code from '../views/vppz/code/index.vue'
import component from 'element-plus/es/components/tree-select/src/tree-select-option.mjs'
import ExamList from  '../views/exam/exam_list/index.vue'


const routes = [
    {
        path:'/',
        component: Layout,
        name:'main',
        children: [
            {
                path: 'dashboard',
                meta: { id: '1', name: '控制台', icon: 'Platform', path: '/dashboard', describe: '用于展示当前系统中的统计数据、统计报表及重要实时数据' },
                component: Dashboard
            },
            {
                path: 'class',
                meta: { id: '2', name: '班级管理', icon: 'Grid' },
                children: [
                    {
                        path: '',
                        alias: ['class'],
                        meta: { id: '1', name: '班级信息', icon: 'Avatar', path: '/class/class', describe: '管理员可以进行编辑，权限修改后需要登出才会生效' },
                        component: Class
                    },
                    {
                        path: 'group',
                        meta: { id: '2', name: '用户列表', icon: 'Menu', path: '/class/group', describe: '菜单规则通常对应一个控制器的方法,同时菜单栏数据也从规则中获取' },
                        component: Group
                    }
                ]
            },
            {
                path: 'vppz',
                meta: { id: '3', name: '试题管理', icon: 'BellFilled' },
                children: [
                    {
                        path: '',
                        alias: ['staff'],
                        meta: { id: '1', name: '基础题', icon: 'Checked', path: '/vppz/staff', describe: '陪护师可以进行创建和修改，设置对应生效状态控制C端选择' },
                        component: Staff
                    },
                    {
                        path: 'order',
                        meta: { id: '2', name: '试卷管理', icon: 'List', path: '/vppz/order', describe: 'C端下单后可以查看所有订单状态，已支付的订单可以完成陪护状态修改' },
                        component: Order
                    },
                    {
                        path: 'code',
                        meta: { id: '3', name: '编程题', icon: 'List', path: '/vppz/code', describe: 'C端下单后可以查看所有订单状态，已支付的订单可以完成陪护状态修改' },
                        component: Code
                    }
                ]
            },
            {
                path: 'exam',
                meta: { id: '4', name: '考试管理', icon: 'BellFilled' },
                children: [
                    {
                        path: 'exam_list',
                        alias: ['list'], 
                        meta:  { id: '1', name: '考试列表', icon: 'BellFilled', path: '/exam/exam_list', describe: '考试列表' },
                        component: ExamList
                    }
                ]
            }
        ]
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