package com.example.demo.controller;

import com.example.demo.entity.Pclass;
import com.example.demo.mapper.PclassMapper;
import com.example.demo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassController {

    @Autowired
    private PclassMapper pclassMapper;

    // 查询班级列表
    @RequestMapping("/findAllClass")
    public Result list() {
        return new Result().success(pclassMapper.selectList(null));
    }

    // 添加班级
    @RequestMapping("/addClass")
    public Result add(String className) {
        Pclass pclass = new Pclass();
        pclass.setClassname(className);
        return new Result().success(pclassMapper.insert(pclass));
    }

    // 得到班级的所有学生
    @RequestMapping("/getStudentsByClass")
    public Result getStudentsByClass(Integer classid) {
        return new Result().success(pclassMapper.getStudentsByClassId(classid));
    }
}
