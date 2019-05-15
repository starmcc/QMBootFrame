package com.qm.frame.mybatis.base;

import com.qm.frame.basic.util.ConvertUtil;
import com.qm.frame.mybatis.dto.QmBaseDto;
import com.qm.frame.mybatis.note.QmStyle;
import com.qm.frame.mybatis.note.QmTable;
import com.qm.frame.mybatis.util.QmBaseStyleUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author 浅梦
 * @Title QmBase实现类
 * @Date 注释时间：2019/1/23 12:43
 */
@Component
public final class QmBaseImpl implements QmBase {

    /**
     * 命名空间
     */
    private static final String QM_NAMESPACE = "QmBaseMapper.";
    /**
     * 获取Mybatis SqlSession
     */
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 注入sqlSessionFactory
     *
     * @param sqlSessionFactory
     */
    protected void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    /**
     * 解析命名空间
     *
     * @param sqlName
     * @return
     */
    private String getSqlName(String sqlName) {
        try {
            String methodName = sqlName.substring(sqlName.indexOf("Mapper") + 6);
            String nameSpace = sqlName.substring(0, sqlName.indexOf("Mapper") + 6);
            return nameSpace + "." + methodName;
        } catch (Exception e) {
            throw new QmBaseException("Mapper命名空间错误!'" + sqlName + "' Error！", e);
        }
    }

    @Override
    public <M> List<M> selectList(String sqlName, Object params) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            List<M> list = session.selectList(getSqlName(sqlName), params);
            session.commit();
            return list;
        } catch (Exception e) {
            throw new QmBaseException(getErrorMsg("selectList"), e);
        } finally {
            session.close();
        }
    }

    @Override
    public <M> M selectOne(String sqlName, Object params) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            M obj = session.selectOne(getSqlName(sqlName), params);
            session.commit();
            return obj;
        } catch (Exception e) {
            throw new QmBaseException(getErrorMsg("selectOne"), e);
        } finally {
            session.close();
        }
    }

    @Override
    public int insert(String sqlName, Object params) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            int result = session.insert(getSqlName(sqlName), params);
            session.commit();
            return result;
        } catch (Exception e) {
            throw new QmBaseException(getErrorMsg("insert"), e);
        } finally {
            session.close();
        }
    }

    @Override
    public int update(String sqlName, Object params) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            int result = session.update(getSqlName(sqlName), params);
            session.commit();
            return result;
        } catch (Exception e) {
            throw new QmBaseException(getErrorMsg("update"), e);
        } finally {
            session.close();
        }
    }

    @Override
    public int delete(String sqlName, Object params) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            int result = session.delete(getSqlName(sqlName), params);
            session.commit();
            return result;
        } catch (Exception e) {
            throw new QmBaseException(getErrorMsg("delete"), e);
        } finally {
            session.close();
        }
    }

    @Override
    public <Q> List<Q> autoSelectList(Q entity, Class<Q> clamm) {
        SqlSession session = sqlSessionFactory.openSession();
        List<Map<String, Object>> mapLis;
        try {
            if (entity == null) {entity = clamm.newInstance();}
            mapLis = session.selectList(QM_NAMESPACE + "selectAuto", new QmBaseDto(entity, false).getParamsMap());
            session.commit();
        } catch (Exception e) {
            throw new QmBaseException(getErrorMsg("autoSelectList"), e);
        } finally {
            session.close();
        }
        // 如果是空的直接返回null
        if (mapLis == null) {return null;}
        // 如果数据库字段是下划线样式 则进行转换
        QmTable qmTable = entity.getClass().getAnnotation(QmTable.class);
        if (qmTable != null && qmTable.style() == QmStyle.UNDERLINE) {
            for (int i = 0; i < mapLis.size(); i++) {
                Map<String, Object> map = mapLis.get(i);
                map = QmBaseStyleUtils.transformMapForHump(map);
                mapLis.set(i, map);
            }
        }
        return ConvertUtil.mapsToObjects(mapLis, clamm);
    }

    @Override
    public <Q> Q autoSelectOne(Q entity, Class<Q> clamm) {
        SqlSession session = sqlSessionFactory.openSession();
        Map<String, Object> map;
        try {
            if (entity == null) {entity = clamm.newInstance();}
            map = session.selectOne(QM_NAMESPACE + "selectAutoOne", new QmBaseDto(entity, false).getParamsMap());
            session.commit();
        } catch (Exception e) {
            throw new QmBaseException(getErrorMsg("autoSelectOne"), e);
        } finally {
            session.close();
        }
        if (map == null) {return null;}
        // 如果数据库字段是下划线样式 则进行转换
        QmTable qmTable = entity.getClass().getAnnotation(QmTable.class);
        if (qmTable != null && qmTable.style() == QmStyle.UNDERLINE) {
            map = QmBaseStyleUtils.transformMapForHump(map);
        }
        // 如果是空的直接返回null
        return ConvertUtil.mapToBean(map, entity);
    }

    @Override
    public <Q> int autoInsert(Q entity) {
        if (entity == null) {return -1;}
        SqlSession session = sqlSessionFactory.openSession();
        try {
            int result = session.insert(QM_NAMESPACE + "insertAuto", new QmBaseDto(entity, true).getParamsMap());
            session.commit();
            return result;
        } catch (Exception e) {
            throw new QmBaseException(getErrorMsg("autoInsert"), e);
        } finally {
            session.close();
        }
    }

    @Override
    public <Q> int autoUpdate(Q entity) {
        if (entity == null) {return -1;}
        SqlSession session = sqlSessionFactory.openSession();
        try {
            int result = session.update(QM_NAMESPACE + "updateAuto", new QmBaseDto(entity, true).getParamsMap());
            session.commit();
            return result;
        } catch (Exception e) {
            throw new QmBaseException(getErrorMsg("autoUpdate"), e);
        } finally {
            session.close();
        }
    }


    @Override
    public <Q> int autoDelete(Q entity) {
        if (entity == null) {return -1;}
        SqlSession session = sqlSessionFactory.openSession();
        try {
            int result = session.delete(QM_NAMESPACE + "deleteAuto", new QmBaseDto(entity, true).getParamsMap());
            session.commit();
            return result;
        } catch (Exception e) {
            throw new QmBaseException(getErrorMsg("autoDelete"), e);
        } finally {
            session.close();
        }
    }

    @Override
    public <Q> int autoSelectCount(Q entity) {
        if (entity == null) {return -1;}
        SqlSession session = sqlSessionFactory.openSession();
        try {
            int result = session.selectOne(QM_NAMESPACE + "selectCount", new QmBaseDto(entity, false).getParamsMap());
            session.commit();
            return result;
        } catch (Exception e) {
            throw new QmBaseException(getErrorMsg("autoSelectCount"), e);
        } finally {
            session.close();
        }
    }

    /**
     * 配置错误信息
     *
     * @param methodName
     * @return
     */
    private String getErrorMsg(String methodName) {
        return "SQL error using " + methodName + "! 使用 " + methodName + " 发生SQL错误!";
    }

}
