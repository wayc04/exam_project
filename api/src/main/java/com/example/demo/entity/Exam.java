package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class Exam {
    @TableId
    private Integer eid; // 主键
    private Integer pid; // 班级id
    private Integer cid; // 所属班级id
    private String pname; // 试卷名称
    private Integer cno; // 所属课程编号
    private Integer useid ; // 创建者id
    private Date examdate; // 考试时间
    private Date endtime; // 考试结束时间
}
