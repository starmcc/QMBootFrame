# QMBootFrame

## 架构介绍
### 开发环境

- JDK1.8
- Maven
- MySQL
- SpringBoot2.1.0
- Redis
- Tomcat8.5

### 封装摘要

- Mybatis数据持久层

  > 数据持久层统一使用`QmBase`进行数据层开发。独立`Mapper`文件，无需创建`Dao`文件。

- Druid数据库连接池。

  > 公认`Java`最友好的数据库连接池。

- QmSercurity权限管理器

  > 实现登录、注销、权限注解拦截、使用`AES`提供的`Token`技术。
  > 详情请见`QmSercurity`说明文档

- Redis缓存工具类

  > 直接调用`QmRedisUtil`即可实现`key`、`value`的存储
  >
  > 依赖`Spring`管理的静态工具类

- 全局Error错误返回JSON信息

  > 在服务器发生任何错误都将返回规范化`JSON`格式的字符串。

- 贯彻使用RestFul风格

  > 在`Controller`中继承`QmController`调用规定的方法。

- 重写RequestBody实现AES双向对称加密数据技术

  > 在请求时拦截，获取`body`参数并对其进行解密格式化，把格式化后的`body`原路放置。

- RequestBody自定义注解@QmBody

  > 在请求时对请求`body`中的`json`数据进行自动装配,在`controller`中参数列表可直接获取对应类型的参数。

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

    - basic

      > 框架基础设施

    - mybatis

      > mybatis框架QmBase设施

    - qmsecurity

      > 用户安全框架



## 关于作者

- 小生不才,黄阶后期,跪求各路高手路过指点迷津
- 浅梦在此感谢各位的Star
- Email:starczt1992@163.com
- 一个纯粹的Java农民

> 指若下键万里行，如入浅梦醉逍遥