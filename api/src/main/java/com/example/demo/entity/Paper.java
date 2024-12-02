package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Paper {
    @TableId
    private Integer pid;
    private Integer createId; // 课程编号
    private String paperName; // 试卷名称
    private Integer cno; // 课程编号
}
