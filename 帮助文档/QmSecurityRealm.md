# 授权认证回调-Realm

## 介绍

框架进行安全认证时，需要创建自定义的`Realm`，在认证过程中回调`QmSecurityRealm`中的方法进行业务拓展和基础认证。



## 接口说明

### noPassCallBack

```Java
void noPassCallBack(int type, 
                    HttpServletRequest request, 
                    HttpServletResponse response) throws Exception;
```

当安全检测不通过时回调该接口，回调该接口最好的处理方式是处理相关业务并推送错误信息。

该接口提供的参数为最常见的`request`、`response`。

值得留意的是`type`参数，该参数回调的值决定回调的情况。

如下表所示：

| type值 |          说明           |
| :----: | :---------------------: |
|   1    |  检测不到token拒绝访问  |
|   2    | 非法token,token提取失败 |
|   3    |      授权验证拦截       |
|   4    |    权限不足,拒绝访问    |



### authorizationUserInfo

提供给调度者的检测用户是否合法的接口
当用户每次请求时进入安全监测时会调用该接口。
**注意：返回null表示本次检测不通过，框架自动进行拦截。**

```java
QmUserInfo authorizationUserInfo(QmUserInfo qmUserInfo, 
                                 HttpServletRequest request, 
                                 HttpServletResponse response);
```



### authorizationMatchingURI

提供给调度者的授权URI接口。

```java
List<String> authorizationMatchingURI(QmUserInfo qmUserInfo);
```



## 示例

```java
/**
 * Copyright © 2018浅梦工作室}. All rights reserved.
 *
 * @author 浅梦
 * @date 2018/12/23 2:20
 * @Description 自定义realm
 */
public class MyRealm implements QmSecurityRealm {

    @Override
    public List<String> authorizationMatchingURI(QmUserInfo qmUserInfo) {
        List<String> list = new ArrayList<>();
        list.add("/**");
        return list;
    }

    @Override
    public QmUserInfo authorizationUserInfo(QmUserInfo qmUserInfo, HttpServletRequest request, HttpServletResponse response) {
        return qmUserInfo;
    }

    @Override
    public void noPassCallBack(int type, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.getWriter().print("安全检测不通过!");
    }
}

```

