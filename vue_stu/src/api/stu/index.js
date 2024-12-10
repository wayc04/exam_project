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