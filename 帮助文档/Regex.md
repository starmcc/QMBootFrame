# 正则校验工具

## 使用步骤

### ***step.1:***

在`resource`目录下创建`verify.properties`文件。

> 下面是一套基础模板

```properties
# ※※※※※※※※※※※※※※※※※※校验正则配置文件※※※※※※※※※※※※※※※※※※
# 验证用户名 限制条件：首字符为英文字母,账号长度为5~20位,且不能出现特殊字符
username=^[A-Za-z][a-zA-Z0-9]{4,19}$
username.errorMsg=用户名格式错误,首字符为英文字母,账号长度为5~20位,且不能出现特殊字符
# 正则表达式：验证密码 限制条件：长度为6~20位
password=^\\w{6,20}$
password.errorMsg=密码长度必须为6~20位
# 正则表达式：验证手机号
mobile=^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$
mobile.errorMsg=手机号格式错误
# 验证邮箱
email=^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$
email.errorMsg=邮箱格式错误
# 验证汉字 3 ~ 10个汉字
chinese=^[\u4e00-\u9fa5]{3,10}$
chinese.errorMsg=验证汉字失败
# 验证身份证
cardId=(^\\d{18}$)|(^\\d{15}$)
cardId.errorMsg=身份证格式错误
# 验证URL
url=http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?
url.errorMsg=URL格式错误
# 验证IP地址
ip=(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)
ip.errorMsg=IP格式错误
```



### ***step.2:***

直接调用`QmVerifyUtil.isRegex`进行使用。

> 具体说明

```java
/**
* 校验内容是否匹配正则表达式
* 使用该方法时，请确保resource目录下存在verify.properties文件
* 根据verify.properties文件的节点检索正则表达式。
*
* @param node  properties的节点名称
* @param value 需要校验的值
* @return 返回布尔类型
*/
static boolean isRegex(String node, String value);
```



### ***Step.3:***

直接调用`QmVerifyUtil.getErrorMsg`进行使用。

> 具体说明

```java
**
* 获取对应节点的错误信息
* @param node
* @return
*/
static String getErrorMsg(String node);
```

