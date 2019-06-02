# 项目配置文件 V1.0.0

## 配置文件目录

* application.yml - 主配置文件
* config.properties - 自定义配置文件
* db.properties - 数据源配置文件
* logback-spring.xml - 框架日志配置文件
* qm-frame.properties - 框架骨干配置文件
* verify.properties - 验证工具配置文件



## Spring主配置文件

> **`application.yml`**

`springboot`配置文件，用于配置`spring`基础配置，一些配置在注释中已描述的不允许更改的地方请勿更改！

```yaml
#※※※※※※※※※※※※Server※※※※※※※※※※※※
server:
  port: 80
  tomcat: 
      url-encoding: UTF-8
  servlet:
    context-path: #项目路径

#※※※※※※※※※※※※SpringBoot基础配置※※※※※※※※※※※※
spring:
  # MVC 配置
  mvc:
    #出现错误时, 直接抛出异常
    throw-exception-if-no-handler-found: true
  resources: 
    #不要为我们工程中的资源文件建立映射
    add-mappings: false
  #配置thymeleaf 官方推荐模板引擎
  thymeleaf:
    prefix: 'classpath:/views/'
    suffix: '.html'
    mode: 'HTML'
    encoding: 'UTF-8'
    content-type: 'text/html'
    cache: false # 开发阶段务必关闭缓存
  #※※※Redis缓存服务器※※※
  redis: 
    database: 0 #Redis数据库索引（默认为0）
    host: 'localhost' #Redis服务器地址
    port: 6379 #Redis服务器连接端口 
    password: '' #Redis服务器连接密码（默认为空）
    pool: 
      max-active: 200 #连接池最大连接数（使用负值表示没有限制）
      max-wait: -1 #连接池最大阻塞等待时间（使用负值表示没有限制）
      max-idle: 10 #连接池中的最大空闲连接
      min-idle: 0 #连接池中的最小空闲连接 
    timeout: 1000 #连接超时时间（毫秒）
  velocity:
    content-type: 'application/json'

#※※※※※※※※※※※※Mybatis※※※※※※※※※※※※
mybatis:
  type-aliases-package: com.qm.code.entity # 指定实体类包路径
  mapper-locations:
  - 'classpath*:**/*Mapper.xml' # 该行配置请勿修改
  configuration:
    map-underscore-to-camel-case: true #是否启动数据库下划线自动映射实体
    auto-mapping-behavior: full # resultMap 自动映射级别设置

#※※※※※※※※※※※※ PageHelper 分页插件 ※※※※※※※※※※※※
pagehelper:
  # helperDialect 分页插件会自动检测当前的数据库链接，自动选择合适的分页方式。 你可以配置helperDialect属性来指定分页插件使用哪种方言。配置时，可以使用下面的缩写值：
  # oracle,mysql,mariadb,sqlite,hsqldb,postgresql,db2,sqlserver,informix,h2,sqlserver2012,derby
  helperDialect: mysql
  # reasonable 分页合理化参数，默认值为false。
  # 当该参数设置为 true 时，pageNum<=0 时会查询第一页， pageNum>pages（超过总数时），会查询最后一页。
  # 默认false 时，直接根据参数进行查询。
  reasonable: true
  # supportMethodsArguments 支持通过 Mapper 接口参数来传递分页参数，默认值false，分页插件会从查询方法的参数值中，自动根据上面 params 配置的字段中取值，查找到合适的值时就会自动分页。
  supportMethodsArguments: true
  # params为了支持startPage(Object params)方法，增加了该参数来配置参数映射，用于从对象中根据属性名取值， 可以配置 pageNum,pageSize,count,pageSizeZero,reasonable，不配置映射的用默认值， 默认值为pageNum=pageNum;pageSize=pageSize;count=countSql;reasonable=reasonable;pageSizeZero=pageSizeZero。
  params: count=countSql
  # 默认值为 false，当该参数设置为 true 时，如果 pageSize=0 或者 RowBounds.limit = 0 就会查询出全部的结果（相当于没有执行分页查询，但是返回结果仍然是 Page 类型）。
  page-size-zero: true

#※※※※※※※※※※※※Logger※※※※※※※※※※※※
logging:
  config: 'classpath:logback-spring.xml'
```



