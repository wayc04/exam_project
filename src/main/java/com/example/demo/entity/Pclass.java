package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "pclass")
public class Pclass {
    private Integer classid;
    private String classname;
}
