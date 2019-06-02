## 接口控制层-封装

> 全面贯彻使用Restful风格

### 请求注解@QmBody

> 该注解为前端传递`body`的`json`数据时进行截获，并对类型进行自动转换。
>
> 注解和`RequestBody`的特性相似，而@`QmBody`则是默认参数是必须传递的。

现在我们的`Controller`是这样的。

```java
@PostMapping("/demo2")
public String demo2 (@QmBody String testParams) {
    return super.sendJSON(QmCode._1,testParams);
}
```

前端传递参数示例

```json
{
    "value":{
        "testParms":"内容"
    }
}
```

> 注解属性

```Java
/**
* 是否必须传递的参数
*/
boolean required() default true;

/**
* 当value的值或者参数名不匹配时，是否允许解析最外层属性到该对象
*/
boolean parseAllFields() default true;

/**
* 参数json中的别名key
*/
String value() default "";
```



### 继承QmController

> 一般我们返回给前端数据时，需要固定的`json`格式。而这些`QmController`已经帮助实现了。

> 该类提供了以下共同使用的属性和方法

- sendJSON(.....);
  - 该方法提供了三个重载方法，方便调用。

> 封装QmController类

```java
/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2018年11月24日 上午1:42:26
 * @Description 父类Controller, 编写Controller请继承该类。
 */
public class QmController {

    private static final Logger LOG = LoggerFactory.getLogger(QmController.class);

    /**
     * 接口回调方法
     *
     * @param code QmCode
     * @return
     */
    public String sendJSON(QmCode code) {
        Map<String, Object> responseMap = new HashMap<>(16);
        responseMap.put("code", code.getCode());
        responseMap.put("msg", QmCode.getMsg(code));
        responseMap.put("data", null);
        responseMap.put("responseTime", new Date());
        return this.parseJsonToResponse(responseMap);
    }

    /**
     * 接口回调方法
     *
     * @param code QmCode
     * @param data 传递数据
     * @return
     */
    public String sendJSON(QmCode code, Object data) {
        Map<String, Object> responseMap = new HashMap<>(16);
        responseMap.put("code", code.getCode());
        responseMap.put("msg", QmCode.getMsg(code));
        responseMap.put("data", data);
        responseMap.put("responseTime", new Date());
        return this.parseJsonToResponse(responseMap);
    }

    /**
     * 接口回调方法
     *
     * @param code QmCode
     * @param msg  自定义消息
     * @param data 传递数据
     * @return
     */
    public String sendJSON(QmCode code, String msg, Object data) {
        Map<String, Object> responseMap = new HashMap<>(16);
        responseMap.put("code", code.getCode());
        responseMap.put("msg", msg);
        responseMap.put("data", data);
        responseMap.put("responseTime", new Date());
        return this.parseJsonToResponse(responseMap);
    }

    /**
     * 解析请求json字符串
     *
     * @param value
     * @return
     */
    public String parseRequestJson(String value) {
        JSONObject jsonObject = JSONObject.parseObject(value);
        String json = jsonObject.getString(QmFrameConstants.REQUEST_DATA_KEY);
        if (QmFrameConstants.AES_START) {
            try {
                json = AesUtil.decryptAES(json);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return json;
    }

    /**
     * 转换json
     *
     * @param responseMap
     * @return
     */
    private String parseJsonToResponse(Map<String, Object> responseMap) {
        //SerializerFeature.WriteMapNullValue设置后,返回Bean时字段为空时默认返回null
        String value = JSONObject.toJSONString(responseMap, SerializerFeature.WriteMapNullValue);
        value = StringEscapeUtils.unescapeJava(value);
        try {
            if (QmFrameConstants.AES_START) {
                value = AesUtil.encryptAES(value);
                Map<String, String> resMap = new HashMap<>(16);
                resMap.put(QmFrameConstants.RESPONSE_DATA_KEY, value);
                return StringEscapeUtils.unescapeJava(JSONObject.toJSONString(resMap, SerializerFeature.WriteMapNullValue));
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.debug("加密失败");
        }
        Map<String, Map<String, Object>> resMap = new HashMap<>(16);
        resMap.put(QmFrameConstants.RESPONSE_DATA_KEY, responseMap);
        return JSONObject.toJSONString(resMap, SerializerFeature.WriteMapNullValue);
    }
}

```