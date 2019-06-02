# AOP切入接口

## 介绍

在方法退出时，框架内部提供一个接口给予后续业务处理功能。在方法离开时你可以得到`JoinPoint`对象，方法返回的结果对象,响应时间等重要信息，便于后续业务拓展。

`JoinPoint`对象提供获取调用方法的类，调用方法的参数列表，方法名等内容。详情可自行百度。

## 使用

> 实现QmOutMethod接口

```java
public class MyOutMethod implements QmOutMethod {

    @Override
    public void responseOutHandling(JoinPoint jp, Object result, Long responseTime) {

    }
}
```

## 接口说明

```java
/**
     * 当请求方法执行完毕时执行该方法
     * @param jp JoinPoint 切入点对象
     * @param result 返回结果
     * @param responseTime 响应时间
     */
    void responseOutHandling(JoinPoint jp, Object result, Long responseTime);
```