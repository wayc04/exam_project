package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.entity.Students;
import com.example.demo.entity.TeaUser;
import com.example.demo.mapper.StudentsMapper;
import com.example.demo.mapper.TeaUserMapper;
import com.example.demo.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private TeaUserMapper teaUserMapper;

    @Autowired
    private StudentsMapper studentsMapper;

    @RequestMapping("/tealogin")
    private Result teacherLogin(String username, String userpwd) {
        LambdaQueryWrapper<TeaUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TeaUser::getUsername, username).eq(TeaUser::getUserpwd, userpwd);
        System.out.println("username: " + username + " userpwd: " + userpwd);
        TeaUser tea =  teaUserMapper.selectOne(wrapper);

        Result<TeaUser> result = new Result<>();

        if(tea == null) {
            System.out.println("登陆失败");
            return result.error("密码错误");
        } else {
            System.out.println("登陆成功");
            result.setMsg("教师登陆成功");
            return result.success(tea);
        }
    }

    @RequestMapping("/stulogin")
    private Result studentLogin(String username, String userpwd) {
        LambdaQueryWrapper<Students> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Students::getUsername, username).eq(Students::getUserpwd, userpwd);
        System.out.println("username: " + username + " userpwd: " + userpwd);
        Students stu =  studentsMapper.selectOne(wrapper);

        Result<Students> result = new Result<>();

        if(stu == null) {
            System.out.println("登陆失败");
            return result.error("密码错误");
        } else {
            System.out.println("登陆成功");
            result.setMsg("学生登陆成功");
            return result.success(stu);
        }
    }
}