## 数据源配置文件

> **`db.properties`**

**注意：** 以下的`dbcp2`和`druid`节点的相关配置为框架默认配置，也就是说，即使不配置的情况下，框架也会默认采用下面的配置。

```properties
#※※※※※※※※※※※※※※※※※※ 数据源配置 ※※※※※※※※※※※※※※※※※※
# 驱动名
driver-class-name=com.mysql.cj.jdbc.Driver
# 连接地址
url=jdbc:mysql://localhost:3306/test
# 用户名
username=root
# 密码
password=123

#※※※※※※※※※※※※※※※※※※ DRUID连接池配置 ※※※※※※※※※※※※※※※※※※
# 下面为连接池的补充设置，应用到上面所有数据源中
# 初始化大小，最小，最大
druid.initialSize=5
druid.minIdle=5
druid.maxActive=20
# 配置获取连接等待超时的时间
druid.maxWait=60000
# 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
druid.timeBetweenEvictionRunsMillis=60000
# 配置一个连接在池中最小生存的时间，单位是毫秒
druid.minEvictableIdleTimeMillis=300000
druid.validationQuery=SELECT 1 FROM DUAL
druid.testWhileIdle=true
druid.testOnBorrow=false
druid.testOnReturn=false
# 打开PSCache，并且指定每个连接上PSCache的大小
druid.poolPreparedStatements=true
druid.maxPoolPreparedStatementPerConnectionSize=20
# 配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
druid.filters=stat,wall
# 通过connectProperties属性来打开mergeSql功能；慢SQL记录
druid.connectionProperties=stat.mergeSql=true;stat.slowSqlMillis=5000
# 合并多个DruidDataSource的监控数据
druid.useGlobalDataSourceStat=true
#白名单
druid.allow=localhost
#黑名单
druid.deny=
#账号
druid.login.username=admin
#密码
druid.login.password=123
#是否可重置数据
druid.reset.enable=false
#用于排除一些不必要的url，比如.js,/jslib/等等。
druid.exclusions=*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*
#druid统计入口
druid.inMatchURL=/druid/*
```



## 框架骨干配置文件

> **`qm-frame.properties`**

```properties
#※※※※※※※※※AES加密设置※※※※※※※※※
#是否启用aes对称加密传输
aes.start=false
#aes秘钥
aes.key=ohiah2019sDShdieub51h8910s
#统一使用的编码方式
aes.encoding=UTF-8
#加密次数
aes.number=1
#※※※※※※※※※请求数据设置※※※※※※※※※
#请求数据时，根据该key名解析数据(rest风格)
body.request.key=value
#返回数据时，使用的最外层key名(rest风格)
body.response.key=value
#返回数据时，默认message的语言 EN/CN
body.response.message.lang=CN

#※※※※※※※※※版本控制※※※※※※※※※
#是否开启版本控制(ture时,每个请求需在header带上version参数,参数值version)
version.start=false
#系统目前版本编号
version.now=1.0.0
#系统容忍请求版本编号(默认允许当前版本)
version.allows-[0]:1.0.1
version.allows-[1]:1.0.2
#※※※※※※※※※日志记录※※※※※※※※※
controller.aop.extend.class=

#※※※※※※※※※特殊请求过滤※※※※※※※※※
#特殊请求不进行解析(包括版本控制和解析json等)
#该配置主要排除第三方API调用接口时特殊请求而框架自动解析json的问题
#适用于动态配置，例：/user/**
request.special.uri-[0]=
```



## 验证工具配置文件

> **`verify.properties`**

该配置文件主要运用于`QmVerifyUtil`工具类，校验正则的设置。

