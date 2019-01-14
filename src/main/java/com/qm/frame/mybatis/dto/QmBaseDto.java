package com.qm.frame.mybatis.dto;

import com.qm.frame.mybatis.note.QmId;
import com.qm.frame.mybatis.note.QmParams;
import com.qm.frame.mybatis.note.QmTable;
import org.apache.commons.lang3.StringUtils;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2018年11月24日 上午2:31:35
 * @Description 数据持久层封装DTO
 */
public final class QmBaseDto {
	private final Object bean;

	private String tableName;

	private List<Map<String, Object>> params = null;

	private Map<String, Object> primaryKey = null;


	/**
	 * 构造一个Dto
	 * @param bean
	 */
	public <T> QmBaseDto(T bean,boolean isPrimaryKey) throws Exception {
		this.bean = bean;
		//this.isPrimaryKey = isPrimaryKey;
		final Class<?> clamm = bean.getClass();
		// 获取该实体的表名
		QmTable table = clamm.getAnnotation(QmTable.class);
		if (table == null || StringUtils.isEmpty(table.name())) {
			tableName = clamm.getSimpleName();
		} else {
			tableName = table.name();
		}
		// 获取该实体的字段进行操作
		final Field[] fields = clamm.getDeclaredFields();
		// 如果该字段为空则返回
		if (fields == null) {
			throw new QmBaseDtoException("检测不到该实体的字段集!");
		}
		// 遍历字段进行参数封装
		for (Field field : fields) {
			// 开放字段权限public
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}
			// 判断是否需要主键策略
			if (isPrimaryKey) {
				// 序列化该主键
				setPrimaryKey(field);
			}
			// 序列化该字段
			setFiledToList(field);
		}
		return;
	}

	/**
	 * 设置主键
	 * @param id
	 * @return
	 */
	private void setPrimaryKey(Field filed) throws QmBaseDtoException {
		QmId idKey = filed.getAnnotation(QmId.class);
		if (idKey == null) {
			return;
		}
		Object obj = null;
		try {
			obj = filed.get(bean);
		} catch (IllegalAccessException e) {
			throw new QmBaseDtoException("读取实体类主键字段发生了异常！");
		}
		if (obj != null) {
			// 不等于null
			// 判断是否设置别名
			primaryKey = new HashMap<>();
			if (StringUtils.isEmpty(idKey.name())) {
				primaryKey.put("key", filed.getName());
			}else {
				primaryKey.put("key", idKey.name());
			}
			primaryKey.put("value", obj);
		} else {
			// 判断是否为uuid策略
			if (idKey.uuid() == false) return;
			// 直接判断uuid规则还是自增规则
			// 判断是否设置别名
			primaryKey = new HashMap<>();
			if (StringUtils.isEmpty(idKey.name())) {
				primaryKey.put("key", filed.getName());
			}else {
				primaryKey.put("key", idKey.name());
			}
			primaryKey.put("value", UUID.randomUUID().toString().replace("-", ""));
		}
		return;
	}


	/**
	 * 设置字段
	 * @param field
	 * @return
	 */
	private void setFiledToList(Field field) throws QmBaseDtoException{
		QmParams qmParams = field.getAnnotation(QmParams.class);
		// 判断是否有该注解，如果存在并且except等于true则不加入该字段。
		if (qmParams != null && qmParams.except()) {
			return;
		}
		Object obj = null;
		try {
			obj = field.get(bean);
			// 如果值是null则不需要这个值了。
			if (obj == null) {
			    return;
			}
		} catch (IllegalAccessException e) {
			throw new QmBaseDtoException("获取字段失败！");
		}
		// 开始获取字段并加入字段列表
		Map<String, Object> fieldMap = new HashMap<String, Object>();
		if (qmParams == null || StringUtils.isEmpty(qmParams.name())) {
			fieldMap.put("key", field.getName());
		}else{
			fieldMap.put("key", qmParams.name());
		}
		fieldMap.put("value", obj);
		if (params == null) {
			params = new ArrayList<>();
		}
		params.add(fieldMap);
		return;
	}


	/**
	 * 获取实体类参数封装Map
	 * @return
	 */
	public Object getParamsMap() {
		Map<String, Object> resMap = new LinkedHashMap<>();
		resMap.put("primaryKey", primaryKey);
		resMap.put("params", params);
		resMap.put("tableName", tableName);
		System.out.println(resMap);
		return resMap;
	}

}
