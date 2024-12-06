package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "students")
public class Students {
    @TableId
    private Integer userid ;
    private String username;
    private String userpwd;
    private String truename;
    private Integer classid;
}