```properties
# ※※※※※※※※※※※※※※※※※※校验正则配置文件※※※※※※※※※※※※※※※※※※
# 验证用户名 限制条件：首字符为英文字母,账号长度为5~20位,且不能出现特殊字符
username=^[A-Za-z][a-zA-Z0-9]{4,19}$
username.errorMsg=用户名格式错误,首字符为英文字母,账号长度为5~20位,且不能出现特殊字符
# 正则表达式：验证密码 限制条件：长度为6~20位
password=^\\w{6,20}$
password.errorMsg=密码长度必须为6~20位
# 正则表达式：验证手机号
mobile=^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$
mobile.errorMsg=手机号格式错误
# 验证邮箱
email=^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$
email.errorMsg=邮箱格式错误
# 验证汉字 3 ~ 10个汉字
chinese=^[\u4e00-\u9fa5]{3,10}$
chinese.errorMsg=验证汉字失败
# 验证身份证
cardId=(^\\d{18}$)|(^\\d{15}$)
cardId.errorMsg=身份证格式错误
# 验证URL
url=http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?
url.errorMsg=URL格式错误
# 验证IP地址
ip=(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)
ip.errorMsg=IP格式错误
```



## 框架日志配置文件

> **`logback-spring`**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- 从高到地低 OFF 、 FATAL 、 ERROR 、 WARN 、 INFO 、 DEBUG 、 TRACE 、 ALL -->
<!-- 日志输出规则  根据当前ROOT 级别，日志输出时，级别高于root默认的级别时  会输出 -->
<!-- 以下  每个配置的 filter 是过滤掉输出文件里面，会出现高级别文件，依然出现低级别的日志信息，通过filter 过滤只记录本级别的日志-->


