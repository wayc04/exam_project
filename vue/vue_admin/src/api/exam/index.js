import { epPropKey } from 'element-plus/es/utils/index.mjs'
import request from '../../utils/request'

// 获取考试列表
export const getExamList = (param) => {
    return request.post('listExams', param)
}

// 根据课程id获取课程
export const findCourseById = (param) => {
    return request.post('findCourseById', param)
}

// 根据老师id获取老师姓名
export const findTeacherById = (param) => {
    return request.post('findTeacherById', param)
}

// 根据考试id删除考试，接收参数Integer eid
export const deleteExam = (param) => {
    return request.post('deleteExam', param)
}

// 根据考试id获取班级列表，接收参数Integer eid
export const getClassesByEid = (param) => {
    return request.post('getClassByExamId', param)
}

// 更新考试信息，接收参数Integer eid, Integer pid,@RequestParam("cids") List<Integer> cids,
// @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date examdate,
// @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endtime
export const updateExam = (param) => {
    return request.post('updateExam', param)
}

// 获取班级信息
export const getAllClasses = (param) => {
    return request.get('findAllClass', param)
}

// 获取所有试卷信息
export const getAllPaper = (param) => {
    return request.get('findAllPaper', param)
}

// 添加考试信息，接收参数为：Integer pid,Integer userid,@RequestParam("cids") List<Integer> cids,
// @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date examdate,
// @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endtime
export const addExam = (param) => {
    return request.post('addExam', param)
}

//获取成绩
export const getGradeByEidAndUserid = (param) => {
    return request.post('getGradeByEidAndUserid', param)
}
//通过userid获取学生信息
export const stubyid = (param) => {
    return request.post('stubyid', param)
}