package com.qm.frame.mybatis.dto;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

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

	private boolean primaryKeyautoIncrement = true;

	private List<Map<String, Object>> params = null;

	private Map<String, Object> primaryKey = null;

	/**
	 * 构造一个Dto
	 * @param bean
	 */
	public <T> QmBaseDto(T bean) {
		this.bean = bean;
		initDto();
	}

	/**
	 * 初始化该类的Dto
	 */
	private void initDto() {
		try {
			final Class<?> clamm = bean.getClass();
			// 获取该实体的表名
			Table table = clamm.getAnnotation(Table.class);
			if (table != null && !table.name().equals("")) {
				tableName = table.name();
			} else {
				tableName = clamm.getSimpleName();
			}
			// 获取该实体的字段进行操作
			final Field[] fields = clamm.getDeclaredFields();
			if (fields != null) {
				for (Field field : fields) {
					// 开放字段权限public
					if (!field.isAccessible()) {
						field.setAccessible(true);
					}
					// 判断是否主键
					if (isPrimaryKey(field)) {
						// 判断是否已设置主键
						if (primaryKey == null) {
							// 设置主键值
							setPrimaryKey(field);
						}
					} else {
						// 判断是否序列化该字段
						if (isField(field)) {
							// 设置字段
							setFiledToList(field);
						}
					}
				}
			}
			return;
		} catch (Exception e) {
		}
	}

	/**
	 * 是否主键
	 * @param id
	 * @return
	 */
	private boolean isPrimaryKey(Field id) {
		Id idKey = id.getAnnotation(Id.class);
		if (idKey == null) {
			return false;
		}
		return true;
	}

	/**
	 * 设置主键
	 * @param id
	 */
	private void setPrimaryKey(Field id) {
		Id idKey = id.getAnnotation(Id.class);
		if (idKey == null) {
			return;
		}
		Object obj;
		try {
			obj = id.get(bean);
		} catch (Exception e) {
			return;
		}
		if (obj != null) {
			if (primaryKey == null) {
				primaryKey = new HashMap<String, Object>();
			}
			primaryKey.put("key", getFieldKeyName(id));
			primaryKey.put("value", obj);
			primaryKeyautoIncrement = false;
		}
	}

	/**
	 * 判断是否序列化该字段
	 * @param field
	 * @return
	 */
	private boolean isField(Field field) {
		Transient trans = field.getAnnotation(Transient.class);
		if (trans != null) {
			return false;
		}
		return true;
	}

	/**
	 * 设置字段
	 * @param field
	 */
	private void setFiledToList(Field field) {
		Object obj;
		try {
			obj = field.get(bean);
		} catch (Exception e) {
			return;
		}
		if (obj != null) {
			if (params == null) {
				params = new ArrayList<Map<String, Object>>();
			}
			Map<String, Object> fieldMap = new HashMap<String, Object>();
			fieldMap.put("key", getFieldKeyName(field));
			fieldMap.put("value", obj);
			params.add(fieldMap);
		}
	}

	/**
	 * 获取该字段的key名
	 * @param field
	 * @return
	 */
	private String getFieldKeyName(Field field) {
		Column column = field.getAnnotation(Column.class);
		if (column == null || column.name().equals("")) {
			return field.getName();
		}
		return column.name();
	}

	/**
	 * 获取实体类参数封装Map
	 * @return
	 */
	public Object getParamsMap() {
		Map<String, Object> resMap = new HashMap<>();
		System.out.println("====================");
		resMap.put("primaryKey", primaryKey);
		resMap.put("params", params);
		resMap.put("tableName", tableName);
		resMap.put("primaryKeyautoIncrement", primaryKeyautoIncrement);
		System.out.println(resMap);
		return resMap;
	}

}
