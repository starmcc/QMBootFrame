package com.qm.frame.basic.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.qm.frame.basic.exception.QmFrameException;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;

import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Properties;

/**
 * QmDataSourceFactory
 *
 * @Author qm
 * @Date 2019/5/1 15:46
 */
public final class QmDataSourceFactory {

    /**
     * properties 配置读取
     */
    private static final Properties PRO = getProperties();
    /**
     * // 连接池类型
     */
    private static String driverClassName = getProString("driver-class-name", "com.mysql.cj.jdbc.Driver");
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
     * 配置Druid数据源
     *
     * @return
     * @throws SQLException
     */
    public final static DruidDataSource getDruidDataSource() throws SQLException {
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

    private final static Properties getProperties() {
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

    private final static boolean getProBoolean(String key) {
        return Boolean.parseBoolean(PRO.getProperty(key, "false"));
    }

    private final static boolean getProBoolean(String key, boolean defaultVal) {
        return Boolean.parseBoolean(PRO.getProperty(key, String.valueOf(defaultVal)));
    }

    private final static int getProInt(String key) {
        return Integer.parseInt(PRO.getProperty(key, "0"));
    }

    private final static int getProInt(String key, int defaultVal) {
        return Integer.parseInt(PRO.getProperty(key, String.valueOf(defaultVal)));
    }

    private final static String getProString(String key) {
        return PRO.getProperty(key);
    }

    private final static String getProString(String key, String defaultVal) {
        return PRO.getProperty(key, defaultVal);
    }

}
