
#  QmSecurity安全框架(V0.9.9)

## 描述

> `QmSecurity`是利用拦截器进行一系列安全校验的权限框架。

利用`AES`进行`token`多重加密生成，使用`md5`对`token`进行签名，建立自身拦截器并对请求进行登录认证和授权认证。如果不通过，则自动踢出请求并返回对应的`JSON`信息。

```
1、登录成功后生成token返回。
2、当该用户在次请求时，通过安全框架层层校验。
3、当该用户这次请求的token值已经过期，程序会重新签发token。
4、当该用户这次请求的token值已经过期，authorizationUserInfo返回null，则直接拦截。
5、当用户请求接口时遇到第3种情况时，会给Response的Header中设置token字段，该token映射的v为新的有效token。
6、注：当前端检测到Response的Header中有token字段时需替换旧的token内容。
```

### 内部协调

框架内部提供接口，由调用者实现授权过程，不接管登录认证操作，只辅助生成密度较高的`token`字符串，专注于校验机制，所见即所得，这是一个轻量级的`URL`权限框架。

## 用户对象

> **QmUserInfo**

### 描述

> 该对象相当于用户主体，使用`token`校验机制时用于签发登录`token`、保存登录用户预留信息。

### 属性

|      属性       |  类型  | 描述                                                         |
| :-------------: | :----: | ------------------------------------------------------------ |
|    identify     | String | 用户唯一识别，框架以此作为主要依仗进行整体校验基础。请保证必须唯一。 |
|      user       | String | 用户缓存对象，登录成功后可随时获取。                         |
|    signTime     |  Date  | 签名时间，token签发时间。内部用于记录登录时间使用。          |
| tokenExpireTime |  long  | token多久会失效，单位(秒)。如果为0，则表示永不失效。默认为60 * 30 |



## 主体接口(Qmbject)

> 主体对象相当于一个当前用户，该对象提供了对用户的一些行为操作的方法。

### 登录

> 方法

```java
String login(QmUserInfo qmUserInfo);
```

> 参数

|    参数    |    类型    |            说明             |
| :--------: | :--------: | :-------------------------: |
| qmUserInfo | QmUserInfo | 该对象用于`token`签名对象。 |

> 返回值

|  类型  |                        说明                        |
| :----: | :------------------------------------------------: |
| String | 返回一个`token`字符串，该字符串经过`AES`双签加密。 |

> 说明

该接口提供给登录方法调用，在生成`token`前请校验用户名和密码正确后，在进行调用，生成的`token`经过双签加密，提高安全性。

### 更新/获取用户权限信息

> 方法

```java
List<String> extractMatchingURI();
```

> 返回值

|     类型     |         说明          |
| :----------: | :-------------------: |
| List<String> | 返回一个`URI`权限集合 |

> 说明

动态获取当前用户角色权限



### 获取当前登录用户对象

> 方法

```java
QmUserInfo getUserInfo();
```

> 返回值

|    类型    |                说明                |
| :--------: | :--------------------------------: |
| QmUserInfo | 返回一个当前`QmUserInfo`用户对象。 |

> 说明

当头部带有`token`的字符串被校验成功并授权进入接口后，可以调用此接口获取当前用户的具体信息。