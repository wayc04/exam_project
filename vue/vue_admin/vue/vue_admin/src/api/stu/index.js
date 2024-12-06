import { epPropKey } from 'element-plus/es/utils/index.mjs'
import request from '../../utils/request'

// 注册用户
export const register = (data) => {
    return request.post('register', data)
}

// 用户登录
export const stulogin = (param) => {
    return request.post('stulogin', param)
}

// 获取学生列表
export const getStuList = (param) => {
    return request.post('stulist', param)
}

// 根据学生信息查询学生，接收参数为：String username, String truename, Integer classid
// 这三个参数都是可选的，可以传一个或者多个，若都不选则查询所有学生
export const findStu = (param) => {
    return request.post('stubyinfo', param)
}

// 获取班级信息
export const getClassOptions = (param) => {
    return request.get('findAllClass', param)
}

// 根据学生id修改学生信息，接收参数为：Integer userid, String username, String userpwd, String truename, Integer classid
export const updateStu = (param) => {
    return request.post('updatestu', param)
}