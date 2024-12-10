
import request from '../../utils/request'

// 注册用户
export const getExamByStuId = (data) => {
    return request.post('getExamByStuId', data)
}

export const findSubjectByPaperId =(param) =>{
    return request.post('findSubjectByPaperId',param)
}

export const findSubjectByPaperIdAndType = (param)=>{
    return request.post('findSubjectByPaerIdAndType',param)
}

export const judgePaperByList = (param)=>{
    return request.post('judgePaperByList',param,{
        headers: {
            'Content-Type': 'application/json', // 确保使用 JSON 格式
            'Accept': 'application/json', // 接受 JSON 响应
        }
    })
}


export const getGradeByEidAndUserid =(param)=>{
    return request.post('getGradeByEidAndUserid',param)
}

