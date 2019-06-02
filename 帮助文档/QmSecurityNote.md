# 注解

## @QmPass

### 描述

> 该注解适用于不需要登录，或需要登录但不需要授权的接口方法。

### 使用方法

> 在方法上标注了该注解后，则该方法不会经过登录认证和授权认证。

> 除此之外，注解中有一个属性`needLogin`。

> 该属性默认为`false`，表明不需要登录认证，忽略授权认证。

> 如果该属性设置为`true`，则表明需要登录认证，忽略授权认证。

### 示例

```Java
/**
 * 如果只需要登录，不需要授权，
 * 则可以在此设置QmPass的属性needLogin = true
 * @return
 */
@QmPass(needLogin = true)
@GetMapping("/hello")
public String hello() {
    return super.sendJSON(QmCode._1, "helloWorld");
}
```

> 上述标识该方法只会执行登录认证，而忽略授权认证。