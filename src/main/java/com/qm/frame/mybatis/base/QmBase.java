package com.qm.frame.mybatis.base;

import java.util.List;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2018年11月24日 上午2:16:16
 * @Description Mybatis数据持久层封装工具
 */
public interface QmBase {
    /**
     * 查询列表
     *
     * @param sqlName 命名空间
     * @param params  参数
     * @return 根据返回指定的类型进行嵌套数据
     */
    <M> List<M> selectList(String sqlName, Object params);

    /**
     * 查询单条记录
     *
     * @param sqlName 命名空间
     * @param params  参数
     * @return 根据返回指定的类型进行嵌套数据
     */
    <M> M selectOne(String sqlName, Object params);


    /**
     * 插入记录
     *
     * @param sqlName 命名空间
     * @param params  参数
     * @return 影响行数
     */
    int insert(String sqlName, Object params);

    /**
     * 修改记录
     *
     * @param sqlName 命名空间
     * @param params  参数
     * @return 影响行数
     */
    int update(String sqlName, Object params);

    /**
     * 删除记录
     *
     * @param sqlName 命名空间
     * @param params  参数
     * @return 影响行数
     */
    int delete(String sqlName, Object params);


    /**
     * 通用查询列表
     *
     * @param entity 实体类(必须带有@QmTable)
     * @param clamm  实体类class对象
     * @return 根据参数指定的类型进行嵌套数据
     */
    <Q> List<Q> autoSelectList(Q entity, Class<Q> clamm);


    /**
     * 通用查询单条记录
     *
     * @param entity 实体类(必须带有@QmTable)
     * @param clamm  实体类class对象
     * @return 根据参数指定的类型进行嵌套数据
     */
    <Q> Q autoSelectOne(Q entity, Class<Q> clamm);


    /**
     * 通用插入记录
     *
     * @param entity 实体类(必须带有@Table和@QmId)
     * @return 影响行数
     */
    <Q> int autoInsert(Q entity);

    /**
     * 通用修改记录
     *
     * @param entity 实体类(必须带有@Table和@QmId)
     * @return 影响行数
     */
    <Q> int autoUpdate(Q entity);

    /**
     * 通用删除记录
     *
     * @param entity 实体类(必须带有@Table和@QmId)
     * @return 影响行数
     */
    <Q> int autoDelete(Q entity);

    /**
     * 通用查询记录数
     *
     * @param entity 实体类(必须带有@Table和@QmId)
     * @return 影响行数
     */
    <Q> int autoSelectCount(Q entity);
}
