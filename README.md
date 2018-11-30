# QMBootFrame

## 架构介绍

### 开发环境

- JDK1.8
- Maven
- MySQL
- SpringBoot2.1.0
- Redis
- Ehcache
- Tomcat8.5

### 封装摘要

- Mybatis数据持久层

  > 数据持久层统一使用`QmBase`进行数据层开发。独立Mapper文件，无需创建Dao文件。

- Druid数据库连接池。（公认`Java`最好的连接池，由阿里巴巴开发）

  > 项目启动时直接访问`http://localhost/druid`即可

- QmSercurity权限管理器

  > 实现登录、注销、权限注解拦截、使用`JWT`提供的`Token`技术。
  >
  > 详情请见QmSercurity说明文档

- Redis缓存工具类

  > 直接调用`QmRedisUtil`即可实现key、value的存储
  >
  > 依赖Spring管理的静态工具类

- Ehcache全局缓存依赖

  > 在方法上直接调用`@Cacheable`注解可直接缓存该方法的返回值。
  >
  > 请了解Ehcache的官方注解说明文档。

- 全局Error错误返回JSON信息

  > 在服务器发生任何错误都将返回规范化JSON格式的字符串。

- 贯彻使用RestFul风格

  > 在`Controller`中继承`QmController`调用规定的方法。

- 重写RequestBody实现对称3DES加密数据技术

  > 在请求时拦截，获取body参数并对其进行解密格式化，把格式化后的body原路放置。

- RequestBody自定义注解@QmBody

  > 在请求时对请求body中的json数据进行自动装配,在controller中参数列表可直接获取对应类型的参数。

- 百度地图工具类（未测试）

- 微信工具类（未完善）

### 架构目录

- com.qm
  > 项目总目录

  - code

    > 项目源码

    - controller

      > 控制器

    - entity

      > 实体类

    - service

      > 业务层

    - sql

      > Mapper文件存放处

  - frame
    > 框架源码

    - baidu

      > 百度地图工具包

    - druid

      > 连接池

    - basic

      > 框架基础设施包

    - mybatis

      > mybatis封装包

    - qmsecurity

      > 权限管理器

    - redis

      > Redis缓存工具包

    - wechat

      > 微信工具包

### 详细说明

请移步【[帮助文档](https://github.com/starmcc/QMBootFrame/wiki)】进行阅读。


### 更新日志

请移步【[更新日志](https://github.com/starmcc/QMBootFrame/wiki/version)】进行阅读。


### 关于作者

- 小生不才,黄阶中级,跪求各路大仙路过指点迷津
- 浅梦在此感谢各位的Star
- email:starczt1992@163.com
- 一个纯粹的Java农民

