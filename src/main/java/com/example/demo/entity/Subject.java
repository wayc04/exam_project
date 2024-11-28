package com.example.demo.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class Subject {
    private Integer sid;
    private Integer cno; // 课程编号
    private String scontent; // 题目内容
    @TableField(fill = FieldFill.INSERT_UPDATE,value = "sa")
    private String sa; // 选项A
    @TableField(fill = FieldFill.INSERT_UPDATE,value = "sb")
    private String sb; // 选项B
    @TableField(fill = FieldFill.INSERT_UPDATE,value = "sc")
    private String sc; // 选项C
    @TableField(fill = FieldFill.INSERT_UPDATE,value = "sd")
    private String sd; // 选项D
    private String skey; // 答案
    private String type; // 题目类型
    // 题目难度
    private Integer difficulty;
}
