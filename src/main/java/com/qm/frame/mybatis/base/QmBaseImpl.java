package com.qm.frame.mybatis.base;

import com.qm.frame.basic.util.ConvertUtil;
import com.qm.frame.mybatis.dto.QmBaseDto;
import com.qm.frame.mybatis.dto.QmBaseDtoException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
	private String getSqlName(String sqlName){
		String methodName = null;
		String nameSpace = null;
		try {
			methodName = sqlName.substring(sqlName.indexOf("Mapper") + 6);
			nameSpace = sqlName.substring(0, sqlName.indexOf("Mapper") + 6);
		} catch (Exception e) {
			throw new QmBaseException("Mapper命名空间错误!'" + sqlName + "' Error！",e);
		}
		return nameSpace + "." + methodName;
	}

	@Override
	public <M> List<M> selectList(String sqlName, Object params){
		SqlSession session = sqlSessionFactory.openSession();
		List<M> list = null;
		try {
			list = session.selectList(getSqlName(sqlName), params);
			session.commit();
		} catch (Exception e) {
			throw new QmBaseException("SQL Error！",e);
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
			obj = session.selectOne(getSqlName(sqlName), params);
			session.commit();
		} catch (Exception e) {
			throw new QmBaseException("SQL Error！",e);
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
			result = session.insert(getSqlName(sqlName), params);
			session.commit();
		} catch (Exception e) {
			throw new QmBaseException("SQL Error！",e);
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
			result = session.update(getSqlName(sqlName), params);
			session.commit();
		} catch (Exception e) {
			throw new QmBaseException("SQL Error！",e);
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
			result = session.delete( getSqlName(sqlName), params);
			session.commit();
		} catch (Exception e) {
			throw new QmBaseException("SQL Error！",e);
		} finally {
			session.close();
		}
		return result;
	}

	@Override
	public <Q> List<Q> autoSelectList(Q entity, Class<Q> clamm) {
		SqlSession session = sqlSessionFactory.openSession();
		List<Map<String, Object>> mapLis;
		try {
			mapLis = session.selectList(QM_NAMESPACE + "selectAuto",
							new QmBaseDto(entity,false).getParamsMap());
			session.commit();
		} catch (Exception e) {
			throw new QmBaseException("SQL Error！",e);
		} finally {
			session.close();
		}
		// 如果是空的直接返回null
		if (mapLis == null) return null;
		return ConvertUtil.mapsToObjects(mapLis,clamm);
	}

	@Override
	public <Q> Q autoSelectOne(Q entity, Class<Q> clamm) {
		SqlSession session = sqlSessionFactory.openSession();
		Map<String, Object> map;
		Q obj = null;
		try {
			obj = clamm.newInstance();
		} catch (Exception e) {
			throw new QmBaseException("Entity newInstance Error!",e);
		}
		try {
			map = session.selectOne(QM_NAMESPACE + "selectAuto",  new QmBaseDto(entity,false).getParamsMap());
			session.commit();
		} catch (Exception e) {
			throw new QmBaseException("SQL Error！",e);
		} finally {
			session.close();
		}
		// 如果是空的直接返回null
		if (map == null) return null;
		ConvertUtil.mapToBean(map, obj);
		return obj;
	}


	@Override
	public <Q> int autoInsert(Q entity) {
		SqlSession session = sqlSessionFactory.openSession();
		int result = 0;
		try {
			result = session.insert(QM_NAMESPACE + "insertAuto", new QmBaseDto(entity,true).getParamsMap());
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new QmBaseException("SQL Error！",e);
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
			result = session.update(QM_NAMESPACE + "updateAuto",  new QmBaseDto(entity,true).getParamsMap());
			session.commit();
		} catch (Exception e) {
			throw new QmBaseException("SQL Error！",e);
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
			result = session.delete(QM_NAMESPACE + "deleteAuto", new QmBaseDto(entity,true).getParamsMap());
			session.commit();
		} catch (Exception e) {
			throw new QmBaseException("SQL Error！",e);
		} finally {
			session.close();
		}
		return result;
	}

	@Override
	public <Q> int autoSelectCount(Q entity) {
		SqlSession session = sqlSessionFactory.openSession();
		int result = 0;
		try {
			result = session.selectOne(QM_NAMESPACE + "selectCount", new QmBaseDto(entity,false).getParamsMap());
			session.commit();
		} catch (Exception e) {
			throw new QmBaseException("SQL Error！",e);
		} finally {
			session.close();
		}
		return result;
	}
}
