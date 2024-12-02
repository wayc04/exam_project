package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class PaperSub {
    @TableId
    private Integer psid; // 主键
    private Integer pid; // 试卷编号
    private Integer sid; // 题目编号
    private Integer cno; // 所属课程编号
}
