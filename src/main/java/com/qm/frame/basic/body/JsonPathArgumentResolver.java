package com.qm.frame.basic.body;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qm.frame.basic.controller.QmController;
import com.qm.frame.basic.exception.QmParamErrorException;
import com.qm.frame.basic.exception.QmParamNullException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @version V1.0 Description: 自定义解析json数据
 * @Title JsonPathArgumentResolver
 * @date 2018年9月10日
 */
public class JsonPathArgumentResolver extends QmController implements HandlerMethodArgumentResolver {

    private static final String JSONBODY_ATTRIBUTE = "JSON_REQUEST_BODY";

    /**
     * 设置支持的方法参数类型
     *
     * @param parameter 方法参数
     * @return 支持的类型
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 支持带@RequestJson注解的参数
        return parameter.hasParameterAnnotation(QmBody.class);
    }

    /**
     * 参数解析，利用fastjson 注意：非基本类型返回null会报空指针异常，要通过反射或者JSON工具类创建一个空对象
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String jsonBody = getRequestBody(webRequest);
        JSONObject jsonObject = JSON.parseObject(jsonBody);
        // 根据@qmBody注解value作为json解析的key
        QmBody qmBody = parameter.getParameterAnnotation(QmBody.class);
        // 注解的value是JSON的key
        String key = qmBody.key();
        Object value = null;
        // 如果@qmBody注解没有设置value，则取参数名FrameworkServlet作为json解析的key
        if (StringUtils.isNotEmpty(key)) {
            value = jsonObject.get(key);
        } else {
            // 注解为设置value则用参数名当做json的key
            key = parameter.getParameterName();
            value = jsonObject.get(key);
        }
        // 如果required = true 则不允许value == null
        if (value == null && qmBody.required()) {
            throw new QmParamNullException(String.format("required param %s is not present", key));
        } else if (value == null && qmBody.required() == false) {
            return value;
        }
        Class<?> parameterType = parameter.getParameterType();
        // 通过注解的value或者参数名解析，能拿到value进行解析
        //基本类型
        if (parameterType.isPrimitive()) {
            return parsePrimitive(parameterType.getName(), value);
        }
        // 基本类型包装类
        if (isPackDataTypes(parameterType)) {
            return parseBasicTypeWrapper(parameterType, value);
            // 字符串类型
        } else if (parameterType == String.class) {
            if (StringUtils.isEmpty(value.toString())) {
                throw new QmParamNullException(String.format("required param %s is not present", key));
            }
            return value.toString();
        }
        // 解析Date时间
        if (parameterType == Date.class) {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .parse(value.toString());
        }
        // 如果是list则解析list
        if (parameterType.isAssignableFrom(List.class)) {
            Type genericType = parameter.getGenericParameterType();
            if (genericType instanceof ParameterizedType) {
                try {
                    ParameterizedType pt = (ParameterizedType) genericType;
                    //得到泛型里的class类型对象
                    Class<?> genericClazz = (Class<?>) pt.getActualTypeArguments()[0];
                    return JSON.parseArray(value.toString(), genericClazz);
                } catch (Exception e) {
                    try {
                        return JSON.parseArray(value.toString());
                    } catch (Exception e1) {
                        if (qmBody.required()) {
                            throw new QmParamErrorException(String.format("required param %s is error", key));
                        }
                        return null;
                    }
                }
            }
            return JSON.parseArray(value.toString());
        }
        return JSON.parseObject(value.toString(), parameterType);
    }


    // 基本类型解析
    private Object parsePrimitive(String parameterTypeName, Object value) {
        final String booleanTypeName = "boolean";
        if (booleanTypeName.equals(parameterTypeName)) {
            return Boolean.valueOf(value.toString());
        }
        final String intTypeName = "int";
        if (intTypeName.equals(parameterTypeName)) {
            return Integer.valueOf(value.toString());
        }
        final String charTypeName = "char";
        if (charTypeName.equals(parameterTypeName)) {
            return value.toString().charAt(0);
        }
        final String shortTypeName = "short";
        if (shortTypeName.equals(parameterTypeName)) {
            return Short.valueOf(value.toString());
        }
        final String longTypeName = "long";
        if (longTypeName.equals(parameterTypeName)) {
            return Long.valueOf(value.toString());
        }
        final String floatTypeName = "float";
        if (floatTypeName.equals(parameterTypeName)) {
            return Float.valueOf(value.toString());
        }
        final String doubleTypeName = "double";
        if (doubleTypeName.equals(parameterTypeName)) {
            return Double.valueOf(value.toString());
        }
        final String byteTypeName = "byte";
        if (byteTypeName.equals(parameterTypeName)) {
            return Byte.valueOf(value.toString());
        }
        return null;
    }

    // 基本类型包装类型解析
    private Object parseBasicTypeWrapper(Class<?> parameterType, Object value) {
        if (Number.class.isAssignableFrom(parameterType)) {
            Number number = (Number) value;
            if (parameterType == Integer.class) {
                return number.intValue();
            } else if (parameterType == Short.class) {
                return number.shortValue();
            } else if (parameterType == Long.class) {
                return number.longValue();
            } else if (parameterType == Float.class) {
                return number.floatValue();
            } else if (parameterType == Double.class) {
                return number.doubleValue();
            } else if (parameterType == Byte.class) {
                return number.byteValue();
            }
        } else if (parameterType == Boolean.class) {
            return value.toString();
        } else if (parameterType == Character.class) {
            return value.toString().charAt(0);
        }
        return null;
    }

    /**
     * 是否为包装数据类型
     */
    private boolean isPackDataTypes(Class clazz) {
        Set<Class> classSet = new HashSet<>();
        classSet.add(Integer.class);
        classSet.add(Long.class);
        classSet.add(Short.class);
        classSet.add(Float.class);
        classSet.add(Double.class);
        classSet.add(Boolean.class);
        classSet.add(Byte.class);
        classSet.add(Character.class);
        return classSet.contains(clazz);
    }


    /**
     * 获取请求体JSON字符串
     */
    private String getRequestBody(NativeWebRequest webRequest) {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        // 有就直接获取
        String jsonBody = (String) webRequest.getAttribute(JSONBODY_ATTRIBUTE, NativeWebRequest.SCOPE_REQUEST);
        // 没有就从请求中读取
        if (jsonBody == null) {
            try {
                jsonBody = IOUtils.toString(servletRequest.getReader());
                webRequest.setAttribute(JSONBODY_ATTRIBUTE, jsonBody, NativeWebRequest.SCOPE_REQUEST);
            } catch (IOException e) {
                throw new QmParamErrorException(e);
            }
        }
        return jsonBody;
    }
}
