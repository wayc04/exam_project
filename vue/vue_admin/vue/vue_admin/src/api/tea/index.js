import { epPropKey } from 'element-plus/es/utils/index.mjs'
import request from '../../utils/request'


// 教师登录
export const tealogin = (param) => {
    return request.post('tealogin', param)
}

// 获取班级信息
export const getClassList = (data) =>{
    return request.post('findAllClass', data)
}

export const getExam = (data) =>{
    return request.post('listExams', data)
}

//新增班级
export const addClass = (data) =>{
    return request.post('addClass', data)
}

//根据班级id获取学生列表
export const getStudentsByClass = (data) =>{
    return request.post('getStudentsByClass', data)
}