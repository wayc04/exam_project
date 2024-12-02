package com.example.demo.controller;

import com.example.demo.mapper.CourseMapper;
import com.example.demo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CourseController {
    @Autowired
    private CourseMapper courseMapper;

    // 查询课程列表
    @RequestMapping("/findAllCourse")
    public Result list() {
        return new Result().success(courseMapper.selectList(null));
    }
}
