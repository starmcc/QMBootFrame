package com.qm.frame.mybatis.note;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/1/23 10:18
 * @Description 数据库字段风格
 */
public enum QmStyle {

    HUMP("驼峰模式"),UNDERLINE("下划线模式");

    private String name;

    public String getName() {
        return name;
    }

    private QmStyle(String name){
        this.name = name;
    }

}
