package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Pclass;
import com.example.demo.entity.Students;

import java.util.List;

public interface PclassMapper extends BaseMapper<Pclass> {
    List<Students> getStudentsByClassId(Integer classid);
}
