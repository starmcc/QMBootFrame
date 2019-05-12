package com.qm.code.service;

import com.qm.code.entity.Student;

import java.util.List;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/5/12 19:13
 * @Description RestfulService接口
 */
public interface RestfulService {


    List<Student> getAutoList(Student student);
    List<Student> getList(Student student);
}
