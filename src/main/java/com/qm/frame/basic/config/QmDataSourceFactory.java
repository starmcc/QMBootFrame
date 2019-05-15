package com.qm.frame.basic.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.qm.frame.basic.exception.QmFrameException;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;

import javax.sql.DataSource;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/5/1 15:46
 * @Description QmDataSourceFactory
 */
public class QmDataSourceFactory {

    /**
     * properties 配置读取
     */
    private static final Properties PRO = getProperties();
    /**
     * // 连接池类型
     */
    private static String typeName = getProString("type-name", "dbcp2");
    private static String driverClassName = getProString("driver-class-name", "com.mysql.jdbc.Driver");
    private static String url = getProString("url", "");
    private static String userName = getProString("username", "root");
    private static String password = getProString("password", "");
    private static ServletRegistrationBean<StatViewServlet> servletRegistrationBean;
    private static FilterRegistrationBean<WebStatFilter> filterFilterRegistrationBean;

    /**
     * 注册一个StatViewServlet，Druid依赖
     *
     * @return ServletRegistrationBean<StatViewServlet>
     */
    public static ServletRegistrationBean<StatViewServlet> getServletRegistrationBean() {
        String allow = getProString("druid.allow", "localhost");
        String deny = getProString("druid.deny", "");
        String loginUsername = getProString("druid.login.username", "admin");
        String loginPassword = getProString("druid.login.password", "123");
        String resetEnable = getProString("druid.reset.enable", "false");
        String inMatchUrl = getProString("druid.inMatchUrl", "/druid/*");
        //org.springframework.boot.context.embedded.ServletRegistrationBean提供类的进行注册.
        servletRegistrationBean = new ServletRegistrationBean<StatViewServlet>
                (new StatViewServlet(), inMatchUrl);
        //添加初始化参数：initParams
        //白名单：
        servletRegistrationBean.addInitParameter("allow", allow);
        //IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not permitted to views this page.
        servletRegistrationBean.addInitParameter("deny", deny);
        //登录查看信息的账号密码.
        servletRegistrationBean.addInitParameter("loginUsername", loginUsername);
        servletRegistrationBean.addInitParameter("loginPassword", loginPassword);
        //是否能够重置数据.
        servletRegistrationBean.addInitParameter("resetEnable", resetEnable);
        return servletRegistrationBean;
    }

    /**
     * 注册一个FilterRegistrationBean，Druid依赖
     *
     * @return FilterRegistrationBean<WebStatFilter>
     */
    public static FilterRegistrationBean<WebStatFilter> getFilterFilterRegistrationBean() {
        filterFilterRegistrationBean = new FilterRegistrationBean<WebStatFilter>(new WebStatFilter());
        // 添加过滤规则.
        filterFilterRegistrationBean.addUrlPatterns("/*");
        // 添加不需要忽略的格式信息.
        String exclusions = getProString("druid.exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        filterFilterRegistrationBean.addInitParameter("exclusions", exclusions);
        return filterFilterRegistrationBean;
    }


    /**
     * 构建一个datasource配置
     */
    public static DataSource getDataSource() {
        DataSource dataSource = null;
        final String[] typeFinal = {"druid", "dbcp2"};
        try {
            // if druid
            if (typeFinal[0].equalsIgnoreCase(typeName)) {
                dataSource = initDruidDataSource();
                // if dbcp2
            } else if (typeFinal[1].equalsIgnoreCase(typeName)) {
                dataSource = initDbcp2DataSource();
            } else {
                dataSource = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataSource;
    }

    /**
     * 配置dbcp2数据源
     *
     * @return
     */
    private static BasicDataSource initDbcp2DataSource() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName(driverClassName);
        basicDataSource.setUrl(url);
        basicDataSource.setUsername(userName);
        basicDataSource.setPassword(password);
        basicDataSource.setMaxWaitMillis(getProInt("dbcp2.max-wait-millis", 10000));
        basicDataSource.setMinIdle(getProInt("dbcp2.min-idle", 5));
        basicDataSource.setInitialSize(getProInt("dbcp2.initial-size", 5));
        basicDataSource.setValidationQuery(getProString("dbcp2.validation-query", "SELECT 1 FROM DUAL"));
        basicDataSource.setConnectionProperties(getProString("dbcp2.connection-properties", "characterencoding=utf8"));
        return basicDataSource;
    }

    /**
     * 配置Druid数据源
     *
     * @return
     * @throws SQLException
     */
    private static DruidDataSource initDruidDataSource() throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(driverClassName);
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(userName);
        druidDataSource.setPassword(password);
        druidDataSource.setInitialSize(getProInt("druid.initialSize", 5));
        druidDataSource.setMinIdle(getProInt("druid.minIdl", 5));
        druidDataSource.setMaxActive(getProInt("druid.maxActive", 20));
        druidDataSource.setMaxWait(getProInt("druid.maxWait", 60000));
        druidDataSource.setTimeBetweenEvictionRunsMillis(getProInt("druid.timeBetweenEvictionRunsMillis", 60000));
        druidDataSource.setMinEvictableIdleTimeMillis(getProInt("druid.minEvictableIdleTimeMillis", 300000));
        druidDataSource.setValidationQuery(getProString("druid.validationQuery", "SELECT 1 FROM DUAL"));
        druidDataSource.setTestWhileIdle(getProBoolean("druid.testWhileIdle", true));
        druidDataSource.setTestOnBorrow(getProBoolean("druid.testOnBorrow", false));
        druidDataSource.setTestOnReturn(getProBoolean("druid.testOnReturn", false));
        druidDataSource.setPoolPreparedStatements(getProBoolean("druid.poolPreparedStatements", true));
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(getProInt("druid.maxPoolPreparedStatementPerConnectionSize", 20));
        druidDataSource.setFilters(getProString("druid.filters", "stat,wall"));
        druidDataSource.setConnectionProperties(getProString("druid.connectionProperties", "stat.mergeSql=true;stat.slowSqlMillis=5000"));
        druidDataSource.setUseGlobalDataSourceStat(getProBoolean("druid.useGlobalDataSourceStat", true));

        return druidDataSource;
    }

    private static Properties getProperties() {
        try {
            Properties properties = new Properties();
            // 读取properties文件,使用InputStreamReader字符流防止文件中出现中文导致乱码
            InputStreamReader inStream = new InputStreamReader
                    (QmDataSourceFactory.class.getClassLoader().getResourceAsStream("db.properties"),
                            "UTF-8");
            properties.load(inStream);
            inStream.close();
            return properties;
        } catch (Exception e) {
            throw new QmFrameException("读取db.properties发生了异常！", e);
        }
    }

    private static boolean getProBoolean(String key) {
        return Boolean.parseBoolean(PRO.getProperty(key, "false"));
    }

    private static boolean getProBoolean(String key, boolean defaultVal) {
        return Boolean.parseBoolean(PRO.getProperty(key, String.valueOf(defaultVal)));
    }

    private static int getProInt(String key) {
        return Integer.parseInt(PRO.getProperty(key, "0"));
    }

    private static int getProInt(String key, int defaultVal) {
        return Integer.parseInt(PRO.getProperty(key, String.valueOf(defaultVal)));
    }

    private static String getProString(String key) {
        return PRO.getProperty(key);
    }

    private static String getProString(String key, String defaultVal) {
        return PRO.getProperty(key, defaultVal);
    }

}
