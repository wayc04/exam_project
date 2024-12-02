package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.entity.Exam;
import com.example.demo.entity.Paper;
import com.example.demo.mapper.ExamMapper;
import com.example.demo.mapper.PaperMapper;
import com.example.demo.mapper.StudentsMapper;
import com.example.demo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class ExamController {
    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private PaperMapper paperMapper;

    @Autowired
    private StudentsMapper studentsMapper;

    // 创建考试
    @RequestMapping("/addExam")
    public Result add(Integer pid, Integer cid,Integer userid,
                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date examdate,
                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endtime) {
        // 根据pid查询出试卷名和cno
        Paper paper = paperMapper.selectById(pid);
        if(paper == null) {
            return new Result().error("试卷不存在");
        }
        String pname = paper.getPaperName();
        Integer cno = paper.getCno();

        Exam exam = new Exam();
        exam.setPid(pid);
        exam.setCid(cid);
        exam.setPname(pname);
        exam.setCno(cno);
        exam.setUseid(userid);
        exam.setExamdate(examdate);
        exam.setEndtime(endtime);
        return new Result().success(examMapper.insert(exam));
    }

    // 修改考试
    @RequestMapping("/updateExam")
    public Result update(Integer eid, Integer pid, Integer cid,
                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date examdate,
                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endtime) {
        // 查询出eid对应的考试
        Exam exam = examMapper.selectById(eid);
        if(exam == null) {
            return new Result().error("考试不存在");
        }
        // 根据pid查询出试卷名和cno
        Paper paper = paperMapper.selectById(pid);
        if(paper == null) {
            return new Result().error("试卷不存在");
        }
        String pname = paper.getPaperName();
        Integer cno = paper.getCno();

        exam.setPid(pid);
        exam.setPname(pname);
        exam.setCid(cid);
        exam.setCno(cno);
        exam.setExamdate(examdate);
        exam.setEndtime(endtime);
        return new Result().success(examMapper.updateById(exam));
    }

    // 删除考试信息
    @RequestMapping("/deleteExam")
    public Result delete(Integer eid) {
        return new Result().success(examMapper.deleteById(eid));
    }

    // 列出所有考试
    @RequestMapping("/listExams")
    public Result list() {
        return new Result().success(examMapper.selectList(null));
    }

    // 根据学生班级列出所有考试信息
    @RequestMapping("/listExamsByClass")
    public Result listByClass(Integer cid) {
        LambdaQueryWrapper<Exam> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Exam::getCid, cid);
        return new Result().success(examMapper.selectList(wrapper));
    }
}
