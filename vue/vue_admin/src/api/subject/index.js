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


//获取所有的试卷信息
export const findAllPaperInfo = (param) => {
    return request.get('findAllPaper', param)
}

//根据id获取试卷信息
export const findSubjectByPaperId = (param) => {
    return request.post('findSubjectByPaperId', param)
}

//获取所有的课程

// 根据课程id获取课程
export const findCourseById = (param) => {
    return request.post('findCourseById', param)
}

// 查询所有课程

export const findAllCourse = (param) => {
    return request.get('findAllCourse', param)
}


//新增试卷
export const addPaper = (param) => {
    return request.post('addPaper', param)
}

//删除试卷
export const deletePaper = (param) => {
    return request.post('deletePaper', param)
}

//根据课程查找题目
export const findSubjectByInfo = (param) => {
    return request.post('findSubjectByInfo', param)
}

//在试卷中加入题目，注意课程号cno要相同
export const addSubjectToPaper = (param) => {
    return request.post('addSubjectToPaper', param)
}


export const addSubject = (param) => {
    return request.post('addSubject', param)
}

// 根据题目类型获取题目
export const findSubjectByType = (param) => {
    return request.post('getSubjectByType', param)
}

// 根据题目id获取测试点信息，获取参数sid：题目id
export const getTestPointBySid = (param) => {
    return request.post('getTestDataBySid', param)
}

// 添加编程题，接受参数为：String scontent, Integer difficulty, Integer timeout, Integer memorylimit
export const addCodeQuestion = (param) => {
    return request.post('addProgrammingSubject', param)
}

// 添加测试点，接受参数为：Integer sid, String t1, String answer1
export const addTestPoint = (param) => {
    return request.post('addTestPoint', param)
}

// 删除测试点，接受参数为：Integer tid
export const deleteTestPoint = (param) => {
    return request.post('deleteTestPoint', param)

}

// 修改测试点，接受参数为：Integer tid, String t1, String answer1
export const updateTestPoint = (param) => {
    return request.post('updateTestPoint', param)
}

// 删除编程题，接受参数为：Integer sid
export const deleteCodeQuestion = (param) => {
    return request.post('deleteProgrammingSubject', param)
}

// 修改编程题，接受参数为：Integer sid, String scontent, Integer difficulty, Integer timeout, Integer memorylimit
export const updateCodeQuestion = (param) => {
    return request.post('updateProgrammingSubject', param)
}