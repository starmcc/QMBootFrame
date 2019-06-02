# 快速入门

## 更改项目名称

> 成功下载框架后，第一时间请更改自己的项目名称，并修改`pom.xml`

```xml
<!-- 包名,项目名,版本 -->
<groupId>com.qm</groupId>
<artifactId>QMBootFrame</artifactId>
<version>0.0.1-SNAPSHOT</version>
<packaging>jar</packaging>
<!-- 项目名 -->
<name>QMBootFrame</name>
<description>Spring-Boot</description>
```

## 修改配置文件

> 修改`db.properties`数据库配置文件

> 修改`Mybatis`的实体类包路径

```yaml
#※※※※※※※※※※※※Mybatis※※※※※※※※※※※※
mybatis:
  type-aliases-package: com.qm.code.entity # 指定实体类包路径
```

> 注意：`mapper-locations`请勿修改，框架`QmBase`需依赖此扫描。

> `classpath*:**/*Mapper.xml`该配置已经默认扫描整个项目的`mapper`文件，无需更改。

> 配置完成后可直接运行项目即可。

##  启动项目

> 直接运行`QmApplication`的`main`方法即可启动项目。