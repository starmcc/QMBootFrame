# 视图框架

## 介绍

框架从V0.7.2开始，引入视图基础框架。框架中使用`SpringBoot`中自带的`springMvc`进行首页跳转,使用`thymeleaf`、`bootstrap`、`JQuery`和自行封装的`qmui.js`作为整体架构。

框架采用单页路由机制，替换`html`的`dom`节点进行页面切换实现，而使用`thymeleaf`模板引擎进行首页渲染和其他页面引入。作为其他模块页面则采用`HTML5`进行开发。



## 目录结构

- views（视图包）
  - src（源码包）
    - frame（框架依赖包）
      - bootstrap-x.x.x-dist（bootstrap框架css依赖）
      - jquery（JQuery依赖）
      - qmui（QMUI基础框架）
    - pages（模块页面）
    - static（静态包）
    - index.html（单页）
    - top.html（头部导航页）



## QMUI 特性

### 单页模板

> index.html

```html
<!DOCTYPE html>
<html lang="zh-CN" xmlns:th="http://www.thymeleaf.org";>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>QM-bootstrap-template</title>
    <!-- 网页图标 -->
    <link rel="shortcut icon" th:href="@{/views/frame/qmui/img/icon.jpg}">
    <!-- bootstrap框架css样式支持 -->
    <link rel="stylesheet" th:href="@{/views/frame/bootstrap-3.3.7-dist/css/bootstrap.min.css}">
    <!-- qm样式封装支持 -->
    <link rel="stylesheet" th:href="@{/views/frame/qmui/css/qmui.css}">
    <!-- jQuery -->
    <script th:src="@{/views/frame/jquery/jquery1.12.4.js}"></script>
    <!-- bootstrap js插件支持 -->
    <script th:src="@{/views/frame/bootstrap-3.3.7-dist/js/bootstrap.min.js}"></script>
    <!-- qmui-js封装支持 -->
    <script th:src="@{/views/frame/qmui/js/qmui.js}"></script>
    <!-- 路由支持 -->
    <pages name="home" to="/views/pages/home.html"  js="/views/pages/home.js"/>
    <pages name="about" to="/views/pages/about.html" js="/views/pages/about.js"/>
</head>
<body >
    <!-- 内容主体区域 -->
    <!-- top -->
    <div th:replace="/top::qm-top"></div>
    <!-- container -->
    <div id="qmui-app"></div>
</body>
</html>

```

- pages路由标签

  > pages作为路由标签，他有三个属性。`name`,`to`,`js`

  - name（路由名）

    > 提供给跳转时索引路由使用。

  - to（路由路径）

    > 作为页面的地址

  - js（js文件路径）

    > 该页面的js路径，动态加载该js文件到单页中。



#### 说明

该`index.html`作为全局使用，引入的`js`如`jquery`、`bootstrap`等均为基础依赖，必须引入。

但作为其他页面的`js`请勿直接引入`index.html`中，避免`js`冲突。可在`pages`标签中设置`js`属性路径。

其中`top`导航使用`thymeleaf`的`th:replace`引入`top.html`。

### 导航模板

```html
<!DOCTYPE html>
<html lang="zh-CN" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Top</title>
</head>
<body>
<!-- top 模块 -->
<nav th:fragment="qm-top" class="navbar navbar-default">
    <!-- 导航栏 -->
    <div class="container-fluid">
        <!-- logo位置 -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#qm-menu-sx-style" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="javascript:;">浅梦工作室</a>
        </div>
        <!-- 菜单 -->
        <div class="collapse navbar-collapse" id="qm-menu-sx-style">
            <!-- 模块 -->
            <ul class="nav navbar-nav">
                <li class="qmui-li-click">
                    <a class="qmui-a-click" qmui-page="home">home</a>
                </li>
                <li class="qmui-li-click">
                    <a class="qmui-a-click" qmui-page="about">about</a>
                </li>
                <li class="dropdown">
                    <a href="javascript:;"
                       class="dropdown-toggle"
                       data-toggle="dropdown"
                       role="button"
                       aria-haspopup="true"
                       aria-expanded="false">菜单三(下拉菜单)
                        <!-- 下拉箭头图标 -->
                        <span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li class="qmui-li-click disabled"><a class="qmui-a-click" href="javascript:;">禁用选项</a></li>
                        <!-- 下划线 -->
                        <li role="separator" class="divider"></li>
                        <li class="qmui-li-click"><a href="javascript:;">菜单下的选项</a></li>
                    </ul>
                </li>
            </ul>
            <!-- 用户栏 -->
            <ul class="nav navbar-nav navbar-right">
                <li class="dropdown">
                    <a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" role="button"
                       aria-haspopup="true" aria-expanded="false">浅梦娱乐 <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li class="qmui-li-click"><a href="javascript:;">用户中心</a></li>
                        <li class="qmui-li-click"><a href="javascript:;">系统设置</a></li>
                        <!-- 下划线 -->
                        <li role="separator" class="divider"></li>
                        <li><a class="qmui-a-click" href="javascript:;">注销</a></li>
                    </ul>
                </li>
            </ul>
        </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
</nav>
</body>
</html>
```



#### 说明

在该页面中使用了`QMUI`自定义的`class`，如`qmui-li-click`，`qmui-li-click`。并使用了`qmui-page`属性修饰了`<a></a>`标签，作用于模块引入。

在`<li></li>`中加入`class="qmui-li-click"`，可使`li`有选中效果，其实是`js`中动态设置每个`li`的`active`。

在`<a></a>`中加入`class="qmui-a-click"`并设置属性`qmui-page="路由名"`,可使点击跳转相应的路由页。



## 使用说明

> 配置`index.html`

在开始使用时，首先配置`index.html`，可使用上方模板复制粘贴快速使用。

路由使用`<pages name="路由名" to="页面url地址" js="该页面的js文件” />`

路由作用的标签如下：

```html
<div id="qmui-app"></div>
```

> 创建页面

在该目录下创建页面和js文件

```
/views/src/pages/..
```

> 在需要跳转页面时，使用a标签进行设置。

```html
<a class="qmui-a-click" qmui-page="路由名">跳转</a>
```