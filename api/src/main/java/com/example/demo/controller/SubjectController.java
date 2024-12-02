package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.example.demo.entity.Subject;
import com.example.demo.mapper.SubjectMapper;
import com.example.demo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubjectController {
    @Autowired
    private SubjectMapper subjectMapper;

    @RequestMapping("/findAllSubject")
    public Result list() {
        return new Result().success(subjectMapper.selectList(null));
    }

    // 添加试题, 需要参数：课程id，试题内容，选项A，选项B，选项C，选项D，答案，题目类型，题目难度
    @RequestMapping("/addSubject")
    public Result add(Integer cno, String scontent, String sa, String sb, String sc, String sd, String skey, String type, Integer difficulty) {
        // 判断题目类型，选择题，判断题，简答题
        // 如果是选择题，那么四个选项不能为空
        if("选择题".equals(type)) {
            if(sa == null || sb == null || sc == null || sd == null) {
                return new Result().error("选择题，选项不能为空");
            }
            else{
                Subject subject = new Subject();
                subject.setCno(cno);
                subject.setScontent(scontent);
                subject.setSa(sa);
                subject.setSb(sb);
                subject.setSc(sc);
                subject.setSd(sd);
                subject.setSkey(skey);
                subject.setType(type);
                subject.setDifficulty(difficulty);
                subjectMapper.insert(subject);
                return new Result().success(subject);
            }
        }
        // 如果是判断题，那么选项A和选项B不能为空，a自动填入正确，b自动填入错误
        else if("判断题".equals(type)) {
                sa = "正确";
                sb = "错误";
                Subject subject = new Subject();
                subject.setCno(cno);
                subject.setScontent(scontent);
                subject.setSa(sa);
                subject.setSb(sb);
                subject.setSkey(skey);
                subject.setType(type);
                subject.setDifficulty(difficulty);
                subjectMapper.insert(subject);
                return new Result().success(subject);

        }
        // 如果是简答题，所有选项都为空
        else if("简答题".equals(type)){
            Subject subject = new Subject();
            subject.setCno(cno);
            subject.setScontent(scontent);
            subject.setSkey(skey);
            subject.setType(type);
            subject.setDifficulty(difficulty);
            subjectMapper.insert(subject);
            return new Result().success(subject);
        }
        return null;
    }

    // 根据各种信息查询试题
    @RequestMapping("/findSubjectByInfo")
    public Result findSubjectByInfo(Integer cno, String scontent,String type, Integer difficulty) {
        LambdaQueryWrapper<Subject> wrapper = new LambdaQueryWrapper<>();
        if(cno != null) {
            wrapper.eq(Subject::getCno, cno);
        }
        if(scontent != null) {
            wrapper.eq(Subject::getScontent, scontent);
        }
        if(type != null) {
            wrapper.eq(Subject::getType, type);
        }
        if(difficulty != null) {
            wrapper.eq(Subject::getDifficulty, difficulty);
        }
        // 如果没有条件，查询所有试题
//        if(cno == null && scontent == null && type == null && difficulty == null) {
//            return new Result().success(subjectMapper.selectList(null));
//        }
        return new Result().success(subjectMapper.selectList(wrapper));
    }

    // 根据试题id删除试题
    @RequestMapping("/deleteSubject")
    public Result deleteSubject(Integer sid) {
        LambdaQueryWrapper<Subject> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Subject::getSid, sid);
        subjectMapper.delete(wrapper);
        return new Result().success("删除成功");
    }
}
