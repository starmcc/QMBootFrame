package com.qm.code.service.impl;

import com.qm.code.entity.Student;
import com.qm.code.service.RestfulService;
import com.qm.frame.mybatis.base.QmBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/5/12 19:14
 * @Description RestfulServices实现
 */
@Service
public class RestfulServiceImpl implements RestfulService {

    @Autowired
    private QmBase qmBase;
    private static final String PAGE_NAME = "Test-RestfulServiceImpl-0-Mapper";


    @Override
    public List<Student> getAutoList(Student student) {
        return qmBase.autoSelectList(student,Student.class);
    }

    @Override
    public List<Student> getList(Student student) {
        Map<String,Object> params = new HashMap<>();
        params.put("student",student);
        return qmBase.selectList(PAGE_NAME + "getList",student);
    }
}
