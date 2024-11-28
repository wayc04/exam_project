package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.example.demo.entity.Students;
import com.example.demo.entity.TeaUser;
import com.example.demo.mapper.StudentsMapper;
import com.example.demo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StudentController {
    @Autowired
    private StudentsMapper studentsMapper;

    @RequestMapping("/register")
    // 生成一个学生账号
    public Result register(String username, String userpwd, String truename, Integer classid) {
        // 检查用户名是否重复
        LambdaQueryWrapper<Students> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Students::getUsername, username);
        Students stu1 = studentsMapper.selectOne(wrapper);
        if(stu1 != null) {
            return new Result().error("用户名已存在");
        }

        // 用户名不重复，生成学生账号
        Students stu = new Students();
        stu.setUsername(username);
        stu.setUserpwd(userpwd);
        stu.setTruename(truename);
        stu.setClassid(classid);
        studentsMapper.insert(stu);

        return new Result().success(stu);
    }

    // 修改密码
    @RequestMapping("/updatepwd")
    public Result updatePwd(String username, String oldPwd, String newPwd) {
        LambdaQueryWrapper<Students> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Students::getUsername, username).eq(Students::getUserpwd, oldPwd);
        Students stu = studentsMapper.selectOne(wrapper);
        if(stu == null) {
            return new Result().error("用户名或密码错误");
        }

        stu.setUserpwd(newPwd);

        studentsMapper.update(stu, wrapper);
//        studentsMapper.updateById(stu);

        return new Result().success(stu);
    }

    // 查询学生列表
    @RequestMapping("/stulist")
    public Result stuList() {
        List<Students> list = studentsMapper.selectList(null);
        return new Result().success(list);
    }

    // 根据条件信息查询学生
    @RequestMapping("/stubyinfo")
    public Result stuByInfo(String username, String truename, Integer classid) {
        LambdaQueryWrapper<Students> wrapper = new LambdaQueryWrapper<>();

        if(username != null) {
            wrapper.eq(Students::getUsername, username);
        }
        if(truename != null) {
            wrapper.eq(Students::getTruename, truename);
        }
        if(classid != null) {
            wrapper.eq(Students::getClassid, classid);
        }
        // 如果没有条件，查询所有学生
        List<Students> list;
        if (username == null && truename == null && classid == null) {
            System.out.println("没有条件，查询所有学生");
            list = studentsMapper.selectList(null);
        } else {
            list = studentsMapper.selectList(wrapper);
        }
        System.out.println("list: " + list);
        return new Result().success(list);
    }

    // 删除学生
    @RequestMapping("/deletestu")
    public Result deleteStu(Integer userid) {
        LambdaQueryWrapper<Students> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Students::getUserid, userid);
        studentsMapper.delete(wrapper);
        return new Result().success("删除成功");
    }

    // 根据id 修改学生信息
    @RequestMapping("/updatestu")
    public Result updateStu(Integer userid, String username, String userpwd, String truename, Integer classid) {
        LambdaQueryWrapper<Students> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Students::getUserid, userid);
        Students stu = studentsMapper.selectOne(wrapper);
        if(stu == null) {
            return new Result().error("用户不存在");
        }

        if(username != null) {
            stu.setUsername(username);
        }
        if(userpwd != null) {
            stu.setUserpwd(userpwd);
        }
        if(truename != null) {
            stu.setTruename(truename);
        }
        if(classid != null) {
            stu.setClassid(classid);
        }

        studentsMapper.update(stu, wrapper);
        return new Result().success(stu);
    }
}
