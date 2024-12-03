import { epPropKey } from 'element-plus/es/utils/index.mjs'
import request from '../../utils/request'

export const getCourseList = (param) => {
    return request.get('findAllSubject', param)
}

// 获取选择题
export const getChoiceQuestion = (param) => {
    return request.get('findSelectSubject', param)
}
// 获取判断题
export const getJudgeQuestion = (param) => {
    return request.get('findJudgeSubject', param)
}
// 获取简答题
export const getShortAnswerQuestion = (param) => {
    return request.get('findShortAnswerSubject', param)
}

// 更新试题信息
export const updateQuestion = (param) => {
    return request.post('updateSubjectbySid', param)
}

// 删除题目
export const deleteQuestion = (param) => {
    return request.post('deleteSubject', param)
}