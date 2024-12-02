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