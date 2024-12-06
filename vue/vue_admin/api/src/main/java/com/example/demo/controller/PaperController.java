package com.example.demo.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.entity.Paper;
import com.example.demo.entity.PaperSub;
import com.example.demo.entity.Subject;
import com.example.demo.mapper.PaperMapper;
import com.example.demo.mapper.PaperSubMapper;
import com.example.demo.mapper.SubjectMapper;
import com.example.demo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PaperController{
    @Autowired
    private PaperMapper paperMapper;

    @Autowired
    private PaperSubMapper paperSubMapper;

    @Autowired
    private SubjectMapper subjectMapper;

    // 查询试卷列表
    @RequestMapping("/findAllPaper")
    public Result list() {
        return new Result().success(paperMapper.selectList(null));
    }

    // 创建试卷,接受参数试卷名字，创建者（老师）id，所属课程ID
    @RequestMapping("/addPaper")
    public Result add(String pname, Integer teaId, Integer cid) {
        Paper paper = new Paper();
        paper.setPaperName(pname);
        paper.setCreateId(teaId);
        paper.setCno(cid);
        return new Result().success(paperMapper.insert(paper));
    }

    // 向试卷中添加题目条款,接受参数试卷id，List<题目id>
    @RequestMapping("/addSubjectToPaper")
    public Result addSubjectToPaper(Integer pid,@RequestParam List<Integer> sids) {
        System.out.println("pid: " + pid);
        System.out.println("sids: " + sids);
        // 检查试卷是否存在
        LambdaQueryWrapper<Paper> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Paper::getPid, pid);
        Paper paper = paperMapper.selectOne(wrapper);
        // 获取试卷的所属课程
        Integer cno = paper.getCno();

        if (paper == null) {
            return new Result().error("试卷不存在");
        }
        // 检查试卷题目是否存在
        for (Integer i : sids) {
            LambdaQueryWrapper<Subject> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(Subject::getSid, i);
            Subject subject = subjectMapper.selectOne(wrapper1);
            if(subject == null) {
                return new Result().error("题目不存在");
            }
        }
        // 添加题目，向试卷中添加题目
        for (Integer i : sids) {
            PaperSub paperSub = new PaperSub();
            paperSub.setPid(pid);
            paperSub.setSid(i);
            paperSub.setCno(cno);
            paperSubMapper.insert(paperSub);
        }
        return new Result().success("向试卷中添加题目成功");
    }

    // 删除试卷,接受参数试卷id
    @RequestMapping("/deletePaper")
    public Result delete(Integer pid) {
        // 先删除试卷中的题目
        LambdaQueryWrapper<PaperSub> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(PaperSub::getPid, pid);
        paperSubMapper.delete(wrapper1);
        // 删除试卷
        LambdaQueryWrapper<Paper> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Paper::getPid, pid);
        paperMapper.delete(wrapper);

        return new Result().success("试卷删除成功！");
    }

    // 根据试卷id查询题目列表
    @RequestMapping("/findSubjectByPaperId")
    public Result findSubjectByPaperId(Integer pid) {
        LambdaQueryWrapper<PaperSub> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaperSub::getPid, pid);
        List<PaperSub> list = paperSubMapper.selectList(wrapper);
        // 根据题目id查询题目
        // 提取出题目id
        List<Subject> subjects = new ArrayList<>();
        for (PaperSub paperSub : list) {
            Integer sid = paperSub.getSid();
            LambdaQueryWrapper<Subject> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(Subject::getSid, sid);
            Subject subject = subjectMapper.selectOne(wrapper1);
            subjects.add(subject);
        }
        return new Result().success(subjects);
    }
}
