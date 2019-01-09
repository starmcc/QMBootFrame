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
			QmTable table = clamm.getAnnotation(QmTable.class);
			if (table == null || StringUtils.isEmpty(table.name())) {
				tableName = clamm.getSimpleName();
			} else {
				tableName = table.name();
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
		QmId idKey = id.getAnnotation(QmId.class);
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
		QmId idKey = id.getAnnotation(QmId.class);
		if (idKey == null) {
			return;
		}
		Object obj;
		try {
			obj = id.get(bean);
		} catch (Exception e) {
			return;
		}
		if (primaryKey == null) {
			primaryKey = new HashMap<String, Object>();
		}
		// 判断是否设置别名
		if (StringUtils.isEmpty(idKey.name())) {
			primaryKey.put("key", id.getName());
		}else {
			primaryKey.put("key", idKey.name());
		}
		if (obj != null) {
			primaryKey.put("value", obj);
			primaryKeyautoIncrement = false;
		}else {
			// 如果没有值，则直接默认为是增加或删除调用，所以直接判断uuid规则还是自增规则
			// 判断是否为uuid策略
			if (idKey.uuid()) {
				primaryKey.put("value", UUID.randomUUID().toString().replace("-", ""));
				primaryKeyautoIncrement = false;
				return;
			}else{
				// 自增id策略
				primaryKeyautoIncrement = true;
			}
		}
	}

	/**
	 * 判断是否序列化该字段
	 * @param field
	 * @return
	 */
	private boolean isField(Field field) {
		QmParams qmParams = field.getAnnotation(QmParams.class);
		if (qmParams != null && qmParams.except()) {
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
			QmParams qmParams = field.getAnnotation(QmParams.class);
			Map<String, Object> fieldMap = new HashMap<String, Object>();
			if (StringUtils.isEmpty(qmParams.name())) {
				fieldMap.put("key", field.getName());
			}else{
				fieldMap.put("key", qmParams.name());
			}
			fieldMap.put("value", obj);
			params.add(fieldMap);
		}
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
