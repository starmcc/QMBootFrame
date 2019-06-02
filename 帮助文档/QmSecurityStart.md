# 开始使用

## **Step.1**

> 创建配置类`SecurityConfig`

```Java
/**
 * Copyright © 2018浅梦工作室}. All rights reserved.
 *
 * @author 浅梦
 * @date 2018/12/23 3:40
 * @Description 实现配置QmSecurity示例
 */
@Configuration
public class SecurityConfig implements WebMvcConfigurer {

    @Autowired
    private MyRealm myRealm;

    /**
     * 初始化QmSecurity安全框架
     * 重写WebMvcConfigurer的addInterceptors方法
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // setTokenSecret 设置token加密秘钥
        QmSecurityContent.setTokenSecret("shdioadnscoi21s90nbjio");
        // 设置请求头和响应头中携带token的字段名 默认为token
        QmSecurityContent.setHeaderTokenKeyName("token");
        // 设置加密次数 默认2次
        QmSecurityContent.setEncryptNumber(2);
        // 设置自定义的realm (这里需要注意的是,自定义的realm如果需要spring注入内容请交由Spring管理)
        QmSecurityContent.setRealm(myRealm);
        // 设置排除URI校验集合
        List<String> passUris = new ArrayList<>();
        passUris.add("/upload/**");
        passUris.add("/system/basic/**");
        QmSecurityContent.setPassUris(passUris);
        // 把框架添加到拦截器队列中,设置接管所有访问路径。
        InterceptorRegistration interceptor = registry.addInterceptor(new QmSecurityInterceptor());
        // 添加拦截路径
        interceptor.addPathPatterns("/**");
        // 设置拦截器的优先级
        interceptor.order(2);
        // 添加静态路径排除
        interceptor.excludePathPatterns("/views/**");
    }

}

```

## **Step.2**

> 创建自定义的`MyRealm`并实现`QmSecurityRealm`接口

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
        // 相关业务处理，并获取该用户的授权URI
        List<String> list = new ArrayList<>();
        list.add("/**");
        return list;
    }

    @Override
    public QmUserInfo authorizationUserInfo(QmUserInfo qmUserInfo, HttpServletRequest request, HttpServletResponse response) {
        // 对该用户进行认证处理，如果返回空，则表示拦截该用户。
        return qmUserInfo;
    }

    @Override
    public void noPassCallBack(int type, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.getWriter().print("安全检测不通过!");
    }
}

```



## **Step.3**

> 使用`@QmPass`注解标注登录接口

```java
/**
* 登录示例
* 增加该@QmPass注解表示该方法不校验登录和权限。
* @param username
* @param password
* @return
*/
@QmPass
@PostMapping("/login")
public String login(@QmBody String username, @QmBody String password) {
    User user = userService.login(username, password);
    if (user == null) {
        // 登录失败
        return super.sendJSON(QmCode._2, "登录失败！", null);
    }
    // 利用QmSecurityManager获取qmbject实例。
    Qmbject qmbject = QmSecurityManager.getQmbject();
    // 创建token签名信息QmUserInfo，并配置该对象的信息
    QmUserInfo qmUserInfo = new QmUserInfo();
    // identify为必须字段，用户唯一识别
    qmUserInfo.setIdentify(user.getUserName());
    // 设置用户缓存对象
    qmUserInfo.setUser(JSON.toJSONString(user));
    // 设置token失效时间 (秒) 10分钟token失效
    qmUserInfo.setTokenExpireTime(60 * 10);
    // 调用login方法，并设置他的过期时间，生成token
    String token = null;
    try {
        token = qmbject.login(qmUserInfo);
    } catch (QmSecurityQmUserInfoException e) {
        // qmUserInfo参数异常
        e.printStackTrace();
        return super.sendJSON(QmCode._3, "qmUserInfo参数异常!", null);
    } catch (QmSecurityCreateTokenException e) {
        // 签发token时发生了异常
        e.printStackTrace();
        return super.sendJSON(QmCode._3, "签名时发生了异常!", null);
    }
    // 将token返回给前端
    return super.sendJSON(QmCode._1, token);
}
```

## **Step.5**

> 访问其他接口时，如果请求`header`中没有`token`，`token`校验失败，授权失败，URI授权失败，都会直接被拦截，同时回调`MyRealm`的`noPassCallBack`。
