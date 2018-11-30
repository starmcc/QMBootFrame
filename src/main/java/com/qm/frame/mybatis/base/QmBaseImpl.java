package com.qm.frame.mybatis.base;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qm.frame.basic.util.ConvertUtil;
import com.qm.frame.mybatis.dto.QmBaseDto;

/**
 * Mybatis封装类
 * @author 浅梦
 */
@Component
public final class QmBaseImpl implements QmBase {

	// 获取Mybatis SqlSession
	@Autowired
	private SqlSessionFactory sqlSessionFactory;

	/**
	 * 命名空间
	 */
	private static final String QM_NAMESPACE = "QmBaseMapper.";

	/**
	 * 注入sqlSessionFactory
	 * @param sqlSessionFactory
	 */
	protected void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	/**
	 * 解析命名空间
	 * @param sqlName
	 * @return
	 */
	private String getSqlName(String sqlName) {
		String methodName = sqlName.substring(sqlName.indexOf("Mapper") + 6);
		String nameSpace = sqlName.substring(0, sqlName.indexOf("Mapper") + 6);
		return nameSpace + "." + methodName;
	}

	@Override
	public <M> List<M> selectList(String sqlName, Object params) {
		SqlSession session = sqlSessionFactory.openSession();
		List<M> list = null;
		try {
			sqlName = getSqlName(sqlName);
			list = session.selectList(sqlName, params);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return list;
	}

	@Override
	public <M> M selectOne(String sqlName, Object params) {
		SqlSession session = sqlSessionFactory.openSession();
		M obj = null;
		try {
			sqlName = getSqlName(sqlName);

			obj = session.selectOne(sqlName, params);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return obj;
	}

	@Override
	public int insert(String sqlName, Object params) {
		SqlSession session = sqlSessionFactory.openSession();
		int result = 0;
		try {
			sqlName = getSqlName(sqlName);
			result = session.insert(sqlName, params);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return result;
	}

	@Override
	public int update(String sqlName, Object params) {
		SqlSession session = sqlSessionFactory.openSession();
		int result = 0;
		try {
			sqlName = getSqlName(sqlName);
			result = session.update(sqlName, params);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return result;
	}

	@Override
	public int delete(String sqlName, Object params) {
		SqlSession session = sqlSessionFactory.openSession();
		int result = 0;
		try {
			sqlName = getSqlName(sqlName);
			result = session.delete(sqlName, params);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return result;
	}

	@Override
	public <Q> List<Q> autoSelectList(Q entity, Class<Q> clamm) {
		SqlSession session = sqlSessionFactory.openSession();
		List<Q> list = null;
		try {
			List<Map<String, Object>> mapLis;
			mapLis = session.selectList(QM_NAMESPACE + "selectAuto", new QmBaseDto(entity).getParamsMap());
			session.commit();
			list = ConvertUtil.mapsToObjects(mapLis, clamm);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return list;
	}

	@Override
	public <Q> int autoInsert(Q entity) {
		SqlSession session = sqlSessionFactory.openSession();
		int result = 0;
		try {
			result = session.insert(QM_NAMESPACE + "insertAuto", new QmBaseDto(entity).getParamsMap());
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return result;
	}

	@Override
	public <Q> int autoUpdate(Q entity) {
		SqlSession session = sqlSessionFactory.openSession();
		int result = 0;
		try {
			result = session.update(QM_NAMESPACE + "updateAuto", new QmBaseDto(entity).getParamsMap());
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return result;
	}

	@Override
	public <Q> int autoDelete(Q entity) {
		SqlSession session = sqlSessionFactory.openSession();
		int result = 0;
		try {
			result = session.delete(QM_NAMESPACE + "deleteAuto", new QmBaseDto(entity).getParamsMap());
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return result;
	}
}
