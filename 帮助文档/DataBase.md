# 数据持久层QmBase

## 描述

### 前言

数据持久层在框架中已经封装成为`QmBase`，开发时直接使用`QmBase`提供的方法进行数据操作。

它是基于`Mybatis`进行的一系列封装，由框架内部实现了`QmBase`的接口。

### 特点

- 自动SQL增删改查操作。
- 高度自由的数据持久交互。
- 完全开源

### 实现过程

`Spring`中获取`SqlSessionFactory`，进而使用`Mybatis`的`SqlSession`传递命名空间和参数列表进行一系列的封装。

### 连接池

目前已集成`Druid`、`dbcp2`两种连接池自由提供使用。

### 半自动SQL

在使用过程中，只需要对实体类进行一些必要的注解修饰，就可以利用实体类进行自动SQL增删改查，无需书写SQL完成业务数据库操作。也可以自行书写SQL完成复杂的业务数据库操作。

## Druid阿里连接池

> 框架中集成`Druid`连接池，该连接池是目前最好的连接池。

### 访问数据分析后台

#### **Step.1**

> 创建`Druid`配置类

```java
package com.qm.code.druid;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.qm.frame.basic.config.QmDataSourceFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/5/2 0:16
 * @Description Druid servlet and filter setting config
 */
@Configuration
public class DruidConfig {

    /**
     * 注册一个StatViewServlet，Druid依赖
     *
     * @return ServletRegistrationBean<StatViewServlet>
     */
    @Bean
    public ServletRegistrationBean<StatViewServlet> setDruidStatViewServlet() {
        return QmDataSourceFactory.getServletRegistrationBean();
    }

    /**
     * 注册一个FilterRegistrationBean，Druid依赖
     *
     * @return FilterRegistrationBean<WebStatFilter>
     */
    @Bean
    public FilterRegistrationBean<WebStatFilter> setDruidWebStatFilter() {
        return QmDataSourceFactory.getFilterFilterRegistrationBean();
    }


}
```

#### **Step.2**

> 配置`db.properties`，更加细致的配置请留意[【配置文件说明】](https://github.com/starmcc/QMBootFrame/wiki/QmSecurityConfig)

```properties
#※※※※※※※※※※※※※※※※※※ 数据源配置 ※※※※※※※※※※※※※※※※※※
# 使用的连接池名称 druid / dbcp2
type-name=druid
# 驱动名
driver-class-name=com.mysql.cj.jdbc.Driver
# 连接地址
url=jdbc:mysql://localhost:3306/test
# 用户名
username=root
# 密码
password=123
```

#### **Step.3**

> 访问`localhost/druid`即可访问。



## DBCP2连接池

> 框架也可以选择`DBCP2`连接池，相对于`Druid`连接池而言，就简单多了。

> 配置一下`db.properties`中的`type-name`节点为`dbcp2`即可。

```properties
#※※※※※※※※※※※※※※※※※※ 数据源配置 ※※※※※※※※※※※※※※※※※※
# 使用的连接池名称 druid / dbcp2
type-name=dbcp2
# 驱动名
driver-class-name=com.mysql.cj.jdbc.Driver
# 连接地址
url=jdbc:mysql://localhost:3306/test
# 用户名
username=root
# 密码
password=123
```



## 进入开发流程

### *Step.1*

> 开发过程中，在`Service`中注入`QmBase`。

```java
@Service
public class UserServiceImpl implements UserService {

	@Resource
	private QmBase qmBase;
}
```

### *Step.2*

#### 使用通用方法

> 调用`QmBase`中提供的通用方法

```Java
@Override
public List<User> getList(User user) {
    return qmBase.autoSelectList(user, User.class);
}
```

#### 使用基础方法

##### 【1】创建对应业务的Mapper文件

> 请根据`yml`全局配置文件中的`mybatis`配置项`mapper-locations`的扫描路径存放`Mapper`文件。
>
> ```
> mapper-locations:
> - 'classpath*:**/*Mapper.xml' # 该行配置请勿修改
> ```
>
> **如需特殊配置，该行配置请勿修改。请自行增加一条扫描路径。**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!-- namespace是自定义的，但请规范它，规范在帮助文档中有所提及 -->
<mapper namespace="UserManager-UserServiceImpl-0-Mapper">
    
	<select id="getUser" resultType="hashmap">
		select * from `qm_user`
	</select>
    
</mapper>
```

> **这里说明namespace的约定规范**
>
> `Mybatis`自动扫描`Mapper`后，将该`Mapper`注册到`Spring`中。
>
> 如果名字不进行规范，会有一些预料不到的错误发生，`spring`内管理的`bean`相互冲突是我们不想看到的。
>
> 如果不按照格式，则直接抛出异常。

```java
namespace="调用模块-调用类名-编号-Mapper"  //大驼峰 + 规范化
```

##### 【2】Service调用QmBase基础方法

```java
@Service
public class UserServiceImpl implements UserService {

	@Resource
	QmBase qmBase;

	private static final String NAMESCAPSE = "Info-UserServiceImpl-0-Mapper";

	@Override
	public List<User> getList(String userName) {
		return qmBase.selectList(NAMESCAPSE + "getUser", userName);
	}
}
```

### *Step.3*

> 实体类中使用框架提供的注解`@QmTable`、`@QmId`，这些注解类似于`JPA`注解的使用

```java
@QmTable(name="qm_user")
public class User {
	@QmId
	private Integer id;
	private String userName;
	private String password;
	private Integer roleId;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", password=" + password + ", roleId=" + roleId + "]";
	}
}
```

> 框架目前只提供3个注解，下面有详细的注解说明，自动SQL是通过实体类的注解进行SQL封装。

### *Step.4*

> 编写控制器接口，访问测试成功获取数据。



## 实体类注解说明

> 目前支持的注解

---

### @QmTable

> 注明该实体类对应数据库的表名，使用`QmBase`的`auto`系列必须注明该注解。一共有两个参数，`name`/`style`，如果不提供任何参数，则默认以类名作为表名。

#### 设置表名

* 属性(`name`)

  > 该属性标注实体类对应的表名，使用`QmBase`的`auto`方法必须提供该注解。标注在实体类的class上即可。

#### 设置实体类风格

* 属性(`style`)

  > 该属性标注该实体类字段风格类型，参数类型`QmStyle`枚举类，默认为`QmStyle.HUMP`即驼峰模式，如要修改为下划线模式则提供`QmStyle.UNDERLINE`。

---

### @QmId

> 注明该字段为主键字段，使用`QmBase`的`autoUpdate`、`autoDelete`方法必须提供该注解，而这些方法是通过id进行对数据表的操作。

#### 设置主键别名

* 属性(`name`)

  > 当类中主键属性名与数据库的主键字段名不一致时，使用`name`属性可改变`QmBase`识别的主键字段名，默认为使用当前属性名作为主键。

#### 设置使用uuid策略

* 属性(`uuid`)

  > 当需要使用uuid策略时，请赐予`uuid`属性为`true`，默认为`flase`。如果为`true`时，在使用`autoInsert`时，会自动生成一个`uuid`而无需手动生成。

---

### @QmParams

> 当该字段不需要识别或名称与数据库不一致时，可以使用该注解进行标注。

#### 设置字段别名

* 属性(`name`)

  > 当类中属性名与数据库的字段名不一致时，使用`name`属性可改变`QmBase`识别的字段名，默认为使用当前属性名作为字段名。


* 属性(`except`)

  > 如果在实体类中需要排除某些字段识别，则给`except`设置为`true`即可。默认为`flase`。