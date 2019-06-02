# 封装日志信息

> 框架采用`logback`作为日志的输出工具。

## 日志模板

> 注意：该名字必须为`logback-spring.xml`且放置在`resources`目录下

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

## 封装基本接口进出信息

> 接口参数打印
>
> 接口定位
>
> 接口出口参数
>
> 接口响应时间

```java
/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 * @author: 浅梦
 * @date: 2018年11月24日 上午1:34:28
 * @Description: 接口日志，返回请求时间，参数，返回值等信息。
 */
@Aspect // 等同于<aop:aspect ref="loggerWhole">
public @Component class LoggerWholeAOP {

	private static final Logger LOG = LoggerFactory.getLogger(LoggerWholeAOP.class);
	
	/**
	 * 记录请求时间
	 */
	private Long starTime;
	
	/**
	 * 切入点范围 execution(* com.qm..*.controller..*.*(..))
	 */
	@Pointcut("execution(* com.qm..*.controller..*.*(..))")
	public void qmPointcut() {
	}

	/**
	 * 环绕增强(最强版)
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Around("qmPointcut()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		System.out.println("※※※※※※※※※※※※※※※※※※");
		return pjp.proceed();
	}
	
	/**
	 * 前置增强
	 * @param jp
	 */
	@Before("qmPointcut()")
	public void before(JoinPoint jp) {
		starTime = System.currentTimeMillis();
		StringBuffer logStr = new StringBuffer();
		// getTarget得到被代理的目标对象(要切入的目标对象)
		logStr.append("\n※※※※※※※※※※※※※※※※※※\n");
		logStr.append("请求定位:" + jp.getTarget().getClass().getName() + "\n");
		// getSignature得到被代理的目标对象的方法名(返回被切入的目标方法名)
		logStr.append("请求方法:【" + jp.getSignature().getName() + "】\n");
		// Arrays.toString(jp.getArgs())获得目标方法的参数列表
		logStr.append("参数列表:【" + Arrays.toString(jp.getArgs()) + "】\n");
		logStr.append("※※※※※※※※※※※※※※※※※※\n");
		LOG.info(logStr.toString());
	}

	/**
	 * 后置增强
	 * @param jp
	 * @param result 方法返回的结果
	 */
	@AfterReturning(pointcut = "qmPointcut()", returning = "result")
	public void afterReturning(JoinPoint jp, Object result) {
		LOG.debug("\n※※※※※※※※※返回结果:【" + result + "】※※※※※※※※※\n");
		Long endTime = System.currentTimeMillis();
		Long time = endTime - starTime;
		LOG.info("\n※※※※※※※※※请求响应耗时：" + time + "/ms※※※※※※※※※");
	}	
}
```