<!-- 属性描述 scan：性设置为true时，配置文件如果发生改变，将会被重新加载，默认值为true scanPeriod:设置监测配置文件是否有修改的时间间隔，如果没有给出时间单位，默认单位是毫秒。当scan为true时，此属性生效。默认的时间间隔为1分钟。
debug:当此属性设置为true时，将打印出logback内部日志信息，实时查看logback运行状态。默认值为false。 -->
<configuration scan="true" scanPeriod="60 seconds" debug="false">
    <!-- 定义日志文件 输入位置 -->
    <property name="log_dir" value="/logs/"/>
    <!-- 日志最大的历史 30天 -->
    <property name="maxHistory" value="30"/>
    <!-- 无颜色格式 -->
    <!--<property name="CONSOLE_LOG_PATTERN" value="QM控制台 > %-5level| %d{yyMMdd HHmmss} | %msg | %logger | %thread%n"/>-->
    <!-- 有颜色格式 -->
    <property name="CONSOLE_LOG_PATTERN"
              value="QM控制台 > %highlight(%-5level)| %d{yyMMdd HHmmss} | %msg | %boldGreen(%logger) | %boldYellow(%thread) %n"/>
    <!-- 注意：生产环境务必使用没有颜色的日志,不然会增加服务器的压力,造成未知的后果自己负责 -->
    <!-- ConsoleAppender 控制台输出日志 -->
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <!-- 对日志进行格式化 -->
        <encoder>
            <pattern>${CONSOLE_LOG_PATTERN}</pattern>
            <charset>UTF-8</charset>
        </encoder>
    </appender>
    <!-- ERROR级别日志 -->
    <!-- 滚动记录文件，先将日志记录到指定文件，当符合某个条件时，将日志记录到其他文件 RollingFileAppender-->
    <appender name="ERROR" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- 过滤器，只记录WARN级别的日志 -->
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>ERROR</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <!-- 最常用的滚动策略，它根据时间来制定滚动策略.既负责滚动也负责出发滚动 -->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!--日志输出位置  可相对、和绝对路径 -->
            <fileNamePattern>${log_dir}/%d{yyyy-MM-dd}/error-log.log</fileNamePattern>
            <!-- 可选节点，控制保留的归档文件的最大数量，超出数量就删除旧文件假设设置每个月滚动，且<maxHistory>是6，
            则只保存最近6个月的文件，删除之前的旧文件。注意，删除旧文件是，那些为了归档而创建的目录也会被删除-->
            <maxHistory>${maxHistory}</maxHistory>
        </rollingPolicy>

        <!-- 按照固定窗口模式生成日志文件，当文件大于20MB时，生成新的日志文件。窗口大小是1到3，当保存了3个归档文件后，将覆盖最早的日志。
        <rollingPolicy class="ch.qos.logback.core.rolling.FixedWindowRollingPolicy">
        <fileNamePattern>${log_dir}/%d{yyyy-MM-dd}/.log.zip</fileNamePattern>
        <minIndex>1</minIndex>
        <maxIndex>3</maxIndex>
        </rollingPolicy>   -->
        <!-- 查看当前活动文件的大小，如果超过指定大小会告知RollingFileAppender 触发当前活动文件滚动
        <triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
        <maxFileSize>5MB</maxFileSize>
        </triggeringPolicy>   -->

        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger - %msg%n</pattern>
        </encoder>
    </appender>


    <!-- WARN级别日志 appender -->
    <appender name="WARN" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- 过滤器，只记录WARN级别的日志 -->
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>WARN</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!-- 按天回滚 daily -->
            <fileNamePattern>${log_dir}/%d{yyyy-MM-dd}/warn-log.log
            </fileNamePattern>
            <!-- 日志最大的历史 60天 -->
            <maxHistory>${maxHistory}</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger - %msg%n</pattern>
        </encoder>
    </appender>


    <!-- INFO级别日志 appender -->
    <appender name="INFO" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- 过滤器，只记录INFO级别的日志 -->
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>INFO</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!-- 按天回滚 daily -->
            <fileNamePattern>${log_dir}/%d{yyyy-MM-dd}/info-log.log
            </fileNamePattern>
            <!-- 日志最大的历史 60天 -->
            <maxHistory>${maxHistory}</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger - %msg%n</pattern>
        </encoder>
    </appender>


    <!-- DEBUG级别日志 appender -->
    <appender name="DEBUG" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- 过滤器，只记录DEBUG级别的日志 -->
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>DEBUG</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!-- 按天回滚 daily -->
            <fileNamePattern>${log_dir}/%d{yyyy-MM-dd}/debug-log.log
            </fileNamePattern>
            <!-- 日志最大的历史 60天 -->
            <maxHistory>${maxHistory}</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger - %msg%n</pattern>
        </encoder>
    </appender>


    <!-- TRACE级别日志 appender -->
    <appender name="TRACE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- 过滤器，只记录ERROR级别的日志 -->
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>TRACE</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!-- 按天回滚 daily -->
            <fileNamePattern>${log_dir}/%d{yyyy-MM-dd}/trace-log.log
            </fileNamePattern>
            <!-- 日志最大的历史 60天 -->
            <maxHistory>${maxHistory}</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger - %msg%n</pattern>
        </encoder>
    </appender>

    <logger name="java.sql.PreparedStatement" value="DEBUG" />
    <logger name="java.sql.Connection" value="DEBUG" />
    <logger name="java.sql.Statement" value="DEBUG" />
    <logger name="com.ibatis" value="DEBUG" />
    <logger name="com.ibatis.common.jdbc.SimpleDataSource" value="DEBUG" />
    <logger name="com.ibatis.common.jdbc.ScriptRunner" level="DEBUG"/>
    <logger name="com.ibatis.sqlmap.engine.impl.SqlMapClientDelegate" value="DEBUG" />
    <logger name="com.alibaba.druid.pool.DruidDataSource" value="DEBUG" />


    <!-- root级别   DEBUG -->
    <root level="INFO">
        <!-- 控制台输出 -->
        <appender-ref ref="STDOUT"/>
        <!-- 文件输出 -->
        <appender-ref ref="ERROR"/>
        <appender-ref ref="WARN"/>
        <appender-ref ref="INFO"/>
        <appender-ref ref="DEBUG"/>
        <appender-ref ref="TRACE"/>
    </root>
</configuration>
```



## 自定义配置文件

> **`config.properties`**

调用`PropertiesUtil`工具类默认读取该配置文件，建议所有自定义的配置都放进该配置文件中。

```properties
#自定义配置文件
xxx.xxx=xxx
```
