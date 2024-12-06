package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestMapping;

@Data
@AllArgsConstructor
@TableName(value = "teauser")
public class TeaUser {
    @TableField(value = "userid")
    private Integer userid;
    @TableField(value = "username")
    private String username;
    @TableField(value = "userpwd")
    private String userpwd;
    @TableField(value = "truename")
    private String truename;
    @TableField(value = "classid")
    private Integer classid;
}
